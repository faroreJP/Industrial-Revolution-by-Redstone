package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityButcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/17.
 */
public class BlockButcher extends BlockMachineBase {
    protected BlockButcher() {
        super("butcher", 2, "Stone");
        setBlockTextureName(IR2.MODID+":machineButcher");
        setBlockName("butcher");
        minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityButcher();
    }
}
