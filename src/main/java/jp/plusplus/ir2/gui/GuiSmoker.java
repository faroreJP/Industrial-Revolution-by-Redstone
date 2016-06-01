package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntitySmoker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/07/04.
 */
public class GuiSmoker extends GuiContainer {
    protected TileEntitySmoker te;
    protected ISidedInventory inventory=null;

    public GuiSmoker(Container p_i1072_1_, TileEntitySmoker t) {
        super(p_i1072_1_);
        te=t;
        this.ySize=197;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        //String s = inventory.getInventoryName();
        String s=te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord).getLocalizedName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, te.getStringColor());
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, te.getStringColor());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/"+te.getGuiFileName()));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        //進捗
        for(int n=0;n<te.stage.length;n++){
            int i1=te.getStageScaled(n, 14);
            if(i1>0) this.drawTexturedModalRect(k+9+18*n, l+41+(14-i1), 176, 14-i1, 15, i1+1);
        }

        if(te.isBurning()){
            int i1=te.getBurnTimeScaled(12);
            this.drawTexturedModalRect(k+81, l+77+(12-i1), 176, 26-i1, 14, i1+2);
        }
    }
}
