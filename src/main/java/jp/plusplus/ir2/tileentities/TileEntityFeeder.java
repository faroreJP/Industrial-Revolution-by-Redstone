package jp.plusplus.ir2.tileentities;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityFeeder extends TileEntityCollector{
    private short range;

    public TileEntityFeeder(){
        super(false);
        workAmount=32*30;
        range=(short)1;
    }
    public TileEntityFeeder(boolean adv){
        super(adv);
        //workAmount=32*30;
        workAmount=adv?128*30:32*30;
        range=(short)(adv?2:1);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        if(par1NBTTagCompound.hasKey("Range")) {
            range = par1NBTTagCompound.getShort("Range");
        }
        else{
            range=1;
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("Range", range);
    }

    @Override
    public boolean usesSlotTakeOnly(){ return false; }

    @Override
    protected void work() {
        int dir=side;
        int x=xCoord,z=zCoord;
        switch(dir){
            case 2:
                z-=2;
                break;
            case 3:
                z+=2;
                break;
            case 4:
                x-=2;
                break;
            case 5:
                x+=2;
                break;
        }
        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x,yCoord,z, x+1,yCoord+1,z+1).expand(range, range, range);

        int feedCount=0;
        for(int i=0;i<this.getSizeInventory();i++) {
            if (itemStacks[i] == null) continue;

            List animals = worldObj.selectEntitiesWithinAABB(EntityAnimal.class, aabb, null);
            if (!animals.isEmpty()) {
                Iterator it = animals.iterator();
                while (it.hasNext()) {
                    EntityAnimal c = (EntityAnimal) it.next();
                    if (!c.isInLove() && c.getGrowingAge() == 0 && c.isBreedingItem(itemStacks[i])) {
                        c.func_146082_f(null);

                        itemStacks[i].stackSize--;
                        if (itemStacks[i].stackSize <= 0)  itemStacks[i] = null;

                        feedCount++;
                        if(feedCount==2)    return;
                    }
                }
            }
        }
    }

    @Override
    public boolean canWork(){
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]==null)	continue;
            if(itemStacks[i].stackSize>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.feeder");
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
