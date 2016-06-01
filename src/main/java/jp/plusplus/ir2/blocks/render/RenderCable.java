package jp.plusplus.ir2.blocks.render;

/**
 * Created by plusplus_F on 2015/01/31.
 */

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCable;
import jp.plusplus.ir2.tileentities.*;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;


public class RenderCable implements ISimpleBlockRenderingHandler{
    public static double w=0.375D;
    public static double[][] offset={
            {0.5D - w / 2, 0.0D, 0.5D - w / 2, 0.5D + w / 2, 0.5D - w / 2, 0.5D + w / 2},
            {0.5D - w / 2, 0.5D + w / 2, 0.5D - w / 2, 0.5D + w / 2, 1.0D, 0.5D + w / 2},
            {0.5D - w / 2, 0.5D - w / 2, 0.0D, 0.5D + w / 2, 0.5D + w / 2, 0.5D - w / 2},
            {0.5D - w / 2, 0.5D - w / 2, 0.5D + w / 2, 0.5D + w / 2, 0.5D + w / 2, 1.0D},
            {0.0D, 0.5D - w / 2, 0.5D - w / 2, 0.5D - w / 2, 0.5D + w / 2, 0.5D + w / 2},
            {0.5F + w / 2, 0.5F - w / 2, 0.5F - w / 2, 1.0F, 0.5F + w / 2, 0.5F + w / 2}
    };

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

    }
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == this.getRenderId()) {
            TileEntity e = world.getTileEntity(x, y, z);
            if (e instanceof TileEntityCable) {
                boolean isSorting=e instanceof TileEntityPipeSorting;
                boolean hasMarker=(e instanceof TileEntityPipeOneWay) || (e instanceof TileEntityPipeExtractor) || (e instanceof TileEntityPipeFluidExtractor);
                byte c = ((TileEntityCable) e).connectState;
                int m=world.getBlockMetadata(x, y, z)&7;
                BlockCable cable=(BlockCable)block;
                float w = 0.375F;

                if(isSorting) {
                    renderer.setOverrideBlockTexture(cable.getIconSide(1));
                    block.setBlockBounds(0.5F - w / 2, 0.5F - w / 2, 0.5F - w / 2, 0.5F + w / 2, 0.5F + w / 2, 0.5F + w / 2);
                    renderer.setRenderBoundsFromBlock(block);
                    renderer.renderStandardBlock(block, x, y, z);
                    renderer.clearOverrideBlockTexture();
                }
                else{
                    block.setBlockBounds(0.5F - w / 2, 0.5F - w / 2, 0.5F - w / 2, 0.5F + w / 2, 0.5F + w / 2, 0.5F + w / 2);
                    renderer.setRenderBoundsFromBlock(block);
                    renderer.renderStandardBlock(block, x, y, z);
                }

                //no connecting
                if (c == 0) {
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    renderer.setRenderBoundsFromBlock(block);
                    renderer.clearOverrideBlockTexture();
                    return true;
                }

                if(!(e instanceof TileEntityPipeBase) && !(e instanceof TileEntityPipeFluid)) {
                    renderer.setOverrideBlockTexture(cable.getIconSide(world.getBlockMetadata(x, y, z)));
                }
                for(int i=1, j=0;j<6;j++){
                    if((c&i)!=0){
                        if(hasMarker && m==j){
                            renderer.setOverrideBlockTexture(cable.getIconSide(1));
                            renderer.setRenderBounds(offset[j][0], offset[j][1],offset[j][2],offset[j][3],offset[j][4],offset[j][5]);
                            renderer.renderStandardBlock(block, x, y, z);
                            renderer.clearOverrideBlockTexture();
                        }
                        else{
                            renderer.setRenderBounds(offset[j][0], offset[j][1],offset[j][2],offset[j][3],offset[j][4],offset[j][5]);
                            renderer.renderStandardBlock(block, x, y, z);
                        }
                    }
                    i=(i<<1);
                }

                renderer.clearOverrideBlockTexture();
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderer.setRenderBoundsFromBlock(block);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return false;
    }

    @Override
    public int getRenderId() {
        return IR2.renderCableId;
    }


}
