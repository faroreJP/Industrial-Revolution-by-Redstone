package jp.plusplus.ir2.mod;

import com.InfinityRaider.AgriCraft.farming.cropplant.CropPlantVanilla;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockReedIron;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/05.
 */

public class CropPlantReed extends CropPlantVanilla {
    protected BlockReedIron plant;
    protected ItemReed seed;

    public CropPlantReed() {
        super(null, null);
        plant=(BlockReedIron)BlockCore.cropReedIron;
        seed=(ItemReed)ItemCore.reedIron;
    }

    @Override
    public Block getBlock() {
        return this.plant;
    }

    @Override
    public ItemStack getSeed() {
        return new ItemStack(this.seed);
    }

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> getAllFruits() {
        ArrayList list = new ArrayList();
        list.add(new ItemStack(ItemCore.reedIron));
        return list;
    }

    @Override
    public ItemStack getRandomFruit(Random random) {
        return new ItemStack(ItemCore.reedIron);
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
        return BlockCore.cropReedIron.getIcon(0, i);
    }

    @Override
    public boolean renderAsFlower() {
        return false;
    }

    @Override
    public String getInformation() {
        String name = ItemCore.reedIron.getUnlocalizedName();
        int index = name.indexOf(95);
        if(index < 0) {
            index = name.indexOf(46);
        }

        name = name.substring(index + 1);
        return "agricraft_journal." + name;
    }
}

