package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityPole;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/06/29.
 */
public class RenderPole extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    final float f3=3.0f/16.0f;
    final float f10 =10.0f/16.0f;
    final float f6=6.0f/16.0f;
    final float f13 =13.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            IIcon icon=block.getIcon(0, metadata);
            Tessellator tessellator = Tessellator.instance;

            GL11.glTranslatef(0f, 0f, 0f);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            float w = (8.0f / 16.0f) / 2.0f;
            block.setBlockBounds(0.5f - w, 0, 0.5f - w, 0.5f + w, 1, 0.5f + w);
            renderer.setRenderBoundsFromBlock(block);

            tessellator.startDrawingQuads();
            tessellator.setNormal(1F, 0F, 0F);
            renderer.renderFaceXPos(block, 0, 0, 0, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0F, 0F);
            renderer.renderFaceXNeg(block, 0, 0, 0, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 0F, 1F);
            renderer.renderFaceZPos(block, 0, 0, 0, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 0F, -1F);
            renderer.renderFaceZNeg(block, 0, 0, 0, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 1F, 0F);
            renderer.renderFaceYPos(block, 0, 0, 0, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, -1F, 0F);
            renderer.renderFaceYNeg(block, 0, 0, 0, icon);
            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            float w = (6.0f / 16.0f) / 2.0f;
            block.setBlockBounds(0.5f - w, 0, 0.5f - w, 0.5f + w, 1, 0.5f + w);
            renderer.setOverrideBlockTexture(block.getIcon(0, world.getBlockMetadata(x,y,z)));
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();

            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return IR2.renderPoleId;
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float p_147500_8_) {
        TileEntityPole te=(TileEntityPole)p_147500_1_;

        if((te.connectState&0x3c)!=0){
            bindTexture(TextureMap.locationBlocksTexture);

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            //GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glScalef(1.0f,1.0f,1.0f);
            GL11.glRotatef(0,0,1,0);

            IIcon icon=te.getBlockType().getIcon(0, te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord));
            //icon=te.getBlockType().getIcon(0, 0);
            TessellatorWrapper.SetBlockRender(false);

            if((te.connectState&4)!=0) TessellatorWrapper.DrawAllFaces(f6,f6,0, f10,f10,5.0f/16.0f, icon);
            if((te.connectState&8)!=0) TessellatorWrapper.DrawAllFaces(f6,f6,11.0f/16.0f, f10,f10,1, icon);
            if((te.connectState&16)!=0) TessellatorWrapper.DrawAllFaces(0,f6,f6, 5.0f/16.0f,f10,f10, icon);
            if((te.connectState&32)!=0) TessellatorWrapper.DrawAllFaces(11.0f/16.0f,f6,f6, 1,f10,f10, icon);

            /*
            TessellatorWrapper.DrawXPos(1, f6, 0, f10, 1, icon);
            TessellatorWrapper.DrawXNeg(0, f6, 0, f10, 1, icon);
            TessellatorWrapper.DrawYPos(0, f10, 0, 1, 1, icon);
            TessellatorWrapper.DrawYNeg(0, f6, 0, 1, 1, icon);
            TessellatorWrapper.DrawZPos(0, f6, 1, 1, f10, icon);
            TessellatorWrapper.DrawZNeg(0, f6, 0, 1, f10, icon);
            */

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }
}
