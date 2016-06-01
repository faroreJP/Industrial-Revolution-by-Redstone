package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class TileEntityAmplifier extends TileEntity implements ISidedInventory, IConductor, IDirectional {
    private static final int[] slots=new int[]{0,1,2};

    public ItemStack[] itemStacks=new ItemStack[3];
    public short rss;
    public short frequency;
    public byte side;

    public TileEntityAmplifier(){
        side=-1;
    }
    public TileEntityAmplifier(byte side){
        this.side=side;
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

        rss             =   par1NBTTagCompound.getShort("RSS");
        frequency      =   par1NBTTagCompound.getShort("Frequency");
        side            =   par1NBTTagCompound.getByte("Side");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
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

        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", frequency);
        par1NBTTagCompound.setByte("Side", side);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound tag=new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void updateEntity(){
        int direction=worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7;
        rss=0;
        frequency=0;
        updateInput(direction);
        if(rss<=0){
            frequency=0;
        }
    }
    private void updateInput(int dir){
        int nX=0,nZ=0;
        switch(dir){
            case 2:
                nZ++;
                break;
            case 3:
                nZ--;
                break;
            case 4:
                nX++;
                break;
            case 5:
                nX--;
                break;
        }
        if(!worldObj.isAirBlock(xCoord+nX, yCoord, zCoord+nZ)){
            TileEntity e=worldObj.getTileEntity(xCoord + nX, yCoord, zCoord + nZ);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(dir);
                short cf=c.getOutputFrequency(dir);
                if(rss<cr)			rss=cr;
                if(frequency<cf)	frequency=cf;

                //check
                if(rss>0 || frequency>0){
                    short mr=getMaxRSS(), mf=getMaxFrequency();
                    if(rss>mr){
                        rss=mr;
                    }
                    if(frequency>mf){
                        frequency=0;
                    }
                }
            }
        }
    }

    public short getAmplifiedValue(){
        short par=0;
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]!=null){
                if(itemStacks[i].getItem()==ItemCore.coil){
                    par-=1;
                }
                else if(itemStacks[i].getItem() instanceof ItemBlock){
                    if(((ItemBlock)itemStacks[i].getItem()).field_150939_a == Blocks.redstone_block) {
                        par += 1;
                    }
                }
            }
        }
        return par;
    }
    public short calcRSS(){
        if(rss==0){
            return 0;
        }

        short par=getAmplifiedValue();
        short r=0;
        if(par==0) r=rss;
        else if(par>0) r=(short)(rss*(1.0+par/3.0));
        else if(par<0) r=(short)(rss/(1.0-par/3.0));
        return r>getMaxRSS()?getMaxRSS():r;
        //short par=(short)(rss*(1.0+getAmplifiedValue()/3.0));
        //return par>0?(par>getMaxRSS()?getMaxRSS():par):0;
    }

    public boolean checkElement(ItemStack item){
        if(item==null)  return false;
        if(item.getItem()==ItemCore.coil)   return true;
        if(!(item.getItem() instanceof ItemBlock))  return false;
        return ((ItemBlock)item.getItem()).field_150939_a == Blocks.redstone_block;
    }

    //------------------------------------------------------------------
    @Override
    public short getOutputRSS(int direction) {
        return (direction==-1 || direction==(side))?calcRSS():0;
    }
    @Override
    public short getOutputFrequency(int direction) {
        if(direction!=-1 && (direction)!=side) return 0;
        if(rss<getMaxRSS()/2) return frequency;
        return  rss+getAmplifiedValue()>getMaxRSS()?0:frequency;
    }
    @Override
    public short getMaxRSS() {
        return 2048;
    }
    @Override
    public short getMaxFrequency() {
        return 2048;
    }
    @Override
    public boolean canConnect(int side){
        return this.side==side || (this.side^1)==side;
    }

    //-----------------------------------------------------------------------------
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
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }

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
    }
    @Override
    public String getInventoryName() {
        return I18n.format("gui.amplifier");
    }
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
    @Override
    public int getInventoryStackLimit() {
        return 1;
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
        return checkElement(itemstack);
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slots;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && j!=(side^1) && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side && j!=(side^1);
    }

    //------------------------------------------------------
    @Override
    public byte getSide() {
        return side;
    }
    @Override
    public void setSide(byte s) {
        side=s;
    }
    @Override
    public boolean isFront(byte s) {
        return side==s;
    }
}
