package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.mod.ForSS2;
import mods.defeatedcrow.common.DCsAppleMilk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntitySower extends TileEntityCollector{
    private short range;

    public TileEntitySower(){
        super(false);
        workAmount=32*30;
        range=(short)4;

        DRAW_TICKS_MAX=40;
    }
    public TileEntitySower(boolean adv){
        super(adv);
        workAmount=adv?128*30:32*30;
        range=(short)(adv?8:4);

        DRAW_TICKS_MAX=40;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        if(par1NBTTagCompound.hasKey("Range")) {
            range = par1NBTTagCompound.getShort("Range");
        }
        else{
            range=4;
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
    public boolean hasCarryingSide(){ return false; }

    @Override
    protected void work() {
        int dir = side;
        int nX = 0, nZ = 0;
        int nnX=0, nnZ=0;
        switch (dir) {
            case 2:
                nZ = -1;
                nnX = 1;
                break;
            case 3:
                nZ = 1;
                nnX = -1;
                break;
            case 4:
                nX = -1;
                nnZ = 1;
                break;
            case 5:
                nX = 1;
                nnZ = -1;
                break;
        }

        for(int k=0;k<3;k++){
            boolean flag = false;
            for (int i = 0; i < itemStacks.length && !flag; i++) {
                if (itemStacks[i] == null) continue;
                Item item = itemStacks[i].getItem();

                if(IR2.cooperatesSS2 && ForSS2.trySowForSS2(itemStacks[i], worldObj, range, xCoord, yCoord, zCoord, nX, nZ, nnX, nnZ)) {
                    // SS2との連携
                    flag = true;
                }
                if(!flag) {
                    // IPlantable実装のブロック・アイテム
                    if (item instanceof IPlantable) {
                        flag = sow((IPlantable) item, itemStacks[i].getItemDamage(), i, nX, nZ, nnX, nnZ);
                    } else if (item instanceof ItemBlock) {
                        Block b = ((ItemBlock) item).field_150939_a;
                        if (b == null) continue;
                        if (b instanceof IPlantable) {
                            flag = sow((IPlantable) b, itemStacks[i].getItemDamage(), i, nX, nZ, nnX, nnZ);
                        }
                    }
                }
                if (itemStacks[i].stackSize <= 0) {
                    itemStacks[i] = null;
                }
            }

            //種を植えられなかった場合、繰り返しても種を植えられないはずなのでbreak
            if(!flag){
                break;
            }
        }

        enableDrawTicks();
    }
    @Override
    public boolean canWork(){
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]==null)			continue;
            if(itemStacks[i].stackSize<=0)	continue;
            Item item=itemStacks[i].getItem();
            if(IR2.cooperatesSS2 && ForSS2.isItemSeed(itemStacks[i])){
                return true;
            }
            if(item instanceof IPlantable)	return true;
            if(item instanceof ItemBlock){
                Block b=((ItemBlock)item).field_150939_a;
                if(b==null)	return false;
                if(b instanceof IPlantable)	return true;
            }
        }
        return false;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.sower");
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        Item item=itemstack.getItem();
        if(IR2.cooperatesSS2 && ForSS2.isItemSeed(itemstack)){
            return true;
        }
        if(item instanceof IPlantable)	return true;
        if(item instanceof ItemBlock){
            Block b=((ItemBlock)item).field_150939_a;
            if(b==null)	return false;
            return b instanceof IPlantable;
        }
        return false;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && isItemValidForSlot(i, itemstack);
    }

    protected boolean sow(IPlantable p, int meta, int i, int nX, int nZ, int nnX, int nnZ) {
        for (int k = 1; k < range + 1 && itemStacks[i].stackSize > 0; k++) {
            for (int m = 0; m < 3; m++) {
                int x = xCoord + nX * k + nnX * (m - 1);
                int y = yCoord;
                int z = zCoord + nZ * k + nnZ * (m - 1);
                Block soil = worldObj.getBlock(x, y - 1, z);
                if (soil != null && soil.canSustainPlant(worldObj, x, y - 1, z, ForgeDirection.UP, p) && worldObj.isAirBlock(x, y, z)) {
                    worldObj.setBlock(x, y, z, p.getPlant(worldObj, x, y, z), meta, 3);
                    itemStacks[i].stackSize--;
                    return true;
                }
            }
        }
        return false;
    }
    protected boolean sow(Block b, int i, int nX, int nZ, int nnX, int nnZ) {
        for (int k = 1; k < range + 1 && itemStacks[i].stackSize > 0; k++) {
            for (int m = 0; m < 3; m++) {
                int x = xCoord + nX * k + nnX * (m - 1);
                int y = yCoord;
                int z = zCoord + nZ * k + nnZ * (m - 1);
                if (b.canPlaceBlockAt(worldObj, x, y, z) && worldObj.isAirBlock(x, y, z)) {
                    worldObj.setBlock(x, y, z, b, 0, 3);
                    itemStacks[i].stackSize--;
                    return true;
                }
            }
        }
        return false;
    }
}
