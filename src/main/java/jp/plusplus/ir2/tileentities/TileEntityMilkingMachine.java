package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/07/04.
 *  でも、魅魔様の・・・・・・
 *  GUIはタンクを流用するで
 *  -> 水生成機を流用します
 */
public class TileEntityMilkingMachine extends TileEntityFountain {
    /*
    public static final int TANK_CAPACITY=8000;
    public TankIR2Base tank=new TankIR2Base(TANK_CAPACITY);
    */

    protected short range;
    protected boolean isAdvanced;

    public TileEntityMilkingMachine(){
        workAmount=32*30;
        range=(short)1;
    }
    public TileEntityMilkingMachine(boolean adv){
        isAdvanced=adv;
        workAmount=adv?128*30:32*30;
        range=(short)(adv?2:1);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        range = par1NBTTagCompound.getShort("Range");
        isAdvanced=par1NBTTagCompound.getBoolean("IsAdvanced");

        /*
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
        */
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("Range", range);
        par1NBTTagCompound.setBoolean("IsAdvanced", isAdvanced);

        /*
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
        */
    }


    @Override
    public void work(){
        ForgeDirection dir=ForgeDirection.getOrientation(side);

        //牛の検索, 1頭から絞る
        int x=xCoord+(1+range)*dir.offsetX, z=zCoord+(1+range)*dir.offsetZ;
        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x,yCoord,z, x+1,yCoord+1,z+1).expand(range, range, range);
        List l=worldObj.selectEntitiesWithinAABB(EntityCow.class, aabb, null);
        Iterator it=l.iterator();
        while(it.hasNext()){
            EntityCow e=(EntityCow)it.next();
            if(e.isChild())	continue;

            tank.fill(new FluidStack(BlockCore.fluidMilk, 1000), true);
            return;
        }
    }
    /*
    public boolean canWork(){
        return tank.getFluidAmount()<TANK_CAPACITY;
    }
    */

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) return;

        /*
        //インベントリの確認
        if (itemStacks[0] == null) return;
        if (itemStacks[0].stackSize <= 0) {
            itemStacks[0] = null;
            return;
        }

        //牛乳の容器があれば牛乳を移す。
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
        */
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if(i==0) return FluidContainerRegistry.fillFluidContainer(new FluidStack(BlockCore.fluidMilk, 1000), itemstack)!=null;
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        return BlockCore.fluidMilk.getIcon();
    }

    /*
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
        int d=forgeDirection.ordinal();
        return d!=side && d!=sideCarrying && fluid == BlockCore.fluidMilk && tank.getFluidAmount()<tank.getCapacity();
    }
    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return sideCarrying==forgeDirection.ordinal();
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
        return getBlockType().getLocalizedName();
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
        if(tank.isEmpty()) return true;
        return FluidContainerRegistry.fillFluidContainer(tank.getFluid(), itemstack)!=null;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slots;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side &&  i==0 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i==1;
    }
    */

}
