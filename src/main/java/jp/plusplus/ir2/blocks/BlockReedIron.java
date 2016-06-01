package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/06/01.
 */
public class BlockReedIron extends BlockReed{

    public BlockReedIron(){
        setBlockName("IR2reedIron");
        setBlockTextureName(IR2.MODID+":reedIron");
        setStepSound(soundTypeGrass);
    }

    @Override
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
        if (p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_) == BlockCore.cropReedIron || this.func_150170_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_)) {
            if (p_149674_1_.isAirBlock(p_149674_2_, p_149674_3_ + 1, p_149674_4_)) {
                int l;

                for (l = 1; p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - l, p_149674_4_) == this; ++l) {
                    ;
                }

                if (l < 3) {
                    int i1 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

                    if (i1 == 15) {
                        p_149674_1_.setBlock(p_149674_2_, p_149674_3_ + 1, p_149674_4_, this);
                        p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, 0, 4);
                    } else {
                        p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, i1 + 1, 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z){
        Block block = world.getBlock(x, y - 1, z);

        if(block==this) return true;

        boolean isBeach = (block == Blocks.grass || block == Blocks.dirt || block == Blocks.sand);
        boolean hasWater = (world.getBlock(x - 1, y-1, z    ).getMaterial() == Material.water ||
                world.getBlock(x + 1, y-1, z    ).getMaterial() == Material.water ||
                world.getBlock(x,     y-1, z - 1).getMaterial() == Material.water ||
                world.getBlock(x,     y-1, z + 1).getMaterial() == Material.water);
        return isBeach && hasWater;
    }


    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_){
        return ItemCore.reedIron;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_){
        return ItemCore.reedIron;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_){
        return 0xffffff;
    }
}
