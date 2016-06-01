package jp.plusplus.ir2.api;

import jp.plusplus.ir2.blocks.render.RenderPipe;

import java.util.Iterator;

/**
 * Created by plusplus_F on 2015/07/10.
 * なんでPipeBaseとPipeFluidが別々に実装されてんの？
 * ってことで描画とかで使うInterface
 */
public interface IPipe {
    public byte getConnectState(); //パイプの接続状態（見た目）
    public byte getConnectStateDisable(); //パイプの接続不可面
    public Iterator getPackets(); //パイプ内のパケット全部
    public int getTextureIndex(); //パイプのテクスチャ
    public int getTextureCenterIndex(); //パイプのテクスチャ
    public PipeType getPipeType(); //パイプの種類
    public int getPipeSide(); //パイプの向き

    //パイプの種類
    public static enum PipeType {
        NORMAL, EXTRACTOR, ONEWAY, SORTER, VOID, INVALID_VALUE
    }
}
