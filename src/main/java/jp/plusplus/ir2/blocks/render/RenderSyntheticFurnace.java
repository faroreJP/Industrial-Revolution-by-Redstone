package jp.plusplus.ir2.blocks.render;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockTransmitter;
import jp.plusplus.ir2.blocks.model.ModelChunkLoader;
import jp.plusplus.ir2.blocks.model.ModelSyntheticFurnace;
import jp.plusplus.ir2.blocks.model.ModelTransmitter;
import jp.plusplus.ir2.tileentities.TileEntitySyntheticFurnace;
import jp.plusplus.ir2.tileentities.TileEntityTransmitter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/08/16.
 */
public class RenderSyntheticFurnace extends RenderMachineBase {
    public static final ResourceLocation rl =new ResourceLocation(IR2.MODID+":textures/models/SyntheticFurnace.png");
    ModelSyntheticFurnace model=new ModelSyntheticFurnace();

    static final float f4=4.0f/16.0f;
    static final float f6=6.0f/16.0f;
    static final float f14=14.0f/16.0f;
    static final float f12=12.0f/16.0f;

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

        bindTexture(rl);
        model.render(null, 0,0,0,0,0,1.0f);
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
        return IR2.renderSyntheticFurnaceId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntitySyntheticFurnace te=(TileEntitySyntheticFurnace)entity;
        float rate=te.getRenderingTicksRate();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + f13, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        rotate(te.side);

        //本体の描画
        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);
        GL11.glPopMatrix();

        //
        if(te.isWorking()){
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef((float) x, (float) y + f13, (float) z);

            TessellatorWrapper.SetBlockRender(false);

            //なんかよく分からなかったので方角ごとに全4パターン
            if(te.side==3){
                TessellatorWrapper.DrawYPos(f4, f6, 0, 1, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, -1);
                TessellatorWrapper.DrawYPos(f4, f6, f4, 1, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(1, 0, 0);
                TessellatorWrapper.DrawYPos(0, f6, f4, f12, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, 1);
                TessellatorWrapper.DrawYPos(0, f6, 0, f12, f12, Blocks.lava.getIcon(0,0));
            }
            else if(te.side==2){
                TessellatorWrapper.DrawYPos(0, f6, f4, f12, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, 1);
                TessellatorWrapper.DrawYPos(0, f6, 0, f12, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(-1, 0, 0);
                TessellatorWrapper.DrawYPos(f4, f6, 0, 1, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, -1);
                TessellatorWrapper.DrawYPos(f4, f6, f4, 1, 1, Blocks.lava.getIcon(0, 0));
            }
            else if(te.side==4){
                TessellatorWrapper.DrawYPos(f4, f6, f4, 1, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, 1);
                TessellatorWrapper.DrawYPos(f4, f6, 0, 1, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(1, 0, 0);
                TessellatorWrapper.DrawYPos(0, f6, 0, f12, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, -1);
                TessellatorWrapper.DrawYPos(0, f6, f4, f12, 1, Blocks.lava.getIcon(0, 0));
            }
            else if(te.side==5){
                TessellatorWrapper.DrawYPos(0, f6, 0, f12, f12, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, -1);
                TessellatorWrapper.DrawYPos(0, f6, f4, f12, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(-1, 0, 0);
                TessellatorWrapper.DrawYPos(f4, f6, f4, 1, 1, Blocks.lava.getIcon(0, 0));
                GL11.glTranslatef(0, 0, 1);
                TessellatorWrapper.DrawYPos(f4, f6, 0, 1, f12, Blocks.lava.getIcon(0, 0));
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

    }
}
