package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityMiner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/16.
 */
public class BlockMiner extends BlockMachineBase {
    protected BlockMiner() {
        super("miner", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineMiner");
        setBlockName("miner");
        //minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityMiner();
    }
}
