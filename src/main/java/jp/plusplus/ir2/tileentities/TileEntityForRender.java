package jp.plusplus.ir2.tileentities;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/08/10.
 * 描画処理のためのTileEntity
 */
public class TileEntityForRender extends TileEntity {
    public static final int MAX_TICKS=1200;
    public int renderingTicks;
    @Override
    public void updateEntity(){
        renderingTicks++;
        if(renderingTicks==MAX_TICKS) renderingTicks=0; //1分ごとにリセット
    }
}
