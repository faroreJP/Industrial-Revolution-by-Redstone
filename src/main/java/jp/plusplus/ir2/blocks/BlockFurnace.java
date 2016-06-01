package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockFurnace extends BlockMachineBase {
    protected BlockFurnace() {
        super("redstoneFurnace", 0, "Stone");
        setBlockTextureName(IR2.MODID+":machineFurnace");
        setBlockName("redstoneFurnace");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnace();
    }

}
