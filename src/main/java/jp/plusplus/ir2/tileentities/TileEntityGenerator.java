package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class TileEntityGenerator extends TileEntity implements IConductor, ISidedInventory {
    private static final int[] slotsUnit =new int[]{0};

    public ItemStack[] itemStacks=new ItemStack[1];

    private short maxRSS;
    private short maxFrequency;
    private String guiName;

    public  short rss;
    public  short frequency;

    private boolean isStone;

    public TileEntityGenerator(short rss, short freq, String gName, String casingName){
        maxRSS=rss;
        maxFrequency=freq;
        guiName=gName;
        isStone=casingName.equals("Stone");
    }
    public TileEntityGenerator(){}


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

        //
        maxRSS			=	par1NBTTagCompound.getShort("MaxRSS");
        maxFrequency	=	par1NBTTagCompound.getShort("MaxFrequency");
        guiName	    =	par1NBTTagCompound.getString("GuiName");
        isStone        =    par1NBTTagCompound.getBoolean("IsStone");
        rss             =   par1NBTTagCompound.getShort("RSS");
        frequency      =   par1NBTTagCompound.getShort("Frequency");
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

        //
        par1NBTTagCompound.setShort("MaxRSS",		  maxRSS);
        par1NBTTagCompound.setShort("MaxFrequency", maxFrequency);
        par1NBTTagCompound.setString("GuiName", guiName);
        par1NBTTagCompound.setBoolean("IsStone", isStone);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", frequency);
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


    public String getGuiFileName(){
        return isStone?"generator.png":"generatorAdv.png";
    }
    public int getStringColor(){
        return isStone?0x404040:0xffffff;
    }

    @Override
    public short getOutputRSS(int direction) {
        if((worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&8)!=0 || direction==0 || direction==1) return 0;
        return rss;
    }
    @Override
    public short getOutputFrequency(int direction) {
        if((worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&8)!=0 || direction==0 || direction==1) return 0;
        return frequency;
    }
    @Override
    public short getMaxRSS() {
        return maxRSS;
    }
    @Override
    public short getMaxFrequency() {
        return maxFrequency;
    }
    @Override
    public boolean canConnect(int side){
        if(side==0 || side==1)		return false;
        return true;
    }


    public void updateEntity() {
        if (!worldObj.isRemote) {
            short pR=rss;
            short pF=frequency;

            //check RS input
            if((worldObj.getBlockMetadata(xCoord,yCoord,zCoord)&8)!=0){
                if(rss>0){
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
                rss=0;
                frequency=0;
            }
            else{
                if(itemStacks[0]==null){
                    rss=0;
                    frequency=0;
                }
                else {
                    Item item = itemStacks[0].getItem();
                    if (!(item instanceof ItemCrystalUnit)) {
                        rss = 0;
                        frequency = 0;
                    }
                    else {
                        ItemCrystalUnit unit = (ItemCrystalUnit) item;

                        //rss
                        if (unit.rss > getMaxRSS()) rss = getMaxRSS();
                        else rss = unit.rss;

                        //f
                        int d;
                        if (getMaxFrequency() > unit.frequency) {
                            d = 100;
                            frequency = unit.frequency;
                        } else {
                            d = (int) (100 * ((double) getMaxFrequency() / unit.frequency));
                            frequency = getMaxFrequency();
                        }

                        //damage
                        itemStacks[0] = unit.setDamageNBT(itemStacks[0], unit.getDamageNBT(itemStacks[0]) + d);
                    }
                }
            }

            if(pR!=rss || pF!=frequency){
                worldObj.notifyBlockOfNeighborChange(xCoord - 1, yCoord, zCoord, getBlockType());
                worldObj.notifyBlockOfNeighborChange(xCoord + 1, yCoord, zCoord, getBlockType());
                worldObj.notifyBlockOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
                worldObj.notifyBlockOfNeighborChange(xCoord, yCoord + 1, zCoord, getBlockType());
                worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord - 1, getBlockType());
                worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord + 1, getBlockType());

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
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
        return I18n.format(guiName);
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
        return itemstack.getItem() instanceof ItemCrystalUnit;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slotsUnit;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=1 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j==1;
    }
}
