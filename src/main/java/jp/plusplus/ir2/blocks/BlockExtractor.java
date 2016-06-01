package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityExtractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockExtractor extends BlockMachineBase {
    protected BlockExtractor() {
        super("extractor", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineExtractor");
        setBlockName("extractor");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityExtractor();
    }
}
