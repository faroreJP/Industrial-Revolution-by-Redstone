package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.container.ContainerAmplifier;
import jp.plusplus.ir2.container.ContainerTransmitter;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import jp.plusplus.ir2.tileentities.TileEntityTransmitter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class GuiTransmitter  extends GuiContainer {
    TileEntityTransmitter entity;

    public GuiTransmitter(EntityPlayer entityPlayer, TileEntityTransmitter tileEntity) {
        super(new ContainerTransmitter(entityPlayer, tileEntity));
        entity=tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s = BlockCore.machineTransmitter.getLocalizedName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/transmitter.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(k+62, l+36, 176, 0, entity.getProgressScaled(16), 15);
        this.drawTexturedModalRect(k+98, l+36, 176, 0, entity.getProgressScaled(16), 15);
    }
}
