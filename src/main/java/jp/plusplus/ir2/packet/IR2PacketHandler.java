package jp.plusplus.ir2.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import jp.plusplus.ir2.IR2;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class IR2PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(IR2.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessageTheEnd.Handler.class, MessageTheEnd.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessageUpdateStateChangeable.Handler.class, MessageUpdateStateChangeable.class, 1, Side.CLIENT);
    }
}
