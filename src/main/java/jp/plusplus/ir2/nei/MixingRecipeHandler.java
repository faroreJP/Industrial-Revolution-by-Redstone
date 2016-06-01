package jp.plusplus.ir2.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.gui.GuiLoom;
import jp.plusplus.ir2.gui.GuiMixer;
import jp.plusplus.ir2.gui.GuiTank;
import jp.plusplus.ir2.tileentities.TileEntityMixer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by plusplus_F on 2015/08/17.
 */
public class MixingRecipeHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(81-5, 32-11, 22, 15), getOverlayIdentifier(), new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "ir2.mixing";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiMixer.class;
    }

    public void drawExtras(int recipe) {
        this.drawProgressBar(81-5, 32-11, 176, 0, 22, 15, 48, 0);

        CachedMixingRecipe r=(CachedMixingRecipe)arecipes.get(recipe);
        if(!r.ingredientFluids.isEmpty()){
            for(int i=0;i<r.ingredientFluids.size();i++){
                FluidStack fs=r.ingredientFluids.get(i);
                int a=fs.amount;

                //流体の描画
                int scale = 32 * a / TileEntityMixer.TANK_CAPACITY;
                if(scale<=0) scale=1;

                int x=33-5+18*i, y=69-11-scale, w=16, h=scale;
                IIcon icon=fs.getFluid().getIcon();
                if(icon==null) return;

                GL11.glPushMatrix();
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);

                GuiDraw.changeTexture(TextureMap.locationBlocksTexture);

                int sx, sy;
                for (sy = 0; h - sy * 16 > 16; sy++) {
                    for (sx = 0; w - sx * 16 > 16; sx++) {
                        GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, 16);
                    }
                    GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, 16);
                }
                for (sx = 0; w - sx * 16 > 16; sx++) {
                    GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, h - sy * 16);
                }
                GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, h - sy * 16);

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();

                GuiDraw.changeTexture(this.getGuiTexture());
            }
        }
        if(r.resultFluid!=null){
            FluidStack fs=r.resultFluid;
            int a=fs.amount;

            //流体の描画
            int scale = 24 * a / TileEntityMixer.TANK_CAPACITY;
            if(scale<=0) scale=1;

            int x=115-5, y=69-11-scale, w=24, h=scale;
            IIcon icon=fs.getFluid().getIcon();
            if(icon==null) return;

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);

            GuiDraw.changeTexture(TextureMap.locationBlocksTexture);

            int sx, sy;
            for (sy = 0; h - sy * 16 > 16; sy++) {
                for (sx = 0; w - sx * 16 > 16; sx++) {
                    GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, 16);
                }
                GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, 16);
            }
            for (sx = 0; w - sx * 16 > 16; sx++) {
                GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, h - sy * 16);
            }
            GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, h - sy * 16);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            GuiDraw.changeTexture(this.getGuiTexture());
        }
    }

    @Override
    public String getGuiTexture() {
        return IR2.MODID+":textures/gui/mixer.png";
    }

    @Override
    public String getRecipeName() {
        return BlockCore.machineMixer.getLocalizedName();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && this.getClass()==MixingRecipeHandler.class) {
            Iterator<Map.Entry<Object[], Object>> it = Recipes.getMixing().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Object[], Object> r=it.next();
                arecipes.add(new CachedMixingRecipe(r.getKey(), r.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        int[] ids= OreDictionary.getOreIDs(result);

        //登録済みアイテムから探す
        Iterator<Map.Entry<Object[], Object>> it = Recipes.getMixing().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Object[], Object> r=it.next();
            Object ret=r.getValue();
            if(ret instanceof ItemStack && Recipes.IsItemEquals((ItemStack)ret, result, ids)){
                arecipes.add(new CachedMixingRecipe(r.getKey(), r.getValue()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        int[] ids= OreDictionary.getOreIDs(ingredient);
        FluidStack fs= FluidContainerRegistry.getFluidForFilledItem(ingredient);

        //登録済みアイテムから探す
        Iterator<Map.Entry<Object[], Object>> it = Recipes.getMixing().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Object[], Object> r=it.next();
            Object[] mat=r.getKey();

            for(int i=0;i<mat.length;i++){
                if(mat[i] instanceof ItemStack){
                    if(Recipes.IsItemEquals((ItemStack)mat[i], ingredient, ids)){
                        arecipes.add(new CachedMixingRecipe(mat, r.getValue()));
                    }
                }
                else if(mat[i] instanceof FluidStack && fs!=null){
                    if(fs.isFluidEqual((FluidStack)mat[i])){
                        arecipes.add(new CachedMixingRecipe(mat, r.getValue()));
                    }
                }
            }
        }
    }

    public class CachedMixingRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public ArrayList<FluidStack> ingredientFluids=new ArrayList<FluidStack>();
        public PositionedStack result;
        public FluidStack resultFluid;

        public CachedMixingRecipe(Object[] mat, Object ret){
            for(int i=0;i<mat.length;i++){
                if(mat[i] instanceof ItemStack){
                    ingredients.add(new PositionedStack((ItemStack)mat[i], 33+18*ingredients.size()-5, 19-11));
                }
                if(mat[i] instanceof FluidStack){
                    ingredientFluids.add((FluidStack)mat[i]);
                }
            }

            if(ret instanceof ItemStack){
                result=new PositionedStack((ItemStack)ret, 119-5,23-11);
            }
            else if(ret instanceof FluidStack){
                resultFluid=(FluidStack)ret;
            }
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(MixingRecipeHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }
    }
}
