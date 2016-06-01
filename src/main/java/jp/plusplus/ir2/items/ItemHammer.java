package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IHammerHandler;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/07/01.
 * パイプの接続不可面を設定する
 */
public class ItemHammer extends Item {

    public ItemHammer(){
        setUnlocalizedName("IR2hammer");
        setTextureName(IR2.MODID + ":hammer");
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
        setFull3D();
    }

    @Override
    public boolean isFull3D(){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        if(IR2.enableDescription) {
            list.add(I18n.format("info.hammer.0"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float disX, float disY, float disZ){
        //if(world.isRemote)         return false;
        if(world.isAirBlock(x,y,z)) return false;
        Block b=world.getBlock(x, y, z);

        if(b instanceof IHammerHandler){
            return ((IHammerHandler)b).hammer(item, player, world, x,y,z,side);
        }

        return false;
    }

}
