package jp.plusplus.ir2.container;

import cpw.mods.fml.common.FMLCommonHandler;
import jp.plusplus.ir2.Recipes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/03/01.
 */
public class SlotWithEXP extends SlotTakeOnly {
    private EntityPlayer player;
    private int amount;
    public SlotWithEXP(EntityPlayer player, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.player=player;
    }

    public ItemStack decrStackSize(int p_75209_1_) {
        if(this.getHasStack()) {
            this.amount += Math.min(p_75209_1_, this.getStack().stackSize);
        }
        return super.decrStackSize(p_75209_1_);
    }
    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        this.onCrafting(p_82870_2_);
        super.onPickupFromSlot(p_82870_1_, p_82870_2_);
    }
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.amount += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    protected void onCrafting(ItemStack itemStack) {
        itemStack.onCrafting(player.worldObj, player, this.amount);
        if(!player.worldObj.isRemote) {
            int i = this.amount;
            float f = Recipes.getEXP(itemStack);
            int j;
            if(f == 0.0F) {
                i = 0;
            } else if(f < 1.0F) {
                j = MathHelper.floor_float((float) i * f);
                if(j < MathHelper.ceiling_float_int((float)i * f) && (float)Math.random() < (float)i * f - (float)j) {
                    ++j;
                }

                i = j;
            }

            while(i > 0) {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, j));
            }
        }

        this.amount = 0;
        FMLCommonHandler.instance().firePlayerSmeltedEvent(player, itemStack);
        if(itemStack.getItem() == Items.iron_ingot) {
            player.addStat(AchievementList.acquireIron, 1);
        }

        if(itemStack.getItem() == Items.cooked_fished) {
            player.addStat(AchievementList.cookFish, 1);
        }
    }
}
