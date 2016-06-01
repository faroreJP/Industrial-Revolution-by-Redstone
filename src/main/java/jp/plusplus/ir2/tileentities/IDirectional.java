package jp.plusplus.ir2.tileentities;

/**
 * Created by plusplus_F on 2015/02/23.
 * なにこのクラス？ぼく知らない！
 */
public interface IDirectional {
    byte getSide();
    void  setSide(byte s);
    boolean isFront(byte s);
}
