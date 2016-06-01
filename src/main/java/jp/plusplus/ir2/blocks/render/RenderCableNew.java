package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.*;
import jp.plusplus.ir2.tileentities.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/06/26.
 */
public class RenderCableNew extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation rlCore=new ResourceLocation(IR2.MODID+":textures/models/CableCore.png");
    public static final ResourceLocation rlMachineOnly=new ResourceLocation(IR2.MODID+":textures/models/CableToMachineOnly.png");
    public static final ResourceLocation rlMachine=new ResourceLocation(IR2.MODID+":textures/models/CableToMachine.png");
    public static final ResourceLocation rlCable=new ResourceLocation(IR2.MODID+":textures/models/CableToCableSide.png");
    public static final ResourceLocation rlUp=new ResourceLocation(IR2.MODID+":textures/models/CableToCableUp.png");


    protected ModelCableCore[] modelCore;
    protected ModelCableToMachineOnly[] modelMachineOnly;
    protected ModelCableToMachine[] modelMachine;
    protected ModelCableToCableSide[] modelCableSide;
    protected ModelCableToCableUp[] modelCableUp;

    public RenderCableNew(){
        modelCore=new ModelCableCore[7];
        for(int i=0;i<modelCore.length;i++) modelCore[i]=new ModelCableCore(0, 4*i);

        modelMachineOnly=new ModelCableToMachineOnly[16];
        for(int i=0;i<modelMachineOnly.length;i++) modelMachineOnly[i]=new ModelCableToMachineOnly(i);

        modelMachine=new ModelCableToMachine[16];
        for(int i=0;i<modelMachine.length;i++) modelMachine[i]=new ModelCableToMachine(i);

        modelCableSide =new ModelCableToCableSide[16];
        for(int i=0;i< modelCableSide.length;i++) modelCableSide[i]=new ModelCableToCableSide(i);

        modelCableUp =new ModelCableToCableUp[16];
        for(int i=0;i< modelCableUp.length;i++) modelCableUp[i]=new ModelCableToCableUp(i);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()) return true;
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return IR2.renderCableId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        if(entity.getClass()!=TileEntityCable.class) return;
        TileEntityCable te=(TileEntityCable)entity;

        ModelCableCore mcc=modelCore[te.metal];
        ModelCableToCableSide mc2cs= modelCableSide[te.getBlockMetadata()];
        ModelCableToCableUp mc2cu=modelCableUp[te.blockMetadata];
        ModelCableToMachineOnly mc2mo=modelMachineOnly[te.blockMetadata];
        ModelCableToMachine mc2m=modelMachine[te.blockMetadata];

        //GL設定その1
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);

        if(te.connectMachineOne) {
            // 1つの機械のみと接続している。
            GL11.glRotatef(180, 0, 0, 1);

            if (te.connectState == 1) {
                //下
                bindTexture(rlCore);
                mcc.setColor(te.metal); //定格にあわせた色指定
                mcc.render(null, 0, 0, 0, 0, 0, 1.0f);
            } else if (te.connectState == 2) {
                //上
                bindTexture(rlUp);
                mc2cu.render(null,0,0,0,0,0,1.0f);
            } else {

                int ccc = (te.connectState >> 2);
                for (int i = 0; i < 4; i++) {
                    if ((ccc & 1) != 0) {
                        //その他、四方のいずれか

                        if (i == 2) {
                            GL11.glRotatef(90, 0, 1, 0);
                            bindTexture(rlMachineOnly);
                            mc2mo.render(null, 0, 0, 0, 0, 0, 1.0f);
                        }
                        else if (i == 3){
                            GL11.glRotatef(90, 0, -1, 0);
                            bindTexture(rlMachineOnly);
                            mc2mo.render(null, 0, 0, 0, 0, 0, 1.0f);
                        }
                        else if(i==0){
                            GL11.glRotatef(180, 0, 1, 0);
                            bindTexture(rlMachineOnly);
                            mc2mo.render(null, 0, 0, 0, 0, 0, 1.0f);
                        }
                        else{
                            bindTexture(rlMachineOnly);
                            mc2mo.render(null, 0, 0, 0, 0, 0, 1.0f);
                        }

                        break;
                    }
                    ccc=(ccc>>1);
                }
            }
        }
        else {
            // 1つのケーブル、複数のケーブルと機械に接続している。
            GL11.glRotatef(180, 0, 0, 1);

            //中心部分
            bindTexture(rlCore);
            mcc.setColor(te.metal); //定格にあわせた色指定
            mcc.render(null, 0, 0, 0, 0, 0, 1.0f);

            if ((te.connectState & 1) != 0) {
                //下
            }
            if ((te.connectState & 2) != 0) {
                //上
                bindTexture(rlUp);
                mc2cu.render(null,0,0,0,0,0,1.0f);
            }

            //その他、四方のいずれか

            int ccc = (te.connectState >> 2);
            for (int i = 0; i < 4; i++) {
                if ((ccc & 1) != 0) {
                    if (i == 2){
                        GL11.glRotatef(90, 0, 1, 0);
                        if(((te.connectStateToCable>>(i+2))&1)!=0){
                            bindTexture(rlCable);
                            mc2cs.render(null, 0,0,0,0,0,1.0f);
                        }
                        else{
                            bindTexture(rlMachine);
                            mc2m.render(null, 0, 0, 0, 0, 0, 1.0f);
                        }
                        GL11.glRotatef(-90, 0, 1, 0);
                    }
                    else if (i == 3){
                        GL11.glRotatef(90, 0, -1, 0);
                        if(((te.connectStateToCable>>(i+2))&1)!=0){
                            bindTexture(rlCable);
                            mc2cs.render(null, 0,0,0,0,0,1.0f);
                        }
                        else{
                            bindTexture(rlMachine);
                            mc2m.render(null, 0,0,0,0,0,1.0f);
                        }
                        GL11.glRotatef(-90, 0, -1, 0);
                    }
                    else if(i==0){
                        GL11.glRotatef(180, 0, 1, 0);
                        if(((te.connectStateToCable>>(i+2))&1)!=0){
                            bindTexture(rlCable);
                            mc2cs.render(null, 0,0,0,0,0,1.0f);
                        }
                        else{
                            bindTexture(rlMachine);
                            mc2m.render(null, 0,0,0,0,0,1.0f);
                        }
                        GL11.glRotatef(-180, 0, 1, 0);
                    }
                    else{
                        if(((te.connectStateToCable>>(i+2))&1)!=0){
                            bindTexture(rlCable);
                            mc2cs.render(null, 0,0,0,0,0,1.0f);
                        }
                        else{
                            bindTexture(rlMachine);
                            mc2m.render(null, 0,0,0,0,0,1.0f);
                        }
                    }
                }
                ccc=(ccc>>1);
            }

        }

        GL11.glPopMatrix();
    }
}
