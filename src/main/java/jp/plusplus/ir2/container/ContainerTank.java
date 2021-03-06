package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class ContainerTank extends Container {
    public int lastProgress;

    private TileEntityTank entity;
    private ISidedInventory inventory;

    public ContainerTank(EntityPlayer player, TileEntityTank tileEntity) {
        this.entity = tileEntity;
        this.inventory = entity;

        //inventory's inventory
        this.addSlotToContainer(new Slot(this.inventory, 0, 80, 17));
        this.addSlotToContainer(new SlotTakeOnly(this.inventory, 1, 80, 51));

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
        if(entity.tank.getFluid()!=null) par1ICrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluid().getFluidID());
        else par1ICrafting.sendProgressBarUpdate(this, 0, -1);
        par1ICrafting.sendProgressBarUpdate(this, 1, entity.tank.getFluidAmount()&0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 2, (entity.tank.getFluidAmount()>>16)&0xffff);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.tank.getFluidAmount()) {
                if(entity.tank.getFluid()!=null) icrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluid().getFluidID());
                else icrafting.sendProgressBarUpdate(this, 0, -1);
                icrafting.sendProgressBarUpdate(this, 1, entity.tank.getFluidAmount()&0xffff);
                icrafting.sendProgressBarUpdate(this, 2, (entity.tank.getFluidAmount()>>16)&0xffff);
            }
        }
        lastProgress = entity.tank.getFluidAmount();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0){
            if(par2!=-1) entity.tank.setFluid(new FluidStack(FluidRegistry.getFluid(par2), 1000));
            else entity.tank.setFluid(null);
        }
        else if(par1==1) {
            entity.tank.setAmount((entity.tank.getFluidAmount()&0xffff0000)|par2);
        }
        else if(par1==2) {
            entity.tank.setAmount((entity.tank.getFluidAmount()&0x0000ffff)|(par2<<16));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return inventory.isUseableByPlayer(entityPlayer);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if (par2 == 1) {
                if (!this.mergeItemStack(stack, 2, 38, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            } else if (par2 == 0) {
                if (!this.mergeItemStack(stack, 2, 38, false)) {
                    return null;
                }
            } else {
                if (inventory.isItemValidForSlot(0, stack)) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return null;
                    }
                } else if (par2 >= 2 && par2 < 29) {
                    if (!this.mergeItemStack(stack, 29, 38, false)) {
                        return null;
                    }
                } else if (par2 >= 29 && par2 < 38 && !this.mergeItemStack(stack, 2, 29, false)) {
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
