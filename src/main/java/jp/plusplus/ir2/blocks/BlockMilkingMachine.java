package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityMilkingMachine;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/07/04.
 */
public class BlockMilkingMachine extends BlockMachineBase {
    protected boolean isAdvanced;
    protected BlockMilkingMachine(boolean adv) {
        super("milking"+(adv?"Adv":""), 1, adv?"Obsidian":"Stone");
        setBlockName("milkingMachine"+(adv?"Adv":""));
        setBlockTextureName(IR2.MODID+":machineMilking");
        isAdvanced=adv;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityMilkingMachine(isAdvanced);
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
        sideIcon = par1IconRegister.registerIcon(IR2.MODID+":"+(isAdvanced?"machineFountain2Side":"machineMilkingSide"));
        carryIcon = par1IconRegister.registerIcon(IR2.MODID+":machineSide");
    }
}
