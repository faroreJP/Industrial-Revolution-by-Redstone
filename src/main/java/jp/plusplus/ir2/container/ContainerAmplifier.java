package jp.plusplus.ir2.container;

import jp.plusplus.ir2.AchievementChecker;
import jp.plusplus.ir2.tileentities.TileEntityAmplifier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class ContainerAmplifier extends Container {
    TileEntityAmplifier entity;
    EntityPlayer player;

    public ContainerAmplifier(EntityPlayer player, TileEntityAmplifier tileEntity){
        entity=tileEntity;
        this.player=player;

        //inventory's inventory
        for(int i=0;i<3;i++){
            this.addSlotToContainer(new Slot(entity, i, 62+18*i, 47));
        }

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

    private void checkAchievement(){
        //if(!entity.getWorldObj().isRemote)   return;

        if(entity.getOutputRSS(-1)>=256){
            player.triggerAchievement(AchievementChecker.challengeToLimit);
        }

        /*
        EntityPlayer p= Minecraft.getMinecraft().thePlayer;
        if(p==null)		return;
        if(p.username.equals(player.username) && entity.getOutputRSS(-1)>=256){
            p.triggerAchievement(AchievementProxy.challengeToLimit);
        }
        */
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

            if(par2>=0 && par2<3){
                //slot -> inventory
                if (!this.mergeItemStack(stack, 3, 39, true)){
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else{
                // Something Wrong!!!
                /*
                if(entity.checkElement(stack)){
                    if (!this.mergeItemStack(stack, 0, 3, false)){
                        checkAchievement();
                        return null;
                    }
                    checkAchievement();
                }
                else
                */
                if(par2>=3 && par2<30){
                    //inventory -> player's slot
                    if (!this.mergeItemStack(stack, 30, 39, false)){
                        return null;
                    }
                }
                else if (par2>=30 && par2<39 && !this.mergeItemStack(stack, 3, 30, false)){
                    return null;
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
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4){
        boolean f=super.mergeItemStack(par1ItemStack, par2, par3, par4);
        checkAchievement();
        return f;
    }
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer){
        ItemStack r=super.slotClick(par1, par2, par3, par4EntityPlayer);
        checkAchievement();
        return r;
    }
}
