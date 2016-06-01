package jp.plusplus.ir2.nei;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiAlloySmelterRusty;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/06/29.
 */
public class RustyAlloyingRecipeHandler extends AlloyingRecipeHandler {

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(80, 35, 22, 15), getOverlayIdentifier(), new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.alloyingR";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiAlloySmelterRusty.class;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==RustyAlloyingRecipeHandler.class) {
            arecipes.add(new CachedAlloyingRecipe(new ItemStack(ItemCore.ingot, 1, 0), new ItemStack(ItemCore.conductor, 1, 0)));
            arecipes.add(new CachedAlloyingRecipe(new ItemStack(ItemCore.ingot, 1, 1), new ItemStack(ItemCore.conductor, 1, 1)));
            arecipes.add(new CachedAlloyingRecipe(new ItemStack(Items.iron_ingot), new ItemStack(ItemCore.conductor, 1, 2)));
        } else {
            if(outputId.equals("item")) {
                this.loadCraftingRecipes((ItemStack)results[0]);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {}

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {}

    @Override
    public String getRecipeName() {
        return BlockCore.alloySmelterRustyIdle.getLocalizedName();
    }
}
