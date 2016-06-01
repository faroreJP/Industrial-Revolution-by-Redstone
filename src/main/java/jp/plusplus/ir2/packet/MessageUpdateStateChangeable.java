package jp.plusplus.ir2.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.IStateChangeable;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/29.
 */
public class MessageUpdateStateChangeable implements IMessage {
    public TileEntity te;

    public MessageUpdateStateChangeable(){}
    public MessageUpdateStateChangeable(TileEntity te){
        this.te=te;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int id=buf.readInt();
        int x=buf.readInt();
        int y=buf.readInt();
        int z=buf.readInt();

        World w=IR2.proxy.getClientWorld();
        if(w!=null && w.provider.dimensionId==id){
            TileEntity tmp=w.getTileEntity(x,y,z);
            if(tmp instanceof TileEntityCable) te=(TileEntityCable)tmp;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(te.getWorldObj().provider.dimensionId);
        buf.writeInt(te.xCoord);
        buf.writeInt(te.yCoord);
        buf.writeInt(te.zCoord);
    }

    public static class Handler implements IMessageHandler<MessageUpdateStateChangeable, IMessage>{
        @Override
        public IMessage onMessage(MessageUpdateStateChangeable message, MessageContext ctx) {
            if(message.te!=null){
                if(message.te instanceof IStateChangeable){
                    ((IStateChangeable) message.te).change();
                }
                message.te.getWorldObj().markBlockForUpdate(message.te.xCoord, message.te.yCoord, message.te.zCoord);
                message.te.markDirty();
            }
            return null;
        }
    }
}
