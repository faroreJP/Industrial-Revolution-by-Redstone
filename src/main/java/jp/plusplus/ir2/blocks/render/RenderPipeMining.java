package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * Created by plusplus_F on 2015/02/17.
 */
public class RenderPipeMining implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int i, int i1, RenderBlocks renderBlocks) {

    }
    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId != this.getRenderId())  return false;

        float w=3.0F/16.0F;

        block.setBlockBounds(0.5F-w, 1.0F-access.getBlockMetadata(x,y,z)/15.0F, 0.5F-w, 0.5F+w, 1.0F, 0.5F+w);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(0, 0, 0, 1, 1, 1);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return false;
    }

    @Override
    public int getRenderId() {
        return IR2.renderPipeMiningId;
    }
}
