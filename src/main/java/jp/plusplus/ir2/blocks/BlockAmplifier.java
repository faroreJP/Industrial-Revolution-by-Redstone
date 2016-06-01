package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class BlockAmplifier extends BlockContainer implements IWrenchHandler {

    @SideOnly(Side.CLIENT)
    protected IIcon iconTop, iconFront, iconBack, iconBottom;
    protected Random rand=new Random();

    protected BlockAmplifier() {
        super(Material.rock);
        setBlockName("amplifier");
        setHardness(3.5f);
        setResistance(10.0f);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public Block setBlockName(String name){
        return super.setBlockName("IR2"+name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par1) {
        return new TileEntityAmplifier();
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        if(par1World.getTileEntity(par2, par3, par4)==null){
            return false;
        }

        if(ItemWrench.isWrench(par5EntityPlayer.getHeldItem())) return false;

        if (par1World.isRemote){
            return true;
        }
        else{
            par5EntityPlayer.openGui(IR2.instance, -1, par1World, par2, par3, par4);
            return true;
        }
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
        iconFront			=	par1IconRegister.registerIcon(IR2.MODID+":amplifierFront");
        iconBack			=	par1IconRegister.registerIcon(IR2.MODID+":amplifierBack");
        iconTop				=	par1IconRegister.registerIcon(IR2.MODID+":casingStoneTop");
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
        TileEntityAmplifier e=(TileEntityAmplifier)par1World.getTileEntity(par2, par3, par4);

        if (l == 0){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
            e.side=3;
        }
        if (l == 1){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
            e.side=4;
        }
        if (l == 2){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
            e.side=2;
        }
        if (l == 3){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
            e.side=5;
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

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6){
        ISidedInventory tileentity = (ISidedInventory) par1World.getTileEntity(par2, par3, par4);

        if (tileentity != null){
            for (int j1 = 0; j1 < tileentity.getSizeInventory(); j1++){
                ItemStack itemstack = tileentity.getStackInSlot(j1);

                if (itemstack != null){
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0){
                        int k1 = this.rand.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize){
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        par1World.spawnEntityInWorld(entityitem);
                    }
                }
            }

            //par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        world.func_147480_a(x, y, z, true);
        return true;
    }
}
