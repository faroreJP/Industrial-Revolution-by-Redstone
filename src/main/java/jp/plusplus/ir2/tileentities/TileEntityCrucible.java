package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by plusplus_F on 2015/05/19.
 */
public class TileEntityCrucible extends TileEntityFountain{
    public TileEntityCrucible(){
        workAmount=128;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getFluidIcon(){
        return Blocks.flowing_lava.getIcon(0,0);
        /*
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
        */
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.isRemote) return;

        //checking inventory
        if (itemStacks[0] == null) return;
        if (itemStacks[0].stackSize <= 0) {
            itemStacks[0] = null;
            return;
        }

        FluidStack fluid = tank.getFluid();
        if (fluid != null && fluid.getFluid() != null) {
            ItemStack get = FluidContainerRegistry.fillFluidContainer(fluid.copy(), itemStacks[0]);
            if (get != null) {
                int cap = FluidContainerRegistry.getContainerCapacity(get);
                if (fluid.amount < cap) return;


                if (itemStacks[1] != null) {
                    if (!itemStacks[1].isItemEqual(get)) return;
                    if (itemStacks[1].stackSize + get.stackSize > itemStacks[1].getMaxStackSize()) return;
                }

                if (itemStacks[1] == null || itemStacks[1].stackSize <= 0) {
                    setInventorySlotContents(1, get);
                } else {
                    itemStacks[1].stackSize += get.stackSize;
                }

                itemStacks[0].stackSize--;
                if (itemStacks[0].stackSize <= 0) {
                    setInventorySlotContents(0, null);
                }

                drain(ForgeDirection.UNKNOWN, cap, true);
                markDirty();
            }
        }
    }

    @Override
    public void work(){
        if(tank.isEmpty()){
            tank.setFluid(new FluidStack(FluidRegistry.LAVA, 250));
        }
        else{
            tank.setAmount(tank.getFluidAmount()+250);
        }
        itemStacks[2].stackSize--;
        if(itemStacks[2].stackSize<=0){
            setInventorySlotContents(2, null);
        }
    }
    @Override
    public boolean canWork() {
        if (itemStacks[2] == null) return false;
        if (itemStacks[2].stackSize <= 0) return false;
        if (itemStacks[2].getItem() != Item.getItemFromBlock(Blocks.cobblestone)) return false;

        return !tank.isFull();
    }

    //--------------------------------------------------------------------------------------------------------------

    @Override
    public String getInventoryName() {
        return BlockCore.machineCrucible.getLocalizedName();
    }
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if(i==2) return itemstack.getItem()==Item.getItemFromBlock(Blocks.cobblestone);
        if(i==0) return FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.LAVA, 1000), itemstack)!=null;
        return false;
    }
}
