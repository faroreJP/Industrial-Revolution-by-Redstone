package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCable;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import jp.plusplus.ir2.tileentities.TileEntityForRender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/10.
 */
public class BlockFan extends BlockContainer {
    public BlockFan() {
        super(Material.circuits);
        setCreativeTab(IR2.tabIR2);
        setBlockName("IR2fan");
        setBlockTextureName(IR2.MODID+":fan");
        setHardness(0);
        setResistance(15.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityForRender();
    }

    @Override
    public int getRenderType() {
        return IR2.renderFanId;
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

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            super.onNeighborBlockChange(world, x, y, z, block);
            if(!canPlaceBlockAt(world,x,y,z)){
                world.func_147480_a(x, y, z, true);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World w, int x, int y, int z) {
        return w.isBlockNormalCubeDefault(x,y+1,z,false);
    }
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
        }
    }

    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float w = (2.0f / 16.0f) / 2.0f;
        setBlockBounds(0.5f - w, 0, 0.5f - w, 0.5f + w, 1, 0.5f + w);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        double w = (2.0 / 16.0) / 2.0;
        return AxisAlignedBB.getBoundingBox(x + 0.5 - w, y+5/16.0, z + 0.5 - w, x + 0.5 + w, y + 1, z + 0.5 + w);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        double d = -0.0625D;
        double w = (2.0 / 16.0) / 2.0;
        return AxisAlignedBB.getBoundingBox(x + 0.5 - w + d, y+5/16.0, z + 0.5 - w + d, x + 0.5 + w - d, y + 1, z + 0.5 + w - d);
    }
}
