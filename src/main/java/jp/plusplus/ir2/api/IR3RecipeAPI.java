package jp.plusplus.ir2.api;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/09/06.
 * 外部から加工レシピ・漁獲・合成テーブルを弄る人用
 */
public class IR3RecipeAPI {
    public static final int COMPOSITION_OTHER =-1;
    public static final int COMPOSITION_PLANT =1;
    public static final int COMPOSITION_TOOL =2;
    public static final int COMPOSITION_FOOD =3;
    public static final int COMPOSITION_ORE =4;
    public static final int COMPOSITION_MAGIC =5;
    public static final int COMPOSITION_DEFAULT_WEIGHT =50;
    public static final int COMPOSITION_DEFAULT_VALUE =1;

    /**
     * RS機械の完成品スロットからアイテムを取り出した際の経験値量を設定する。
     * @param item 対象アイテム
     * @param exp 値はかまどと同じ感じ
     */
    public static void AddEXPItem(ItemStack item, float exp){
        Recipes.addEXP(item, exp);
    }

    /**
     * 紡績レシピを追加する。
     * @param input 素材アイテムスタック。実際の消費量はこいつのstackSize*各機械固有の倍率によって決定される(羊毛は1で登録されている)。
     * @param output 生成品
     */
    public static void AddSpinning(ItemStack input, ItemStack output){
        Recipes.addSpinning(input, output);
    }
    public static void AddSpinning(String input, ItemStack output){
        Recipes.addSpinning(new Recipes.RecipeItemStack(input, output));
    }

    /**
     * 織機の加工レシピを追加する。
     * @param input 素材アイテムスタック
     * @param output 生成品
     */
    public static void AddWeaving(ItemStack input, ItemStack output){
        Recipes.addWeaving(input, output);
    }
    public static void AddWeaving(String input, ItemStack output){
        Recipes.addWeaving(input, output);
    }

    /**
     * 合金レシピを追加する。相方はレッドストーン固定。
     * @param input 素材となる金属インゴット
     * @param output 生成品
     */
    public static void AddAlloying(ItemStack input, ItemStack output){
        Recipes.addAlloying(input, output);
    }
    public static void AddAlloying(String input, ItemStack output){
        Recipes.addAlloying(new Recipes.RecipeItemStack(input, output));
    }

    /**
     * 漁獲テーブルにアイテムを追加する。
     * @param fish 釣果アイテム
     * @param normal 通常のバイームでの出現重み(0<=)
     * @param sea 海洋バイームでの出現重み(0<=)
     */
    public static void AddFishing(ItemStack fish, int normal, int sea) {
        if (normal < 0 || sea < 0) {
            IR2.logger.error("Error:Adding fishing table:" + fish.getDisplayName() + "(nor" + normal + ",sea" + sea + ")");
            return;
        }
        if (normal > 0) Recipes.addFishing(fish, normal);
        if (sea > 0) Recipes.addFishingSea(fish, sea);
    }

    /**
     * ユニークな合成テーブルを作成する。
     * @param name 合成テーブルの登録名
     * @return
     */
    public static int GetUniqueCompositionTableID(String name){
        return Recipes.getUniqueIdForBuilding(name);
    }

    /**
     * 合成テーブルにアイテムを追加する
     * @param id 対象合成テーブルID
     * @param input 登録アイテムスタック
     * @param value そのアイテムの価値 (0<)
     * @param weight そのアイテムの重み (0<=)
     */
    public static void AddComposition(int id, ItemStack input, int value, int weight) {
        if (value <= 0 || weight < 0) {
            IR2.logger.error("Error Adding composition:" + "id" + id + "," + input.getDisplayName() + "(v" + value + ",w" + weight + ")");
            return;
        }
        Recipes.addBuildingItem(id, input, weight, value);
    }

    /**
     * 破砕レシピを追加する
     * @param input 素材アイテムスタック
     * @param output 確率(0<)と生成物のペア。最大3つまで。
     */
    public static void AddCrushing(ItemStack input, CrushPair ... output){
        if(output.length>3){
            IR2.logger.error("Error Adding crushing: too many output ... "+input.getDisplayName()+","+ output.length);
            return;
        }

        ArrayList<Object> list=new ArrayList<Object>(output.length*2);
        for (CrushPair cp : output){
            if(cp.probability<=0){
                IR2.logger.error("Error Adding crushing: zero probability ... "+input.getDisplayName()+","+ cp.toString());
                return;
            }
            list.add(cp.probability);
            list.add(cp.output);
        }

        Object[] obj=new Object[list.size()];
        list.toArray(obj);

        Recipes.addCrushing(input, obj);
    }

    /**
     * 抽出レシピを追加する
     * @param input 抽出対象アイテムスタック
     * @param output 抽出できるレッドストーンの個数(0<)
     */
    public static void AddExtracting(ItemStack input, int output){
        if(output<=0){
            IR2.logger.error("Error Adding extracting: output is zero ... "+input.getDisplayName());
            return;
        }

        Recipes.addExtracting(input, output);
    }
    public static void AddExtracting(String input, int output){
        if(output<=0){
            IR2.logger.error("Error Adding extracting: output is zero ... "+input);
            return;
        }

        Recipes.addExtracting(new Recipes.RecipeItemStack.ExtractingRecipeItemStack(input, output));
    }

    /**
     * 燻製レシピを追加する
     * @param input 素材
     * @param output 生成物
     */
    public static void AddSmoking(ItemStack input, ItemStack output){
        Recipes.addSmoking(input, output);
    }

    /**
     * ミキサーレシピを追加する
     * @param output 生成物。ItemStack or FluidStack
     * @param input 最大4個の素材。ItemStack or FluidStackで、各最大2つまで。
     */
    public static void AddMixing(Object output, Object ... input){
        Recipes.addMixing(output, input);
    }

    /**
     * 染色機の染色対象を追加する。
     * @param item 指定するアイテム。Blockを染めたい場合はItem.getItemFromBlockとかで。
     */
    public static void AddDyingGoods(Item item){
        Recipes.addDying(item);
    }

    /**
     * 収穫機の収穫対象を追加する。
     * ここで追加したブロックは、ブロックが破壊される形で収穫される。
     * @param target 対象ブロック
     * @param metadata 対象メタ値(0-15)
     */
    public static void AddHarvestTarget(Block target, int metadata){
        if(metadata<0 || metadata>15){
            IR2.logger.error("Error Adding harvest target:invalid metadata ... "+metadata);
            return;
        }
        Recipes.addHarvestTarget(target, metadata);
    }

    /**
     * 採掘機の採掘対象を追加する。
     * @param target 対象ブロック
     * @param metadata 対象メタ値(0-15)
     */
    public static void AddMiningTarget(Block target, int metadata){
        if(metadata<0 || metadata>15){
            IR2.logger.error("Error Adding harvest target:invalid metadata ... "+metadata);
            return;
        }
        Recipes.addOre(target, metadata);
    }

    /**
     * オートスポナーでスポーンできる生物を追加する。
     * @param input 素材
     * @param mobName 生物の名前
     */
    public static void AddSpawnableMob(ItemStack input, String mobName){
        Recipes.addMob(input, mobName);
    }

    /**
     * 破砕レシピ登録のためのクラス
     */
    public static final class CrushPair{
        public ItemStack output;
        public float probability;

        /**
         * 破砕アイテムの確率と生成物のペアを作成する
         * @param prob 出現確率(0<)
         * @param output 生成物
         */
        public CrushPair(float prob, ItemStack output){
            this.output=output;
            probability=prob;
        }

        @Override
        public String toString(){
            return "("+(int)(probability*100)+"%:"+output.toString()+")";
        }
    }
}
