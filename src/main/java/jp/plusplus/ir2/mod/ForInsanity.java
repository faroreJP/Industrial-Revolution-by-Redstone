package jp.plusplus.ir2.mod;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IHarvestable;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.tileentities.TileEntityHarvester;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class ForInsanity {
    public static void setup() {
        Registry.RegisterItemSanity(new ItemStack(ItemCore.canSoilent), 2, -6);
    }

    public static boolean tryHarvestForInsanity(TileEntityHarvester teh, int x, int y, int z){
        World w=teh.getWorldObj();
        Block block=w.getBlock(x, y, z);
        if(block instanceof IHarvestable){
            IHarvestable ih=(IHarvestable)block;

            if(ih.canHarvest(w,x,y,z)){
                ArrayList<ItemStack> get=ih.harvest(w, x,y,z,w.rand);
                for(ItemStack itemStack : get){
                    teh.insertItem(itemStack, teh.itemStacks);
                }
                return true;
            }
        }
        return false;
    }
}
