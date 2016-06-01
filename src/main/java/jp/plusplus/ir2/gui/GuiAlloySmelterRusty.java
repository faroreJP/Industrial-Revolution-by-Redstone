package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityAlloySmelter;
import jp.plusplus.ir2.tileentities.TileEntityAlloySmelterRusty;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiAlloySmelterRusty extends GuiContainer {
    TileEntityAlloySmelterRusty entity;
    public GuiAlloySmelterRusty(Container container, TileEntityAlloySmelterRusty tileEntity){
        super(container);
        entity=tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s = entity.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/alloySmelterRusty.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if(entity.isBurning()){
            int i1=entity.getBurnTimeScaled(12);
            this.drawTexturedModalRect(k+46, l+37+(12-i1), 176, 12-i1, 14, i1+2);
        }

        this.drawTexturedModalRect(k+80, l+35, 176, 14, entity.getProgressScaled(22), 15);
    }
}
