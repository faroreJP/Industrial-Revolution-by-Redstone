package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemReed;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ItemCore {
    public static Item.ToolMaterial materialKnife;
    public static Item.ToolMaterial materialKnifeMP;
    public static Item.ToolMaterial materialKnifeGrip;
    public static Item.ToolMaterial materialRagnarok;
    public static Item.ToolMaterial materialDrill;

    public static Item appleRed;
    public static Item seedCotton;
    public static Item cottonRaw;
    public static Item seedMelonLapis;
    public static Item melonLapis;
    public static Item seedWheatGlow;
    public static Item wheatGlow;
    public static Item potatoQuartz;
    public static Item reedIron;
    public static Item seedRadishTin;
    public static Item radishTin;
    public static Item seedCornCopper;
    public static Item cornCopper;
    public static Item seedOrangeAluminum;
    public static Item orangeAluminum;


    public static Item crust;
    public static Item dust;
    public static Item ingot;
    public static Item conductor;

    public static Item knittingWool;
    public static Item stringCotton;
    public static Item cloth;
    public static Item silk;
    public static Item flour;
    public static Item coil;
    public static Item waterweed;
    public static Item waterproof;
    public static Item screw;
    public static Item bootsRubber;
    public static Item bucketRedstone;
    public static Item leather;

    public static Item gearWood;
    public static Item gearStone;
    public static Item gearIron;

    public static Item primeMoverWood;
    public static Item primeMoverStone;
    public static Item primeMoverIron;

    public static Item crystalUnitVillager;
    public static Item crystalUnit;
    public static Item crystalUnitDouble;
    public static Item crystalUnitAdv;
    public static Item crystalUnitEnder;
    public static Item crystalUnitCreeper;

    public static Item knife;
    public static Item knifeMP;
    public static Item knifeGrip;
    public static Item knifeBlade;
    public static Item ragnarok;
    public static Item injector;
    public static Item needle;

    public static Item multimeter;
    public static Item wrench;
    public static Item hammer;
    public static Item drill;

    public static Item canOxygen;
    public static Item bag;
    public static Item can;
    public static Item canSoilent;
    public static Item canYamatoni;

    public static Item bookRecipe;
    public static Item bookTutorial;

    public static Item foodSmoked;
    public static Item cheese;


    public static void RegisterItems(){
        crust=new ItemCrust();
        dust=new ItemDust();
        ingot=new ItemIngot();
        conductor=new ItemConductor();
        GameRegistry.registerItem(crust, "itemCrust");
        GameRegistry.registerItem(dust, "dust");
        GameRegistry.registerItem(ingot, "ingot");
        GameRegistry.registerItem(conductor, "conductor");

        //materials
        appleRed=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2appleRed").setTextureName(IR2.MODID+":appleRed");
        knittingWool=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2knittingWool").setTextureName(IR2.MODID+":knittingWool");
        stringCotton=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2stringCotton").setTextureName(IR2.MODID+":stringCotton");
        cloth=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2cloth").setTextureName(IR2.MODID+":cloth");
        silk=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2silk").setTextureName(IR2.MODID+":silk");
        flour=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2flour").setTextureName(IR2.MODID+":flour");
        coil=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2coil").setTextureName(IR2.MODID+":coil");
        waterweed=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2waterweed").setTextureName(IR2.MODID+":waterweed");
        waterproof=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2waterproof").setTextureName(IR2.MODID+":waterproof");
        screw=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2screw").setTextureName(IR2.MODID+":screw");
        bootsRubber =new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2bootsRubber").setTextureName(IR2.MODID+":bootsRubber");
        bucketRedstone=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2bucketRedstone").setTextureName(IR2.MODID+":bucketRedstone");
        leather=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2leather").setTextureName(IR2.MODID+":leather");
        GameRegistry.registerItem(appleRed, "appleRed");
        GameRegistry.registerItem(knittingWool, "knittingWool");
        GameRegistry.registerItem(stringCotton, "stringCotton");
        GameRegistry.registerItem(cloth, "cloth");
        GameRegistry.registerItem(silk, "silk");
        GameRegistry.registerItem(flour, "flour");
        GameRegistry.registerItem(coil, "coil");
        GameRegistry.registerItem(waterweed, "waterweed");
        GameRegistry.registerItem(waterproof, "waterproof");
        GameRegistry.registerItem(screw, "screw");
        GameRegistry.registerItem(bootsRubber, "bootsRubber");
        GameRegistry.registerItem(leather, "leather");
        //GameRegistry.registerItem(bucketRedstone, "bucketRedstone");

        seedCotton=new ItemSeedCotton();
        cottonRaw=new Item().setUnlocalizedName("IR2cottonRaw").setTextureName(IR2.MODID+":cottonRaw").setCreativeTab(IR2.tabIR2);
        seedMelonLapis=new ItemSeedMelonLapis();
        melonLapis=new Item().setUnlocalizedName("IR2melonLapis").setTextureName(IR2.MODID+":melonLapis").setCreativeTab(IR2.tabIR2);
        seedWheatGlow=new ItemSeedWheatGlow();
        wheatGlow=new Item().setUnlocalizedName("IR2wheatGlow").setTextureName(IR2.MODID+":wheatGlow").setCreativeTab(IR2.tabIR2);
        reedIron=new ItemReed(BlockCore.cropReedIron).setUnlocalizedName("IR2reedIron").setTextureName(IR2.MODID+":reedIron").setCreativeTab(IR2.tabIR2);
        potatoQuartz=new ItemPotatoQuartz();
        GameRegistry.registerItem(seedCotton, "seedCotton");
        GameRegistry.registerItem(cottonRaw, "cottonRaw");
        GameRegistry.registerItem(seedMelonLapis, "seedMelonLapis");
        GameRegistry.registerItem(melonLapis, "melonLapis");
        GameRegistry.registerItem(seedWheatGlow, "seedWheatGlow");
        GameRegistry.registerItem(wheatGlow, "wheatGlow");
        GameRegistry.registerItem(reedIron, "reedIron");
        GameRegistry.registerItem(potatoQuartz, "potatoQuartz");

        gearWood=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2gearWood").setTextureName(IR2.MODID+":gearWood");
        GameRegistry.registerItem(gearWood, "gearWood");
        gearStone=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2gearStone").setTextureName(IR2.MODID+":gearStone");
        GameRegistry.registerItem(gearStone, "gearStone");
        gearIron=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2gearIron").setTextureName(IR2.MODID+":gearIron");
        GameRegistry.registerItem(gearIron, "gearIron");

        primeMoverWood=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2primeMoverWood").setTextureName(IR2.MODID+":primeMoverWood");
        GameRegistry.registerItem(primeMoverWood, "primeMoverWood");
        primeMoverStone=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2primeMoverStone").setTextureName(IR2.MODID+":primeMoverStone");
        GameRegistry.registerItem(primeMoverStone, "primeMoverStone");
        primeMoverIron=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2primeMoverIron").setTextureName(IR2.MODID+":primeMoverIron");
        GameRegistry.registerItem(primeMoverIron, "primeMoverIron");

        crystalUnitVillager=new ItemCrystalUnit((short)32, (short)32, 60*5).setUnlocalizedName("crystalUnitVillager").setTextureName(IR2.MODID + ":crystalUnitVillager");
        crystalUnit=new ItemCrystalUnit((short)32, (short)32, 60*10).setUnlocalizedName("crystalUnit").setTextureName(IR2.MODID + ":crystalUnit");
        crystalUnitDouble=new ItemCrystalUnit((short)32, (short)64, 60*10).setUnlocalizedName("crystalUnitDouble").setTextureName(IR2.MODID + ":crystalUnitDouble");
        crystalUnitAdv =new ItemCrystalUnit((short)64, (short)128, 60*20).setUnlocalizedName("crystalUnitAdvanced").setTextureName(IR2.MODID + ":crystalUnitAdv");
        crystalUnitEnder =new ItemCrystalUnit((short)128, (short)128, 60*30).setUnlocalizedName("crystalUnitEnder").setTextureName(IR2.MODID + ":crystalUnitEnder");
        crystalUnitCreeper=new ItemCrystalUnit((short)16, (short)16, 60*10).setUnlocalizedName("crystalUnitCreeper").setTextureName(IR2.MODID + ":crystalUnitCreeper");
        GameRegistry.registerItem(crystalUnitVillager, "crystalUnitVillager");
        GameRegistry.registerItem(crystalUnit, "crystalUnit");
        GameRegistry.registerItem(crystalUnitDouble, "crystalUnitDouble");
        GameRegistry.registerItem(crystalUnitAdv, "crystalUnitAdv");
        GameRegistry.registerItem(crystalUnitEnder, "crystalUnitEnder");
        GameRegistry.registerItem(crystalUnitCreeper, "crystalUnitCreeper");

        knife=new ItemKnife(materialKnife);
        knifeMP=new ItemKnifeMP(materialKnifeMP);
        knifeGrip=new ItemGrip(materialKnifeGrip);
        knifeBlade=new Item().setCreativeTab(IR2.tabIR2).setUnlocalizedName("IR2knifeBlade").setTextureName(IR2.MODID+":knifeBlade");
        ragnarok=new ItemRagnarok(materialRagnarok);
        injector=new ItemNeedleInjector();
        needle=new Item().setUnlocalizedName("IR2needle").setTextureName(IR2.MODID+":needle").setCreativeTab(IR2.tabIR2);
        GameRegistry.registerItem(knife, "knife");
        GameRegistry.registerItem(knifeMP, "knifeMP");
        GameRegistry.registerItem(knifeGrip, "knifeGrip");
        GameRegistry.registerItem(knifeBlade, "knifeBlade");
        GameRegistry.registerItem(ragnarok, "ragnarok");
        GameRegistry.registerItem(injector, "injector");
        GameRegistry.registerItem(needle, "needle");


        multimeter=new ItemMultimeter();
        wrench=new ItemWrench();
        hammer=new ItemHammer();
        drill=new ItemDrill(materialDrill);
        GameRegistry.registerItem(multimeter, "multimeter");
        GameRegistry.registerItem(wrench, "wrench");
        GameRegistry.registerItem(hammer, "hammer");
        GameRegistry.registerItem(drill, "drill");

        canOxygen=new ItemOxygen(100);
        bag=new ItemBag();
        can=new ItemCan();
        canSoilent=new ItemCanSoilent();
        canYamatoni=new ItemCanYamatoni();
        GameRegistry.registerItem(canOxygen, "canOxygen");
        GameRegistry.registerItem(bag, "bag");
        GameRegistry.registerItem(can, "can");
        GameRegistry.registerItem(canSoilent, "canSoilent");
        GameRegistry.registerItem(canYamatoni, "canYamatoni");

        bookTutorial=new ItemBookTutorial();
        bookRecipe=new ItemBookRecipe();
        GameRegistry.registerItem(bookTutorial, "bookTutorial");
        GameRegistry.registerItem(bookRecipe, "bookRecipe");

        foodSmoked=new ItemFoodSmoked();
        cheese=new ItemFood(5, 3, false).setUnlocalizedName("IR2cheese").setTextureName(IR2.MODID+":cheese").setCreativeTab(IR2.tabIR2);
        GameRegistry.registerItem(foodSmoked, "foodSmoked");
        GameRegistry.registerItem(cheese, "cheese");
    }
    public static void addToolMaterials(){
        materialKnife=EnumHelper.addToolMaterial(IR2.MODID+":knife", 0, 20*360, 8.0F, 9.0F, 0);
        materialKnifeMP=EnumHelper.addToolMaterial(IR2.MODID+":knifeMP", 0, 20*120, 8.0F, 4.0F, 0);
        materialKnifeGrip=EnumHelper.addToolMaterial(IR2.MODID+":knifeGrip", 0, 64, 1.0F, 0.0F, 0);
        materialRagnarok=EnumHelper.addToolMaterial(IR2.MODID+":ragnarok", 0, 1400, 8.0F, 11.0F, 0);
        materialDrill=EnumHelper.addToolMaterial(IR2.MODID+":drill", 3, 1400, 12.0f, 0, 0);
    }
}
