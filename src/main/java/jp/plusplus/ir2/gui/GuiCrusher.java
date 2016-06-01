package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityCrusher;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiCrusher extends GuiProcessor {

    public GuiCrusher(Container container, TileEntityCrusher tileEntity){
        super(container, tileEntity, tileEntity);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/"+entity.getGuiFileName()));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(k+50, l+26, 176, 0, entity.getProgressScaled(22), 15);
    }
}
