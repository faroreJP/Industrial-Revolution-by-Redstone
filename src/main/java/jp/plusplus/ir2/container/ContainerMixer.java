package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityMixer;
import jp.plusplus.ir2.tileentities.TileEntityTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by plusplus_F on 2015/07/26.
 */
public class ContainerMixer extends Container {
    public int lastProgress;
    public int[] lastAmount;

    private TileEntityMixer entity;
    private ISidedInventory inventory;

    public ContainerMixer(EntityPlayer player, TileEntityMixer tileEntity) {
        this.entity = tileEntity;
        this.inventory = entity;

        lastAmount=new int[entity.tank.length];

        //inventory's inventory
        for(int i=0;i<4;i++) addSlotToContainer(new Slot(inventory, i, 33+18*(i%2), 19+34*(i/2)));
        addSlotToContainer(new SlotWithEXP(player, inventory, 4, 119, 23));
        addSlotToContainer(new Slot(inventory, 5, 119, 49));
        addSlotToContainer(new SlotWithEXP(player, inventory, 6, 83, 53));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);

        for(int i=0;i<entity.tank.length;i++){
            int k=i*3+2;

            if(entity.tank[i].isEmpty()) par1ICrafting.sendProgressBarUpdate(this, k, -1);
            else par1ICrafting.sendProgressBarUpdate(this, k, entity.tank[i].getFluid().getFluid().getID());

            int amt=entity.tank[i].getFluidAmount();
            par1ICrafting.sendProgressBarUpdate(this, k+1, amt&0xffff);
            par1ICrafting.sendProgressBarUpdate(this, k+2, (amt>>16)&0xffff);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if(lastProgress!=entity.progress){
                icrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
                icrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);
            }

            for(int n=0;n<entity.tank.length;n++) {
                int k = n * 3 + 2;

                if(lastAmount[n]!=entity.tank[n].getFluidAmount()){
                    if(entity.tank[n].isEmpty()) icrafting.sendProgressBarUpdate(this, k, -1);
                    else icrafting.sendProgressBarUpdate(this, k, entity.tank[n].getFluid().getFluid().getID());

                    int amt=entity.tank[n].getFluidAmount();
                    icrafting.sendProgressBarUpdate(this, k + 1, amt & 0xffff);
                    icrafting.sendProgressBarUpdate(this, k + 2, (amt >> 16) & 0xffff);
                }
            }
        }
        lastProgress = entity.progress;
        for(int i=0;i<lastAmount.length;i++) lastAmount[i]=entity.tank[i].getFluidAmount();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) entity.progress=((entity.progress&0xffff0000)|par2);
        else if(par1==1) entity.progress=((entity.progress&0x0000ffff)|(par2<<16));
        else {
            int j=par1-2;
            int k=j/3;
            int m=j%3;

            if(m==0) {
                if(par2!=-1) entity.tank[k].setFluid(new FluidStack(FluidRegistry.getFluid(par2), 1000));
                else entity.tank[k].setFluid(null);
            }
            else if(m==1) {
                entity.tank[k].setAmount((entity.tank[k].getFluidAmount()&0xffff0000)|par2);
            }
            else if(m==2){
                entity.tank[k].setAmount((entity.tank[k].getFluidAmount()&0x0000ffff)|(par2<<16));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return inventory.isUseableByPlayer(entityPlayer);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2<7){
                if (!this.mergeItemStack(stack, 7, 43, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else{
                if (inventory.isItemValidForSlot(0, stack)) {
                    if (!this.mergeItemStack(stack, 0, 2, false)) {
                        return null;
                    }
                }
                else if (inventory.isItemValidForSlot(2, stack)) {
                    if (!this.mergeItemStack(stack, 2, 4, false)) {
                        return null;
                    }
                }
                else if (par2 >= 7 && par2 < 34) {
                    if (!this.mergeItemStack(stack, 34, 43, false)) {
                        return null;
                    }
                } else if (par2 >= 34 && par2 < 43 && !this.mergeItemStack(stack, 7, 34, false)) {
                    return null;
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return itemStack;
    }
}
