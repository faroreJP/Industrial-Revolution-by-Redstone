package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by plusplus_F on 2015/07/04.
 * 燻製品。
 */
public class ItemFoodSmoked extends ItemFood {
    public static final String[] NAMES = {"Pork", "Beef", "Chicken", "Salmon"};
    public static final int[] HEAL = {4, 4, 3, 3};
    protected IIcon[] icons;

    public ItemFoodSmoked() {
        super(0, 1.25f, true);
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName("IR2smoked");
        setTextureName(IR2.MODID+":smoked");
        setCreativeTab(IR2.tabIR2);
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

    @Override
    public void registerIcons(IIconRegister register){
        icons=new IIcon[NAMES.length];
        for(int i=0;i<icons.length;i++) icons[i]=register.registerIcon(IR2.MODID+":smoked"+NAMES[i]);
    }

    @Override
    public IIcon getIconFromDamage(int meta){
        return icons[meta];
    }

    //回復量
    @Override
    public int func_150905_g(ItemStack p_150905_1_) {
        int m=p_150905_1_.getItemDamage();
        if(m<0 || m>=HEAL.length) m=0;
        return HEAL[m];
    }
}
