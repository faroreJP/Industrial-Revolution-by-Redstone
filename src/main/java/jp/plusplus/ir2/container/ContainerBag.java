package jp.plusplus.ir2.container;

import jp.plusplus.ir2.inventory.InventoryBag;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/03/09.
 */
public class ContainerBag  extends Container {
    private InventoryBag inventory;

    public ContainerBag(InventoryPlayer inventoryPlayer) {
        inventory = new InventoryBag(inventoryPlayer);
        inventory.openInventory();

        for(int i=0;i<15;i++){
            this.addSlotToContainer(new Slot(inventory, i, 44 + (i%5) * 18, 25 + (i/5) * 18));
        }

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                ItemStack item=inventoryPlayer.getStackInSlot(j + i * 9 + 9);
                if(item!=null && item.getItem()==ItemCore.bag) {
                    this.addSlotToContainer(new SlotShowOnly(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 99 + i * 18));
                }
                else{
                    this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 99 + i * 18));
                }
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            ItemStack item=inventoryPlayer.getStackInSlot(i);
            if(item!=null && item.getItem()==ItemCore.bag) {
                this.addSlotToContainer(new SlotShowOnly(inventoryPlayer, i, 8 + i * 18, 157));
            }
            else{
                this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 157));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            }

            else if (slot.getStack() != null && slot.getStack().getItem() == ItemCore.bag) {
                return null;
            } else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.inventory.closeInventory();
    }
}