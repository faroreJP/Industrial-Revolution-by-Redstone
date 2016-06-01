package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.tileentities.TileEntityShop;
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
import shift.mceconomy2.MCEconomy2;
import shift.mceconomy2.ShopManager;
import shift.mceconomy2.api.MCEconomyAPI;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/18.
 */
public class BlockShop extends BlockContainer implements IWrenchHandler {
    protected IIcon topIcon;
    protected IIcon bottomIcon;
    protected IIcon sideIcon;
    public Random rand;

    protected BlockShop() {
        super(Material.rock);
        setBlockTextureName(IR2.MODID + ":machineShop");
        setBlockName("IR2shop");
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
        setResistance(17.5f);
        setHarvestLevel("pickaxe", 0);
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
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityShop();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        topIcon = par1IconRegister.registerIcon(IR2.MODID+":casingStoneTop");
        bottomIcon = par1IconRegister.registerIcon(IR2.MODID+":casingStoneBottom");
        sideIcon = par1IconRegister.registerIcon(IR2.MODID+":casingStoneSide");
        blockIcon=par1IconRegister.registerIcon(getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1)           return topIcon;
        else if(par1==0)        return bottomIcon;
        else if(par1==par2) return blockIcon;
        return sideIcon;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        if(ItemWrench.isWrench(par5EntityPlayer.getHeldItem())) return false;

        if (par1World.isRemote){
            return true;
        }
        else{
            TileEntity te=par1World.getTileEntity(par2, par3, par4);
            if(te instanceof TileEntityShop){
                if(((TileEntityShop) te).canWork()){
                    MCEconomyAPI.openShopGui(Recipes.GetShopId(), par5EntityPlayer, par1World, par2, par3, par4);
                }
            }
            return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(!world.isRemote){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityShop){
                ((TileEntityShop) te).updateConnectState();
                te.markDirty();
                world.markBlockForUpdate(x,y,z);
            }

            super.onNeighborBlockChange(world, x, y, z, block);
        }
    }

    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random){
        TileEntity e1=par1World.getTileEntity(par2, par3, par4);
        if(e1==null){
            return;
        }
        if(e1 instanceof TileEntityShop){
            TileEntityShop tileEntity =  (TileEntityShop)e1;
            if(tileEntity.canWork()){
                createParticle(par1World, par2, par3, par4);
            }
        }

    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
        //向きの情報を設定する。
        TileEntity te=w.getTileEntity(x, y, z);
        if(te!=null && te instanceof TileEntityShop) {
            TileEntityShop t = (TileEntityShop) te;

            //シングルの場合
            int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            if (l == 0) t.side = 2;
            else if (l == 1) t.side = 5;
            else if (l == 2) t.side = 3;
            else if (l == 3) t.side = 4;

            w.setBlockMetadataWithNotify(x, y, z, t.side, 2);
        }
    }
    protected void createParticle(World par1World, int par2, int par3, int par4){
        TileEntity t=par1World.getTileEntity(par2, par3, par4);
        if(t==null || !(t instanceof TileEntityMachineBase)){
            return;
        }

        int l = ((TileEntityMachineBase)t).side;
        float f = (float)par2 + 0.5F;
        float f1 = (float)par3 + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
        float f2 = (float)par4 + 0.5F;
        float f3 = 0.52F;
        float f4 = rand.nextFloat() * 0.6F - 0.3F;

        if (l == 4){
            par1World.spawnParticle("reddust", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 5){
            par1World.spawnParticle("reddust", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2){
            par1World.spawnParticle("reddust", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3){
            par1World.spawnParticle("reddust", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
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

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        world.func_147480_a(x,y,z,true);
        return true;
    }
}
