package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.ModelCrystalUnit;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import jp.plusplus.ir2.tileentities.TileEntityGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class RenderGenerator extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    private ModelCrystalUnit[] mcu=new ModelCrystalUnit[5];

    float f4=4.0f/16.0f;
    float f6=6.0f/16.0f;
    float f10=10.0f/16.0f;
    float f12=12.0f/16.0f;
    float f13=13.0f/16.0f;

    public RenderGenerator(){
        for(int i=0;i<mcu.length;i++) mcu[i]=new ModelCrystalUnit(i);
    }

    private void drawBlock(Block block, RenderBlocks renderer, int x, int y, int z, float bx, float by, float bz, float w, float h, float d){
        renderer.renderMinX=bx;
        renderer.renderMinY=by;
        renderer.renderMinZ=bz;
        renderer.renderMaxX=bx+w;
        renderer.renderMaxY=by+h;
        renderer.renderMaxZ=bz+d;
        block.setBlockBounds(bx, by, bz, bx+w, by+h, bz+d);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        IIcon tSide;
        IIcon tTop;
        IIcon tBottom;

        if (modelId == this.getRenderId()){
            tSide=block.getIcon(2,0);
            tTop=block.getIcon(1,0);
            tBottom=block.getIcon(0,0);

            Tessellator tessellator = Tessellator.instance;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderer.setRenderBoundsFromBlock(block);

            renderer.renderMaxY=f13;

            //下
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, tBottom);
            tessellator.draw();

            //上

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMaxX=renderer.renderMaxZ=f6;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinZ=f6;
            renderer.renderMaxZ=f10;
            renderer.renderMinX=0.0f;
            renderer.renderMaxX=f6;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinZ=f10;
            renderer.renderMaxZ=1.0f;
            renderer.renderMinX=0.0f;
            renderer.renderMaxX=f6;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            //上のくぼみ
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderMinX=renderer.renderMinZ=f6;
            renderer.renderMaxX=renderer.renderMaxZ=f10;
            renderer.renderMaxY=f12;
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderMinY=f12;
            renderer.renderMaxY=f13;
            renderer.renderMinX=renderer.renderMaxX=f6;
            renderer.renderFaceXPos(block, 0.0d, 0.0d, 0.0d, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderMinX=renderer.renderMaxX=f10;
            renderer.renderFaceXNeg(block, 0.0d, 0.0d, 0.0d, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderMinZ=renderer.renderMaxZ=f6;
            renderer.renderFaceZPos(block, 0.0d, 0.0d, 0.0d, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderMinZ=renderer.renderMaxZ=f10;
            renderer.renderFaceZNeg(block, 0.0d, 0.0d, 0.0d, tSide);
            tessellator.draw();

            renderer.renderMinX=renderer.renderMinZ=0.0f;
            renderer.renderMaxX=renderer.renderMaxZ=1.0f;
            renderer.renderMinY=0;
            renderer.renderMaxY=f13;

            //横
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, tSide);
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
            IIcon tSide = block.getIcon(2, 0);
            IIcon tTop = block.getIcon(1, 0);
            IIcon tBottom = block.getIcon(0, 0);
            Tessellator tessellator = Tessellator.instance;
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderMaxY = f13;

            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            int l = block.colorMultiplier(world, x, y, z);
            float f = (float) (l >> 16 & 255) / 255.0F;
            float f1 = (float) (l >> 8 & 255) / 255.0F;
            float f2 = (float) (l & 255) / 255.0F;
            float f3;
            if (EntityRenderer.anaglyphEnable) {
                f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }
            tessellator.setColorOpaque_F(f, f1, f2);

            //上
            renderer.renderMaxX=renderer.renderMaxZ=f6;
            renderer.renderFaceYPos(block, x, y, z, tTop);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderFaceYPos(block, x, y, z, tTop);
            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, x, y, z, tTop);

            renderer.renderMinZ=f6;
            renderer.renderMaxZ=f10;
            renderer.renderMinX=0.0f;
            renderer.renderMaxX=f6;
            renderer.renderFaceYPos(block, x, y, z, tTop);

            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, x, y, z, tTop);

            renderer.renderMinZ=f10;
            renderer.renderMaxZ=1.0f;
            renderer.renderMinX=0.0f;
            renderer.renderMaxX=f6;
            renderer.renderFaceYPos(block, x, y, z, tTop);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderFaceYPos(block, x, y, z, tTop);
            renderer.renderMinX=f10;
            renderer.renderMaxX=1.0f;
            renderer.renderFaceYPos(block, x, y, z, tTop);

            //上のくぼみ
            renderer.renderMinX=renderer.renderMinZ=f6;
            renderer.renderMaxX=renderer.renderMaxZ=f10;
            renderer.renderMaxY=f12;
            renderer.renderFaceYPos(block, x, y, z, tTop);

            renderer.renderMinY=f12;
            renderer.renderMaxY=f13;
            renderer.renderMinX=renderer.renderMaxX=f6;
            renderer.renderFaceXPos(block, x, y, z, tSide);
            renderer.renderMinX=renderer.renderMaxX=f10;
            renderer.renderFaceXNeg(block, x, y, z, tSide);
            renderer.renderMinX=f6;
            renderer.renderMaxX=f10;
            renderer.renderMinZ=renderer.renderMaxZ=f6;
            renderer.renderFaceZPos(block, x, y, z, tSide);
            renderer.renderMinZ=renderer.renderMaxZ=f10;
            renderer.renderFaceZNeg(block, x, y, z, tSide);

            renderer.renderMinX=renderer.renderMinZ=0.0f;
            renderer.renderMaxX=renderer.renderMaxZ=1.0f;
            renderer.renderMinY=0;
            renderer.renderMaxY=f13;

            //下
            //tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, x, y, z, tBottom);

            //横
            //tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, x, y, z, tSide);

            //tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, x, y, z, tSide);

            //tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, x, y, z, tSide);

            //tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, x, y, z, tSide);

            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);

            renderer.renderMinX = renderer.renderMinY = renderer.renderMinZ = 0;
            renderer.renderMaxX = renderer.renderMaxY = renderer.renderMaxZ = 1;

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
        return IR2.renderPGId;
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float p_147500_8_) {
        if(p_147500_1_ instanceof TileEntityGenerator){
            ItemStack unit=((TileEntityGenerator) p_147500_1_).getStackInSlot(0);
            if(unit!=null && unit.getItem() instanceof ItemCrystalUnit){
                ItemCrystalUnit cu=(ItemCrystalUnit)unit.getItem();

                GL11.glPushMatrix();
                GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
                float scale = 0.0625f;
                GL11.glScalef(scale,scale,scale);
                GL11.glRotatef(180,0,0,1);

                bindTexture(cu.getResourceLocation());
                cu.getModel().render(null, 0, 0, 0, 0, 0, 1.0f);

                GL11.glPopMatrix();
            }
        }
    }
}
