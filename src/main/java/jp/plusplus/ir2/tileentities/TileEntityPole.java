package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/06/29.
 * 電柱。
 * 基本的にはケーブルと同じだが、ケーブル以外とは上下方向でのみ接続し、またRSSの減衰が起きない。
 */
public class TileEntityPole extends TileEntity implements IConductor {
    protected short rss, freq;
    public boolean hasUpdated=false; //接続状態が更新されたか否か
    public byte connectState; //接続情報、下位8bitが六方向に対応


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
        //IR2.logger.info("con:"+connectState);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("ConnectState", connectState);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", freq);
        par1NBTTagCompound.setBoolean("HasUpdatedConnectState", hasUpdated);
    }

    public void markUpdateConnectingState(){
        hasUpdated=false;
    }

    @Override
    public void updateEntity(){
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
    }


    public void updateConnectState2() {
        int con = 0;

        //接続情報の更新
        for (int i = 0; i < 6; i++) {
            boolean flag = false;
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof TileEntityCable) {
                flag = true;
            } else if (entity instanceof IConductor) {
                //ケーブル以外に関しては、上下方向のみ接続する
                flag = i<2 && ((IConductor) entity).canConnect(dir.getOpposite().ordinal());
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


    //----------------------------- IConductor ------------------------------

    @Override
    public short getOutputRSS(int direction) {
        if(direction==0 || direction==1 || direction==-1) return rss;
        return (((connectState>>direction)&1)!=0)?rss:0;
    }
    @Override
    public short getOutputFrequency(int direction) {
        if(direction==0 || direction==1 || direction==-1) return freq;
        return (((connectState>>direction)&1)!=0)?freq:0;
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
        if(side==0 || side==1) return true;

        ForgeDirection dir=ForgeDirection.getOrientation(side);
        TileEntity te=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
        return te instanceof TileEntityCable;
    }
}
