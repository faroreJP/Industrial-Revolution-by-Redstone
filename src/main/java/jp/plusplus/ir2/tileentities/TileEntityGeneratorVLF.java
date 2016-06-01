package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class TileEntityGeneratorVLF extends TileEntity implements IConductor {
    @Override
    public short getOutputRSS(int direction) {
        if(direction==0 || direction==1) return 0;
        return (short)((worldObj.getBlockMetadata(xCoord,yCoord,zCoord)&8)==0?16:0);
    }

    @Override
    public boolean canUpdate(){
        return false;
    }

    @Override
    public short getOutputFrequency(int direction) {
        if(direction==0 || direction==1) return 0;
        return (short)((worldObj.getBlockMetadata(xCoord,yCoord,zCoord)&8)==0?4:0);
    }
    @Override
    public short getMaxRSS() {
        return 16;
    }
    @Override
    public short getMaxFrequency() {
        return 4;
    }
    @Override
    public boolean canConnect(int side){
        if(side==0 || side==1)		return false;
        return true;
    }
}
