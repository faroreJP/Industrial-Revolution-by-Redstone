package jp.plusplus.ir2.api;

/**
 * Created by plusplus_F on 2015/01/31.
 * 発展RS信号を扱う TileEntity 全てに実装する必要があるインターフェース
 * こいつを実装さえしていればだいたい上手くいくんじゃないですか？
 */
public interface IConductor {

    /**
     * TEから見た方向 direction への出力 RSS を得る
     * IR3では0-2048までを扱う
     * @param direction
     * @return
     */
    short getOutputRSS(int direction);

    /**
     * TEから見た方向 direction への出力周波数 を得る
     * IR3では0-2048までを扱う
     * @param direction
     * @return
     */
    short getOutputFrequency(int direction);

    /**
     * こいつが許容できる最大の RSS を得る
     * IR3では0-2048までを扱う
     * @return
     */
    short getMaxRSS();

    /**
     * こいつが許容できる最大の周波数を得る
     * IR3では0-2048までを扱う
     * @return
     */
    short getMaxFrequency();

    /**
     * TEが方向 side へ接続できるかを返す
     * trueだと接続する
     * @param side
     * @return
     */
    boolean canConnect(int side);
}
