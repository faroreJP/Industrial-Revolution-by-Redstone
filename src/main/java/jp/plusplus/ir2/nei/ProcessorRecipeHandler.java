package jp.plusplus.ir2.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.FMLLog;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.gui.GuiProcessor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/03/01.
 */
public class ProcessorRecipeHandler extends TemplateRecipeHandler {
    private HashMap<ItemStack, ItemStack> recipes;
    private String unlocalizedGUIName;

    public ProcessorRecipeHandler(){
        unlocalizedGUIName="SampleRecipe";
    }




    public PositionedStack getResult() {
        return null;
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiProcessor.class;
    }

    @Override
    public String getOverlayIdentifier() {
        return "R-processor";
    }
    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/processor.png";
    }
    @Override
    public String getRecipeName() {
        //return I18n.format(unlocalizedGUIName);
        return "R-processor";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 25, 20, 20), "R-processor"));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        FMLLog.severe("output:"+outputId);
        if (getRecipeName().equals(outputId)) {
            if (recipes == null || recipes.isEmpty()) return;
            for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
                ItemStack item = recipe.getValue();
                ItemStack in = recipe.getKey();
                arecipes.add(new recipeCacher(in, item));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (recipes == null || recipes.isEmpty()) return;
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            ItemStack item = recipe.getValue();
            ItemStack in = recipe.getKey();
            if (NEIServerUtils.areStacksSameType(item, result)) {
                arecipes.add(new recipeCacher(in, item));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (recipes == null || recipes.isEmpty()) return;
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            ItemStack item = recipe.getValue();
            ItemStack in = recipe.getKey();
            if (ingredient.getItem() == in.getItem() && ingredient.getItemDamage() == in.getItemDamage()) {
                arecipes.add(new recipeCacher(ingredient, item));
            }
        }
    }

    public class recipeCacher extends CachedRecipe {
        private PositionedStack input;
        private PositionedStack result;

        public recipeCacher(ItemStack in, ItemStack out) {
            in.stackSize = 1;
            this.input = new PositionedStack(in, 48, 21);
            this.result= new PositionedStack(out, 102, 21);
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }
        @Override
        public PositionedStack getIngredient(){
            return this.input;
        }
    }

}
