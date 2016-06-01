package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityFountain;
import jp.plusplus.ir2.tileentities.TileEntityPump;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class GuiPump extends GuiContainer{
    private TileEntityPump entity;

    public GuiPump(Container p_i1072_1_, TileEntityPump t) {
        super(p_i1072_1_);
        entity =t;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=entity.getBlockType().getLocalizedName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/pump.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k+68, l+35, 176, 0, entity.getProgressScaled(22), 15);

        int amount = entity.tank.getFluidAmount();
        if (amount > 0) {
            IIcon icon = entity.getFluidIcon();
            if (icon != null) {
                int scale = 52 * amount / entity.tank.getCapacity();
                GuiTank.drawFluid(this, k + 98, l + 69 - scale, icon, 34, scale);
            }
        }
    }
}
