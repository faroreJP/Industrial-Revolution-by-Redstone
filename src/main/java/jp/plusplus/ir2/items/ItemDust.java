package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class ItemDust extends Item {
    public static final int[] COLORS= {
            0xffffaa, 0xcc6633, 0xaaaaaa, 0xddddff, 0xffff33,
            0xdddddd, 0xccccee,0xeeeeee,0x333333,0xeaeaea,
            0xffffff, 0xb3833b,0x1f1f1f
    };
    public static final String[] NAMES= {
            "Tin", "Copper", "Iron", "Silver", "Gold",
            "Manganese", "Cobalt","Aluminium","Silicon","Nickel",
            "Magnesium", "Wood","Coal"
    };

    public ItemDust(){
        setCreativeTab(IR2.tabIR2);
        setHasSubtypes(true);
        setMaxDamage(0);
        setTextureName(IR2.MODID+":dustTin");
        setUnlocalizedName("IR2dust");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int i=0;i<NAMES.length;i++) par3List.add(new ItemStack(par1, 1, i));
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName(par1ItemStack) + NAMES[par1ItemStack.getItemDamage()];
    }
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        int d = par1ItemStack.getItemDamage();
        if(d<0 || d>=COLORS.length) return 0xffffff;
        return COLORS[d];
    }
}
