package jp.plusplus.ir2;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.render.*;
import jp.plusplus.ir2.gui.GuiHandler;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemIngot;
import jp.plusplus.ir2.packet.IR2PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by plusplus_F on 2015/01/31.
 *
 * IR3を開発環境に組み込んでくれてありがとーーーー！！
 * IR3と連携とかいろいろやりたい方へ・・・・・・
 *
 * ブロックとかアイテムのインスタンスどこ？
 * -> BlockCore または ItemCore を参照してください
 *
 * 加工レシピとか漁獲テーブルとか合成テーブルを追加したい！
 * -> api.IR3RecipeAPIクラスを参照してください
 * -> Recipesクラスを参照してください
 *
 * RS機械を追加したい！
 * -> BlockMachineBase, TileEntityMachineBase を継承してください
 *    アイテムクラスにItemMachineは使わないほうがいいかもしれません
 *
 * 発展RS信号を扱うブロックを作りたい！
 * -> IConductor を実装した TileEntity を生成するブロックを実装してください。
 *
 * IR3のレンチとか金槌に対応したい！
 * -> api パッケージ内の IWrenchHandler, IHammerHandler を block に実装してください
 *
 */
@Mod(modid = IR2.MODID, version = IR2.VERSION, name = IR2.NAME, dependencies = "required-after:Forge@[10.13.4.1448,);required-after:mceconomy2@[2.5.0,)")
public class IR2 {
    public static final String NAME="Industrial Revolution by Redstone";
    public static final String MODID = "jp-plusplus-ir2";
    public static final String VERSION = "3.1.1";

    @Mod.Instance(IR2.MODID)
    public static IR2 instance;

    public static final CreativeTabs tabIR2=new Tab("tab-"+IR2.MODID);
    public static Logger logger= LogManager.getLogger("IR3");

    public static final int GUI_ID_BAG =1;

    //--------------------------------------------------------------------
    public static int renderCableId;
    public static int renderCableNewId;
    public static int renderDirectionalId;
    public static int renderAmplifierId;
    public static int renderPipeMiningId;
    public static int renderPipeId;
    public static int renderFluidTankId;
    public static int renderPGId;
    public static int renderPoleId;
    public static int renderFanId;
    public static int renderCeilingLightId;

    public static int renderMultiId;
    public static int renderMachineSpinningId;
    public static int renderCrusherId;
    public static int renderAlloySmelterId;
    public static int renderExtractorId;
    public static int renderTransmitterId;
    public static int renderChunkLoaderId;
    public static int renderFountainId;
    public static int renderPumpId;
    public static int renderFisherId;
    public static int renderHarvesterId;
    public static int renderSowerId;
    public static int renderSpawnerId;
    public static int renderSyntheticFurnaceId;

    //--------------------------------------------------------------------
    public static int needleId;
    public static int torpedoId;

    //--------------------------------------------------------------------
    public static boolean machinesExplode;
    public static boolean oresAreGenerated;
    public static boolean generateTinOre;
    public static boolean generateCopperOre;
    public static boolean generateSilverOre;
    public static boolean generateManganeseCrust;
    public static boolean generateCobaltCrust;
    public static boolean enableDescription;
    public static boolean enableDescriptionOfRating;
    public static boolean enableGenericComposition;
    public static boolean useOldPipe;

    public static boolean cooperatesAMT2;
    public static boolean cooperatesSS2;
    public static boolean cooperatesIC2;
    public static boolean cooperatesTiC;
    public static boolean cooperatesAgri;
    public static boolean cooperatesInsanity;
    public static boolean cooperateMMCAPI;

    @SidedProxy(clientSide = "jp.plusplus.ir2.ProxyClient", serverSide = "jp.plusplus.ir2.ProxyServer")
    public static ProxyServer proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ItemCore.addToolMaterials();

        BlockCore.registerBlocks();
        ItemCore.RegisterItems();

        Recipes.RegisterOreDictionary();

        MinecraftForge.ORE_GEN_BUS.register(this);
        //MinecraftForge.EVENT_BUS.register(this);
        //FMLCommonHandler.instance().bus().register(this);
        AchievementChecker.init();

        IR2PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LoadConfiguration();

        proxy.registerTileEntity();
        proxy.registerAchievement();

        ForgeChunkManager.setForcedChunkLoadingCallback(IR2.instance, ChunkLoaderManager.instance());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandler());
        GameRegistry.registerFuelHandler((IFuelHandler) BlockCore.logRed);
        Recipes.RegisterRecipes();
        Recipes.RegisterEXPTable();
        Recipes.RegisterFilledCan();
        /*
        if(enableGenericComposition) Recipes.RegisterBuildingItems2();
        else Recipes.RegisterBuildingItems();
        */
        Recipes.RegisterBuildingItems2();
        Recipes.SetUpCooperation();
        MinecraftForge.addGrassSeed(new ItemStack(ItemCore.seedCotton), 5);

        //chest
        ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(ItemCore.bookTutorial, 0, 1, 1, 10));
        //ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(ItemCore.bookRecipe, 0, 1, 1, 4));

        for(int i=0;i<ItemIngot.NAMES.length;i++) {
            ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(ItemCore.ingot, i, 2, 6, 8));
        }

        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.flour, 0, 2, 5, 12));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.crystalUnitVillager, 0, 1, 1, 8));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.crystalUnit, 0, 1, 1, 4));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.cloth, 0, 1, 4, 6));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.silk, 0, 1, 4, 6));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.knittingWool, 0, 2, 5, 10));

        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
    }

    public void LoadConfiguration(){
        Configuration cfg=new Configuration(new File("./config/IR3.cfg"));

        cfg.load();
        enableDescription=cfg.getBoolean("EnableDescription", "General", true, "");
        enableDescriptionOfRating=cfg.getBoolean("EnableDescriptionOfRating", "General", true, "");
        machinesExplode=cfg.getBoolean("MachinesExplode", "General", true, "");
        oresAreGenerated =cfg.getBoolean("GenerateOres", "General", true, "");
        generateTinOre=cfg.getBoolean("GenerateTinOre", "General", true, "");
        generateCopperOre=cfg.getBoolean("GenerateCopperOre", "General", true, "");
        generateSilverOre=cfg.getBoolean("GenerateSilverOre", "General", true, "");
        generateManganeseCrust=cfg.getBoolean("GenerateManganeseCrust", "General", true, "");
        generateCobaltCrust=cfg.getBoolean("GenerateCobaltCrust", "General", true, "");
        //enableGenericComposition=cfg.getBoolean("EnableGenericComposition", "General", true, "");
        useOldPipe=cfg.getBoolean("UseOldPipe", "General", false, "");

        cooperatesAMT2=(cfg.getBoolean("CooperatesAMT2", "General", true, "")& Loader.isModLoaded("DCsAppleMilk"));
        cooperatesSS2=(cfg.getBoolean("CooperatesSS2", "General", true, "")&Loader.isModLoaded("SextiarySector"));
        cooperatesIC2=(cfg.getBoolean("CooperatesIC2", "General", true, "")&Loader.isModLoaded("IC2"));
        cooperatesTiC=(cfg.getBoolean("CooperatesTiC", "General", true, "")&Loader.isModLoaded("TConstruct"));
        cooperatesAgri=(cfg.getBoolean("CooperatesAgri", "General", true, "")&Loader.isModLoaded("AgriCraft"));
        cooperatesInsanity=(cfg.getBoolean("CooperatesInsanity", "General", true, "")&Loader.isModLoaded("jp-plusplus-fbs"));
        cooperateMMCAPI=(cfg.getBoolean("CooperatesMMCAPI", "General", true, "")&Loader.isModLoaded("jp.MinecraftModderJapan.ModCooperationAPI"));

        needleId=cfg.get("Entity", "NeedleId", 57).getInt();
        torpedoId=cfg.get("Entity", "TorpedoId", 58).getInt();

        String s=cfg.getString("Woods", "Recipes", "", "");
        if(s.length()>0) {
            String[] w = s.split(",");
            for (int i = 0; i < w.length; i++) {
                Block b = (Block) Block.blockRegistry.getObject(w[i]);
                if (b != null) {
                    Recipes.addWood(b);
                }
            }
        }
        s=cfg.getString("Leaves", "Recipes", "", "");
        if(s.length()>0){
            String[] w=s.split(",");
            for(int i=0;i<w.length;i++){
                Block b=(Block)Block.blockRegistry.getObject(w[i]);
                if(b!=null) {
                    Recipes.addLeave(b);
                }
            }
        }

        s=cfg.getString("Ores", "Recipes", "", "[Usage]BlockName,Metadata");
        if(s.length()>0){
            String[] w=s.split(",");
            for(int i=0;i<w.length/2;i++){
                Block b=(Block)Block.blockRegistry.getObject(w[i*2]);
                if(b!=null) {
                    Recipes.addOre(b, Integer.parseInt(w[i*2+1]));
                }
            }
        }
        s=cfg.getString("Stones", "Recipes", "", "[Usage]BlockName,Metadata");
        if(s.length()>0){
            String[] w=s.split(",");
            for(int i=0;i<w.length/2;i++){
                Block b=(Block)Block.blockRegistry.getObject(w[i*2]);
                if(b!=null) {
                    Recipes.addStone(b, Integer.parseInt(w[i * 2 + 1]));
                }
            }
        }

        s=cfg.getString("DyeingGoods", "Recipes", "", "");
        if(s.length()>0) {
            String[] w = s.split(",");
            for (int i = 0; i < w.length; i++) {
                Block b = (Block) Block.blockRegistry.getObject(w[i]);
                if (b != null) {
                    //Recipes.addWood(b);
                    Recipes.addDying(Item.getItemFromBlock(b));
                }
                else{
                    Item item = (Item)Item.itemRegistry.getObject(w[i]);
                    if(item!=null){
                        Recipes.addDying(item);
                    }
                }
            }
        }

        s=cfg.getString("Mobs", "Recipes", "", "[Usage]ItemName,ItemMetadata,EntityName [Example]rotten_flesh,0,Zombie");
        if(s.length()>0) {
            String[] w = s.split(",");
            int size = w.length / 3;
            for (int i = 0; i < size; i++) {
                Item item = (Item) Item.itemRegistry.getObject(w[i * 3]);
                if (item == null) continue;
                ItemStack material = new ItemStack(item, 1, Integer.parseInt(w[i * 3 + 1]));
                Recipes.addMob(material, w[i * 3 + 2]);
            }
        }

        cfg.save();
    }

    @SubscribeEvent
    public void generateOrePre(OreGenEvent.Pre event){
        if(!oresAreGenerated)   return;

        WorldGenerator genTin = new WorldGenMinable(BlockCore.ore, 0, 8, Blocks.stone);
        WorldGenerator genCopper = new WorldGenMinable(BlockCore.ore, 1, 8, Blocks.stone);
        WorldGenerator genSilver = new WorldGenMinable(BlockCore.ore, 2, 8, Blocks.stone);
        WorldGenerator genManganese=new WorldGenMinable(BlockCore.crust, 0, 16, Blocks.gravel);
        WorldGenerator genCobalt=new WorldGenMinable(BlockCore.crust, 1, 16, Blocks.gravel);

        //tin
        if(generateTinOre && TerrainGen.generateOre(event.world, event.rand, genTin, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<14;i++){
                genTin.generate(event.world, event.rand, event.worldX+event.rand.nextInt(16), 30+event.rand.nextInt(20), event.worldZ+event.rand.nextInt(16));
            }
        }
        //copper
        if(generateCopperOre && TerrainGen.generateOre(event.world, event.rand, genCopper, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<10;i++){
                genCopper.generate(event.world, event.rand, event.worldX+event.rand.nextInt(16), 20+event.rand.nextInt(20), event.worldZ+event.rand.nextInt(16));
            }
        }
        //silver
        if(generateSilverOre && TerrainGen.generateOre(event.world, event.rand, genSilver, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<4;i++){
                genSilver.generate(event.world, event.rand, event.worldX+event.rand.nextInt(16), 1+event.rand.nextInt(29), event.worldZ+event.rand.nextInt(16));
            }
        }

        byte[] bIds=event.world.getChunkFromBlockCoords(event.worldX, event.worldZ).getBiomeArray();
        for(int k=0;k<bIds.length;k++){
            if(bIds[k]==0 || bIds[k]==24){
                //manganese
                if(generateManganeseCrust && TerrainGen.generateOre(event.world, event.rand, genSilver, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
                    for(int i=0;i<7;i++){
                        genManganese.generate(event.world, event.rand, event.worldX+event.rand.nextInt(16), 16+event.rand.nextInt(48), event.worldZ+event.rand.nextInt(16));
                    }
                }
                //cobalt
                if(generateCobaltCrust  && TerrainGen.generateOre(event.world, event.rand, genSilver, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
                    for(int i=0;i<7;i++){
                        genCobalt.generate(event.world, event.rand, event.worldX+event.rand.nextInt(16), 16+event.rand.nextInt(48), event.worldZ+event.rand.nextInt(16));
                    }
                }
                break;
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.loadNEI();
    }

    @EventHandler
    public void receiveIMCE(FMLInterModComms.IMCEvent event){
        IMCEventReceiver.receive(event);
    }
}
