package jp.plusplus.ir2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.BlockPotato;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

/**
 * Created by plusplus_F on 2015/06/01.
 */
public class BlockCropPotatoQuartz extends BlockPotato {

    public BlockCropPotatoQuartz(){
        setBlockTextureName("potatoes");
        setBlockName("IR2cropPotatoQuartz");
    }

    protected Item func_149866_i() {
        return ItemCore.potatoQuartz;
    }

    protected Item func_149865_P() {
        return ItemCore.potatoQuartz;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        blockIcon=p_149651_1_.registerIcon(IR2.MODID+":potatoQuartz");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int meta) {
        if (meta < 0 || meta > 7) {
            meta = 7;
        }
        return meta == 7 ? blockIcon : super.getIcon(p_149691_1_, meta);
    }
}
