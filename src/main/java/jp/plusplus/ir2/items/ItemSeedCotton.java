package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

/**
 * Created by plusplus_F on 2015/05/31.
 */
public class ItemSeedCotton extends ItemSeeds{
    public ItemSeedCotton() {
        super(BlockCore.cropCotton, Blocks.farmland);
        setCreativeTab(IR2.tabIR2);
        setUnlocalizedName("IR2seedCotton");
        setTextureName(IR2.MODID+":seedCotton");
    }
}
