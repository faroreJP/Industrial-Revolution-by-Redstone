package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockTransmitter;
import jp.plusplus.ir2.blocks.model.ModelChunkLoader;
import jp.plusplus.ir2.blocks.model.ModelHarvester;
import jp.plusplus.ir2.blocks.model.ModelTransmitter;
import jp.plusplus.ir2.tileentities.TileEntityHarvester;
import jp.plusplus.ir2.tileentities.TileEntityTransmitter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/08/18.
 */
public class RenderHarvester extends RenderMachineBase {
    public static final ResourceLocation rl =new ResourceLocation(IR2.MODID+":textures/models/Harvester.png");
    private ModelHarvester model=new ModelHarvester();

    float f13=13.0f/16.0f;
    float f5=5.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId!=getRenderId()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.25f, 0f, 0.1f);

        float scaleEx=0.5f;
        GL11.glScalef(scaleEx, scaleEx, scaleEx);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, -1);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 0);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 1);

        GL11.glTranslatef(0.5f, f13, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);

        bindTexture(rl);
        model.render(null, 0,0,0,0,0,1.0f);
        model.renderFrame(1, 1.0f);
        model.renderWheel(0, 1.0f);
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
        return IR2.renderHarvesterId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityHarvester te=(TileEntityHarvester)entity;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + f13, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        rotate(te.side);

        bindTexture(rl);
        model.render(null, 0,0,0,0,0,1.0f);
        model.renderFrame(te.range, 1.0f);
        model.renderWheel(te.getDrawTicksRate()*te.range, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPopMatrix();
        bindTexture(TextureMap.locationBlocksTexture);
    }
}
