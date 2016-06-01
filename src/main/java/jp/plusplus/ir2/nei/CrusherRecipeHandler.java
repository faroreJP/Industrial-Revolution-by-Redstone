package jp.plusplus.ir2.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiCrusher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class CrusherRecipeHandler extends TemplateRecipeHandler {

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(50, 26, 22, 15), "ir2.crusher", new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.crusher";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineCrusher.getLocalizedName();
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 162 - 5, 65);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(50-5, 26-11, 176, 0, 22, 15, 48, 0);

        String str;
        int s=0;
        for(Float f : ((CachedCrushingRecipe)arecipes.get(recipe)).prob){
            str=String.format("%.1f%%", f*100);
            GuiDraw.fontRenderer.drawString(str, 98 - (GuiDraw.fontRenderer.getStringWidth(str)) / 2 - 5+26*s, 48 - 11, 0x404040);
            s++;
        }
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrusher.class;
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("ir2.crusher") && this.getClass() == CrusherRecipeHandler.class) {
            Recipes.RecipeItemStack.CrushingRecipeItemStack rp;
            Iterator<Recipes.RecipeItemStack.CrushingRecipeItemStack> it = Recipes.getCrushing().iterator();

            //登録済みアイテム
            while(it.hasNext()) {
                rp=it.next();
                arecipes.add(new CrusherRecipeHandler.CachedCrushingRecipe(rp));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Recipes.RecipeItemStack.CrushingRecipeItemStack rp;
        Iterator<Recipes.RecipeItemStack.CrushingRecipeItemStack> it = Recipes.getCrushing().iterator();

        int[] Ids=OreDictionary.getOreIDs(result);

        while(it.hasNext()) {
            rp=it.next();

            Object[] obj=rp.getProducts();
            for(int i=1;i<obj.length;i+=2){
                ItemStack item=(ItemStack)obj[i];

                int[] idRet=OreDictionary.getOreIDs(item);
                if(idRet!=null){
                    for(int d : idRet){
                        String s=OreDictionary.getOreName(d);
                        if(s.startsWith("ore") || s.startsWith("dust") || s.startsWith("ingot") || s.startsWith("crushed") || s.startsWith("gem")){
                            for(int id : Ids){
                                if(id==d){
                                    arecipes.add(new CrusherRecipeHandler.CachedCrushingRecipe(rp));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Recipes.RecipeItemStack.CrushingRecipeItemStack rp;
        Iterator<Recipes.RecipeItemStack.CrushingRecipeItemStack> it = Recipes.getCrushing().iterator();

        int[] Ids=OreDictionary.getOreIDs(ingredient);

        //登録済みアイテム
        while(it.hasNext()) {
            rp=it.next();

            if(rp.isMatch(ingredient)){
                arecipes.add(new CrusherRecipeHandler.CachedCrushingRecipe(rp));
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/crusher2_NEI.png";
    }


    public class CachedCrushingRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public float[] prob;
        public PositionedStack[] results;

        public CachedCrushingRecipe(Recipes.RecipeItemStack.CrushingRecipeItemStack im){
            ingredients.add(new PositionedStack(im.getMaterial(), 21-5, 25-11));

            Object[] obj=im.getProducts();
            prob=new float[obj.length/2];
            results=new PositionedStack[obj.length/2];
            for(int i=0;i<prob.length;i++){
                prob[i]=(Float)obj[2*i];
                results[i]=new PositionedStack((ItemStack)obj[2*i+1], 90-5+26*i, 25-11);
            }
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            ArrayList stacks = new ArrayList();
            for(PositionedStack ps : results){
                stacks.add(ps);
            }

            return stacks;
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(CrusherRecipeHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

    }
}
