package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/24.
 */
public class TileEntityLoader extends TileEntityCollector {
    public static final int RANGE = 4;

    public TileEntityLoader(){
        super(false);
        workAmount=32;
    }

    @Override
    public boolean usesSlotTakeOnly(){ return false; }

    @Override
    protected void work() {
        ForgeDirection dir=ForgeDirection.getOrientation(side);
        int x=xCoord;
        int y=yCoord;
        int z=zCoord;

        AxisAlignedBB aabb;
        switch (dir){
            case NORTH:
                aabb=AxisAlignedBB.getBoundingBox(x-RANGE, y-1, z-1, x+RANGE+1, y+2, z);
                break;
            case SOUTH:
                aabb=AxisAlignedBB.getBoundingBox(x-RANGE, y-1, z+1, x+RANGE+1, y+2, z+2);
                break;
            case WEST:
                aabb=AxisAlignedBB.getBoundingBox(x-1, y-1, z-RANGE, x, y+2, z+RANGE);
                break;
            default:
                aabb=AxisAlignedBB.getBoundingBox(x+1, y-1, z-RANGE, x+2, y+2, z+RANGE);
                break;
        }

        List entities=worldObj.selectEntitiesWithinAABB(Entity.class, aabb, null);
        if(entities.isEmpty()) return;

        boolean flag=false;
        Iterator it=entities.iterator();
        while(it.hasNext() && !flag){
            Entity e=(Entity)it.next();

            if(e instanceof EntityMinecartFurnace){
                //かまどトロッコだけ特別な処理
                EntityMinecartFurnace emf=(EntityMinecartFurnace)e;

                for(int i=0;i<itemStacks.length;i++) {
                    ItemStack itemStack = itemStacks[i];
                    if (itemStack == null || itemStack.getItem() != Items.coal) continue;

                    try {

                        //向きの設定
                        if(emf.pushX==0 && emf.pushZ==0){
                            switch (dir){
                                case NORTH: emf.pushX=1; break;
                                case SOUTH: emf.pushX=-1; break;
                                case WEST: emf.pushZ=-1; break;
                                case EAST: emf.pushZ=1; break;
                            }
                        }

                        //むりやりトロッコを動かす
                        Class cl = emf.getClass();
                        Field fld = cl.getDeclaredField("fuel");
                        fld.setAccessible(true);
                        fld.setInt(emf, fld.getInt(emf) + 3600);

                        flag = true;
                        itemStacks[i].stackSize--;
                        if (itemStacks[i].stackSize <= 0) {
                            itemStacks[i] = null;
                        }
                        break;
                    } catch (Exception e1) {
                        IR2.logger.error(e1);
                    }
                }
            }
            else if(e instanceof IInventory){
                IInventory inv=(IInventory)e;
                int size=inv.getSizeInventory();
                int limit=inv.getInventoryStackLimit();
                for (int i = 0; i < itemStacks.length; i++) {
                    ItemStack packet=itemStacks[i];
                    if (packet == null) continue;

                    for(int k=0;k<size;k++){
                        ItemStack dest=inv.getStackInSlot(k);
                        if(dest==null){
                            inv.setInventorySlotContents(k, new ItemStack(packet.getItem(), 1, packet.getItemDamage()));
                            flag=true;
                        }
                        else{
                            if(dest.isItemEqual(packet) && dest.stackSize+1<=limit){
                                dest.stackSize++;
                                flag=true;
                            }
                        }
                        if(flag) break;
                    }
                    if(flag){
                        itemStacks[i].stackSize--;
                        if(itemStacks[i].stackSize<=0){
                            itemStacks[i]=null;
                        }
                        break;
                    }
                }
            }

        }
    }
    @Override
    public boolean canWork(){
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]!=null) return true;
        }
        return false;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.loader");
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side;
    }

}
