package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.ir2.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityWoodcutter extends TileEntityCollector{
    public static final int LIMIT_VERTICAL=32;
    public static final int LIMIT_HORIZONTAL_WOOD=3;
    public static final int LIMIT_HORIZONTAL_LEAVE=12;

    private LinkedList<Position> nextWoods;
    private LinkedList<Position> nextLeaves;
    private short rangeHorizontal;


    public TileEntityWoodcutter(){
        super(false);
        workAmount=32*60;
        rangeHorizontal=(short)4;
    }
    public TileEntityWoodcutter(boolean adv){
        //workAmount=32*60;
        super(adv);
        workAmount=adv?128*60:32*60;
        rangeHorizontal=(short)(adv?8:4);
        nextWoods=new LinkedList<Position>();
        nextLeaves=new LinkedList<Position>();
    }


    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        if(par1NBTTagCompound.hasKey("RangeHorizontal")) {
            rangeHorizontal = par1NBTTagCompound.getShort("RangeHorizontal");
        }
        else{
            rangeHorizontal=4;
        }

        NBTTagList nbttaglist;
        nextWoods=new LinkedList<Position>();
        nextLeaves=new LinkedList<Position>();

        nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("NextWoods");
        if(nbttaglist!=null) {
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
                Position p = new Position();

                p.x = nbt.getInteger("X");
                p.y = nbt.getInteger("Y");
                p.z = nbt.getInteger("Z");
                p.distanceH = nbt.getInteger("DistanceH");
                p.distanceV = nbt.getInteger("DistanceV");
                nextWoods.add(p);
            }
        }

        nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("NextLeaves");
        if(nbttaglist!=null) {
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
                Position p = new Position();

                p.x = nbt.getInteger("X");
                p.y = nbt.getInteger("Y");
                p.z = nbt.getInteger("Z");
                p.distanceH = nbt.getInteger("DistanceH");
                p.distanceV = nbt.getInteger("DistanceV");
                nextLeaves.add(p);
            }
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setShort("RangeHorizontal", rangeHorizontal);

        NBTTagList nbttaglist;
        Iterator<Position> it;

        nbttaglist = new NBTTagList();
        it=nextWoods.iterator();
        while(it.hasNext()){
            NBTTagCompound nbt=new NBTTagCompound();
            Position p=it.next();

            nbt.setInteger("X", p.x);
            nbt.setInteger("Y", p.y);
            nbt.setInteger("Z", p.z);
            nbt.setInteger("DistanceH", p.distanceH);
            nbt.setInteger("DistanceV", p.distanceV);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("NextWoods", nbttaglist);

        nbttaglist = new NBTTagList();
        it=nextLeaves.iterator();
        while(it.hasNext()){
            NBTTagCompound nbt=new NBTTagCompound();
            Position p=it.next();

            nbt.setInteger("X", p.x);
            nbt.setInteger("Y", p.y);
            nbt.setInteger("Z", p.z);
            nbt.setInteger("DistanceH", p.distanceH);
            nbt.setInteger("DistanceV", p.distanceV);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("NextLeaves", nbttaglist);
    }

    @Override
    public boolean canWork() {
        return nextWoods.isEmpty() && nextLeaves.isEmpty();
    }
    @Override
    public String getInventoryName() {
        return I18n.format("gui.woodcutter");
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote && rss>0 && frequency>0){
            Iterator<Position> it;
            LinkedList<Position> nw=nextWoods;
            LinkedList<Position> nl=nextLeaves;

            nextWoods=new LinkedList<Position>();
            nextLeaves=new LinkedList<Position>();

            it=nw.iterator();
            while(it.hasNext()){
                cut(it.next(), LIMIT_HORIZONTAL_WOOD);
            }

            it=nl.iterator();
            while(it.hasNext()){
                cut(it.next(), LIMIT_HORIZONTAL_LEAVE);
            }
        }
    }
    @Override
    public void work(){
        // direction
        int dir=side;
        int nX=0, nZ=0;
        switch(dir){
            case 2:
                nZ=-1;
                break;
            case 3:
                nZ=1;
                break;
            case 4:
                nX=-1;
                break;
            case 5:
                nX=1;
                break;
        }

        // cutting
        for(int i=1;i<rangeHorizontal+1;i++){
            boolean f=judgeTarget(xCoord+nX*i, yCoord, zCoord+nZ*i, 0, 0);
            if(f){
                break;
            }
        }
    }

    private boolean judgeTarget(int x, int y, int z, int dh, int dv) {
        Block b = worldObj.getBlock(x, y, z);
        if(b==Blocks.air) return false;

        /*
        int m=worldObj.getBlockMetadata(x, y, z);
        if(b instanceof BlockRotatedPillar) m=(m&3);
        else                                 m=(m&7);
        */

        if (Recipes.isWood(b)) {
            nextWoods.add(new Position(x, y, z, dh, dv));
            return true;
        } else if (Recipes.isLeave(b)) {
            nextLeaves.add(new Position(x, y, z, dh, dv));
            return true;
        }
        return false;
    }
    private void cut(Position pos, int limit) {
        Block b = worldObj.getBlock(pos.x, pos.y, pos.z);
        if(b==Blocks.air)   return;

        Iterator<ItemStack> it = b.getDrops(worldObj, pos.x, pos.y, pos.z, worldObj.getBlockMetadata(pos.x, pos.y, pos.z), 0).iterator();
        while (it.hasNext()) {
            insertItem(it.next(), itemStacks);
        }
        worldObj.func_147480_a(pos.x, pos.y, pos.z, false);

        int ndh = pos.distanceH + 1;
        int ndv = pos.distanceV + 1;
        if (ndh <= limit) {
            judgeTarget(pos.x - 1, pos.y, pos.z, ndh, pos.distanceV);
            judgeTarget(pos.x + 1, pos.y, pos.z, ndh, pos.distanceV);
            judgeTarget(pos.x, pos.y, pos.z + 1, ndh, pos.distanceV);
            judgeTarget(pos.x, pos.y, pos.z - 1, ndh, pos.distanceV);
        }
        if (ndv <= LIMIT_VERTICAL) {
            judgeTarget(pos.x, pos.y + 1, pos.z, pos.distanceH, ndv);
            judgeTarget(pos.x, pos.y - 1, pos.z, pos.distanceH, ndv);
        }
    }

    private class Position{
        public int x;
        public int y;
        public int z;
        public int distanceH;
        public int distanceV;

        private Position(int x, int y, int z, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.z = z;
            distanceH = dx;
            distanceV = dy;
        }
        private Position(){
        }
    }
}
