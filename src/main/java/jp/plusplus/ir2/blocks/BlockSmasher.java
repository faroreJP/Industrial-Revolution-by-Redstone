package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySmasher;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/16.
 */
public class BlockSmasher extends BlockMachineBase {
    protected BlockSmasher() {
        super("smasher", 1, "Stone");
        setBlockTextureName(IR2.MODID+":machineSmasher");
        setBlockName("smasher");
        minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySmasher();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x,y,z, block);

        TileEntity te=world.getTileEntity(x,y,z);
        if(te!=null) te.markDirty();
    }
}
