package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityCrusher extends TileEntityMachineBase implements ISidedInventory{
    private static final int[] slotsMaterial=new int[]{0};
    private static final int[] slotsProduct=new int[]{1,2,3};

    public ItemStack[] itemStacks=new ItemStack[4];
    public boolean isAdvanced;

    //
    public EntityItem materialItem;

    public TileEntityCrusher(){
        workAmount=128*15;
        isAdvanced=true;
    }
    public TileEntityCrusher(boolean adv){
        isAdvanced=adv;
        workAmount=adv?128*15:32*15;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0>=0 && b0<itemStacks.length){
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        materialItem=null;
        if(par1NBTTagCompound.hasKey("MaterialItem")){
            materialItem=new EntityItem(worldObj);
            materialItem.readFromNBT(par1NBTTagCompound.getCompoundTag("MaterialItem"));
        }

        if(par1NBTTagCompound.hasKey("IsAdvanced")) isAdvanced=par1NBTTagCompound.getBoolean("IsAdvanced");
        else isAdvanced=true;
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i=0;i<itemStacks.length;i++){
            if (itemStacks[i] != null){
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                itemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        if(materialItem!=null){
            NBTTagCompound nbt=new NBTTagCompound();
            materialItem.writeToNBT(nbt);
            par1NBTTagCompound.setTag("MaterialItem", nbt);
        }

        par1NBTTagCompound.setBoolean("IsAdvanced", isAdvanced);
    }

    @Override
    public void updateEntity(){
        super.updateEntity();

        //破砕エフェクトを出す
        if(IR2.proxy.getClientWorld()!=null && worldObj.isRemote && isWorking() && materialItem!=null){
            createParticle();
        }
    }

    @Override
    protected void work() {
        ItemStack[] products=Recipes.getCrushingProducts(itemStacks[0], isAdvanced?1.5f:1.0f);
        for(int i=0;i<3;i++) {
            if (products[i] != null) {
                if (itemStacks[1 + i] == null) {
                    itemStacks[1 + i] = products[i];
                } else if (itemStacks[1 + i].isItemEqual(products[i])) {
                    itemStacks[1 + i].stackSize += products[i].stackSize;
                }
            }
        }

        itemStacks[0].stackSize--;
        if (itemStacks[0].stackSize <= 0) {
            itemStacks[0] = null;
        }
    }
    @Override
    public boolean canWork() {
        materialItem=null;
        if (itemStacks[0] == null) return false;

        Object[] products = Recipes.findCrushing(itemStacks[0]);
        if (products == null) {
            return false;
        }
        //if (itemStacks[0].stackSize < 1) return false;

        for (int i = 0; i < products.length/2; i++) {
            if (itemStacks[1 + i] == null) continue;

            ItemStack p=(ItemStack) products[2 * i + 1];
            if (!itemStacks[1 + i].isItemEqual(p)) return false;

            int result = itemStacks[1 + i].stackSize + p.stackSize;
            if (!(result <= getInventoryStackLimit() && result <= p.getMaxStackSize())) {
                return false;
            }
        }

        materialItem=new EntityItem(worldObj, 0,0,0,itemStacks[0].copy());
        materialItem.getEntityItem().stackSize=1;
        return true;
    }
    public String getGuiFileName(){
        return isAdvanced?"crusher2.png":"crusher2_NEI.png";
    }
    public int getStringColor(){
        return isAdvanced?0xffffff:0x404040;
    }

    @SideOnly(Side.CLIENT)
    protected void createParticle(){
        ItemStack iStack=materialItem.getEntityItem();
        Item item=iStack.getItem();
        EntityFX fx;
        if(item instanceof ItemBlock) {
            fx = new EntityBlockDustFX(worldObj, xCoord + 0.5f, yCoord + 1.5f, zCoord + 0.5f, 0, 0, 0, ((ItemBlock) item).field_150939_a, iStack.getItemDamage());

            //なんか初期設定されないんでムリヤリ
            fx.motionX = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.4F);
            fx.motionY = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.4F);
            fx.motionZ = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.4F);

            float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
            float f1 = MathHelper.sqrt_double(fx.motionX * fx.motionX + fx.motionY * fx.motionY + fx.motionZ * fx.motionZ);
            fx.motionX = fx.motionX / (double)f1 * (double)f * 0.4000000059604645D;
            fx.motionY = fx.motionY / (double)f1 * (double)f * 0.4000000059604645D + 0.10000000149011612D;
            fx.motionZ = fx.motionZ / (double)f1 * (double)f * 0.4000000059604645D;
        }
        else{
            fx=new EntityBreakingFX(worldObj, xCoord+0.5f,yCoord+1.5f,zCoord+0.5f, item);
        }

        //worldObj.spawnEntityInWorld(fx);
        Minecraft.getMinecraft().effectRenderer.addEffect((EntityFX) fx);
    }

    //-----------------------------ISidedInventory-----------------------------
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
        return I18n.format("gui.crusher");
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
        return i==0 && Recipes.findCrushing(itemstack)!=null;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1!=(int)sideCarrying?slotsMaterial:slotsProduct;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if(j==side)	return false;
        return isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }
}
