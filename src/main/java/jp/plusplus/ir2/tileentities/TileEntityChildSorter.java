package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/07/05.
 * 子供仕分けマシン。
 */
public class TileEntityChildSorter extends TileEntityMachineBase {
    protected int range;
    public TileEntityChildSorter(){
        workAmount=5*32;
        range=1;
    }

    public void work(){
        ForgeDirection dir=ForgeDirection.getOrientation(side);
        ForgeDirection back=dir.getOpposite();

        int x=xCoord+(1+range)*dir.offsetX,z=zCoord+(1+range)*dir.offsetZ;
        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x,yCoord,z, x+1,yCoord+1,z+1).expand(range, range, range);
        List l=worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, aabb, null);
        Iterator it=l.iterator();
        while(it.hasNext()){
            EntityLivingBase e=(EntityLivingBase)it.next();
            if(e.isChild()){
                e.setPositionAndUpdate(xCoord+back.offsetX, yCoord+0.0625f, zCoord+back.offsetZ);
                //e.addPotionEffect(new PotionEffect(Potion.poison.getId(), 30, 30));
                return;
            }
        }
    }

    @Override
    public boolean canWork(){
        //範囲内に空間があるかのみを見る
        ForgeDirection ops=ForgeDirection.getOrientation(side).getOpposite();
        int bx=xCoord+(1+range)*ops.offsetX-1;
        int by=yCoord;
        int bz=zCoord+(1+range)*ops.offsetZ-1;

        for(int i=0;i<range;i++){
            for(int j=0;j<range;j++){
                for(int k=0;k<range;k++){
                    if(!worldObj.isAirBlock(bx+i, by+j, bz+k)) return false;
                }
            }
        }

        return true;
    }
}
