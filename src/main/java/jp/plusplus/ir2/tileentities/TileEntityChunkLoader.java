package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.ChunkLoaderManager;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by plusplus_F on 2015/08/10.
 */
public class TileEntityChunkLoader extends TileEntityMachineBase {
    public boolean lastState;

    public TileEntityChunkLoader(){
        workAmount=32*30;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        lastState=par1NBTTagCompound.getBoolean("LastState");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setBoolean("LastState", lastState);
    }



    @Override
    public void updateEntity(){
        super.updateEntity();

        if(worldObj.isRemote) return;

        //状態の変化に伴うチャンクローダの更新
        if(lastState!=isWorking()){
            lastState=isWorking();
            markDirty();

            if(lastState) ChunkLoaderManager.setChunkLoader(worldObj, xCoord, yCoord, zCoord);
            else ChunkLoaderManager.removeChunkLoader(worldObj, xCoord, yCoord, zCoord);
        }
    }
    @Override
    public boolean canWork(){
        return true;
    }
}
