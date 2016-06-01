package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.IDirectional;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class RenderDirectional implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        IIcon tFront;
        IIcon tSide;
        IIcon tTop;
        IIcon tBottom;

        if (modelID == this.getRenderId()){
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
        if (modelId == this.getRenderId()) {
            TileEntity te=world.getTileEntity(x,y,z);
            if(!(te instanceof IDirectional))   return false;

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
        return IR2.renderDirectionalId;
    }
}
