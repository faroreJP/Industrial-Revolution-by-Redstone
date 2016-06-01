package jp.plusplus.ir2.blocks.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IPipe;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockPipe;
import jp.plusplus.ir2.blocks.model.ModelPipe;
import jp.plusplus.ir2.blocks.model.ModelPipeCenter;
import jp.plusplus.ir2.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Iterator;

/**
 * Created by plusplus_F on 2015/07/10.
 * パイプの描画クラス。ちょっとでも軽くなったらいいなーとか思ってる
 */
public class RenderPipe extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    private static final float f7=7f/16f;
    private static final float f9=9f/16f;
    private static final float f5=5f/16f;
    private static final float f11=11f/16f;
    private static final float f5p1=5f/16f+0.001f;
    private static final float f11m1=11f/16f-0.001f;
    private static final float f599=5.98f/16f;

    public static final float[][] OFFSET={
            {0,-0.34375f,0}, {0,0.34375f,0},
            {0,0,-0.34375f}, {0,0,0.34375f},
            {-0.34375f,0,0}, {0.34375f,0,0}
    };
    public static final float[][] ROTATION={
            {90,-1,0,0}, {90,1,0,0},
            {180,0,1,0},{0,0,0,1},
            {90,0,-1,0},{90,0,1,0}
    };
    public static final int[][] NORMAL={
            {0,-1,0},{0,1,0},
            {0,0,-1},{0,0,1},
            {-1,0,0},{1,0,0}
    };
    public static final float[][] BOX_POS={
            {f5p1,0,f11m1,f5p1},{f5p1,f11m1,f11m1,1},
            {0,f5p1,f5p1,f11m1},{f11m1,f5p1,1,f11m1}
    };
    public static final float[][] BOX_POS2={
            {0,f11m1}, {f5p1,1},
    };

    public ResourceLocation rl;

    protected ModelPipe[] modelPipe;
    protected ModelPipeCenter[] modelCenter;
    protected static final int INDEX_EXTRACTOR=7;
    protected static final int INDEX_ONEWAY=8;
    protected static final int INDEX_VOID=8;
    protected static final int INDEX_SORTER=7;



    public RenderPipe(){
        modelPipe=new ModelPipe[18];
        for(int i=0;i<modelPipe.length;i++) modelPipe[i]=new ModelPipe(22*(i/5), 11*(i%5));

        modelCenter=new ModelPipeCenter[9];
        for(int i=0;i<modelCenter.length;i++) modelCenter[i]=new ModelPipeCenter(24*(i/5), 55+12*(i%5));

        if(IR2.useOldPipe) rl=new ResourceLocation(IR2.MODID+":textures/models/PipeOld.png");
        else rl=new ResourceLocation(IR2.MODID+":textures/models/Pipe.png");
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    //なにもしない
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){

            /*
            //昔のパイプ
            if(IR2.useOldPipe){
                TileEntity e = world.getTileEntity(x, y, z);
                if (e instanceof IPipe) {
                    IPipe p=(IPipe)e;
                    IPipe.PipeType pt=p.getPipeType();

                    boolean isSorting=(pt== IPipe.PipeType.SORTER);
                    boolean hasMarker=(pt== IPipe.PipeType.ONEWAY || pt== IPipe.PipeType.EXTRACTOR || pt==IPipe.PipeType.SORTER);
                    byte c = p.getConnectState();
                    byte dc = p.getConnectStateDisable();
                    int m=p.getPipeSide();
                    BlockPipe pipe=(BlockPipe)block;
                    IIcon icon=pipe.getIcon(0,0);
                    float w = 0.375F;


                    //中心部分の描画
                    block.setBlockBounds(0.5F - w / 2, 0.5F - w / 2, 0.5F - w / 2, 0.5F + w / 2, 0.5F + w / 2, 0.5F + w / 2);
                    renderer.setRenderBoundsFromBlock(block);
                    for(int i=0;i<6;i++){
                        if((dc&(1<<i))!=0){
                            //接続不可面
                            renderer.setOverrideBlockTexture(pipe.getIconSide(-1));
                        }
                        else if(isSorting){
                            //ソートパイプ
                            renderer.setOverrideBlockTexture(pipe.getIconSide(1));
                        }

                        if(i==0) renderer.renderFaceYNeg(block, x,y,z,icon);
                        else if(i==1) renderer.renderFaceYPos(block, x, y, z, icon);
                        else if(i==2) renderer.renderFaceXNeg(block, x,y,z,icon);
                        else if(i==3) renderer.renderFaceXPos(block, x,y,z,icon);
                        else if(i==4) renderer.renderFaceZNeg(block, x,y,z,icon);
                        else if(i==5) renderer.renderFaceZPos(block, x,y,z,icon);
                    }

                    //どこにも繋がってないならここで終了
                    if (c == 0) {
                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        renderer.setRenderBoundsFromBlock(block);
                        renderer.clearOverrideBlockTexture();
                        return true;
                    }

                    //各面の描画
                    for(int i=1, j=0;j<6;j++){
                        if((c&i)!=0){
                            if(hasMarker && m==j){
                                renderer.setOverrideBlockTexture(pipe.getIconSide(1));
                                renderer.setRenderBounds(OFFSET[j][0], OFFSET[j][1],OFFSET[j][2],OFFSET[j][3],OFFSET[j][4],OFFSET[j][5]);
                                renderer.renderStandardBlock(block, x, y, z);
                                renderer.clearOverrideBlockTexture();
                            }
                            else{
                                renderer.setRenderBounds(OFFSET[j][0], OFFSET[j][1],OFFSET[j][2],OFFSET[j][3],OFFSET[j][4],OFFSET[j][5]);
                                renderer.renderStandardBlock(block, x, y, z);
                            }
                        }
                        i=(i<<1);
                    }

                    renderer.clearOverrideBlockTexture();
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    renderer.setRenderBoundsFromBlock(block);
                }
            }
            */

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
        return IR2.renderPipeId;
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float p_147500_8_) {
        if (!(p_147500_1_ instanceof IPipe)) return;
        IPipe te = (IPipe) p_147500_1_;

        IPipe.PipeType type=te.getPipeType();
        boolean isNormal=(type== IPipe.PipeType.NORMAL || type== IPipe.PipeType.VOID);
        int side=te.getPipeSide();
        int tex=te.getTextureIndex();

        //パケットの描画
        if(IR2.useOldPipe){

        }
        else if(te instanceof TileEntityPipeBase){
            float d=0;
            int dir;
            EntityItem dummy=((TileEntityPipeBase) te).getEntityItem();

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
            GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.5f, 0.5f, 0.5f);

            Iterator<PacketItemStack> it=te.getPackets();
            while(it.hasNext()){
                PacketItemStack p=it.next();
                dummy.setEntityItemStack(p.getItemStack());

                //距離と角度
                dir=p.getDirection();
                if(p.isReversed()) d=1f*(p.getTicksRate()-1.0f);
                else d=1f*p.getTicksRate();

                RenderManager.instance.renderEntityWithPosYaw(dummy, d*NORMAL[dir][0],d*NORMAL[dir][1]-0.3f,d*NORMAL[dir][2], 0,0);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
        else if(te instanceof TileEntityPipeFluid){
            bindTexture(TextureMap.locationBlocksTexture);
            TessellatorWrapper.SetBlockRender(false);
            TileEntityPipeFluid tf=(TileEntityPipeFluid)te;
            IIcon ic;
            float h;

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

            //上下
            for(int i=0;i<2;i++){
                ic=tf.getFluidIcon(i);
                if(ic!=null) {
                    h=tf.getDrawTicksScaled(i, 0.5f);
                    TessellatorWrapper.DrawAllFaces(0.5f-h*f5p1, BOX_POS2[i][0], 0.5f-h*f5p1, 0.5f+h*f5p1, BOX_POS2[i][1], 0.5f+h*f5p1, ic);
                }
            }

            //南北
            for(int i=0;i<2;i++){
                ic=tf.getFluidIcon(i+2);
                if(ic!=null) {
                    h=tf.getDrawTicksScaled(i+2, 0.5f);
                    TessellatorWrapper.DrawAllFaces(0.5f-h*f5p1, 0.5f-h*f5p1, BOX_POS2[i][0], 0.5f+h*f5p1, 0.5f+h*f5p1, BOX_POS2[i][1], ic);
                }
            }
            //東西
            for(int i=0;i<2;i++){
                ic=tf.getFluidIcon(i+4);
                if(ic!=null) {
                    h=tf.getDrawTicksScaled(i+4, 0.5f);
                    TessellatorWrapper.DrawAllFaces(BOX_POS2[i][0], 0.5f-h*f5p1, 0.5f-h*f5p1, BOX_POS2[i][1], 0.5f+h*f5p1, 0.5f+h*f5p1, ic);
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

        bindTexture(rl);
        //中心部分の描画
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glScalef(0.0625f,0.0625f,0.0625f);
        if(type== IPipe.PipeType.SORTER){
            modelCenter[INDEX_SORTER].render(null, 0, 0, 0, 0, 0, 1.0f);
        }
        else if(type== IPipe.PipeType.VOID){
            modelCenter[INDEX_VOID].render(null, 0, 0, 0, 0, 0, 1.0f);
        }
        else{
            modelCenter[te.getTextureCenterIndex()].render(null, 0, 0, 0, 0, 0, 1.0f);
        }
        GL11.glPopMatrix();

        //6方向に対しなんか描画
        byte con = te.getConnectState();
        if(con!=0){
            for (int i = 0, k = 1; i < 6; i++) {
                if ((con & k) != 0) {
                    GL11.glPushMatrix();

                    GL11.glTranslatef((float) x + 0.5f + OFFSET[i][0], (float) y + 0.5f + OFFSET[i][1], (float) z + 0.5f + OFFSET[i][2]);
                    GL11.glRotatef(180,0,1,0);
                    GL11.glRotatef(ROTATION[i][0], ROTATION[i][1], ROTATION[i][2], ROTATION[i][3]);
                    GL11.glScalef(0.0625f,0.0625f,0.0625f);

                    //こうしときゃたぶんきっとはやくうごくでしょ
                    if(isNormal){
                        modelPipe[tex].render(null, 0, 0, 0, 0, 0, 1.0f);
                    }
                    else{
                        if(side==i){
                            //たぶん抽出パイプが一番多く使われるでしょ
                            if(type== IPipe.PipeType.EXTRACTOR) modelPipe[INDEX_EXTRACTOR].render(null, 0, 0, 0, 0, 0, 1.0f);
                            else if(type== IPipe.PipeType.ONEWAY) modelPipe[INDEX_ONEWAY].render(null,0,0,0,0,0,1.0f);
                            else if(type== IPipe.PipeType.SORTER) modelPipe[INDEX_SORTER+10].render(null,0,0,0,0,0,1.0f);
                            //else modelPipe[INDEX_VOID].render(null,0,0,0,0,0,1.0f);
                        }
                        else{
                            modelPipe[tex].render(null, 0, 0, 0, 0, 0, 1.0f);
                        }
                    }

                    GL11.glPopMatrix();
                }
                k = (k << 1);
            }
        }

        //接続不可な面
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        //GL11.glScalef(0.0625f,0.0625f,0.0625f);

        con=te.getConnectStateDisable();
        if(con!=0) {
            bindTexture(TextureMap.locationBlocksTexture);
            IIcon icon = BlockCore.pipeTin.getIcon(0, 0);
            TessellatorWrapper.SetBlockRender(false);

            if ((con & 1) != 0) TessellatorWrapper.DrawYNeg(f7, f5, f7, f9, f9, icon);
            con = (byte) (con >> 1);
            if ((con & 1) != 0) TessellatorWrapper.DrawYPos(f7, f11, f7, f9, f9, icon);
            con = (byte) (con >> 1);

            if ((con & 1) != 0) TessellatorWrapper.DrawZNeg(f7, f7, f5, f9, f9, icon);
            con = (byte) (con >> 1);
            if ((con & 1) != 0) TessellatorWrapper.DrawZPos(f7, f7, f11, f9, f9, icon);
            con = (byte) (con >> 1);

            if ((con & 1) != 0) TessellatorWrapper.DrawXNeg(f5, f7, f7, f9, f9, icon);
            con = (byte) (con >> 1);
            if ((con & 1) != 0) TessellatorWrapper.DrawXPos(f11, f7, f7, f9, f9, icon);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
