package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.*;
import jp.plusplus.ir2.tileentities.TileEntityForRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/08/10.
 */
public class RenderCeilingLight implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation rl=new ResourceLocation(IR2.MODID+":textures/models/CeilingLight.png");

    ModelCeilingLight0 model0=new ModelCeilingLight0();
    ModelCeilingLight1 model1=new ModelCeilingLight1();
    ModelCeilingLight2 model2=new ModelCeilingLight2();
    ModelCeilingLight3 model3=new ModelCeilingLight3();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            renderer.minecraftRB.renderEngine.bindTexture(rl);

            GL11.glPushMatrix();
            GL11.glRotatef(180, 0, 0, 1);

            float scale = 0.0625f;
            GL11.glScalef(scale, scale, scale);

            int meta=(metadata&3);
            if(meta==0) model0.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==1) model1.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==2) model2.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==3) model3.render(null, 0, 0, 0, 0, 0, 1.0f);

            GL11.glPopMatrix();

            renderer.minecraftRB.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
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
        return IR2.renderCeilingLightId;
    }
}
