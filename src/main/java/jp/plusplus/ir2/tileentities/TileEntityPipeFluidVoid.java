package jp.plusplus.ir2.tileentities;

import java.util.Iterator;

/**
 * Created by plusplus_F on 2015/08/16.
 */
public class TileEntityPipeFluidVoid extends TileEntityPipeFluid {
    @Override
    public PipeType getPipeType() {
        return PipeType.VOID;
    }

    @Override
    protected int decideDestination(PacketFluidStack packet){
        return -1;
    }

    @Override
    protected void onDestinationNotFound(Iterator<PacketFluidStack> it){
        it.remove();
    }
}
