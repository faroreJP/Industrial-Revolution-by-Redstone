package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityChildSorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/07/26.
 */
public class BlockChildSorter extends BlockMachineBase {
    protected BlockChildSorter() {
        super("childSorter", 1, "Stone");
        setBlockTextureName(IR2.MODID + ":machineChildSorter");
        setBlockName("childSorter");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityChildSorter();
    }

    @Override
    public void openGUI(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer){
        //チャンクローダはGUIを持たない機械のため、何もしない
    }
}
