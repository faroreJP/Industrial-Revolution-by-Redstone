package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCable;
import jp.plusplus.ir2.blocks.BlockPole;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by plusplus_F on 2015/06/29.
 */
public class ItemPole extends ItemBlock {
    public ItemPole(Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxDamage(0);
        this.hasSubtypes = true;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int d=par1ItemStack.getItemDamage();
        if(d<0 || d>=BlockPole.NAMES.length) d=0;

        return super.getUnlocalizedName(par1ItemStack) + BlockPole.NAMES[d];
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (IR2.enableDescription) {
            par3List.add(I18n.format("info.cable.0"));
        }

        if (IR2.enableDescriptionOfRating) {
            String s = "2048RSS 2048Hz Max";
            par3List.add(EnumChatFormatting.RED + s);
        }
    }

}
