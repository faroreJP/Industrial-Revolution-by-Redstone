package jp.plusplus.ir2;

import jp.plusplus.ir2.tileentities.MultiBlockPosition;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/08/10.
 * ChunkLoader機能をうまいこと管理してくれる子
 */
public class ChunkLoaderManager implements ForgeChunkManager.LoadingCallback{
    private static ChunkLoaderManager obj=new ChunkLoaderManager();
    private static HashMap<MultiBlockPosition, ForgeChunkManager.Ticket> tickets=new HashMap<MultiBlockPosition, ForgeChunkManager.Ticket>();

    //インスタンスの取得
    public static ChunkLoaderManager instance(){
        return obj;
    }

    //チャンクローダの登録
    public static void setChunkLoader(World w, int x, int y, int z){
        //チケットの要求
        ForgeChunkManager.Ticket t=ForgeChunkManager.requestTicket(IR2.instance, w, ForgeChunkManager.Type.NORMAL);
        if(t==null) return;

        //チケットに情報書き込み
        t.getModData().setString("type", "block");
        t.getModData().setInteger("x", x);
        t.getModData().setInteger("y", y);
        t.getModData().setInteger("z", z);

        //チケットをどうたら
        tickets.put(new MultiBlockPosition(x,y,z,-1), t);
        ForgeChunkManager.forceChunk(t, w.getChunkFromBlockCoords(x, z).getChunkCoordIntPair());
    }

    //チャンクローダの削除
    public static void removeChunkLoader(World world, int x, int y, int z){
        MultiBlockPosition p=new MultiBlockPosition(x,y,z,-1);

        //チケットが存在するか確認
        if(!tickets.containsKey(p)) return;

        //チャンクロードの停止
        ForgeChunkManager.Ticket t=tickets.get(p);
        if(!ForgeChunkManager.getPersistentChunksFor(t.world).isEmpty()){
            ForgeChunkManager.unforceChunk(t, world.getChunkFromBlockCoords(x, z).getChunkCoordIntPair());
        }

        //削除
        tickets.remove(p);
    }

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
        //チケット全部見てなんか処理してる
        for(ForgeChunkManager.Ticket t : tickets){
            if(t.getModData().getString("type").equals("block")){
                int x = t.getModData().getInteger("x");
                int y = t.getModData().getInteger("y");
                int z = t.getModData().getInteger("z");
                Block b=world.getBlock(x,y,z);

                //チャンクローダか判定してそれぞれ処理
                if(b instanceof IChunkLoader){
                    if(((IChunkLoader)b).canLoad(world, x, y, z)){
                        setChunkLoader(world, x, y, z);
                    }else{
                        removeChunkLoader(world, x, y, z);
                    }

                }else{
                    removeChunkLoader(world, x, y, z);
                }
            }
        }
    }

    public static HashMap<MultiBlockPosition, ForgeChunkManager.Ticket> getTickets(){
        return tickets;
    }

    // チャンクローダ用インターフェース
    public interface IChunkLoader{
        public boolean canLoad(World world,int x, int y, int z);
    }

}
