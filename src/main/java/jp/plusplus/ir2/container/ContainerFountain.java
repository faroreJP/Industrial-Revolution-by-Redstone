package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityFountain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class ContainerFountain extends Container {
    public int lastProgress;
    public int lastAmount;

    private TileEntityFountain entity;
    private ISidedInventory inventory;

    public ContainerFountain(EntityPlayer player, TileEntityFountain tileEntity) {
        this.entity = tileEntity;
        this.inventory = entity;

        //inventory's inventory
        this.addSlotToContainer(new Slot(this.inventory, 0, 134, 17));
        this.addSlotToContainer(new SlotTakeOnly(this.inventory, 1, 134, 53));
        this.addSlotToContainer(new Slot(this.inventory, 2, 44, 35));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluidAmount());
        par1ICrafting.sendProgressBarUpdate(this, 1, entity.progress);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastAmount != entity.tank.getFluidAmount()) {
                icrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluidAmount());
            }
            if (lastProgress != entity.progress) {
                icrafting.sendProgressBarUpdate(this, 1, entity.progress & 0xffff);
                icrafting.sendProgressBarUpdate(this, 2, (entity.progress >> 16) & 0xffff);
            }
        }
        lastAmount=entity.tank.getFluidAmount();
        lastProgress = entity.progress;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return inventory.isUseableByPlayer(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) entity.tank.setAmount(par2);
        else if (par1 == 1) entity.progress = ((entity.progress & 0xffff0000) | par2);
        else if (par1 == 2) entity.progress = ((entity.progress & 0x0000ffff) | (par2 << 16));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2>=0 && par2<3){
                if (!this.mergeItemStack(stack, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if (inventory.isItemValidForSlot(0, stack)) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return null;
                    }
                } else if (inventory.isItemValidForSlot(2, stack)) {
                    if (!this.mergeItemStack(stack, 2, 3, false)) {
                        return null;
                    }
                } else if (par2 >= 3 && par2 < 30) {
                    if (!this.mergeItemStack(stack, 30, 39, false)) {
                        return null;
                    }
                } else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(stack, 3, 30, false)) {
                    return null;
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return itemStack;
    }
}
