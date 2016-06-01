package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/17.
 */
public class BlockPipeMining extends Block {
    protected BlockPipeMining() {
        super(Material.glass);
        setStepSound(Block.soundTypeStone);
        //setCreativeTab(IR2.tabIR2);
        setBlockName("IR2pipeMining");
        setBlockTextureName(IR2.MODID+":pipeMining");
    }


    @Override
    public int getRenderType(){
        return IR2.renderPipeMiningId;
    }
    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
        return true;
    }


    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return !super.canPlaceBlockAt(world, x, y, z)?false:this.canBlockStay(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(!this.canBlockStay(world, x, y, z)) {
            world.func_147480_a(x, y, z, true);


            y++;
            Block b=world.getBlock(x,y,z);
            while(b==this){
                y++;
                b=world.getBlock(x,y,z);
            }
            IR2.logger.info("x:"+x+",y:"+y+",z:"+z);
            if(b instanceof BlockMachineBase){
                TileEntity te=world.getTileEntity(x,y,z);
                if(te instanceof TileEntityMachineBase){
                    ((TileEntityMachineBase) te).markUpdateCanWorkState();
                    te.markDirty();
                }
                world.markBlockForUpdate(x,y,z);
            }

        }
    }
    @Override
    public boolean canBlockStay(World world, int x, int y, int z){
        Block b=world.getBlock(x,y+1,z);
        return b == BlockCore.pipeMining || b instanceof BlockMachineBase;
    }
    @Override
    public Item getItemDropped(int i1, Random r, int i2){
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
        float w=0.375f/2.0f;
        //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setBlockBounds(0.5f - w, 0.5f - w, 0.5f - w, 0.5f+w, 0.5f+w, 0.5f+w);
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
        return null;
    }
    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        return;
    }
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
        double d=-0.0625D;
        double w=0.375/2.0;
        return AxisAlignedBB.getBoundingBox((double) par2 + 0.5 - w + d, (double) par3 + 0.5 - w + d, (double) par4 + 0.5 - w + d, (double) par2 + 0.5 + w - d, (double) par3 + 0.5 + w - d, (double) par4 + 0.5 + w - d);
    }
}
