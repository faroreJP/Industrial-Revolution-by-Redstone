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
public class ItemIngot extends Item {
    public static final int[] COLORS= {
            0xffffaa, 0xcc6633, 0xddddff,
            0xdddddd, 0xccccee,0xeeeeee,0x333333,0xeaeaea
    };
    public static final String[] NAMES= {
            "Tin", "Copper", "Silver",
            "Manganese", "Cobalt","Aluminium","Silicon","Nickel"
    };

    public ItemIngot(){
        setCreativeTab(IR2.tabIR2);
        setHasSubtypes(true);
        setMaxDamage(0);
        setTextureName(IR2.MODID+":ingotTin");
        setUnlocalizedName("IR2ingot");
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
