package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import jp.plusplus.ir2.tileentities.TileEntityModulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class BlockModulator extends BlockAmplifier{

    protected BlockModulator() {
        setBlockName("modulator");
        setHarvestLevel("pickaxe", 3);
        setHardness(50.0f);
        setResistance(6000.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par1) {
        return new TileEntityModulator();
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister){
        blockIcon			=	par1IconRegister.registerIcon(IR2.MODID+":casingObsidianSide");
        iconFront			=	par1IconRegister.registerIcon(IR2.MODID+":modulatorFront");
        iconBack			=	par1IconRegister.registerIcon(IR2.MODID+":modulatorBack");
        iconTop				=	par1IconRegister.registerIcon(IR2.MODID+":casingObsidianTop");
        iconBottom			=	par1IconRegister.registerIcon(IR2.MODID+":casingObsidianBottom");
    }
}
