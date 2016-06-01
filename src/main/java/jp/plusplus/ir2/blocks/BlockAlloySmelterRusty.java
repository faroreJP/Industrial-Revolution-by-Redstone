package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityAlloySmelterRusty;
import net.minecraft.block.*;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/02.
 */
public class BlockAlloySmelterRusty extends BlockContainer{

    protected Random rand = new Random();

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;

    private boolean isActive;
    private static boolean keepInventory;

    public BlockAlloySmelterRusty(boolean isActive) {
        super(Material.rock);
        this.setBlockName("IR2alloySmelterRusty");
        this.setHardness(3.8f);
        this.setResistance(8.0f);
        this.setStepSound(Block.soundTypeMetal);
        this.isActive=isActive;
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityAlloySmelterRusty();
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        if (par1World.isRemote){
            return true;
        }
        else{
            par5EntityPlayer.openGui(IR2.instance, -1, par1World, par2, par3, par4);
            return true;
        }
    }


    @Override
    public int getRenderType(){
        return IR2.renderDirectionalId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2){
        return par1!=par2?iconSide:blockIcon;
    }
    @Override
    public Item getItemDropped(int p_getItemDropped_1_, Random p_getItemDropped_2_, int p_getItemDropped_3_) {
        return Item.getItemFromBlock(BlockCore.alloySmelterRustyIdle);
    }
    @Override
    @SideOnly(Side.CLIENT)
     public Item getItem(World p_getItem_1_, int p_getItem_2_, int p_getItem_3_, int p_getItem_4_) {
        return Item.getItemFromBlock(BlockCore.alloySmelterRustyIdle);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
        int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if(l == 0){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
        }
        if(l == 1){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
        }
        if(l == 2){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }
        if(l == 3){
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
        }
    }
    public void onBlockAdded(World par1World, int par2, int par3, int par4){
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
    }

    public void registerBlockIcons(IIconRegister par1IconRegister){
        iconSide=par1IconRegister.registerIcon(IR2.MODID+":brickRusty");
        blockIcon=par1IconRegister.registerIcon(IR2.MODID+":alloySmelterRusty"+(isActive?"On":"Off"));
    }
    public static void setKeepInventory(boolean flag){
        keepInventory=flag;
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
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        if(!keepInventory){
            TileEntityAlloySmelterRusty entity = (TileEntityAlloySmelterRusty) par1World.getTileEntity(x, y, z);

            if (entity != null){
                for (int j1 = 0; j1 < entity.getSizeInventory(); j1++){
                    ItemStack itemstack = entity.getStackInSlot(j1);

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
                            EntityItem entityitem = new EntityItem(par1World, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

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

                par1World.func_147453_f(x, y, z, block);
            }
        }
        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if(this.isActive) {
            int var6 = world.getBlockMetadata(x, y, z);
            float var7 = (float)x + 0.5F;
            float var8 = (float)y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)z + 0.5F;
            float var10 = 0.52F;
            float var11 = rand.nextFloat() * 0.6F - 0.3F;
            if(var6 == 4) {
                world.spawnParticle("smoke", (double) (var7 - var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (var7 - var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
            } else if(var6 == 5) {
                world.spawnParticle("smoke", (double) (var7 + var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (var7 + var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
            } else if(var6 == 2) {
                world.spawnParticle("smoke", (double) (var7 + var11), (double) var8, (double) (var9 - var10), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (var7 + var11), (double) var8, (double) (var9 - var10), 0.0D, 0.0D, 0.0D);
            } else if(var6 == 3) {
                world.spawnParticle("smoke", (double) (var7 + var11), (double) var8, (double) (var9 + var10), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (var7 + var11), (double) var8, (double) (var9 + var10), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
