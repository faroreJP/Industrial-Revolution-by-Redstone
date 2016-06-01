package jp.plusplus.ir2.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/08/16.
 * 虚無のパイプ ウリス
 */
public class TileEntityPipeVoid extends TileEntityPipeBase {

    @Override
    public PipeType getPipeType() {
        return PipeType.VOID;
    }

    @Override
    protected int decideDestination(PacketItemStack packet){
        return -1;
    }

    @Override
    protected void SpillPacket(PacketItemStack packet){
        if(packet!=null && packet.getDirection()==-1) return;
        super.SpillPacket(packet);
    }
}
