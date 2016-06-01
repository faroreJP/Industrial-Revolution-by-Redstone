package jp.plusplus.ir2.tileentities;

import jp.plusplus.fbs.entity.EntityButterfly;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityItemCollector extends TileEntityCollector {
    public TileEntityItemCollector() {
        super(true);
        workAmount = 128;
    }

    protected int getRange() {
        if (frequency <= 32) return 1;
        if (frequency <= 64) return 2;
        if (frequency <= 128) return 3;
        return 4;
    }

    @Override
    protected void work() {
        int range = getRange();
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        int x = xCoord + dir.offsetX * (range + 1);
        int z = zCoord + dir.offsetZ * (range + 1);
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x, yCoord, z, x + 1, yCoord + 1, z + 1).expand(range, 0, range);

        if (IR2.cooperatesInsanity) {
            getButterfly(aabb.copy());
        }

        List l = worldObj.selectEntitiesWithinAABB(EntityItem.class, aabb, null);
        Iterator it = l.iterator();
        if (it.hasNext()) {
            EntityItem e = (EntityItem) it.next();
            insertItem(e.getEntityItem().copy(), itemStacks);
            e.setDead();
        }
    }

    @Override
    public boolean canWork() {
        return true;
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    public boolean getButterfly(AxisAlignedBB aabb) {
        aabb.maxY+=getRange();

        List l = worldObj.selectEntitiesWithinAABB(EntityButterfly.class, aabb, null);
        Iterator it = l.iterator();
        if (it.hasNext()) {
            EntityButterfly e = (EntityButterfly) it.next();
            insertItem(new ItemStack(ItemCore.butterfly, 1), itemStacks);
            e.setDead();
        }

        return false;
    }
}
