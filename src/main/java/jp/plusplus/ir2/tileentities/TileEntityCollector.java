package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityCollector extends TileEntityMachineBase implements ISidedInventory {
    private static final int[] slotsItem =new int[]{0,1,2,3,4,5,6,7,8};
    public ItemStack[] itemStacks=new ItemStack[9];
    protected boolean isAdvanced;

    protected boolean enableDrawTicks;
    protected int drawTicks;
    protected int DRAW_TICKS_MAX=10; //敢えて子クラスで変えられるようにしてる

    public TileEntityCollector(boolean adv){
        isAdvanced=adv;
    }

    @Override
    public boolean hasCarryingSide(){ return true; }

    public boolean usesSlotTakeOnly(){ return true; }

    public boolean insertItem(ItemStack itemStack, ItemStack[] slots) {
        markDirty();
        ItemStack item = itemStack.copy();

        for (int i = 0; i < slots.length && item!=null; i++) {
            if (slots[i] == null) {
                slots[i] = item;
                item = null;
            } else if (slots[i].isItemEqual(item)) {
                if (slots[i].stackSize + item.stackSize <= slots[i].getMaxStackSize()) {
                    slots[i].stackSize += item.stackSize;
                    item = null;
                } else {
                    item.stackSize -= slots[i].getMaxStackSize() - slots[i].stackSize;
                    slots[i].stackSize = slots[i].getMaxStackSize();
                }
            }
        }

        if (item == null) {
            return true;
        } else {
            Random rand = new Random();
            float f = rand.nextFloat() * 0.8F + 0.1F;
            float f1 = rand.nextFloat() * 0.8F + 0.1F;
            float f2 = rand.nextFloat() * 0.8F + 0.1F;
            while (itemStack.stackSize > 0) {
                int k1 = rand.nextInt(21) + 10;

                if (k1 > itemStack.stackSize) {
                    k1 = itemStack.stackSize;
                }

                itemStack.stackSize -= k1;
                EntityItem entityitem = new EntityItem(worldObj, (double) ((float) xCoord + f), (double) ((float) yCoord + f1), (double) ((float) zCoord + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
                worldObj.spawnEntityInWorld(entityitem);
            }
            return false;
        }
    }

    @Override
    public boolean hasSlotWithEXP(){ return false; }

    @Override
    public void updateEntity(){
        super.updateEntity();

        //描画まわりの処理
        if(enableDrawTicks){
            drawTicks++;
            if(drawTicks>DRAW_TICKS_MAX){
                enableDrawTicks=false;
                drawTicks=0;
            }
        }
    }

    public void enableDrawTicks(){
        enableDrawTicks=true;
        drawTicks=0;

        markDirty();
    }
    public float getDrawTicksRate(){
        return enableDrawTicks?(float)drawTicks/DRAW_TICKS_MAX:0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        isAdvanced = par1NBTTagCompound.getBoolean("IsAdvanced");
        enableDrawTicks = par1NBTTagCompound.getBoolean("EnableDrawTicks");
        drawTicks = par1NBTTagCompound.getInteger("DrawTicks");

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

        par1NBTTagCompound.setBoolean("IsAdvanced", isAdvanced);
        par1NBTTagCompound.setBoolean("EnableDrawTicks", enableDrawTicks);
        par1NBTTagCompound.setInteger("DrawTicks", drawTicks);

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
    public String getGuiFileName(){
        return isAdvanced?"collectorAdv.png":"collector.png";
    }
    @Override
    public int getStringColor(){
        return isAdvanced?0xffffff:0x404040;
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
        return I18n.format("gui.collector");
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
        return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slotsItem;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return false;
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }
}
