package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by plusplus_F on 2015/02/24.
 */
public class BlockNormal extends Block {

    protected BlockNormal() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
    }
}
