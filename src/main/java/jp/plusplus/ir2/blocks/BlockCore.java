package jp.plusplus.ir2.blocks;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.items.*;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class BlockCore {
    public static Fluid fluidMilk;
    public static Fluid fluidRedstone;

    public static Block saplingRed;
    public static Block logRed;
    public static Block leaveRed;
    public static Block saplingGold;
    public static Block logGold;
    public static Block leaveGold;
    public static Block cropCotton;
    public static Block cropMelonLapis;
    public static Block blockMelonLapis;
    public static Block cropWheatGlow;
    public static Block cropPotatoQuartz;
    public static Block cropReedIron;

    public static Block ore;
    public static Block block;
    public static Block crust;

    public static Block deco;
    public static Block fan;
    public static Block lightCeiling;

    public static Block cableTin;
    public static Block cableCopper;
    public static Block cableIron;
    public static Block cableSilver;
    public static Block cableGold;
    public static Block cableAluminium;
    public static Block cableNickel;

    public static Block pipeTin;
    public static Block pipeTinExtractor;
    public static Block pipeTinOneWay;
    public static Block pipeTinSorting;
    public static Block pipeTinVoid;
    public static Block pipeTinFluid;
    public static Block pipeTinFluidExtractor;
    public static Block pipeTinFluidVoid;

    public static Block pipeCopper;
    public static Block pipeCopperExtractor;
    public static Block pipeCopperOneWay;
    public static Block pipeCopperSorting;
    public static Block pipeCopperVoid;
    public static Block pipeCopperFluid;
    public static Block pipeCopperFluidExtractor;
    public static Block pipeCopperFluidVoid;

    public static Block pipeIron;
    public static Block pipeIronExtractor;
    public static Block pipeIronOneWay;
    public static Block pipeIronSorting;
    public static Block pipeIronVoid;
    public static Block pipeIronFluid;
    public static Block pipeIronFluidExtractor;
    public static Block pipeIronFluidVoid;

    public static Block pipeSilver;
    public static Block pipeSilverExtractor;
    public static Block pipeSilverOneWay;
    public static Block pipeSilverSorting;
    public static Block pipeSilverVoid;
    public static Block pipeSilverFluid;
    public static Block pipeSilverFluidExtractor;
    public static Block pipeSilverFluidVoid;

    public static Block pipeGold;
    public static Block pipeGoldExtractor;
    public static Block pipeGoldOneWay;
    public static Block pipeGoldSorting;
    public static Block pipeGoldVoid;
    public static Block pipeGoldFluid;
    public static Block pipeGoldFluidExtractor;
    public static Block pipeGoldFluidVoid;

    public static Block pipeAluminium;
    public static Block pipeAluminiumExtractor;
    public static Block pipeAluminiumOneWay;
    public static Block pipeAluminiumSorting;
    public static Block pipeAluminiumVoid;
    public static Block pipeAluminiumFluid;
    public static Block pipeAluminiumFluidExtractor;
    public static Block pipeAluminiumFluidVoid;

    public static Block pipeNickel;
    public static Block pipeNickelExtractor;
    public static Block pipeNickelOneWay;
    public static Block pipeNickelSorting;
    public static Block pipeNickelVoid;
    public static Block pipeNickelFluid;
    public static Block pipeNickelFluidExtractor;
    public static Block pipeNickelFluidVoid;

    public static Block pipeMining;
    public static Block pole;

    public static Block casingStone;
    public static Block casingObsidian;
    public static Block casingStoneMulti;
    public static Block casingObsidianMulti;

    public static Block generatorVLF;
    public static Block generatorLF;
    public static Block generator;
    public static Block generatorHV;
    public static Block generatorHF;
    public static Block generatorVHF;
    public static Block generatorVHV;

    public static Block amplifier;
    public static Block modulator;
    public static Block relay;
    public static Block regulator;

    public static Block machineSpinningProt;
    public static Block machineSpinning;
    public static Block machineSpinningAdv;
    public static Block machineFurnaceProt;
    public static Block machineFurnace;
    public static Block machineFurnaceAdv;
    public static Block machineFurnaceVar;
    public static Block machineAlloySmelter;
    public static Block machineAlloySmelterAdv;
    public static Block machineLoom;
    public static Block machineCrusher;
    public static Block machineCrusherAdv;
    public static Block machineCrusherVar;
    public static Block machineExtractor;
    public static Block machineDyer;
    public static Block machineAutoCrafter;
    public static Block machineFountain;
    public static Block machineCrucible;
    public static Block machineSyntheticFurnace;
    public static Block machineSmoker;
    public static Block machineCheeseMaker;
    public static Block machineMayoMaker;
    public static Block machineTransmitter;
    public static Block machineTransmitterWithLoader;
    public static Block machineChunkLoader;
    public static Block machinePump;
    public static Block machineMixer;
    public static Block machineChildSorter;
    public static Block machineShop;

    public static Block machineFisher;
    public static Block machineShearer;
    public static Block machineSower;
    public static Block machineHarvester;
    public static Block machineFeeder;
    public static Block machineWoodcutter;
    public static Block machineMilking;

    public static Block machineFisherAdv;
    public static Block machineShearerAdv;
    public static Block machineSowerAdv;
    public static Block machineHarvesterAdv;
    public static Block machineFeederAdv;
    public static Block machineWoodcutterAdv;
    public static Block machineMilkingAdv;

    public static Block machineSmasher;
    public static Block machineAutoSpawner;
    public static Block machineMiner;
    public static Block machineButcher;
    public static Block machineItemCollector;
    public static Block machineLoader;
    public static Block machineUnloader;

    public static Block tankSmall;
    public static Block tankMedium;
    public static Block tankLarge;

    public static Block unifier;

    public static Block brickRusty;
    public static Block alloySmelterRustyIdle;
    public static Block alloySmelterRustyActive;

    public static void registerBlocks() {
        fluidMilk=new Fluid("IR2fluidMilk").setUnlocalizedName("IR2fluidMilk").setLuminosity(15);
        fluidRedstone=new Fluid("IR2fluidRedstone").setUnlocalizedName("IR2fluidRedstone").setLuminosity(15);
        FluidRegistry.registerFluid(fluidMilk);
        FluidRegistry.registerFluid(fluidRedstone);

        saplingRed=new BlockSaplingIR2().setBlockName("IR2saplingRed").setBlockTextureName(IR2.MODID + ":saplingRed");
        logRed=new BlockLogIR2().setBlockName("IR2logRed");
        leaveRed=new BlockLeaveIR2().setBlockName("IR2leaveRed");
        saplingGold=new BlockSaplingIR2().setBlockName("IR2saplingGold").setBlockTextureName(IR2.MODID + ":saplingGold");
        logGold=new BlockLogIR2().setBlockName("IR2logGold");
        leaveGold=new BlockLeaveIR2().setBlockName("IR2leaveGold");
        cropCotton=new BlockCotton();
        blockMelonLapis=new BlockMelonLapis();
        cropMelonLapis=new BlockCropMelonLapis();
        cropWheatGlow=new BlockWheatGlow();
        cropPotatoQuartz=new BlockCropPotatoQuartz();
        cropReedIron=new BlockReedIron();
        GameRegistry.registerBlock(saplingRed, "saplingRed");
        GameRegistry.registerBlock(logRed, "logRed");
        GameRegistry.registerBlock(leaveRed, "leaveRed");
        GameRegistry.registerBlock(saplingGold, "saplingGold");
        GameRegistry.registerBlock(logGold, "logGold");
        GameRegistry.registerBlock(leaveGold, "leaveGold");
        GameRegistry.registerBlock(cropCotton, "cropCotton");
        GameRegistry.registerBlock(cropMelonLapis, "cropMelonLapis");
        GameRegistry.registerBlock(blockMelonLapis, "blockMelonLapis");
        GameRegistry.registerBlock(cropWheatGlow, "cropWheatGlow");
        GameRegistry.registerBlock(cropPotatoQuartz, "cropPotatoQuartz");
        GameRegistry.registerBlock(cropReedIron, "cropReedIron");

        //
        ore=new BlockOre().setBlockName("IR2ore").setBlockTextureName(IR2.MODID+":ore").setHardness(1.0f).setResistance(15.0F);
        GameRegistry.registerBlock(ore, ItemOre.class, "ore");
        block=new BlockOre().setBlockName("IR2block").setBlockTextureName(IR2.MODID+":block").setHardness(1.5f).setResistance(30.0F);
        GameRegistry.registerBlock(block, ItemOre.class, "block");
        crust=new BlockCrust();
        GameRegistry.registerBlock(crust, ItemOre.class, "crust");

        //
        deco=new BlockDecoration();
        fan=new BlockFan();
        lightCeiling=new BlockCeilingLight();
        GameRegistry.registerBlock(deco, ItemWithMeta.class, "decoration");
        GameRegistry.registerBlock(fan, "fan");
        GameRegistry.registerBlock(lightCeiling, ItemWithMeta.class, "lightCeiling");

        //----------------------------------------- cable----------------------------------------------------------------
        cableTin =new BlockCable((short)16, (short)16).setBlockName("cableTin").setBlockTextureName(IR2.MODID+":cableTin");
        cableCopper =new BlockCable((short)32, (short)16).setBlockName("cableCopper").setBlockTextureName(IR2.MODID+":cableCopper");
        cableIron =new BlockCable((short)32, (short)32).setBlockName("cableIron").setBlockTextureName(IR2.MODID + ":cableIron");
        cableSilver =new BlockCable((short)64, (short)64).setBlockName("cableSilver").setBlockTextureName(IR2.MODID + ":cableSilver");
        cableGold =new BlockCable((short)256, (short)256).setBlockName("cableGold").setBlockTextureName(IR2.MODID + ":cableGold");
        cableAluminium =new BlockCable((short)2048, (short)64).setBlockName("cableAluminium").setBlockTextureName(IR2.MODID + ":cableAluminium");
        cableNickel =new BlockCable((short)64, (short)2048).setBlockName("cableNickel").setBlockTextureName(IR2.MODID + ":cableNickel");
        GameRegistry.registerBlock(cableTin, ItemCable.class, "cableTin");
        GameRegistry.registerBlock(cableCopper, ItemCable.class, "cableCopper");
        GameRegistry.registerBlock(cableIron, ItemCable.class, "cableIron");
        GameRegistry.registerBlock(cableSilver, ItemCable.class, "cableSilver");
        GameRegistry.registerBlock(cableGold, ItemCable.class, "cableGold");
        GameRegistry.registerBlock(cableAluminium, ItemCable.class, "cableAluminium");
        GameRegistry.registerBlock(cableNickel, ItemCable.class, "cableNickel");

        //----------------------------------------- pipe----------------------------------------------------------------

        pipeTin =new BlockPipe((short)16, (short)16, "pipe", 1).setBlockName("pipeTin");
        pipeTinExtractor =new BlockPipe((short)16, (short)16, "pipeExtractor", 2).setBlockName("pipeTin");
        pipeTinOneWay =new BlockPipe((short)16, (short)16, "pipeOneWay", 2).setBlockName("pipeTin");
        pipeTinSorting =new BlockPipe((short)16, (short)16, "pipeSorting", 2).setBlockName("pipeTin");
        pipeTinVoid =new BlockPipe((short)16, (short)16, "pipeVoid", 2).setBlockName("pipeTin");
        pipeTinFluid =new BlockPipe((short)16, (short)16, "pipeFluid", 1).setBlockName("pipeTin");
        pipeTinFluidExtractor =new BlockPipe((short)16, (short)16, "pipeFluidExtractor", 2).setBlockName("pipeTin");
        pipeTinFluidVoid =new BlockPipe((short)16, (short)16, "pipeFluidVoid", 2).setBlockName("pipeTin");
        GameRegistry.registerBlock(pipeTin, ItemPipe.class, "pipeTin");
        GameRegistry.registerBlock(pipeTinExtractor, ItemPipe.class, "pipeTinExtractor");
        GameRegistry.registerBlock(pipeTinOneWay, ItemPipe.class, "pipeTinOneWay");
        GameRegistry.registerBlock(pipeTinSorting, ItemPipe.class, "pipeTinSorting");
        GameRegistry.registerBlock(pipeTinVoid, ItemPipe.class, "pipeTinVoid");
        GameRegistry.registerBlock(pipeTinFluid, ItemPipe.class, "pipeTinFluid");
        GameRegistry.registerBlock(pipeTinFluidExtractor, ItemPipe.class, "pipeTinFluidExtractor");
        GameRegistry.registerBlock(pipeTinFluidVoid, ItemPipe.class, "pipeTinFluidVoid");

        pipeCopper =new BlockPipe((short)32, (short)16, "pipe", 1).setBlockName("pipeCopper");
        pipeCopperExtractor =new BlockPipe((short)32, (short)16, "pipeExtractor", 2).setBlockName("pipeCopper");
        pipeCopperOneWay =new BlockPipe((short)32, (short)16, "pipeOneWay", 2).setBlockName("pipeCopper");
        pipeCopperSorting =new BlockPipe((short)32, (short)16, "pipeSorting", 2).setBlockName("pipeCopper");
        pipeCopperVoid =new BlockPipe((short)32, (short)16, "pipeVoid", 2).setBlockName("pipeCopper");
        pipeCopperFluid =new BlockPipe((short)32, (short)16, "pipeFluid", 1).setBlockName("pipeCopper");
        pipeCopperFluidExtractor =new BlockPipe((short)32, (short)16, "pipeFluidExtractor", 2).setBlockName("pipeCopper");
        pipeCopperFluidVoid =new BlockPipe((short)32, (short)16, "pipeFluidVoid", 2).setBlockName("pipeCopper");
        GameRegistry.registerBlock(pipeCopper, ItemPipe.class, "pipeCopper");
        GameRegistry.registerBlock(pipeCopperExtractor, ItemPipe.class, "pipeCopperExtractor");
        GameRegistry.registerBlock(pipeCopperOneWay, ItemPipe.class, "pipeCopperOneWay");
        GameRegistry.registerBlock(pipeCopperSorting, ItemPipe.class, "pipeCopperSorting");
        GameRegistry.registerBlock(pipeCopperVoid, ItemPipe.class, "pipeCopperVoid");
        GameRegistry.registerBlock(pipeCopperFluid, ItemPipe.class, "pipeCopperFluid");
        GameRegistry.registerBlock(pipeCopperFluidExtractor, ItemPipe.class, "pipeCopperFluidExtractor");
        GameRegistry.registerBlock(pipeCopperFluidVoid, ItemPipe.class, "pipeCopperFluidVoid");

        pipeIron =new BlockPipe((short)32, (short)32, "pipe", 1).setBlockName("pipeIron");
        pipeIronExtractor =new BlockPipe((short)32, (short)32, "pipeExtractor", 2).setBlockName("pipeIron");
        pipeIronOneWay =new BlockPipe((short)32, (short)32, "pipeOneWay", 2).setBlockName("pipeIron");
        pipeIronSorting =new BlockPipe((short)32, (short)32, "pipeSorting", 2).setBlockName("pipeIron");
        pipeIronVoid =new BlockPipe((short)32, (short)32, "pipeVoid", 2).setBlockName("pipeIron");
        pipeIronFluid =new BlockPipe((short)32, (short)32, "pipeFluid", 1).setBlockName("pipeIron");
        pipeIronFluidExtractor =new BlockPipe((short)32, (short)32, "pipeFluidExtractor", 2).setBlockName("pipeIron");
        pipeIronFluidVoid =new BlockPipe((short)32, (short)32, "pipeFluidVoid", 2).setBlockName("pipeIron");
        GameRegistry.registerBlock(pipeIron, ItemPipe.class, "pipeIron");
        GameRegistry.registerBlock(pipeIronExtractor, ItemPipe.class, "pipeIronExtractor");
        GameRegistry.registerBlock(pipeIronOneWay, ItemPipe.class, "pipeIronOneWay");
        GameRegistry.registerBlock(pipeIronSorting, ItemPipe.class, "pipeIronSorting");
        GameRegistry.registerBlock(pipeIronVoid, ItemPipe.class, "pipeIronVoid");
        GameRegistry.registerBlock(pipeIronFluid, ItemPipe.class, "pipeIronFluid");
        GameRegistry.registerBlock(pipeIronFluidExtractor, ItemPipe.class, "pipeIronFluidExtractor");
        GameRegistry.registerBlock(pipeIronFluidVoid, ItemPipe.class, "pipeIronFluidVoid");

        pipeSilver =new BlockPipe((short)64, (short)64, "pipe", 1).setBlockName("pipeSilver");
        pipeSilverExtractor =new BlockPipe((short)64, (short)64, "pipeExtractor", 2).setBlockName("pipeSilver");
        pipeSilverOneWay =new BlockPipe((short)64, (short)64, "pipeOneWay", 2).setBlockName("pipeSilver");
        pipeSilverSorting =new BlockPipe((short)64, (short)64, "pipeSorting", 2).setBlockName("pipeSilver");
        pipeSilverVoid =new BlockPipe((short)64, (short)64, "pipeVoid", 2).setBlockName("pipeSilver");
        pipeSilverFluid =new BlockPipe((short)64, (short)64, "pipeFluid", 1).setBlockName("pipeSilver");
        pipeSilverFluidExtractor =new BlockPipe((short)64, (short)64, "pipeFluidExtractor", 2).setBlockName("pipeSilver");
        pipeSilverFluidVoid =new BlockPipe((short)64, (short)64, "pipeFluidVoid", 2).setBlockName("pipeSilver");
        GameRegistry.registerBlock(pipeSilver, ItemPipe.class, "pipeSilver");
        GameRegistry.registerBlock(pipeSilverExtractor, ItemPipe.class, "pipeSilverExtractor");
        GameRegistry.registerBlock(pipeSilverOneWay, ItemPipe.class, "pipeSilverOneWay");
        GameRegistry.registerBlock(pipeSilverSorting, ItemPipe.class, "pipeSilverSorting");
        GameRegistry.registerBlock(pipeSilverVoid, ItemPipe.class, "pipeSilverVoid");
        GameRegistry.registerBlock(pipeSilverFluid, ItemPipe.class, "pipeSilverFluid");
        GameRegistry.registerBlock(pipeSilverFluidExtractor, ItemPipe.class, "pipeSilverFluidExtractor");
        GameRegistry.registerBlock(pipeSilverFluidVoid, ItemPipe.class, "pipeSilverFluidVoid");

        pipeGold =new BlockPipe((short)256, (short)256, "pipe", 1).setBlockName("pipeGold");
        pipeGoldExtractor =new BlockPipe((short)256, (short)256, "pipeExtractor", 2).setBlockName("pipeGold");
        pipeGoldOneWay =new BlockPipe((short)256, (short)256, "pipeOneWay", 2).setBlockName("pipeGold");
        pipeGoldSorting =new BlockPipe((short)256, (short)256, "pipeSorting", 2).setBlockName("pipeGold");
        pipeGoldVoid =new BlockPipe((short)256, (short)256, "pipeVoid", 2).setBlockName("pipeGold");
        pipeGoldFluid =new BlockPipe((short)256, (short)256, "pipeFluid", 1).setBlockName("pipeGold");
        pipeGoldFluidExtractor =new BlockPipe((short)256, (short)256, "pipeFluidExtractor", 2).setBlockName("pipeGold");
        pipeGoldFluidVoid =new BlockPipe((short)256, (short)256, "pipeFluidVoid", 2).setBlockName("pipeGold");
        GameRegistry.registerBlock(pipeGold, ItemPipe.class, "pipeGold");
        GameRegistry.registerBlock(pipeGoldExtractor, ItemPipe.class, "pipeGoldExtractor");
        GameRegistry.registerBlock(pipeGoldOneWay, ItemPipe.class, "pipeGoldOneWay");
        GameRegistry.registerBlock(pipeGoldSorting, ItemPipe.class, "pipeGoldSorting");
        GameRegistry.registerBlock(pipeGoldVoid, ItemPipe.class, "pipeGoldVoid");
        GameRegistry.registerBlock(pipeGoldFluid, ItemPipe.class, "pipeGoldFluid");
        GameRegistry.registerBlock(pipeGoldFluidExtractor, ItemPipe.class, "pipeGoldFluidExtractor");
        GameRegistry.registerBlock(pipeGoldFluidVoid, ItemPipe.class, "pipeGoldFluidVoid");

        pipeAluminium =new BlockPipe((short)2048, (short)64, "pipe", 1).setBlockName("pipeAluminium");
        pipeAluminiumExtractor =new BlockPipe((short)2048, (short)64, "pipeExtractor", 2).setBlockName("pipeAluminium");
        pipeAluminiumOneWay =new BlockPipe((short)2048, (short)64, "pipeOneWay", 2).setBlockName("pipeAluminium");
        pipeAluminiumSorting =new BlockPipe((short)2048, (short)64, "pipeSorting", 2).setBlockName("pipeAluminium");
        pipeAluminiumVoid =new BlockPipe((short)2048, (short)64, "pipeVoid", 2).setBlockName("pipeAluminium");
        pipeAluminiumFluid =new BlockPipe((short)2048, (short)64, "pipeFluid", 1).setBlockName("pipeAluminium");
        pipeAluminiumFluidExtractor =new BlockPipe((short)2048, (short)64, "pipeFluidExtractor", 2).setBlockName("pipeAluminium");
        pipeAluminiumFluidVoid =new BlockPipe((short)2048, (short)64, "pipeFluidVoid", 2).setBlockName("pipeAluminium");
        GameRegistry.registerBlock(pipeAluminium, ItemPipe.class, "pipeAluminium");
        GameRegistry.registerBlock(pipeAluminiumExtractor, ItemPipe.class, "pipeAluminiumExtractor");
        GameRegistry.registerBlock(pipeAluminiumOneWay, ItemPipe.class, "pipeAluminiumOneWay");
        GameRegistry.registerBlock(pipeAluminiumSorting, ItemPipe.class, "pipeAluminiumSorting");
        GameRegistry.registerBlock(pipeAluminiumVoid, ItemPipe.class, "pipeAluminiumVoid");
        GameRegistry.registerBlock(pipeAluminiumFluid, ItemPipe.class, "pipeAluminiumFluid");
        GameRegistry.registerBlock(pipeAluminiumFluidExtractor, ItemPipe.class, "pipeAluminiumFluidExtractor");
        GameRegistry.registerBlock(pipeAluminiumFluidVoid, ItemPipe.class, "pipeAluminiumFluidVoid");

        pipeNickel =new BlockPipe((short)64, (short)2048, "pipe", 1).setBlockName("pipeNickel");
        pipeNickelExtractor =new BlockPipe((short)64, (short)2048, "pipeExtractor", 2).setBlockName("pipeNickel");
        pipeNickelOneWay =new BlockPipe((short)64, (short)2048, "pipeOneWay", 2).setBlockName("pipeNickel");
        pipeNickelSorting =new BlockPipe((short)64, (short)2048, "pipeSorting", 2).setBlockName("pipeNickel");
        pipeNickelVoid =new BlockPipe((short)64, (short)2048, "pipeVoid", 2).setBlockName("pipeNickel");
        pipeNickelFluid =new BlockPipe((short)64, (short)2048, "pipeFluid", 1).setBlockName("pipeNickel");
        pipeNickelFluidExtractor =new BlockPipe((short)64, (short)2048, "pipeFluidExtractor", 2).setBlockName("pipeNickel");
        pipeNickelFluidVoid =new BlockPipe((short)64, (short)2048, "pipeFluidVoid", 2).setBlockName("pipeNickel");
        GameRegistry.registerBlock(pipeNickel, ItemPipe.class, "pipeNickel");
        GameRegistry.registerBlock(pipeNickelExtractor, ItemPipe.class, "pipeNickelExtractor");
        GameRegistry.registerBlock(pipeNickelOneWay, ItemPipe.class, "pipeNickelOneWay");
        GameRegistry.registerBlock(pipeNickelSorting, ItemPipe.class, "pipeNickelSorting");
        GameRegistry.registerBlock(pipeNickelVoid, ItemPipe.class, "pipeNickelVoid");
        GameRegistry.registerBlock(pipeNickelFluid, ItemPipe.class, "pipeNickelFluid");
        GameRegistry.registerBlock(pipeNickelFluidExtractor, ItemPipe.class, "pipeNickelFluidExtractor");
        GameRegistry.registerBlock(pipeNickelFluidVoid, ItemPipe.class, "pipeNickelFluidVoid");

        pipeMining=new BlockPipeMining();
        pole=new BlockPole();
        GameRegistry.registerBlock(pipeMining, "pipeMining");
        GameRegistry.registerBlock(pole, ItemPole.class, "pole");

        //--------------------------------------------------------------------------------------------------------------

        casingStone=new BlockCasing().setBlockName("IR2casingStone").setBlockTextureName("Stone").setHardness(3.5f).setResistance(17.5f);
        casingStone.setHarvestLevel("pickaxe", 0);
        casingObsidian=new BlockCasing().setBlockName("IR2casingObsidian").setBlockTextureName("Obsidian").setHardness(50.0f).setResistance(6000.0f);
        casingObsidian.setHarvestLevel("pickaxe", 3);
        casingStoneMulti=new BlockMulti().setBlockName("IR2casingStone").setBlockTextureName("casingStone").setHardness(3.5f).setResistance(17.5f);
        casingStoneMulti.setHarvestLevel("pickaxe", 0);
        casingObsidianMulti=new BlockMulti().setBlockName("IR2casingObsidian").setBlockTextureName("casingObsidian").setHardness(50.0f).setResistance(6000.0f);
        casingObsidianMulti.setHarvestLevel("pickaxe", 3);
        GameRegistry.registerBlock(casingStone, "casingStone");
        GameRegistry.registerBlock(casingObsidian, "casingObsidian");
        GameRegistry.registerBlock(casingStoneMulti, "casingStoneMulti");
        GameRegistry.registerBlock(casingObsidianMulti, "casingObsidianMulti");

        generatorVLF=new BlockGeneratorVLF();
        generatorLF=new BlockGenerator((short)32, (short)16, "Stone").setBlockName("generatorLF").setBlockTextureName(IR2.MODID+":generatorLF");
        generator=new BlockGenerator((short)32, (short)32, "Stone").setBlockName("generator").setBlockTextureName(IR2.MODID + ":generator");
        generatorHV=new BlockGenerator((short)64, (short)32, "Stone").setBlockName("generatorHV").setBlockTextureName(IR2.MODID + ":generatorHV");
        generatorHF=new BlockGenerator((short)32, (short)64, "Obsidian").setBlockName("generatorHF").setBlockTextureName(IR2.MODID + ":generatorHF");
        generatorVHF=new BlockGenerator((short)32, (short)128, "Obsidian").setBlockName("generatorVHF").setBlockTextureName(IR2.MODID + ":generatorVHF");
        generatorVHV=new BlockGenerator((short)128, (short)32, "Obsidian").setBlockName("generatorVHV").setBlockTextureName(IR2.MODID + ":generatorVHV");
        GameRegistry.registerBlock(generatorVLF, ItemGenerator.class, "generatorVLF");
        GameRegistry.registerBlock(generatorLF, ItemGenerator.class, "generatorLF");
        GameRegistry.registerBlock(generator, ItemGenerator.class, "generator");
        GameRegistry.registerBlock(generatorHV, ItemGenerator.class, "generatorHV");
        GameRegistry.registerBlock(generatorHF, ItemGenerator.class, "generatorHF");
        GameRegistry.registerBlock(generatorVHF, ItemGenerator.class, "generatorVHF");
        GameRegistry.registerBlock(generatorVHV, ItemGenerator.class, "generatorVHV");

        amplifier=new BlockAmplifier();
        GameRegistry.registerBlock(amplifier, ItemMachine.class, "amplifier");
        modulator=new BlockModulator();
        GameRegistry.registerBlock(modulator, ItemMachine.class, "modulator");
        relay=new BlockRelay();
        GameRegistry.registerBlock(relay, ItemMachine.class, "relay");
        regulator=new BlockRegulator();
        GameRegistry.registerBlock(regulator, ItemMachine.class, "regulator");

        machineSpinningProt=new BlockSpinningProt();
        machineFurnaceProt=new BlockFurnaceProt();
        GameRegistry.registerBlock(machineSpinningProt, ItemMachine.class, "spinningProt");
        GameRegistry.registerBlock(machineFurnaceProt, ItemMachine.class, "redstoneFurnaceProt");

        machineSpinning=new BlockSpinning("spinning", 1, "Stone").setBlockTextureName(IR2.MODID + ":machineSpinning");
        machineFurnace=new BlockFurnace();
        machineAlloySmelter=new BlockAlloySmelter("alloySmelter", 2, "Stone").setBlockTextureName(IR2.MODID + ":machineAlloySmelter");
        machineCrusher =new BlockCrusher(false);
        machineDyer=new BlockDyer();
        machineLoom =new BlockLoom();
        machineAutoCrafter=new BlockAutoCrafter();
        machineFountain =new BlockFountain();
        machineCrucible =new BlockCrucible();
        machineSyntheticFurnace =new BlockSyntheticFurnace();
        machineSmoker=new BlockSmoker();
        machineCheeseMaker=new BlockCheeseMaker();
        machinePump=new BlockPump();
        machineMixer=new BlockMixer();
        machineChildSorter=new BlockChildSorter();
        machineShop=new BlockShop();
        GameRegistry.registerBlock(machineSpinning, ItemMachine.class, "spinning");
        GameRegistry.registerBlock(machineFurnace, ItemMachine.class, "redstoneFurnace");
        GameRegistry.registerBlock(machineAlloySmelter, ItemMachine.class, "alloySmelter");
        GameRegistry.registerBlock(machineCrusher, ItemMachine.class, "crusher2");
        GameRegistry.registerBlock(machineLoom, ItemMachine.class, "loom");
        GameRegistry.registerBlock(machineDyer, ItemMachine.class, "dyer");
        GameRegistry.registerBlock(machineAutoCrafter, ItemMachine.class, "crafter");
        GameRegistry.registerBlock(machineFountain, ItemMachine.class, "fountain");
        GameRegistry.registerBlock(machineCrucible, ItemMachine.class, "crucible");
        GameRegistry.registerBlock(machineSyntheticFurnace, ItemMachine.class, "syntheticFurnace");
        GameRegistry.registerBlock(machineSmoker, ItemMachine.class, "smoker");
        GameRegistry.registerBlock(machineCheeseMaker, ItemMachine.class, "cheeseMaker");
        GameRegistry.registerBlock(machinePump, ItemMachine.class, "pump");
        GameRegistry.registerBlock(machineMixer, ItemMachine.class, "mixer");
        GameRegistry.registerBlock(machineChildSorter, ItemMachine.class, "childSorter");
        GameRegistry.registerBlock(machineShop, ItemMachine.class, "shop");

        machineSpinningAdv=new BlockSpinning("spinningAdv", 1, "Obsidian").setBlockTextureName(IR2.MODID + ":machineSpinningAdv");
        machineFurnaceAdv=new BlockFurnaceAdvanced();
        machineAlloySmelterAdv=new BlockAlloySmelter("alloySmelterAdv", 2, "Obsidian").setBlockTextureName(IR2.MODID + ":machineAlloySmelterAdv");
        machineCrusherAdv =new BlockCrusher(true);
        machineExtractor=new BlockExtractor();
        machineTransmitter=new BlockTransmitter(false);
        machineTransmitterWithLoader=new BlockTransmitter(true);
        machineChunkLoader=new BlockChunkLoader();
        GameRegistry.registerBlock(machineSpinningAdv, ItemMachine.class, "spinningAdv");
        GameRegistry.registerBlock(machineFurnaceAdv, ItemMachine.class, "redstoneFurnaceAdvanced");
        GameRegistry.registerBlock(machineAlloySmelterAdv, ItemMachine.class, "alloySmelterAdv");
        GameRegistry.registerBlock(machineCrusherAdv, ItemMachine.class, "crusher");
        GameRegistry.registerBlock(machineExtractor, ItemMachine.class, "extractor");
        GameRegistry.registerBlock(machineTransmitter, ItemMachine.class, "transmitter");
        GameRegistry.registerBlock(machineTransmitterWithLoader, ItemMachine.class, "transmitterWithLoader");
        GameRegistry.registerBlock(machineChunkLoader, ItemMachine.class, "chunkLoader");

        machineFurnaceVar=new BlockFurnaceVariant();
        machineCrusherVar =new BlockCrusherVariant();
        GameRegistry.registerBlock(machineFurnaceVar, ItemMachine.class, "redstoneFurnaceVar");
        GameRegistry.registerBlock(machineCrusherVar, ItemMachine.class, "crusherVar");

        machineFisher=new BlockFisher(false);
        machineShearer=new BlockShearer(false);
        machineSower=new BlockSower(false);
        machineHarvester=new BlockHarvester(false);
        machineFeeder=new BlockFeeder(false);
        machineWoodcutter=new BlockWoodcutter(false);
        machineMilking=new BlockMilkingMachine(false);
        GameRegistry.registerBlock(machineFisher, ItemMachine.class, "fisher");
        GameRegistry.registerBlock(machineShearer, ItemMachine.class, "shearer");
        GameRegistry.registerBlock(machineSower, ItemMachine.class, "sower");
        GameRegistry.registerBlock(machineHarvester, ItemMachine.class, "harvester");
        GameRegistry.registerBlock(machineFeeder, ItemMachine.class, "feeder");
        GameRegistry.registerBlock(machineWoodcutter, ItemMachine.class, "woodcutter");
        GameRegistry.registerBlock(machineMilking, ItemMachine.class, "milking");

        machineFisherAdv=new BlockFisher(true);
        machineShearerAdv=new BlockShearer(true);
        machineSowerAdv=new BlockSower(true);
        machineHarvesterAdv=new BlockHarvester(true);
        machineFeederAdv=new BlockFeeder(true);
        machineWoodcutterAdv=new BlockWoodcutter(true);
        machineMilkingAdv=new BlockMilkingMachine(true);
        GameRegistry.registerBlock(machineFisherAdv, ItemMachine.class, "fisherAdv");
        GameRegistry.registerBlock(machineShearerAdv, ItemMachine.class, "shearerAdv");
        GameRegistry.registerBlock(machineSowerAdv, ItemMachine.class, "sowerAdv");
        GameRegistry.registerBlock(machineHarvesterAdv, ItemMachine.class, "harvesterAdv");
        GameRegistry.registerBlock(machineFeederAdv, ItemMachine.class, "feederAdv");
        GameRegistry.registerBlock(machineWoodcutterAdv, ItemMachine.class, "woodcutterAdv");
        GameRegistry.registerBlock(machineMilkingAdv, ItemMachine.class, "milkingAdv");

        machineSmasher=new BlockSmasher();
        machineButcher=new BlockButcher();
        GameRegistry.registerBlock(machineSmasher, ItemMachine.class, "smasher");
        GameRegistry.registerBlock(machineButcher, ItemMachine.class, "butcher");

        machineAutoSpawner=new BlockAutoSpawner();
        machineMiner=new BlockMiner();
        machineItemCollector=new BlockItemCollector();
        machineLoader=new BlockLoader();
        machineUnloader=new BlockUnloader();
        GameRegistry.registerBlock(machineAutoSpawner, ItemMachine.class, "autoSpawner");
        GameRegistry.registerBlock(machineMiner, ItemMachine.class, "miner");
        GameRegistry.registerBlock(machineItemCollector, ItemMachine.class, "itemCollector");
        GameRegistry.registerBlock(machineLoader, ItemMachine.class, "loader");
        GameRegistry.registerBlock(machineUnloader, ItemMachine.class, "unloader");

        brickRusty=new BlockNormal().setBlockName("IR2brickRusty").setBlockTextureName(IR2.MODID+":brickRusty").setHardness(2.f).setResistance(20.0f);
        brickRusty.setHarvestLevel("pickaxe", 0);
        GameRegistry.registerBlock(brickRusty, "brickRusty");

        alloySmelterRustyIdle=(new BlockAlloySmelterRusty(false)).setCreativeTab(IR2.tabIR2);
        alloySmelterRustyActive=(new BlockAlloySmelterRusty(true)).setLightLevel(0.8f);
        GameRegistry.registerBlock(alloySmelterRustyIdle, ItemMachine.class, "alloySmelterRustyOff");
        GameRegistry.registerBlock(alloySmelterRustyActive, "alloySmelterRustyOn");

        tankSmall=new BlockTank(32000, "tankSmall").setBlockName("IR2tankSmall").setBlockTextureName(IR2.MODID+":tankSmall");
        tankMedium=new BlockTank(128000, "tankMedium").setBlockName("IR2tankMedium").setBlockTextureName(IR2.MODID + ":tankMedium");
        tankLarge=new BlockTank(512000, "tankLarge").setBlockName("IR2tankLarge").setBlockTextureName(IR2.MODID + ":tankLarge");
        GameRegistry.registerBlock(tankSmall, ItemMachine.class, "tankSmall");
        GameRegistry.registerBlock(tankMedium, ItemMachine.class,  "tankMedium");
        GameRegistry.registerBlock(tankLarge, ItemMachine.class,  "tankLarge");

        unifier=new BlockUnifier();
        GameRegistry.registerBlock(unifier, ItemMachine.class, "unifier");
    }
}
