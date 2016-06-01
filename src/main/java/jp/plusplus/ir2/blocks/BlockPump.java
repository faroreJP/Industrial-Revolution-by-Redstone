package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityPump;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/08/17.
 */
public class BlockPump extends BlockMachineBase {
    protected BlockPump() {
        super("pump", 1, "Stone");
        setBlockTextureName(IR2.MODID+":machinePump");
        setBlockName("pump");

        multiHeight=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityPump();
    }

    @Override
    public int getRenderType(){
        return IR2.renderPumpId;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x,y,z, block);

        TileEntity te=world.getTileEntity(x,y,z);
        if(te!=null) te.markDirty();
    }
}
