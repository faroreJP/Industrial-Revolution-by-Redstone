package jp.plusplus.ir2.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockStem;

/**
 * Created by plusplus_F on 2015/06/01.
 */
public class BlockCropMelonLapis extends BlockStem {
    protected BlockCropMelonLapis() {
        super(BlockCore.blockMelonLapis);
        setBlockName("IR2cropMelonLapis");
        setBlockTextureName("melon_stem");
        setHardness(0.0F);
        setStepSound(soundTypeWood);
    }
}
