package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/15.
 * 装飾用の無機能ブロック
 */
public class BlockDecoration extends Block implements IBlockMeta{
    public static final String[] NAMES={"Vent","Zinc"};
    protected IIcon[] icons=new IIcon[NAMES.length];
    protected IIcon[] iconZinc=new IIcon[16];

    protected BlockDecoration() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setBlockName("IR2deco");
        setHardness(1.5f);
        setResistance(5.0f);
    }


    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int i=0;i<NAMES.length;i++) par3List.add(new ItemStack(par1, 1, i));
    }
    @Override
    public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side){
        int meta=w.getBlockMetadata(x, y, z);
        if(meta==1){
            //トタン
            if(x<0) x=4-(MathHelper.abs_int(x)%4);
            if(y<0) y=4-(MathHelper.abs_int(y)%4);
            if(z<0) z=4-(MathHelper.abs_int(z)%4);

            //Y方向・・・XZ平面
            if(side==0) return iconZinc[((x%4))+4*((z%4))];
            if(side==1) return iconZinc[(x%4)+4*(z%4)];
            //Z方向・・・XY平面
            if(side==3) return iconZinc[(x%4)+4*(3-(y%4))];
            if(side==2) return iconZinc[(3-(x%4))+4*(3-(y%4))];
            //X方向・・・ZY平面
            if(side==5) return iconZinc[(3-(z%4))+4*(3-(y%4))];
            if(side==4) return iconZinc[(z%4)+4*(3-(y%4))];
        }
        return this.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon(int side, int meta){
        if(meta<0 || meta>=icons.length) meta=0;
        return icons[meta];
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister){
        for(int i=0;i< NAMES.length;i++){
            if(i==1) icons[i]=par1IconRegister.registerIcon(IR2.MODID+":deco"+NAMES[i]+"0");
            else icons[i]=par1IconRegister.registerIcon(IR2.MODID+":deco"+NAMES[i]);
        }
        for(int i=0;i<16;i++){
            iconZinc[i]=par1IconRegister.registerIcon(IR2.MODID+":decoZinc"+i);
        }
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return getUnlocalizedName()+NAMES[meta];
    }
}
