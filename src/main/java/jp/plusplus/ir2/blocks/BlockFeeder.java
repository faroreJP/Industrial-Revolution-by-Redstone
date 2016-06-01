package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityFeeder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockFeeder extends BlockMachineBase {
    private boolean isAdvanced;

    protected BlockFeeder(boolean adv) {
        super("feeder"+(adv?"Adv":""), 2, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID + ":machineFeeder" +(adv?"Adv":""));
        setBlockName("feeder" +(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFeeder(isAdvanced);
    }
}
