package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.items.ItemWrench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/07.
 * 鉱石統一マン
 */
public class BlockUnifier extends Block {
    public BlockUnifier() {
        super(Material.rock);
        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
        setResistance(17.5f);
        setHarvestLevel("pickaxe", 0);

        setBlockName("IR2unifier");
        setBlockTextureName(IR2.MODID + ":unifierSide");
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null) {
            ItemStack get = Recipes.unifierItem(itemStack);
            if (get != null) {
                if (!par1World.isRemote) {
                    player.entityDropItem(new ItemStack(get.getItem(), itemStack.stackSize, get.getItemDamage()), player.getEyeHeight());
                    player.setCurrentItemOrArmor(0, null);
                    player.inventory.markDirty();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        if (p_149691_1_ == 0 || p_149691_1_ == 1) return BlockCore.casingStone.getIcon(p_149691_1_, p_149691_2_);
        return this.blockIcon;
    }
}
