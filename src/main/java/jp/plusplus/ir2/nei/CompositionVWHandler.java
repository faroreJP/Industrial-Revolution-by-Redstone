package jp.plusplus.ir2.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiSyntheticFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/08/31.
 */
public class CompositionVWHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(62, 36, 52, 15), getOverlayIdentifier(), new Object[0]));

    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.composition";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiSyntheticFurnace.class;
    }

    public void drawExtras(int recipe) {
        //this.drawProgressBar(75-5, 26-11, 176, 0, 22, 15, 48, 0);
        CachedVW vw=(CachedVW)arecipes.get(recipe);
        for(int i=0;i<vw.ingredients.size();i++){
            String t="table:"+Recipes.getTableNameFromId(vw.ingTable.get(i))+",weight:"+vw.ingWeights.get(i)+",value:"+vw.ingValues.get(i);
            if(vw.ingEXP.get(i)>0){
                t+=",enchant:"+vw.ingEXP.get(i);
            }
            GuiDraw.fontRenderer.drawString(t,26-5,17-11+18*i+3,0x404040);
        }
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/synDummy.png";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineSyntheticFurnace.getLocalizedName();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==CompositionVWHandler.class) {
            Iterator<Recipes.BuildingPair> it = Recipes.getBuildingItems().iterator();
            CachedVW vw=new CachedVW();

            while(it.hasNext()){
                Recipes.BuildingPair r=it.next();

                if(vw.addIngredient(r)==3){
                    arecipes.add(vw);
                    vw=new CachedVW();
                }
            }
            if(vw.ingredients.size()>0){
                arecipes.add(vw);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        int[] ids= OreDictionary.getOreIDs(result);

        //登録済みアイテムから探す
        Iterator<Recipes.BuildingPair> it = Recipes.getBuildingItems().iterator();
        while(it.hasNext()){
            Recipes.BuildingPair r=it.next();
            if(Recipes.IsItemEquals(r.itemstack, result, ids)){
                CachedVW vw=new CachedVW();
                vw.addIngredient(r);
                arecipes.add(vw);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        /*
        int[] ids= OreDictionary.getOreIDs(ingredient);

        //登録済みアイテムから探す
        Iterator<Map.Entry<ItemStack, ItemStack>> it = Recipes.getWeaving().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ItemStack, ItemStack> r=it.next();
            if(Recipes.IsItemEquals(r.getKey(), ingredient, ids)){
                arecipes.add(new CachedVW(ingredient, r.getValue()));
            }
        }
        */

        int[] ids= OreDictionary.getOreIDs(ingredient);

        //登録済みアイテムから探す
        Iterator<Recipes.BuildingPair> it = Recipes.getBuildingItems().iterator();
        while(it.hasNext()){
            Recipes.BuildingPair r=it.next();
            if(Recipes.IsItemEquals(r.itemstack, ingredient, ids)){
                CachedVW vw=new CachedVW();
                vw.addIngredient(r);
                arecipes.add(vw);
            }
        }
    }

    public class CachedVW extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public ArrayList<Integer> ingValues=new ArrayList<Integer>();
        public ArrayList<Integer> ingWeights=new ArrayList<Integer>();
        public ArrayList<Integer> ingEXP=new ArrayList<Integer>();
        public ArrayList<Integer> ingTable=new ArrayList<Integer>();

        public CachedVW(){}

        public int addIngredient(Recipes.BuildingPair bp){
            int s=ingredients.size();
            ingredients.add(new PositionedStack(bp.getMaterial(), 8-5, 17-11+18*s));
            ingValues.add(bp.value);
            ingWeights.add(bp.weight);
            ingTable.add(bp.type);
            ingEXP.add(Recipes.getBuildingEnchantLevel(bp.getMaterial()));
            return s+1;
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(CompositionVWHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }
    }
}
