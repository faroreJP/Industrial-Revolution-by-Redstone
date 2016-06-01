package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityAutoCrafter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockAutoCrafter extends BlockMachineBase {
    protected BlockAutoCrafter() {
        super("crafter", 1, "Stone");
        setBlockTextureName(IR2.MODID+":machineCrafter");
        setBlockName("crafter");
        minFrequency=1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityAutoCrafter();
    }
}
