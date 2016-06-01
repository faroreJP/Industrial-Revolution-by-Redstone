package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

/**
 * Created by plusplus_F on 2015/05/31.
 */
public class ItemPotatoQuartz extends ItemSeeds{
    public ItemPotatoQuartz() {
        super(BlockCore.cropPotatoQuartz, Blocks.farmland);
        setCreativeTab(IR2.tabIR2);
        setUnlocalizedName("IR2potatoQuartz");
        setTextureName(IR2.MODID+":potatoQuartz");
    }
}
