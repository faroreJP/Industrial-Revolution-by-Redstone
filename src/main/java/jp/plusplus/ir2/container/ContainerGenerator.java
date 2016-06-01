package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import jp.plusplus.ir2.tileentities.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerGenerator extends Container {
    private TileEntityGenerator entity;
    private int lastRSS;
    private int lastFrequency;

    public ContainerGenerator(EntityPlayer player, TileEntityGenerator tileEntity){
        entity=tileEntity;

        //inventory's inventory
        this.addSlotToContainer(new Slot(entity, 0, 44, 25));

        //player's inventory
        for(int i=0;i<3;i++){
            for(int j=0;j<9;j++){
                this.addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 84+i*18));
            }
        }

        //player's slots
        for(int i=0;i<9;i++){
            this.addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 142));
        }
    }


    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if(slot!=null && slot.getHasStack()){
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2==0){
                if (!this.mergeItemStack(stack, 1, 37, true)){
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else{
                if(par2>=1 && par2<28){
                    if(stack.getItem() instanceof ItemCrystalUnit){
                        if(!this.mergeItemStack(stack, 0, 1, false)){
                            return null;
                        }
                    }
                    else if(!this.mergeItemStack(stack, 28, 37, false)){
                        return null;
                    }
                }
                else if (par2>=28 && par2<37){
                    if(stack.getItem() instanceof ItemCrystalUnit){
                        if(!this.mergeItemStack(stack, 0, 1, false)){
                            return null;
                        }
                    }
                    else if(!this.mergeItemStack(stack, 1, 28, false)){
                        return null;
                    }
                }
            }

            if (stack.stackSize == 0){
                slot.putStack((ItemStack)null);
            }
            else{
                slot.onSlotChanged();
            }

            if (stack.stackSize == itemStack.stackSize){
                return null;
            }
            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return itemStack;
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.getOutputRSS(-1));
        par1ICrafting.sendProgressBarUpdate(this, 1, entity.getOutputFrequency(-1));
    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int r=entity.getOutputRSS(-1);
        int f=entity.getOutputFrequency(-1);
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);


            if (lastRSS != r)   icrafting.sendProgressBarUpdate(this, 0, r);
            if (lastFrequency != f)   icrafting.sendProgressBarUpdate(this, 1, f);
        }
        lastRSS=r;
        lastFrequency=f;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) entity.rss = (short)par2;
        if(par1==1) entity.frequency = (short)par2;
    }
}