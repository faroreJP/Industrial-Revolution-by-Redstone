package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class TileEntitySpinningProt extends TileEntitySpinning {
    public TileEntitySpinningProt(){
        super(4*12, 4, "spinningProt");
    }

    @Override
    protected void work() {
        ItemStack product= Recipes.getSpinning(itemStacks[0], materialAmount);
        if(product==null) return;

        Random r = worldObj.rand;
        if (r.nextDouble()>=0.1) {// failure 10%
            if (itemStacks[1] == null) {
                itemStacks[1] = product.copy();
            } else if (itemStacks[1].isItemEqual(product)) {
                itemStacks[1].stackSize += product.stackSize;
            }
        }

        itemStacks[0].stackSize -= materialAmount*Recipes.getSpinningMaterialAmount(itemStacks[0]);
        if (itemStacks[0].stackSize <= 0) {
            itemStacks[0] = null;
        }
    }
    @Override
    public String getGuiFileName(){
        return "spinning.png";
    }
    @Override
    public int getStringColor(){
        return 0x404040;
    }
}
