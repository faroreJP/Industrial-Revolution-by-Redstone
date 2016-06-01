package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class TileEntityCheeseMaker extends TileEntityMachineBase implements IFluidHandler, ISidedInventory {
    public TankIR2Base tank;
    public static int TANK_CAP=8000;

    private static final int[] slotsMaterial=new int[]{0};
    private static final int[] slotsProduct=new int[]{1,2};
    public ItemStack[] itemStacks=new ItemStack[3];

    //copy and copy
    public TileEntityCheeseMaker(){
        tank=new TankIR2Base(TANK_CAP);
        workAmount=32*15;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<itemStacks.length){
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }

        int c=par1NBTTagCompound.getInteger("TankCapacity");
        tank = new TankIR2Base(c);
        if (par1NBTTagCompound.hasKey("Tank")) {
            tank.readFromNBT(par1NBTTagCompound.getCompoundTag("Tank"));
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i=0;i<itemStacks.length;i++){
            if (itemStacks[i] != null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbt);
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        par1NBTTagCompound.setInteger("TankCapacity", tank.getCapacity());
        NBTTagCompound nbt = new NBTTagCompound();
        tank.writeToNBT(nbt);
        par1NBTTagCompound.setTag("Tank", nbt);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if(!worldObj.isRemote){

            //checking inventory
            if (itemStacks[0] == null) return;
            if (itemStacks[0].stackSize <= 0) {
                itemStacks[0] = null;
                return;
            }

            FluidStack fluid2 = FluidContainerRegistry.getFluidForFilledItem(itemStacks[0]);
            if(fluid2 != null && fluid2.getFluid()!=null) {
                int put = fill(ForgeDirection.UNKNOWN, fluid2, false);

                if (put == fluid2.amount) {
                    ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(itemStacks[0]);
                    if (emptyContainer != null) {
                        if (itemStacks[1] != null) {
                            if (!itemStacks[1].isItemEqual(emptyContainer)) return;
                            if (itemStacks[1].stackSize + emptyContainer.stackSize > itemStacks[1].getMaxStackSize()) return;
                            itemStacks[1].stackSize+=emptyContainer.stackSize;
                        }
                        else {
                            setInventorySlotContents(1, emptyContainer);
                        }
                    }

                    itemStacks[0].stackSize--;
                    if(itemStacks[0].stackSize<=0){
                        setInventorySlotContents(0, null);
                    }

                    fill(ForgeDirection.UNKNOWN, fluid2, true);
                    markDirty();
                }
            }
        }
    }

    @Override
    public void work(){
        //牛乳減らす
        tank.drain(250, true);

        //アイテム増やす
        ItemStack pr=new ItemStack(ItemCore.cheese);
        if(itemStacks[2]==null) itemStacks[2]=pr;
        else itemStacks[2].stackSize+=pr.stackSize;
    }

    public boolean canWork(){
        if(tank.getFluidAmount()<250) return false;
        if(tank.getFluid().getFluid()!=BlockCore.fluidMilk) return false;

        if(itemStacks[2]==null) return true;
        if(itemStacks[2].getItem()!=ItemCore.cheese) return false;
        if(itemStacks[2].stackSize>=itemStacks[2].getMaxStackSize()) return false;

        return true;
    }

    public String getGuiFileName(){
        return "cheeseMaker.png";
    }

    //--------------------- IFluidHandler -----------------------

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean b) {
        if (resource == null || resource.getFluid() == null){
            return 0;
        }

        FluidStack current = tank.getFluid();
        FluidStack resourceCopy = resource.copy();
        if (current != null && current.amount > 0 && !current.isFluidEqual(resourceCopy)){
            return 0;
        }

        int i = 0;
        int used = tank.fill(resourceCopy, b);
        resourceCopy.amount -= used;
        i += used;

        return i;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection,  FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (tank.getFluidType() == resource.getFluid()) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int max, boolean b) {
        return tank.drain(max, b);
    }
    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        return fluid != null && fluid==BlockCore.fluidMilk && tank.getFluidAmount()<tank.getCapacity();
    }
    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return true;
    }
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    //------------------ ISidedInventory ---------------------------

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }
    @Override
    public ItemStack getStackInSlot(int i) {
        return itemStacks[i];
    }
    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (itemStacks[i] != null){
            ItemStack itemstack;

            if (itemStacks[i].stackSize <= j){
                itemstack = itemStacks[i];
                itemStacks[i] = null;
                markDirty();
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }
                markDirty();
                return itemstack;
            }
        }
        else{
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (itemStacks[i] != null){
            ItemStack itemstack = itemStacks[i];
            itemStacks[i] = null;
            markDirty();
            return itemstack;
        }
        else{
            return null;
        }
    }
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()){
            itemStack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }
    @Override
    public String getInventoryName() {
        return BlockCore.machineCheeseMaker.getLocalizedName();
    }
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double)xCoord+0.5D, (double)yCoord+0.5D, (double)zCoord+0.5D) <= 64.0D;
    }
    @Override
    public void openInventory() {
    }
    @Override
    public void closeInventory() {
    }
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        FluidStack fs=FluidContainerRegistry.getFluidForFilledItem(itemstack);
        if(fs==null) return false;
        return fs.getFluid()==BlockCore.fluidMilk;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1==sideCarrying?slotsProduct:slotsMaterial;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return i==0 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i!=0 && j==sideCarrying;
    }



}
