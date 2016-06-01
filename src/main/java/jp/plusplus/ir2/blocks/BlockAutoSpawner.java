package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityAutoSpawner;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/16.
 */
public class BlockAutoSpawner extends BlockMachineBase {
    protected IIcon iconTank;
    protected IIcon iconHead;
    protected IIcon iconFluid;

    protected BlockAutoSpawner() {
        super("autoSpawner", 3, "Obsidian");
        setBlockTextureName(IR2.MODID+":machineAutoSpawner2Front");
        setBlockName("autoSpawner");
        //minFrequency=1;
        multiHeight=2;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir){
        super.registerBlockIcons(ir);
        iconTank=ir.registerIcon(IR2.MODID+":machineAutoSpawnerTank");
        topIcon=iconHead=ir.registerIcon(IR2.MODID+":tankSmallTop");
        iconFluid=ir.registerIcon(IR2.MODID+":fluidSpawner");
    }

    @Override
    public IIcon getIcon(int i, int k){
        if(i==-1) return iconTank;
        if(i==-2) return iconHead;
        if(i==-3) return iconFluid;
        return super.getIcon(i,k);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityAutoSpawner();
    }

    @Override
    public int getRenderType(){
        return IR2.renderSpawnerId;
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
