package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityButcher extends TileEntityCollector{
    public static final int RANGE = 1;
    public Random rand=new Random();

    public TileEntityButcher(){
        super(false);
        workAmount=32*2;
    }

    @Override
    protected void work() {
        ForgeDirection dir=ForgeDirection.getOrientation(side);
        int x=xCoord+dir.offsetX*(RANGE+1);
        int z=zCoord+dir.offsetZ*(RANGE+1);

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x,yCoord,z, x+1,yCoord+1,z+1).expand(RANGE, 0, RANGE);
        List l=worldObj.selectEntitiesWithinAABB(EntityLiving.class, aabb, null);
        Iterator it=l.iterator();
        while(it.hasNext()){
            EntityLiving e=(EntityLiving)it.next();
            if(e.isDead)	continue;

            e.attackEntityFrom(new DamageSource("generic"), 6.0f);

            if(e.getHealth()<=0.0F){
                List items=worldObj.selectEntitiesWithinAABB(EntityItem.class, aabb.expand(1,0,1), null);
                Iterator iIt=items.iterator();
                //FMLLog.severe(iIt.toString());
                while(iIt.hasNext()){
                    EntityItem iE=(EntityItem)iIt.next();
                    insertItem(iE.getEntityItem().copy(), itemStacks);
                    iE.setDead();
                }

                if(rand.nextFloat()<0.5f){
                    insertItem(new ItemStack(Items.experience_bottle), itemStacks);
                }
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
        return I18n.format("gui.butcher");
    }
}
