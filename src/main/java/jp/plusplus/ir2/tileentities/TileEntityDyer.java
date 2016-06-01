package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by plusplus_F on 2015/02/13.
 */
public class TileEntityDyer extends TileEntityMachineBase implements ISidedInventory {
    private static final int[] slotsMaterial=new int[]{0};
    private static final int[] slotsProduct=new int[]{2};
    private static final int[] slotsDye=new int[]{1};

    public static final int DYE_MAX=8;

    public ItemStack[] itemStacks;
    public byte dyeQuantity;
    public byte dyeColor;

    public TileEntityDyer(){
        itemStacks=new ItemStack[3];
        workAmount=32*2;
    }

    @Override
    public boolean hasSlotWithEXP(){ return false; }

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

        dyeQuantity=par1NBTTagCompound.getByte("DyeQuantity");
        dyeColor=par1NBTTagCompound.getByte("DyeColor");
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

        par1NBTTagCompound.setByte("DyeQuantity", dyeQuantity);
        par1NBTTagCompound.setByte("DyeColor", dyeColor);
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote){
            updateDye();
        }
    }

    @Override
    protected void work() {
        if (itemStacks[2] == null) {
            Item item=itemStacks[0].getItem();
            if(item instanceof ItemBlock){
                Block b=((ItemBlock) item).field_150939_a;
                if(b instanceof BlockColored) itemStacks[2] = new ItemStack(itemStacks[0].getItem(), 1, BlockColored.func_150031_c(dyeColor));
                else                            itemStacks[2] = new ItemStack(itemStacks[0].getItem(), 1, dyeColor);
            }
            else {
                itemStacks[2] = new ItemStack(itemStacks[0].getItem(), 1, dyeColor);
            }
        } else{
            itemStacks[2].stackSize += 1;
        }

        itemStacks[0].stackSize--;
        if (itemStacks[0].stackSize <= 0) {
            itemStacks[0] = null;
        }

        dyeQuantity--;
    }
    @Override
    public boolean canWork() {
        if (dyeQuantity <= 0) return false;
        if (itemStacks[0] == null) return false;
        if (itemStacks[0].stackSize <= 0) return false;

        if (!Recipes.isDyingGoods(itemStacks[0])) return false;

        if (itemStacks[2] == null) return true;
        if (itemStacks[2].getItem() != itemStacks[0].getItem()) return false;

        Item item=itemStacks[2].getItem();
        if(item instanceof ItemBlock){
            Block b=((ItemBlock) item).field_150939_a;
            if(b instanceof BlockColored)   return BlockColored.func_150031_c(itemStacks[2].getItemDamage())==dyeColor;
            else                             return itemStacks[2].getItemDamage() == dyeColor;
        }

        if (itemStacks[2].getItemDamage() != dyeColor) return false;
        return itemStacks[2].stackSize + 1 <= itemStacks[2].getMaxStackSize();
    }
    @Override
    public String getGuiFileName(){
        return "dyer.png";
    }
    @Override
    public int getStringColor(){
        return 0x404040;
    }

    protected void updateDye() {
        ItemStack item = itemStacks[1];
        if (item == null) return;

        byte newColor = -1;

        if (item.getItem() == Items.dye) {
            newColor = (byte) item.getItemDamage();
        } else {
            int[] Ids = OreDictionary.getOreIDs(item);
            for (int i = 0; i < ItemDye.field_150923_a.length && newColor == -1; i++) {
                String s = ItemDye.field_150923_a[i];
                String color = "dye" + Character.toUpperCase(s.charAt(0)) + s.substring(1);
                int dId = OreDictionary.getOreID(color);
                for (int k = 0; k < Ids.length; k++) {
                    if (dId == Ids[k]) {
                        newColor = (byte) i;
                        break;
                    }
                }
            }
        }

        if (newColor!=-1 && (dyeQuantity == 0 || dyeColor != newColor)) {
            dyeColor = newColor;
            dyeQuantity = DYE_MAX;

            item.stackSize--;
            if (item.stackSize <= 0) itemStacks[1] = null;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }
    public int getDyeQuantityScaled(int i){
        return i*(int)dyeQuantity/TileEntityDyer.DYE_MAX;
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
        return I18n.format("gui.dyer");
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
        if(i==0)    return Recipes.isDyingGoods(itemstack);
        if(i==1)    return itemstack.getItem()==Items.dye;
        return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if(var1==(int)sideCarrying) return slotsProduct;
        if(var1==1 || (sideCarrying==1 && var1==0)) return slotsDye;
        return slotsMaterial;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }
}
