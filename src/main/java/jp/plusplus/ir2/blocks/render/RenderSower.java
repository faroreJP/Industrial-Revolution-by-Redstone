package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.ModelMachineSpinning;
import jp.plusplus.ir2.blocks.model.ModelSower;
import jp.plusplus.ir2.tileentities.TileEntitySower;
import jp.plusplus.ir2.tileentities.TileEntitySpinning;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/07/12.
 */
public class RenderSower extends RenderMachineBase {
    public static final ResourceLocation rl=new ResourceLocation(IR2.MODID+":textures/models/Sower.png");
    ModelSower model=new ModelSower();

    float f13=13.0f/16.0f;
    float f5=5.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        if(modelId!=getRenderId()) return;
        //super.renderInventoryBlock(block, metadata, modelId, renderer);

        GL11.glPushMatrix();
        //GL11.glTranslatef(0.125f, -0.125f, -0.125f - 0.0625f);
        GL11.glTranslatef(0.2f,0,-0.15f);

        float scaleEx=0.7f;
        GL11.glScalef(scaleEx, scaleEx, scaleEx);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 0);
        RenderMachineBase.renderCasing(block, renderer, 0, 0, 1);

        GL11.glTranslatef(0.5f, f13, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        //GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);
        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        model.renderCatapult(0,1f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public int getRenderId() {
        return IR2.renderSowerId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + f13, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        //GL11.glRotatef(90, 0, 1, 0);

        TileEntitySower te=(TileEntitySower)entity;

        rotate(te.side);
        GL11.glTranslatef(0, 0, -2*0.0625f);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);

        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        model.renderCatapult(te.getDrawTicksRate(), 1.f);

        GL11.glPopMatrix();
    }
}
