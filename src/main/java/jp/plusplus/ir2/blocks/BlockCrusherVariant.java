package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.tileentities.TileEntityCrusher;
import jp.plusplus.ir2.tileentities.TileEntityCrusherVariant;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/13.
 */
public class BlockCrusherVariant extends BlockCrusher {
    public BlockCrusherVariant() {
        super(true);
        setBlockName("crusherVar");
        maxFrequency=2048;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityCrusherVariant();
    }

}
