package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityWoodcutter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockWoodcutter extends BlockMachineBase {
    private boolean isAdvanced;

    protected BlockWoodcutter(boolean adv) {
        super("woodcutter"+(adv?"Adv":""), 1, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID+":machineWoodcutter"+(adv?"Adv":""));
        setBlockName("woodcutter"+(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityWoodcutter(isAdvanced);
    }
}
