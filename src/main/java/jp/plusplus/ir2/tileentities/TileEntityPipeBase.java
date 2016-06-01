package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.api.IPipe;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockPipe;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/06.
 * アイテムを搬送するパイプの基底クラス。実装がだいぶアレ
 */
public class TileEntityPipeBase extends TileEntityCable implements IPipe {
    public static final int PACKET_MAX_CAPACITY=64;

    protected LinkedList<PacketItemStack> itemQueue;
    public byte connectStatePipe;
    protected short offerCount;
    protected short transportDelay=0;
    protected Random random;
    protected int nextDirectionIndex;
    protected LinkedList<Integer> connectDirections;
    protected int delay=0;

    protected int pIndex=-1, pIndexCenter=-1;
    private EntityItem dummyEntity;

    public TileEntityPipeBase(){
        itemQueue=new LinkedList<PacketItemStack>();
        random=new Random();
        connectDirections=new LinkedList<Integer>();
    }

    public void offerPacket(PacketItemStack packet){
        if(packet!=null && packet.getItemStack()!=null && packet.getItemStack().stackSize>0) {
            itemQueue.add(packet);
            offerCount++;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        PacketItemStack[] itemStacks=new PacketItemStack[nbttaglist.tagCount()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<nbttaglist.tagCount()){
                itemStacks[b0]=new PacketItemStack(null);
                itemStacks[b0].readFromNBT(nbt);
            }
        }

        itemQueue=new LinkedList<PacketItemStack>();
        for(int i=0;i<itemStacks.length;i++){
            itemQueue.add(itemStacks[i]);
        }

        offerCount=par1NBTTagCompound.getShort("OfferCount");
        transportDelay=par1NBTTagCompound.getShort("TransportDelay");
        nextDirectionIndex =par1NBTTagCompound.getInteger("NextDirection");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        //par1NBTTagCompound.setInteger("QueueSize", itemQueue.size());

        NBTTagList nbttaglist = new NBTTagList();
        int size=itemQueue.size();
        for(int i=0;i<size;i++){
            PacketItemStack packet=itemQueue.get(i);
            NBTTagCompound nbt=new NBTTagCompound();
            nbt.setByte("Slot", (byte) i);
            packet.writeToNBT(nbt);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        par1NBTTagCompound.setShort("OfferCount", offerCount);
        par1NBTTagCompound.setShort("TransportDelay", transportDelay);
        par1NBTTagCompound.setInteger("NextDirection", nextDirectionIndex);
    }

    @Override
    public byte getConnectState() {
        return connectState;
    }

    @Override
    public byte getConnectStateDisable() {
        return connectStateDisable;
    }

    @Override
    public Iterator<PacketItemStack> getPackets(){
        return itemQueue.iterator();
    }

    //ハードコードばんじゃーい
    @Override
    public int getTextureIndex() {
        if(pIndex!=-1) return pIndex;

        BlockPipe b=(BlockPipe)getBlockType();
        if(b.maxRSS== ((BlockPipe)BlockCore.pipeTin).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeTin).maxFrequency) pIndex=0;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeCopper).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeCopper).maxFrequency) pIndex=1;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeIron).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeIron).maxFrequency) pIndex=2;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeSilver).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeSilver).maxFrequency) pIndex=3;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeGold).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeGold).maxFrequency) pIndex=4;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeAluminium).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeAluminium).maxFrequency) pIndex=5;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeNickel).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeNickel).maxFrequency) pIndex=6;
        else pIndex=0;

        return pIndex;
    }

    @Override
    public int getTextureCenterIndex() {
        if(pIndexCenter!=-1) return pIndexCenter;

        BlockPipe b=(BlockPipe)getBlockType();
        if(b.maxRSS== ((BlockPipe)BlockCore.pipeTin).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeTin).maxFrequency) pIndexCenter=0;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeCopper).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeCopper).maxFrequency) pIndexCenter=1;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeIron).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeIron).maxFrequency) pIndexCenter=2;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeSilver).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeSilver).maxFrequency) pIndexCenter=3;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeGold).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeGold).maxFrequency) pIndexCenter=4;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeAluminium).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeAluminium).maxFrequency) pIndexCenter=5;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeNickel).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeNickel).maxFrequency) pIndexCenter=6;
        else pIndexCenter=0;

        return pIndexCenter;
    }

    @Override
    public PipeType getPipeType() {
        return PipeType.NORMAL;
    }

    @Override
    public int getPipeSide() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7;
    }

    public EntityItem getEntityItem(){
        if(dummyEntity==null) dummyEntity=new EntityItem(worldObj);
        return dummyEntity;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        //入力が存在するとき、パイプとしての処理
        boolean flag=false;
        short r=getOutputRSS(-1), f=getOutputFrequency(-1);
        if(r > 0 && f>0) {

            //レート
            float rate=1.0f;
            if(f>1024) rate=5.0f;
            else if(f>512) rate=4.0f;
            else if(f>256) rate=3.0f;
            else if(f>128) rate=2.5f;
            else if(f>64) rate=2.0f;
            else if(f>32) rate=1.5f;

            //新
            Iterator<PacketItemStack> it=getPackets();
            while(it.hasNext()){
                PacketItemStack packet=it.next();
                if(packet.update(rate)){
                    if(packet.isReversed()){
                        //反転をfalseにし、行き先を決定する
                        packet.setReverse(false);
                        packet.setTicks(packet.getTicks()-PacketBase.TICK_MAX);
                        packet.setDirection(decideDestination(packet));

                        //行き先が見つからなかったとき
                        if(packet.getDirection()==-1){
                            if(!worldObj.isRemote) SpillPacket(packet);
                            it.remove();
                        }
                    }
                    else{
                        //行き先へアイテムを送る
                        if(!worldObj.isRemote) transportPacket3(packet);
                        it.remove();
                    }
                    flag=true;
                }
            }
            if(flag){
                worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
                worldObj.notifyBlockChange(xCoord,yCoord,zCoord,getBlockType());
            }
        }
    }

    @Override
    public boolean change() {
        int con = 0, con2=0;

        //中心部の金属の色の決定
        Block b=worldObj.getBlock(xCoord,yCoord,zCoord);
        if(b== BlockCore.cableTin) metal=0;
        else if(b==BlockCore.cableCopper) metal=1;
        else if(b==BlockCore.cableIron) metal=2;
        else if(b==BlockCore.cableSilver) metal=3;
        else if(b==BlockCore.cableGold) metal=4;
        else if(b==BlockCore.cableAluminium) metal=5;
        else if(b==BlockCore.cableNickel) metal=6;

        short pr=rss, pf=frequency;
        byte pc=connectState, pc2=connectStatePipe;

        //ケーブルの接続状態と入力を更新する
        for(int i=0;i<6;i++){
            if(((connectStateDisable>>i)&1)!=0) continue;

            boolean flag = false;
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if(entity instanceof TileEntityPipeBase){
                //パイプとの接続判定
                flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                if(flag){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }
            else if(entity instanceof TileEntityCable){
                //flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                flag=true;
            }
            else if(entity instanceof IConductor){
                //その他
                flag=((IConductor)entity).canConnect(dir.getOpposite().ordinal());
            }

            if(!(entity instanceof TileEntityPipeSorting) && entity instanceof IInventory){
                //インベントリ判定
                boolean flag2=true;
                if(entity instanceof TileEntityMachineBase){
                    if(!((TileEntityMachineBase) entity).canConnect(dir.getOpposite().ordinal())){
                        flag2=false;
                    }
                }
                if(flag2){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }

            if (flag) {
                con = (con | (1 << i));

                //ついでに入力も更新する
                if (entity instanceof IConductor) {
                    updateInput2((IConductor) entity, dir.ordinal());
                }
            }
        }

        connectState = (byte)(con|con2);
        connectStatePipe = (byte)con2;

        return pr!=rss || pf!=frequency || pc!=connectState || pc2!=connectStatePipe;
    }

    @Override
    public void updateConnectState2(){
        int con=0;
        int con2=0;

        connectDirections.clear();

        for(int i=0;i<6;i++){
            if(((connectStateDisable>>i)&1)!=0) continue;

            boolean flag=false;
            ForgeDirection dir=ForgeDirection.getOrientation(i);
            TileEntity entity=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);

            if(entity instanceof TileEntityPipeBase){
                //パイプとの接続判定
                flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                if(flag){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }
            else if(entity instanceof TileEntityCable){
                //flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                flag=true;
            }
            else if(entity instanceof IConductor){
                //その他
                flag=((IConductor)entity).canConnect(dir.getOpposite().ordinal());
            }

            if(!(entity instanceof TileEntityPipeSorting) && entity instanceof IInventory){
                //インベントリ判定
                boolean flag2=true;
                if(entity instanceof TileEntityMachineBase){
                    if(!((TileEntityMachineBase) entity).canConnect(dir.getOpposite().ordinal())){
                        flag2=false;
                    }
                }
                if(flag2){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }

            if(flag){
                con = (con | (1 << i));
                updateInput2((IConductor) entity, dir.ordinal());
            }
        }
        connectState = (byte)(con|con2);
        connectStatePipe = (byte)con2;
    }
    protected int decideDestination(PacketItemStack packet){
        int conCount = connectDirections.size();

        //来た道しかなかったら即失敗
        if (conCount <= 1) {
            return -1;
        }

        //インデックスが最大接続数を超えないように
        nextDirectionIndex %= conCount;

        //方向の決定
        int dir=-1;
        int t = connectDirections.get(nextDirectionIndex);
        ForgeDirection destDir = ForgeDirection.getOrientation(t);
        TileEntity destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);
        if (canTransportTo(destEntity, packet.getItemStack(), t) && packet.whereFrom() != t) {
            dir = t;
        }
        else{
            //なんか複雑なループだけど要は最初のインデックス以外の接続先インデックス全てを見て回ってる
            for (int n = (nextDirectionIndex + 1) % conCount; n != nextDirectionIndex; n = (n + 1) % conCount) {
                t = connectDirections.get(n);
                destDir = ForgeDirection.getOrientation(t);
                destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);

                if (canTransportTo(destEntity, packet.getItemStack(), t) && packet.whereFrom() != t) {
                    dir = t;
                    break;
                }
            }
            if(dir!=-1) {
                nextDirectionIndex = connectDirections.indexOf(dir);
            }
        }

        //仕上げ
        if(dir==-1) nextDirectionIndex = 0;
        nextDirectionIndex++;

        return dir;
    }
    protected  void transportPacket3(PacketItemStack packet) {
        if(packet==null){
            return;
        }
        /*
        int conCount = connectDirections.size();

        if (conCount <= 1) {
            SpillPacket(packet);
            return;
        }
        */

        //搬送する
        ForgeDirection destDir = ForgeDirection.getOrientation(packet.getDirection());
        TileEntity destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);
        transportTo(destEntity, packet, packet.getDirection());
    }

    protected boolean canTransportTo(TileEntity entity, ItemStack item, int dir){
        dir^=1;
        if(entity instanceof TileEntityPipeBase)    return true;
        else if(entity instanceof ISidedInventory){
            ISidedInventory inv=(ISidedInventory)entity;
            int slots[]=inv.getAccessibleSlotsFromSide(dir);
            for(int i=0;i<slots.length;i++){
                if(!inv.canInsertItem(slots[i], item, dir))  continue;
                ItemStack is=inv.getStackInSlot(i);
                if (is != null && (!is.isItemEqual(item) || is.stackSize + item.stackSize > is.getMaxStackSize())) continue;
                return true;
            }
            return false;
        }
        else if(entity instanceof TileEntityChest) {
            TileEntityChest inv[] = new TileEntityChest[2];
            inv[0] = (TileEntityChest) entity;

            //ラージチェストに対応
            if (inv[0].adjacentChestXNeg != null) inv[1] = inv[0].adjacentChestXNeg;
            if (inv[0].adjacentChestXPos != null) inv[1] = inv[0].adjacentChestXPos;
            if (inv[0].adjacentChestZNeg != null) inv[1] = inv[0].adjacentChestZNeg;
            if (inv[0].adjacentChestZPos != null) inv[1] = inv[0].adjacentChestZPos;

            //チェストの持つ最大2つのインベントリに対して処理
            for (int k = 0; k < 2; k++) {
                if (inv[k] == null) break;

                int size = inv[k].getSizeInventory();
                for (int i = 0; i < size; i++) {
                    if (!inv[k].isItemValidForSlot(i, item)) continue;
                    ItemStack is = inv[k].getStackInSlot(i);
                    if (is != null && (!is.isItemEqual(item) || is.stackSize + item.stackSize > is.getMaxStackSize())) continue;
                    return true;
                }
            }
        }
        else if(entity instanceof IInventory){
            IInventory inv=(IInventory)entity;
            int slots=inv.getSizeInventory();
            for(int i=0;i<slots;i++){
                if(!inv.isItemValidForSlot(i, item)) continue;
                ItemStack is=inv.getStackInSlot(i);
                if (is != null && (!is.isItemEqual(item) || is.stackSize + item.stackSize > is.getMaxStackSize())) continue;
                return true;
            }
            return false;
        }
        return false;
    }
    protected void transportTo(TileEntity entity, PacketItemStack packet, int dir){
        if(packet==null) return;
        if(entity==null || dir==-1){
            SpillPacket(packet);
            return;
        }

        dir^=1;
        ItemStack item=packet.getItemStack();

        //パイプ
        if(entity instanceof TileEntityPipeBase) {
            TileEntityPipeBase e = (TileEntityPipeBase) entity;
            packet.setDirection(dir^1);
            packet.setReverse(true);
            packet.setTicks(packet.getTicks()-PacketBase.TICK_MAX);
            e.offerPacket(packet);
            return;
        }

        //パイプ以外のTileEntity
        int x = entity.xCoord, y = entity.yCoord, z = entity.zCoord;

        if(entity instanceof TileEntityChest) {
            TileEntityChest inv[] = new TileEntityChest[2];
            inv[0] = (TileEntityChest) entity;

            //ラージチェストに対応
            if (inv[0].adjacentChestXNeg != null) inv[1] = inv[0].adjacentChestXNeg;
            if (inv[0].adjacentChestXPos != null) inv[1] = inv[0].adjacentChestXPos;
            if (inv[0].adjacentChestZNeg != null) inv[1] = inv[0].adjacentChestZNeg;
            if (inv[0].adjacentChestZPos != null) inv[1] = inv[0].adjacentChestZPos;

            //チェストの持つ最大2つのインベントリに対して処理
            for (int k = 0; k < 2; k++) {
                if (inv[k] == null) break;

                boolean flag=false;
                int size = inv[k].getSizeInventory();
                for(int i=0;i<size;i++){
                    if(inv[k].isItemValidForSlot(i, item)){
                        ItemStack itemTarget=inv[k].getStackInSlot(i);

                        int limit=inv[0].getInventoryStackLimit();
                        if(limit>item.getMaxStackSize()){
                            limit=item.getMaxStackSize();
                        }

                        if(itemTarget==null) {
                            if (item.stackSize <= limit) {
                                inv[k].setInventorySlotContents(i, item);
                                update(inv[k].xCoord,inv[k].yCoord,inv[k].zCoord,inv[k]);
                                return;
                            } else {
                                int t = item.stackSize;
                                item.stackSize = limit;
                                inv[k].setInventorySlotContents(i, item.copy());
                                item.stackSize = t - limit;
                                flag=true;
                            }
                        }
                        else if(itemTarget.isItemEqual(item)) {
                            if (itemTarget.stackSize + item.stackSize <= limit) {
                                itemTarget.stackSize += item.stackSize;
                                update(inv[k].xCoord,inv[k].yCoord,inv[k].zCoord,inv[k]);
                                return;
                            } else {
                                item.stackSize -= limit - itemTarget.stackSize;
                                itemTarget.stackSize = limit;
                                flag=true;
                            }
                        }
                    }
                }

                //インベントリを弄っていたら更新
                if(flag){
                    update(inv[k].xCoord,inv[k].yCoord,inv[k].zCoord,inv[k]);
                }
            }
        }
        else if(entity instanceof ISidedInventory){
            ISidedInventory inv = (ISidedInventory)entity;
            int[] slots=inv.getAccessibleSlotsFromSide(dir);
            //int limit=inv.getInventoryStackLimit();

            boolean flag=false;
            for(int i=0;i<slots.length;i++){
                //FMLLog.severe("pipe i="+i+",slots="+slots[i]);
                if(inv.canInsertItem(slots[i], item, dir) /*inv.isItemValidForSlot(i, item)*/){
                    ItemStack itemTaget=inv.getStackInSlot(slots[i]);

                    int limit=inv.getInventoryStackLimit();
                    if(limit>item.getMaxStackSize()){
                        limit=item.getMaxStackSize();
                    }

                    if(itemTaget==null) {
                        if (item.stackSize <= limit) {
                            inv.setInventorySlotContents(slots[i], item);
                            update(x,y,z,entity);
                            return;
                        } else {
                            int t = item.stackSize;
                            item.stackSize = limit;
                            inv.setInventorySlotContents(slots[i], item.copy());
                            item.stackSize = t - limit;
                            flag=true;
                        }
                    }
                    else if(itemTaget.isItemEqual(item)) {
                        if (itemTaget.stackSize + item.stackSize <= limit) {
                            itemTaget.stackSize += item.stackSize;
                            update(x,y,z,entity);
                            return;
                        } else {
                            item.stackSize -= limit - itemTaget.stackSize;
                            itemTaget.stackSize = limit;
                            flag=true;
                        }
                    }
                }
            }
            if(flag){
                update(x,y,z,entity);
            }
        }
        else if(entity instanceof IInventory){
            IInventory inv = (IInventory)entity;
            int size=inv.getSizeInventory();
            //int limit=inv.getInventoryStackLimit();

            boolean flag=false;
            for(int i=0;i<size;i++){
                if(inv.isItemValidForSlot(i, item)){
                    ItemStack itemTaget=inv.getStackInSlot(i);

                    int limit=inv.getInventoryStackLimit();
                    if(limit>item.getMaxStackSize()){
                        limit=item.getMaxStackSize();
                    }

                    if(itemTaget==null) {
                        if (item.stackSize <= limit) {
                            inv.setInventorySlotContents(i, item);
                            update(x,y,z,entity);
                            return;
                        } else {
                            int t = item.stackSize;
                            item.stackSize = limit;
                            inv.setInventorySlotContents(i, item.copy());
                            item.stackSize = t - limit;
                            flag=true;
                        }
                    }
                    else if(itemTaget.isItemEqual(item)) {
                        if (itemTaget.stackSize + item.stackSize <= limit) {
                            itemTaget.stackSize += item.stackSize;
                            update(x,y,z,entity);
                            return;
                        } else {
                            item.stackSize -= limit - itemTaget.stackSize;
                            itemTaget.stackSize = limit;
                            flag=true;
                        }
                    }
                }
            }
            if(flag){
                update(x,y,z,entity);
            }
        }

        SpillPacket(packet);
    }
    protected void SpillPacket(PacketItemStack packet){
        Random rand = new Random();
        ItemStack itemStack=packet.getItemStack();
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

    /**
     * 対象の座標のブロックを更新する
     * @param x
     * @param y
     * @param z
     * @param entity
     */
    protected void update(int x, int y, int z, TileEntity entity){
        worldObj.markBlockForUpdate(x,y,z);
        worldObj.notifyBlockChange(x,y,z,entity.getBlockType());
    }
}
