package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.ModelChunkLoader;
import jp.plusplus.ir2.blocks.model.ModelPump;
import jp.plusplus.ir2.tileentities.TileEntityChunkLoader;
import jp.plusplus.ir2.tileentities.TileEntityPump;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/08/16.
 */
public class RenderPump extends RenderMachineBase {
    public static final ResourceLocation rl=new ResourceLocation(IR2.MODID+":textures/models/Pump.png");
    ModelPump model=new ModelPump();

    float f13=13.0f/16.0f;
    float f5=5.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId!=getRenderId()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(0f, -0.25f, 0f);

        float scaleEx=0.8f;
        GL11.glScalef(scaleEx, scaleEx, scaleEx);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 0);

        GL11.glTranslatef(0.5f, f13, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);

        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        model.renderLever(0.f, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return IR2.renderPumpId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityPump te=(TileEntityPump)entity;
        float rate=te.getRenderingTicksRate();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + f13, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        rotate(te.side);

        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        model.renderLever(rate, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPopMatrix();
    }
}
