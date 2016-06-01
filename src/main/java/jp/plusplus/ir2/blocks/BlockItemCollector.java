package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityItemCollector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/18.
 */
public class BlockItemCollector extends BlockMachineBase {
    protected BlockItemCollector() {
        super("itemCollector", 2, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineItemCollector");
        setBlockName("itemCollector");
        minRSS=1;
        minFrequency=1;
        maxFrequency=256;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityItemCollector();
    }
}
