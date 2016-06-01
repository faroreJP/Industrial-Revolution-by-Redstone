package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCable;
import jp.plusplus.ir2.packet.IR2PacketHandler;
import jp.plusplus.ir2.packet.MessageUpdateStateChangeable;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class BlockCable extends BlockContainer {
    public String textureFileName;

    private IIcon[] iconSide;

    public short maxRSS;
    public short maxFrequency;

    public BlockCable(short rss, short freq) {
        super(Material.circuits);
        setCreativeTab(IR2.tabIR2);
        maxRSS = rss;
        maxFrequency = freq;
    }

    @Override
    public Block setBlockName(String par1Str) {
        textureFileName = par1Str;
        return super.setBlockName("IR2" + par1Str);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int a) {
        return new TileEntityCable();
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 15));
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        TileEntity e1 = par1World.getTileEntity(par2, par3, par4);
        if (e1 == null) {
            return;
        }
        if (e1 instanceof TileEntityCable && par5Random.nextInt(2) == 1) {
            TileEntityCable tileEntity = (TileEntityCable) e1;
            if (tileEntity.getOutputRSS(-1) > 0) {
                double dx = par2 + 0.5D + (par5Random.nextDouble() - 0.5D) * 2.0D * 0.4D;
                double dy = par3 + 0.5D + (par5Random.nextDouble() - 0.5D) * 2.0D * 0.4D;
                double dz = par4 + 0.5D + (par5Random.nextDouble() - 0.5D) * 2.0D * 0.4D;
                par1World.spawnParticle("reddust", dx, dy, dz, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        iconSide = new IIcon[16];
        for (int i = 0; i < 16; i++) {
            iconSide[i] = par1IconRegister.registerIcon(IR2.MODID + ":cableSide" + ItemCable.COLOR_NAMES[i]);
        }

        super.registerBlockIcons(par1IconRegister);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return super.getIcon(par1, par2);
    }

    public IIcon getIconSide(int meta) {
        return iconSide[meta];
    }

    @Override
    public int getRenderType() {
        return IR2.renderCableNewId;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float w = (8.0f / 16.0f) / 2.0f;
        setBlockBounds(0.5f - w, 0, 0.5f - w, 0.5f + w, w, 0.5f + w);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        double d = 0.0D;
        double w = (2.0/16.0) / 2.0D;

        setBlockBoundsBasedOnState(par1World, x, y, z);

        //center
        AxisAlignedBB b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y, z + 0.5D - w, x + 0.5D + w, y + w, z + 0.5D + w);
        if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
            par6List.add(b);
        }


        TileEntity t = par1World.getTileEntity(x, y, z);
        if (t instanceof TileEntityCable) {
            int con = ((TileEntityCable) t).connectState;
            if ((con & 32) != 0) {
                b = AxisAlignedBB.getBoundingBox(x + 0.5D + w, y, z + 0.5D - w, x + 1.0D, y+w, z + 0.5D + w);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
            if ((con & 8) != 0) {
                b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y, z + 0.5D + w, x + 0.5D + w, y + w, z + 1.0D);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
            if ((con & 16) != 0) {
                b = AxisAlignedBB.getBoundingBox(x, y, z + 0.5D - w, x + 0.5D - w, y + w, z + 0.5D + w);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
            if ((con & 4) != 0) {
                b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y, z, x + 0.5D + w, y+w, z + 0.5D - w);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
            /*
            if ((con & 1) != 0) {
                b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y, z + 1.0D - w, x + 0.5D + w, y+1, z + 0.5D + w);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
            */
            if ((con & 2) != 0) {
                b = AxisAlignedBB.getBoundingBox(x + 0.5D - w, y+w, z + 0.5D - w, x + 0.5D + w, y+1, z + 0.5D + w);
                if (b != null && par5AxisAlignedBB.intersectsWith(b)) {
                    par6List.add(b);
                }
            }
        }
        //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        double d = -0.0625D;
        double w = (6.0 / 16.0) / 2.0;
        return AxisAlignedBB.getBoundingBox(x + 0.5 - w + d, y, z + 0.5 - w + d, x + 0.5 + w - d, y + w - d, z + 0.5 + w - d);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
        super.breakBlock(world, x, y, z, block, p_149749_6_);

        if (!world.isRemote) {
            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCable) {
            if (((TileEntityCable) te).change() && !world.isRemote) {
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
        }

        if (!world.isRemote) {
            super.onNeighborBlockChange(world, x, y, z, block);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlockOfNeighborChange(x, y, z, this);
            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }
    }

}
