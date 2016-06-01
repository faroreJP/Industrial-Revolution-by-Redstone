package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityCheeseMaker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/07/26.
 */
public class BlockCheeseMaker extends BlockMachineBase {
    protected BlockCheeseMaker() {
        super("cheeseMaker", 1, "Stone");
        setBlockTextureName(IR2.MODID + ":machineCheeseMaker");
        setBlockName("cheeseMaker");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityCheeseMaker();
    }
}
