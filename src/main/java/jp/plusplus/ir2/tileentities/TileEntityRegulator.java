package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/02/15.
 */
public class TileEntityRegulator extends TileEntity implements IConductor {
    short rss;
    short frequency;

    public void updateEntity(){
        short tmpRSS=0, tmpFreq=4096;

        for(int i=0;i<6;i++){
            ForgeDirection dir=ForgeDirection.getOrientation(i);
            TileEntity e=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
            if(e instanceof IConductor){
                IConductor c=(IConductor)e;
                short r=c.getOutputRSS(i^1);
                if(r>tmpRSS) {
                    tmpRSS =r;
                    tmpFreq=c.getOutputFrequency(i^1);
                }
                else if(r==tmpRSS){
                    short f=c.getOutputFrequency(i^1);
                    if(f<tmpFreq) {
                        tmpFreq = f;
                    }
                }
            }
        }

        if(rss!=tmpRSS || frequency!=tmpFreq){
            int r=0;
            if(tmpFreq<=16) r=tmpRSS;
            else if(tmpFreq<=32) r=tmpRSS/2;
            else if(tmpFreq<=64) r=tmpRSS/4;
            else if(tmpFreq<=128) r=tmpRSS/8;
            else r=tmpRSS/16;

            if(r>15) r=15;
            markDirty();
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, r, 3);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        rss=tmpRSS;
        frequency=tmpFreq;
    }

    @Override
    public short getOutputRSS(int direction) {
        return 0;
        //return rss;
    }

    @Override
    public short getOutputFrequency(int direction) {
        return 0;
        //return rss>0?frequency:0;
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
        return true;
    }
}
