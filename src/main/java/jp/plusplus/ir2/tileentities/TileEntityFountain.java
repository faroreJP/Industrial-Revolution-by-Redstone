package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.api.BlockMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by plusplus_F on 2015/05/19.
 */
public class TileEntityFountain extends TileEntityMachineBase implements ISidedInventory, IFluidHandler{
    private static final int[] slotsItem =new int[]{0,2};
    private static final int[] slotsFilled=new int[]{1};
    public ItemStack[] itemStacks=new ItemStack[3];

    public TankIR2Base tank=new TankIR2Base(TANK_CAPACITY);
    public static final int TANK_CAPACITY=8000;

    public boolean lastWorkingState;

    public TileEntityFountain(){
        workAmount=128;
    }


    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        lastWorkingState=par1NBTTagCompound.getBoolean("LastWorkingState");

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<itemStacks.length){
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }

        tank = new TankIR2Base(TANK_CAPACITY);
        if (par1NBTTagCompound.hasKey("Tank")) {
            tank.readFromNBT(par1NBTTagCompound.getCompoundTag("Tank"));
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setBoolean("LastWorkingState", lastWorkingState);

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

        NBTTagCompound nbt = new NBTTagCompound();
        tank.writeToNBT(nbt);
        par1NBTTagCompound.setTag("Tank", nbt);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        //見た目の変化
        if(!lastWorkingState){
            if(lastCanWork && (isPowered || rss>=((BlockMachineBase)getBlockType()).getMinRSS())){
                lastWorkingState=true;
                markDirty();
                worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            }
        }
        else{
            if(!lastCanWork || (!isPowered && rss<((BlockMachineBase)getBlockType()).getMinRSS())){
                lastWorkingState=false;
                markDirty();
                worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            }
        }

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
            tank.setFluid(new FluidStack(FluidRegistry.WATER, 1000));
        }
        else{
            tank.setAmount(tank.getFluidAmount()+1000);
        }
    }
    @Override
    public boolean canWork(){
        return !tank.isFull();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        return Blocks.flowing_water.getIcon(0,0);
    }

    //--------------------------------------------------------------------------------------------------------------


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
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }

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
    }
    @Override
    public String getInventoryName() {
        return BlockCore.machineFountain.getLocalizedName();
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
        if(i==0) return FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, 1000), itemstack)!=null;
        return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1==(int)sideCarrying?slotsFilled:slotsItem;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && i!=1 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }

    //--------------------------------------------------------------------------------------------------------------

    @Override
    public int fill(ForgeDirection forgeDirection, FluidStack fluidStack, boolean b) {
        return 0;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection,  FluidStack resource, boolean doDrain) {
        if (resource == null || forgeDirection.ordinal()==side) {
            return null;
        }
        if (tank.getFluidType() == resource.getFluid()) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int max, boolean b) {
        if(forgeDirection.ordinal()==side) return null;
        return tank.drain(max, b);
    }
    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        return false;
    }
    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return forgeDirection.ordinal()==sideCarrying;
    }
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        return new FluidTankInfo[]{tank.getInfo()};
    }
}
