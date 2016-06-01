package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.container.ContainerFackingDummy;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.*;

/**
 * Created by plusplus_F on 2015/03/08.
 */
public class TileEntityAutoCrafter extends TileEntityMachineBase implements ISidedInventory, IFluidHandler {
    public static int TANK_CAPACITY=8000;
    public static int TANK_NUM=3;
    public TankIR2Base[] tank;

    private static final int[] slotsMaterial=new int[]{0,1,2,3,4,5,6,7,8};
    private static final int[] slotsProduct=new int[]{9};
    private static final int[] slotsTank=new int[]{11,12,13};

    public ItemStack[] itemStacks=new ItemStack[15];
    public ItemStack[] preItemStack=new ItemStack[9];

    public boolean checkedInventory;
    public Container dummy=new ContainerFackingDummy();
    public InventoryCrafting fackingInventory=new InventoryCrafting(dummy,3,3);

    private HashMap<ItemStack, Integer> nextInsertIndexes;

    public TileEntityAutoCrafter(){
        workAmount=32*5;
        tank=new TankIR2Base[TANK_NUM];
        for(int i=0;i<tank.length;i++){
            tank[i]=new TankIR2Base(TANK_CAPACITY);
        }
        nextInsertIndexes=new HashMap<ItemStack, Integer>();
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

        nbttaglist=(NBTTagList)par1NBTTagCompound.getTag("Tanks");
        tank=new TankIR2Base[TANK_NUM];
        for(int i=0;i<TANK_NUM;i++){
            tank[i]=new TankIR2Base(TANK_CAPACITY);
        }
        for(int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<TANK_NUM){
                tank[b0].readFromNBT(nbt.getCompoundTag("Tank"));
            }
        }


        checkedInventory=false;
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
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

        nbttaglist=new NBTTagList();
        for(int i=0;i<tank.length;i++){
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("Slot", (byte) i);

            NBTTagCompound nbt2=new NBTTagCompound();
            tank[i].writeToNBT(nbt2);
            nbt.setTag("Tank", nbt2);

            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Tanks", nbttaglist);
    }


    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(int k){
        Fluid fluid = tank[k].getFluidType();
        return fluid != null ? fluid.getIcon() : null;
    }

    @Override
    public void updateEntity() {
        for(int i=0;i<preItemStack.length;i++){
            if((preItemStack[i]==null && itemStacks[i]!=null) || (preItemStack[i]!=null && itemStacks[i]==null)){
                checkedInventory=false;
                break;
            }
            if(preItemStack[i]!=null){
                if(!preItemStack[i].isItemEqual(itemStacks[i]) || preItemStack[i].stackSize!=itemStacks[i].stackSize){
                    checkedInventory=false;
                    break;
                }
            }
        }

        if (!checkedInventory && !worldObj.isRemote) {
            checkedInventory = true;
            itemStacks[10] = getResult();

            //ItemStack-Craft Material
            nextInsertIndexes.clear();
            Iterator<Map.Entry<ItemStack, Integer>> it;
            for(int i=0;i<9;i++){
                if(itemStacks[i]==null) continue;
                if(nextInsertIndexes.isEmpty()) nextInsertIndexes.put(itemStacks[i].copy(), i);
                else{
                    boolean find=false, removed=false;
                    it=nextInsertIndexes.entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry<ItemStack, Integer> e=it.next();
                        if(e.getKey().isItemEqual(itemStacks[i])){
                            find=true;
                            if(itemStacks[e.getValue()].stackSize>itemStacks[i].stackSize){
                                nextInsertIndexes.remove(e.getKey());
                                nextInsertIndexes.put(itemStacks[i].copy(), i);
                            }
                            break;
                        }
                    }
                    if(!find){
                        nextInsertIndexes.put(itemStacks[i].copy(), i);
                    }
                }
            }

            for(int i=0;i<preItemStack.length;i++){
                if(itemStacks[i]!=null) preItemStack[i]=itemStacks[i].copy();
                else preItemStack[i]=null;
            }
        }

        //Fluid
        if (!worldObj.isRemote && itemStacks[14] != null) {
            FluidStack fluid2 = FluidContainerRegistry.getFluidForFilledItem(itemStacks[14]);
            if (fluid2 != null && fluid2.getFluid() != null) {

                int index = -1;
                FluidStack resourceCopy = fluid2.copy();
                for (int k = 0; k < tank.length; k++) {
                    FluidStack current = tank[k].getFluid();
                    if (index == -1 && (current == null || current.amount == 0)) {
                        index = k;
                        continue;
                    }
                    if (current!=null && current.isFluidEqual(resourceCopy)) {
                        index = k;
                        break;
                    }
                }
                if (index != -1) {
                    int put = fill(ForgeDirection.UNKNOWN, fluid2, index, false);
                    if (put == fluid2.amount) {
                        int ii = slotsTank[index];
                        ItemStack empty = FluidContainerRegistry.drainFluidContainer(itemStacks[14]);

                        if (itemStacks[ii] == null || (itemStacks[ii].isItemEqual(empty) && itemStacks[ii].stackSize + 1 <= itemStacks[ii].getMaxStackSize())) {
                            fill(ForgeDirection.UNKNOWN, fluid2, index, true);

                            itemStacks[14].stackSize--;
                            if (itemStacks[14].stackSize <= 0) {
                                itemStacks[14] = null;
                            }

                            if (itemStacks[ii] == null) {
                                itemStacks[ii] = new ItemStack(empty.getItem(), 1, empty.getItemDamage());
                            } else {
                                itemStacks[ii].stackSize++;
                            }
                            markDirty();
                        }
                    }
                }
            }
        }
        super.updateEntity();
    }

    @Override
    protected void work() {
        onInventoryChanged();
        if(canWork()) {
            ItemStack p = itemStacks[10];
            if (itemStacks[9] == null) itemStacks[9] = p.copy();
            else itemStacks[9].stackSize += p.stackSize;

            try {
                itemStacks[9].getItem().onCreated(itemStacks[9], worldObj, null);
            }catch (Exception exc){
                FMLLog.severe(exc.toString());
            }

            for (int i = 0; i < 9; i++) {
                if (itemStacks[i] != null) {
                    //fluid
                    FluidStack fluid2 =  FluidContainerRegistry.getFluidForFilledItem(itemStacks[i]);
                    if(fluid2!=null && fluid2.getFluid()!=null){

                        //search same fluid
                        boolean useFluid=false;
                        for(int k=0;k<tank.length;k++){
                            FluidStack fluid=tank[k].getFluid();
                            if(fluid==null) continue;
                            if(fluid.amount< 1000) continue;
                            if(!fluid.isFluidEqual(fluid2)) continue;

                            fluid.amount-=1000;
                            if(fluid.amount==0){
                                tank[k].setFluid(null);
                            }
                            useFluid=true;
                            break;
                        }
                        if(useFluid) continue;

                        /*
                        int put = fill(ForgeDirection.UNKNOWN, fluid2, i-slotsTank[0], false);
                        if (put == fluid2.amount){
                            fill(ForgeDirection.UNKNOWN, fluid2, i-slotsTank[0], true);
                            itemStacks[i]=FluidContainerRegistry.drainFluidContainer(itemStacks[i]);
                        }
                        */
                    }

                    //normal materials
                    itemStacks[i].stackSize--;
                    if (itemStacks[i].stackSize <= 0) {
                        itemStacks[i] = itemStacks[i].getItem().getContainerItem(itemStacks[i]);
                    }
                }
            }
            onInventoryChanged();
        }
    }
    @Override
    public boolean canWork() {
        if(itemStacks[10]==null) return false;

        //
        int[] fAmount=new int[3];
        for(int i=0;i<3;i++) fAmount[i]=tank[i].getFluidAmount();

        for(int i=0;i<9;i++){
            if(itemStacks[i]==null) continue;

            //bucket
            FluidStack fluid2 =  FluidContainerRegistry.getFluidForFilledItem(itemStacks[i]);
            if(fluid2!=null && fluid2.getFluid()!=null){
                boolean find=false;
                for(int k=0;k<3;k++){
                    if(fAmount[k]<1000) continue;
                    if(!tank[k].getFluid().isFluidEqual(fluid2)) continue;

                    find=true;
                    fAmount[k]-=1000;
                    break;
                }
                if(!find){
                    return false;
                }
            }
            else if(itemStacks[i].stackSize<=1){
                return false;
            }
        }

        if (itemStacks[9] == null) return true;
        if (!itemStacks[9].isItemEqual(itemStacks[10])) return false;
        if (itemStacks[9].stackSize + itemStacks[10].stackSize > itemStacks[9].getMaxStackSize()) return false;

        return true;
    }
    @Override
    public String getGuiFileName(){
        return "crafter.png";
    }
    @Override
    public int getStringColor(){
        return 0x404040;
    }

    public void onInventoryChanged(){
        checkedInventory=false;
        /*
        if(worldObj.isRemote) return;
        //FMLLog.severe("slotChanged");
        itemStacks[10]=getResult();
        */
    }

    // f*cking InventoryCrafting

    public ItemStack getResult() {
        for (int i = 0; i < 9; i++) {
            fackingInventory.setInventorySlotContents(i, itemStacks[i]);
        }
        return CraftingManager.getInstance().findMatchingRecipe(fackingInventory, worldObj);
    }

    //-----------------------ISidedInventory---------------------
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
        onInventoryChanged();
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
        onInventoryChanged();
        markDirty();
    }
    @Override
    public String getInventoryName() {
        return I18n.format("gui.crafter");
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
        if(i==9 || i==10) return false;
        if(nextInsertIndexes.isEmpty()) return false;

        Iterator<Map.Entry<ItemStack, Integer>> it=nextInsertIndexes.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ItemStack, Integer> e=it.next();
            if(e.getKey().isItemEqual(itemstack)){
                return i==e.getValue();
            }
        }

        return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1==(int)sideCarrying?slotsProduct:slotsMaterial;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }

    //------------------------------------------------------------------------------------

    public int fill(ForgeDirection from, FluidStack resource, int k, boolean b) {
        if (resource == null || resource.getFluid() == null || k<0 || k>=tank.length) {
            return 0;
        }

        FluidStack current = tank[k].getFluid();
        FluidStack resourceCopy = resource.copy();
        if (current != null && current.amount > 0 && !current.isFluidEqual(resourceCopy)) {
            return 0;
        }

        int i = 0;
        int used = tank[k].fill(resourceCopy, b);
        resourceCopy.amount -= used;
        i += used;

        return i;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean b) {
        if (resource == null || resource.getFluid() == null){
            return 0;
        }
        int indexNull=-1;
        int indexEqual=-1;
        FluidStack resourceCopy = resource.copy();
        for(int k=0;k<tank.length;k++){
            FluidStack current = tank[k].getFluid();
            if(indexNull==-1 && (current==null || current.amount==0)){
                indexNull=k;
                continue;
            }
            if(current!=null && current.isFluidEqual(resourceCopy)){
                indexEqual=k;
                break;
            }
        }
        if(indexEqual!=-1){
            int i = 0;
            int used = tank[indexEqual].fill(resourceCopy, b);
            resourceCopy.amount -= used;
            i += used;
            return i;
        }
        if(indexNull!=-1){
            int i = 0;
            int used = tank[indexNull].fill(resourceCopy, b);
            resourceCopy.amount -= used;
            i += used;
            return i;
        }

        return 0;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection,  FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        /*
        if (tank.getFluidType() == resource.getFluid()) {
            return tank.drain(resource.amount, doDrain);
        }
        */
        return null;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int max, boolean b) {
        return null;
    }
    @Override
    public boolean canFill(ForgeDirection forgeDirection, Fluid fluid) {
        if(fluid==null) return false;
        for(int i=0;i<tank.length;i++){
            if(tank[i].isEmpty()) return true;
        }
        return false;
    }
    @Override
    public boolean canDrain(ForgeDirection forgeDirection, Fluid fluid) {
        return false;
    }
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection forgeDirection) {
        LinkedList<FluidTankInfo> list=new LinkedList<FluidTankInfo>();
        for(int i=0;i<tank.length;i++){
            list.add(tank[i].getInfo());
        }

        FluidTankInfo[] a=new FluidTankInfo[list.size()];
        list.toArray(a);

        return a;
    }

}
