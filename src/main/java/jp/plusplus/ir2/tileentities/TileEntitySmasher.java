package jp.plusplus.ir2.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntitySmasher extends TileEntityCollector{
    public TileEntitySmasher(){
        super(false);
        workAmount=32*1;
    }

    @Override
    public boolean canWork() {
        return canDestroy();
    }
    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    @Override
    public void work(){
        if(!canDestroy())   return;

        ForgeDirection dir=ForgeDirection.getOrientation(side);
        int x=xCoord + dir.offsetX;
        int y=yCoord + dir.offsetY;
        int z=zCoord + dir.offsetZ;
        Block b=worldObj.getBlock(x, y, z);

        Iterator<ItemStack> it = b.getDrops(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z), 0).iterator();
        while (it.hasNext()) {
            insertItem(it.next(), itemStacks);
        }
        worldObj.func_147480_a(x, y, z, false);
    }

    public boolean canDestroy(){
        ForgeDirection dir=ForgeDirection.getOrientation(side);
        int x=xCoord + dir.offsetX;
        int y=yCoord + dir.offsetY;
        int z=zCoord + dir.offsetZ;

        if(worldObj.isAirBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ)) return false;

        Block b=worldObj.getBlock(x, y, z);
        if(b.getBlockHardness(worldObj, x, y, z)<=-1.0f || b.getMaterial()== Material.water || b.getMaterial()== Material.lava || b.getMaterial()== Material.fire){
            return false;
        }
        return true;
    }
}
