package jp.plusplus.ir2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.VillagerRegistry;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/08.
 */
public class VillagerTradeHandler implements VillagerRegistry.IVillageTradeHandler {
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        recipeList.add(new MerchantRecipe(new ItemStack(ItemCore.knittingWool, 4+random.nextInt(4)), Items.emerald));
        recipeList.add(new MerchantRecipe(new ItemStack(ItemCore.cloth, 1+random.nextInt(1)), Items.emerald));
        recipeList.add(new MerchantRecipe(new ItemStack(ItemCore.silk, 1+random.nextInt(1)), Items.emerald));

        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1+random.nextInt(3)), new ItemStack(ItemCore.gearWood, 4+random.nextInt(5))));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1+random.nextInt(3)), new ItemStack(ItemCore.gearStone, 4+random.nextInt(5))));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1+random.nextInt(3)), new ItemStack(ItemCore.gearIron, 4+random.nextInt(5))));


        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 8+random.nextInt(9)), new ItemStack(ItemCore.crystalUnitVillager)));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 16+random.nextInt(65-16)), new ItemStack(ItemCore.crystalUnit), new ItemStack(ItemCore.crystalUnitAdv)));

        recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 32+random.nextInt(65-32)), new ItemStack(ItemCore.screw)));

        //FMLLog.severe("added Trade");
    }
}
