package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class TileEntityExtractor extends TileEntityMachineBase implements ISidedInventory {
    private static final int[] slotsMaterial=new int[]{0};
    private static final int[] slotsProduct=new int[]{1};

    public ItemStack[] itemStacks;

    public TileEntityExtractor(){
        itemStacks=new ItemStack[2];
        workAmount=128*20;
    }

    @Override
    public boolean hasSlotWithEXP(){ return false; }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<itemStacks.length){
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i=0;i<itemStacks.length;i++){
            if (itemStacks[i] != null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbt);
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    @Override
    protected void work() {
        int product = Recipes.getExtracting(itemStacks[0]);
        if (product > 0) {
            if (itemStacks[1] == null) {
                itemStacks[1] = new ItemStack(Items.redstone, product);
            } else if (itemStacks[1].getItem() == Items.redstone) {
                itemStacks[1].stackSize += product;
            }

            itemStacks[0].stackSize--;
            if (itemStacks[0].stackSize <= 0) {
                itemStacks[0] = null;
            }
        }
    }
    @Override
    public boolean canWork() {
        if (itemStacks[0] == null) return false;
        if (itemStacks[0].stackSize <= 0) return false;

        int result = Recipes.getExtracting(itemStacks[0]);
        if (result == 0) return false;

        if (itemStacks[1] == null) return true;
        if (itemStacks[1].getItem() != Items.redstone) return false;
        return itemStacks[1].stackSize + result <= itemStacks[1].getMaxStackSize();
    }
    @Override
    public String getGuiFileName(){
        return "processorAdv.png";
    }
    @Override
    public int getStringColor(){
        return 0xffffff;
    }


    //-----------------------ISidedInventory---------------------
    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }
    @Override
    public ItemStack getStackInSlot(int i) {
        return itemStacks[i];
    }
    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (itemStacks[i] != null){
            ItemStack itemstack;

            if (itemStacks[i].stackSize <= j){
                itemstack = itemStacks[i];
                itemStacks[i] = null;
                markDirty();
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }

                markDirty();
                return itemstack;
            }
        }
        else{
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (itemStacks[i] != null){
            ItemStack itemstack = itemStacks[i];
            itemStacks[i] = null;
            return itemstack;
        }
        else{
            return null;
        }
    }
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()){
            itemStack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }
    @Override
    public String getInventoryName() {
        return I18n.format("gui.extractor");
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
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double)xCoord+0.5D, (double)yCoord+0.5D, (double)zCoord+0.5D) <= 64.0D;
    }
    @Override
    public void openInventory() {

    }
    @Override
    public void closeInventory() {

    }
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i==0 && Recipes.getExtracting(itemstack)>0;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1==(int)sideCarrying?slotsProduct:slotsMaterial;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }
}
