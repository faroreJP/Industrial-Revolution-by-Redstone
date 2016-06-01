package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/14.
 */
public class GuiPipeSorting extends GuiContainer {
    public GuiPipeSorting(Container p_i1072_1_) {
        super(p_i1072_1_);
        this.xSize=175;
        this.ySize=225;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){

        int col=0x404040;

        //inventory
        String s = I18n.format("gui.pipeSorting");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, col);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, col);

        for(int i=0;i<6;i++){
            s=I18n.format("gui.direction."+i);
            int w=this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, 4+(40-w)/2, 20+19*i, col);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/pipeSorting.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
