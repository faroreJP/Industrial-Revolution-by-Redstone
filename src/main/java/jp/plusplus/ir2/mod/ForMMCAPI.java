package jp.plusplus.ir2.mod;

import jp.MinecraftModderJapan.ModCooperationAPI.api.block.ICrop;
import jp.plusplus.ir2.tileentities.TileEntityHarvester;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/03/29.
 */
public class ForMMCAPI {
    public static boolean tryHarvest(TileEntityHarvester te, Block block, int x, int y, int z){
        if(block instanceof ICrop){
            ICrop ic=(ICrop)block;
        }
        return false;
    }
}
