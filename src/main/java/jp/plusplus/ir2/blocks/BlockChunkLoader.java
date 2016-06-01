package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.ChunkLoaderManager;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityChunkLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/08/10.
 */
public class BlockChunkLoader extends BlockMachineBase implements ChunkLoaderManager.IChunkLoader{
    protected BlockChunkLoader() {
        super("chunkLoader", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineChunkLoader");
        setBlockName("chunkLoader");

        multiHeight=2;

        minRSS=32;
        minFrequency=32;
        maxRSS=2048;
        maxFrequency=2048;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityChunkLoader();
    }

    @Override
    public int getRenderType(){
        return IR2.renderChunkLoaderId;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void openGUI(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer){
        //チャンクローダはGUIを持たない機械のため、何もしない
    }

    @Override
    public boolean canLoad(World world, int x, int y, int z) {
        TileEntity te=world.getTileEntity(x,y,z);
        if(te instanceof TileEntityChunkLoader){
            return ((TileEntityChunkLoader) te).isWorking();
        }
        return false;
    }
}
