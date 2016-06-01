package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityMixer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/08/17.
 */
public class BlockMixer extends BlockMachineBase {
    protected BlockMixer() {
        super("mixer", 1, "Stone");
        setBlockTextureName(IR2.MODID + ":machineMixer");
        setBlockName("mixer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityMixer();
    }
}
