package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFurnaceAdvanced;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/07.
 */
public class BlockFurnaceVariant extends BlockMachineBase {
    protected BlockFurnaceVariant() {
        super("redstoneFurnaceVar", 1, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineFurnaceAdv");
        setBlockName("redstoneFurnaceVar");

        maxFrequency=2048;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnaceAdvanced();
    }

}
