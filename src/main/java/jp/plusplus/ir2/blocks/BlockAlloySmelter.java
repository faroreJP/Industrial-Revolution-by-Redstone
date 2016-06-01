package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityAlloySmelter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockAlloySmelter extends BlockMachineBase {
    protected  boolean isStone;

    protected BlockAlloySmelter(String iName, int iRow, String cName) {
        super(iName, iRow, cName);
        setBlockName(iName);

        isStone=cName.equals("Stone");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return isStone?(new TileEntityAlloySmelter(32*15, 4, "alloySmelter")):(new TileEntityAlloySmelter(128*12, 2, "alloySmelterAdv"));
    }

}
