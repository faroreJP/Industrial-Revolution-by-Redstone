package jp.plusplus.ir2.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.tileentities.TileEntityAutoCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ContainerAutoCrafter extends Container {

    public int lastProgress;
    public int[] lastAmount=new int[3];

    private TileEntityAutoCrafter entity;
    private ISidedInventory inventory;

    public ContainerAutoCrafter(EntityPlayer player, TileEntityAutoCrafter tileEntity) {
        this.entity = tileEntity;
        this.inventory = entity;

        //inventory's inventory
        for(int i=0;i<9;i++){
            this.addSlotToContainer(new Slot(entity, i, 26+18*(i%3),25+18*(i/3)));
        }
        this.addSlotToContainer(new SlotTakeOnly(this.inventory, 9, 134, 42));
        this.addSlotToContainer(new SlotShowOnly(this.inventory, 10, 98, 25));

        for(int i=0;i<entity.tank.length;i++) {
            this.addSlotToContainer(new SlotTakeOnly(entity, 11 + i, 26 + 18 * i, 79));
        }
        this.addSlotToContainer(new Slot(this.inventory, 14, 116, 79));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 114 + i * 18));
            }
        }
        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 172));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.progress & 0xffff);
        par1ICrafting.sendProgressBarUpdate(this, 1, (entity.progress >> 16) & 0xffff);
        for(int i=0;i<3;i++) {
            if(entity.tank[i].getFluid()!=null) {
                par1ICrafting.sendProgressBarUpdate(this, 2 + 2 * i, entity.tank[i].getFluid().getFluidID());
                par1ICrafting.sendProgressBarUpdate(this, 3 + 2 * i, entity.tank[i].getFluidAmount());
            }
            else{
                par1ICrafting.sendProgressBarUpdate(this, 2 + 2 * i, -1);
                par1ICrafting.sendProgressBarUpdate(this, 3 + 2 * i, 0);
            }
        }
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
            for(int k=0;k<3;k++){
                if(lastAmount[k]!=entity.tank[k].getFluidAmount()) {
                    if(entity.tank[k].getFluid()!=null) {
                        icrafting.sendProgressBarUpdate(this, 2 + 2 * k, entity.tank[k].getFluid().getFluidID());
                        icrafting.sendProgressBarUpdate(this, 3 + 2 * k, entity.tank[k].getFluidAmount());
                    }
                    else{
                        icrafting.sendProgressBarUpdate(this, 2 + 2 * k, -1);
                        icrafting.sendProgressBarUpdate(this, 3 + 2 * k, 0);
                    }
                }
            }
        }
        lastProgress = entity.progress;
        for(int k=0;k<3;k++) {
            lastAmount[k] = entity.tank[k].getFluidAmount();
        }
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
        else {
            if (par1 % 2 != 0) entity.tank[(par1 - 2) / 2].setAmount(par2);
            else {
                if (par2 != -1) entity.tank[(par1 - 2) / 2].setFluid(new FluidStack(FluidRegistry.getFluid(par2), 1000));
                else entity.tank[(par1 - 1) / 2].setFluid(null);
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2>=0 && par2<=9){
                if (!this.mergeItemStack(stack, 15, 51, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else if(par2>=11 && par2<=14){
                if (!this.mergeItemStack(stack, 15, 51, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
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
