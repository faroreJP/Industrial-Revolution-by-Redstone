package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/07/02.
 * マルチブロックからなる機械のレンダラ基底
 * このクラスでは筐体部分しか描画しない。
 */
public abstract class RenderMachineBase extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{
    protected float f001=0.01f;
    protected float f099=0.99f;
    protected float f13=13.0f/16.0f;
    protected float f5=5.0f/16.0f;

    public static void renderCasing(Block block, RenderBlocks renderer, int x, int y, int z) {
        IIcon icon;

        Tessellator tessellator = Tessellator.instance;

        /*
        GL11.glTranslatef(0f, -0.25f, 0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        */

        //------------------------ブロック---------------------------
        renderer.setRenderBounds(0, 0, 0, 1, 13.0f / 16.0f, 1);
        //横
        icon = block.getIcon(2, 0);

        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);
        renderer.renderFaceXPos(block, x, y, z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        renderer.renderFaceXNeg(block, x, y, z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        renderer.renderFaceZPos(block, x, y, z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        renderer.renderFaceZNeg(block, x, y, z, icon);
        tessellator.draw();

        //上
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
        renderer.renderFaceYPos(block, x, y, z, block.getIcon(1, 0));
        tessellator.draw();

        //下
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);
        renderer.renderFaceYNeg(block, x, y, z, block.getIcon(0, 0));
        tessellator.draw();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        IIcon icon;

        Tessellator tessellator = Tessellator.instance;

        /*
        GL11.glTranslatef(0f, -0.25f, 0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        */

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

        /*
        GL11.glTranslatef(0f, 0.25f, 0f);
        GL11.glScalef(1f, 1f, 1f);
        */
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMachineBase){
                TileEntityMachineBase tm=(TileEntityMachineBase)te;
                if(tm.hasCarryingSide()) {
                    ForgeDirection cd = ForgeDirection.getOrientation(tm.sideCarrying);
                    renderer.setOverrideBlockTexture(block.getIcon(-1, 0));
                    if (cd == ForgeDirection.DOWN) block.setBlockBounds(0.25f, -0.0625f, 0.25f, 0.75f, 0, 0.75f);
                    if (cd == ForgeDirection.UP) block.setBlockBounds(0.25f, 13.f/16.f, 0.25f, 0.75f, 13.f/16.f+0.0625f, 0.75f);
                    if (cd == ForgeDirection.WEST) block.setBlockBounds(-0.0625f, 0.25f, 0.25f, 0, 0.75f, 0.75f);
                    if (cd == ForgeDirection.EAST) block.setBlockBounds(1.0f, 0.25f, 0.25f, 1.0625f, 0.75f, 0.75f);
                    if (cd == ForgeDirection.SOUTH) block.setBlockBounds(0.25f, 0.25f, 1, 0.75f, 0.75f, 1.0625f);
                    if (cd == ForgeDirection.NORTH) block.setBlockBounds(0.25f, 0.25f, -0.0625f, 0.75f, 0.75f, 0);

                    renderer.setRenderBoundsFromBlock(block);
                    renderer.renderStandardBlock(block, x, y, z);
                    renderer.clearOverrideBlockTexture();
                }
            }

            block.setBlockBounds(0,0,0,1,1,1);
            renderer.setRenderBounds(0,0,0,1,f13,1);
            renderer.renderStandardBlock(block, x,y, z);
            renderer.setRenderBounds(0,0,0,1,1,1);

            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    protected void rotate(int side){
        switch(side){
            case 4:
                GL11.glRotatef(90, 0, -1, 0);
                break;
            case 5:
                GL11.glRotatef(90, 0, 1, 0);
                break;
            case 3:
                GL11.glRotatef(180, 0, 1, 0);
                break;
            default:
                break;
        }
    }
}
