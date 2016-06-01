package jp.plusplus.ir2.inventory;

import jp.plusplus.ir2.items.ItemBag;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by plusplus_F on 2015/03/09.
 */
public class InventoryBag implements IInventory {
    private InventoryPlayer inventoryPlayer;
    private ItemStack currentItem;
    private ItemStack[] items;

    public InventoryBag(InventoryPlayer inventory) {
        inventoryPlayer = inventory;
        currentItem = inventoryPlayer.getCurrentItem();

        //InventorySize
        items = new ItemStack[15];
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack decrStackSize(int i, int size) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].stackSize <= size) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.items[i].splitStack(size);

                if (this.items[i].stackSize == 0) {
                    this.items[i] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];
            this.items[i] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack item) {
        this.items[i] = item;

        if (item != null && item.stackSize > this.getInventoryStackLimit()) {
            item.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "InventoryItem";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
        if (!currentItem.hasTagCompound()) {
            currentItem.setTagCompound(new NBTTagCompound());
            currentItem.getTagCompound().setTag("Items", new NBTTagList());
            currentItem.getTagCompound().setInteger("Color", 0xffffff);
        }
        else if(!currentItem.getTagCompound().hasKey("Items")){
            currentItem.getTagCompound().setTag("Items", new NBTTagList());
        }

        NBTTagList tags = (NBTTagList) currentItem.getTagCompound().getTag("Items");
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
            int slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < items.length) {
                items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public void closeInventory() {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setByte("Slot", (byte) i);
                items[i].writeToNBT(compound);
                tagList.appendTag(compound);
            }
        }
        ItemStack result = new ItemStack(currentItem.getItem(), 1, currentItem.getItemDamage());
        result.setTagCompound(new NBTTagCompound());;
        result.getTagCompound().setTag("Items", tagList);
        result.getTagCompound().setInteger("Color", currentItem.getTagCompound().getInteger("Color"));

        inventoryPlayer.mainInventory[inventoryPlayer.currentItem] = result;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack item) {
        return item.getItem() != ItemCore.bag;
    }
}