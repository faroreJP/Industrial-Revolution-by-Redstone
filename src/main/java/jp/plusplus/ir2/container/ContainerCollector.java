package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityCollector;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerCollector extends Container {

    public int lastProgress;

    private TileEntityMachineBase entity;
    private ISidedInventory inventory;

    public ContainerCollector(EntityPlayer player, TileEntityCollector tileEntity) {
        this.entity = tileEntity;
        this.inventory = tileEntity;

        //inventory's inventory
        if(!tileEntity.usesSlotTakeOnly()) {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                this.addSlotToContainer(new Slot(inventory, i, 8 + 18 * i, 38));
            }
        }
        else if(entity.hasSlotWithEXP()){
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                this.addSlotToContainer(new SlotWithEXP(player, inventory, i, 8 + 18 * i, 38));
            }
        }
        else {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                this.addSlotToContainer(new SlotTakeOnly(inventory, i, 8 + 18 * i, 38));
            }
        }

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
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 1, (entity.progress>>16)&0xffff);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.progress) {
                icrafting.sendProgressBarUpdate(this, 0, entity.progress&0xffff);
                icrafting.sendProgressBarUpdate(this, 1, (entity.progress>>16)&0xffff);
            }
        }
        lastProgress = entity.progress;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return inventory.isUseableByPlayer(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) entity.progress = ((entity.progress&0xffff0000)|par2);
        if(par1==1) entity.progress = ((entity.progress&0xffff)|(par2<<16));

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if (par2>=0 && par2<9) {
                if (!this.mergeItemStack(stack, 9, 36, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if(inventory.isItemValidForSlot(0, itemStack)){
                    if (!this.mergeItemStack(stack, 0, 9, false)){
                        return null;
                    }
                }
                else if (par2 >= 9 && par2 < 36) {
                    if (!this.mergeItemStack(stack, 36, 45, false)) {
                        return null;
                    }
                } else if (par2 >= 36 && par2 < 45 && !this.mergeItemStack(stack, 9, 36, false)) {
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
