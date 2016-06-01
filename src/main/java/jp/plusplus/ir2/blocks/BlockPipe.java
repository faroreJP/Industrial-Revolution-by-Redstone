package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IHammerHandler;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/07.
 * パイプのブロック
 */
public class BlockPipe extends BlockCable implements IHammerHandler,IWrenchHandler {

    public String infoName;
    public int infoRow;
    protected Random rand;
    protected IIcon iconMarker;
    protected IIcon iconDisable;

    public int pipeType;

    public BlockPipe(short rss, short freq, String iName, int iRow) {
        super(rss, freq);
        infoName=iName;
        infoRow=iRow;
        rand=new Random();

        if(infoName.endsWith("Extractor"))      pipeType=1;
        if(infoName.endsWith("OneWay"))          pipeType=2;
        if(infoName.endsWith("Sorting"))         pipeType=3;
        if(infoName.endsWith("Fluid"))           pipeType=4;
        if(infoName.endsWith("FluidExtractor")) pipeType=5;
        if(infoName.endsWith("Void"))            pipeType=6;
        if(infoName.endsWith("FluidVoid"))      pipeType=7;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int a) {
        switch (pipeType){
            case 1:
                return new TileEntityPipeExtractor();
            case 2:
                return new TileEntityPipeOneWay();
            case 3:
                return new TileEntityPipeSorting();
            case 4:
                return new TileEntityPipeFluid();
            case 5:
                return new TileEntityPipeFluidExtractor();
            case 6:
                return new TileEntityPipeVoid();
            case 7:
                return new TileEntityPipeFluidVoid();
            default:
                return new TileEntityPipeBase();
        }
    }
    @Override
    public Block setBlockName(String n){
        setBlockTextureName(IR2.MODID+":"+n+(infoName.indexOf("Fluid")!=-1?"Fluid":""));
        return super.setBlockName(n);
    }
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
        par3List.add(new ItemStack(par1, 1, 0));
    }
    @Override
    public int damageDropped(int par1){
        return 0;
    }
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        blockIcon=par1IconRegister.registerIcon(getTextureName());
        if(infoName.endsWith("Extractor")){
            iconMarker=par1IconRegister.registerIcon(IR2.MODID+":pipeExtractor");
        }
        else if(infoName.endsWith("OneWay")){
            iconMarker=par1IconRegister.registerIcon(IR2.MODID+":pipeOneWay");
        }
        else if(infoName.endsWith("Sorting")){
            iconMarker=par1IconRegister.registerIcon(IR2.MODID+":pipeSorting");
        }
        else if(infoName.endsWith("Void")){
            iconMarker=par1IconRegister.registerIcon(IR2.MODID+":pipeVoid");
        }
        iconDisable=par1IconRegister.registerIcon(IR2.MODID+":pipeNot");
    }
    @Override
    public IIcon getIcon(int par1, int par2){
        return blockIcon;
    }
    @Override
    public IIcon getIconSide(int meta){
        if(meta==-1) return iconDisable;
        return meta==0?blockIcon:iconMarker;
    }

    public int onBlockPlaced(World world, int x, int y, int z, int meta, float p_onBlockPlaced_6_, float p_onBlockPlaced_7_, float p_onBlockPlaced_8_, int p_onBlockPlaced_9_) {
        int var10 = Facing.oppositeSide[meta];
        return var10;
    }


    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (pipeType == 0) return false;

        ItemStack item = par5EntityPlayer.getHeldItem();
        if (item != null) return false;

        if (par1World.isRemote) {
            return true;
        } else {

            if (pipeType==3 && !par5EntityPlayer.isSneaking()) {
                par5EntityPlayer.openGui(IR2.instance, -1, par1World, par2, par3, par4);
            } else {
                int[] next = {1, 2, 3, 4, 5, 0};
                par1World.setBlockMetadataWithNotify(par2, par3, par4, next[par1World.getBlockMetadata(par2, par3, par4) & 7], 2);
            }
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

        if(tileentity instanceof TileEntityPipeBase){
            TileEntityPipeBase inv=(TileEntityPipeBase)tileentity;
            Iterator<PacketItemStack> itemStacks=inv.getPackets();
            while(itemStacks.hasNext()){
                ItemStack itemstack = itemStacks.next().getItemStack();

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
        }

        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @Override
    public int getRenderType() {
        return IR2.renderPipeId;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
        float w=0.375f/2.0f;
        //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setBlockBounds(0.5f-w, 0.5f-w, 0.5f-w, 0.5f+w, 0.5f+w, 0.5f+w);
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
        return null;
    }
    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity){
        double d=0.0D;
        double w=0.375D/2.0D;

        setBlockBoundsBasedOnState(par1World, par2, par3, par4);

        //center
        AxisAlignedBB b=AxisAlignedBB.getBoundingBox((double) par2 + 0.5D - w, (double) par3 + 0.5D - w, (double) par4 + 0.5D - w, (double) par2 + 0.5D + w, (double) par3 + 0.5D + w, (double) par4 + 0.5D + w);
        if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
            par6List.add(b);
        }


        TileEntity t=par1World.getTileEntity(par2, par3, par4);
        if(t instanceof TileEntityCable){
            int con=((TileEntityCable)t).connectState;
            if((con&32)!=0){
                b=AxisAlignedBB.getBoundingBox(par2 + 0.5D + w, par3 + 0.5D - w, par4 + 0.5D - w, par2 + 1.0D, par3 + 0.5D + w, par4 + 0.5D + w);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
            if((con&8)!=0){
                b=AxisAlignedBB.getBoundingBox(par2+0.5D-w, par3+0.5D-w, par4+0.5D+w, par2+0.5D+w, par3+0.5D+w, par4+1.0D);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
            if((con&16)!=0){
                b=AxisAlignedBB.getBoundingBox(par2, par3 + 0.5D - w, par4 + 0.5D - w, par2 + 0.5D - w, par3 + 0.5D + w, par4 + 0.5D + w);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
            if((con&4)!=0){
                b=AxisAlignedBB.getBoundingBox(par2 + 0.5D - w, par3 + 0.5D - w, par4, par2 + 0.5D + w, par3 + 0.5D + w, par4 + 0.5D - w);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
            if((con&1)!=0){
                b=AxisAlignedBB.getBoundingBox(par2 + 0.5D - w, par3, par4 + 0.5D - w, par2 + 0.5D + w, par3 + 0.5D - w, par4 + 0.5D + w);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
            if((con&2)!=0){
                b=AxisAlignedBB.getBoundingBox(par2 + 0.5D - w, par3 + 0.5D + w, par4 + 0.5D - w, par2 + 0.5D + w, par3 + 1.0D, par4 + 0.5D + w);
                if(b!=null && par5AxisAlignedBB.intersectsWith(b)){
                    par6List.add(b);
                }
            }
        }
        //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
        double d=-0.0625D;
        double w=0.375/2.0;
        return AxisAlignedBB.getBoundingBox((double) par2 + 0.5 - w + d, (double) par3 + 0.5 - w + d, (double) par4 + 0.5 - w + d, (double) par2 + 0.5 + w - d, (double) par3 + 0.5 + w - d, (double) par4 + 0.5 + w - d);
    }

    @Override
    public boolean hammer(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        TileEntity entity=world.getTileEntity(x,y,z);
        if(entity instanceof TileEntityCable){
            ((TileEntityCable) entity).hammer(item, player, world, side);
        }
        entity.markDirty();
        world.markBlockForUpdate(x,y,z);
        world.notifyBlockChange(x,y,z,this);
        return true;
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        int[] next = {1, 2, 3, 4, 5, 0};
        world.setBlockMetadataWithNotify(x, y, z, next[world.getBlockMetadata(x, y, z) & 7], 2);
        return true;
    }
}
