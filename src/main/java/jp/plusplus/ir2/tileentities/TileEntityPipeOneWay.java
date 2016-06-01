package jp.plusplus.ir2.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class TileEntityPipeOneWay extends TileEntityPipeBase {

    /*
    @Override
    protected  void transportPacket2() {
        if (itemQueue.isEmpty()) return;
        PacketItemStack packet = itemQueue.peek();
        if(packet==null){
            itemQueue.poll();
            return;
        }

        int dir=(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
        ForgeDirection destDir=ForgeDirection.getOrientation(dir);
        TileEntity destEntity=worldObj.getTileEntity(xCoord+destDir.offsetX, yCoord+destDir.offsetY, zCoord+destDir.offsetZ);

        if(!canTransportTo(destEntity, packet.getItemStack(), dir)){
            SpillPacket(itemQueue.poll());
            return;
        }

        transportTo(destEntity, itemQueue.poll(), dir);
    }
    */

    @Override
    protected int decideDestination(PacketItemStack packet){
        int dir=(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
        ForgeDirection destDir=ForgeDirection.getOrientation(dir);
        TileEntity destEntity=worldObj.getTileEntity(xCoord+destDir.offsetX, yCoord+destDir.offsetY, zCoord+destDir.offsetZ);

        if(!canTransportTo(destEntity, packet.getItemStack(), dir)){
            return -1;
        }

        return dir;
    }

    @Override
    public PipeType getPipeType() {
        return PipeType.ONEWAY;
    }
}
