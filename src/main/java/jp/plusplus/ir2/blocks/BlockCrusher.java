package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityCrusher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class BlockCrusher extends BlockMachineBase {
    public boolean isAdvanced;

    protected BlockCrusher(boolean adv) {
        super("crusher"+(adv?"Adv":""), adv?3:2, adv?"Obsidian":"Stone");
        setBlockTextureName(IR2.MODID+":machineCrusher");
        setBlockName("crusher"+(adv?"Adv":""));
        isAdvanced=adv;

        multiWidth=2;
        multiHeight=2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityCrusher(isAdvanced);
    }

    @Override
    public int getRenderType(){
        return IR2.renderCrusherId;
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
