package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityUnloader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/16.
 */
public class BlockUnloader extends BlockMachineBase {
    protected BlockUnloader() {
        super("unloader", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineUnloader");
        setBlockName("unloader");
        //minFrequency=1;
        minRSS=1;
        minFrequency=1;
        maxRSS=2048;
        maxFrequency=2048;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityUnloader();
    }
}
