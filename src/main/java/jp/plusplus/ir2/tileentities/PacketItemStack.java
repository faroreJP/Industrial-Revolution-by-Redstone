package jp.plusplus.ir2.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by plusplus_F on 2015/02/06.
 * アイテムスタックのパケット
 */
public class PacketItemStack extends PacketBase{
    private ItemStack itemStack;

    public PacketItemStack(ItemStack item){
        itemStack=item;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);

        if(itemStack!=null){
            itemStack.writeToNBT(nbt);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);

        itemStack=ItemStack.loadItemStackFromNBT(nbt);
    }
}
