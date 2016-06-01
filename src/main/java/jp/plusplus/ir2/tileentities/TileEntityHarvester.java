package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.crop.BlockCrop;
import ic2.core.crop.TileEntityCrop;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.mod.ForInsanity;
import jp.plusplus.ir2.mod.ForMMCAPI;
import jp.plusplus.ir2.mod.ForSS2;
import mods.defeatedcrow.api.plants.IRightClickHarvestable;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import tconstruct.world.blocks.OreberryBush;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/06.
 */
public class TileEntityHarvester extends TileEntityCollector{
    public short range;
    public Random rand=new Random();

    public TileEntityHarvester(){
        super(false);
        workAmount=32*30;
        range=(short)4;
        DRAW_TICKS_MAX=range*20-10;
    }
    public TileEntityHarvester(boolean adv){
        //workAmount=32*30;
        super(adv);
        workAmount=adv?128*30:32*30;
        range=(short)(adv?8:4);
        DRAW_TICKS_MAX=(range-1)*20+1;
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
    public void updateEntity(){
        super.updateEntity();

        //IR2.logger.info("harvester e:"+enableDrawTicks);

        //ここで収穫処理
        if(!worldObj.isRemote && enableDrawTicks){
            int t=drawTicks-1;

            if(t%20==0){
                int tt=t/20+1;
                int nX=0, nZ=0;
                int nnX=0, nnZ=0;

                switch(side){
                    case 2:
                        nZ=-1;
                        nnX=1;
                        break;
                    case 3:
                        nZ=1;
                        nnX=-1;
                        break;
                    case 4:
                        nX=-1;
                        nnZ=1;
                        break;
                    case 5:
                        nX=1;
                        nnZ=-1;
                        break;
                }

                for(int i=0;i<3;i++){
                    int x = xCoord + nX * tt + nnX * (i - 1);
                    int y = yCoord;
                    int z = zCoord + nZ * tt + nnZ * (i - 1);

                    if (worldObj.isAirBlock(x, y, z)) continue;
                    Block block = worldObj.getBlock(x, y, z);
                    int meta=worldObj.getBlockMetadata(x, y, z);
                    boolean harvested=false;

                    if(IR2.cooperatesAMT2){
                        if(block instanceof IRightClickHarvestable){
                            for(int n=0;n<2;n++) {
                                Block bbb=worldObj.getBlock(x,y+n,z);
                                if(!(bbb instanceof IRightClickHarvestable)) break;
                                IRightClickHarvestable rch=(IRightClickHarvestable)bbb;

                                if (rch.isHarvestable(worldObj, x, y+n, z)) {
                                    rch.onHarvest(worldObj, x, y+n, z, this, null);
                                    worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                                    harvested=true;
                                }
                            }
                        }
                    }
                    if(!harvested && IR2.cooperatesSS2 && ForSS2.tryHarvestForSS2(this, block, x,y,z)) {
                        harvested = true;
                    }
                    if(!harvested && IR2.cooperatesInsanity && ForInsanity.tryHarvestForInsanity(this, x, y, z)) {
                        harvested = true;
                    }
                    if(!harvested && IR2.cooperateMMCAPI && ForMMCAPI.tryHarvest(this, block, x, y, z)) {
                        harvested = true;
                    }
                    if(!harvested && IR2.cooperatesIC2){
                        if(block instanceof BlockCrop){
                            TileEntity te=worldObj.getTileEntity(x,y,z);
                            if(te!=null && te instanceof TileEntityCrop){
                                ItemStack[] ret = ((TileEntityCrop) te).harvest_automated(true);
                                if(ret!=null) {
                                    for (int k = 0; k < ret.length; k++) {
                                        insertItem(ret[k], itemStacks);
                                    }
                                    worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                                    harvested=true;
                                }
                            }
                        }
                    }
                    if(!harvested && IR2.cooperatesTiC){
                        if(block instanceof OreberryBush && meta>=12){
                            worldObj.setBlock(x, y, z, block, meta - 4, 3);
                            ItemStack get=new ItemStack(GameRegistry.findItem("TConstruct", "oreBerries"), rand.nextInt(3) + 1, meta % 4 + ((OreberryBush) block).itemMeat);
                            insertItem(get, itemStacks);
                            worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                            harvested=true;
                        }
                    }
                    if(!harvested && IR2.cooperatesAgri){

                        /*
                        AgriCraft
                        Copyright (c) 2014 InfinityRaider
                        http://opensource.org/licenses/mit-license.php
                        */

                        if(block instanceof com.InfinityRaider.AgriCraft.blocks.BlockCrop){
                            TileEntity te=worldObj.getTileEntity(x,y,z);
                            if(te instanceof com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop){
                                com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop crop=(com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop)te;
                                if(!crop.hasWeed() && !crop.isCrossCrop() && crop.isMature()){
                                    worldObj.setBlockMetadataWithNotify(x,y,z,2,2);

                                    ArrayList<ItemStack> drops = crop.getFruits();
                                    for(ItemStack is : drops){
                                        insertItem(is, itemStacks);
                                    }
                                    worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                                    crop.markForUpdate();
                                }
                            }
                        }
                    }
                    if (!harvested && block instanceof IGrowable) {
                        if (!((IGrowable) block).func_149851_a(worldObj, x, y, z, false)) {
                            ArrayList<ItemStack> products = block.getDrops(worldObj, x, y, z, meta, 0);
                            if (products != null) {
                                for (int k = 0; k < products.size(); k++) {
                                    insertItem(products.get(k), itemStacks);
                                }
                            }
                            worldObj.func_147480_a(x, y, z, false);
                            harvested=true;
                        }
                    }
                    if(!harvested && Recipes.isHarvestTarget(block, meta)){
                        ArrayList<ItemStack> products = block.getDrops(worldObj, x, y, z, meta, 0);
                        if (products != null) {
                            for (int k = 0; k < products.size(); k++) {
                                insertItem(products.get(k), itemStacks);
                            }
                        }
                        worldObj.func_147480_a(x, y, z, false);
                        harvested=true;
                    }
                }
            }
            markDirty();
        }
    }

    @Override
    public void work(){
        enableDrawTicks();
    }

    @Override
    public boolean canWork() {
        return true;
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }
}
