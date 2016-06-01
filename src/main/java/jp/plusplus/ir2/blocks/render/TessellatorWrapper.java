package jp.plusplus.ir2.blocks.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

/**
 * Created by plusplus_F on 2015/06/28.
 * なんで！！！てってれーたーには！！！！面描画メソッドが！！！！用意されてないんですか！！！！！１１１１
 */
public class TessellatorWrapper {
    private static Tessellator tessellator=Tessellator.instance;
    private static boolean isBlockRender;

    public static void SetBlockRender(boolean flag){
        isBlockRender=flag;
    }

    public static void DrawXPos(float bx, float by, float bz, float my, float mz, IIcon icon){
        float minU=icon.getInterpolatedU(bz);
        float maxU=icon.getInterpolatedU(mz);
        float minV=icon.getInterpolatedV(by);
        float maxV=icon.getInterpolatedV(my);

        if(isBlockRender){
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            tessellator.addVertexWithUV(bx, my, bz, minU, minV);
            tessellator.addVertexWithUV(bx, my, mz, maxU, minV);
            tessellator.addVertexWithUV(bx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, by, bz, minU, maxV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            tessellator.addVertexWithUV(bx, my, bz, minU, minV);
            tessellator.addVertexWithUV(bx, my, mz, maxU, minV);
            tessellator.addVertexWithUV(bx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, by, bz, minU, maxV);
            tessellator.draw();
        }
    }
    public static void DrawXNeg(float bx, float by, float bz, float my, float mz, IIcon icon){
        float minU=icon.getInterpolatedU(16 * bz);
        float maxU=icon.getInterpolatedU(16 * mz);
        float minV=icon.getInterpolatedV(16 * by);
        float maxV=icon.getInterpolatedV(16 * my);

        if(isBlockRender){
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            tessellator.addVertexWithUV(bx, my, bz, minU, minV);
            tessellator.addVertexWithUV(bx, by, bz, minU, maxV);
            tessellator.addVertexWithUV(bx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, my, mz, maxU, minV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            tessellator.addVertexWithUV(bx, my, bz, minU, minV);
            tessellator.addVertexWithUV(bx, by, bz, minU, maxV);
            tessellator.addVertexWithUV(bx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, my, mz, maxU, minV);
            tessellator.draw();
        }
    }

    public static void DrawYPos(float bx, float by, float bz, float mx, float mz, IIcon icon){
        float minU=icon.getInterpolatedU(16 * bx);
        float maxU=icon.getInterpolatedU(16 * mx);
        float minV=icon.getInterpolatedV(16 * bz);
        float maxV=icon.getInterpolatedV(16 * mz);

        if(isBlockRender){
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(bx, by, mz, minU, maxV);
            tessellator.addVertexWithUV(mx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(bx, by, mz, minU, maxV);
            tessellator.addVertexWithUV(mx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.draw();
        }
    }
    public static void DrawYNeg(float bx, float by, float bz, float mx, float mz, IIcon icon){
        float minU=icon.getInterpolatedU(16 * bx);
        float maxU=icon.getInterpolatedU(16 * mx);
        float minV=icon.getInterpolatedV(16 * bz);
        float maxV=icon.getInterpolatedV(16 * mz);

        if(isBlockRender){
            tessellator.setNormal(0.0f,-1.0f,0.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.addVertexWithUV(mx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, by, mz, minU, maxV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f,-1.0f,0.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.addVertexWithUV(mx, by, mz, maxU, maxV);
            tessellator.addVertexWithUV(bx, by, mz, minU, maxV);
            tessellator.draw();
        }
    }

    public static void DrawZPos(float bx, float by, float bz, float mx, float my, IIcon icon){
        float minU=icon.getInterpolatedU(16 * bx);
        float maxU=icon.getInterpolatedU(16 * mx);
        float minV=icon.getInterpolatedV(16 * by);
        float maxV=icon.getInterpolatedV(16 * my);

        if(isBlockRender){
            tessellator.setNormal(0.0f,0.0f,1.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.addVertexWithUV(mx, my, bz, maxU, maxV);
            tessellator.addVertexWithUV(bx, my, bz, minU, maxV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f,0.0f,1.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.addVertexWithUV(mx, my, bz, maxU, maxV);
            tessellator.addVertexWithUV(bx, my, bz, minU, maxV);
            tessellator.draw();
        }
    }
    public static void DrawZNeg(float bx, float by, float bz, float mx, float my, IIcon icon){
        float minU=icon.getInterpolatedU(16*bx);
        float maxU=icon.getInterpolatedU(16*mx);
        float minV=icon.getInterpolatedV(16 * by);
        float maxV=icon.getInterpolatedV(16*my);

        if(isBlockRender){
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(bx, my, bz, minU, maxV);
            tessellator.addVertexWithUV(mx, my, bz, maxU, maxV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
        }
        else{
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator.addVertexWithUV(bx, by, bz, minU, minV);
            tessellator.addVertexWithUV(bx, my, bz, minU, maxV);
            tessellator.addVertexWithUV(mx, my, bz, maxU, maxV);
            tessellator.addVertexWithUV(mx, by, bz, maxU, minV);
            tessellator.draw();
        }
    }

    public static void DrawAllFaces(float bx, float by, float bz, float mx, float my, float mz, IIcon icon) {
        DrawYNeg(bx, by, bz, mx, mz, icon);
        DrawYPos(bx, my, bz, mx, mz, icon);
        DrawZNeg(bx, by, bz, mx, my, icon);
        DrawZPos(bx, by, mz, mx, my, icon);
        DrawXNeg(bx, by, bz, my, mz, icon);
        DrawXPos(mx, by, bz, my, mz, icon);
    }

}
