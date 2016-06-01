package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityHarvester;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockHarvester extends BlockMachineBase {
    private boolean isAdvanced;
    protected BlockHarvester(boolean adv) {
        super("harvester"+(adv?"Adv":""), 1, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID+":machineHarvester"+(adv?"Adv":""));
        setBlockName("harvester"+(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;

        multiWidth=3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityHarvester(isAdvanced);
    }

    @Override
    public int getRenderType(){
        return IR2.renderHarvesterId;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
