package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySpinningProt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockSpinningProt extends BlockMachineBase {
    protected BlockSpinningProt() {
        super("spinningProt", 2, "Stone");
        setBlockTextureName(IR2.MODID+":machineSpinningProt");
        setBlockName("spinningProt");

        minRSS=1;
        minFrequency=1;
        maxRSS=16;
        maxFrequency=8;

        multiWidth=2;
        multiHeight=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySpinningProt();
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
