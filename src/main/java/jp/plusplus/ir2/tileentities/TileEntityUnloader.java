package jp.plusplus.ir2.tileentities;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/24.
 */
public class TileEntityUnloader extends TileEntityCollector {
    public static final int RANGE = 2;

    public TileEntityUnloader(){
        super(false);
        workAmount=32;
    }

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

        Iterator it=entities.iterator();
        while(it.hasNext()){
            Entity e=(Entity)it.next();
            if(!(e instanceof IInventory)) continue;

            IInventory inv=(IInventory)e;
            int size=inv.getSizeInventory();
            for (int i = 0; i < size; i++) {
                ItemStack packet=inv.getStackInSlot(i);
                if (packet == null) continue;

                insertItem(new ItemStack(packet.getItem(), 1, packet.getItemDamage()), itemStacks);
                packet.stackSize--;
                if(packet.stackSize<=0){
                    inv.setInventorySlotContents(i, null);
                }

                return;
            }
        }
    }
    @Override
    public boolean canWork(){
        return true;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.unloader");
    }
}
