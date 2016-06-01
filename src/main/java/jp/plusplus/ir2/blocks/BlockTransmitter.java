package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.ChunkLoaderManager;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityTransmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class BlockTransmitter extends BlockMachineBase implements ChunkLoaderManager.IChunkLoader{
    public boolean withLoader;

    protected BlockTransmitter(boolean flag) {
        super("transmitter", flag?2:1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineTransmitter");
        setBlockName("transmitter"+(flag?"WithLoader":""));
        withLoader=flag;
        //minFrequency=1;
        minRSS=32;
        minFrequency=256;
        maxRSS=2048;
        maxFrequency=2048;

        multiWidth=2;
        multiHeight=2;
        multiDepth=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityTransmitter(withLoader);
    }

    @Override
    public boolean canLoad(World world, int x, int y, int z) {
        if(!withLoader) return false;

        TileEntity te=world.getTileEntity(x,y,z);
        if(!(te instanceof TileEntityTransmitter)) return false;
        short r=((TileEntityTransmitter) te).rss;
        short f=((TileEntityTransmitter) te).frequency;

        return r>=minRSS && r<=maxRSS && f>=minFrequency && f<=maxFrequency;
    }


    @Override
    public int getRenderType() {
        return IR2.renderTransmitterId;
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
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }
}
