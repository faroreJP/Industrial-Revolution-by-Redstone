package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.tileentities.TileEntityRegulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/15.
 */
public class BlockRegulator extends BlockContainer implements IWrenchHandler {
    protected BlockRegulator() {
        super(Material.rock);
        setBlockName("IR2regulator");
        setHardness(3.5f);
        setResistance(10.0f);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setBlockTextureName(IR2.MODID+":regulator");
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityRegulator();
    }

    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int p_149709_5_) {
        return access.getBlockMetadata(x,y,z);
    }

    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int p_149748_5_) {
        return access.getBlockMetadata(x,y,z);
    }

    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        world.func_147480_a(x, y, z, true);
        return true;
    }
}
