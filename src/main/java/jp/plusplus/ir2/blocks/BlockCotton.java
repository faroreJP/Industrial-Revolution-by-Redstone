package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by plusplus_F on 2015/05/31.
 */
public class BlockCotton extends BlockCrops {
    public BlockCotton(){
        //setCreativeTab(IR2.tabIR2);
        setBlockTextureName(IR2.MODID+":cotton");
        setBlockName("IR2cropCotton");
    }

    protected Item func_149866_i() {
        return ItemCore.seedCotton;
    }
    protected Item func_149865_P() {
        return ItemCore.cottonRaw;
    }

}
