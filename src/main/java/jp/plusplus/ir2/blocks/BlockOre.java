package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class BlockOre extends Block {
    public static final String[] NAMES= {
            "Tin", "Copper", "Silver"
    };
    public static final int[] HARVEST_LEVELS={
            1,1,2,
    };
    public static final float[] HARDNESS={
            2.5f,2.8f,3.5f
    };
    public IIcon[] icons;

    public BlockOre() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);

        for(int i=0;i<NAMES.length;i++){
            setHarvestLevel("pickaxe", HARVEST_LEVELS[i], i);
        }
    }

    @Override
    public int damageDropped(int par1){
        return par1;
    }
    @Override
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < NAMES.length; i++) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return super.getBlockHardness(world, x, y, z)*HARDNESS[world.getBlockMetadata(x,y,z)];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons=new IIcon[NAMES.length];
        for(int i=0;i<NAMES.length;i++){
            icons[i]=iconRegister.registerIcon(getTextureName()+NAMES[i]);
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[meta];
    }

}
