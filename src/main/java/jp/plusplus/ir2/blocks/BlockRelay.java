package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import jp.plusplus.ir2.tileentities.TileEntityRelay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class BlockRelay extends BlockContainer implements IWrenchHandler {

    @SideOnly(Side.CLIENT)
    protected IIcon iconTop, iconFront, iconBack, iconBottom;
    protected Random rand=new Random();

    protected BlockRelay() {
        super(Material.rock);
        setBlockName("IR2relay");
        setHardness(3.5f);
        setResistance(10.0f);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par1) {
        return new TileEntityRelay();
    }
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random){
        TileEntity e1=par1World.getTileEntity(par2, par3, par4);
        if(e1==null){
            return;
        }
        if(e1 instanceof TileEntityAmplifier){
            TileEntityAmplifier tileEntity =  (TileEntityAmplifier)e1;
            if(tileEntity.getOutputRSS(-1)>0){
                double dx=par2+0.5D+par5Random.nextDouble()*0.2D;
                double dy=par3+0.5D+(par5Random.nextDouble()-0.5D)*2.0D*0.6D;
                double dz=par4+0.5D+par5Random.nextDouble()*0.2D;

                int side=2+par5Random.nextInt(4);
                if(side==2)	dz-=0.6D;
                if(side==3)	dz+=0.6D;
                if(side==4)	dx-=0.6D;
                if(side==5)	dx+=0.6D;
                par1World.spawnParticle("reddust", dx, dy, dz, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public IIcon getIcon(int par1, int par2){
        if(par1==1)			return iconTop;
        if(par1==0)			return iconBottom;
        if(par1==(par2&7))	return iconFront;
        if(par1==((par2&7)^1))	return iconBack;
        return blockIcon;
    }
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister){
        blockIcon			=	par1IconRegister.registerIcon(IR2.MODID+":casingStoneSide");
        iconFront			=	par1IconRegister.registerIcon(IR2.MODID+":relay");
        iconBack			=	par1IconRegister.registerIcon(IR2.MODID+":relay");
        iconTop			=	par1IconRegister.registerIcon(IR2.MODID+":casingStoneTop");
        iconBottom			=	par1IconRegister.registerIcon(IR2.MODID+":casingStoneBottom");
    }

    @Override
    public int getRenderType(){
        return IR2.renderAmplifierId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
        int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
       // TileEntityRelay e=(TileEntityRelay)par1World.getTileEntity(par2, par3, par4);

        if (l == 0){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }
        if (l == 1){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
        }
        if (l == 2){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
        }
        if (l == 3){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
        }

    }
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4){
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
    }

    private void setDefaultDirection(World par1World, int x, int y, int z) {
        if(!par1World.isRemote) {
            Block var5 = par1World.getBlock(x, y, z - 1);
            Block var6 = par1World.getBlock(x, y, z + 1);
            Block var7 = par1World.getBlock(x - 1, y, z);
            Block var8 = par1World.getBlock(x + 1, y, z);
            byte var9 = 3;
            if(var5.func_149730_j() && !var6.func_149730_j()) {
                var9 = 3;
            }

            if(var6.func_149730_j() && !var5.func_149730_j()) {
                var9 = 2;
            }

            if(var7.func_149730_j() && !var8.func_149730_j()) {
                var9 = 5;
            }

            if(var8.func_149730_j() && !var7.func_149730_j()) {
                var9 = 4;
            }

            par1World.setBlockMetadataWithNotify(x, y, z, var9, 2);
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int l = world.getBlockMetadata(x, y, z);

        boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z);
        if(flag1) {
            world.setBlockMetadataWithNotify(x,y,z,l|8,2);
        }
        else{
            world.setBlockMetadataWithNotify(x,y,z,l&7,2);
        }
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        world.func_147480_a(x, y, z, true);
        return true;
    }
}
