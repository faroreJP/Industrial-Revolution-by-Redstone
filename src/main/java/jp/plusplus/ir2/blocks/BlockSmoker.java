package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySmoker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/07/26.
 */
public class BlockSmoker extends BlockMachineBase {
    protected BlockSmoker() {
        super("smoker", 2, "Stone");
        setBlockTextureName(IR2.MODID + ":machineSmoker");
        setBlockName("smoker");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySmoker();
    }
}
