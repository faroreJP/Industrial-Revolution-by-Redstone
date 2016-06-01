package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/03/08.
 */
public class TileEntityMixer extends TileEntityMachineBase implements ISidedInventory, IFluidHandler {
    public static int TANK_CAPACITY=16000;
    public static int TANK_NUM=3;
    public TankIR2Base[] tank=new TankIR2Base[TANK_NUM];

    private static final int[] slotsMaterial=new int[]{0,1,2,3};
    private static final int[] slotsProduct=new int[]{4,5,6};
    private static final int[] slotsTank=new int[]{2,3,5};

    public ItemStack[] itemStacks=new ItemStack[7];

    public TileEntityMixer(){
        workAmount=32*5;
        for(int i=0;i<tank.length;i++){
            tank[i]=new TankIR2Base(TANK_CAPACITY);
        }
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
        //Fluid
        if (!worldObj.isRemote) {
            //流体コンテナ用スロットにアイテムがあれば処理
            for(int i=0;i<slotsTank.length;i++){
                if(itemStacks[slotsTank[i]]==null) continue;
                ItemStack is1=itemStacks[slotsTank[i]];

                FluidStack fs=FluidContainerRegistry.getFluidForFilledItem(is1);
                if(fs!=null && fs.getFluid()!=null){
                    int put=tank[i].fill(fs, false);
                    if(put==fs.amount){
                        //全てタンクに入るなら処理
                        ItemStack empty=FluidContainerRegistry.drainFluidContainer(is1);

                        //空コンテナスロットに入るなら処理
                        boolean flag=false;
                        if(itemStacks[6]==null) flag=true;
                        else flag=(itemStacks[6].isItemEqual(empty) && itemStacks[6].stackSize+empty.stackSize<empty.getMaxStackSize());

                        if(flag){
                            tank[i].fill(fs, true);

                            if(itemStacks[6]==null) itemStacks[6]=empty.copy();
                            else itemStacks[6].stackSize+=empty.stackSize;

                            itemStacks[slotsTank[i]].stackSize--;
                            if(itemStacks[slotsTank[i]].stackSize<=0) itemStacks[slotsTank[i]]=null;

                            markDirty();
                        }
                    }
                }
                else if(!tank[i].isEmpty()){
                    //こちらは搬出
                    ItemStack is2=FluidContainerRegistry.fillFluidContainer(tank[i].getFluid().copy(), is1);
                    if(is2==null) continue;

                    //タンクから規定の量取り出せるなら処理
                    int cap=FluidContainerRegistry.getContainerCapacity(is2);
                    if(tank[i].getFluidAmount()<cap) continue;

                    //空コンテナスロットに入るなら処理
                    boolean flag=false;
                    if(itemStacks[6]==null) flag=true;
                    else flag=(itemStacks[6].isItemEqual(is2) && itemStacks[6].stackSize+is2.stackSize<is2.getMaxStackSize());

                    if(flag){
                        tank[i].drain(cap, true);

                        if(itemStacks[6]==null) itemStacks[6]=is2.copy();
                        else itemStacks[6].stackSize+=is2.stackSize;

                        itemStacks[slotsTank[i]].stackSize--;
                        if(itemStacks[slotsTank[i]].stackSize<=0) itemStacks[slotsTank[i]]=null;

                        markDirty();
                    }
                }
            }
        }
        super.updateEntity();
    }

    @Override
    protected void work() {
        Object[] mat=getMaterialArray();

        //完成品の生成
        Object ret=Recipes.getMixing(mat);
        if(ret==null) return;
        if(ret instanceof ItemStack){
            ItemStack pr=(ItemStack)ret;

            if(itemStacks[4]==null) itemStacks[4]=pr.copy();
            else itemStacks[4].stackSize+=pr.stackSize;
        }
        else if(ret instanceof FluidStack){
            FluidStack pr=(FluidStack)ret;

            if(tank[2].isEmpty()) tank[2].setFluid(pr.copy());
            else tank[2].fill(pr.copy(), true);
        }

        //材料の消費
        Recipes.consumeMaterialForMixing(mat);
        for(int i=0;i<2;i++){
            if(itemStacks[slotsMaterial[i]]!=null && itemStacks[slotsMaterial[i]].stackSize<=0) itemStacks[slotsMaterial[i]]=null;
            if(tank[i].getFluidAmount()<=0) tank[i].setFluid(null);
        }
    }
    @Override
    public boolean canWork() {
        Object[] mat=getMaterialArray();
        if(mat.length==0) return false;

        Object ret=Recipes.getMixing(mat);
        if(ret==null) return false;

        if(ret instanceof ItemStack){
            ItemStack pr=itemStacks[slotsProduct[0]];
            if(pr==null) return true;
            if(!pr.isItemEqual((ItemStack)ret)) return false;
            return pr.stackSize+((ItemStack) ret).stackSize<=pr.getMaxStackSize();
        }
        if(ret instanceof FluidStack){
            FluidStack pr=tank[1].getFluid();
            if(pr==null) return true;
            if(!pr.isFluidEqual((FluidStack)ret)) return false;
            return pr.amount+((FluidStack) ret).amount<=tank[1].getCapacity();
        }
        return true;
    }
    @Override
    public String getGuiFileName(){
        return "mixer.png";
    }
    @Override
    public int getStringColor(){
        return 0x404040;
    }

    public Object[] getMaterialArray(){
        LinkedList<Object> ls=new LinkedList<Object>();
        for(int i=0;i<2;i++){
            if(itemStacks[slotsMaterial[i]]!=null) ls.add(itemStacks[slotsMaterial[i]]);
            if(!tank[i].isEmpty()) ls.add(tank[i].getFluid());
        }

        Object[] obj=new Object[ls.size()];
        ls.toArray(obj);
        return obj;
    }

    public int getFluidScaled(int index, int sc){
        return sc*tank[index].getFluidAmount()/tank[index].getCapacity();
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
        if(i==4 || i==5) return false;
        if(i==2 || i==3) return FluidContainerRegistry.isContainer(itemstack);
        return true;
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
