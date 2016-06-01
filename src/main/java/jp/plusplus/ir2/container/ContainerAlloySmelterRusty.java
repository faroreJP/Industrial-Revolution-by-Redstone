package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityAlloySmelterRusty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/02.
 */
public class ContainerAlloySmelterRusty  extends Container {
    public int lastProgress;
    public int lastBurnTime;
    public int lastItemBurnTime;

    private TileEntityAlloySmelterRusty entity;

    public ContainerAlloySmelterRusty(EntityPlayer player, TileEntityAlloySmelterRusty tileEntity) {
        this.entity = tileEntity;

        //inventory's inventory
        addSlotToContainer(new Slot(entity, 0, 36, 17));
        addSlotToContainer(new Slot(entity, 1, 54, 17));
        addSlotToContainer(new SlotWithEXP(player, entity, 2, 116, 35));
        addSlotToContainer(new Slot(entity, 3, 45, 53));

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
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress);
        par1ICrafting.sendProgressBarUpdate(this, 1, entity.burnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, entity.itemBurnTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.progress) icrafting.sendProgressBarUpdate(this, 0, entity.progress);
            if (lastBurnTime != entity.burnTime) icrafting.sendProgressBarUpdate(this, 1, entity.burnTime);
            if (lastItemBurnTime != entity.itemBurnTime) icrafting.sendProgressBarUpdate(this, 2, entity.itemBurnTime);

        }
        lastProgress = entity.progress;
        lastBurnTime = entity.burnTime;
        lastItemBurnTime = entity.itemBurnTime;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) entity.progress = (short)par2;
        if(par1==1) entity.burnTime = (short)par2;
        if(par1==2) entity.itemBurnTime = (short)par2;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if(slot!=null && slot.getHasStack()){
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2==2){
                //product -> player
                if (!this.mergeItemStack(stack, 4, 40, true)){
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else if(par2==0 || par2==1 || par2==3){
                //material or fuel -> player
                if(!this.mergeItemStack(stack, 4, 40, false)){
                    return null;
                }
            }
            else{
                //player's inventory or slots -> fuel
                if(entity.isItemFuel(stack)){
                    if (!this.mergeItemStack(stack, 3, 4, false)){
                        return null;
                    }
                }
                else{
                    boolean flag1=false;
                    //player's inventory or slots -> material
                    for(int i=0;i<2;i++){
                        if(entity.itemStacks[i]==null){
                            flag1=true;
                            if (!this.mergeItemStack(stack, 0, 2, false)){
                                return null;
                            }
                            break;
                        }
                        else if (entity.itemStacks[i].isItemEqual(stack)){
                            flag1=true;
                            if (!this.mergeItemStack(stack, 0, 2, false)){
                                return null;
                            }
                            break;
                        }
                    }
                    if(!flag1){
                        if(par2>=2 && par2<29){
                            //player's inventory -> slots
                            if (!this.mergeItemStack(stack, 31, 40, false)){
                                return null;
                            }
                        }
                        else if (par2>=31 && par2<40 && !this.mergeItemStack(stack, 4, 31, false)){
                            return null;
                        }
                    }
                }
            }

            if (stack.stackSize == 0){
                slot.putStack(null);
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
}