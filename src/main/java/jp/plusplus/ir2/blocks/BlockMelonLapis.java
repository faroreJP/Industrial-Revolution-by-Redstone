package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockPotato;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/06/01.
 */
public class BlockMelonLapis extends BlockMelon {
    public BlockMelonLapis(){
        setBlockName("IR2blockMelonLapis");
        setBlockTextureName(IR2.MODID+":blockMelonLapis");
        setCreativeTab(IR2.tabIR2);
        setHardness(1.0F);
        setStepSound(soundTypeWood);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return ItemCore.melonLapis;
    }
}
