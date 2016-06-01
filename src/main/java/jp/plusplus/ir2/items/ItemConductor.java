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
public class ItemConductor extends Item {
    public static final int[] COLORS= {
            0xffdddd, 0xcc3300, 0xccaaaa, 0xffccee, 0xffdd00,
            0xeecccc, 0xefdfdf,0xffdddd,0x993333,0xfceaea
    };
    public static final String[] NAMES= {
            "Tin", "Copper", "Iron", "Silver", "Gold",
            "Manganese", "Cobalt","Aluminium","Silicon","Nickel"
    };

    public ItemConductor(){
        setCreativeTab(IR2.tabIR2);
        setHasSubtypes(true);
        setMaxDamage(0);
        setTextureName(IR2.MODID+":conductorTin");
        setUnlocalizedName("IR2conductor");
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
