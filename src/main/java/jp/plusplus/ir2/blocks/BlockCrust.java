package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemCrust;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class BlockCrust extends Block {
    Random rand=new Random();

    protected BlockCrust() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setHarvestLevel("pickaxe", 2);
        setHardness(3.0f);
        setResistance(18.0f);
        setBlockName("IR2crust");
        setBlockTextureName(IR2.MODID + ":crust");
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < ItemCrust.NAMES.length; i++) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }
    @Override
    public Item getItemDropped(int meta, Random p_149650_2_, int p_149650_3_){
        return ItemCore.crust;
    }
    @Override
    public int damageDropped(int p_149692_1_) {
        return p_149692_1_;
    }
    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 1;
    }
    @Override
    public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
        int j = p_149679_2_.nextInt(p_149679_1_ + 2) - 1;
        if (j < 0) {
            j = 0;
        }

        return this.quantityDropped(p_149679_2_) * (j + 1);
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
        super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_, p_149690_7_);
    }

    @Override
    public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_) {
        return MathHelper.getRandomIntegerInRange(this.rand, 3, 4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        return ItemCrust.COLORS[p_149741_1_];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
        return ItemCrust.COLORS[access.getBlockMetadata(x,y,z)];
    }
}
