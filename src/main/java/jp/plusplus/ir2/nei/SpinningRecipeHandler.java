package jp.plusplus.ir2.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiLoom;
import jp.plusplus.ir2.gui.GuiSpinning;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class SpinningRecipeHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(75, 26, 22, 15), getOverlayIdentifier(), new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.spinning";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiSpinning.class;
    }

    public void drawExtras(int recipe) {
        this.drawProgressBar(75-5, 26-11, 176, 0, 22, 15, 48, 0);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==SpinningRecipeHandler.class) {
            Iterator<Recipes.RecipeItemStack> it=Recipes.getSpinning().iterator();
            while(it.hasNext()){
                Recipes.RecipeItemStack r=it.next();
                arecipes.add(new CachedSpinningRecipe(r.getMaterial(), r.getProduct()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Iterator<Recipes.RecipeItemStack> it=Recipes.getSpinning().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack r=it.next();
            if(r.getProduct().isItemEqual(result)){
                arecipes.add(new CachedSpinningRecipe(r.getMaterial(), result));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        int[] ids= OreDictionary.getOreIDs(ingredient);

        Iterator<Recipes.RecipeItemStack> it=Recipes.getSpinning().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack r=it.next();
            if(r.isMatch(ingredient)){
                arecipes.add(new CachedSpinningRecipe(ingredient, r.getProduct()));
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/spinning.png";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineSpinning.getLocalizedName();
    }

    public class CachedSpinningRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;

        public CachedSpinningRecipe(ItemStack m, ItemStack p){
            ingredients.add(new PositionedStack(m, 44-5, 25-11));
            result=new PositionedStack(p, 116-5,25-11);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(SpinningRecipeHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

    }
}
