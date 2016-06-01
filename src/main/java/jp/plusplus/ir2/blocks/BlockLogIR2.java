package jp.plusplus.ir2.blocks;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by plusplus_F on 2015/05/21.
 */
public class BlockLogIR2 extends BlockNewLog implements IFuelHandler{
    public BlockLogIR2(){
        setCreativeTab(IR2.tabIR2);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_getSubBlocks_1_, CreativeTabs p_getSubBlocks_2_, List p_getSubBlocks_3_) {
        p_getSubBlocks_3_.add(new ItemStack(p_getSubBlocks_1_, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_registerBlockIcons_1_) {
        this.field_150167_a = new IIcon[1];
        this.field_150166_b = new IIcon[1];

        if (this == BlockCore.logRed) {
            this.field_150167_a[0] = p_registerBlockIcons_1_.registerIcon(IR2.MODID + ":logRedSide");
            this.field_150166_b[0] = p_registerBlockIcons_1_.registerIcon(IR2.MODID + ":logRedTop");
        } else {
            this.field_150167_a[0] = p_registerBlockIcons_1_.registerIcon("log_" + BlockOldLog.field_150168_M[2]);
            this.field_150166_b[0] = p_registerBlockIcons_1_.registerIcon("log_" + BlockOldLog.field_150168_M[2] + "_top");
        }
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        Item item=itemStack.getItem();
        if(!(item instanceof ItemBlock)) return 0;
        Block b=((ItemBlock) item).field_150939_a;

        if(b instanceof BlockLogIR2) return 15*20;
        if(b instanceof BlockSaplingIR2) return 5*20;

        return 0;
    }
}
