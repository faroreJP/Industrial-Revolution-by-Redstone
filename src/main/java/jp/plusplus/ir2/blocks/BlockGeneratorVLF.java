package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.tileentities.TileEntityGeneratorVLF;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class BlockGeneratorVLF extends BlockContainer implements IWrenchHandler {
    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
    @SideOnly(Side.CLIENT)
    private IIcon BottomIcon;
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;

    public BlockGeneratorVLF(){
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setBlockName("IR2generatorVLF");
        setHardness(3.5f);
        setResistance(20.0f);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 0);

        setBlockBounds(0,0,0,1,13.0f/16.0f,1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityGeneratorVLF();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        this.TopIcon = par1IconRegister.registerIcon(IR2.MODID+":generatorVLF");
        this.BottomIcon = par1IconRegister.registerIcon(IR2.MODID+":casingStoneBottom");
        this.SideIcon = par1IconRegister.registerIcon(IR2.MODID+":generatorStoneSide2");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1){
            return TopIcon;
        }
        else if(par1==0){
            return BottomIcon;
        }
        return SideIcon;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        par1World.notifyBlockChange(x-1,y,z,this);
        par1World.notifyBlockChange(x+1,y,z,this);
        par1World.notifyBlockChange(x,y-1,z,this);
        par1World.notifyBlockChange(x,y+1,z,this);
        par1World.notifyBlockChange(x,y,z-1,this);
        par1World.notifyBlockChange(x,y,z+1,this);

        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int l = world.getBlockMetadata(x, y, z);

        boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z);
        if (flag1) world.setBlockMetadataWithNotify(x, y, z, l | 8, 2);
        else world.setBlockMetadataWithNotify(x, y, z, l & 7, 2);

        if (l != world.getBlockMetadata(x, y, z)) {
            world.notifyBlockChange(x - 1, y, z, this);
            world.notifyBlockChange(x + 1, y, z, this);
            world.notifyBlockChange(x, y - 1, z, this);
            world.notifyBlockChange(x, y + 1, z, this);
            world.notifyBlockChange(x, y, z - 1, this);
            world.notifyBlockChange(x, y, z + 1, this);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlockChange(x - 1, y, z, this);
            world.notifyBlockChange(x + 1, y, z, this);
            world.notifyBlockChange(x, y - 1, z, this);
            world.notifyBlockChange(x, y + 1, z, this);
            world.notifyBlockChange(x, y, z - 1, this);
            world.notifyBlockChange(x, y, z + 1, this);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        setBlockBounds(0,0,0,1,13.0f/16.0f,1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        setBlockBoundsBasedOnState(par1World, x, y, z);

        //center
        AxisAlignedBB b = AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + (13.0 / 16.0), z + 1.0D);
        if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
            par6List.add(b);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        world.func_147480_a(x, y, z, true);
        return true;
    }
}
