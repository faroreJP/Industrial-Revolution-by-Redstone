package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityFurnaceAdvanced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerFurnaceAdvanced extends Container {

    public int lastProgress;

    private TileEntityFurnaceAdvanced entity;
    private ISidedInventory inventory;

    public ContainerFurnaceAdvanced(EntityPlayer player, TileEntityFurnaceAdvanced tileEntity) {
        this.entity = tileEntity;
        this.inventory = tileEntity;

        //inventory's inventory
        for(int i=0;i<2;i++){
            this.addSlotToContainer(new Slot(entity, i, 30+i*18, 25));
        }
        for(int i=0;i<2;i++){
            this.addSlotToContainer(new SlotFurnace(player, entity, 2+i, 102+i*26, 25));
        }

        //player inventory
        for(int i=0;i<3;i++) {
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
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.progress) {
                icrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
                icrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);
            }
        }
        lastProgress = entity.progress;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return inventory.isUseableByPlayer(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) entity.progress=((entity.progress&0xffff0000)|par2);
        else if(par1==1) entity.progress=((entity.progress&0x0000ffff)|(par2<<16));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if(slot!=null && slot.getHasStack()){
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2>=0 && par2<4){
                //material/product -> inventory
                if (!this.mergeItemStack(stack, 4, 40, false)){
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else{
                //inventory -> material/product
                if(FurnaceRecipes.smelting().getSmeltingResult(stack)!=null){
                    //材料
                    if (!this.mergeItemStack(stack, 0, 2, false)){
                        return null;
                    }
                }
                else if(par2>=4 && par2<31){
                    if (!this.mergeItemStack(stack, 31, 40, false)){
                        return null;
                    }
                }
                else if (par2>=31 && par2<40 && !this.mergeItemStack(stack, 4, 31, false)){
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
}
