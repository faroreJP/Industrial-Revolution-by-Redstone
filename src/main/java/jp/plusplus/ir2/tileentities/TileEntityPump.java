package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
 * Created by plusplus_F on 2015/08/10.
 * 僕は信じてる。このクラスがきちんと動作してくれる事を。
 */
public class TileEntityPump extends TileEntityMachineBase implements IFluidHandler, ISidedInventory{
    public static final int RANGE=16;
    public static final int TANK_CAPACITY=16000;
    public TankIR2Base tank;

    private static final int[] slots=new int[]{0, 2};
    private static final int[] slotsProduct=new int[]{1};
    public ItemStack[] itemStacks=new ItemStack[3];

    public TileEntityPump(){
        tank=new TankIR2Base(TANK_CAPACITY);
        workAmount=32;
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

    protected int getNextY(){
        int x=xCoord;
        int y=yCoord-1;
        int z=zCoord;

        Block b=worldObj.getBlock(x,y,z);
        while(b== BlockCore.pipeMining){
            y--;
            b=worldObj.getBlock(x,y,z);
        }
        return y;
    }

    @Override
    public boolean canWork() {
        int y = getNextY();
        Block b = worldObj.getBlock(xCoord, y, zCoord);

        //流体ならtrue
        if(b.getMaterial()==Material.water || b.getMaterial() == Material.lava) return true;
        if(b instanceof IFluidBlock) return true;

        //中空ならtrue
        if (b.getMaterial()== Material.air) return true;

        b = worldObj.getBlock(xCoord, y + 1, zCoord);
        return b == BlockCore.pipeMining && worldObj.getBlockMetadata(xCoord, y + 1, zCoord) != 15;
    }

    @Override
    public void work(){
        boolean flag=false;
        int y=getNextY();
        int meta=worldObj.getBlockMetadata(xCoord, y + 1, zCoord);

        if(y+1==yCoord) {
            flag=true;
        }
        else{
            flag=(meta==15);
        }

        if(flag) {
            if(worldObj.isAirBlock(xCoord,y,zCoord)) setPipe(y);
            else tryDrain(xCoord, y, zCoord);
        }
        else{
            worldObj.setBlockMetadataWithNotify(xCoord,y+1,zCoord, meta+3, 2);
        }
    }

    //
    public void tryDrain(int x, int y, int z){
        LinkedList<MultiBlockPosition> list=new LinkedList<MultiBlockPosition>(); //調査対象座標
        LinkedList<MultiBlockPosition> closed=new LinkedList<MultiBlockPosition>(); //調査済み座標
        LinkedList<MultiBlockPosition> fluids=new LinkedList<MultiBlockPosition>(); //吸い上げ対象となりうるブロック
        MultiBlockPosition pos, pos2, max;
        ForgeDirection fd;
        Block b;
        Material m;

        //初期化。ここではMultiBlockPosition.sideは距離として扱う
        pos2=new MultiBlockPosition(x,y,z,0, true);
        b=worldObj.getBlock(x,y,z);
        m=b.getMaterial();
        if(b instanceof IFluidBlock){
            if(tank.isEmpty() || ((IFluidBlock) b).getFluid()==tank.getFluidType()){
                list.add(pos2);
                if(((IFluidBlock) b).canDrain(worldObj, pos2.x, pos2.y, pos2.z)){
                    fluids.add(pos2);
                }
            }
        }
        else if(m==Material.water){
            if(tank.isEmpty() || tank.getFluidType()==FluidRegistry.WATER){
                list.add(pos2);
                if(worldObj.getBlockMetadata(pos2.x, pos2.y, pos2.z)==0){
                    fluids.add(pos2);
                }
            }
        }
        else if(m==Material.lava){
            if(tank.isEmpty() || tank.getFluidType()==FluidRegistry.LAVA){
                list.add(pos2);
                if(worldObj.getBlockMetadata(pos2.x, pos2.y, pos2.z)==0){
                    fluids.add(pos2);
                }
            }
        }

        //リストで調査
        while(!list.isEmpty()){
            //調査リストから取り出す
            pos=list.pollFirst();
            closed.add(pos);

            //四方について調べる
            for(int i=0;i<4;i++){
                //ずれた座標が調査済みでなければ調査リストに突っ込む
                fd=ForgeDirection.getOrientation(i+2);
                pos2=new MultiBlockPosition(pos.x+fd.offsetX, pos.y+fd.offsetY, pos.z+fd.offsetZ, pos.side+1, true);
                if(closed.contains(pos2)) continue;

                //流体のブロックなら突っ込む
                b=worldObj.getBlock(pos2.x, pos2.y, pos2.z);
                m=b.getMaterial();
                if(b instanceof IFluidBlock){
                    if(tank.isEmpty() || ((IFluidBlock) b).getFluid()==tank.getFluidType()){
                        list.add(pos2);
                        if(((IFluidBlock) b).canDrain(worldObj, pos2.x, pos2.y, pos2.z)){
                            fluids.add(pos2);
                        }
                    }
                }
                else if(m==Material.water){
                    if(tank.isEmpty() || tank.getFluidType()==FluidRegistry.WATER){
                        list.add(pos2);
                        if(worldObj.getBlockMetadata(pos2.x, pos2.y, pos2.z)==0){
                            fluids.add(pos2);
                        }
                    }
                }
                else if(m==Material.lava){
                    if(tank.isEmpty() || tank.getFluidType()==FluidRegistry.LAVA){
                        list.add(pos2);
                        if(worldObj.getBlockMetadata(pos2.x, pos2.y, pos2.z)==0){
                            fluids.add(pos2);
                        }
                    }
                }
            }
        }
        if(fluids.isEmpty()) return;

        //fluidsの中から最遠点を求める
        //基本的に一番最後の要素が最遠点
        max=fluids.getLast();

        //最遠点（多分）が流体なら吸う
        //ここのブロックは確実に吸える
        b=worldObj.getBlock(max.x, max.y, max.z);
        if(b instanceof IFluidBlock){
            IFluidBlock fb=(IFluidBlock)b;
            FluidStack fs=fb.drain(worldObj, max.x, max.y, max.z, false);
            if(fb.getFluid()!=null && fs!=null){
                //タンク内に流体的にも容量的にも入るのなら入れる
                if((tank.isEmpty() || (fb.getFluid()==tank.getFluidType() && tank.getFluidAmount()+fs.amount<=TANK_CAPACITY))) {
                    fs = fb.drain(worldObj, max.x, max.y, max.z, true);
                    tank.fill(fs, true);
                }
            }
        }
        else if(b.getMaterial()==Material.water){
            Fluid f=FluidRegistry.WATER;
            FluidStack fs=new FluidStack(f, 1000);
            if(fs!=null){
                //タンク内に流体的にも容量的にも入るのなら入れる
                if((tank.isEmpty() || (f==tank.getFluidType() && tank.getFluidAmount()+fs.amount<=TANK_CAPACITY))) {
                    worldObj.setBlockToAir(max.x, max.y, max.z);
                    tank.fill(fs, true);
                }
            }
        }
        else if(b.getMaterial()==Material.lava){
            Fluid f=FluidRegistry.LAVA;
            FluidStack fs=new FluidStack(f, 1000);
            if(fs!=null){
                //タンク内に流体的にも容量的にも入るのなら入れる
                if((tank.isEmpty() || (f==tank.getFluidType() && tank.getFluidAmount()+fs.amount<=TANK_CAPACITY))) {
                    worldObj.setBlockToAir(max.x, max.y, max.z);
                    tank.fill(fs, true);
                }
            }
        }

        //吸った座標がポンプ直下ならパイプを置く
        if(max.side==0) setPipe(max.y);
    }

    public void setPipe(int y){
        worldObj.setBlock(xCoord, y, zCoord, BlockCore.pipeMining, 0, 2);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
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
    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
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
        return false;
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
        return BlockCore.machineMiner.getLocalizedName();
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
        return var1==sideCarrying?slotsProduct:slots;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return i==0 && j!=side && j!=sideCarrying && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i==1 && j==sideCarrying;
    }

}
