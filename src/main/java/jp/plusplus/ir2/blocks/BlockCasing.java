package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.packet.IR2PacketHandler;
import jp.plusplus.ir2.packet.MessageUpdateStateChangeable;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 * 筐体。2.1.0以前はただの無機能ブロックであり、中間素材だったが、
 * 3.0.0リニューアルでマルチブロックの筐体として重要な役割を担うことになる。
 * metadata ... 8:非描画フラグ 4:接続済みフラグ 2:下が筐体フラグ
 */
public class BlockCasing extends BlockContainer implements IWrenchHandler {
    private IIcon iconTop;
    private IIcon iconBottom;
    private IIcon iconConnect;

    protected BlockCasing() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        iconTop = par1IconRegister.registerIcon(IR2.MODID+":casing"+getTextureName()+"Top");
        iconBottom = par1IconRegister.registerIcon(IR2.MODID+":casing"+getTextureName()+"Bottom");
        blockIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+getTextureName()+"Side");
        iconConnect = par1IconRegister.registerIcon(IR2.MODID+":casing"+getTextureName()+"Front");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if((par2&2)!=0) return iconTop;
        if (par1 == 1) return iconTop;
        if (par1 == 0) return iconBottom;
        if ((par2&4)!=0) return iconConnect;
        return blockIcon;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        //非接続状態なら何もしない
        if((par1World.getBlockMetadata(par2, par3, par4)&4)==0) return false;

        TileEntity entity=par1World.getTileEntity(par2, par3, par4);
        if(!(entity instanceof TileEntityMultiBlock)) return false;
        if(ItemWrench.isWrench(par5EntityPlayer.getHeldItem())) return false;
        if(par1World.isRemote) return true;

        //機械のGUIを開こうとする
        TileEntityMultiBlock te=(TileEntityMultiBlock)entity;
        te.getMachineCore();

        BlockMachineBase b=te.machineType;
        if(b==null) return true;

        b.openGUI(par1World, te.machineX, te.machineY, te.machineZ, par5EntityPlayer);
        return true;
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        //繋がってたら処理は接続ブロックに任せる
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMultiBlock) {
            TileEntityMultiBlock tm = (TileEntityMultiBlock) te;
            tm.getMachineCore();
            if (tm.machineType != null) {
                return tm.machineType.wrench(item, player, world, tm.machineX, tm.machineY, tm.machineZ, side);
            }
        }

        //なんかアレならここで破壊される
        if(!world.isRemote) world.func_147480_a(x, y, z, true);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMultiBlock();
    }

    @Override
    public int getMobilityFlag() {
        return 2;
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
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int side) {
        if((w.getBlockMetadata(x,y,z)&8)!=0) return true;
        return true;
    }

    @Override
    public void onBlockPreDestroy(World w, int x, int y, int z, int meta) {
        if (!w.isRemote) {
            //接続している場合、その接続先の機械をぶちこわす
            TileEntity entity = w.getTileEntity(x, y, z);
            if (entity instanceof TileEntityMultiBlock) {
                TileEntityMultiBlock te = (TileEntityMultiBlock) entity;
                te.getMachineCore();

                BlockMachineBase b = te.machineType;
                if (b != null) {
                    w.func_147480_a(te.machineX, te.machineY, te.machineZ, true);
                }
            }
        }
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block block, int m) {
        if (!w.isRemote) {
            w.notifyBlockOfNeighborChange(x, y + 1, z, this);
            w.notifyBlockOfNeighborChange(x, y - 1, z, this);
            w.notifyBlockOfNeighborChange(x + 1, y, z, this);
            w.notifyBlockOfNeighborChange(x - 1, y, z, this);
            w.notifyBlockOfNeighborChange(x, y, z + 1, this);
            w.notifyBlockOfNeighborChange(x, y, z - 1, this);
        }

        super.breakBlock(w, x, y, z, block, m);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMultiBlock && ((TileEntityMultiBlock) te).change() && !world.isRemote) {
            IR2PacketHandler.INSTANCE.sendToDimension(new MessageUpdateStateChangeable(te), world.provider.dimensionId);
            world.markBlockForUpdate(x, y, z);
            te.markDirty();

            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }

        if (!world.isRemote) {
            int m = world.getBlockMetadata(x, y, z);
            if (world.getBlock(x, y - 1, z) == this) {
                world.setBlockMetadataWithNotify(x, y, z, m | 2, 2);
            } else {
                world.setBlockMetadataWithNotify(x, y, z, m & 13, 2);
            }

            super.onNeighborBlockChange(world, x, y, z, block);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlockOfNeighborChange(x, y, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
        setBlockBounds(0,0,0,1,1,1);
    }
}
