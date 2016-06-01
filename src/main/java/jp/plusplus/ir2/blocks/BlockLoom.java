package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityLoom;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockLoom extends BlockMachineBase {
    protected BlockLoom() {
        super("loom", 1, "Stone");
        setBlockTextureName(IR2.MODID+":machineLoom");
        setBlockName("loom");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityLoom();
    }

}
