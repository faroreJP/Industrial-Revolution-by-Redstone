package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class GuiCollector extends GuiProcessor {
    public GuiCollector(Container container, TileEntityMachineBase tileEntity, ISidedInventory inventory) {
        super(container, tileEntity, inventory);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(IR2.MODID, "textures/gui/"+entity.getGuiFileName()));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(k+15, l+19, 0, 166, entity.getProgressScaled(146), 15);
    }
}
