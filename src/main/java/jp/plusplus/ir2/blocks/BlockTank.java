package jp.plusplus.ir2.blocks;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.tileentities.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/17.
 */
public class BlockTank extends BlockContainer {
    public Random rand=new Random();
    private IIcon topIcon;
    public int capacity;
    public String infoName;

    public BlockTank(int c, String iName) {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
        setResistance(17.5f);
        setHarvestLevel("pickaxe", 0);

        capacity=c;
        infoName=iName;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityTank(capacity);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
        TileEntityTank tile = (TileEntityTank) par1World.getTileEntity(par2, par3, par4);

        if (tile != null){
            FluidStack fluid = tile.tank.getFluid();

            if(itemstack==null){
                return openGui(par1World, par2, par3, par4, par5EntityPlayer);
            }
            else{
                FluidStack fluid2 =  FluidContainerRegistry.getFluidForFilledItem(itemstack);

                if(fluid2!=null && fluid2.getFluid()!=null){
                    int put = tile.fill(ForgeDirection.UNKNOWN, fluid2, false);

                    if (put == fluid2.amount){
                        tile.fill(ForgeDirection.UNKNOWN, fluid2, true);

                        ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(itemstack);
                        if (emptyContainer != null){
                            if (!par5EntityPlayer.inventory.addItemStackToInventory(emptyContainer.copy())){
                                par5EntityPlayer.entityDropItem(emptyContainer.copy(), 1);
                            }
                        }

                        if (!par5EntityPlayer.capabilities.isCreativeMode && itemstack.stackSize-- <= 0){
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                        }

                        tile.markDirty();
                        par5EntityPlayer.inventory.markDirty();
                        par1World.markBlockForUpdate(par2, par3, par4);

                        //par1World.playSoundAtEntity(par5EntityPlayer, "random.pop", 0.4F, 1.8F);

                        return true;
                    }
                }
                else{
                    if (fluid != null && fluid.getFluid() != null){
                        ItemStack get = FluidContainerRegistry.fillFluidContainer(fluid.copy(), itemstack);

                        if (get != null){
                            int cap=FluidContainerRegistry.getContainerCapacity(get);
                            if (fluid.amount < cap) return true;

                            IR2.logger.info("cap:"+cap);
                            tile.drain(ForgeDirection.UNKNOWN, cap, true);

                            if (!par5EntityPlayer.inventory.addItemStackToInventory(get.copy())){
                                par5EntityPlayer.entityDropItem(get.copy(), 1);
                            }

                            if (!par5EntityPlayer.capabilities.isCreativeMode && itemstack.stackSize-- <= 0){
                                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                            }

                            tile.markDirty();
                            par5EntityPlayer.inventory.markDirty();
                            par1World.markBlockForUpdate(par2, par3, par4);

                            par1World.playSoundAtEntity(par5EntityPlayer, "random.pop", 0.4F, 1.8F);
                            return true;
                        }
                        else{
                            return openGui(par1World, par2, par3, par4, par5EntityPlayer);
                        }

                    }
                    else{
                        return openGui(par1World, par2, par3, par4, par5EntityPlayer);
                    }
                }
            }
        }

        return true;
    }

    private boolean openGui(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
        if (!par1World.isRemote) par5EntityPlayer.openGui(IR2.instance, -1, par1World, x, y, z);
        return true;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        super.registerBlockIcons(par1IconRegister);
        topIcon = par1IconRegister.registerIcon(getTextureName()+"Top");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1 || par1==0){
            return topIcon;
        }
        return super.getIcon(par1, par2);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
        return true;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public int getRenderType(){
        return IR2.renderFluidTankId;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntity tileentity = par1World.getTileEntity(x, y, z);

        if(tileentity==null){
            super.breakBlock(par1World, x, y, z, block, par6);
            return;
        }

        if(tileentity instanceof ISidedInventory){
            ISidedInventory inv=(ISidedInventory)tileentity;

            for (int j1 = 0; j1 < inv.getSizeInventory(); j1++){
                ItemStack itemstack = inv.getStackInSlot(j1);

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
            //par1World.func_96440_m(x, y, z, block);
        }

        super.breakBlock(par1World, x, y, z, block, par6);
    }

}
