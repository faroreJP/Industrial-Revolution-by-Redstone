package jp.plusplus.ir2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import jp.plusplus.ir2.api.IR3RecipeAPI;
import jp.plusplus.ir2.blocks.*;
import jp.plusplus.ir2.blocks.BlockOre;
import jp.plusplus.ir2.items.ItemCan;
import jp.plusplus.ir2.items.ItemConductor;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemDrill;
import jp.plusplus.ir2.mod.ForAgriCraft;
import jp.plusplus.ir2.mod.ForInsanity;
import jp.plusplus.ir2.mod.ForSS2;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.ProductItem;
import shift.mceconomy2.api.shop.ProductList;

import javax.annotation.Nullable;
import java.lang.Float;
import java.util.*;

/**
 * Created by plusplus_F on 2015/01/31.
 * レシピとかいろいろ。
 * 加工レシピの追加はここのstaticなメソッドでどうぞ。
 * ただ、いろいろ注意書きがあるんでよろすく。
 *
 *
*/
public class Recipes {
    private static Recipes instance=new Recipes();;

    private HashMap<ItemStack, Float> expTable=new HashMap<ItemStack, Float>();

    private ArrayList<RecipeItemStack> recipeAlloying=new ArrayList<RecipeItemStack>();
    private ArrayList<RecipeItemStack.CrushingRecipeItemStack> recipeCrushing=new ArrayList<RecipeItemStack.CrushingRecipeItemStack>();
    private ArrayList<RecipeItemStack.ExtractingRecipeItemStack> recipeExtracting=new ArrayList<RecipeItemStack.ExtractingRecipeItemStack>();
    private ArrayList<RecipeItemStack> recipeWeaving=new ArrayList<RecipeItemStack>();
    private ArrayList<RecipeItemStack> recipeSpinning=new ArrayList<RecipeItemStack>();
    private ArrayList<RecipeItemStack> recipeSmoking=new ArrayList<RecipeItemStack>();
    private HashMap<Object[], Object> recipeMixing=new HashMap<Object[], Object>();
    private LinkedList<Block> woods=new LinkedList<Block>();
    private LinkedList<Block> leaves=new LinkedList<Block>();
    private LinkedList<Tuple> harvestTargets=new LinkedList<Tuple>();
    private LinkedList<Tuple> ores=new LinkedList<Tuple>();
    private LinkedList<Tuple> stones=new LinkedList<Tuple>();
    private HashMap<ItemStack, String> mobs=new HashMap<ItemStack, String>();
    private LinkedList<Item> recipeDying=new LinkedList<Item>();
    private HashMap<Integer, ItemStack> unifierItems=new HashMap<Integer, ItemStack>();

    private int additionalCrushCount;
    private int additionalFishingCount;
    private int additionalBuildingCount;

    private FishableTable itemFishing=new FishableTable();
    private FishableTable itemFishingSea=new FishableTable();

    private static int uniqueForBuilding=6;
    private ArrayList<BuildingPair> buildingItems=new ArrayList<BuildingPair>();
    private HashMap<ItemStack, Integer> buildingEncahntItems=new LinkedHashMap<ItemStack, Integer>();
    private HashMap<Integer, String> buildingTableNames=new HashMap<Integer, String>();

    private  Random random=new Random();

    private Object shopList;
    private int shopId;

    // このへんでIR2のレシピを登録してる。よそからは絶対に呼ぶな。
    public static void RegisterRecipes(){
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_OTHER, "other");
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_PLANT, "plant");
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_TOOL, "tool");
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_FOOD, "food");
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_ORE, "ore");
        instance.buildingTableNames.put(IR3RecipeAPI.COMPOSITION_MAGIC, "magic");

        // ###########################################################################
        //                            クラフト
        // ###########################################################################
        //
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.piston, "www","cmc","crc", 'w',"plankWood", 'm',"ingotCopper", 'c',Blocks.cobblestone, 'r',Items.redstone));
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.piston, "www","cmc","crc", 'w',"plankWood", 'm',"ingotAluminium", 'c',Blocks.cobblestone, 'r',Items.redstone));

        //
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian), new ItemStack(Items.lava_bucket), new ItemStack(Items.water_bucket));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian), new ItemStack(ItemCore.can, 1, FluidRegistry.LAVA.getID()+1), new ItemStack(ItemCore.can, 1, FluidRegistry.LAVA.getID()+1), new ItemStack(ItemCore.can, 1, FluidRegistry.WATER.getID()+1), new ItemStack(ItemCore.can, 1, FluidRegistry.WATER.getID()+1));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 4), new ItemStack(BlockCore.logRed));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 4, 2), new ItemStack(BlockCore.logGold));
        GameRegistry.addRecipe(new ItemStack(Blocks.wool), "**","**", '*',ItemCore.cottonRaw);

        //blocks
        for(int i=0;i< BlockOre.NAMES.length;i++) {
            GameRegistry.addRecipe(new ItemStack(BlockCore.block, 1, i), "###", "###", "###", '#', new ItemStack(ItemCore.ingot, 1, i));
            GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.ingot, 9, i), new ItemStack(BlockCore.block, 1, i));
        }

        //materials
        GameRegistry.addRecipe(new ItemStack(BlockCore.blockMelonLapis),    "###","###","###", '#',ItemCore.melonLapis);
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.melonLapis, 9), new ItemStack(BlockCore.blockMelonLapis));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.seedMelonLapis), new ItemStack(ItemCore.melonLapis));

        GameRegistry.addRecipe(new ItemStack(ItemCore.gearWood, 2),    " # ","# #"," # ", '#',Blocks.planks);
        GameRegistry.addRecipe(new ItemStack(ItemCore.gearStone, 2),   " # ","# #"," # ", '#',Blocks.cobblestone);
        GameRegistry.addRecipe(new ItemStack(ItemCore.gearIron, 2),    " # ","# #"," # ", '#',Items.iron_ingot);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.primeMoverWood),  "wkw","pgp","ccc", 'w', Blocks.planks,      'k',"ir2.string", 'p',Blocks.piston, 'g',"gearWood", 'c',BlockCore.cableTin));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.primeMoverStone), "wkw","pgp","ccc", 'w', Blocks.cobblestone, 'k',"ir2.string", 'p',Blocks.piston, 'g',"gearStone", 'c',BlockCore.cableCopper));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.primeMoverIron),  "wkw","pgp","ccc", 'w', Items.iron_ingot,   'k',"ir2.string", 'p',Blocks.piston, 'g',"gearIron", 'c',BlockCore.cableGold));

        GameRegistry.addRecipe(new ItemStack(ItemCore.crystalUnit),     "rrr","rqr","rrr", 'r',Items.redstone, 'q',Items.quartz);
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.crystalUnitDouble), ItemCore.crystalUnit, ItemCore.crystalUnit);
        GameRegistry.addRecipe(new ItemStack(ItemCore.crystalUnitAdv),  "lcl","cjc","lcl", 'l',new ItemStack(Items.dye, 1, 4), 'c',ItemCore.crystalUnit, 'j',Items.diamond);
        GameRegistry.addRecipe(new ItemStack(ItemCore.crystalUnitAdv),  "lcl","cjc","lcl", 'l',new ItemStack(Items.dye, 1, 4), 'c',ItemCore.crystalUnit, 'j',Items.emerald);
        GameRegistry.addRecipe(new ItemStack(ItemCore.crystalUnitEnder),  "r+r","*e*","r+r", 'r',Blocks.redstone_block, 'e',Items.ender_pearl, '+',Items.diamond, '*',Items.emerald);
        GameRegistry.addRecipe(new ItemStack(ItemCore.crystalUnitEnder),  "r+r","*e*","r+r", 'r',Blocks.redstone_block, 'e',Items.ender_pearl, '*',Items.diamond, '+',Items.emerald);

        GameRegistry.addRecipe(new ItemStack(BlockCore.casingStone, 2),    "###","# #","***", '#',Blocks.cobblestone, '*',Blocks.stone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.casingObsidian, 2), "###","# #","***", '#',Blocks.obsidian, '*',Blocks.quartz_block);

        GameRegistry.addRecipe(new ItemStack(ItemCore.coil, 2),       "###","#*#","###", '#',new ItemStack(ItemCore.conductor, 1, 1), '*',Items.iron_ingot);
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemCore.leather, new ItemStack(Items.slime_ball), "ir2.cloth"));

        //weapons
        GameRegistry.addRecipe(new ItemStack(ItemCore.knife),       "#-","#-","cl", '#',new ItemStack(ItemCore.conductor, 1, 2), '-',BlockCore.cableSilver, 'c',ItemCore.crystalUnitAdv, 'l',Items.leather);
        GameRegistry.addRecipe(new ItemStack(ItemCore.knifeGrip),   " -"," -","cl", '-',BlockCore.cableSilver, 'c',ItemCore.crystalUnitDouble, 'l',Items.leather);
        GameRegistry.addRecipe(new ItemStack(ItemCore.knifeBlade),  "#","#", '#',new ItemStack(ItemCore.conductor, 1, 2));
        GameRegistry.addRecipe(new ItemStack(ItemCore.injector),    "cc ","cci","  i", 'c',new ItemStack(ItemCore.can, 1, 0), 'i',Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(ItemCore.needle, 4),   "  i"," c ", 'i',Items.iron_ingot, 'c',new ItemStack(ItemCore.conductor, 1, 2));

        //utilities
        GameRegistry.addRecipe(new ItemStack(ItemCore.multimeter), " # ","#*#","-#-", '#',Blocks.cobblestone, '*',new ItemStack(ItemCore.conductor, 1, 4), '-',BlockCore.cableGold);
        GameRegistry.addRecipe(new ItemStack(ItemCore.wrench),     " # "," ##","#  ", '#',Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(ItemCore.hammer),     "  #"," | ","|  ", '#',Blocks.iron_block, '|',Items.stick);
        GameRegistry.addRecipe(ItemDrill.getDrill(ItemCore.drill, 0), " gi"," pg","g  ", 'i',new ItemStack(ItemCore.conductor, 2), 'p',ItemCore.primeMoverIron, 'g',Items.emerald);
        GameRegistry.addRecipe(ItemDrill.getDrill(ItemCore.drill, 1), " gi"," pg","g  ", 'i',new ItemStack(ItemCore.conductor, 2), 'p',ItemCore.primeMoverIron, 'g',Items.diamond);
        GameRegistry.addRecipe(new ItemStack(BlockCore.unifier),   "b","c", 'b',Items.book, 'c',BlockCore.casingStone);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.can, 2),  "#"," ","#",    '#',"ingotTin"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.can, 4),  "#"," ","#",    '#',"ingotAluminium"));

        GameRegistry.addRecipe(new ItemStack(ItemCore.canSoilent),  "***","*#*","***",    '#',new ItemStack(ItemCore.can), '*',new ItemStack(Items.rotten_flesh));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.canOxygen), new ItemStack(ItemCore.can), new ItemStack(ItemCore.waterweed));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.canYamatoni), new ItemStack(ItemCore.can), new ItemStack(Items.beef));

        //GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.bookRecipe), new ItemStack(Items.book), new ItemStack(ItemCore.crystalUnit));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.bookTutorial), new ItemStack(Items.book), new ItemStack(Items.redstone), new ItemStack(Items.redstone));
        GameRegistry.addRecipe(new ItemStack(ItemCore.bag), "###","# #","###", '#',ItemCore.cloth);

        //decorations
        GameRegistry.addRecipe(new ItemStack(BlockCore.fan), "|","*","#", '|',Items.stick, '*',ItemCore.gearWood, '#',Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(BlockCore.lightCeiling, 4, 0), " + "," # ","   ", '+',Blocks.glass, '#',Blocks.glowstone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.lightCeiling, 4, 1), "   "," # "," + ", '+',Blocks.glass, '#',Blocks.glowstone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.lightCeiling, 4, 2), "   "," #+","   ", '+',Blocks.glass, '#',Blocks.glowstone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.lightCeiling, 4, 3), "   ","+# ","   ", '+',Blocks.glass, '#',Blocks.glowstone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.deco, 1, 0), "###","#+#","###", '#',Blocks.cobblestone, '+',BlockCore.fan);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.deco, 16, 1), "#+#","+ +","#+#", '#',Items.iron_ingot, '+',"ingotTin"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.deco, 16, 1), "+#+","# #","+#+", '#',Items.iron_ingot, '+',"ingotTin"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.deco, 16, 1), "#+#","+ +","#+#", '#',Items.iron_ingot, '+',"ingotZinc"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.deco, 16, 1), "+#+","# #","+#+", '#',Items.iron_ingot, '+',"ingotZinc"));

        //cables
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableTin, 6, 15),        "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableTin, 6, 15),        "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableCopper, 6, 15),     "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableCopper, 6, 15),     "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableIron, 6, 15),       "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableIron, 6, 15),       "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableSilver, 6, 15),     "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableSilver, 6, 15),     "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableGold, 6, 15),       "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableGold, 6, 15),       "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableAluminium, 6, 15),  "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableAluminium, 6, 15),  "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableNickel, 6, 15),     "#-#","***","-#-", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.cableNickel, 6, 15),     "-#-","***","#-#", '#',"ir2.leather", '-',"ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableTin, 1, i), new ItemStack(BlockCore.cableTin, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableCopper, 1, i), new ItemStack(BlockCore.cableCopper, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableIron, 1, i), new ItemStack(BlockCore.cableIron, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableSilver, 1, i), new ItemStack(BlockCore.cableSilver, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableGold, 1, i), new ItemStack(BlockCore.cableGold, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableAluminium, 1, i), new ItemStack(BlockCore.cableAluminium, 1, 15), new ItemStack(Items.dye, 1, i));
        for(int i=0;i<16;i++)   GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.cableNickel, 1, i), new ItemStack(BlockCore.cableNickel, 1, 15), new ItemStack(Items.dye, 1, i));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeTin, 6),         "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeCopper, 6),      "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeIron, 6),        "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeSilver, 6),      "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGold, 6),        "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGold, 6),        "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeAluminium, 6),   "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeNickel, 6),      "***","---","***", '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeTinExtractor, 6),         "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeCopperExtractor, 6),      "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeIronExtractor, 6),        "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeSilverExtractor, 6),      "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGoldExtractor, 6),        "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeAluminiumExtractor, 6),   "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeNickelExtractor, 6),      "***","-#-","***", '#', Items.quartz, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeTinOneWay, 6),         "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeCopperOneWay, 6),      "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeIronOneWay, 6),        "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeSilverOneWay, 6),      "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGoldOneWay, 6),        "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeAluminiumOneWay, 6),   "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeNickelOneWay, 6),      "***","-#-","***", '#', Items.emerald, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeTinSorting, 6),         "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeCopperSorting, 6),      "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeIronSorting, 6),        "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeSilverSorting, 6),      "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGoldSorting, 6),        "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeAluminiumSorting, 6),   "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeNickelSorting, 6),      "***","-#-","***", '#', Items.diamond, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeTinVoid, 6),         "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeCopperVoid, 6),      "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeIronVoid, 6),        "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeSilverVoid, 6),      "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeGoldVoid, 6),        "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeAluminiumVoid, 6),   "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 7)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.pipeNickelVoid, 6),      "***","-#-","***", '#', Blocks.obsidian, '-', "ir2.string", '*',new ItemStack(ItemCore.conductor, 1, 9)));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeTinFluid, 3),                   new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeTin), new ItemStack(BlockCore.pipeTin), new ItemStack(BlockCore.pipeTin));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeCopperFluid, 3),                new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeCopper), new ItemStack(BlockCore.pipeCopper), new ItemStack(BlockCore.pipeCopper));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeIronFluid, 3),                  new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeIron), new ItemStack(BlockCore.pipeIron), new ItemStack(BlockCore.pipeIron));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeSilverFluid, 3),                new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeSilver), new ItemStack(BlockCore.pipeSilver), new ItemStack(BlockCore.pipeSilver));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeGoldFluid, 3),                  new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeGold), new ItemStack(BlockCore.pipeGold), new ItemStack(BlockCore.pipeGold));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeAluminiumFluid, 3),             new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeAluminium), new ItemStack(BlockCore.pipeAluminium), new ItemStack(BlockCore.pipeAluminium));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeNickelFluid, 3),                new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeNickel), new ItemStack(BlockCore.pipeNickel), new ItemStack(BlockCore.pipeNickel));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeTinFluidExtractor, 3),          new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeTinExtractor), new ItemStack(BlockCore.pipeTinExtractor), new ItemStack(BlockCore.pipeTinExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeCopperFluidExtractor, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeCopperExtractor), new ItemStack(BlockCore.pipeCopperExtractor), new ItemStack(BlockCore.pipeCopperExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeIronFluidExtractor, 3),         new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeIronExtractor), new ItemStack(BlockCore.pipeIronExtractor), new ItemStack(BlockCore.pipeIronExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeSilverFluidExtractor, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeSilverExtractor), new ItemStack(BlockCore.pipeSilverExtractor), new ItemStack(BlockCore.pipeSilverExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeGoldFluidExtractor, 3),         new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeGoldExtractor), new ItemStack(BlockCore.pipeGoldExtractor), new ItemStack(BlockCore.pipeGoldExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeAluminiumFluidExtractor, 3),    new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeAluminiumExtractor), new ItemStack(BlockCore.pipeAluminiumExtractor), new ItemStack(BlockCore.pipeAluminiumExtractor));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeNickelFluidExtractor, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeNickelExtractor), new ItemStack(BlockCore.pipeNickelExtractor), new ItemStack(BlockCore.pipeNickelExtractor));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeTinFluidVoid, 3),          new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeTinVoid), new ItemStack(BlockCore.pipeTinVoid), new ItemStack(BlockCore.pipeTinVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeCopperFluidVoid, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeCopperVoid), new ItemStack(BlockCore.pipeCopperVoid), new ItemStack(BlockCore.pipeCopperVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeIronFluidVoid, 3),         new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeIronVoid), new ItemStack(BlockCore.pipeIronVoid), new ItemStack(BlockCore.pipeIronVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeSilverFluidVoid, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeSilverVoid), new ItemStack(BlockCore.pipeSilverVoid), new ItemStack(BlockCore.pipeSilverVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeGoldFluidVoid, 3),         new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeGoldVoid), new ItemStack(BlockCore.pipeGoldVoid), new ItemStack(BlockCore.pipeGoldVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeAluminiumFluidVoid, 3),    new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeAluminiumVoid), new ItemStack(BlockCore.pipeAluminiumVoid), new ItemStack(BlockCore.pipeAluminiumVoid));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockCore.pipeNickelFluidVoid, 3),       new ItemStack(ItemCore.waterproof), new ItemStack(BlockCore.pipeNickelVoid), new ItemStack(BlockCore.pipeNickelVoid), new ItemStack(BlockCore.pipeNickelVoid));

        //generators
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVLF),   "rrr","tcp","rrr", 'r',Items.redstone, 't',Blocks.redstone_torch, 'c',BlockCore.casingStone, 'p',Items.repeater);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVLF),   "rtr","rcr","rpr", 'r',Items.redstone, 't',Blocks.redstone_torch, 'c',BlockCore.casingStone, 'p',Items.repeater);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVLF),   "rpr","rcr","rtr", 'r',Items.redstone, 't',Blocks.redstone_torch, 'c',BlockCore.casingStone, 'p',Items.repeater);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVLF),   "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableTin, '#', BlockCore.casingStone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorLF),    "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableCopper, '#', BlockCore.casingStone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generator),      "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableIron, '#', BlockCore.casingStone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorHV),    "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableSilver, '#', BlockCore.casingStone);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorHF),    "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableGold, '#', BlockCore.casingObsidian);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVHF),   "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableNickel, '#', BlockCore.casingObsidian);
        GameRegistry.addRecipe(new ItemStack(BlockCore.generatorVHV),   "r-r","-#-","r-r", 'r', Items.redstone, '-',BlockCore.cableAluminium, '#', BlockCore.casingObsidian);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.amplifier),      "*+*","-#-","*-*", '-',BlockCore.cableAluminium, '#',BlockCore.casingStone, '*',"ingotSilicon", '+',Items.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.amplifier),      "+++","-#-","*-*", '-',BlockCore.cableAluminium, '#',BlockCore.casingStone, '*',"ingotSilicon", '+',Items.emerald));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.modulator),      "*+*","-#-","*-*", '-',BlockCore.cableNickel, '#',BlockCore.casingObsidian, '*',"ingotSilicon", '+',Items.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.modulator),      "+++","-#-","*-*", '-',BlockCore.cableNickel, '#',BlockCore.casingObsidian, '*',"ingotSilicon", '+',Items.emerald));
        GameRegistry.addRecipe(new ItemStack(BlockCore.relay),          " * ","-#-", '-',BlockCore.cableGold, '#',BlockCore.casingStone, '*',ItemCore.coil);
        GameRegistry.addRecipe(new ItemStack(BlockCore.regulator),      "#-#","+**","#-#", '-',BlockCore.cableGold, '#',Blocks.stonebrick, '*',ItemCore.coil, '+',Items.repeater);

        //machines
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSpinningProt),   " * ","*#*"," p ", '*', "gearWood", '#', BlockCore.casingStone, 'p',Blocks.piston));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFurnaceProt),    "sss","s#s","*p*", '*', "gearWood", '#', BlockCore.casingStone, 'p',Blocks.piston, 's',Blocks.cobblestone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineShop),           " c ","*#*"," p ", '*', "gearWood", '#', BlockCore.casingStone, 'p',Blocks.piston, 'c',new ItemStack(ItemCore.can)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSpinning),     " * ","*#*"," p ", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFurnace),      "sss","s#s","*p*", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, 's',Blocks.cobblestone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineAlloySmelter), "r*r","*#*","p-p", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, 'r',Blocks.redstone_block, '-',BlockCore.cableIron));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineCrusher),      "d*d","*#*","p-p", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, '-',BlockCore.cableIron, 'd',Items.diamond, 'f',Items.flint));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineLoom),         "***","*#*","p-p", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, '-',BlockCore.cableIron));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineDyer),         "*c*","$#&","*p*", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, 'c',Items.cauldron, '$', new ItemStack(Items.dye, 1, 1), '&', new ItemStack(Items.dye, 1, 11)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineAutoCrafter),  "*w*","*#*","p-p", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, '-',BlockCore.cableIron, 'w',Blocks.crafting_table));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFountain),     "btb","b#b","*p*", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 't',BlockCore.tankSmall, 'b',Items.water_bucket));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineCrucible),     "btb","b#b","*p*", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 't',BlockCore.tankSmall, 'b',Items.lava_bucket));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSyntheticFurnace),     "q-g","e#d","*p*", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'q',Blocks.quartz_block,'g',Blocks.gold_block, 'e',Blocks.emerald_block, 'd',Blocks.diamond_block, '-',BlockCore.cableGold));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineMixer),        "t*t","*#*","p-p", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, 't',BlockCore.tankSmall, '-',BlockCore.cableIron));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineCheeseMaker),  "www","*#*","-p-", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, '-',BlockCore.cableIron, 'w',new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSmoker),       "www","*#*","-p-", '*', "gearStone", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverStone, '-',BlockCore.cableIron, 'w',Blocks.brick_block));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSpinningAdv),     " * ","*#*"," p ", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFurnaceAdv),      "sss","s#s","*p*", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 's',Blocks.obsidian));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineAlloySmelterAdv), "r*r","*#*","p-p", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'r',Blocks.redstone_block, '-',BlockCore.cableGold));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineCrusherAdv),         "d*d","*#*","p-p", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, '-',BlockCore.cableGold, 'd',Items.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineExtractor),       "d*d","*#*","p-p", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, '-',BlockCore.cableGold, 'd',Items.ender_pearl));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineTransmitter),     "d*d","*#*","p-p", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, '-',BlockCore.cableNickel, 'd',Items.ender_eye));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineChunkLoader),     "dcd","*#*","p-p", '*', "gearIron", '#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, '-',BlockCore.cableNickel, 'd',Items.diamond, 'c',Items.clock));

        GameRegistry.addRecipe(new ItemStack(BlockCore.machineTransmitterWithLoader),     "n n","tnc","n n", 'n', ItemCore.screw, 't', BlockCore.machineTransmitter, 'c',BlockCore.machineChunkLoader);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineTransmitterWithLoader),     "ntn"," n ","ncn", 'n', ItemCore.screw, 't', BlockCore.machineTransmitter, 'c',BlockCore.machineChunkLoader);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFisher),          "*f*","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'f',Items.fishing_rod));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFeeder),          "*g*","b#b"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'g',Items.golden_apple, 'b',Items.bowl));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineShearer),         "*s*","s#s"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 's',Items.shears));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineWoodcutter),      " a ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'a',Items.iron_axe));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSower),           " b ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'b',Items.bow));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineHarvester),       " h ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'h',Items.iron_hoe));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSmasher),         " a ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'a',Items.iron_pickaxe));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineAutoSpawner),     "xyz","*#*"," p ", '*',"gearIron",'#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'x',Items.diamond, 'y',Items.ender_eye, 'z',Items.emerald));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineMiner),           " a ","*#*"," p ", '*',"gearIron",'#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'a',Items.diamond_pickaxe));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineButcher)  ,       " s ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 's',Items.iron_sword));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineItemCollector)  , " b ","*#*"," p ", '*',"gearIron",'#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'b',Items.bowl));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineLoader),          " * ","h#*"," p ", '*',"gearIron",'#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'h',Blocks.hopper));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineUnloader),        " h ","*#*"," p ", '*',"gearIron",'#', BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'h',Blocks.hopper));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machinePump),            " a ","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'a',BlockCore.tankSmall));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineChildSorter),     "www","*#*"," p ", '*',"gearWood",'#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 'w',Items.wheat));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineMilking),         "btb","b#b","*p*", '*', "gearWood", '#', BlockCore.casingStone, 'p',ItemCore.primeMoverWood, 't',BlockCore.tankSmall, 'b',Items.milk_bucket));

        //upgrade
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineSpinning), " n ", " m ", " p ", 'n',ItemCore.screw, 'p',ItemCore.primeMoverStone, 'm', BlockCore.machineSpinningProt);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineFurnace), " n ", " m ", " p ", 'n',ItemCore.screw, 'p',ItemCore.primeMoverStone, 'm', BlockCore.machineFurnaceProt);

        GameRegistry.addRecipe(new ItemStack(BlockCore.machineSpinningAdv), " # ", "nmn", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineSpinning);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineFurnaceAdv), " # ", "nmn", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineFurnace);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineAlloySmelterAdv), " # ", "nmn", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineAlloySmelter);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineCrusherAdv), " # ", "nmn", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineCrusher);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFisherAdv),       " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineFisher));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineFeederAdv),       " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineFeeder));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineShearerAdv),      " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineShearer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineWoodcutterAdv),   " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineWoodcutter));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineSowerAdv),        " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineSower));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineHarvesterAdv),    " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineHarvester));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.machineMilkingAdv),      " # ", "nm*", " p ", 'n',ItemCore.screw, '#',BlockCore.casingObsidian, '*',"gearIron", 'p',ItemCore.primeMoverIron, 'm', BlockCore.machineMilking));

        GameRegistry.addRecipe(new ItemStack(BlockCore.machineFurnaceVar), "nnn", "nmn", "nnn", 'n',ItemCore.screw, 'm', BlockCore.machineFurnaceAdv);
        GameRegistry.addRecipe(new ItemStack(BlockCore.machineCrusherVar), "nnn", "nmn", "nnn", 'n',ItemCore.screw, 'm', BlockCore.machineCrusherAdv);

        //tanks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.tankSmall), "###","o o","###", '#',"ingotTin", 'o',Blocks.glass));
        GameRegistry.addRecipe(new ItemStack(BlockCore.tankMedium), "###","o o","###", '#',new ItemStack(Items.iron_ingot), 'o',Blocks.glass);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.tankLarge), "###","o o","###", '#',"ingotAluminium", 'o',Blocks.glass));


        //bricks
        GameRegistry.addRecipe(new ItemStack(BlockCore.brickRusty), " b ","bsb"," b ", 's',Blocks.sandstone, 'b',Items.brick);
        GameRegistry.addRecipe(new ItemStack(BlockCore.alloySmelterRustyIdle), "bbb","b b","bbb", 'b',BlockCore.brickRusty);


        // ###########################################################################
        //                            製錬
        // ###########################################################################

        //smelting
        GameRegistry.addSmelting(BlockCore.logRed, new ItemStack(Items.coal, 1, 1), 0.15f);
        GameRegistry.addSmelting(BlockCore.logGold, new ItemStack(Items.coal, 1, 1), 0.15f);

        GameRegistry.addSmelting(new ItemStack(BlockCore.ore, 1, 0), new ItemStack(ItemCore.ingot, 1, 0), 0.5f);
        GameRegistry.addSmelting(new ItemStack(BlockCore.ore, 1, 1), new ItemStack(ItemCore.ingot, 1, 1), 0.6f);
        GameRegistry.addSmelting(new ItemStack(BlockCore.ore, 1, 2), new ItemStack(ItemCore.ingot, 1, 2), 0.85f);

        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 0), new ItemStack(ItemCore.ingot, 1, 0), 0.5f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 1), new ItemStack(ItemCore.ingot, 1, 1), 0.6f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 2), new ItemStack(Items.iron_ingot), 0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 3), new ItemStack(ItemCore.ingot, 1, 2), 0.85f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 4), new ItemStack(Items.gold_ingot), 1.0f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 5), new ItemStack(ItemCore.ingot, 1, 3), 0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 6), new ItemStack(ItemCore.ingot, 1, 4), 0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 7), new ItemStack(ItemCore.ingot, 1, 5), 0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 8), new ItemStack(ItemCore.ingot, 1, 6), 0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemCore.dust, 1, 9), new ItemStack(ItemCore.ingot, 1, 7), 0.7f);

        GameRegistry.addSmelting(ItemCore.flour, new ItemStack(Items.bread), 0.35f);


        // ###########################################################################
        //                            紡績
        // ###########################################################################
        addSpinning(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ItemCore.knittingWool));
        addSpinning(new ItemStack(ItemCore.cottonRaw, 2), new ItemStack(ItemCore.stringCotton));


        // ###########################################################################
        //                            合金
        // ###########################################################################
        addAlloying(new RecipeItemStack("ingotTin", new ItemStack(ItemCore.conductor, 1, 0)));
        addAlloying(new RecipeItemStack("ingotCopper", new ItemStack(ItemCore.conductor, 1,  1)));
        addAlloying(new RecipeItemStack(new ItemStack(Items.iron_ingot),     new ItemStack(ItemCore.conductor, 1,  2)));
        addAlloying(new RecipeItemStack("ingotSilver", new ItemStack(ItemCore.conductor, 1,  3)));
        addAlloying(new RecipeItemStack(new ItemStack(Items.gold_ingot),     new ItemStack(ItemCore.conductor, 1,  4)));
        addAlloying(new RecipeItemStack("ingotManganese", new ItemStack(ItemCore.conductor, 1,  5)));
        addAlloying(new RecipeItemStack("ingotCobalt", new ItemStack(ItemCore.conductor, 1,  6)));
        addAlloying(new RecipeItemStack("ingotAluminium", new ItemStack(ItemCore.conductor, 1,  7)));
        addAlloying(new RecipeItemStack("ingotSilicon", new ItemStack(ItemCore.conductor, 1,  8)));
        addAlloying(new RecipeItemStack("ingotNickel", new ItemStack(ItemCore.conductor, 1,  9)));


        // ###########################################################################
        //                            破砕
        // ###########################################################################
        addCrushing(new ItemStack(Blocks.stone), new Object[]{1.0f, new ItemStack(Blocks.gravel)});
        addCrushing(new ItemStack(Blocks.cobblestone), new Object[]{1.0f, new ItemStack(Blocks.gravel)});
        addCrushing(new ItemStack(Blocks.gravel), new Object[]{1.0f, new ItemStack(Blocks.sand), 1.0f, new ItemStack(Items.flint), 0.25f, new ItemStack(Items.flint)});

        addCrushing(new ItemStack(Blocks.redstone_ore), new Object[]{1.0f, new ItemStack(Items.redstone, 10), 0.8f, new ItemStack(Items.redstone, 1)});
        addCrushing(new ItemStack(Blocks.lapis_ore), new Object[]{1.0f, new ItemStack(Items.dye, 8, 4), 0.6f, new ItemStack(Items.dye, 1, 4)});
        addCrushing(new ItemStack(Blocks.coal_ore), new Object[]{1.0f, new ItemStack(Items.coal), 0.5f, new ItemStack(Items.coal), 0.5f, new ItemStack(Items.coal)});
        addCrushing("gemCoal", new Object[]{1.0f, new ItemStack(ItemCore.dust, 1, 12)});

        addCrushing(new ItemStack(Blocks.waterlily), new Object[]{1.0f, new ItemStack(ItemCore.waterproof)});
        addCrushing(new ItemStack(Blocks.cactus), new Object[]{1.0f, new ItemStack(ItemCore.waterproof)});
        addCrushing(new ItemStack(ItemCore.waterweed), new Object[]{1.0f, new ItemStack(ItemCore.waterproof)});

        addCrushing(new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE), new Object[]{1.f, new ItemStack(ItemCore.dust, 4, 11)});
        addCrushing(new ItemStack(Blocks.log2, 1, OreDictionary.WILDCARD_VALUE), new Object[]{1.f, new ItemStack(ItemCore.dust, 4, 11)});
        addCrushing(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE), new Object[]{1.f, new ItemStack(ItemCore.dust, 1, 11)});

        addCrushing(new ItemStack(BlockCore.crust, 1, 0), new Object[]{1.0f, new ItemStack(ItemCore.crust, 1, 0), 0.5f, new ItemStack(ItemCore.crust, 1, 0), 0.5f, new ItemStack(ItemCore.crust, 1, 0)});
        addCrushing(new ItemStack(BlockCore.crust, 1, 1), new Object[]{1.0f, new ItemStack(ItemCore.crust, 1, 1), 0.5f, new ItemStack(ItemCore.crust, 1, 1), 0.5f, new ItemStack(ItemCore.crust, 1, 1)});
        addCrushing(new ItemStack(ItemCore.crust, 1, 0), new Object[]{1.0f, new ItemStack(ItemCore.dust, 1, 5), 1.0f, new ItemStack(ItemCore.dust, 1, 8), 1.0f, new ItemStack(ItemCore.dust, 1, 7)});
        addCrushing(new ItemStack(ItemCore.crust, 1, 1), new Object[]{1.0f, new ItemStack(ItemCore.dust, 1, 6), 1.0f, new ItemStack(ItemCore.dust, 1, 9), 1.0f, new ItemStack(ItemCore.dust, 1, 1)});

        addCrushing(new ItemStack(Items.bone), new Object[]{1.0f, new ItemStack(Items.dye, 5, 15)});
        addCrushing(new ItemStack(Items.blaze_rod), new Object[]{1.0f, new ItemStack(Items.blaze_powder, 5)});
        addCrushing(new ItemStack(Items.wheat), new Object[]{1.0f, new ItemStack(ItemCore.flour, 1)});
        addCrushing(new ItemStack(ItemCore.wheatGlow), new Object[]{1.0f,new ItemStack(ItemCore.flour), 0.5f, new ItemStack(Items.glowstone_dust)});
        addCrushing(new ItemStack(ItemCore.melonLapis), new Object[]{0.20f, new ItemStack(Items.dye, 1, 4)});
        addCrushing(new ItemStack(ItemCore.potatoQuartz), new Object[]{0.5f, new ItemStack(Items.quartz)});
        addCrushing(new ItemStack(ItemCore.reedIron), new Object[]{0.5f, new ItemStack(ItemCore.dust, 1, 2)});
        addCrushing(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), new Object[]{1.0f, new ItemStack(Items.string, 4)});

        RegisterCrushingItems();


        // ###########################################################################
        //                            抽出
        // ###########################################################################
        addExtracting(new ItemStack(ItemCore.appleRed), 9);
        addExtracting(new ItemStack(BlockCore.saplingRed), 3);
        addExtracting(new ItemStack(BlockCore.logRed), 1);
        addExtracting(new ItemStack(BlockCore.leaveRed), 1);
        addExtracting(new ItemStack(Blocks.redstone_ore), 10);
        addExtracting(new ItemStack(Blocks.redstone_block), 9);
        addExtracting(new ItemStack(Blocks.redstone_lamp), 4);
        addExtracting(new ItemStack(Items.repeater), 3);
        addExtracting(new ItemStack(Blocks.redstone_torch), 1);
        addExtracting(new ItemStack(Items.comparator), 3);
        addExtracting(new ItemStack(Blocks.noteblock), 1);
        addExtracting(new ItemStack(Blocks.dropper), 1);
        addExtracting(new ItemStack(Blocks.dispenser), 1);
        addExtracting(new ItemStack(Blocks.piston), 1);
        addExtracting(new ItemStack(Blocks.sticky_piston), 1);
        addExtracting(new ItemStack(Blocks.golden_rail, 6), 1);
        addExtracting(new ItemStack(Blocks.detector_rail, 6), 1);
        addExtracting(new ItemStack(Blocks.activator_rail, 6), 1);
        addExtracting(new ItemStack(Items.compass), 1);
        addExtracting(new ItemStack(Items.clock), 1);

        for(int i=0;i< ItemConductor.NAMES.length;i++){
            addExtracting(new ItemStack(ItemCore.conductor, 1, i), 2);
        }
        addExtracting(new ItemStack(BlockCore.cableTin, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableCopper, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableIron, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableSilver, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableGold, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableAluminium, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.cableNickel, 1, OreDictionary.WILDCARD_VALUE), 2);
        addExtracting(new ItemStack(BlockCore.pipeTin), 2);
        addExtracting(new ItemStack(BlockCore.pipeTinExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeTinOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeTinSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeCopper), 2);
        addExtracting(new ItemStack(BlockCore.pipeCopperExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeCopperOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeCopperSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeIron), 2);
        addExtracting(new ItemStack(BlockCore.pipeIronExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeIronOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeIronSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeSilver), 2);
        addExtracting(new ItemStack(BlockCore.pipeSilverExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeSilverOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeSilverSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeGold), 2);
        addExtracting(new ItemStack(BlockCore.pipeGoldExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeGoldOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeGoldSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeAluminium), 2);
        addExtracting(new ItemStack(BlockCore.pipeAluminiumExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeAluminiumOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeAluminiumSorting), 2);
        addExtracting(new ItemStack(BlockCore.pipeNickel), 2);
        addExtracting(new ItemStack(BlockCore.pipeNickelExtractor), 2);
        addExtracting(new ItemStack(BlockCore.pipeNickelOneWay), 2);
        addExtracting(new ItemStack(BlockCore.pipeNickelSorting), 2);
        addExtracting(new ItemStack(ItemCore.crystalUnitVillager), 4);
        addExtracting(new ItemStack(ItemCore.crystalUnit), 8);
        addExtracting(new ItemStack(ItemCore.crystalUnitDouble), 16);
        addExtracting(new ItemStack(ItemCore.crystalUnitAdv), 32);
        addExtracting(new ItemStack(ItemCore.crystalUnitEnder), 36);
        //addExtracting(new ItemStack(BlockCore.generatorVLF), 10);


        // ###########################################################################
        //                            織機
        // ###########################################################################
        addWeaving(new RecipeItemStack("ir2.string", 4, new ItemStack(ItemCore.cloth)));
        addWeaving(new ItemStack(Items.string, 16), new ItemStack(ItemCore.silk));


        // ###########################################################################
        //                            染物
        // ###########################################################################
        addDying(Item.getItemFromBlock(Blocks.wool));
        addDying(Item.getItemFromBlock(BlockCore.cableTin));
        addDying(Item.getItemFromBlock(BlockCore.cableCopper));
        addDying(Item.getItemFromBlock(BlockCore.cableIron));
        addDying(Item.getItemFromBlock(BlockCore.cableSilver));
        addDying(Item.getItemFromBlock(BlockCore.cableGold));

        // ###########################################################################
        //                            燻製
        // ###########################################################################
        addSmoking(new ItemStack(Items.porkchop), new ItemStack(ItemCore.foodSmoked, 1, 0));
        addSmoking(new ItemStack(Items.beef), new ItemStack(ItemCore.foodSmoked, 1, 1));
        addSmoking(new ItemStack(Items.chicken), new ItemStack(ItemCore.foodSmoked, 1, 2));
        addSmoking(new ItemStack(Items.fish, 1, 1), new ItemStack(ItemCore.foodSmoked, 1, 3));

        // #############################################################
        //                    ミキサー
        // #############################################################
        addMixing(new ItemStack(Blocks.obsidian), new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(FluidRegistry.LAVA, 1000));
        addMixing(new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(Blocks.ice), new ItemStack(Items.blaze_powder));

        // ###########################################################################
        //                            収穫対象
        // ###########################################################################
        addHarvestTarget(Blocks.reeds, 32767);
        addHarvestTarget(BlockCore.cropReedIron, 32767);
        addHarvestTarget(Blocks.cactus, 32767);
        addHarvestTarget(Blocks.melon_block, 32767);
        addHarvestTarget(BlockCore.blockMelonLapis, 32767);
        addHarvestTarget(Blocks.pumpkin, 32767);
        addHarvestTarget(Blocks.waterlily, 32767);

        // ###########################################################################
        //                            通常釣り
        // ###########################################################################
        addFishing(new ItemStack(Items.fish, 1, 0), 10000);
        addFishing(new ItemStack(Items.bowl), 800);
        addFishing(new ItemStack(Items.leather), 800);
        addFishing(new ItemStack(Items.potionitem), 800);
        addFishing(new ItemStack(Items.bone), 800);
        addFishing(new ItemStack(Blocks.tripwire_hook), 800);
        addFishing(new ItemStack(Items.gold_nugget), 800);
        addFishing(new ItemStack(Items.stick), 800);
        addFishing(new ItemStack(Items.string), 800);
        addFishing(new ItemStack(Items.bucket), 500);
        addFishing(new ItemStack(Blocks.waterlily), 800);
        addFishing(new ItemStack(Blocks.redstone_torch), 500);
        addFishing(new ItemStack(Items.name_tag), 125);
        addFishing(new ItemStack(Items.saddle), 125);
        addFishing(new ItemStack(Items.diamond), 80);
        addFishing(new ItemStack(Items.emerald), 80);
        addFishing(new ItemStack(Items.ender_eye), 50);
        addFishing(new ItemStack(Items.experience_bottle), 50);
        addFishing(new ItemStack(ItemCore.crystalUnit), 80);
        addFishing(new ItemStack(ItemCore.coil), 250);
        addFishing(new ItemStack(ItemCore.screw), 50);

        // ###########################################################################
        //                            海釣り
        // ###########################################################################
        addFishingSea(new ItemStack(Items.fish, 1, 0), 3000);
        addFishingSea(new ItemStack(Items.fish, 1, 1), 3000);
        addFishingSea(new ItemStack(Items.fish, 1, 2), 2000);
        addFishingSea(new ItemStack(Items.fish, 1, 3), 2000);
        addFishingSea(new ItemStack(ItemCore.waterweed), 2000);
        addFishingSea(new ItemStack(Items.bowl), 800);
        addFishingSea(new ItemStack(Items.potionitem), 800);
        addFishingSea(new ItemStack(Blocks.tripwire_hook), 800);
        addFishingSea(new ItemStack(Items.gold_nugget), 800);
        addFishingSea(new ItemStack(Items.stick), 800);
        addFishingSea(new ItemStack(Items.string), 500);
        addFishingSea(new ItemStack(Items.dye, 1, 0), 1000);
        addFishingSea(new ItemStack(Items.bucket), 500);
        addFishingSea(new ItemStack(Blocks.redstone_torch), 800);
        addFishingSea(new ItemStack(Items.name_tag), 180);
        addFishingSea(new ItemStack(Items.saddle), 180);
        addFishingSea(new ItemStack(Items.diamond), 100);
        addFishingSea(new ItemStack(Items.emerald), 100);
        addFishingSea(new ItemStack(Items.ender_eye), 80);
        addFishingSea(new ItemStack(Items.experience_bottle), 125);
        addFishingSea(new ItemStack(ItemCore.crystalUnit), 100);
        addFishingSea(new ItemStack(ItemCore.coil), 500);
        addFishingSea(new ItemStack(ItemCore.screw), 80);

        //
        ArrayList<ItemStack> tmpList;
        tmpList=OreDictionary.getOres("itemRubber");
        if(!tmpList.isEmpty()){
            addFishing(new ItemStack(ItemCore.bootsRubber), 800);
            addFishingSea(new ItemStack(ItemCore.bootsRubber), 800);
            GameRegistry.addSmelting(new ItemStack(ItemCore.bootsRubber), tmpList.get(0).copy(), 0.15f);
            FMLLog.info("Added "+ItemCore.bootsRubber.getUnlocalizedName()+" to Fishing Item Table");
        }

        addFishingFromOreDictionary("gemRuby", 80, 100);
        addFishingFromOreDictionary("gemSapphire", 80, 100);
        if(!addFishingFromOreDictionary("gemGreensapphire", 80, 100)){
            addFishingFromOreDictionary("gemGreenSapphire", 80, 100);
        }

        addFishingFromOreDictionary("gemAmethyst", 80, 100);
        addFishingFromOreDictionary("gemRedGarnet", 80, 100);
        addFishingFromOreDictionary("gemYellowGarnet", 80, 100);
        addFishingFromOreDictionary("gemGarnet", 80, 100);
        addFishingFromOreDictionary("gemOlivine", 80, 100);
        addFishingFromOreDictionary("gemAmber", 80, 100);

        IR2.logger.info("Added " + instance.additionalFishingCount + " Fishing Results from OreDictionary");


        // ###########################################################################
        //                            伐採
        // ###########################################################################
        addWood(Blocks.log);
        addWood(Blocks.log2);
        addLeave(Blocks.leaves);
        addLeave(Blocks.leaves2);

        // ###########################################################################
        //                            鉱物
        // ###########################################################################
        addOre(Blocks.coal_ore, 0);
        addOre(Blocks.iron_ore, 0);
        addOre(Blocks.gold_ore, 0);
        addOre(Blocks.redstone_ore, 0);
        addOre(Blocks.lit_redstone_ore, 0);
        addOre(Blocks.diamond_ore, 0);
        addOre(Blocks.emerald_ore, 0);
        addOre(Blocks.clay, 0);
        addOre(Blocks.quartz_ore, 0);
        addOre(BlockCore.ore, 0);
        addOre(BlockCore.ore, 1);
        addOre(BlockCore.ore, 2);

        // ###########################################################################
        //                            埋め立て用
        // ###########################################################################
        addStone(Blocks.stone, 0);
        addStone(Blocks.cobblestone, 0);
        addStone(Blocks.sand, 0);
        addStone(Blocks.gravel, 0);
        for(int i=0;i<BlockDirt.field_150009_a.length;i++) {
            addStone(Blocks.dirt, i);
        }
        for(int i=0;i<BlockSandStone.field_150157_a.length;i++) {
            addStone(Blocks.sandstone, i);
        }
        addStone(Blocks.netherrack, 0);
        addStone(Blocks.soul_sand, 0);


        // ###########################################################################
        //                            スポナー
        // ###########################################################################
        addMob(new ItemStack(Items.rotten_flesh), "Zombie");
        addMob(new ItemStack(Items.bone), "Skeleton");
        addMob(new ItemStack(Items.string), "Spider");
        addMob(new ItemStack(Items.spider_eye), "Spider");
        addMob(new ItemStack(Items.gunpowder), "Creeper");
        addMob(new ItemStack(Items.slime_ball), "Slime");
        addMob(new ItemStack(Items.ghast_tear), "Ghast");
        addMob(new ItemStack(Items.ender_pearl), "Enderman");
        addMob(new ItemStack(Items.blaze_rod), "Blaze");
        addMob(new ItemStack(Items.magma_cream), "LavaSlime");
        addMob(new ItemStack(Items.porkchop), "Pig");
        for(int i=0;i<16;i++) {
            addMob(new ItemStack(Blocks.wool, 1, i), "Sheep");
        }
        addMob(new ItemStack(Items.beef), "Cow");
        addMob(new ItemStack(Items.feather), "Chicken");
        addMob(new ItemStack(Items.chicken), "Chicken");
        addMob(new ItemStack(Items.emerald), "Villager");


        //FMLLog.severe(instance.recipeCrushing.toString());

        //--------------------------------------------------------------------
        //                鉱石ユニファイア
        //--------------------------------------------------------------------
        String[] ores=OreDictionary.getOreNames();
        for(String n : ores){
            if(n.length()>3 && (n.startsWith("ore") || n.startsWith("gem"))){
                addUnifierName(n);
                continue;
            }
            if(n.length()>4 && n.startsWith("dust")){
                addUnifierName(n);
                continue;
            }
            if(n.length()>5 && n.startsWith("ingot")){
                addUnifierName(n);
                continue;
            }
            if(n.length()>7 && n.startsWith("crushed")){
                addUnifierName(n);
                continue;
            }
        }

    }
    public static void RegisterOreDictionary(){
        OreDictionary.registerOre("oreTin", new ItemStack(BlockCore.ore, 1, 0));
        OreDictionary.registerOre("oreCopper", new ItemStack(BlockCore.ore, 1, 1));
        OreDictionary.registerOre("oreSilver", new ItemStack(BlockCore.ore, 1, 2));

        OreDictionary.registerOre("blockTin", new ItemStack(BlockCore.block, 1, 0));
        OreDictionary.registerOre("blockCopper", new ItemStack(BlockCore.block, 1, 1));
        OreDictionary.registerOre("blockSilver", new ItemStack(BlockCore.block, 1, 2));

        OreDictionary.registerOre("ingotTin", new ItemStack(ItemCore.ingot, 1, 0));
        OreDictionary.registerOre("ingotCopper", new ItemStack(ItemCore.ingot, 1, 1));
        OreDictionary.registerOre("ingotSilver", new ItemStack(ItemCore.ingot, 1, 2));
        OreDictionary.registerOre("ingotManganese", new ItemStack(ItemCore.ingot, 1, 3));
        OreDictionary.registerOre("ingotCobalt", new ItemStack(ItemCore.ingot, 1, 4));
        OreDictionary.registerOre("ingotAluminium", new ItemStack(ItemCore.ingot, 1, 5));
        OreDictionary.registerOre("ingotSilicon", new ItemStack(ItemCore.ingot, 1, 6));
        OreDictionary.registerOre("ingotNickel", new ItemStack(ItemCore.ingot, 1, 7));

        OreDictionary.registerOre("dustTin", new ItemStack(ItemCore.dust, 1, 0));
        OreDictionary.registerOre("dustCopper", new ItemStack(ItemCore.dust, 1, 1));
        OreDictionary.registerOre("dustIron", new ItemStack(ItemCore.dust, 1, 2));
        OreDictionary.registerOre("dustSilver", new ItemStack(ItemCore.dust, 1, 3));
        OreDictionary.registerOre("dustGold", new ItemStack(ItemCore.dust, 1, 4));
        OreDictionary.registerOre("dustManganese", new ItemStack(ItemCore.dust, 1, 5));
        OreDictionary.registerOre("dustCobalt", new ItemStack(ItemCore.dust, 1, 6));
        OreDictionary.registerOre("dustAluminium", new ItemStack(ItemCore.dust, 1, 7));
        OreDictionary.registerOre("dustSilicon", new ItemStack(ItemCore.dust, 1, 8));
        OreDictionary.registerOre("dustNickel", new ItemStack(ItemCore.dust, 1, 9));
        OreDictionary.registerOre("dustMagnesium", new ItemStack(ItemCore.dust, 1, 10));
        OreDictionary.registerOre("dustWood", new ItemStack(ItemCore.dust, 1, 11));
        OreDictionary.registerOre("dustCoal", new ItemStack(ItemCore.dust, 1, 12));

        OreDictionary.registerOre("gearWood", ItemCore.gearWood);
        OreDictionary.registerOre("gearStone", ItemCore.gearStone);
        OreDictionary.registerOre("gearIron", ItemCore.gearIron);

        OreDictionary.registerOre("flour", ItemCore.flour);
        OreDictionary.registerOre("itemFlour", ItemCore.flour);
        OreDictionary.registerOre("dustFlour", ItemCore.flour);
        OreDictionary.registerOre("foodFlour", ItemCore.flour);

        OreDictionary.registerOre("seedCotton", ItemCore.seedCotton);
        OreDictionary.registerOre("cropCotton", ItemCore.cottonRaw);

        OreDictionary.registerOre("cloth", ItemCore.cloth);
        OreDictionary.registerOre("itemCloth", ItemCore.cloth);
        OreDictionary.registerOre("craftingCloth", ItemCore.cloth);
        OreDictionary.registerOre("craftingSmallCloth", ItemCore.cloth);
        OreDictionary.registerOre("craftingFilterCloth", ItemCore.cloth);
        OreDictionary.registerOre("clothBlankets", ItemCore.cloth);
        OreDictionary.registerOre("ir2.cloth", ItemCore.cloth);

        OreDictionary.registerOre("silk", ItemCore.silk);
        OreDictionary.registerOre("itemSilk", ItemCore.silk);
        OreDictionary.registerOre("clothSilk", ItemCore.silk);
        OreDictionary.registerOre("ir2.cloth", ItemCore.silk);

        OreDictionary.registerOre("ir2.string", ItemCore.knittingWool);
        OreDictionary.registerOre("ir2.string", ItemCore.stringCotton);

        OreDictionary.registerOre("ir2.leather", Items.leather);
        OreDictionary.registerOre("ir2.leather", ItemCore.leather);

        OreDictionary.registerOre("dyeBlue", new ItemStack(ItemCore.dust, 1, 6));


        //----------------------------------------------------------------------
        //  どうしてここでFluidContainerの設定をしているかって？ 聞くな。
        //----------------------------------------------------------------------
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockCore.fluidMilk, 1000), new ItemStack(Items.milk_bucket), new ItemStack(Items.bucket));
        //FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockCore.fluidRedstone, 1000), new ItemStack(ItemCore.bucketRedstone), new ItemStack(Items.bucket));
    }
    public static void RegisterEXPTable(){
        addEXP(new ItemStack(Blocks.gravel), 0.05f);
        addEXP(new ItemStack(Items.flint), 0.1f);
        addEXP(new ItemStack(Blocks.sand), 0.05f);

        addEXP(new ItemStack(ItemCore.dust, 1, 0), 0.2f);
        addEXP(new ItemStack(ItemCore.dust, 1, 1), 0.25f);
        addEXP(new ItemStack(ItemCore.dust, 1, 2), 0.3f);
        addEXP(new ItemStack(ItemCore.dust, 1, 3), 0.35f);
        addEXP(new ItemStack(ItemCore.dust, 1, 4), 0.4f);
        for(int i=5;i<10;i++) addEXP(new ItemStack(ItemCore.dust, 1, i), 0.2f);

        addEXP(new ItemStack(Items.redstone), 0.25f);
        addEXP(new ItemStack(Items.dye, 1, 4), 0.25f);
        addEXP(new ItemStack(Items.diamond), 1.0f);
        addEXP(new ItemStack(Items.emerald), 1.0f);
        addEXP(new ItemStack(Items.quartz), 0.7f);

        addEXP(new ItemStack(ItemCore.conductor, 1, 0), 0.4f);
        addEXP(new ItemStack(ItemCore.conductor, 1, 1), 0.5f);
        addEXP(new ItemStack(ItemCore.conductor, 1, 2), 0.6f);
        addEXP(new ItemStack(ItemCore.conductor, 1, 3), 0.7f);
        addEXP(new ItemStack(ItemCore.conductor, 1, 4), 0.8f);
        for(int i=5;i<10;i++) addEXP(new ItemStack(ItemCore.conductor, 1, i), 0.4f);

        addEXP(new ItemStack(ItemCore.knittingWool), 0.3f);
        addEXP(new ItemStack(ItemCore.cloth), 0.8f);
        addEXP(new ItemStack(ItemCore.silk), 0.8f);

        addEXP(new ItemStack(Items.dye, 1, 15), 0.3f);
        addEXP(new ItemStack(Items.blaze_powder), 0.5f);
        addEXP(new ItemStack(ItemCore.flour), 0.1f);
    }
    public static void RegisterBuildingItems2(){

        //enchantment lv
        addBuildingEnchantItem(new ItemStack(Items.dye, 1, 4), 1);
        addBuildingEnchantItem(new ItemStack(Items.ender_pearl), 5);
        addBuildingEnchantItem(new ItemStack(Items.ender_eye), 8);
        addBuildingEnchantItem(new ItemStack(Blocks.lapis_block), 9);
        addBuildingEnchantItem(new ItemStack(Items.nether_star), 30);

        //others
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.feather), 50, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.brick), 35, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.netherbrick), 35, 2);
        //addBuildingItem(COMPOSITION_OTHER, new ItemStack(Blocks.dirt, 1, 1), 40, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.sand, 1, 1), 40, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.dirt, 1, 1), 40, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.netherrack), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.soul_sand), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.leather), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.dye), 40, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.string), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.slime_ball), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.dirt, 1, 2), 40, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.packed_ice), 35, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.clay), 40, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.blaze_powder), 1, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.ice), 40, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.mycelium), 40, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.bookshelf), 60, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.lit_pumpkin), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.bone), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.gunpowder), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.spider_eye), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.magma_cream), 1, 11);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.book), 60, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.blaze_rod), 60, 12);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.mossy_cobblestone), 40, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.speckled_melon), 60, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.ender_pearl), 80, 32);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.ender_eye), 80, 38);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Items.nether_star), 1, 500);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_OTHER, new ItemStack(Blocks.dragon_egg), 1, 1000);

        //plants
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.grass), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.tallgrass), 1, 1);
        for(int i=0;i<3;i++)    addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.tallgrass, 1, i), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.pumpkin_seeds), 40, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.melon_seeds), 40, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.reeds), 80, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.cactus), 80, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.vine), 80, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.waterlily), 40, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.melon), 50, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.dye, 1, 3), 75, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.yellow_flower), 50, 3);
        for(int i=0;i<BlockFlower.field_149859_a.length;i++)    addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.red_flower, 1, i), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.wheat), 40, 4);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.cottonRaw), 40, 4);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.carrot), 40, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.potato), 40, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.poisonous_potato), 1, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.red_mushroom), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.brown_mushroom), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.red_mushroom_block), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.brown_mushroom_block), 1, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.nether_wart), 50, 7);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.pumpkin), 40, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.cocoa), 1, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Blocks.melon_block), 40, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.wheat), 1, 36);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.saplingRed), 15, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.saplingGold), 15, 20);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.seedMelonLapis), 15, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.seedWheatGlow), 15, 12);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.wheatGlow), 50, 12);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.potatoQuartz), 15, 12);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.reedIron), 15, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.melonLapis), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.blockMelonLapis), 40, 27);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(Items.apple), 50, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.appleRed), 30, 8);

        //Ore
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.flint), 60, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.gold_nugget), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.redstone), 1, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.dye, 1, 4), 1, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.glowstone_dust), 1, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Blocks.obsidian), 60, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Items.coal), 1, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Blocks.glowstone), 80, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Blocks.quartz_stairs), 1, 40);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_ORE, new ItemStack(Blocks.coal_block), 1, 45);

        //Tool
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.wooden_axe), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.wooden_pickaxe), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.wooden_shovel), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.wooden_hoe), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.wooden_sword), 50, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.fire_charge), 35, 4);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.leather_helmet), 35, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.leather_chestplate), 35, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.leather_leggings), 35, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.leather_boots), 35, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.flint_and_steel), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.lead), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.stone_axe), 50, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.stone_pickaxe), 50, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.stone_shovel), 50, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.stone_hoe), 50, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.stone_sword), 50, 6);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.shears), 80, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.fishing_rod), 50, 10);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.bucket), 75, 12);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_axe), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_pickaxe), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_shovel), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_hoe), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_sword), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_helmet), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_chestplate), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_leggings), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_boots), 50, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.compass), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_11), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_13), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_blocks), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_cat), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_chirp), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_far), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_mall), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_mellohi), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_stal), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_strad), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_wait), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.record_ward), 35, 18);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.saddle), 40, 20);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.experience_bottle), 35, 20);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.iron_horse_armor), 40, 22);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.clock), 35, 25);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_axe), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_pickaxe), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_shovel), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_hoe), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_sword), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_helmet), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_chestplate), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_leggings), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_boots), 50, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.name_tag), 50, 30);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.golden_horse_armor), 40, 35);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_axe), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_pickaxe), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_shovel), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_hoe), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_sword), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_helmet), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_chestplate), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_leggings), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_boots), 40, 50);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(Items.diamond_horse_armor), 40, 60);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(ItemCore.ragnarok), 30, 64);

        //food
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.chicken), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.porkchop), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.beef), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.fish, 1, 0), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.fish, 1, 1), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cookie), 50, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.egg), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.rotten_flesh), 1, 1);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.bread), 80, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.baked_potato), 80, 2);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cooked_chicken), 80, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cooked_porkchop), 80, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cooked_beef), 80, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cooked_fished, 1, 0), 80, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cooked_fished, 1, 1), 80, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.fish, 1, 2), 1, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.fish, 1, 3), 1, 3);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.mushroom_stew), 50, 5);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.cake), 40, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.pumpkin_pie), 40, 8);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.golden_carrot), 40, 15);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.golden_apple, 1, 0), 40, 20);
        addBuildingItem(IR3RecipeAPI.COMPOSITION_FOOD, new ItemStack(Items.golden_apple, 1, 1), 25, 60);

        //ゲームバランス、こわれる
        String[] names=OreDictionary.getOreNames();

        for(String n : names){
            if(n.startsWith("ore")){
                addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, n, 50, 5);

                String _n="block"+n.substring(3);
                if(_n.length()>"block".length()) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, _n, 1, 60);
            }
            else if(n.startsWith("gem")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, n, 1, 5);
            else if(n.startsWith("ingot")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, n, 1, 5);
            else if(n.startsWith("dust")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, n, 1, 5);
            else if(n.startsWith("crushed")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_ORE, n, 1, 5);

            else if(n.startsWith("fish")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_FOOD, n, 50, 1);
            else if(n.startsWith("food")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_FOOD, n, 50, 2);
            else if(n.startsWith("cooked")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 2);

            else if(n.startsWith("seed")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 1);
            else if(n.startsWith("sapling")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 1);
            else if(n.startsWith("crop")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 2);
            else if(n.startsWith("tree")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 2);
            else if(n.startsWith("log")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 1);
            else if(n.startsWith("fruit")) addBuildingFromOreDictionary(IR3RecipeAPI.COMPOSITION_PLANT, n, 50, 2);
        }

        IR2.logger.info("Added "+instance.additionalBuildingCount+" Composition Items from OreDictionary");
    }
    public static void RegisterCrushingItems(){
        String[] names=OreDictionary.getOreNames();

        for(String n : names) {
            if(!n.equals("oreRedstone") && !n.equals("oreLapis") && !n.equals("gemCoal") && addCrushingFromOreDictionary(n, null)){
                instance.additionalCrushCount++;
            }
        }

        IR2.logger.info("Added "+instance.additionalCrushCount+" Additional Crush Recipes from OreDictionary");
    }
    public static void RegisterFilledCan(){
        Iterator<Map.Entry<String, Fluid>> it=FluidRegistry.getRegisteredFluids().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Fluid> e=it.next();
            int id=FluidRegistry.getFluidID(e.getKey());
            FluidContainerRegistry.registerFluidContainer(new FluidStack(e.getValue(), ItemCan.CAPACITY), new ItemStack(ItemCore.can, 1, id+1), new ItemStack(ItemCore.can, 1, 0));
        }
    }

    // 連携要素のセットアップ
    public static void SetUpCooperation(){
        if(Loader.isModLoaded("mceconomy2")) {

            //----------------------------------------------------------------------------------------------
            int k = 1;
            if (Loader.isModLoaded("net.minecraft.scalar.cutall.mod_CutAllSMP")) {
                k = 0;
            }
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.knittingWool), 80);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.stringCotton), 60);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.cloth), 150);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.silk), 80);

            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.can, 1, 32767), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.canOxygen), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.canSoilent), 10);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.canYamatoni), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.wrench), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.hammer), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.needle), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.injector), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.multimeter), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.knifeBlade), 20);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.knifeGrip), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.knife), 1500);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.knifeMP), 1300);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.coil), 20);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.leather), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bookTutorial), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bookRecipe), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bag), -1);

            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.seedCotton), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.cottonRaw), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.seedMelonLapis), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.melonLapis), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.blockMelonLapis), 9);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.reedIron), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.seedWheatGlow), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.wheatGlow), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.potatoQuartz), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.waterweed), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.flour), 1);

            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.saplingRed), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.saplingGold), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.leaveRed), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.leaveGold), 0);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.logRed), 3 * k);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.logGold), 3 * k);

            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.brickRusty), 20);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.alloySmelterRustyIdle), -1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.alloySmelterRustyActive), -1);

            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.ore, 1, 0), 12);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.ore, 1, 1), 16);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.ore, 1, 2), 60);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.crust, 1, 0), 80);
            MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.crust, 1, 1), 80);

            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 0), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 1), 40);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 2), 50);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 3), 150);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 4), 200);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 5), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 6), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 7), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 8), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 9), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 10), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 11), 1);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.dust, 1, 12), 5);

            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 0), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 1), 40);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 2), 150);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 3), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 4), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 5), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 6), 100);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.ingot, 1, 7), 100);

            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.foodSmoked, 1, 0), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.foodSmoked, 1, 1), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.foodSmoked, 1, 2), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.foodSmoked, 1, 3), 30);
            MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.cheese), 50);

            //----------------------------------------------------------------------------------------------
            // ショップ
            //----------------------------------------------------------------------------------------------
            ProductList pl = new ProductList() {
                @Override
                public String getProductListName() {
                    return "shop.ir2.shop";
                }
            };
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.waterproof), 50));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.can), 80));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.needle), 200));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.leather), 250));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.gearWood), 250));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.gearStone), 500));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.gearIron), 750));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.coil), 1200));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.crystalUnitCreeper), 2000));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.knifeBlade), 2500));
            pl.addItemProduct(new ProductItem(new ItemStack(BlockCore.relay), 3000));
            pl.addItemProduct(new ProductItem(new ItemStack(BlockCore.regulator), 3000));
            pl.addItemProduct(new ProductItem(new ItemStack(ItemCore.screw), 50000));

            instance.shopId = MCEconomyAPI.registerProductList(pl);
            instance.shopList = pl;

        }

        if(IR2.cooperatesAgri){
            ForAgriCraft.setup();
        }
        if(IR2.cooperatesSS2){
            ForSS2.setup();
        }
        if(IR2.cooperatesInsanity){
            ForInsanity.setup();
        }
    }

    // レシピ大全用
    public static void WriteRecipesToNBT(NBTTagCompound nbt){
        /*
        nbt.setString("title", I18n.format("book.IR2recipe.title"));
        nbt.setString("author", "F(@plusplus_san)");

        NBTTagList pages=new NBTTagList();

        int row;
        String text;
        //spinning
        text='['+BlockCore.machineSpinning.getLocalizedName()+"]\n"+I18n.format("book.IR2recipe.spinning").replace('$', '\n')+"\n\n["+Blocks.wool.getLocalizedName()+"]->"+"["+I18n.format(ItemCore.knittingWool.getUnlocalizedName()+".name")+"]";
        pages.appendTag(new NBTTagString(text));
        //alloying
        text='['+BlockCore.machineAlloySmelter.getLocalizedName()+"]\n"+I18n.format("book.IR2recipe.alloying").replace('$', '\n');
        pages.appendTag(new NBTTagString(text));
        {
            Iterator<RecipeItemStack> it=instance.recipeAlloying.iterator();
            row=1;
            text='['+BlockCore.machineAlloySmelter.getLocalizedName()+"]\n";
            while(it.hasNext()){
                RecipeItemStack r=it.next();

                text+="\n["+r.getKey().getDisplayName()+"]->["+r.getValue().getDisplayName()+"]\n";

                row++;
                if(row==5){
                    pages.appendTag(new NBTTagString(text));
                    row=1;
                    text='['+BlockCore.machineAlloySmelter.getLocalizedName()+"]\n";
                }
            }
            if(row>0) {
                pages.appendTag(new NBTTagString(text));
            }
        }
        //weaving
        text='['+BlockCore.machineLoom.getLocalizedName()+"]\n"+I18n.format("book.IR2recipe.weaving").replace('$', '\n');
        {
            Iterator<Map.Entry<ItemStack, ItemStack>> it = instance.recipeWeaving.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ItemStack, ItemStack> r = it.next();
                text += "\n[" + r.getKey().getDisplayName() + ':' + r.getKey().stackSize + "]->[" + r.getValue().getDisplayName() + "]\n";
            }
            pages.appendTag(new NBTTagString(text));
        }

        //crushing
        text='['+BlockCore.machineCrusherAdv.getLocalizedName()+"]\n"+I18n.format("book.IR2recipe.crushing").replace('$', '\n');
        pages.appendTag(new NBTTagString(text));
        {
            Iterator<Map.Entry<ItemStack, Object[]>> it=instance.recipeCrushing.entrySet().iterator();
            row=0;
            text='['+BlockCore.machineCrusherAdv.getLocalizedName()+"]\n";
            while(it.hasNext()){
                Map.Entry<ItemStack, Object[]> r=it.next();
                Object[] results=r.getValue();

                text+="\n["+r.getKey().getDisplayName()+"]->\n";
                for(int i=0;i<results.length;i+=2){
                    int p= MathHelper.floor_float((Float)results[i]*100);
                    ItemStack item=(ItemStack)results[i+1];
                    text+="[("+p+"%)"+item.getDisplayName()+':'+item.stackSize+"]\n";
                }

                row++;
                if(row==3){
                    pages.appendTag(new NBTTagString(text));
                    row=0;
                    text='['+BlockCore.machineCrusherAdv.getLocalizedName()+"]\n";
                }
            }
            if(row>0) {
                pages.appendTag(new NBTTagString(text));
            }
        }

        //extracting
        text='['+BlockCore.machineExtractor.getLocalizedName()+"]\n"+I18n.format("book.IR2recipe.extracting").replace('$', '\n');
        pages.appendTag(new NBTTagString(text));
        {
            Iterator<Map.Entry<ItemStack, Integer>> it=instance.recipeExtracting.entrySet().iterator();
            row=1;
            text='['+BlockCore.machineExtractor.getLocalizedName()+"]\n";
            while(it.hasNext()){
                Map.Entry<ItemStack, Integer> r=it.next();

                text+="\n["+r.getKey().getDisplayName()+"]->[RS:"+r.getValue()+"]\n";

                row++;
                if(row==5){
                    pages.appendTag(new NBTTagString(text));
                    row=1;
                    text='['+BlockCore.machineExtractor.getLocalizedName()+"]\n";
                }
            }
            if(row>0) {
                pages.appendTag(new NBTTagString(text));
            }
        }

        nbt.setTag("pages", pages);
        */
    }

    // oreの鉱石辞書登録情報を見てitemと対応するかどうか確認する。
    public static boolean IsItemEquals(ItemStack ore, ItemStack item, @Nullable int[] itemIds){
        if(ore==null || item==null) return false;

        if(OreDictionary.itemMatches(ore, item, false)) return true;

        if(itemIds==null) itemIds=OreDictionary.getOreIDs(item);

        int[] oIds=OreDictionary.getOreIDs(ore);
        for(int i=0;i<oIds.length;i++){
            for(int j=0;j<itemIds.length;j++){
                if(oIds[i]==itemIds[j]){
                    return true;
                }
            }
        }
        return  false;
    }

    public static int GetShopId(){
        return instance.shopId;
    }

    // #############################################################
    //                    経験値
    // #############################################################
    /**
     * IR2の加工機械の完成品スロット(SlotWithEXP)から取り出したときに得られる経験値量を登録する
     * 経験値の設定量はバニラかまど準拠
     * @param m
     * @param amount
     */
    public static void addEXP(ItemStack m, Float amount){
        instance.expTable.put(m, amount);
    }

    /**
     * SlotWithEXPから取り出した際の経験値量を返す
     * @param m
     * @return
     */
    public static float getEXP(ItemStack m){
        int[] materialIds=OreDictionary.getOreIDs(m);
        Iterator<Map.Entry<ItemStack, Float>> it=instance.expTable.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ItemStack, Float> r=it.next();

            if(r.getKey().isItemEqual(m)){
                return r.getValue();
            }

            int[] productIds=OreDictionary.getOreIDs(r.getKey());
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return r.getValue();
                    }
                }
            }
        }
        return 0;
    }

    // #############################################################
    //                    紡績レシピ
    // #############################################################
    //紡績レシピを追加する。 mは素材で、stackSizeに補正値をかけたものが実際の消費数となる
    private static RecipeItemStack cachedSpinning;

    /**
     * 紡績機で扱う紡績レシピを追加する。
     * @param m 材料。stackSize に各紡績機固有の料消費数倍率(scale)をかけた物が実際の消費量となるので注意
     * @param p 完成品
     */
    public static void addSpinning(ItemStack m, ItemStack p){
        addSpinning(new RecipeItemStack(m, p));
    }
    public static void addSpinning(RecipeItemStack r){
        instance.recipeSpinning.add(r);
    }

    /**
     * 紡績で得られるものを返す
     * @param m 素材アイテムスタック
     * @param scale 各紡績機固有の、材料消費数倍率
     * @return .copy()して使ってね
     */
    public static ItemStack getSpinning(ItemStack m, int scale){
        //キャッシュを確認
        if(cachedSpinning!=null && cachedSpinning.isMatch(m) && m.stackSize>=cachedSpinning.getMaterial().stackSize*scale) return cachedSpinning.getProduct();

        //レシピ走査
        Iterator<RecipeItemStack> it=instance.recipeSpinning.iterator();
        while(it.hasNext()){
            RecipeItemStack r=it.next();

            //なんか布関連でアレなことになりそうだったから・・・
            if(r.isMatch(m) && m.stackSize>=r.getMaterial().stackSize*scale){
                cachedSpinning=r;
                return r.getProduct();
            }
        }
        return null;
    }

    /**
     * mの紡績レシピでの登録stackSizeを得る
     * @param m
     * @return
     */
    public static int getSpinningMaterialAmount(ItemStack m){
        //キャッシュを確認
        if(cachedSpinning!=null && cachedSpinning.isMatch(m)) return cachedSpinning.getMaterial().stackSize;

        //レシピ走査
        Iterator<RecipeItemStack> it=instance.recipeSpinning.iterator();
        while(it.hasNext()){
            RecipeItemStack r=it.next();

            if(r.isMatch(m)){
                cachedSpinning=r;
                return r.getMaterial().stackSize;
            }
        }
        return 0;
    }
    public static ArrayList<RecipeItemStack> getSpinning(){ return instance.recipeSpinning; }


    // #############################################################
    //                    合金レシピ
    // #############################################################

    /**
     * RS合金のレシピを追加する
     * このメソッドに引数としてレッドストーンを渡す必要は無い。
     * @param m 対象アイテム
     * @param p 完成品RS合金
     */
    public static void addAlloying(ItemStack m, ItemStack p){
        //instance.recipeAlloying.put(m, p);
        addAlloying(new RecipeItemStack(m, p));
    }
    public static void addAlloying(RecipeItemStack r){
        instance.recipeAlloying.add(r);
    }

    /**
     * 素材アイテムスタックからRS合金を得る
     * @param m 素材
     * @return
     */
    public static ItemStack getAlloying(ItemStack m){

        Iterator<RecipeItemStack> it=instance.recipeAlloying.iterator();
        while(it.hasNext()) {
            RecipeItemStack r = it.next();
            if(r.isMatch(m)) return r.getProduct();

            //if (IsItemEquals(r.getKey(), m, materialIds)) return r.getValue();

            /*
            if (r.getKey().isItemEqual(m)) {
                return r.getValue();
            }

            int[] productIds = OreDictionary.getOreIDs(r.getKey());
            for (int i = 0; i < productIds.length; i++) {
                for (int j = 0; j < materialIds.length; j++) {
                    if (productIds[i] == materialIds[j]) {
                        return r.getValue();
                    }
                }
            }
            */
        }

        return null;
    }
    public static ArrayList<RecipeItemStack> getAlloying(){
        return instance.recipeAlloying;
    }

    // #############################################################
    //                    破砕レシピ
    // #############################################################
    private static RecipeItemStack.CrushingRecipeItemStack cachedCrushing;
    /**
     * 破砕レシピを追加する
     * @param m 素材アイテムスタック
     * @param o 生成物1つあたり生成確率(Float)と生成物(ItemStack)の2つ組(順固定)で構成されるObject配列。
     *          生成物は最大3個まで登録できる
     */
    public static void addCrushing(ItemStack m, Object[] o){
        addCrushing(new RecipeItemStack.CrushingRecipeItemStack(m, o));
    }
    public static void addCrushing(String m, Object[] o){
        addCrushing(new RecipeItemStack.CrushingRecipeItemStack(m, o));
    }
    public static void addCrushing(RecipeItemStack.CrushingRecipeItemStack r){
        instance.recipeCrushing.add(r);
    }

    /**
     * 鉱石辞書から破砕レシピを登録する
     * @param oreName
     * @param ore
     * @return
     */
    public static boolean addCrushingFromOreDictionary(String oreName, @Nullable ItemStack ore){
        if(oreName.length()<3) return false;

        // prefix "ore"かつ"gem"または"crushed"または"dust"が存在する場合、破砕レシピの追加
        // ---------------------------------------------------------------------------------------------
        if(oreName.startsWith("ore") && Character.isUpperCase(oreName.charAt(3))){
            String suf=oreName.substring(3);
            ArrayList<ItemStack> items;

            //宝石
            items=OreDictionary.getOres("gem"+suf);
            if(items.size()>0){
                ItemStack it=items.get(0);
                it.stackSize=1;

                if(ore==null){
                    ArrayList<ItemStack> al=OreDictionary.getOres(oreName);
                    if(!al.isEmpty()){
                        ore=al.get(0).copy();
                        ore.stackSize=1;
                    }
                }

                if(ore!=null){
                    addCrushing("ore"+suf, new Object[]{1.0f ,it.copy(), 0.5f,it.copy(), 0.5f,it.copy()});
                    return true;
                }
            }

            //砕いた
            items=OreDictionary.getOres("crushed"+suf);
            if(items.size()>0){
                ItemStack it=items.get(0);
                it.stackSize=1;

                if(ore==null){
                    ArrayList<ItemStack> al=OreDictionary.getOres(oreName);
                    if(!al.isEmpty()){
                        ore=al.get(0).copy();
                        ore.stackSize=1;
                    }
                }

                if(ore!=null){
                    addCrushing("ore"+suf, new Object[]{1.0f ,it.copy(), 0.5f,it.copy(), 0.5f,it.copy()});
                    return true;
                }
            }

            //粉
            items=OreDictionary.getOres("dust"+suf);
            if(items.size()>0){
                ItemStack it=items.get(0);
                it.stackSize=1;

                if(ore==null){
                    ArrayList<ItemStack> al=OreDictionary.getOres(oreName);
                    if(!al.isEmpty()){
                        ore=al.get(0).copy();
                        ore.stackSize=1;
                    }
                }

                if(ore!=null){
                    addCrushing("ore"+suf, new Object[]{1.0f ,it.copy(), 0.5f,it.copy(), 0.5f,it.copy()});
                    return true;
                }
            }
        }

        // prefix "gem"かつ"crushed"または"dust"が存在する場合、破砕レシピの追加
        // ---------------------------------------------------------------------------------------------
        if(oreName.startsWith("gem") && Character.isUpperCase(oreName.charAt(3))){
            String suf=oreName.substring(3);
            ArrayList<ItemStack> items;

            //砕いた
            items=OreDictionary.getOres("crushed"+suf);
            if(items.size()>0){
                ItemStack it=items.get(0);
                it.stackSize=1;

                if(ore==null){
                    ArrayList<ItemStack> al=OreDictionary.getOres(oreName);
                    if(!al.isEmpty()){
                        ore=al.get(0).copy();
                        ore.stackSize=1;
                    }
                }
                if(ore!=null){
                    addCrushing("gem"+suf, new Object[]{1.0f ,it.copy()});
                    return true;
                }
            }

            //粉
            items=OreDictionary.getOres("dust"+suf);
            if(items.size()>0){
                ItemStack it=items.get(0);
                it.stackSize=1;


                if(ore==null){
                    ArrayList<ItemStack> al=OreDictionary.getOres(oreName);
                    if(!al.isEmpty()){
                        ore=al.get(0).copy();
                        ore.stackSize=1;
                    }
                }
                if(ore!=null){
                    addCrushing("gem"+suf, new Object[]{1.0f ,it.copy()});
                    return true;
                }
            }
        }

        return false;
    }
    public static Object[] findCrushing(ItemStack m){
        int[] materialIds=OreDictionary.getOreIDs(m);

        //キャッシュ
        if(cachedCrushing!=null && cachedCrushing.isMatch(m)) return cachedCrushing.getProducts();

        //レシピまわす
        Iterator<RecipeItemStack.CrushingRecipeItemStack> it=instance.recipeCrushing.iterator();
        while(it.hasNext()){
            RecipeItemStack.CrushingRecipeItemStack r=it.next();

            if(r.isMatch(m)){
                cachedCrushing=r;
                return r.getProducts();
            }

            /*
            if(IsItemEquals(r.getKey(), m, materialIds)){
                cachedCrushing=r;
                return r.getValue();
            }
            */
        }

        return null;
    }
    public static ItemStack[] getCrushingProducts(ItemStack m, float prob2){
        Object[] o=findCrushing(m);
        if(o==null)     return null;

        LinkedList<ItemStack> list=new LinkedList<ItemStack>();
        for(int i=0;i<3;i++){
            if(2*i<o.length) {
                if (instance.random.nextFloat() <= (Float) o[2 * i]*prob2) {
                    list.add(((ItemStack) o[2 * i + 1]).copy());
                } else {
                    list.add(null);
                }
            }
            else{
                list.add(null);
            }
        }

        ItemStack[] r=new ItemStack[list.size()];
        list.toArray(r);
        return r;
    }
    public static ArrayList<RecipeItemStack.CrushingRecipeItemStack> getCrushing() {
        return instance.recipeCrushing;
    }

    // #############################################################
    //                    抽出レシピ
    // #############################################################
    public static RecipeItemStack.ExtractingRecipeItemStack cachedExtracting;
    public static void addExtracting(ItemStack m, int p){
        //instance.recipeExtracting.put(m, p);
        addExtracting(new RecipeItemStack.ExtractingRecipeItemStack(m, p));
    }
    public static void addExtracting(RecipeItemStack.ExtractingRecipeItemStack r){
        instance.recipeExtracting.add(r);
    }

    public static int getExtracting(ItemStack m){
        //キャッシュ
        if(cachedExtracting!=null && cachedExtracting.isMatch(m) && m.stackSize>=cachedExtracting.getMaterial().stackSize) return cachedExtracting.getProductAmount();

        //レシピ
        Iterator<RecipeItemStack.ExtractingRecipeItemStack> it=instance.recipeExtracting.iterator();
        while(it.hasNext()){
            RecipeItemStack.ExtractingRecipeItemStack r=it.next();

            if(r.isMatch(m) && m.stackSize>=r.getMaterial().stackSize){
                cachedExtracting=r;
                return r.getProductAmount();
            }
        }
        return 0;
    }
    public static ArrayList<RecipeItemStack.ExtractingRecipeItemStack> getExtracting(){
        return instance.recipeExtracting;
    }

    // #############################################################
    //                    織機レシピ
    // #############################################################
    public static void addWeaving(ItemStack m, ItemStack p){
        addWeaving(new RecipeItemStack(m, p));
    }
    public static void addWeaving(String m, ItemStack p){
        addWeaving(new RecipeItemStack(m, p));
    }
    public static void addWeaving(RecipeItemStack r){
        instance.recipeWeaving.add(r);
    }
    public static ItemStack getWeaving(ItemStack m){
        Iterator<RecipeItemStack> it=instance.recipeWeaving.iterator();
        while(it.hasNext()){
            RecipeItemStack r=it.next();
            if(r.isMatch(m) && m.stackSize>=r.getMaterial().stackSize) return r.getProduct();
        }
        return null;
    }
    public static ItemStack consumeWeavingMaterial(ItemStack m){
        int[] materialIds=OreDictionary.getOreIDs(m);
        Iterator<RecipeItemStack> it=instance.recipeWeaving.iterator();
        while(it.hasNext()){
            RecipeItemStack r=it.next();

            if(r.isMatch(m)){
                m.stackSize-=r.getMaterial().stackSize;
                if(m.stackSize<=0)  return null;
                else                 return m;
            }
        }
        return m;
    }
    public static ArrayList<RecipeItemStack> getWeaving(){ return instance.recipeWeaving; }

    // #############################################################
    //                    燻製レシピ
    // #############################################################
    public static RecipeItemStack cachedSmoking;
    public static void addSmoking(ItemStack m, ItemStack p){
        addSmoking(new RecipeItemStack(m, p));
    }
    public static void addSmoking(RecipeItemStack r){
        instance.recipeSmoking.add(r);
    }
    public static ItemStack getSmoking(ItemStack m){
        //キャッシュ
        if(cachedSmoking!=null && cachedSmoking.isMatch(m)) return cachedSmoking.getProduct();

        //レシピ
        Iterator<RecipeItemStack> it=instance.recipeSmoking.iterator();
        while(it.hasNext()){
            RecipeItemStack r=it.next();

            if(r.isMatch(m)){
                cachedSmoking=r;
                return r.getProduct();
            }

            /*
            if(OreDictionary.itemMatches(r.getKey(), m, false)){
                return r.getValue();
            }

            int[] productIds=OreDictionary.getOreIDs(r.getKey());
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return r.getValue();
                    }
                }
            }
            */
        }
        return null;
    }
    public static ArrayList<RecipeItemStack> getSmoking(){ return instance.recipeSmoking; }
    public static short GetSmokingFuelBurnTime(ItemStack itemStack){
        if(itemStack==null) return 0;
        Item item=itemStack.getItem();

        if(item instanceof ItemBlock){
            //原木・木材なら問答無用
            Block b=((ItemBlock) item).field_150939_a;

            if(b instanceof BlockLog) return 20*125;
            if(b instanceof BlockWood) return 20*33;
            if(b instanceof BlockLeaves) return 20*12;
            if(b instanceof BlockSapling) return 20*12;
        }

        if(item==ItemCore.dust && itemStack.getItemDamage()==11){
            //木材の粉はちょっとお得
            return 20*45;
        }

        return 0;
    }

    // #############################################################
    //                    ミキサー
    // #############################################################
    public static Map.Entry<Object[], Object> cachedMixing;
    public static void addMixing(Object p, Object ... m){
        int itemCount=0, fluidCount=0;

        for(int i=0;i<m.length;i++){
            //1つのレシピ中で素材が重複しないかチェック
            if(m[i] instanceof ItemStack){
                ItemStack is1=(ItemStack)m[i];
                itemCount++;
                if(itemCount==3){
                    IR2.logger.error("Error Adding Mixing Recipe:Too many ItemStack");
                    return;
                }

                for(int k=i+1;k<m.length;k++){
                    if(m[k] instanceof ItemStack){
                        ItemStack is2=(ItemStack)m[k];

                        if(IsItemEquals(is1, is2, null)){
                            IR2.logger.error("Error Adding Mixing Recipe:Duplicate material "+is1.getUnlocalizedName()+","+is2.getUnlocalizedName());
                            return;
                        }
                    }
                }
            }
            else if(m[i] instanceof FluidStack){
                FluidStack fs1=(FluidStack)m[i];
                fluidCount++;
                if(fluidCount==3){
                    IR2.logger.error("Error Adding Mixing Recipe:Too many FluidStack");
                    return;
                }

                for(int k=i+1;k<m.length;k++){
                    if(m[k] instanceof FluidStack){
                        FluidStack fs2=(FluidStack)m[k];

                        if(fs1.isFluidEqual(fs2)){
                            IR2.logger.error("Error Adding Mixing Recipe:Duplicate material "+fs1.getUnlocalizedName()+","+fs2.getUnlocalizedName());
                            return;
                        }
                    }
                }
            }
            else{
                //ここにくるってことは...
                IR2.logger.error("Error Adding Mixing Recipe: Invalid material "+m[i].toString());
                return;
            }
        }

        instance.recipeMixing.put(m, p);
    }
    public static Object getMixing(Object[] m){
        //キャッシュ
        if(cachedMixing!=null && _compare(cachedMixing.getKey(), m)) return cachedMixing.getValue();

        //レシピ
        Iterator<Map.Entry<Object[], Object>> it=instance.recipeMixing.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Object[], Object> r=it.next();

            if(_compare(r.getKey(), m)){
                cachedMixing=r;
                return r.getValue();
            }
        }

        return null;
    }
    public static void consumeMaterialForMixing(Object[] m){
        Map.Entry<Object[], Object> r=null;

        if(cachedMixing!=null && _compare(cachedMixing.getKey(), m)) r=cachedMixing;
        if(r==null){
            //レシピ
            Iterator<Map.Entry<Object[], Object>> it=instance.recipeMixing.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Object[], Object> re=it.next();

                if(_compare(re.getKey(), m)){
                    r=cachedMixing=re;
                    break;
                }
            }
        }

        //実際に消費する
        Object[] rr=r.getKey();
        for(int i=0;i<rr.length;i++){
            if(rr[i] instanceof ItemStack){
                for(int k=0;k<m.length;k++){
                    if(!(m[k] instanceof ItemStack)) continue;

                    ItemStack m1=(ItemStack)m[k];
                    ItemStack r1=(ItemStack)rr[i];
                    if(Recipes.IsItemEquals(m1, r1, null)){
                        ((ItemStack) m[k]).stackSize-=((ItemStack)rr[i]).stackSize;
                        break;
                    }
                }
            }
            else if(rr[i] instanceof FluidStack){
                for(int k=0;k<m.length;k++){
                    if(!(m[k] instanceof FluidStack)) continue;

                    FluidStack m1=(FluidStack)m[k];
                    FluidStack r1=(FluidStack)rr[i];
                    if(m1.isFluidEqual(r1)){
                        ((FluidStack) m[k]).amount-=((FluidStack)rr[i]).amount;
                        break;
                    }
                }
            }
        }
    }
    public static boolean _compare(Object[] recipe, Object[] m2){
        if(recipe.length!=m2.length) return false; //長さが違えば絶対違う

        boolean[] flag=new boolean[recipe.length];
        for(int i=0;i<flag.length;i++) flag[i]=false;

        for(int i=0;i<recipe.length;i++){
            boolean found=false;

            if(recipe[i] instanceof ItemStack){
                // ItemStackの比較
                ItemStack is1=(ItemStack)recipe[i];

                for(int k=0;k<m2.length;k++){
                    if(flag[k]) continue;
                    if(m2[k] instanceof ItemStack){
                        ItemStack is2=(ItemStack)m2[k];
                        if(IsItemEquals(is1, is2, null) && is1.stackSize<=is2.stackSize){
                            flag[k]=true;
                            found=true;
                            break;
                        }
                    }
                }
            }
            if(recipe[i] instanceof FluidStack){
                // FluidStackの比較
                FluidStack fs1=(FluidStack)recipe[i];

                for(int k=0;k<m2.length;k++){
                    if(flag[k]) continue;
                    if(m2[k] instanceof FluidStack){
                        FluidStack fs2=(FluidStack)m2[k];
                        if(fs1.isFluidEqual(fs2) && fs1.amount<=fs2.amount){
                            flag[k]=true;
                            found=true;
                            break;
                        }
                    }
                }
            }

            if(!found) return false;
        }

        return true;
    }
    public static HashMap<Object[], Object> getMixing(){ return instance.recipeMixing; }

    public static Tuple cachedHarvesting;
    public static void addHarvestTarget(Block b, int m){
        instance.harvestTargets.add(new Tuple(b, m));
    }
    public static boolean isHarvestTarget(Block block, int meta) {
        //キャ
        if(cachedHarvesting!=null && cachedHarvesting.getFirst()==block && cachedHarvesting.getSecond().equals(meta)) return true;

        //レ
        Iterator<Tuple> it = instance.harvestTargets.iterator();
        while (it.hasNext()) {
            Tuple t=it.next();
            if(t.getFirst()==block && (t.getSecond().equals(meta) || t.getSecond().equals(OreDictionary.WILDCARD_VALUE))){
                cachedHarvesting=t;
                return true;
            }
        }
        return false;
    }

    public static Block cachedWood, cachedLeave;
    public static void addWood(Block block){
        instance.woods.add(block);
    }
    public static boolean isWood(Block block){
        if(cachedWood!=null && cachedWood==block) return true;

        if(block instanceof BlockLog){
            cachedWood=block;
            return true;
        }

        Iterator<Block> it=instance.woods.iterator();
        while(it.hasNext()){
            if(block == it.next()){
                cachedWood=block;
                return true;
            }
        }
        return false;
    }
    public static void addLeave(Block block){
        instance.leaves.add(block);
    }
    public static boolean isLeave(Block block) {
        if(cachedLeave!=null && cachedLeave==block) return true;

        if (block instanceof BlockLeaves) {
            cachedLeave=block;
            return true;
        }

        Iterator<Block> it = instance.leaves.iterator();
        while (it.hasNext()) {
            if (block == it.next()){
                cachedLeave=block;
                return true;
            }
        }
        return false;
    }

    public static Map.Entry<ItemStack, String> cachedMob;
    public static void addMob(ItemStack m, String p){
        instance.mobs.put(m, p);
    }
    public static String getMob(ItemStack m){
        int[] materialIds=OreDictionary.getOreIDs(m);

        //キャ
        if(cachedMob!=null && IsItemEquals(cachedMob.getKey(), m, materialIds)) return cachedMob.getValue();

        //レ
        Iterator<Map.Entry<ItemStack, String>> it=instance.mobs.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ItemStack, String> r=it.next();

            if(IsItemEquals(r.getKey(), m, materialIds)){
                cachedMob=r;
                return r.getValue();
            }
            /*
            if(r.getKey().isItemEqual(m)){
                return r.getValue();
            }

            int[] productIds=OreDictionary.getOreIDs(r.getKey());
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return r.getValue();
                    }
                }
            }
            */
        }
        return null;
    }

    public static ItemStack cachedOre, cachedStone;
    public static void addOre(Block b, int meta){
        instance.ores.add(new Tuple(b, meta));
    }
    public static boolean isOre(Block b, int meta){
        ItemStack m=new ItemStack(b, 1, meta);
        int[] materialIds=OreDictionary.getOreIDs(m);

        //キャ
        if(cachedOre!=null && IsItemEquals(cachedOre, m, materialIds)) return true;

        //汎用処理 "ore"
        for(Integer id : materialIds){
            String name=OreDictionary.getOreName(id);
            if(name.startsWith("ore") && name.length()>3){
                cachedOre=m;
                return true;
            }
        }

        //レ
        Iterator<Tuple> it=instance.ores.iterator();
        while(it.hasNext()){
            Tuple r=it.next();
            ItemStack t=new ItemStack((Block)r.getFirst(), 1, (Integer)r.getSecond());

            if(IsItemEquals(t, m, materialIds)){
                cachedOre=t;
                return true;
            }

            /*
            if(m.isItemEqual(t)){
                return true;
            }

            int[] productIds=OreDictionary.getOreIDs(t);
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return true;
                    }
                }
            }
            */
        }
        return false;
    }
    public static void addStone(Block b, int meta){
        instance.stones.add(new Tuple(b, meta));
    }
    public static boolean isStone(Block b, int meta){
        ItemStack m=new ItemStack(b, 1, meta);
        int[] materialIds=OreDictionary.getOreIDs(m);

        //キャ
        if(cachedStone!=null && IsItemEquals(cachedStone, m, materialIds)) return true;

        //レ
        Iterator<Tuple> it=instance.stones.iterator();
        while(it.hasNext()){
            Tuple r=it.next();
            ItemStack t=new ItemStack((Block)r.getFirst(), 1, (Integer)r.getSecond());

            if(IsItemEquals(t, m, materialIds)){
                cachedStone=t;
                return true;
            }

            /*
            if(m.isItemEqual(t)){
                return true;
            }

            int[] productIds=OreDictionary.getOreIDs(t);
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return true;
                    }
                }
            }
            */
        }
        return false;
    }

    public static void addDying(Item item){
        instance.recipeDying.add(item);
    }
    public static boolean isDyingGoods(ItemStack item){
        return instance.recipeDying.contains(item.getItem());
    }


    // #############################################################
    //                    漁獲テーブル
    // #############################################################

    // get～の引数probは釣りに成功する確率
    public static void addFishing(ItemStack p, int w){
        instance.itemFishing.add(p, w);
    }
    public static ItemStack getFishing(float prob){
        return instance.itemFishing.get(prob);
    }
    public static void addFishingSea(ItemStack p, int w){
        instance.itemFishingSea.add(p, w);
    }
    public static ItemStack getFishingSea(float prob){
        return instance.itemFishingSea.get(prob);
    }
    public static boolean addFishingFromOreDictionary(String name, int w, int ws){
        ArrayList<ItemStack> tmpList=OreDictionary.getOres(name);
        if(!tmpList.isEmpty()){
            ItemStack t=tmpList.get(0);
            if(w>0) addFishing(t.copy(), w);
            if(ws>0) addFishingSea(t.copy(), ws);
            if(w>0 || ws>0){
                //FMLLog.info("Added " + name + " to Fishing Item Table");
                return true;
            }
        }
        return false;
    }


    // #############################################################
    //                    合成テーブル
    // #############################################################

    /**
     * ユニークな合成テーブルのIDを得る
     * ここで得た合成テーブルIDを用いて合成アイテムを登録することで、
     * そのmod独自の合成テーブルを使用する事が出来る
     * @param tableName NEIで表示されるテーブル名
     * @return
     */
    public static int getUniqueIdForBuilding(String tableName){
        uniqueForBuilding++;
        instance.buildingTableNames.put(uniqueForBuilding-1, tableName);
        return uniqueForBuilding-1;
    }
    public void _addBI(int t, ItemStack item, int w, int v){
        buildingItems.add(new BuildingPair(t, item, w, v));
    }
    public void _addBI(int t, String oreName, int w, int v){
        buildingItems.add(new BuildingPair(t, oreName, w, v));
    }

    /**
     * 合成テーブルにアイテムを登録する
     * @param t 合成テーブルID
     * @param item 登録アイテムスタック
     * @param w 出現の重み
     * @param v 価値
     */
    public static void addBuildingItem(int t, ItemStack item, int w, int v){
        instance._addBI(t, item, w, v);
    }
    public static void addBuildingItem(int t, String oreName, int w, int v){
        instance._addBI(t, oreName, w, v);
    }

    /**
     * 合成時に付与されるエンチャントのLvを登録する
     * @param item 登録アイテムスタック
     * @param lv そのアイテム1個あたりの経験値Lv(スティーブのLvと同じ)
     */
    public static void addBuildingEnchantItem(ItemStack item, int lv){
        instance.buildingEncahntItems.put(item, lv);
    }
    public static boolean addBuildingFromOreDictionary(int type, String name, int w, int v){
        ArrayList<ItemStack> tmpList=OreDictionary.getOres(name);
        if(!tmpList.isEmpty()){
            ItemStack t=tmpList.get(0);
            if(w>0 && v>0){
                addBuildingItem(type, name, w, v);
                //FMLLog.info("Added " + name + " to Composition Table");
                instance.additionalBuildingCount++;
                return true;
            }
        }
        return false;
    }
    public static BuildingPair getBuildingPair(ItemStack item){
        //int[] materialIds=OreDictionary.getOreIDs(item);
        Iterator<BuildingPair> it=instance.buildingItems.iterator();
        while(it.hasNext()) {
            BuildingPair r = it.next();

            if (r.isMatch(item)) {
                return r;
            }
        }

        /*
        it=instance.buildingItems.iterator();
        while(it.hasNext()){
            BuildingPair r = it.next();
            int[] productIds=OreDictionary.getOreIDs(r.itemstack);

            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return r;
                    }
                }
            }
        }
        */
        return null;
    }
    public static int getBuildingEnchantLevel(ItemStack item){
        //int[] materialIds=OreDictionary.getOreIDs(item);
        Iterator<Map.Entry<ItemStack, Integer>> it=instance.buildingEncahntItems.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ItemStack, Integer> r=it.next();

            if(OreDictionary.itemMatches(r.getKey(), item, false)){
                return r.getValue();
            }

            /*
            int[] productIds=OreDictionary.getOreIDs(r.getKey());
            for(int i=0;i<productIds.length;i++){
                for(int j=0;j<materialIds.length;j++){
                    if(productIds[i]==materialIds[j]){
                        return r.getValue();
                    }
                }
            }
            */
        }
        return 0;
    }
    public static ItemStack[] getBuildingResults(ItemStack[] mat, int invSize){
        if(mat==null || mat.length==0) return new ItemStack[0];

        LinkedList<ItemStack> results=new LinkedList<ItemStack>();
        HashMap<Integer, Integer> types=new HashMap<Integer, Integer>();
        int valueSum=0;
        int enchantSum=0;
        int weightSum=0;
        int type= IR3RecipeAPI.COMPOSITION_OTHER;

        //materials
        for(int i=0;i<mat.length;i++) {
            if (mat[i] == null) continue;

            //Value
            BuildingPair pair = getBuildingPair(mat[i]);
            if (pair != null) {
                valueSum += pair.value * mat[i].stackSize;
                if (!types.containsKey(pair.type)) {
                    types.put(pair.type, 1);
                } else {
                    int v = types.get(pair.type);
                    types.remove(pair.type);
                    types.put(pair.type, v + 1);
                }
            } else {
                valueSum += mat[i].stackSize;
                if (!types.containsKey(IR3RecipeAPI.COMPOSITION_OTHER)) {
                    types.put(IR3RecipeAPI.COMPOSITION_OTHER, 1);
                } else {
                    int v = types.get(IR3RecipeAPI.COMPOSITION_OTHER);
                    types.remove(IR3RecipeAPI.COMPOSITION_OTHER);
                    types.put(IR3RecipeAPI.COMPOSITION_OTHER, v + 1);
                }
            }

            //Enchant
            int enc=getBuildingEnchantLevel(mat[i]);
            enchantSum+=enc*mat[i].stackSize;
        }
        if(valueSum<=0) valueSum=1;
        valueSum=(int)MathHelper.ceiling_double_int(valueSum * 0.2);

        //Decide Building Types
        int tNum=0;
        Iterator<Map.Entry<Integer, Integer>> itType=types.entrySet().iterator();
        while(itType.hasNext()){
            Map.Entry<Integer, Integer> t=itType.next();
            if(tNum<t.getValue()){
                type=t.getKey();
                tNum=t.getValue();
            }
        }

        //Generate Available Item List
        LinkedList<BuildingPair> available=new LinkedList<BuildingPair>();
        Iterator<BuildingPair> itBuild=instance.buildingItems.iterator();
        while(itBuild.hasNext()){
            BuildingPair p=itBuild.next();
            if(p.type==type && p.value<=valueSum) {
                available.add(p);
                weightSum+=p.weight;
            }
        }

        //Generate Results
        Random random=new Random();
        for(int i=0;i<invSize && valueSum>0;i++) {
            Iterator<BuildingPair> itAvailable = available.iterator();
            int randValue = random.nextInt(weightSum);
            int sum = 0;

            while (itAvailable.hasNext()) {
                BuildingPair p = itAvailable.next();
                sum += p.weight;
                if (randValue <= sum) {
                    //Decide StackSize
                    int stMax = valueSum / p.value;
                    ItemStack is=p.getMaterial();
                    if(stMax>is.getMaxStackSize()) stMax=is.getMaxStackSize();
                    ItemStack rr = new ItemStack(is.getItem(), 1 + (stMax>0?random.nextInt(stMax):0), is.getItemDamage());
                    if(is.hasTagCompound()){
                        rr.setTagCompound((NBTTagCompound)is.getTagCompound().copy());
                    }

                    //ワイルドカードかどうか
                    if(rr.getItemDamage()==OreDictionary.WILDCARD_VALUE){
                        rr.setItemDamage(0);
                    }

                    //Tool Enchantment
                    if(enchantSum>0 && rr.isItemEnchantable() && !rr.isItemEnchanted() && random.nextFloat()<0.8f){
                        int eMax=enchantSum;
                        if(eMax>30) eMax=30;

                        int use=1+random.nextInt(eMax);
                        EnchantmentHelper.addRandomEnchantment(random, rr, use);
                        enchantSum-=use;
                    }

                    valueSum -= rr.stackSize * p.value;
                    results.add(rr);
                    break;
                    //If you want to equivalent exchanging ... add codes more.
                }
            }
        }

        if(results.isEmpty()){
            results.add(new ItemStack(Blocks.dirt));
        }

        ItemStack[] resultArrays=new ItemStack[results.size()];
        results.toArray(resultArrays);
        return resultArrays;
    }
    public static ArrayList<BuildingPair> getBuildingItems(){ return instance.buildingItems; }
    public static String getTableNameFromId(int id){ return instance.buildingTableNames.get(id); }

    // #############################################################
    //                    ユニファイア
    // #############################################################
    public static Map.Entry<Integer, ItemStack> cachedOreID;
    public static void addUnifierName(String name, ItemStack item){
        instance.unifierItems.put(OreDictionary.getOreID(name), item);
    }
    public static void addUnifierName(String name){
        ArrayList<ItemStack> list=OreDictionary.getOres(name);
        if(!list.isEmpty()){
            addUnifierName(name, list.get(0));
        }
    }
    public static ItemStack unifierItem(ItemStack item){
        int[] ids=OreDictionary.getOreIDs(item);

        if(cachedOreID!=null){
            for(int i=0;i<ids.length;i++){
                if(cachedOreID.getKey()==ids[i]){
                    return cachedOreID.getValue();
                }
            }
        }

        for(Map.Entry<Integer, ItemStack> it : instance.unifierItems.entrySet()){
            for(int i=0;i<ids.length;i++){
                if(it.getKey()==ids[i]){
                    cachedOreID=it;
                    return it.getValue();
                }
            }
        }

        return null;
    }


    private class FishableTable{
        private LinkedList<Fishable> table=new LinkedList<Fishable>();
        private int weightSum;

        private void add(ItemStack p, int w){
            table.add(new Fishable(p, w));
            weightSum+=w;
        }
        private ItemStack get(float prob){
            if(random.nextFloat()>prob)    return null;

            Iterator<Fishable> it= table.iterator();
            int r=random.nextInt(weightSum);
            int sum=0;
            while(it.hasNext()){
                Fishable f=it.next();
                sum+=f.weight;
                if(r<=sum){
                    return f.itemStack.copy();
                }
            }

            return null;
        }
        private class Fishable{
            private ItemStack itemStack;
            private int weight;

            private Fishable(ItemStack itemstack, int weight){
                itemStack=itemstack;
                this.weight=weight;
            }
        }
    }
    public class BuildingPair{
        public boolean isOre;
        public ItemStack itemstack;
        public int oreId;

        public int type;
        public int weight;
        public int value;

        private BuildingPair(int t, ItemStack item, int w, int v){
            type=t;
            isOre=false;
            itemstack=item;
            weight=w;
            value=v;

            if(itemstack==null || itemstack.getItem()==null){
                throw new IllegalArgumentException();
            }
        }
        private BuildingPair(int t, String id, int w, int v){
            type=t;
            isOre=true;
            oreId=OreDictionary.getOreID(id);
            weight=w;
            value=v;
        }

        public boolean isMatch(ItemStack itemStack){
            if(isOre){
                int[] names=OreDictionary.getOreIDs(itemStack);
                for(int i=0;i<names.length;i++){
                    if(oreId ==names[i]) return true;
                }
                return false;
            }
            else{
                return OreDictionary.itemMatches(itemstack, itemStack, false);
            }
        }
        public ItemStack getMaterial(){
            if(isOre){
                if(itemstack==null){
                    ArrayList<ItemStack> list=OreDictionary.getOres(OreDictionary.getOreName(oreId));
                    if(!list.isEmpty()) itemstack=list.get(0).copy();
                }
                return  itemstack;
            }
            else{
                return itemstack;
            }
        }
    }

    /**
     * レシピ判定用クラス
     */
    public static class RecipeItemStack{
        protected boolean isOre;

        protected ItemStack mat;

        protected int matId;
        protected int matAmount;

        protected ItemStack mat2itemStack;

        protected ItemStack prod;

        public RecipeItemStack(ItemStack mat, ItemStack prod){
            this.mat=mat;
            this.prod=prod;
            this.isOre=false;
        }
        public RecipeItemStack(String mat, ItemStack prod){
            this.matId =OreDictionary.getOreID(mat);
            this.matAmount=1;
            this.prod=prod;
            this.isOre=true;
        }
        public RecipeItemStack(String mat, int amount, ItemStack prod){
            this.matId =OreDictionary.getOreID(mat);
            this.matAmount=amount;
            this.prod=prod;
            this.isOre=true;
        }

        public boolean isMatch(ItemStack stack){
            if(isOre){
                int[] names=OreDictionary.getOreIDs(stack);
                for(int i=0;i<names.length;i++){
                    if(matId ==names[i]) return true;
                }
                return false;
            }
            else{
                return OreDictionary.itemMatches(mat, stack, false);
            }
        }

        public ItemStack getProduct(){
            return prod;
        }
        public ItemStack getMaterial(){
            if(isOre){
                if(mat2itemStack==null){
                    ArrayList<ItemStack> list=OreDictionary.getOres(OreDictionary.getOreName(matId));
                    if(!list.isEmpty()) mat2itemStack=list.get(0).copy();
                }
                mat2itemStack.stackSize=matAmount;
                return  mat2itemStack;
            }
            else{
                return mat;
            }
        }

        public static class CrushingRecipeItemStack extends RecipeItemStack{
            protected Object[] xxx;
            protected float[] prob;
            protected ItemStack[] prods;

            public CrushingRecipeItemStack(ItemStack mat, Object[] prod){
                super(mat, null);

                xxx=prod;
                prob=new float[prod.length/2];
                prods=new ItemStack[prod.length/2];
                for(int i=0;i<prob.length;i++){
                    prob[i]=(Float)prod[i*2];
                    prods[i]=(ItemStack)prod[i*2+1];
                }
            }
            public CrushingRecipeItemStack(String mat, Object[] prod){
                super(mat, null);

                xxx=prod;
                prob=new float[prod.length/2];
                prods=new ItemStack[prod.length/2];
                for(int i=0;i<prob.length;i++){
                    prob[i]=(Float)prod[i*2];
                    prods[i]=(ItemStack)prod[i*2+1];
                }
            }

            public int getProductAmount(){ return prods.length; }
            public ItemStack getProduct(int i){
                if(i<0 || i>=prods.length) i=0;
                return prods[i];
            }
            public float getProbability(int i){
                if(i<0 || i>=prob.length) i=0;
                return prob[i];
            }

            public Object[] getProducts(){
                return xxx;
            }

            @Override
            public ItemStack getProduct(){ return getProduct(0); }
        }
        public static class ExtractingRecipeItemStack extends RecipeItemStack{
            public ExtractingRecipeItemStack(ItemStack mat, int amt){
                super(mat, new ItemStack(Items.redstone, amt));
            }
            public ExtractingRecipeItemStack(String mat, int amt){
                super(mat, new ItemStack(Items.redstone, amt));
            }

            public int getProductAmount(){ return getProduct().stackSize; }
        }
    }

}
