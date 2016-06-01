package jp.plusplus.ir2.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.tileentities.TileEntityHarvester;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import shift.sextiarysector.SSBlocks;
import shift.sextiarysector.SSCrops;
import shift.sextiarysector.SSFluids;
import shift.sextiarysector.SSRecipes;
import shift.sextiarysector.api.SextiarySectorAPI;
import shift.sextiarysector.api.agriculture.ICrop;
import shift.sextiarysector.api.agriculture.TileFarmland;
import shift.sextiarysector.api.event.PlayerEatenEvent;
import shift.sextiarysector.block.BlockAbstractFarmland;
import shift.sextiarysector.block.BlockCrop;
import shift.sextiarysector.item.ItemSeed;
import shift.sextiarysector.tileentity.TileEntityCrop;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/02/20.
 */
public class ForSS2 {
    public static void setup() {
        SSRecipes.sawmill.add(new ItemStack(BlockCore.logRed), new ItemStack(Blocks.planks, 6, 0));
        SSRecipes.sawmill.add(new ItemStack(BlockCore.logGold), new ItemStack(Blocks.planks, 6, 1));

        SSRecipes.timeMachine.add(new ItemStack(ItemCore.cottonRaw), new ItemStack(ItemCore.seedCotton));
        SSRecipes.timeMachine.add(new ItemStack(ItemCore.wheatGlow), new ItemStack(ItemCore.seedWheatGlow));
        SSRecipes.timeMachine.add(new ItemStack(ItemCore.melonLapis), new ItemStack(ItemCore.seedMelonLapis));

        SSRecipes.extractor.add(new ItemStack(ItemCore.waterweed), new ItemStack(ItemCore.waterproof), new FluidStack(SSFluids.oxygen, 50));

        MinecraftForge.EVENT_BUS.register(new ForSS2());
    }

    @SubscribeEvent
    public void onPlayerEatenEvent(PlayerEatenEvent e) {
        EntityPlayer player=e.entityPlayer;
        Item food=e.food.getItem();

        if(food==ItemCore.foodSmoked){
            SextiarySectorAPI.playerManager.addMoistureExhaustion(player, 12.f);
            SextiarySectorAPI.playerManager.addStaminaStats(player, 2, 2);
        }
        else if(food==ItemCore.cheese){
            SextiarySectorAPI.playerManager.addMoistureExhaustion(player, 4.f);
        }
        else if(food==ItemCore.canSoilent){
            SextiarySectorAPI.playerManager.addMoistureExhaustion(player, 8.f);
            SextiarySectorAPI.playerManager.addStaminaStats(player, 6, 6);
        }
        else if(food==ItemCore.canYamatoni){
            SextiarySectorAPI.playerManager.addMoistureExhaustion(player, 2.f);
            SextiarySectorAPI.playerManager.addStaminaStats(player, 4, 2);
        }
    }

    public static boolean tryHarvestForSS2(TileEntityHarvester te, Block block, int x, int y, int z){
        if(block instanceof BlockCrop){
            World worldObj=te.getWorldObj();
            TileEntity tileEntity=worldObj.getTileEntity(x,y,z);
            TileEntity tileEntity1=worldObj.getTileEntity(x,y-1,z);

            if(tileEntity instanceof TileEntityCrop && tileEntity1 instanceof TileFarmland) {
                TileEntityCrop tc = (TileEntityCrop) tileEntity;
                TileFarmland tf=(TileFarmland)tileEntity1;
                ICrop ic=tc.getCrop();

                if(ic.canHarvest(tc, tf)){
                    ArrayList<ItemStack> products=ic.harvest(tc,tf);
                    if(products!=null && !products.isEmpty()){
                        for(ItemStack it : products){
                            te.insertItem(it, te.itemStacks);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isItemSeed(ItemStack itemStack){
        return itemStack!=null?itemStack.getItem() instanceof ItemSeed:false;
    }

    public static boolean trySowForSS2(ItemStack itemStack, World world, int range, int x, int y, int z, int nX, int nZ, int nnX, int nnZ){
        String name= ItemSeed.getSeedName(itemStack);
        if(name.equals("none")) return false;

        ICrop ic=SSCrops.cropManager.getCrop(name);
        if(ic==null) return false;

        for (int k = 1; k < range + 1 && itemStack.stackSize > 0; k++) {
            for (int m = 0; m < 3; m++) {
                int xx = x + nX * k + nnX * (m - 1);
                int yy = y;
                int zz = z + nZ * k + nnZ * (m - 1);

                TileEntity tileEntity=world.getTileEntity(xx,yy-1,zz);
                if (world.isAirBlock(xx, yy, zz) && tileEntity instanceof TileFarmland){
                    BlockAbstractFarmland af=(BlockAbstractFarmland)world.getBlock(xx,yy-1,zz);
                    TileFarmland tf=(TileFarmland)tileEntity;

                    if(af!=null && ic.canBlockStay(af.getName(), tf)){
                        if (world.setBlock(xx, yy, zz, SSBlocks.crop)){
                            TileEntityCrop tc = (TileEntityCrop) world.getTileEntity(xx, yy, zz);
                            tc.setCrop(ic);
                            itemStack.stackSize--;
                            return true;
                        }
                    }
                }
            }
        }


        return false;
    }
}
