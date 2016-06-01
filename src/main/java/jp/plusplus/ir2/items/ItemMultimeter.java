package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IConductor;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ItemMultimeter extends Item {

    public ItemMultimeter(){
        setUnlocalizedName("IR2multimeter");
        setTextureName(IR2.MODID + ":multimeter");
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        if(IR2.enableDescription) {
            list.add(I18n.format("info.multimeter.0"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float disX, float disY, float disZ){
        if(world.isRemote)         return false;
        if(world.isAirBlock(x,y,z)) return false;

        //player.addChatComponentMessage(new ChatComponentText("[MetaData] "+world.getBlockMetadata(x, y, z)));

        Block b=world.getBlock(x, y, z);
        if(/*(b == Blocks.redstone_wire || b == BlockCore.regulator) && */false){
            player.addChatComponentMessage(new ChatComponentText("[Redstone Multimeter] "+world.getBlockMetadata(x, y, z)+"RSS"));
        }
        else {
            TileEntity e = world.getTileEntity(x, y, z);
            if (e instanceof IConductor) {
                IConductor ec = (IConductor) e;
                player.addChatComponentMessage(new ChatComponentText("[Redstone Multimeter] " + ec.getOutputRSS(side) + "RSS, " + ec.getOutputFrequency(side) + "Hz"));
                return true;
            }
            else {
                player.addChatComponentMessage(new ChatComponentText("[Redstone Multimeter] " + world.getIndirectPowerLevelTo(x, y, z, side) + "RSS"));
            }
        }
        return false;
    }
}
