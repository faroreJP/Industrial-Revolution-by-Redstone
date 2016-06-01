package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFurnaceProt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockFurnaceProt extends BlockMachineBase {
    protected BlockFurnaceProt() {
        super("redstoneFurnaceProt", 1, "Stone");
        setBlockTextureName(IR2.MODID+":machineFurnaceProt");
        setBlockName("redstoneFurnaceProt");

        minRSS=1;
        minFrequency=1;
        maxRSS=16;
        maxFrequency=8;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnaceProt();
    }

}
