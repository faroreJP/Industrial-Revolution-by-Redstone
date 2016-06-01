package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityFisher extends TileEntityCollector{
    public short ticks;

    public TileEntityFisher(){
        super(false);
        workAmount=32*20;
    }
    public TileEntityFisher(boolean adv){
        super(adv);
        workAmount=adv?128*20:32*20;
    }

    @Override
    public void updateEntity(){
        if(!worldObj.isRemote){
            ticks++;
            if(ticks>=20){
                markUpdateCanWorkState();
                ticks=0;
            }
        }

        super.updateEntity();
    }

    @Override
    protected void work() {
        boolean isSea=false;
        byte[] bIds=worldObj.getChunkFromBlockCoords(xCoord, zCoord).getBiomeArray();
        for(int k=0;k<bIds.length;k++) {
            if(bIds[k]==0 || bIds[k]==24) {
                isSea=true;
                break;
            }
        }

        ItemStack itemStack;
        if(isSea)   itemStack=Recipes.getFishingSea(isAdvanced?1.1f:0.65f);
        else        itemStack=Recipes.getFishing(isAdvanced?1.1f:0.65f);

        if(itemStack==null)     return;

        //振動子の場合、ちょっとだけ耐久減らす
        if(itemStack.getItem() instanceof ItemCrystalUnit){
            //itemStack.setItemDamage(itemStack.getMaxDamage()/2+worldObj.rand.nextInt(itemStack.getMaxDamage()/3));
            ItemCrystalUnit cu=(ItemCrystalUnit)itemStack.getItem();
            cu.getDamageNBT(itemStack);
            cu.setDamageNBT(itemStack, 0.5*cu.maxDamageNBT*(1+0.3* worldObj.rand.nextDouble()));
        }

        insertItem(itemStack, itemStacks);
    }
    @Override
    public boolean canWork() {
        for(int i=0;i<3;i++){
            if(worldObj.getBlock(xCoord, yCoord-1-i, zCoord)!= Blocks.water){
                return false;
            }
        }
        return true;
    }

    @Override
    public String getInventoryName() {
        return I18n.format("gui.fisher");
    }
}
