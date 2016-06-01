package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySyntheticFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/05/19.
 */
public class BlockSyntheticFurnace extends BlockMachineBase {
    protected BlockSyntheticFurnace() {
        super("syntheticFurnace", 2, "Obsidian");
        setBlockTextureName(IR2.MODID + ":machineSyntheticFurnace");
        setBlockName("syntheticFurnace");
        minRSS=512;
        minFrequency=512;
        maxRSS=2048;
        maxFrequency=2048;

        multiWidth=2;
        multiHeight=2;
        multiDepth=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySyntheticFurnace();
    }


    @Override
    public int getRenderType() {
        return IR2.renderSyntheticFurnaceId;
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
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }
}
