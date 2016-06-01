package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockAlloySmelterRusty;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.*;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by plusplus_F on 2015/02/02.
 */
public class TileEntityAlloySmelterRusty extends TileEntity  implements ISidedInventory,IDirectional {
    private static final int[] slotsMaterial = new int[]{0, 1};
    private static final int[] slotsProduct = new int[]{2};
    private static final int[] slotsFuel = new int[]{3};

    private static final int MATERIAL_AMOUNT = 4;
    private static final int COOK_TIME =20*30;

    public ItemStack[] itemStacks = new ItemStack[4];
    public short progress;
    public short burnTime;
    public short itemBurnTime;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0 >= 0 && b0 < itemStacks.length) {
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }

        progress    =   par1NBTTagCompound.getShort("Progress");
        burnTime    =   par1NBTTagCompound.getShort("BurnTime");
        itemBurnTime    =   par1NBTTagCompound.getShort("ItemBurnTime");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbt);
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        par1NBTTagCompound.setShort("Progress", progress);
        par1NBTTagCompound.setShort("BurnTime", burnTime);
        par1NBTTagCompound.setShort("ItemBurnTime", itemBurnTime);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound tag=new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        this.readFromNBT(pkt.func_148857_g());
    }


    public int getBurnTimeScaled(int par1){
        return itemBurnTime>0?(burnTime*par1/itemBurnTime):0;
    }
    public int getProgressScaled(int par1){
        return progress*par1/COOK_TIME;
    }
    public static short getItemBurnTime(ItemStack itemStack){
        if (itemStack == null){
            return 0;
        }
        else{
            Item i = itemStack.getItem();
            if (i == Items.coal){
                return 80*20;
            }
            else{
                return 0;
            }
        }
    }

    @Override
    public void updateEntity(){
        boolean burnFlag=isBurning();
        boolean flag1=false;
        boolean canWorkFlag=canWork();

        if(burnFlag){
            burnTime--;
        }

        if(!this.worldObj.isRemote){
            if(burnTime == 0 && canWorkFlag){
                itemBurnTime = burnTime = getItemBurnTime(itemStacks[3]);

                if(burnTime > 0){
                    flag1 = true;

                    if (itemStacks[3] != null){
                        itemStacks[3].stackSize--;
                        if(itemStacks[3].stackSize<=0) {
                            itemStacks[3] = null;
                        }
                    }
                }

                if(flag1 && !burnFlag){
                    // off -> on
                    changeBlockState(true);
                }
            }
            if(burnTime==0 && burnFlag){
                // on -> off
                changeBlockState(false);
            }

            if(this.isBurning()) {
                if (canWorkFlag) {
                    progress++;
                    if (progress == COOK_TIME) {
                        work();
                    }
                } else {
                    progress = 0;
                    /*
                    if(!checkEstablishingState()){
                        burnTime=0;
                        changeBlockState(false);
                    }
                    */
                }
            }
        }

        if(flag1){
            ;
        }
    }

    protected void work() {
        ItemStack ingot;
        if (itemStacks[0].getItem() == Items.redstone) {
            ingot = itemStacks[1];

            itemStacks[1].stackSize -= 1;
            if (itemStacks[1].stackSize <= 0) itemStacks[1] = null;
            itemStacks[0].stackSize -= MATERIAL_AMOUNT;
            if (itemStacks[0].stackSize <= 0) itemStacks[0] = null;
        } else {
            ingot = itemStacks[0];

            itemStacks[0].stackSize -= 1;
            if (itemStacks[0].stackSize <= 0) itemStacks[0] = null;
            itemStacks[1].stackSize -= MATERIAL_AMOUNT;
            if (itemStacks[1].stackSize <= 0) itemStacks[1] = null;
        }

        ItemStack product = Recipes.getAlloying(ingot);

        if (product != null) {
            if (itemStacks[2] == null) {
                itemStacks[2] = product.copy();
            } else if (itemStacks[2].isItemEqual(product)) {
                itemStacks[2].stackSize += product.stackSize;
            }
        }
        progress = 0;
    }

    protected boolean checkEstablishingState(){
        Block[] b=new Block[11];
        int x=xCoord;
        int y=yCoord;
        int z=zCoord;
        int i;

        int meta=(worldObj.getBlockMetadata(x,y,z)&7);

        switch (meta) {
            case 2:
                //south
                for (i = 0; i < 2; i++) {
                    b[0 + 5 * i] = worldObj.getBlock(x - 1, y + i, z);
                    b[1 + 5 * i] = worldObj.getBlock(x + 1, y + i, z);
                    b[2 + 5 * i] = worldObj.getBlock(x - 1, y + i, z + 1);
                    b[3 + 5 * i] = worldObj.getBlock(x, y + i, z + 1);
                    b[4 + 5 * i] = worldObj.getBlock(x + 1, y + i, z + 1);
                }
                b[10] = worldObj.getBlock(x, y + 1, z);
                break;

            case 3:
                //north
                for (i = 0; i < 2; i++) {
                    b[0 + 5 * i] = worldObj.getBlock(x - 1, y + i, z);
                    b[1 + 5 * i] = worldObj.getBlock(x + 1, y + i, z);
                    b[2 + 5 * i] = worldObj.getBlock(x - 1, y + i, z - 1);
                    b[3 + 5 * i] = worldObj.getBlock(x, y + i, z - 1);
                    b[4 + 5 * i] = worldObj.getBlock(x + 1, y + i, z - 1);
                }
                b[10] = worldObj.getBlock(x, y + 1, z);
                break;

            case 4:
                //east
                for (i = 0; i < 2; i++) {
                    b[0 + 5 * i] = worldObj.getBlock(x, y + i, z - 1);
                    b[1 + 5 * i] = worldObj.getBlock(x, y + i, z + 1);
                    b[2 + 5 * i] = worldObj.getBlock(x + 1, y + i, z - 1);
                    b[3 + 5 * i] = worldObj.getBlock(x + 1, y + i, z);
                    b[4 + 5 * i] = worldObj.getBlock(x + 1, y + i, z + 1);
                }
                b[10] = worldObj.getBlock(x, y + 1, z);
                break;

            case 5:
                //west
                for (i = 0; i < 2; i++) {
                    b[0 + 5 * i] = worldObj.getBlock(x, y + i, z - 1);
                    b[1 + 5 * i] = worldObj.getBlock(x, y + i, z + 1);
                    b[2 + 5 * i] = worldObj.getBlock(x - 1, y + i, z - 1);
                    b[3 + 5 * i] = worldObj.getBlock(x - 1, y + i, z);
                    b[4 + 5 * i] = worldObj.getBlock(x - 1, y + i, z + 1);
                }
                b[10] = worldObj.getBlock(x, y + 1, z);
                break;

            default:
                return false;
        }

        for (i=0;i<b.length;i++){
            if(b[i]!=BlockCore.brickRusty){
                return false;
            }
        }

        return true;
    }
    public boolean isBurning(){
        return burnTime > 0 && itemBurnTime>0;
    }
    public static boolean isItemFuel(ItemStack par0ItemStack){
        return getItemBurnTime(par0ItemStack) > 0;
    }

    protected boolean canWork() {
        if (itemStacks[0] == null || itemStacks[1] == null) return false;
        if (itemStacks[0].stackSize <= 0 || itemStacks[1].stackSize <= 0) return false;

        ItemStack ingot;
        ItemStack redstone;
        if (itemStacks[0].getItem() == Items.redstone) {
            ingot = itemStacks[1];
            redstone = itemStacks[0];
        } else if (itemStacks[1].getItem() == Items.redstone) {
            ingot = itemStacks[0];
            redstone = itemStacks[1];
        } else {
            return false;
        }

        if (redstone.stackSize < MATERIAL_AMOUNT) return false;

        //指定のインゴットのみ！
        boolean flag=false;
        if(ingot.getItem()==Items.iron_ingot) flag=true;
        else{
            if(ingot.getItem()== ItemCore.ingot && (ingot.getItemDamage()==0 || ingot.getItemDamage()==1)){
                flag=true;
            }
            else{
                int[] ids=OreDictionary.getOreIDs(ingot);
                int tin=OreDictionary.getOreID("ingotTin");
                int copper=OreDictionary.getOreID("ingotCopper");
                for(int i : ids){
                    if(i==tin || i==copper) {
                        flag=true;
                        break;
                    }
                }
            }
        }
        if(!flag) return false;

        ItemStack result = Recipes.getAlloying(ingot);
        if (result == null) return false;

        //block
        if(!checkEstablishingState())   return false;

        if (itemStacks[2] == null) return true;
        if (!itemStacks[2].isItemEqual(result)) return false;
        return itemStacks[2].stackSize + 1 < itemStacks[2].getMaxStackSize();
    }

    private void changeBlockState(boolean active){
        int meta=worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        BlockAlloySmelterRusty.setKeepInventory(true);
        worldObj.setBlock(xCoord, yCoord, zCoord, active? BlockCore.alloySmelterRustyActive:BlockCore.alloySmelterRustyIdle);
        BlockAlloySmelterRusty.setKeepInventory(false);
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
        validate();
        worldObj.setTileEntity(xCoord, yCoord, zCoord, this);

        //FMLLog.severe("change->"+active);
        //BlockFurnace
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
        if (itemStacks[i] != null) {
            ItemStack itemstack;

            if (itemStacks[i].stackSize <= j) {
                itemstack = itemStacks[i];
                itemStacks[i] = null;
                markDirty();
                return itemstack;
            } else {
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0) {
                    itemStacks[i] = null;
                }

                markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (itemStacks[i] != null) {
            ItemStack itemstack = itemStacks[i];
            itemStacks[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.alloySmelterRusty");
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i != 0 && i != 1) return false;
        return itemstack.getItem() == Items.redstone || Recipes.getAlloying(itemstack) != null;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if(var1==0)   return slotsProduct;
        if(var1==1)   return slotsMaterial;
        return slotsFuel;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j != (blockMetadata&7) && isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j != (blockMetadata&7);
    }

    @Override
    public byte getSide() {
        return (byte)(blockMetadata&7);
    }
    @Override
    public void setSide(byte s) {
        blockMetadata = ((blockMetadata & 8) | s);
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, blockMetadata, 2);
    }
    @Override
    public boolean isFront(byte s) {
        return (blockMetadata&7)==s;
    }
}
