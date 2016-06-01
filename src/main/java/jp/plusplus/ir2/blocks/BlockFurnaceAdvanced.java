package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFurnaceAdvanced;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockFurnaceAdvanced extends BlockMachineBase {
    protected BlockFurnaceAdvanced() {
        super("redstoneFurnaceAdv", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineFurnaceAdv");
        setBlockName("redstoneFurnaceAdv");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnaceAdvanced();
    }

}
