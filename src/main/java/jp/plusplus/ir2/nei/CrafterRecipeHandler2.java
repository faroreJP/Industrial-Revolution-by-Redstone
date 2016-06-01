package jp.plusplus.ir2.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/08/31.
 * Shpeless
 */
public class CrafterRecipeHandler2 extends CrafterRecipeHandler1 {
    public int[][] stackorder = new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}, {1, 2}, {2, 0}, {2, 1}, {2, 2}};

    public CrafterRecipeHandler2() {
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.shapeless", new Object[0]);
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("crafting") && this.getClass() == CrafterRecipeHandler2.class) {
            List allrecipes = CraftingManager.getInstance().getRecipeList();
            Iterator i$ = allrecipes.iterator();

            while(i$.hasNext()) {
                IRecipe irecipe = (IRecipe)i$.next();
                CrafterRecipeHandler2.CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes) {
                    recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
                } else if(irecipe instanceof ShapelessOreRecipe) {
                    recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
                }

                if(recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    public void loadCraftingRecipes(ItemStack result) {
        /*
        List allrecipes = CraftingManager.getInstance().getRecipeList();
        Iterator i$ = allrecipes.iterator();

        while(i$.hasNext()) {
            IRecipe irecipe = (IRecipe)i$.next();
            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CrafterRecipeHandler2.CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes) {
                    recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
                } else if(irecipe instanceof ShapelessOreRecipe) {
                    recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
                }

                if(recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
        */
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        /*
        List allrecipes = CraftingManager.getInstance().getRecipeList();
        Iterator i$ = allrecipes.iterator();

        while(i$.hasNext()) {
            IRecipe irecipe = (IRecipe)i$.next();
            CrafterRecipeHandler2.CachedShapelessRecipe recipe = null;
            if(irecipe instanceof ShapelessRecipes) {
                recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
            } else if(irecipe instanceof ShapelessOreRecipe) {
                recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
            }

            if(recipe != null && recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                this.arecipes.add(recipe);
            }
        }
        */
    }

    private CrafterRecipeHandler2.CachedShapelessRecipe shapelessRecipe(ShapelessRecipes recipe) {
        return recipe.recipeItems == null?null:new CrafterRecipeHandler2.CachedShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
    }

    public CrafterRecipeHandler2.CachedShapelessRecipe forgeShapelessRecipe(ShapelessOreRecipe recipe) {
        ArrayList items = recipe.getInput();
        Iterator i$ = items.iterator();

        Object item;
        do {
            if(!i$.hasNext()) {
                return new CrafterRecipeHandler2.CachedShapelessRecipe(items, recipe.getRecipeOutput());
            }

            item = i$.next();
        } while(!(item instanceof List) || !((List)item).isEmpty());

        return null;
    }

    public boolean isRecipe2x2(int recipe) {
        return this.getIngredientStacks(recipe).size() <= 4;
    }

    public class CachedShapelessRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedShapelessRecipe() {
            this.ingredients = new ArrayList();
        }

        public CachedShapelessRecipe(ItemStack output) {
            this();
            this.setResult(output);
        }

        public CachedShapelessRecipe(Object[] input, ItemStack output) {
            this((List) Arrays.asList(input), output);
        }

        public CachedShapelessRecipe(List input, ItemStack output) {
            this(output);
            this.setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            this.ingredients.clear();

            for(int ingred = 0; ingred < items.size(); ++ingred) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
                stack.setMaxSize(1);
                this.ingredients.add(stack);
            }

        }

        public void setResult(ItemStack output) {
            this.result = new PositionedStack(output, 119, 24);
        }

        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(cycleticks / 20, this.ingredients);
        }

        public PositionedStack getResult() {
            return this.result;
        }
    }
}
