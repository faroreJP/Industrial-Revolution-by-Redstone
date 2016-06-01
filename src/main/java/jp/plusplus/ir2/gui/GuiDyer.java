package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityDyer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiDyer extends GuiProcessor {
    TileEntityDyer entityDyer;
    public GuiDyer(Container container, TileEntityDyer tileEntity){
        super(container, tileEntity, tileEntity);
        entityDyer=tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/"+entity.getGuiFileName()));
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if(entityDyer.dyeQuantity>0){
            int i1=entityDyer.getDyeQuantityScaled(16);
            this.drawTexturedModalRect(k + 54, l + 25 + (16 - i1), 16*entityDyer.dyeColor, 166+16-i1, 16, i1);
        }

        this.drawTexturedModalRect(k+80, l+26, 176, 0, entity.getProgressScaled(22), 15);
    }
}
