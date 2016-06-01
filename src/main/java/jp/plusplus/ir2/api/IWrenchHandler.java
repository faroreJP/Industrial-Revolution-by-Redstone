package jp.plusplus.ir2.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/07/01.
 * レンチ使用可能なブロックに実装すべきインターフェース
 */
public interface IWrenchHandler {
    /**
     * EntityPlayerがレンチで右クリックした際に呼ばれる
     * World.isRemote関係なく呼ばれるので、このメソッド内で鯖蔵の処理を分ける必要がある
     * また、レンチ側で特にブロックの更新などはかけないので、必要に応じてこのメソッド内で
     * World.markBlockForUpdateとかしてやる必要がある
     * @param item
     * @param player
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @return
     */
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side);
}
