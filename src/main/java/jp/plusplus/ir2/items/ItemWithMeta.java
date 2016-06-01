package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockPipe;
import jp.plusplus.ir2.blocks.IBlockMeta;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/15.
 */
public class ItemWithMeta extends ItemBlock {
    public ItemWithMeta(Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_){
        if(field_150939_a instanceof IBlockMeta) return ((IBlockMeta) field_150939_a).getUnlocalizedName(p_77667_1_.getItemDamage());
        else return this.field_150939_a.getUnlocalizedName();
    }


    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if(field_150939_a== BlockCore.deco && par1ItemStack.getItemDamage()==1){
            par3List.add(I18n.format("info.tutanaga.0"));
            par3List.add(I18n.format("info.tutanaga.1"));
        }
    }

}
