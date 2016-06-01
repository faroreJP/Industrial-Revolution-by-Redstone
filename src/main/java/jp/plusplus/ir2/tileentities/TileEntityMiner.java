package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityMiner extends TileEntityCollector{
    public static final int RANGE=16;

    private LinkedList<Position> nextOres;
    private LinkedList<Position> nextStones;
    public boolean hasOre;
    public int indexOre;
    public int indexStone;

    public TileEntityMiner(){
        super(true);
        //workAmount=256*2;
        workAmount=32;
        nextOres =new LinkedList<Position>();
        nextStones =new LinkedList<Position>();
    }

    @Override
    public boolean usesSlotTakeOnly(){
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        hasOre=par1NBTTagCompound.getBoolean("HasOre");
        indexOre=par1NBTTagCompound.getInteger("IndexOre");

        NBTTagList nbttaglist;
        nextOres =new LinkedList<Position>();
        nextStones =new LinkedList<Position>();

        nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("NextOres");
        if(nbttaglist!=null) {
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
                Position p = new Position();
                p.readFromNBT(nbt);
                nextOres.add(p);
            }
        }

        nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("NextStones");
        if(nbttaglist!=null) {
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
                Position p = new Position();
                p.readFromNBT(nbt);
                nextStones.add(p);
            }
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setBoolean("HasOre", hasOre);
        par1NBTTagCompound.setInteger("IndexOre", indexOre);
        //par1NBTTagCompound.setInteger("IndexStone", indexStone);

        NBTTagList nbttaglist;
        Iterator<Position> it;

        nbttaglist = new NBTTagList();
        it= nextOres.iterator();
        while(it.hasNext()){
            NBTTagCompound nbt=new NBTTagCompound();
            Position p=it.next();
            p.writeToNBT(nbt);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("NextOres", nbttaglist);

        nbttaglist = new NBTTagList();
        it= nextStones.iterator();
        while (it.hasNext()){
            NBTTagCompound nbt=new NBTTagCompound();
            Position p=it.next();
            p.writeToNBT(nbt);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("NextStones", nbttaglist);
    }
    protected boolean canMine(Block b){
        Material m=b.getMaterial();
        return m!=Material.water && m!=Material.lava && b!=Blocks.bedrock && b!=Blocks.air;
    }
    protected int getNextY(){
        int x=xCoord;
        int y=yCoord-1;
        int z=zCoord;

        Block b=worldObj.getBlock(x,y,z);
        while(b==BlockCore.pipeMining){
            y--;
            b=worldObj.getBlock(x,y,z);
        }
        return y;
    }

    @Override
    public boolean canWork() {
        int y = getNextY();
        Block b = worldObj.getBlock(xCoord, y, zCoord);
        if (b.getBlockHardness(worldObj, xCoord, y, zCoord)>-1.0F) return true;

        b = worldObj.getBlock(xCoord, y + 1, zCoord);
        return b == BlockCore.pipeMining && worldObj.getBlockMetadata(xCoord, y + 1, zCoord) != 15;
    }
    @Override
    public String getInventoryName() {
        return BlockCore.machineMiner.getLocalizedName();
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote && rss>0 && frequency>0){

            //check inventory
            hasOre=false;
            indexStone=-1;
            for(int i=0;i<itemStacks.length;i++){
                if(itemStacks[i]==null) continue;

                Item item=itemStacks[i].getItem();
                if(!(item instanceof ItemBlock)){
                    hasOre=true;
                    indexOre=i;
                    break;
                }

                ItemBlock ib=(ItemBlock)item;
                if(!Recipes.isStone(ib.field_150939_a, itemStacks[i].getItemDamage())){
                    hasOre=true;
                    indexOre=i;
                    break;
                }
                else if(indexStone==-1){
                    indexStone=i;
                }
            }

            //mine
            if(!nextOres.isEmpty()){
                Iterator<Position> it;
                LinkedList<Position> nw= nextOres;
                nextOres=new LinkedList<Position>();

                it=nw.iterator();
                while(it.hasNext()){
                    mine(it.next(), RANGE);
                }

            }
            else if(!nextStones.isEmpty()){
                //search stone
                /*
                int index=-1;
                for(int i=0;i<itemStacks.length;i++){
                    if(itemStacks[i]==null) continue;
                    if(!(itemStacks[i].getItem() instanceof ItemBlock)) continue;
                    Block b=((ItemBlock) itemStacks[i].getItem()).field_150939_a;
                    if(Recipes.isStone(b, itemStacks[i].getItemDamage())){
                        index=i;
                        break;
                    }
                }
                */

                //fill
                if(indexStone!=-1){
                    itemStacks[indexStone]=landfill(nextStones.pop(), itemStacks[indexStone]);
                }
                else{
                    //nextStones.clear();
                }
            }
        }
    }
    @Override
    public void work(){
        boolean flag=false;
        int y=getNextY();
        int meta=worldObj.getBlockMetadata(xCoord, y + 1, zCoord);

        if(y+1==yCoord) {
            flag=true;
        }
        else{
            flag=(meta==15);
        }

        if(flag) {
            judgeTarget(xCoord, y, zCoord, 0, false, true, true);
            judgeTarget(xCoord - 1, y, zCoord, 0, false, false, true);
            judgeTarget(xCoord + 1, y, zCoord, 0, false, false, true);
            judgeTarget(xCoord, y, zCoord - 1, 0, false, false, true);
            judgeTarget(xCoord, y, zCoord + 1, 0, false, false, true);
            judgeTarget(xCoord - 1, y, zCoord + 1, 0, false, false, true);
            judgeTarget(xCoord - 1, y, zCoord - 1, 0, false, false, true);
            judgeTarget(xCoord + 1, y, zCoord - 1, 0, false, false, true);
            judgeTarget(xCoord + 1, y, zCoord + 1, 0, false, false, true);
        }
        else{
            worldObj.setBlockMetadataWithNotify(xCoord,y+1,zCoord, meta+1, 2);
        }
    }

    private boolean judgeTarget(int x, int y, int z, int d, boolean fill, boolean pipe, boolean always) {
        Block b = worldObj.getBlock(x, y, z);
        Material m=b.getMaterial();
        if((!pipe && m==Material.air) || b==BlockCore.pipeMining || b==Blocks.bedrock) return false;
        if(!pipe && (m==Material.water || m==Material.lava || m==Material.fire)) return false;

        int meta=worldObj.getBlockMetadata(x,y,z);

        if(m==Material.air || always || Recipes.isOre(b, meta)){
            nextOres.offer(new Position(x,y,z, d, fill, pipe));
            return true;
        }
        return false;
    }
    private void mine(Position pos, int limit) {
        Block b = worldObj.getBlock(pos.x, pos.y, pos.z);
        if (b != Blocks.air) {
            Iterator<ItemStack> it = b.getDrops(worldObj, pos.x, pos.y, pos.z, worldObj.getBlockMetadata(pos.x, pos.y, pos.z), 0).iterator();
            while (it.hasNext()) {
                insertItem(it.next(), itemStacks);
            }
            worldObj.func_147480_a(pos.x, pos.y, pos.z, false);
            if (pos.doesLandfill) {
                nextStones.push(pos);
            }
        }
        if (pos.doesSetPipe) {
            worldObj.setBlock(pos.x, pos.y, pos.z, BlockCore.pipeMining, 1, 2);
        }

        int nd = pos.distance + 1;
        if (nd <= limit) {
            judgeTarget(pos.x - 1, pos.y, pos.z, pos.distance, true, false, false);
            judgeTarget(pos.x + 1, pos.y, pos.z, pos.distance, true, false, false);
            judgeTarget(pos.x, pos.y, pos.z + 1, pos.distance, true, false, false);
            judgeTarget(pos.x, pos.y, pos.z - 1, pos.distance, true, false, false);
            if (!pos.doesSetPipe) {
                judgeTarget(pos.x, pos.y + 1, pos.z, pos.distance, true, false, false);
                judgeTarget(pos.x, pos.y - 1, pos.z, pos.distance, true, false, false);
            }
        }
    }
    private ItemStack landfill(Position pos, ItemStack stone){
        if(!worldObj.isAirBlock(pos.x, pos.y, pos.z)) return stone;

        if(stone.getItem() instanceof  ItemBlock) {
            ItemBlock b=(ItemBlock)stone.getItem();
            worldObj.setBlock(pos.x, pos.y, pos.z, b.field_150939_a, stone.getItemDamage(), 2);
            stone.stackSize--;
        }
        return stone.stackSize>0?stone:null;
    }



    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        Item item=itemstack.getItem();
        if(!(item instanceof ItemBlock))    return false;

        return Recipes.isStone(((ItemBlock)item).field_150939_a, itemstack.getItemDamage());
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if(j==side || j==sideCarrying) return false;

        Item item=itemstack.getItem();
        if(!(item instanceof ItemBlock))    return false;

        return Recipes.isStone(((ItemBlock)item).field_150939_a, itemstack.getItemDamage());
    }
    /*
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if(j==side) return false;

        if(hasOre)  return i==indexOre;
        else         return i!=indexStone;
    }
    */

    private class Position{
        public int x;
        public int y;
        public int z;
        public int distance;
        public boolean doesLandfill;
        public boolean doesSetPipe;

        private Position(int x, int y, int z, int d, boolean flag) {
            this.x = x;
            this.y = y;
            this.z = z;
            distance = d;
            this.doesLandfill =flag;
            doesSetPipe=false;
        }
        private Position(int x, int y, int z, int d, boolean flag, boolean flag2) {
            this.x = x;
            this.y = y;
            this.z = z;
            distance = d;
            this.doesLandfill =flag;
            doesSetPipe=flag2;
        }
        private Position(){
        }

        private void writeToNBT(NBTTagCompound nbt){
            nbt.setInteger("X", x);
            nbt.setInteger("Y", y);
            nbt.setInteger("Z", z);
            nbt.setInteger("Distance", distance);
            nbt.setBoolean("DoesLandfill", doesLandfill);
            nbt.setBoolean("DoesSetPipe", doesSetPipe);
        }
        private void readFromNBT(NBTTagCompound nbt){
            x = nbt.getInteger("X");
            y = nbt.getInteger("Y");
            z = nbt.getInteger("Z");
            distance = nbt.getInteger("Distance");
            doesLandfill = nbt.getBoolean("DoesLandfill");
            doesSetPipe = nbt.getBoolean("DoesSetPipe");
        }
    }
}
