package jp.plusplus.ir2.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiLoom;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class ExtractingRecipeHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(75, 26, 22, 15), getOverlayIdentifier(), new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.extracting";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiLoom.class;
    }

    public void drawExtras(int recipe) {
        this.drawProgressBar(75-5, 26-11, 176, 0, 22, 15, 48, 0);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==ExtractingRecipeHandler.class) {
            Iterator<Recipes.RecipeItemStack.ExtractingRecipeItemStack> it = Recipes.getExtracting().iterator();
            while(it.hasNext()){
                Recipes.RecipeItemStack.ExtractingRecipeItemStack r=it.next();
                arecipes.add(new CachedExtractingRecipe(r.getMaterial(), r.getProductAmount()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if(result.getItem()!=Items.redstone) return;

        Iterator<Recipes.RecipeItemStack.ExtractingRecipeItemStack> it = Recipes.getExtracting().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack.ExtractingRecipeItemStack r=it.next();
            arecipes.add(new CachedExtractingRecipe(r.getMaterial(), r.getProductAmount()));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        //登録済みアイテムから探す
        Iterator<Recipes.RecipeItemStack.ExtractingRecipeItemStack> it = Recipes.getExtracting().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack.ExtractingRecipeItemStack r=it.next();
            if(r.isMatch(ingredient)){
                arecipes.add(new CachedExtractingRecipe(ingredient, r.getProductAmount()));
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/processor.png";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineExtractor.getLocalizedName();
    }

    public class CachedExtractingRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;

        public CachedExtractingRecipe(ItemStack mat, int amount){
            ingredients.add(new PositionedStack(mat, 44-5, 25-11));
            result=new PositionedStack(new ItemStack(Items.redstone, amount), 116-5,25-11);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(ExtractingRecipeHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

    }
}
