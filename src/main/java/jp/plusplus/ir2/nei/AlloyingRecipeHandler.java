package jp.plusplus.ir2.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiAlloySmelter;
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
 * Created by plusplus_F on 2015/06/29.
 */
public class AlloyingRecipeHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(80, 26, 22, 15), getOverlayIdentifier(), new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.alloying";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiAlloySmelter.class;
    }

    public void drawExtras(int recipe) {
        this.drawProgressBar(80-5, 26-11, 176, 0, 22, 15, 48, 0);
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/alloySmelter.png";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineAlloySmelter.getLocalizedName();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==AlloyingRecipeHandler.class) {
            Iterator<Recipes.RecipeItemStack> it = Recipes.getAlloying().iterator();
            while(it.hasNext()){
                Recipes.RecipeItemStack r=it.next();
                arecipes.add(new CachedAlloyingRecipe(r.getMaterial(), r.getProduct()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        int[] ids= OreDictionary.getOreIDs(result);

        //登録済みアイテムから探す
        Iterator<Recipes.RecipeItemStack> it = Recipes.getAlloying().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack r=it.next();
            if(Recipes.IsItemEquals(r.getProduct(), result, ids)){
                arecipes.add(new CachedAlloyingRecipe(r.getMaterial(), r.getProduct()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        int[] ids= OreDictionary.getOreIDs(ingredient);

        //登録済みアイテムから探す
        Iterator<Recipes.RecipeItemStack> it = Recipes.getAlloying().iterator();
        while(it.hasNext()){
            Recipes.RecipeItemStack r=it.next();
            if(r.isMatch(ingredient)){
                arecipes.add(new CachedAlloyingRecipe(ingredient, r.getProduct()));
            }
        }
    }


    public class CachedAlloyingRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;

        public CachedAlloyingRecipe(ItemStack mat, ItemStack res){
            ingredients.add(new PositionedStack(mat, 36-5, 25-11));
            ingredients.add(new PositionedStack(new ItemStack(Items.redstone), 54-5, 25-11));
            result=new PositionedStack(res, 116-5,25-11);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(AlloyingRecipeHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }
    }
}
