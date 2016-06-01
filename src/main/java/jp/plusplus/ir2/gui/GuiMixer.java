package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityAutoCrafter;
import jp.plusplus.ir2.tileentities.TileEntityMixer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiMixer extends GuiContainer {
    protected TileEntityMixer entity;
    protected ISidedInventory inventory;

    public GuiMixer(Container container, TileEntityMixer tileEntity){
        super(container);
        entity=tileEntity;
        this.inventory=entity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        //String s = inventory.getInventoryName();
        String s=entity.getWorldObj().getBlock(entity.xCoord, entity.yCoord, entity.zCoord).getLocalizedName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, entity.getStringColor());
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, entity.getStringColor());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/" + entity.getGuiFileName()));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 81, l + 32, 176, 0, entity.getProgressScaled(22), 15);

        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        for (int n = 0; n < 3; n++) {
            int amount = entity.tank[n].getFluidAmount();
            if (amount > 0) {
                IIcon icon = entity.getFluidIcon(n);
                if (icon != null) {
                    int scale;
                    if(n<2){
                        scale=entity.getFluidScaled(n, 32);
                        GuiTank.drawFluid(this, k + 33 + 18 * n, l + 69 - scale, icon, 16, scale);
                    }
                    else{
                        scale=entity.getFluidScaled(n, 24);
                        GuiTank.drawFluid(this, k + 115 + 18 * n, l + 69 - scale, icon, 24, scale);
                    }
                }
            }
        }

    }
}
