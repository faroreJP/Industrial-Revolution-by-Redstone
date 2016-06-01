package jp.plusplus.ir2.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.gui.GuiFurnaceAdvanced;
import jp.plusplus.ir2.gui.GuiFurnaceRS;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class RSAdvFurnaceRecipeHandler extends TemplateRecipeHandler {

    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(75, 26, 24, 18), getOverlayIdentifier(), new Object[0]));
    }

    public String getOverlayIdentifier() {
        return "ir2.smelting2";
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFurnaceAdvanced.class;
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.furnace", new Object[0]);
    }

    public void drawExtras(int recipe) {
        this.drawProgressBar(75-5, 26-11, 176, 0, 22, 15, 48, 0);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass() == RSAdvFurnaceRecipeHandler.class) {
            Map recipes = FurnaceRecipes.smelting().getSmeltingList();
            Iterator i$ = recipes.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry recipe = (Map.Entry)i$.next();
                this.arecipes.add(new RSAdvFurnaceRecipeHandler.SmeltingPair((ItemStack)recipe.getKey(), (ItemStack)recipe.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    public void loadCraftingRecipes(ItemStack result) {}
    public void loadUsageRecipes(String inputId, Object... ingredients) {}
    public void loadUsageRecipes(ItemStack ingredient) {}

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/processor.png";
    }


    public class SmeltingPair extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        PositionedStack result;

        public SmeltingPair(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            ingredients.add(new PositionedStack(ingred, 44-5, 25-11));
            this.result=new PositionedStack(result, 116-5,25-11);
        }

        public java.util.List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RSAdvFurnaceRecipeHandler.this.cycleticks / 48, ingredients);
        }

        public PositionedStack getResult() {
            return this.result;
        }
    }
}
