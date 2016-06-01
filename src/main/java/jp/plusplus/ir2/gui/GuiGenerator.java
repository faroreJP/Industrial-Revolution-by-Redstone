package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.container.ContainerGenerator;
import jp.plusplus.ir2.tileentities.TileEntityGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiGenerator  extends GuiContainer {
    TileEntityGenerator entity;

    public GuiGenerator(EntityPlayer player, TileEntityGenerator tileEntity) {
        super(new ContainerGenerator(player, tileEntity));
        entity=tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

        int col = entity.getStringColor();

        //inventory
        String s = entity.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, col);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, col);

        //output
        s = I18n.format("gui.output");
        if (s == null) s = "output";
        this.fontRendererObj.drawString(s, 130, 20, col);

        s=String.format("%3dRSS", entity.getOutputRSS(2));
        this.fontRendererObj.drawString(s, 154-fontRendererObj.getStringWidth(s), 30, col);

        s=String.format("%3dHz ", entity.getOutputFrequency(2));
        this.fontRendererObj.drawString(s, 154-fontRendererObj.getStringWidth(s), 40, col);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/"+entity.getGuiFileName()));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if(entity.getOutputRSS(-1)>0){
            this.drawTexturedModalRect(k+78, l+26, 176, 0, 22, 15);
        }
    }

}
