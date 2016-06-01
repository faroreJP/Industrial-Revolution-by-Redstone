package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.ChunkLoaderManager;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockTransmitter;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/23.
 * 物体転送機！
 * 実装はやや複雑で、しかも動作は保障されていない！！！！
 */
public class TileEntityTransmitter extends TileEntityMachineBase implements ISidedInventory{
    public static LinkedList<Position> positions=new LinkedList<Position>();
    private static final int[] slots=new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};

    public ItemStack[] itemStacks=new ItemStack[19];
    public LinkedList<ItemStack> packets=new LinkedList<ItemStack>();
    public boolean hasRegistered=false;
    public int lastDestIndex;

    public EntityItem dummy;

    public boolean withLoader;
    public boolean lastState;

    public TileEntityTransmitter(){
        workAmount=512*2;
    }
    public TileEntityTransmitter(boolean withLoader){
        workAmount=512*2;
        this.withLoader=withLoader;
    }


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

        nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Packets");
        packets.clear();
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            packets.offer(ItemStack.loadItemStackFromNBT(nbt));
        }

        lastDestIndex = par1NBTTagCompound.getInteger("LastDestIndex");
        lastState = par1NBTTagCompound.getBoolean("LastState");
        withLoader = par1NBTTagCompound.getBoolean("WithLoader");


        if(par1NBTTagCompound.hasKey("NextItem")){
            dummy=new EntityItem(worldObj);
            dummy.readFromNBT(par1NBTTagCompound.getCompoundTag("NextItem"));
        }
        else{
            dummy=null;
        }
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

        nbttaglist = new NBTTagList();
        Iterator<ItemStack> it = packets.iterator();
        while (it.hasNext()) {
            ItemStack item = it.next();
            NBTTagCompound nbt = new NBTTagCompound();
            item.writeToNBT(nbt);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Packets", nbttaglist);

        par1NBTTagCompound.setInteger("LastDestIndex", lastDestIndex);
        par1NBTTagCompound.setBoolean("LastState", lastState);
        par1NBTTagCompound.setBoolean("WithLoader", withLoader);

        if(dummy!=null){
            NBTTagCompound n1=new NBTTagCompound();
            dummy.writeToNBT(n1);
            par1NBTTagCompound.setTag("NextItem", n1);
        }
    }

    @Override
    public boolean hasCarryingSide(){ return false; }

    @Override
    public void updateEntity(){
        if(!hasRegistered){
            hasRegistered=true;
            if(!worldObj.isRemote){
                positions.add(new Position(xCoord,yCoord,zCoord,worldObj.provider.dimensionId));
            }
        }

        super.updateEntity();

        if(withLoader && !worldObj.isRemote){
            if(lastState!=isWorking()){
                lastState=isWorking();
                markDirty();

                if(lastState) ChunkLoaderManager.setChunkLoader(worldObj, xCoord, yCoord, zCoord);
                else ChunkLoaderManager.removeChunkLoader(worldObj, xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public void work(){
        if(!packets.isEmpty()){
            receivePackets();
        }

        //decide itemSlot
        int itemIndex=-1;
        ItemStack packet=null;
        for(int i=0;i<9;i++){
            ItemStack item=itemStacks[i];
            if(item==null) continue;
            packet=item;
            itemIndex=i;
            break;
        }
        if(packet==null) return;;

        //アイテムの行き先を探す
        LinkedList<TileEntityTransmitter> destList=new LinkedList<TileEntityTransmitter>();
        Iterator<Position> it=positions.iterator();
        int dId=worldObj.provider.dimensionId;
        int count=0;
        while(it.hasNext()){
            count++;
            Position pos=it.next();
            if(pos.equalPos(xCoord, yCoord, zCoord) && pos.dimId==dId){
                //完全に同じものであれば飛ばす
                continue;
            }

            //瑠璃！なぜ瑠璃がここに！？まさか自力で脱出を！？
            World w=DimensionManager.getWorld(pos.dimId);
            if(w.getChunkFromBlockCoords(pos.x, pos.z).isChunkLoaded){
                Block b=w.getBlock(pos.x, pos.y, pos.z);

                if(!(b instanceof BlockTransmitter)){
                    it.remove();
                    continue;
                }
                TileEntityTransmitter e=pos.getTileEntity(w);
                if(e==null){
                    it.remove();
                    continue;
                }

                //彼女は瑠璃ではない
                BlockTransmitter bt=(BlockTransmitter)b;
                if(e.rss>=bt.getMinRSS() && e.rss<=bt.getMaxRSS() && e.frequency>=bt.getMinFrequency() && e.frequency<=bt.getMaxFrequency()) {
                    ItemStack key = e.getStackInSlot(18);
                    if (key == null) continue;
                    if (key.isItemEqual(itemStacks[18])) {
                        destList.add(e);
                    }
                }
            }
        }
        if(destList.isEmpty()) return;

        //アイテムの行き先を決める
        TileEntityTransmitter destination=null;
        if(destList.size()==1){
            destination=destList.getFirst();
            lastDestIndex=0;
        }
        else{
            if(lastDestIndex>=destList.size()){
                lastDestIndex=0;
            }
            //rotates list
            for(int i=0;i<lastDestIndex;i++){
                destList.addLast(destList.getFirst());
                destList.removeFirst();
            }

            while(destList.size()>1){
                TileEntityTransmitter e=destList.getFirst();
                int i=findDestinationInsertableSlot(packet, e);
                if(i!=-1){
                    break;
                }
                destList.removeFirst();
            }
            destination=destList.getFirst();
            lastDestIndex++;
        }

        if(destination==null) return;

        //アイテムの行き先スロットを決める
        int destIndex=findDestinationInsertableSlot(packet, destination);
        if(destIndex==-1) return;

        //アイテムを送る
        //FMLLog.severe("packets:"+destination.packets.size());
        //FMLLog.severe(destination.toString());
        destination.packets.offer(new ItemStack(packet.getItem(), 1, packet.getItemDamage()));

        packet.stackSize--;
        if(packet.stackSize<=0) itemStacks[itemIndex]=null;
    }
    @Override
    public boolean canWork(){
        if(itemStacks[18]!=null){
            dummy=new EntityItem(worldObj, 0,0,0,itemStacks[18].copy());
            dummy.getEntityItem().stackSize=1;
            //dummy.rotationPitch=dummy.rotationYaw=0;
        }
        return hasRegistered && itemStacks[18]!=null;
    }

    //行き先の挿入可能なスロットを探す
    protected int findDestinationInsertableSlot(ItemStack stack, TileEntityTransmitter e){
        int index=-1;
        for(int i=9;i<itemStacks.length-1;i++){
            ItemStack item=e.getStackInSlot(i);
            if(item==null){
                index=i;
                break;
            }
            if(item.isItemEqual(stack) && item.stackSize+1<=item.getMaxStackSize()){
                index=i;
                break;
            }
        }
        return index;
    }
    //送る予定だったやつをこぼす
    protected void SpillPacket(ItemStack packet){
        Random rand = new Random();
        ItemStack itemStack=packet;
        float f = rand.nextFloat() * 0.8F + 0.1F;
        float f1 = rand.nextFloat() * 0.8F + 0.1F;
        float f2 = rand.nextFloat() * 0.8F + 0.1F;

        while (itemStack.stackSize > 0) {
            int k1 = rand.nextInt(21) + 10;

            if (k1 > itemStack.stackSize) {
                k1 = itemStack.stackSize;
            }

            itemStack.stackSize -= k1;
            EntityItem entityitem = new EntityItem(worldObj, (double) ((float) xCoord + f), (double) ((float) yCoord + f1), (double) ((float) zCoord + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));

            if (itemStack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }
    //パケットを受け取る
    protected void receivePackets(){
        //Iterator<ItemStack> it=packets.iterator();
        while(!packets.isEmpty()){
            ItemStack item=packets.poll();
            boolean flag=false;

            for(int i=9;i<18;i++){
                if(itemStacks[i]==null){
                    itemStacks[i]=item.copy();
                    flag=true;
                    break;
                }
                else if(itemStacks[i].isItemEqual(item) && itemStacks[i].stackSize+1<=getInventoryStackLimit()){
                    itemStacks[i].stackSize++;
                    flag=true;
                    break;
                }
            }

            if(!flag){
                SpillPacket(item);
            }
            //it.remove();
        }
        markDirty();
    }

    //----------------------------ISidedInventory------------------------------
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
        return true;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return slots;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && j!=(side^1) && i<9 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=side && j!=(side^1) && i>=9 && i<18;
    }

    //物体転送機の設置dimensionと座標
    private class Position{
        int x,y,z;
        int dimId;

        private Position(int x,int y,int z, int dimId){
            this.x=x;
            this.y=y;
            this.z=z;
            this.dimId=dimId;
        }
        private Position(){

        }
        private TileEntityTransmitter getTileEntity(World w){
            TileEntity t=w.getTileEntity(x,y,z);
            if(t instanceof TileEntityTransmitter) return (TileEntityTransmitter)t;
            return null;
        }
        public boolean equalPos(int x, int y, int z){
            return this.x==x && this.y==y && this.z==z;
        }
        public boolean equals(Object o){
            if(!(o instanceof Position)) return false;
            Position p=(Position)o;
            return x==p.x && y==p.y && z==p.z && dimId==p.dimId;
        }
    }
}
