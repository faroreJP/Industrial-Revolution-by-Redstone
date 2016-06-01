package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.TileEntityFountain;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/06/28.
 */
public class RenderFountain extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    float f899=8.99f/16.0f;
    float f099=0.99f;
    float f001=0.01f;
    float f7=7.00f/16.0f;
    float f9=9.00f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        IIcon tFront;
        IIcon tSide;
        IIcon tTop;
        IIcon tBottom;

        if (modelId == this.getRenderId()){
            tFront=block.getIcon(2, 2);
            tSide=block.getIcon(2,0);
            tTop=block.getIcon(1,0);
            tBottom=block.getIcon(0,0);

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
        if(modelId==getRenderId()){

            //ブロック自体の描画
            TileEntity te=world.getTileEntity(x, y, z);
            if(te instanceof TileEntityMachineBase){
                TileEntityMachineBase tm=(TileEntityMachineBase)te;
                if(tm.hasCarryingSide()){
                    ForgeDirection cd=ForgeDirection.getOrientation(tm.sideCarrying);
                    if(!world.isSideSolid(x,y,z,cd,false)){
                        renderer.setOverrideBlockTexture(block.getIcon(-1, 0));
                        if(cd==ForgeDirection.DOWN) block.setBlockBounds(0.25f, -0.0625f, 0.25f, 0.75f, 0, 0.75f);
                        if(cd==ForgeDirection.UP) block.setBlockBounds(0.25f, 1, 0.25f, 0.75f, 1.0625f, 0.75f);
                        if(cd==ForgeDirection.WEST) block.setBlockBounds(-0.0625f, 0.25f, 0.25f, 0, 0.75f, 0.75f);
                        if(cd==ForgeDirection.EAST) block.setBlockBounds(1.0f, 0.25f, 0.25f, 1.0625f, 0.75f, 0.75f);
                        if(cd==ForgeDirection.SOUTH) block.setBlockBounds(0.25f, 0.25f, 1, 0.75f, 0.75f, 1.0625f);
                        if(cd==ForgeDirection.NORTH) block.setBlockBounds(0.25f, 0.25f, -0.0625f, 0.75f, 0.75f, 0);

                        renderer.setRenderBoundsFromBlock(block);
                        renderer.renderStandardBlock(block, x, y, z);
                        renderer.clearOverrideBlockTexture();
                    }
                }
            }

            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            Tessellator tessellator = Tessellator.instance;

            tessellator.setNormal(0.0F, 1F, 0.0F);
            renderer.renderFaceYPos(block, x, y-1.0+f001, z, block.getIcon(0,0));

            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, x, y+f899, z, block.getIcon(0,0));


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
        return IR2.renderFountainId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        TileEntityFountain te=(TileEntityFountain)entity;

        IIcon icon=te.getFluidIcon();
        if(icon==null) return;
        bindTexture(TextureMap.locationBlocksTexture);


        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        //GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        //なんでもやんは一面描画するメソッド作ってないの？？？？？？？
        TessellatorWrapper.SetBlockRender(false);

        float height=f899*((float)te.tank.getFluidAmount()/(float)te.tank.getCapacity());
        //溜まった水
        if(height>0){
            TessellatorWrapper.DrawXPos(f099, f001, f001, height, f099, icon);
            TessellatorWrapper.DrawXNeg(f001, f001, f001, height, f099, icon);
            TessellatorWrapper.DrawZPos(f001, f001, f099, f099, height, icon);
            TessellatorWrapper.DrawZNeg(f001, f001, f001, f099, height, icon);

            if(height<f899){
                TessellatorWrapper.DrawYPos(f001, height, f001, f099, f099, icon);
            }
        }

        //落ちてくる水
        if(height<=0) height=f001;
        if(te.lastWorkingState && height<f899){
            TessellatorWrapper.DrawXPos(f9, height, f7, f899, f9, icon);
            TessellatorWrapper.DrawXNeg(f7, height, f7, f899, f9, icon);
            TessellatorWrapper.DrawZPos(f7, height, f9, f9, f899, icon);
            TessellatorWrapper.DrawZNeg(f7, height, f7, f9, f899, icon);
        }


        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
