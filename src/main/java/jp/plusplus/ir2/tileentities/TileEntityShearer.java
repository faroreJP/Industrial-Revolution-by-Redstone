package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityShearer extends TileEntityCollector{
    private short range;

    public TileEntityShearer(){
        super(false);
        workAmount=32*30;
        range=(short)1;
    }
    public TileEntityShearer(boolean adv){
        super(adv);
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
    protected void work() {
        ForgeDirection dir=ForgeDirection.getOrientation(side);

        int x=xCoord+(1+range)*dir.offsetX,z=zCoord+(1+range)*dir.offsetZ;
        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x,yCoord,z, x+1,yCoord+1,z+1).expand(range, range, range);
        List l=worldObj.selectEntitiesWithinAABB(EntitySheep.class, aabb, null);
        Iterator it=l.iterator();
        while(it.hasNext()){
            EntitySheep e=(EntitySheep)it.next();
            if(e.getSheared() || e.isChild())	continue;
            Iterator<ItemStack> r=e.onSheared(null, worldObj, (int)e.posX, (int)e.posY, (int)e.posZ, 0).iterator();
            while(r.hasNext()){
                insertItem(r.next(), itemStacks);
            }
            return;
        }
    }
    @Override
    public boolean canWork() {
        return true;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.shearer");
    }
}
