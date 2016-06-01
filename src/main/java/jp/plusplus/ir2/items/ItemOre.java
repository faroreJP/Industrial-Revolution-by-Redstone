package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCrust;
import jp.plusplus.ir2.blocks.BlockOre;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class ItemOre extends ItemBlock{
    public ItemOre(Block p_i45328_1_) {
        super(p_i45328_1_);
        setCreativeTab(IR2.tabIR2);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        if(field_150939_a instanceof BlockOre) {
            return super.getUnlocalizedName() + BlockOre.NAMES[par1ItemStack.getItemDamage()];
        }
        if(field_150939_a instanceof BlockCrust) {
            return super.getUnlocalizedName() + ItemCrust.NAMES[par1ItemStack.getItemDamage()];
        }
        return super.getUnlocalizedName();
    }
}
