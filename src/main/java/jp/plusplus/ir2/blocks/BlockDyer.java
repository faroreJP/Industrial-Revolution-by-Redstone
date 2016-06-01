package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityDyer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockDyer extends BlockMachineBase {
    protected BlockDyer() {
        super("dyer", 2, "Stone");
        setBlockTextureName(IR2.MODID+":machineDyer");
        setBlockName("dyer");
        minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDyer();
    }
}
