package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityAutoSpawner;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/06/28.
 */
public class RenderSpawner extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    float f001=0.01f;
    float f099=0.99f;
    float f13=13.0f/16.0f;


    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        IIcon icon;

        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(0f,-0.25f,0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);

        //------------------------ブロック---------------------------
        renderer.setRenderBounds(0, 0, 0, 1, f13, 1);
        //横
        icon=block.getIcon(2,0);

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

        //上
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
        renderer.renderFaceYPos(block, 0, 0, 0, block.getIcon(1,0));
        tessellator.draw();

        //下
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(0,0));
        tessellator.draw();

        //--------------------------タンク------------------------
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);

        //横
        icon=block.getIcon(-1,0);

        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);
        renderer.renderFaceXPos(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        renderer.renderFaceXNeg(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        renderer.renderFaceZPos(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        renderer.renderFaceZNeg(block, 0, f13, 0, icon);
        tessellator.draw();

        //上
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
        renderer.renderFaceYPos(block, 0, f13, 0, block.getIcon(-2, 0));
        tessellator.draw();

        //----------------------------------液体-----------------------------
        renderer.setRenderBounds(f001, f001, f001, f099,f099,f099);
        icon=block.getIcon(-3,0);

        GL11.glPushMatrix();
        //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);
        renderer.renderFaceXPos(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        renderer.renderFaceXNeg(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        renderer.renderFaceZPos(block, 0, f13, 0, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        renderer.renderFaceZNeg(block, 0, f13, 0, icon);
        tessellator.draw();

        //GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        //GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_NONE);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1,1,1,1);
        GL11.glPopMatrix();

        GL11.glScalef(1f, 1f, 1f);
        GL11.glTranslatef(0f, 0.25f, 0f);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            renderer.setRenderBounds(0,0,0,1,f13,1);
            renderer.renderStandardBlock(block, x,y,z);

            //
            Tessellator tessellator = Tessellator.instance;

            IIcon icon=block.getIcon(-1, 0);
            renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
            tessellator.setNormal(1F, 0F, 0F);
            renderer.renderFaceXPos(block, x, y + f13, z, icon);
            tessellator.setNormal(-1F, 0F, 0F);
            renderer.renderFaceXNeg(block, x, y + f13, z, icon);

            /*
            tessellator.setNormal(0F, 1F, 0F);
            renderer.renderFaceYPos(block, x, y + f13, z, icon);
            tessellator.setNormal(0F, -1F, 0F);
            renderer.renderFaceYNeg(block, x, y + f13, z, icon);
            */

            tessellator.setNormal(0F, 0F, 1F);
            renderer.renderFaceZPos(block, x, y + f13, z, icon);
            tessellator.setNormal(0F, 0F, -1F);
            renderer.renderFaceZNeg(block, x, y + f13, z, icon);

            tessellator.setNormal(0F, 1F, 0F);
            renderer.renderFaceYPos(block, x, y + f13, z, block.getIcon(-2, 0));

        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return IR2.renderSpawnerId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityAutoSpawner te = (TileEntityAutoSpawner) entity;

        //----------------------------素材アイテム--------------------------------
        EntityItem item=te.nextStack;
        if(item!=null && item.getEntityItem()!=null){
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float) x+0.5f, (float) y + f13+0.225f+0.05f*MathHelper.sin(360.0f*te.itemTicks/2560.0f), (float) z+0.5f);

            GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(item, 0,0,0, 0,0);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }

        //-----------------------------タンク内の液体-----------------------------------
        IIcon icon = te.getBlockType().getIcon(-3, 0);
        if (icon == null) return;
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        GL11.glTranslatef((float) x, (float) y + f13, (float) z);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        TessellatorWrapper.SetBlockRender(false);
        TessellatorWrapper.DrawXPos(f099, f001, f001, f099, f099, icon);
        TessellatorWrapper.DrawXNeg(f001, f001, f001, f099, f099, icon);
        TessellatorWrapper.DrawZPos(f001, f001, f099, f099, f099, icon);
        TessellatorWrapper.DrawZNeg(f001, f001, f001, f099, f099, icon);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

    }
}
