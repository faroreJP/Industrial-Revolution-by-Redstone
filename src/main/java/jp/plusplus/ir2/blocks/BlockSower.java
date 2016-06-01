package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntitySower;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockSower extends BlockMachineBase {
    private boolean isAdvanced;
    protected BlockSower(boolean adv) {
        super("sower"+(adv?"Adv":""), 2, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID + ":machineSower" +(adv?"Adv":""));
        setBlockName("sower"+(adv?"Adv":""));
        if(adv) {
            maxRSS = 256;
            maxFrequency = 256;
        }
        else minFrequency=1;
        isAdvanced=adv;

        multiDepth=2;
        multiHeight=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntitySower(isAdvanced);
    }


    @Override
    public int getRenderType(){
        return IR2.renderSowerId;
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
