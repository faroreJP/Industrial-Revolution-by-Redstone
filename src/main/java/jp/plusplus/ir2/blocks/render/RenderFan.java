package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.model.*;
import jp.plusplus.ir2.tileentities.TileEntityForRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/08/10.
 */
public class RenderFan extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation locationFan =new ResourceLocation(IR2.MODID+":textures/models/Fan.png");
    public static final ResourceLocation locationCeilingLight=new ResourceLocation(IR2.MODID+":textures/models/CeilingLight.png");
    ModelFan modelFan =new ModelFan();

    ModelCeilingLight0 modelCL0 =new ModelCeilingLight0();
    ModelCeilingLight1 modelCL1 =new ModelCeilingLight1();
    ModelCeilingLight2 modelCL2 =new ModelCeilingLight2();
    ModelCeilingLight3 modelCL3 =new ModelCeilingLight3();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
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
        return false;
    }

    @Override
    public int getRenderId() {
        return IR2.renderFanId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        Block b=te.getBlockType();

        if(b== BlockCore.fan){
            bindTexture(locationFan);

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
            GL11.glRotatef(180, 0, 0, 1);

            float scale = 0.0625f;
            GL11.glScalef(scale, scale, scale);

            modelFan.render(null, 0, 0, 0, 0, 0, 1.0f);
            if(te instanceof TileEntityForRender){
                GL11.glRotatef(30*360.0f*((TileEntityForRender) te).renderingTicks/TileEntityForRender.MAX_TICKS, 0, 1, 0);
            }
            modelFan.renderBlade(1.0f);

            GL11.glPopMatrix();

            bindTexture(TextureMap.locationBlocksTexture);
        }
        else if(b==BlockCore.lightCeiling){
            bindTexture(locationCeilingLight);

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
            GL11.glRotatef(180, 0, 0, 1);

            float scale = 0.0625f;
            GL11.glScalef(scale, scale, scale);

            int meta=te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
            if((meta&4)!=0){
                GL11.glRotatef(90.0f, 0, 1, 0);
            }
            meta=(meta&3);
            if(meta==0) modelCL0.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==1) modelCL1.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==2) modelCL2.render(null, 0, 0, 0, 0, 0, 1.0f);
            else if(meta==3) modelCL3.render(null, 0, 0, 0, 0, 0, 1.0f);

            GL11.glPopMatrix();

            bindTexture(TextureMap.locationBlocksTexture);
        }
    }
}
