package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntitySmoker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerSmoker extends Container {

    public int lastProgress;
    public int lastBurnTime;
    public short[] lastStage=new short[9];

    private TileEntitySmoker entity;
    private ISidedInventory inventory;

    public ContainerSmoker(EntityPlayer player, TileEntitySmoker tileEntity) {
        this.inventory = this.entity = tileEntity;

        //inventory's inventory
        for(int i=0;i<9;i++) this.addSlotToContainer(new Slot(this.inventory, i, 8+18*i, 58));
        for(int i=0;i<9;i++) this.addSlotToContainer(new SlotWithEXP(player, this.inventory, i+9, 8+18*i, 23));
        this.addSlotToContainer(new Slot(this.inventory, 18, 80, 93));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 115 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 173));
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 2, entity.burnTime);
        for(int i=0;i<9;i++) par1ICrafting.sendProgressBarUpdate(this, 3+i, entity.stage[i]);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != entity.progress){
                icrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
                icrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);
            }
            if (lastBurnTime != entity.burnTime) icrafting.sendProgressBarUpdate(this, 2, entity.burnTime);

            for (int j = 0; j < 9; j++) {
                if (lastStage[j] != entity.stage[j]) icrafting.sendProgressBarUpdate(this, 3 + j, entity.stage[j]);
            }
        }
        lastProgress = entity.progress;
        lastBurnTime = entity.burnTime;
        for (int i = 0; i < 9; i++) lastStage[i] = entity.stage[i];
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
        else if(par1==2) entity.burnTime=(short)par2;
        else entity.stage[par1-3]=(short)par2;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2<19){
                //インベントリ内スロット
                if(!this.mergeItemStack(stack, 19, 55, true)) return null;
                slot.onSlotChange(stack, itemStack);
            }
            else{
                //プレイヤーのインベントリ
                if(inventory.isItemValidForSlot(0, stack)) if(!this.mergeItemStack(stack, 0, 9, false)) return null;
                else if(par2>=19 && par2<46) if (!this.mergeItemStack(stack, 46, 55, false)) return null;
                else if(par2>=46 && par2<55) if (!this.mergeItemStack(stack, 19, 46, false)) return null;
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
