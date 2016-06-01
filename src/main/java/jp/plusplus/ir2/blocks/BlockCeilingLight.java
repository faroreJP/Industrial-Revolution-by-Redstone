package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityForRender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/10.
 * 吊るし照明
 * meta: LRxx
 * L:点灯
 * R:向き(1で90度回転)
 * xx:0-4でモデルの種類
 */
public class BlockCeilingLight extends BlockContainer {
    public BlockCeilingLight() {
        super(Material.circuits);
        setCreativeTab(IR2.tabIR2);
        setBlockName("IR2ceilingLight");
        setBlockTextureName("glowstone");
        setHardness(0f);
        setResistance(15.0F);
    }

    @Override
    public int damageDropped(int par1){
        return par1&3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list){
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
    }

    @Override
    public int getRenderType() {
        return IR2.renderCeilingLightId;
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
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block != this) {
            return block.getLightValue(world, x, y, z);
        }

        int meta=world.getBlockMetadata(x,y,z);
        return (meta&8)!=0?15:0;
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase p, ItemStack it) {
        int l = MathHelper.floor_double((double) (p.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int meta=(it.getItemDamage()|8);

        if(l==1 || l==3){
            meta=(meta|4);
        }
        w.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        if(!par1World.isRemote){
            int m=par1World.getBlockMetadata(par2, par3, par4);
            if((m&8)!=0) m=(m&7);
            else m=(m|8);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, m, 2);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityForRender();
    }


    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta=world.getBlockMetadata(x,y,z)&3;
        float w = (4.0f / 16.0f) / 2.0f;

        if(meta<2) setBlockBounds(0.5f - w, 1-7*w, 0.5f - w, 0.5f + w, 1, 0.5f + w);
        else setBlockBounds(0.5f - 3*w, 1-w, 0.5f - 3*w, 0.5f + 3*w, 1, 0.5f + 3*w);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        int meta=par1World.getBlockMetadata(x,y,z)&3;
        double w = (4.0 / 16.0) / 2.0;
        if(meta<2) return AxisAlignedBB.getBoundingBox(x + 0.5 - w, y+1-7*w, z + 0.5 - w, x + 0.5 + w, y + 1, z + 0.5 + w);
        else return AxisAlignedBB.getBoundingBox(x + 0.5 - 3*w, y+1-w, z + 0.5 - 3*w, x + 0.5 + 3*w, y + 1, z + 0.5 + 3*w);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta=world.getBlockMetadata(x,y,z)&3;
        double d = -0.0625D;
        double w = (4.0 / 16.0) / 2.0;
        if(meta<2) return AxisAlignedBB.getBoundingBox(x + 0.5 - w + d, y+1-7*w, z + 0.5 - w + d, x + 0.5 + w - d, y + 1, z + 0.5 + w - d);
        else return AxisAlignedBB.getBoundingBox(x + 0.5 - 3*w + d, y+1-w, z + 0.5 - 3*w + d, x + 0.5 + 3*w - d, y + 1, z + 0.5 + 3*w - d);
    }
}
