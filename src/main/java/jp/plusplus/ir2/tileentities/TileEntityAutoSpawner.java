package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.ir2.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityAutoSpawner extends TileEntityCollector{
    public static final int SPAWN_RANGE=2;
    private Random rand=new Random();

    public EntityItem nextStack;//描画用
    public int itemTicks;

    public TileEntityAutoSpawner(){
        //workAmount=32*30;
        super(true);
        workAmount=128*8;
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        itemTicks++;
        if(itemTicks>=2560) itemTicks=0;

        if(!lastCanWork){
            if(nextStack!=null) nextStack=null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);

        if(nbt.hasKey("NextItem")){
            nextStack=new EntityItem(worldObj);
            nextStack.readFromNBT(nbt.getCompoundTag("NextItem"));
        }
        else{
            nextStack=null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);

        if(nextStack!=null){
            NBTTagCompound n1=new NBTTagCompound();
            nextStack.writeToNBT(n1);
            nbt.setTag("NextItem", n1);
        }
    }

    @Override
    public boolean usesSlotTakeOnly(){
        return false;
    }

    @Override
    protected void work() {
        ItemStack material=null;
        String entityName=null;
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]==null) continue;
            entityName=Recipes.getMob(itemStacks[i]);
            if(entityName==null)    continue;
            material=itemStacks[i];

            float r;
            if(frequency<64) r=1;
            else if(frequency<128) r=0.8f;
            else if(frequency<256) r=0.6f;
            else if(frequency<512) r=0.4f;
            else                     r=0.35f;

            if(rand.nextFloat()<r) {
                itemStacks[i].stackSize--;
                if (itemStacks[i].stackSize <= 0) itemStacks[i] = null;
            }

            break;
        }
        if(material==null)  return;

        ForgeDirection dir=ForgeDirection.getOrientation(side);
        EntityLiving entity = (EntityLiving) EntityList.createEntityByName(entityName, worldObj);
        if (entity == null) return;
        entity.onSpawnWithEgg(null);

        for(int i=0;i<100;i++) {
            double x = xCoord + 0.5 + (SPAWN_RANGE + 1) * dir.offsetX + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * SPAWN_RANGE;
            double y = yCoord;
            double z = zCoord + 0.5 + (SPAWN_RANGE + 1) * dir.offsetZ + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * SPAWN_RANGE;
            float a = worldObj.rand.nextFloat() * 360.0F;
            entity.setLocationAndAngles(x, y, z, a, 0);
            if (!entity.getCanSpawnHere()) continue;
            worldObj.spawnEntityInWorld(entity);
            worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);
            entity.spawnExplosionParticle();
            entity.playLivingSound();
            break;
        }
    }
    @Override
    public boolean canWork() {
        for(int i=0;i<itemStacks.length;i++){
            if(itemStacks[i]==null) continue;
            if(Recipes.getMob(itemStacks[i])!=null){
                nextStack=new EntityItem(worldObj, 0,0,0,itemStacks[i].copy());
                nextStack.getEntityItem().stackSize=1;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return Recipes.getMob(itemstack)!=null;
    }@Override
     public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return j!=side && isItemValidForSlot(i, itemstack);
    }
}
