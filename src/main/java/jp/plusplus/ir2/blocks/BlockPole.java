package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import jp.plusplus.ir2.tileentities.TileEntityPole;
import net.minecraft.block.*;
import net.minecraft.block.BlockWood;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/06/29.
 */
public class BlockPole extends BlockContainer {
    public static final String[] NAMES={
            "Oak", "Spruce", "Birch", "Jungle", "Acacia",
            "BigOak", "Stone", "Cobble", "Brick", "NetherBrick",
            "StoneBrick", "SandStone", "RustyBrick"
    };
    protected IIcon[] icons=new IIcon[NAMES.length];
    protected IIcon icon;

    protected BlockPole() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setBlockName("IR2pole");
        setHardness(1.5f);
        setResistance(5.0f);

        for(int i=0;i<6;i++) setHarvestLevel("axe", 0, i);
        for(int i=6;i<NAMES.length;i++) setHarvestLevel("pickaxe", 0, i);
        //setBlockTextureName(IR2.MODID+":pole");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityPole();
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        //for(int i=0;i<NAMES.length;i++) par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public IIcon getIcon(int par1, int par2){
        if(par2==-1) return icon;
        if(par2<0 || par2>=icons.length) par2=0;
        return icons[par2];
    }
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister){
        //icon=par1IconRegister.registerIcon(IR2.MODID+":poleCable");
        icon=par1IconRegister.registerIcon(IR2.MODID+":amplifierBack");

        for(int i=0;i<BlockWood.field_150096_a.length;i++){
            icons[i]=par1IconRegister.registerIcon("planks_"+ BlockWood.field_150096_a[i]);
        }
        icons[6]=par1IconRegister.registerIcon("stone");
        icons[7]=par1IconRegister.registerIcon("cobblestone");
        icons[8]=par1IconRegister.registerIcon("brick");
        icons[9]=par1IconRegister.registerIcon("nether_brick");
        icons[10]=par1IconRegister.registerIcon("stonebrick");
        icons[11]=par1IconRegister.registerIcon("sandstone_normal");
        icons[12]=par1IconRegister.registerIcon(IR2.MODID+":brickRusty");
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    public int getRenderType() {
        return IR2.renderPoleId;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float w = (8.0f / 16.0f) / 2.0f;
        setBlockBounds(0.5f - w, 0, 0.5f - w, 0.5f + w, 1, 0.5f + w);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        double d = 0.0D;
        double w = (8.0/16.0) / 2.0D;

        setBlockBoundsBasedOnState(par1World, x, y, z);

        //center
        AxisAlignedBB b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y, z + 0.5D - w, x + 0.5D + w, y + 1, z + 0.5D + w);
        if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
            par6List.add(b);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        double d = -0.0625D;
        double w = (8.0 / 16.0) / 2.0;
        return AxisAlignedBB.getBoundingBox(x+0.5-w, y, z+0.5-w, x+0.5+w, y+1, z+0.5+w);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
        super.breakBlock(world, x, y, z, block, p_149749_6_);

        if (!world.isRemote) {
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityPole) {
            ((TileEntityPole) te).markUpdateConnectingState();
            world.markBlockForUpdate(x, y, z);
            te.markDirty();
        }

        if (!world.isRemote) {
            super.onNeighborBlockChange(world, x, y, z, block);
        }
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        }
    }

}
