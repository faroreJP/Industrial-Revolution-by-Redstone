package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.worldgen.WorldGenGoldTree;
import jp.plusplus.ir2.worldgen.WorldGenRedTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/20.
 */
public class BlockSaplingIR2 extends BlockSapling {
    public BlockSaplingIR2(){
        this.setCreativeTab(IR2.tabIR2);
        setCreativeTab(IR2.tabIR2);
        setStepSound(soundTypeGrass);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return blockIcon;
    }

    public int damageDropped(int p_149692_1_) {
        return 0;
    }
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        blockIcon=p_149651_1_.registerIcon(getTextureName());
    }

    @Override
    public void func_149878_d(World world, int x, int y, int z, Random rand) {
        if(TerrainGen.saplingGrowTree(world, rand, x, y, z)) {
            int l = world.getBlockMetadata(x, y, z) & 7;
            Object object;
            if(this==BlockCore.saplingRed) {
                object=new WorldGenRedTree(true, this, BlockCore.logRed, BlockCore.leaveRed);
            }
            else{
                object=new WorldGenGoldTree(true, false);
            }

            int i1 = 0;
            int j1 = 0;
            boolean flag = false;

            Block block = Blocks.air;
            if(flag) {
                world.setBlock(x + i1, y, z + j1, block, 0, 4);
                world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
                world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
                world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
            } else {
                world.setBlock(x, y, z, block, 0, 4);
            }

            if(!((WorldGenerator)object).generate(world, rand, x + i1, y, z + j1)) {
                if(flag) {
                    world.setBlock(x + i1, y, z + j1, this, l, 4);
                    world.setBlock(x + i1 + 1, y, z + j1, this, l, 4);
                    world.setBlock(x + i1, y, z + j1 + 1, this, l, 4);
                    world.setBlock(x + i1 + 1, y, z + j1 + 1, this, l, 4);
                } else {
                    world.setBlock(x, y, z, this, l, 4);
                }
            }

        }
    }

}
