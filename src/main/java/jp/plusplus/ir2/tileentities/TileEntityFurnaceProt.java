package jp.plusplus.ir2.tileentities;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class TileEntityFurnaceProt extends TileEntityFurnace {
    Random rand;

    public TileEntityFurnaceProt(){
        super();
        workAmount=4*15;
        rand=new Random();
    }

    @Override
    protected void work() {
        ItemStack product = FurnaceRecipes.smelting().getSmeltingResult(itemStacks[0]);
        if (product != null) {
            if (rand.nextDouble()>=0.125) { // failure 12.5%
                if (itemStacks[1] == null) {
                    itemStacks[1] = product.copy();
                } else if (itemStacks[1].isItemEqual(product)) {
                    itemStacks[1].stackSize += product.stackSize;
                }
            }
            itemStacks[0].stackSize--;
            if (itemStacks[0].stackSize <= 0) {
                itemStacks[0] = null;
            }
        }
    }
    @Override
    public String getInventoryName() {
        return I18n.format("gui.redstoneFurnaceProt");
    }
}
