package jp.plusplus.ir2;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.entity.EntityNeedle;
import jp.plusplus.ir2.tileentities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ProxyServer {
    public EntityPlayer getClientPlayer(){ return null; }
    public World getClientWorld() {
        return null;
    }
    public void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityMultiBlock.class, IR2.MODID+"-multiBlock");
        GameRegistry.registerTileEntity(TileEntityPole.class, IR2.MODID+"-pole");
        GameRegistry.registerTileEntity(TileEntityForRender.class, IR2.MODID+"-forRender");

        GameRegistry.registerTileEntity(TileEntityCable.class, IR2.MODID+"-cable");
        GameRegistry.registerTileEntity(TileEntityPipeBase.class, IR2.MODID+"-pipe");
        GameRegistry.registerTileEntity(TileEntityPipeExtractor.class, IR2.MODID+"-pipeExtractor");
        GameRegistry.registerTileEntity(TileEntityPipeOneWay.class, IR2.MODID+"-pipeOneWay");
        GameRegistry.registerTileEntity(TileEntityPipeSorting.class, IR2.MODID+"-pipeSorting");
        GameRegistry.registerTileEntity(TileEntityPipeVoid.class, IR2.MODID+"-pipeVoid");
        GameRegistry.registerTileEntity(TileEntityPipeFluid.class, IR2.MODID+"-pipeFluid");
        GameRegistry.registerTileEntity(TileEntityPipeFluidExtractor.class, IR2.MODID+"-pipeFluidExtractor");
        GameRegistry.registerTileEntity(TileEntityPipeFluidVoid.class, IR2.MODID+"-pipeFluidVoid");

        GameRegistry.registerTileEntity(TileEntityGeneratorVLF.class, IR2.MODID + "-generatorVLF");
        GameRegistry.registerTileEntity(TileEntityGenerator.class, IR2.MODID+"-generator");
        GameRegistry.registerTileEntity(TileEntityAmplifier.class, IR2.MODID+"-amplifier");
        GameRegistry.registerTileEntity(TileEntityModulator.class, IR2.MODID+"-modulator");
        GameRegistry.registerTileEntity(TileEntityRelay.class, IR2.MODID+"-relay");
        GameRegistry.registerTileEntity(TileEntityRegulator.class, IR2.MODID+"-regulator");

        GameRegistry.registerTileEntity(TileEntityMachineBase.class, IR2.MODID + "-machineBase");
        GameRegistry.registerTileEntity(TileEntitySpinningProt.class, IR2.MODID + "-spinningProt");
        GameRegistry.registerTileEntity(TileEntitySpinning.class, IR2.MODID + "-spinning");
        GameRegistry.registerTileEntity(TileEntityFurnaceProt.class, IR2.MODID + "-furnaceProt");
        GameRegistry.registerTileEntity(TileEntityFurnace.class, IR2.MODID + "-furnace");
        GameRegistry.registerTileEntity(TileEntityFurnaceAdvanced.class, IR2.MODID + "-furnaceAdv");
        GameRegistry.registerTileEntity(TileEntityFurnaceVariant.class, IR2.MODID + "-furnaceVar");
        GameRegistry.registerTileEntity(TileEntityAlloySmelter.class, IR2.MODID + "-alloySmelter");
        GameRegistry.registerTileEntity(TileEntityLoom.class, IR2.MODID + "-loom");
        GameRegistry.registerTileEntity(TileEntityCrusher.class, IR2.MODID + "-crusher");
        GameRegistry.registerTileEntity(TileEntityCrusherVariant.class, IR2.MODID + "-crusherVar");
        GameRegistry.registerTileEntity(TileEntityExtractor.class, IR2.MODID + "-extractor");
        GameRegistry.registerTileEntity(TileEntityDyer.class, IR2.MODID + "-dyer");
        GameRegistry.registerTileEntity(TileEntityAutoCrafter.class, IR2.MODID + "-autoCrafter");
        GameRegistry.registerTileEntity(TileEntityFountain.class, IR2.MODID + "-fountain");
        GameRegistry.registerTileEntity(TileEntityCrucible.class, IR2.MODID + "-crucible");
        GameRegistry.registerTileEntity(TileEntitySyntheticFurnace.class, IR2.MODID + "-syntheticFurnace");
        GameRegistry.registerTileEntity(TileEntitySmoker.class, IR2.MODID + "-smoker");
        GameRegistry.registerTileEntity(TileEntityMixer.class, IR2.MODID + "-mixer");
        GameRegistry.registerTileEntity(TileEntityCheeseMaker.class, IR2.MODID + "-cheeseMaker");
        GameRegistry.registerTileEntity(TileEntityTransmitter.class, IR2.MODID + "-transmitter");
        GameRegistry.registerTileEntity(TileEntityChunkLoader.class, IR2.MODID + "-chunkLoader");
        GameRegistry.registerTileEntity(TileEntityShop.class, IR2.MODID + "-shop");

        GameRegistry.registerTileEntity(TileEntityFisher.class, IR2.MODID + "-fisher");
        GameRegistry.registerTileEntity(TileEntityShearer.class, IR2.MODID + "-shearer");
        GameRegistry.registerTileEntity(TileEntitySower.class, IR2.MODID + "-sower");
        GameRegistry.registerTileEntity(TileEntityHarvester.class, IR2.MODID + "-harvester");
        GameRegistry.registerTileEntity(TileEntityFeeder.class, IR2.MODID + "-feeder");
        GameRegistry.registerTileEntity(TileEntityWoodcutter.class, IR2.MODID + "-woodcutter");
        GameRegistry.registerTileEntity(TileEntitySmasher.class, IR2.MODID + "-smasher");
        GameRegistry.registerTileEntity(TileEntityAutoSpawner.class, IR2.MODID + "-autoSpawner");
        GameRegistry.registerTileEntity(TileEntityMiner.class, IR2.MODID + "-miner");
        GameRegistry.registerTileEntity(TileEntityButcher.class, IR2.MODID + "-butcher");
        GameRegistry.registerTileEntity(TileEntityItemCollector.class, IR2.MODID + "-itemCollector");
        GameRegistry.registerTileEntity(TileEntityLoader.class, IR2.MODID + "-loader");
        GameRegistry.registerTileEntity(TileEntityUnloader.class, IR2.MODID + "-unloader");
        GameRegistry.registerTileEntity(TileEntityChildSorter.class, IR2.MODID + "-childSorter");
        GameRegistry.registerTileEntity(TileEntityMilkingMachine.class, IR2.MODID + "-mikingMachine");
        GameRegistry.registerTileEntity(TileEntityPump.class, IR2.MODID + "-pump");

        GameRegistry.registerTileEntity(TileEntityAlloySmelterRusty.class, IR2.MODID + "-alloySmelterRusty");
        GameRegistry.registerTileEntity(TileEntityTank.class, IR2.MODID + "-tank");

        EntityRegistry.registerModEntity(EntityNeedle.class, IR2.MODID+"-needle", IR2.needleId, IR2.instance, 128, 5, true);
    }
    public int registerRenderer(ISimpleBlockRenderingHandler renderer){
        return -1;
    }
    public void registerAchievement(){
        AchievementChecker.register();
    }
    public void loadNEI(){}
}
