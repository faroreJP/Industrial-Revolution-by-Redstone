package jp.plusplus.ir2.container;

import jp.plusplus.ir2.tileentities.TileEntityPipeSorting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerPipeSorting extends Container {

    private TileEntityPipeSorting entity;

    public ContainerPipeSorting(EntityPlayer player, TileEntityPipeSorting tileEntity) {
        this.entity = tileEntity;


        //inventory's inventory
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                this.addSlotToContainer(new Slot(entity, j + i * 7, 44 + j * 18, 20 + i * 18));
            }
        }

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 144 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 202));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        //par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        /*
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.progress) {
                icrafting.sendProgressBarUpdate(this, 0, entity.progress);
            }
        }
        lastProgress = entity.progress;
        */
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2>=0 && par2<6*7) {
                if (!this.mergeItemStack(stack, 6*7, 6*7+4*9, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if(par2 >= 6*7 && par2 < 6*7+3*9) {
                    if (!this.mergeItemStack(stack, 0, 6*7, false)) {
                        return null;
                    }
                } else if (par2 >= 6*7+3*9 && par2 < 6*7+4*9 && !this.mergeItemStack(stack, 6*7+3*9, 6*7+4*9, false)) {
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
