package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySpinning;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockSpinning extends BlockMachineBase {
    protected  boolean isStone;

    protected BlockSpinning(String iName, int iRow, String cName) {
        super(iName, iRow, cName);
        setBlockName(iName);

        isStone=cName.equals("Stone");
        multiWidth=2;
        multiHeight=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return isStone?(new TileEntitySpinning(32*10, 4, "spinning")):(new TileEntitySpinning(128*10, 2, "spinningAdv"));
    }

    @Override
    public int getRenderType(){
        return IR2.renderMachineSpinningId;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

}
