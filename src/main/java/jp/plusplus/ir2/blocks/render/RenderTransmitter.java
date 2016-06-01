package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockTransmitter;
import jp.plusplus.ir2.blocks.model.ModelChunkLoader;
import jp.plusplus.ir2.blocks.model.ModelTransmitter;
import jp.plusplus.ir2.tileentities.TileEntityTransmitter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/08/16.
 */
public class RenderTransmitter extends RenderMachineBase {
    public static final ResourceLocation rlCL =new ResourceLocation(IR2.MODID+":textures/models/ChunkLoader.png");
    public static final ResourceLocation rlT =new ResourceLocation(IR2.MODID+":textures/models/Transmitter.png");
    ModelChunkLoader modelCL =new ModelChunkLoader();
    ModelTransmitter modelT =new ModelTransmitter();

    float f13=13.0f/16.0f;
    float f5=5.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId!=getRenderId()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(0f, 0f, 0f);

        float scaleEx=0.5f;
        GL11.glScalef(scaleEx, scaleEx, scaleEx);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 0);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 1);
        RenderMachineBase.renderCasing(block, renderer, 1, 0, 0);
        RenderMachineBase.renderCasing(block, renderer, 1, 0, 1);

        GL11.glTranslatef(0.5f, f13, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);

        //ローダの描画
        if(((BlockTransmitter)block).withLoader){
            bindTexture(rlCL);
            modelCL.render(null, 0, 0, 0, 0, 0, 1.0f);
            modelCL.renderNeedles(0.f, 1.0f);
        }

        //本体の描画
        bindTexture(rlT);
        modelT.render(null, 0,0,0,0,0,1.0f);

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
        return IR2.renderTransmitterId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityTransmitter te=(TileEntityTransmitter)entity;
        float rate=te.getRenderingTicksRate();

        //----------------------------素材アイテム--------------------------------
        EntityItem item=te.dummy;
        if(item!=null && item.getEntityItem()!=null){
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float) x+0.5f, (float) y + f13+0.4f, (float) z+0.5f);

            if(te.side==3) GL11.glTranslatef(1.0f,0,0);
            else if(te.side==2) GL11.glTranslatef(-1.0f,0,0);
            else if(te.side==4) GL11.glTranslatef(0,0,1.0f);
            else if(te.side==5) GL11.glTranslatef(0,0,-1.0f);
            RenderManager.instance.renderEntityWithPosYaw(item, 0,0,0, 0,0);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }

        //-------------------------本体----------------------------------
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + f13, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        rotate(te.side);

        //チャンクローダーの描画
        if(te.withLoader){
            bindTexture(rlCL);
            modelCL.render(null, 0, 0, 0, 0, 0, 1.0f);
            //IR2.logger.info(rate);
            modelCL.renderNeedles(rate, 1.0f);
        }

        //本体の描画
        bindTexture(rlT);
        modelT.render(null, 0,0,0,0,0,1.0f);

        GL11.glPopMatrix();
        bindTexture(TextureMap.locationBlocksTexture);
    }
}
