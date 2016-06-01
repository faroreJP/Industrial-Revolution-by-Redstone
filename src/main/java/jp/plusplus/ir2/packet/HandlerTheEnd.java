package jp.plusplus.ir2.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemRagnarok;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

/**
 * Created by plusplus_F on 2015/06/05.
 */
public class HandlerTheEnd  implements IMessageHandler<MessageTheEnd, IMessage> {
    @Override
    public IMessage onMessage(MessageTheEnd message, MessageContext ctx) {
        EntityPlayer ep = ctx.getServerHandler().playerEntity;
        World w = MinecraftServer.getServer().worldServers[0];
        ((ItemRagnarok) ItemCore.ragnarok).TheEnd(w, ep);
        return null;
    }
}