package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/21.
 */
public class BlockLeaveIR2 extends BlockNewLeaf {
    public static final String[] LEAVE_NAME = new String[]{"oakRed", "birchGold"};

    public BlockLeaveIR2(){
        setCreativeTab(IR2.tabIR2);
        //setBlockName("IR2leaveRed");
        getUnlocalizedName();
    }


    public int damageDropped(int p_damageDropped_1_) {
        return 0;
    }
    public int getDamageValue(World p_getDamageValue_1_, int p_getDamageValue_2_, int p_getDamageValue_3_, int p_getDamageValue_4_) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_getIcon_1_, int p_getIcon_2_) {
        return this.blockIcon;
    }
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_getSubBlocks_1_, CreativeTabs p_getSubBlocks_2_, List p_getSubBlocks_3_) {
        p_getSubBlocks_3_.add(new ItemStack(p_getSubBlocks_1_, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_registerBlockIcons_1_) {
        this.blockIcon=p_registerBlockIcons_1_.registerIcon(IR2.MODID+":leaveRed");
    }

    public String[] func_150125_e() {
        return LEAVE_NAME;
    }


    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
        return this==BlockCore.leaveRed?0xe72828:0xfafa1b;
    }
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_getRenderColor_1_) {
        return this==BlockCore.leaveRed?0xe72828:0xfafa1b;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int p_shouldSideBeRendered_5_) {
        //Block var6 = access.getBlock(x, y, z);
        //return var6 == this?false:super.shouldSideBeRendered(access, x, y, z, p_shouldSideBeRendered_5_);

        return true;
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {
        if (this == BlockCore.leaveRed) {
            if (p_150124_1_.rand.nextInt(p_150124_6_) == 0) {
                this.dropBlockAsItem(p_150124_1_, p_150124_2_, p_150124_3_, p_150124_4_, new ItemStack(ItemCore.appleRed, 1, 0));
            }
        } else {
            if (p_150124_1_.rand.nextInt(p_150124_6_) - 30 <= 0) {
                this.dropBlockAsItem(p_150124_1_, p_150124_2_, p_150124_3_, p_150124_4_, new ItemStack(Items.gold_nugget, 1, 0));
            }
        }
    }
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(this==BlockCore.leaveRed?BlockCore.saplingRed:BlockCore.saplingGold);
    }
}
