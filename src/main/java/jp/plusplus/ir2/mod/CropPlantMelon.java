package jp.plusplus.ir2.mod;

import com.InfinityRaider.AgriCraft.farming.cropplant.CropPlantVanilla;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockCropMelonLapis;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/05.
 */

public class CropPlantMelon extends CropPlantVanilla {
    protected Random rand=new Random();
    protected BlockCropMelonLapis plant;

    public CropPlantMelon() {
        super(null, (ItemSeeds)ItemCore.seedMelonLapis);
        plant=(BlockCropMelonLapis)BlockCore.cropMelonLapis;
    }

    @Override
    public Block getBlock() {
        return this.plant;
    }

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> getAllFruits() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(ItemCore.melonLapis, 1));
        return ret;
    }

    @Override
    public ItemStack getRandomFruit(Random random) {
        return new ItemStack(ItemCore.melonLapis, 1);
    }

    @Override
    public ArrayList<ItemStack> getFruitsOnHarvest(int gain, Random rand) {
        int amount = (int)Math.ceil(((double)gain + 0.0D) / 3.0D);

        ArrayList list;
        for(list = new ArrayList(); amount > 0; --amount) {
            list.add(this.getRandomFruit(rand));
        }

        return list;
    }

    @Override
    public boolean canBonemeal() {
        return true;
    }

    @Override
    public float getHeight(int i) {
        return 0.8125F;
    }

    @Override
    public IIcon getPlantIcon(int i) {
        return BlockCore.cropMelonLapis.getIcon(0, i);
    }

    @Override
    public boolean renderAsFlower() {
        return false;
    }

    @Override
    public String getInformation() {
        String name = ItemCore.melonLapis.getUnlocalizedName();
        int index = name.indexOf(95);
        if(index < 0) {
            index = name.indexOf(46);
        }

        name = name.substring(index + 1);
        return "agricraft_journal." + name;
    }
}