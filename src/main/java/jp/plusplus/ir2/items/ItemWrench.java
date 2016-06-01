package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ItemWrench extends Item {

    public ItemWrench(){
        setUnlocalizedName("IR2wrench");
        setTextureName(IR2.MODID + ":wrench");
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
            list.add(I18n.format("info.wrench.0"));
            list.add(I18n.format("info.wrench.1"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float disX, float disY, float disZ){
        //if(world.isRemote)         return false;
        if(world.isAirBlock(x,y,z)) return false;
        Block b=world.getBlock(x, y, z);

        if(b instanceof IWrenchHandler) {
            return ((IWrenchHandler) b).wrench(item, player, world, x,y,z,side);
        }

        return false;
    }

    public static boolean isWrench(ItemStack item){
        if(item!=null){
            if(item.getItem()== ItemCore.wrench){
                return true;
            }
        }
        return false;
    }
}
