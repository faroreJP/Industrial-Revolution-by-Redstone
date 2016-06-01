package jp.plusplus.ir2.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemRagnarok;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/06/05.
 */
public class MessageTheEnd  implements IMessage {
    public int dId;
    public boolean flag;

    public MessageTheEnd() {}
    public MessageTheEnd(int id, boolean flag) {
        this.flag=flag;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dId=buf.readInt();
        flag = buf.readByte()==1;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dId);
        buf.writeByte((byte)(this.flag?1:0));
    }

    public static class Handler  implements IMessageHandler<MessageTheEnd, IMessage> {
        @Override
        public IMessage onMessage(MessageTheEnd message, MessageContext ctx) {
            EntityPlayer ep = IR2.proxy.getClientPlayer();
            World w=IR2.proxy.getClientWorld();
            if(w!=null && w.provider.dimensionId==message.dId){
                ((ItemRagnarok) ItemCore.ragnarok).TheEnd(w, ep);
            }
            return null;
        }
    }
}
