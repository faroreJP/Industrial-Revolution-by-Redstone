package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFisher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockFisher extends BlockMachineBase {
    boolean isAdvanced;
    protected BlockFisher(boolean adv) {
        super("fisher", adv?1:2, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID+":machineFisher"+(adv?"Adv":""));
        setBlockName("fisher"+(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFisher(isAdvanced);
    }
}
