package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityCrucible;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/05/19.
 */
public class BlockCrucible extends BlockMachineBase {
    protected BlockCrucible() {
        super("crucible", 1, "Obsidian");
        setBlockTextureName(IR2.MODID + ":machineCrucible2Front");
        setBlockName("crucible");
        minRSS=1;
        minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityCrucible();
    }


    @Override
    public int getRenderType() {
        return IR2.renderFountainId;
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

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        super.registerBlockIcons(par1IconRegister);
        topIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Top");
        bottomIcon = par1IconRegister.registerIcon(IR2.MODID+":tankSmallTop");
        sideIcon = par1IconRegister.registerIcon(IR2.MODID+":machineFountain2Side");
        carryIcon = par1IconRegister.registerIcon(IR2.MODID+":machineSide");
    }
}
