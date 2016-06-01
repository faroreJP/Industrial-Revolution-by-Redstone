package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.tileentities.TileEntityGenerator;
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
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockGenerator extends BlockContainer implements IWrenchHandler {
    protected short rss;
    protected short frequency;

    private String casingName;
    private IIcon sideIcon;
    private IIcon bottomIcon;

    protected Random rand;

    protected BlockGenerator(short rss, short freq, String cName) {
        super(Material.rock);
        this.rss=rss;
        frequency=freq;
        casingName=cName;

        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);

        if(cName.equals("Obsidian")){
            setHardness(50.0f);
            setResistance(6000.0f);
            setHarvestLevel("pickaxe", 3);
        }
        else{
            setHardness(3.5f);
            setResistance(17.5f);
            setHarvestLevel("pickaxe", 0);
        }

        rand=new Random();
    }

    @Override
    public Block setBlockName(String name){
        return super.setBlockName("IR2"+name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityGenerator(rss, frequency, getUnlocalizedName()+".name", casingName);
    }

    public short getOutputRSS(){
        return rss;
    }
    public short getOutputFrequency(){
        return frequency;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        sideIcon = par1IconRegister.registerIcon(IR2.MODID+":generator"+casingName+"Side2");
        bottomIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Bottom");
        super.registerBlockIcons(par1IconRegister);
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1){
            return blockIcon;
        }
        else if(par1==0){
            return bottomIcon;
        }
        return sideIcon;
    }

    @Override
    public int getRenderType() {
        return IR2.renderPGId;
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
        return par5!=0;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9){
        if(par1World.getTileEntity(par2, par3, par4)==null){
            return false;
        }

        if(ItemWrench.isWrench(player.getHeldItem())) return false;

        ItemStack itemStack=player.getCurrentEquippedItem();
        if(itemStack!=null && itemStack.getItem() instanceof ItemCrystalUnit){
            TileEntityGenerator te=(TileEntityGenerator)par1World.getTileEntity(par2, par3, par4);
            if(te.itemStacks[0]==null){
                if(!par1World.isRemote){
                    te.itemStacks[0]=itemStack;
                    te.markDirty();
                    player.setCurrentItemOrArmor(0, null);
                    player.inventory.markDirty();
                }
                return true;
            }
        }

        if (par1World.isRemote){
            return true;
        }
        else{
            player.openGui(IR2.instance, -1, par1World, par2, par3, par4);
            return true;
        }
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
        if (flag1){
            world.setBlockMetadataWithNotify(x, y, z, l | 8, 2);
        }
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
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        if(!world.isRemote) world.func_147480_a(x, y, z, true);
        return true;
    }
}
