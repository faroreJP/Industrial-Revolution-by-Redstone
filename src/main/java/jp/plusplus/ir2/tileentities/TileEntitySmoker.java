package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by plusplus_F on 2015/07/04.
 * 燻製窯。ぶっちゃけRS機械である必要はないと思う。
 */
public class TileEntitySmoker extends TileEntityMachineBase implements ISidedInventory{
    private static final int[] slotsMaterial=new int[]{0,1,2,3,4,5,6,7,8,18};
    private static final int[] slotsProduct=new int[]{9,10,11,12,13,14,15,16,17};

    public ItemStack[] itemStacks=new ItemStack[19];

    public short burnTime, itemBurnTime;

    public short[] stage=new short[9];      //各スロット進捗
    public static final short STAGE_MAX=10;

    public TileEntitySmoker(){
        workAmount=32*120/STAGE_MAX; // だいたい32Hz時に2分で1度燻製が終わる
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

        for(int i=0;i<stage.length;i++){
            stage[i]=par1NBTTagCompound.getShort("Stage"+i);
        }

        burnTime=par1NBTTagCompound.getShort("BurnTime");
        itemBurnTime=par1NBTTagCompound.getShort("ItemBurnTime");
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

        for(int i=0;i<stage.length;i++){
            par1NBTTagCompound.setShort("Stage"+i, stage[i]);
        }

        par1NBTTagCompound.setShort("BurnTime", burnTime);
        par1NBTTagCompound.setShort("ItemBurnTime", itemBurnTime);
    }

    @Override
    public void updateEntity(){
        super.updateEntity();

        boolean burn=isBurning();

        //燃やす
        if(burn){
            burnTime--;
            if(burnTime<=0) markDirty();
        }

        //新しく燃やす
        int bIndex=18;
        if(worldObj.isRemote) return;
        if(itemStacks[bIndex]==null) return;
        if(burn) return;

        short b=Recipes.GetSmokingFuelBurnTime(itemStacks[bIndex]);
        if(b>0){
            burnTime=itemBurnTime=b;

            if (itemStacks[bIndex] != null){
                itemStacks[bIndex].stackSize--;
                if(itemStacks[bIndex].stackSize<=0) {
                    itemStacks[bIndex] = null;
                }
            }

            markDirty();
        }
    }

    @Override
    public void work(){
        //全ての材料スロットに対してstageを1つ進める
        for(int i=0;i<9;i++){
            if(itemStacks[i]==null) continue;

            //作業できるかの確認
            ItemStack ret= Recipes.getSmoking(itemStacks[i]);
            if(ret==null) continue;
            if(itemStacks[i+9]!=null && (!itemStacks[i+9].isItemEqual(ret) || itemStacks[i+9].stackSize+ret.stackSize>ret.getMaxStackSize())){
                continue;
            }

            //進める
            stage[i]++;

            //完成
            if(stage[i]==STAGE_MAX){
                if(itemStacks[i+9]==null){
                    itemStacks[i+9]=ret.copy();
                }
                if(itemStacks[i+9].isItemEqual(ret) && itemStacks[i+9].stackSize+ret.stackSize<=ret.getMaxStackSize()){
                    itemStacks[i+9].stackSize+=ret.stackSize;
                }

                //材料の消費
                itemStacks[i].stackSize--;
                if(itemStacks[i].stackSize<=0) itemStacks[i]=null;

                //
                stage[i]=0;
            }
        }
    }

    @Override
    public boolean canWork(){
        //加熱状況の確認
        if(!isBurning()){
            return false;
        }

        boolean flag=false;

        //材料の確認と、stageのリセット
        for(int i=0;i<9;i++) {
            if(itemStacks[i]==null){
                stage[i]=0;
                continue;
            }

            //完成品の確認
            ItemStack ret= Recipes.getSmoking(itemStacks[i]);
            if(ret==null){
                stage[i]=0;
                continue;
            }

            if(itemStacks[i+9]==null){
                flag=true;
                continue;
            }
            if(!itemStacks[i+9].isItemEqual(ret) || itemStacks[i+9].stackSize+ret.stackSize>ret.getMaxStackSize()){
                stage[i]=0;
                continue;
            }

            flag=true;
        }

        return flag;
    }

    public boolean isBurning(){
        return itemBurnTime>0 && burnTime>0;
    }
    public int getBurnTimeScaled(int p){
        if(itemBurnTime==0) return 0;
        return p*burnTime/itemBurnTime;
    }
    public int getStageScaled(int index, int p){
        return p*stage[index]/STAGE_MAX;
    }

    public String getGuiFileName(){
        return "smoker.png";
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
        if(i<9 && Recipes.getSmoking(itemstack)!=null) return true;
        if(i==18 && Recipes.GetSmokingFuelBurnTime(itemstack)>0) return true;
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
        return j==sideCarrying;
    }
}
