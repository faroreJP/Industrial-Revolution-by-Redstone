package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityFountain;
import jp.plusplus.ir2.tileentities.TileEntityTank;
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
 * Created by plusplus_F on 2015/05/18.
 */
public class RenderFluidTank extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    float f099=0.99f;
    float f001=0.01f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        IIcon tFront;
        IIcon tSide;
        IIcon tTop;
        IIcon tBottom;

        if (modelID == this.getRenderId()) {
            tFront = block.getIcon(2, 2);
            tSide = block.getIcon(2, 0);
            tTop = block.getIcon(1, 0);
            tBottom = block.getIcon(0, 0);

            Tessellator tessellator = Tessellator.instance;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderer.setRenderBoundsFromBlock(block);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, tBottom);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, tFront);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == this.getRenderId()) {
            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            Tessellator tessellator = Tessellator.instance;

            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, x, y + 1 - f001, z, block.getIcon(0, 0));

            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, x, y - 1 + f001, z, block.getIcon(0, 0));

            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return true;
    }

    @Override
    public int getRenderId() {
        return IR2.renderFluidTankId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityTank te = (TileEntityTank) entity;

        IIcon icon = te.getFluidIcon();
        if (icon == null) return;
        bindTexture(TextureMap.locationBlocksTexture);

        //溜まった水の描画
        float a=(float) te.tank.getFluidAmount();
        if(a>0) {
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

            //高さを決める
            float height = f099 * (a / (float) te.tank.getCapacity());
            if(height<f001) height=f001;

            if (height > 0) {
                TessellatorWrapper.SetBlockRender(false);
                TessellatorWrapper.DrawXPos(f099, f001, f001, height, f099, icon);
                TessellatorWrapper.DrawXNeg(f001, f001, f001, height, f099, icon);
                TessellatorWrapper.DrawZPos(f001, f001, f099, f099, height, icon);
                TessellatorWrapper.DrawZNeg(f001, f001, f001, f099, height, icon);

                if (height < f099) {
                    TessellatorWrapper.DrawYPos(f001, height, f001, f099, f099, icon);
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

}
