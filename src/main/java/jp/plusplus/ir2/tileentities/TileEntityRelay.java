package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/02/15.
 */
public class TileEntityRelay extends TileEntity implements IConductor {
    short rss;
    short frequency;

    public void updateEntity(){
        short tmpRSS=0, tmpFreq=0;

        int dirId=(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
        ForgeDirection dir=ForgeDirection.getOrientation(dirId);
        TileEntity e;

        e=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
        if(e instanceof IConductor){
            IConductor c=(IConductor)e;
            tmpRSS=c.getOutputRSS(dirId^1);
            tmpFreq=c.getOutputFrequency(dirId^1);
        }

        dirId=(dirId^1);
        dir=dir.getOpposite();
        e=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
        if(e instanceof IConductor){
            IConductor c=(IConductor)e;
            short r=c.getOutputRSS(dirId^1);
            if(r>tmpRSS) {
                tmpRSS =r;
                tmpFreq = c.getOutputFrequency(dirId^1);
            }
        }

        rss=tmpRSS;
        frequency=tmpFreq;
    }

    private boolean isPowered(int meta){
        return (meta&8)!=0;
    }

    @Override
    public short getOutputRSS(int direction) {
        int meta=worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if(!isPowered(meta)) return 0;
        meta=(meta&7);
        if(meta!=direction && meta!=(direction^1))  return 0;
        return rss;
    }

    @Override
    public short getOutputFrequency(int direction) {
        int meta=worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if(!isPowered(meta)) return 0;
        meta=(meta&7);
        if(meta!=direction && meta!=(direction^1))  return 0;
        return frequency;
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
        int m=worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7;
        return m==side || m==(side^1);
    }
}
