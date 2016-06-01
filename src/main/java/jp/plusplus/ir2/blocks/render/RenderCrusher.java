package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCrusher;
import jp.plusplus.ir2.blocks.model.ModelCrusher;
import jp.plusplus.ir2.tileentities.TileEntityCrusher;
import jp.plusplus.ir2.tileentities.TileEntitySpinning;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/07/04.
 */
public class RenderCrusher extends RenderMachineBase {
    public static final ResourceLocation rlA=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher_a.png");
    public static final ResourceLocation rl2A=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher2_a.png");
    public static final ResourceLocation rlB=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher_b.png");
    public static final ResourceLocation rlC=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher_c.png");
    public static final ResourceLocation rl2C=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher2_c.png");
    public static final ResourceLocation rlD=new ResourceLocation(IR2.MODID+":textures/models/MachineCrusher_d.png");
    ModelCrusher model=new ModelCrusher();

    float f13=13.0f/16.0f;
    float f5=5.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        if(modelId!=getRenderId()) return;

        GL11.glPushMatrix();
        //GL11.glTranslatef(0.125f, -0.125f, -0.125f - 0.0625f);
        GL11.glTranslatef(0.2f,0,-0.15f);

        float scaleEx=0.7f;
        GL11.glScalef(scaleEx, scaleEx, scaleEx);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 0);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 1);

        GL11.glTranslatef(0.5f, f13, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);

        BlockCrusher bc=(BlockCrusher)block;

        if(bc.isAdvanced){
            bindTexture(rlA);
            model.renderCasing(1.0f);
            bindTexture(rlB);
            model.renderBelt(1.0f);
            bindTexture(rlC);
            model.renderBox(1.0f);
            bindTexture(rlD);
            model.renderGear(0.f, 1.0f);
        }
        else{
            bindTexture(rl2A);
            model.renderCasing(1.0f);
            bindTexture(rlB);
            model.renderBelt(1.0f);
            bindTexture(rl2C);
            model.renderBox(1.0f);
            bindTexture(rlD);
            model.renderGear(0.f, 1.0f);
        }

        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public int getRenderId() {
        return IR2.renderCrusherId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityCrusher te=(TileEntityCrusher)entity;
        float rate=te.getRenderingTicksRate();

        //アイテム
        EntityItem item=te.materialItem;
        if(item!=null && item.getEntityItem()!=null){
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float) x+0.5f, (float) y + f13+0.25f+0.45f-0.45f*rate, (float) z+0.5f);

            GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(item, 0,0,0, 0,0);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }

        //機械
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x+0.5f, (float)y + f13, (float)z+0.5f);
        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);
        GL11.glRotatef(180, 0, 0, 1);

        rotate(te.side);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);

        if(te.isAdvanced){
            bindTexture(rlA);
            model.renderCasing(1.0f);
            bindTexture(rlC);
            model.renderBox(1.0f);
            bindTexture(rlB);
            model.renderBelt(1.0f);
            bindTexture(rlD);
            model.renderGear(-rate, 1.0f);
        }
        else {
            bindTexture(rl2A);
            model.renderCasing(1.0f);
            bindTexture(rl2C);
            model.renderBox(1.0f);
            bindTexture(rlB);
            model.renderBelt(1.0f);
            bindTexture(rlD);
            model.renderGear(-rate, 1.0f);
        }

        GL11.glPopMatrix();
    }
}
