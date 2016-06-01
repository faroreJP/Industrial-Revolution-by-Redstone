package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityShearer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockShearer extends BlockMachineBase {
    private boolean isAdvanced;
    protected BlockShearer(boolean adv) {
        super("shearer"+(adv?"Adv":""), 1, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID+":machineShearer"+(adv?"Adv":""));
        setBlockName("shearer"+(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityShearer(isAdvanced);
    }
}
