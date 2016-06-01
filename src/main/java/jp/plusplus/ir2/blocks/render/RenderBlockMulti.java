package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/07/01.
 */
public class RenderBlockMulti implements ISimpleBlockRenderingHandler {
    float f13=13.0f/16.0f;

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
        if(modelId==getRenderId()){

            int meta=world.getBlockMetadata(x,y,z);
            if((meta&8)!=0) return true; //マルチブロック機械の上の段にあるなら何も描画しない

            if((meta&4)==0){ //機械と繋がっていないのであれば通常通り
                renderer.setRenderBounds(0,0,0,1,1,1);
                renderer.renderStandardBlock(block, x,y,z);
                return true;
            }

            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMultiBlock){
                TileEntityMultiBlock tmu=((TileEntityMultiBlock) te);
                TileEntity tmp=tmu.getMachineTe();

                if(tmp instanceof TileEntityMachineBase){
                    TileEntityMachineBase tm=(TileEntityMachineBase)tmp;
                    if(tm.hasCarryingSide()) {
                        ForgeDirection cd = ForgeDirection.getOrientation(tm.sideCarrying);
                        Block b=tmp.getBlockType();

                        renderer.setOverrideBlockTexture(b.getIcon(-1, 0));
                        if (cd == ForgeDirection.DOWN) b.setBlockBounds(0.25f, -0.0625f, 0.25f, 0.75f, 0, 0.75f);
                        if (cd == ForgeDirection.UP) b.setBlockBounds(0.25f, 13.f/16.f, 0.25f, 0.75f, 13.f/16.f+0.0625f, 0.75f);
                        if (cd == ForgeDirection.WEST) b.setBlockBounds(-0.0625f, 0.25f, 0.25f, 0, 0.75f, 0.75f);
                        if (cd == ForgeDirection.EAST) b.setBlockBounds(1.0f, 0.25f, 0.25f, 1.0625f, 0.75f, 0.75f);
                        if (cd == ForgeDirection.SOUTH) b.setBlockBounds(0.25f, 0.25f, 1, 0.75f, 0.75f, 1.0625f);
                        if (cd == ForgeDirection.NORTH) b.setBlockBounds(0.25f, 0.25f, -0.0625f, 0.75f, 0.75f, 0);

                        renderer.setRenderBoundsFromBlock(b);
                        renderer.renderStandardBlock(b, x, y, z);
                        renderer.clearOverrideBlockTexture();
                        b.setBlockBounds(0, 0, 0, 1, 1, 1);
                    }
                }
            }

            block.setBlockBounds(0,0,0,1,1,1);
            renderer.setRenderBounds(0,0,0,1,f13,1);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0,0,0,1,1,1);


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
        return IR2.renderMultiId;
    }

}
