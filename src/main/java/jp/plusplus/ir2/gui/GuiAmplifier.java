package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.container.ContainerAmplifier;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class GuiAmplifier  extends GuiContainer {
    TileEntityAmplifier entity;

    public GuiAmplifier(EntityPlayer entityPlayer, TileEntityAmplifier tileEntity) {
        super(new ContainerAmplifier(entityPlayer, tileEntity));
        entity=tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s = entity.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        //input
        s=I18n.format("gui.input");
        this.fontRendererObj.drawString(s, 22, 20, 4210752);

        s=String.format("%3dRSS", entity.rss);
        this.fontRendererObj.drawString(s, 46-fontRendererObj.getStringWidth(s), 30, 4210752);
        s=String.format("%3dHz ", entity.frequency);
        this.fontRendererObj.drawString(s, 46-fontRendererObj.getStringWidth(s), 40, 4210752);

        //output
        s=I18n.format("gui.output");
        this.fontRendererObj.drawString(s, 130, 20, 4210752);
        //this.fontRendererObj.drawString(entity.getOutputRSS(-1)+"RSS", 130, 30, 4210752);
        //this.fontRendererObj.drawString(entity.getOutputFrequency(-1)+"Hz", 130, 40, 4210752);

        s=String.format("%3dRSS", entity.getOutputRSS(-1));
        this.fontRendererObj.drawString(s, 154-fontRendererObj.getStringWidth(s), 30, 4210752);
        s=String.format("%3dHz ", entity.getOutputFrequency(-1));
        this.fontRendererObj.drawString(s, 154-fontRendererObj.getStringWidth(s), 40, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/amplifier.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if(entity.getOutputRSS(-1)==0){
            this.drawTexturedModalRect(k+61, l+26, 176, 0, 54, 15);
        }
    }

}