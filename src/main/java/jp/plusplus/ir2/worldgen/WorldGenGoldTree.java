package jp.plusplus.ir2.worldgen;

import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/21.
 */
public class WorldGenGoldTree  extends WorldGenAbstractTree {
    private boolean field_150531_a;

    public WorldGenGoldTree(boolean p_i45449_1_, boolean p_i45449_2_) {
        super(p_i45449_1_);
        this.field_150531_a = p_i45449_2_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
        int l = p_76484_2_.nextInt(3) + 5;
        if(this.field_150531_a) {
            l += p_76484_2_.nextInt(7);
        }

        boolean flag = true;
        if(p_76484_4_ >= 1 && p_76484_4_ + l + 1 <= 256) {
            int j1;
            int k1;
            for(int block2 = p_76484_4_; block2 <= p_76484_4_ + 1 + l; ++block2) {
                byte isSoil = 1;
                if(block2 == p_76484_4_) {
                    isSoil = 0;
                }

                if(block2 >= p_76484_4_ + 1 + l - 2) {
                    isSoil = 2;
                }

                for(j1 = p_76484_3_ - isSoil; j1 <= p_76484_3_ + isSoil && flag; ++j1) {
                    for(k1 = p_76484_5_ - isSoil; k1 <= p_76484_5_ + isSoil && flag; ++k1) {
                        if(block2 >= 0 && block2 < 256) {
                            p_76484_1_.getBlock(j1, block2, k1);
                            if(!this.isReplaceable(p_76484_1_, j1, block2, k1)) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if(!flag) {
                return false;
            } else {
                Block var18 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);
                boolean var19 = var18.canSustainPlant(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
                if(var19 && p_76484_4_ < 256 - l - 1) {
                    var18.onPlantGrow(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, p_76484_3_, p_76484_4_, p_76484_5_);

                    int k2;
                    for(k2 = p_76484_4_ - 3 + l; k2 <= p_76484_4_ + l; ++k2) {
                        j1 = k2 - (p_76484_4_ + l);
                        k1 = 1 - j1 / 2;

                        for(int block3 = p_76484_3_ - k1; block3 <= p_76484_3_ + k1; ++block3) {
                            int l1 = block3 - p_76484_3_;

                            for(int i2 = p_76484_5_ - k1; i2 <= p_76484_5_ + k1; ++i2) {
                                int j2 = i2 - p_76484_5_;
                                if(Math.abs(l1) != k1 || Math.abs(j2) != k1 || p_76484_2_.nextInt(2) != 0 && j1 != 0) {
                                    Block block1 = p_76484_1_.getBlock(block3, k2, i2);
                                    if(block1.isAir(p_76484_1_, block3, k2, i2) || block1.isLeaves(p_76484_1_, block3, k2, i2)) {
                                        this.setBlockAndNotifyAdequately(p_76484_1_, block3, k2, i2, BlockCore.leaveGold, 0);
                                    }
                                }
                            }
                        }
                    }

                    for(k2 = 0; k2 < l; ++k2) {
                        Block var20 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + k2, p_76484_5_);
                        if(var20.isAir(p_76484_1_, p_76484_3_, p_76484_4_ + k2, p_76484_5_) || var20.isLeaves(p_76484_1_, p_76484_3_, p_76484_4_ + k2, p_76484_5_)) {
                            this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_ + k2, p_76484_5_, BlockCore.logGold, 0);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}