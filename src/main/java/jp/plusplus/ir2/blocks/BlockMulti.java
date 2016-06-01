package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.items.ItemWrench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/06/30.
 * マルチブロックの機械の補助
 * メタ値下位3bitは接続方向を示す
 */
public class BlockMulti extends BlockContainer {
    IIcon iconTop;
    IIcon iconBottom;

    protected BlockMulti() {
        super(Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random){
        /*
        TileEntity e1=par1World.getTileEntity(par2, par3, par4);
        if(e1==null){
            return;
        }
        if(e1 instanceof TileEntityMachineBase){
            TileEntityMachineBase tileEntity =  (TileEntityMachineBase)e1;
            if(tileEntity.lastCanWork && (tileEntity.isPowered || tileEntity.rss>=getMinRSS())){
                createParticle(par1World, par2, par3, par4);
            }
        }
        */
    }

    // 接続しているRS機械を得る
    public BlockMachineBase getMachine(World w, int x, int y, int z){
        int dir=(w.getBlockMetadata(x,y,z)&7);
        ForgeDirection fDir=ForgeDirection.getOrientation(dir);

        Block b=w.getBlock(x+fDir.offsetX, y+fDir.offsetY, z+fDir.offsetZ);
        if(b instanceof BlockMulti){
            return getMachine(w, x+fDir.offsetX, y+fDir.offsetY, z+fDir.offsetZ);
        }
        if(b instanceof BlockMachineBase){
            return (BlockMachineBase)b;
        }
        return null;
    }


    //接続してる機械のGUIを開く
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
            BlockMachineBase bmb=getMachine(par1World, par2, par3, par4);
            if(bmb!=null) {
                bmb.openGUI(par1World, par2, par3, par4, par5EntityPlayer);
            }

            return true;
        }
    }

    //接続してるブロック全てを破壊する
    @Override
    public void breakBlock(World w, int x, int y, int z, Block block, int par6){
        //if(par)

        /*
        int dir=(w.getBlockMetadata(x,y,z)&7);
        ForgeDirection fDir=ForgeDirection.getOrientation(dir);

        Block b=w.getBlock(x+fDir.offsetX, y+fDir.offsetY, z+fDir.offsetZ);
        if(b instanceof BlockMulti){
            breakBlock(w, x+fDir.offsetX, y+fDir.offsetY, z+fDir.offsetZ, block, par6);
        }
        if(b instanceof BlockMachineBase){
            ((BlockMachineBase) b).destroyMultiBlock(w, x+fDir.offsetX, y+fDir.offsetY, z+fDir.offsetZ);
        }
        */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        iconTop = par1IconRegister.registerIcon(IR2.MODID+":"+getTextureName()+"Top");
        iconBottom = par1IconRegister.registerIcon(IR2.MODID+":"+getTextureName()+"Bottom");
        blockIcon=par1IconRegister.registerIcon(IR2.MODID+":"+getTextureName()+"Front");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1){
            return iconTop;
        }
        else if(par1==0){
            return iconBottom;
        }
        return blockIcon;
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
    public int getRenderType() {
        return IR2.renderMultiId;
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
    public int getMobilityFlag() {
        return 2;
    }
}
