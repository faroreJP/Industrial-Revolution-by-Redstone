package jp.plusplus.ir2.tileentities;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class TileEntityPipeExtractor extends TileEntityPipeBase {
    protected int extractTicks;

    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        extractTicks=nbt.getInteger("ExtractTicks");
    }
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setInteger("ExtractTicks", extractTicks);
    }

    @Override
    public void updateEntity() {
        /*
        if(getOutputRSS(-1)>0 &&  extractTicks==0){
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            worldObj.notifyBlockChange(xCoord,yCoord,zCoord,getBlockType());
        }
        */

        super.updateEntity();

        if(!worldObj.isRemote){
            if(getOutputRSS(-1)>0){
                if(extractTicks<=0){
                    extractPacket2();

                    short f=getOutputFrequency(-1);
                    if(f>1024) extractTicks=2;
                    else if(f>512) extractTicks=5;
                    else if(f>256) extractTicks=10;
                    else if(f>128) extractTicks=12;
                    else if(f>64) extractTicks=14;
                    else if(f>32) extractTicks=16;
                    else if(f>16) extractTicks=18;
                    else extractTicks=20;
                }
                extractTicks--;
            }
        }
    }
    protected void extractPacket2(){
        ForgeDirection dir=ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
        TileEntity entity=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
        if(entity==null) return;

        int x = entity.xCoord, y = entity.yCoord, z = entity.zCoord;

        if(entity instanceof TileEntityChest){
            TileEntityChest[] inv=new TileEntityChest[2];
            inv[0]=(TileEntityChest)entity;
            int size = inv[0].getSizeInventory();

            if(inv[0].adjacentChestXNeg!=null) inv[1]=inv[0].adjacentChestXNeg;
            if(inv[0].adjacentChestXPos!=null) inv[1]=inv[0].adjacentChestXPos;
            if(inv[0].adjacentChestZNeg!=null) inv[1]=inv[0].adjacentChestZNeg;
            if(inv[0].adjacentChestZPos!=null) inv[1]=inv[0].adjacentChestZPos;

            for(int k=0;k<2;k++){
                if(inv[k]==null)    continue;

                for (int i = 0; i < size; i++) {
                    ItemStack item = inv[k].getStackInSlot(i);
                    if (item == null) continue;

                    ItemStack it=item.copy();
                    it.stackSize=1;

                    PacketItemStack packet = new PacketItemStack(it);
                    packet.setDirection(dir.ordinal()^1);
                    packet.setReverse(true);
                    offerPacket(packet);

                    item.stackSize--;
                    if(item.stackSize<=0) {
                        inv[k].setInventorySlotContents(i, null);
                    }
                    entity.markDirty();
                    worldObj.markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
                    update(x,y,z,entity);
                    return;
                }
            }
        }
        else if (entity instanceof ISidedInventory) {
            ISidedInventory inv = (ISidedInventory) entity;
            int[] slots = inv.getAccessibleSlotsFromSide(dir.ordinal()^1);

            for (int i = 0; i < slots.length; i++) {
                ItemStack item = inv.getStackInSlot(slots[i]);
                if (item == null) continue;

                if (inv.canExtractItem(slots[i], item, dir.ordinal()^1)) {
                    ItemStack it=item.copy();
                    it.stackSize=1;

                    PacketItemStack packet = new PacketItemStack(it);
                    packet.setDirection(dir.ordinal()^ 1);
                    packet.setReverse(true);
                    offerPacket(packet);

                    item.stackSize--;
                    if(item.stackSize<=0) {
                        inv.setInventorySlotContents(slots[i], null);
                    }
                    entity.markDirty();
                    worldObj.markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
                    update(x,y,z,entity);
                    break;
                }
            }
        } else if (entity instanceof IInventory) {
            IInventory inv = (IInventory) entity;
            int size = inv.getSizeInventory();

            for (int i = 0; i < size; i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item == null) continue;

                ItemStack it=item.copy();
                it.stackSize=1;

                PacketItemStack packet = new PacketItemStack(it);
                packet.setDirection(dir.ordinal()^1);
                packet.setReverse(true);
                offerPacket(packet);

                item.stackSize--;
                if(item.stackSize<=0) {
                    inv.setInventorySlotContents(i, null);
                }
                entity.markDirty();
                worldObj.markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
                update(x,y,z,entity);
                break;
            }
        }
    }

    @Override
    public PipeType getPipeType() {
        return PipeType.EXTRACTOR;
    }
}
