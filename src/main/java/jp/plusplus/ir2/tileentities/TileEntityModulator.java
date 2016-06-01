package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class TileEntityModulator extends TileEntityAmplifier {

    public TileEntityModulator(){
        side=-1;
    }

    @Override
    public short getAmplifiedValue(){
        short par=0;
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]==null) continue;
            if(itemStacks[i].getItem()==ItemCore.coil){
                par+=1;
            }
        }
        return par;
    }

    public short calcRSS(){
        if(rss==0)  return 0;
        short par=(short)(rss/(1.0+getAmplifiedValue()/3.0));
        return par>0?(par>getMaxRSS()?getMaxRSS():par):0;
    }

    public boolean checkElement(ItemStack item){
        if(item==null)  return false;
        return item.getItem()==ItemCore.coil;
    }

    //------------------------------------------------------------------
    @Override
    public short getOutputRSS(int direction) {
        return (direction==-1 || direction==(side))?calcRSS():0;
    }
    @Override
    public short getOutputFrequency(int direction) {
        if(direction!=-1 && (direction)!=side) return 0;
        if(calcRSS()==0)                        return 0;
        short par=(short)(frequency*(1+getAmplifiedValue()/3.0));
        return par>getMaxFrequency()?getMaxFrequency():par;
    }
    //-----------------------------------------------------------------------------
    @Override
    public String getInventoryName() {
        return I18n.format("gui.modulator");
    }
}
