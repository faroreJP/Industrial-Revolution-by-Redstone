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
public class TileEntityAlloySmelter extends TileEntityMachineBase implements ISidedInventory{
    private static final int[] slotsMaterial=new int[]{0,1};
    private static final int[] slotsProduct=new int[]{2};

    public ItemStack[] itemStacks=new ItemStack[3];

    public int materialAmount;
    public String guiName;

    public TileEntityAlloySmelter(){
    }
    public TileEntityAlloySmelter(int w, int m, String gName){
        this();
        workAmount=w;
        materialAmount=m;
        guiName=gName;
    }


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

        materialAmount  =   par1NBTTagCompound.getInteger("MaterialAmount");
        guiName          =   par1NBTTagCompound.getString("GuiName");
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

        par1NBTTagCompound.setInteger("MaterialAmount", materialAmount);
        par1NBTTagCompound.setString("GuiName", guiName);
    }


    @Override
    protected void work() {
        ItemStack ingot;
        if (itemStacks[0].getItem() == Items.redstone) {
            ingot = itemStacks[1];

            itemStacks[1].stackSize -= 1;
            if (itemStacks[1].stackSize <= 0) itemStacks[1] = null;
            itemStacks[0].stackSize -= materialAmount;
            if (itemStacks[0].stackSize <= 0) itemStacks[0] = null;
        } else {
            ingot = itemStacks[0];

            itemStacks[0].stackSize -= 1;
            if (itemStacks[0].stackSize <= 0) itemStacks[0] = null;
            itemStacks[1].stackSize -= materialAmount;
            if (itemStacks[1].stackSize <= 0) itemStacks[1] = null;
        }

        ItemStack product = Recipes.getAlloying(ingot);

        if (product != null) {
            if (itemStacks[2] == null) {
                itemStacks[2] = product.copy();
            } else if (itemStacks[2].isItemEqual(product)) {
                itemStacks[2].stackSize += product.stackSize;
            }
        }
    }
    @Override
    public boolean canWork() {
        if (itemStacks[0] == null || itemStacks[1] == null)             return false;
        if(itemStacks[0].stackSize<=0 || itemStacks[1].stackSize<=0)  return false;

        ItemStack ingot;
        ItemStack redstone;
        if(itemStacks[0].getItem()==Items.redstone){
            ingot=itemStacks[1];
            redstone=itemStacks[0];
        }
        else if(itemStacks[1].getItem()==Items.redstone){
            ingot=itemStacks[0];
            redstone=itemStacks[1];
        }
        else{
            return false;
        }

        if(redstone.stackSize<materialAmount)   return false;

        ItemStack result=Recipes.getAlloying(ingot);
        if(result==null)    return false;

        if(itemStacks[2]==null)                  return true;
        if(!itemStacks[2].isItemEqual(result))     return false;
        return itemStacks[2].stackSize+1<=itemStacks[2].getMaxStackSize();
    }
    @Override
    public String getGuiFileName(){
        return guiName+".png";
    }
    @Override
    public int getStringColor(){
        return guiName.endsWith("Adv")?0xffffff:0x404040;
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
        return I18n.format("tile."+guiName+".name");
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
        if(i!=0 && i!=1)    return false;
        return itemstack.getItem()==Items.redstone || Recipes.getAlloying(itemstack)!=null;
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
