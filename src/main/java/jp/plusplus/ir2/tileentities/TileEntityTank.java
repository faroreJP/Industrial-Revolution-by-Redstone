package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by plusplus_F on 2015/05/16.
 */
public class TileEntityTank extends TileEntity implements IFluidHandler, ISidedInventory {
    public TankIR2Base tank;

    private static final int[] slots=new int[]{0,1};
    public ItemStack[] itemStacks=new ItemStack[2];

    //copy and copy

    public TileEntityTank(){
        tank=new TankIR2Base(1000);
    }
    public TileEntityTank(int c){
        tank=new TankIR2Base(c);
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
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @SideOnly(Side.CLIENT)
    public int getMetadata(){
        return this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }
    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
    }


    @Override
    public void updateEntity() {
        if(worldObj.isRemote) return;

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
        else{
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
        return fluid != null && tank.getFluidAmount()<tank.getCapacity();
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
        return I18n.format("gui.ir2tank");
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

        if(tank.isEmpty()) return true;
        return fs.getFluid()==tank.getFluidType();
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slots;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return i==0 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i==1;
    }



}
