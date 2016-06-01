package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.RenderBlockFluid;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class GuiTank extends GuiContainer{
    private TileEntityTank entity;

    public GuiTank(Container p_i1072_1_, TileEntityTank t) {
        super(p_i1072_1_);
        entity =t;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=entity.getWorldObj().getBlock(entity.xCoord, entity.yCoord, entity.zCoord).getLocalizedName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);

        FluidTankInfo t=entity.getTankInfo(ForgeDirection.UNKNOWN)[0];
        if(t.fluid!=null) {
            s = t.fluid.getLocalizedName();
            this.fontRendererObj.drawString(s, 105, 20, 0x404040);

            //s = String.format("%6dmb/%6dmb", t.fluid.amount, t.capacity);
            s=String.format("%.2f%%", 100.0*t.fluid.amount/t.capacity);
            this.fontRendererObj.drawString(s, 105, 32, 0x404040);
            s=""+t.fluid.amount+"mb";
            this.fontRendererObj.drawString(s, 105, 44, 0x404040);
        }
        else{
            s = "0.00%";
            this.fontRendererObj.drawString(s, 105, 32, 0x404040);
            this.fontRendererObj.drawString("0mb", 105, 44, 0x404040);
        }
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/tank.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int amount = entity.tank.getFluidAmount();
        if (amount > 0) {
            IIcon icon = entity.getFluidIcon();
            if (icon != null && entity.tank.getFluidAmount()>0) {
                int scale = 50 * amount / entity.tank.getCapacity();
                drawFluid(this, k + 8, l + 67 - scale, icon, 70, scale);
            }
        }
    }

    public static void drawFluid(GuiContainer gui, int x, int y, IIcon icon, int w, int h) {
        if(h==0) h=1;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        gui.mc.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        int sx, sy;
        for (sy = 0; h - sy * 16 > 16; sy++) {
            for (sx = 0; w - sx * 16 > 16; sx++) {
                gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, 16);
            }
            gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, 16);
        }
        for (sx = 0; w - sx * 16 > 16; sx++) {
            gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, h - sy * 16);
        }
        gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, h - sy * 16);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
