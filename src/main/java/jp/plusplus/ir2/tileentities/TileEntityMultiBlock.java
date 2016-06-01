package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/07/01.
 */
public class TileEntityMultiBlock extends TileEntity implements IConductor,ISidedInventory, IStateChangeable {
    public short rss, freq;
    public boolean hasUpdated=false; //接続状態が更新されたか否か
    public byte connectState; //接続情報、下位8bitが六方向に対応
    public byte machineDirection=-1; //機械はどの向きにあんの？

    public int machineX, machineY, machineZ;
    public BlockMachineBase machineType;

    //大元のブロックを得る
    public Block getMachineCore() {
        if(machineType!=null) return machineType;

        int m=worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        //IR2.logger.info("meta:"+m);
        if((m&4)==0) return null;

        Block b=worldObj.getBlock(machineX, machineY, machineZ);
        if(!(b instanceof BlockMachineBase)){
            //IR2.logger.info("block is not machine");
            return null;
        }

        machineType=(BlockMachineBase)b;
        return machineType;
    }

    public void setMachineInfo(int dir){
        machineDirection=(byte)dir;
    }
    public void setMachineInfo(int x, int y, int z){
        machineX=x;
        machineY=y;
        machineZ=z;
        machineType=null;
        markDirty();
        worldObj.notifyBlockOfNeighborChange(xCoord,yCoord,zCoord,getBlockType());

        //IR2.logger.info("set machine info:"+x+","+y+","+z);
    }
    public void deleteMachineInfo(){
        machineX=machineY=machineZ=0;
        machineDirection=-1;
        machineType=null;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        connectState = par1NBTTagCompound.getByte("ConnectState");
        rss = par1NBTTagCompound.getShort("RSS");
        freq = par1NBTTagCompound.getShort("Frequency");
        hasUpdated=par1NBTTagCompound.getBoolean("HasUpdatedConnectState");
        machineDirection = par1NBTTagCompound.getByte("MachineDirection");

        machineX=par1NBTTagCompound.getInteger("MachineX");
        machineY=par1NBTTagCompound.getInteger("MachineY");
        machineZ=par1NBTTagCompound.getInteger("MachineZ");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("ConnectState", connectState);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", freq);
        par1NBTTagCompound.setBoolean("HasUpdatedConnectState", hasUpdated);
        par1NBTTagCompound.setByte("MachineDirection", machineDirection);

        par1NBTTagCompound.setInteger("MachineX", machineX);
        par1NBTTagCompound.setInteger("MachineY", machineY);
        par1NBTTagCompound.setInteger("MachineZ", machineZ);
    }

    public void markUpdateConnectingState(){
        hasUpdated=false;
    }

    @Override
    public boolean canUpdate(){
        return false;
    }

    @Override
    public void updateEntity(){
        /*
        rss = 0;
        freq = 0;

        if(!hasUpdated){
            updateConnectState2();
            //Block b=worldObj.getBlock(xCoord, yCoord, zCoord);
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            hasUpdated=true;
        }

        if (connectState != 0) {
            for (int i = 0; i < 6; i++) {
                if (((connectState >> i) & 0x1) != 0) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

                    if (entity instanceof IConductor) {
                        updateInput2((IConductor) entity, dir.ordinal());
                    }
                }
            }
        }
        */
    }


    public void updateConnectState2() {
        int con = 0;

        //接続情報の更新
        for (int i = 0; i < 6; i++) {
            boolean flag = false;
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof IConductor) {
                flag=true;
            }

            if (flag) {
                con = (con | (1 << i));
            }
        }
        connectState = (byte) con;
    }

    protected void updateInput2(IConductor entity, int dir) {
        short r = entity.getOutputRSS(dir ^ 1);
        short f = entity.getOutputFrequency(dir ^ 1);

        if (rss < r) {
            rss = r;
            freq = f;
        } else if (rss == r && freq > f) {
            freq = f;
        }
    }


    @Override
    public boolean change() {
        byte pc=connectState;
        short pr=rss, pf=freq;

        rss=freq=0;

        int con = 0;
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof IConductor && canConnect(i)) {
                con = (con | (1 << i));
                updateInput2((IConductor)entity, i);
            }
        }
        connectState = (byte) con;

        return connectState!=pc || pr!=rss || pf!=freq;
    }


    //----------------------------- IConductor ------------------------------

    @Override
    public short getOutputRSS(int direction) {
        return 0;
        /*
        if(direction==0 || direction==1 || direction==-1) return rss;
        return (((connectState>>direction)&1)!=0)?rss:0;
        */
    }
    @Override
    public short getOutputFrequency(int direction) {
        return 0;
        /*
        if(direction==0 || direction==1 || direction==-1) return freq;
        return (((connectState>>direction)&1)!=0)?freq:0;
        */
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
    public boolean canConnect(int side) {
        TileEntity te=getMachineTe();
        if(te instanceof TileEntityMachineBase) return ((TileEntityMachineBase) te).canConnect(side);
        else return true;
    }

    //--------------------------- ISidedInventory -------------------------
    public TileEntity getMachineTe(){
        Block b=getMachineCore();
        if(b==null) return null;
        return worldObj.getTileEntity(machineX, machineY, machineZ);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        TileEntity te=getMachineTe();
        if(te instanceof ISidedInventory){
            return ((ISidedInventory)te).getAccessibleSlotsFromSide(i);
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        TileEntity te=getMachineTe();
        if(te instanceof ISidedInventory){
            return ((ISidedInventory)te).canInsertItem(p_102007_1_, p_102007_2_, p_102007_3_);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        TileEntity te=getMachineTe();
        if(te instanceof ISidedInventory){
            return ((ISidedInventory)te).canExtractItem(p_102008_1_, p_102008_2_, p_102008_3_);
        }
        return false;
    }

    @Override
    public int getSizeInventory() {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).getStackInSlot(p_70301_1_);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).decrStackSize(p_70298_1_, p_70298_2_);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).getStackInSlotOnClosing(p_70304_1_);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            ((IInventory)te).setInventorySlotContents(p_70299_1_, p_70299_2_);
        }
    }

    @Override
    public String getInventoryName() {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).getInventoryName();
        }
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).isUseableByPlayer(p_70300_1_);
        }
        return false;
    }

    @Override
    public void openInventory() {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            ((IInventory)te).openInventory();
        }
    }

    @Override
    public void closeInventory() {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            ((IInventory)te).closeInventory();
        }
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        TileEntity te=getMachineTe();
        if(te instanceof IInventory){
            return ((IInventory)te).isItemValidForSlot(p_94041_1_, p_94041_2_);
        }
        return false;
    }
}
