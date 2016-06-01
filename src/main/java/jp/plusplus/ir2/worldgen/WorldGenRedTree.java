package jp.plusplus.ir2.worldgen;

import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/21.
 */
public class WorldGenRedTree  extends WorldGenAbstractTree {
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final int metaWood;
    private final int metaLeaves;

    private Block sapling;
    private Block log;
    private Block leave;

    public WorldGenRedTree(boolean p_i2027_1_, Block sapling, Block log, Block leave) {
        this(p_i2027_1_, 4, 0, 0, false);
        this.sapling=sapling;
        this.log=log;
        this.leave=leave;
    }

    public WorldGenRedTree(boolean p_i2028_1_, int p_i2028_2_, int p_i2028_3_, int p_i2028_4_, boolean p_i2028_5_) {
        super(p_i2028_1_);
        this.minTreeHeight = p_i2028_2_;
        this.metaWood = p_i2028_3_;
        this.metaLeaves = p_i2028_4_;
        this.vinesGrow = p_i2028_5_;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        int l = rand.nextInt(3) + this.minTreeHeight;
        boolean flag = true;
        if (y >= 1 && y + l + 1 <= 256) {
            byte b0;
            int k1;
            for (int block2 = y; block2 <= y + 1 + l; ++block2) {
                b0 = 1;
                if (block2 == y) {
                    b0 = 0;
                }

                if (block2 >= y + 1 + l - 2) {
                    b0 = 2;
                }

                for (int isSoil = x - b0; isSoil <= x + b0 && flag; ++isSoil) {
                    for (k1 = z - b0; k1 <= z + b0 && flag; ++k1) {
                        if (block2 >= 0 && block2 < 256) {
                            world.getBlock(isSoil, block2, k1);
                            if (!this.isReplaceable(world, isSoil, block2, k1)) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block var21 = world.getBlock(x, y - 1, z);
                boolean var22 = var21.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) sapling);
                if (var22 && y < 256 - l - 1) {
                    var21.onPlantGrow(world, x, y - 1, z, x, y, z);
                    b0 = 3;
                    byte b1 = 0;

                    int l1;
                    int i2;
                    int j2;
                    int i3;
                    for (k1 = y - b0 + l; k1 <= y + l; ++k1) {
                        i3 = k1 - (y + l);
                        l1 = b1 + 1 - i3 / 2;

                        for (i2 = x - l1; i2 <= x + l1; ++i2) {
                            j2 = i2 - x;

                            for (int k2 = z - l1; k2 <= z + l1; ++k2) {
                                int l2 = k2 - z;
                                if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || rand.nextInt(2) != 0 && i3 != 0) {
                                    Block block1 = world.getBlock(i2, k1, k2);
                                    if (block1.isAir(world, i2, k1, k2) || block1.isLeaves(world, i2, k1, k2)) {
                                        this.setBlockAndNotifyAdequately(world, i2, k1, k2, leave, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (k1 = 0; k1 < l; ++k1) {
                        Block block = world.getBlock(x, y + k1, z);
                        if (block.isAir(world, x, y + k1, z) || block.isLeaves(world, x, y + k1, z)) {
                            this.setBlockAndNotifyAdequately(world, x, y + k1, z, log, this.metaWood);
                            if (this.vinesGrow && k1 > 0) {
                                if (rand.nextInt(3) > 0 && world.isAirBlock(x - 1, y + k1, z)) {
                                    this.setBlockAndNotifyAdequately(world, x - 1, y + k1, z, Blocks.vine, 8);
                                }

                                if (rand.nextInt(3) > 0 && world.isAirBlock(x + 1, y + k1, z)) {
                                    this.setBlockAndNotifyAdequately(world, x + 1, y + k1, z, Blocks.vine, 2);
                                }

                                if (rand.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z - 1)) {
                                    this.setBlockAndNotifyAdequately(world, x, y + k1, z - 1, Blocks.vine, 1);
                                }

                                if (rand.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z + 1)) {
                                    this.setBlockAndNotifyAdequately(world, x, y + k1, z + 1, Blocks.vine, 4);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow) {
                        for (k1 = y - 3 + l; k1 <= y + l; ++k1) {
                            i3 = k1 - (y + l);
                            l1 = 2 - i3 / 2;

                            for (i2 = x - l1; i2 <= x + l1; ++i2) {
                                for (j2 = z - l1; j2 <= z + l1; ++j2) {
                                    if (world.getBlock(i2, k1, j2).isLeaves(world, i2, k1, j2)) {
                                        if (rand.nextInt(4) == 0 && world.getBlock(i2 - 1, k1, j2).isAir(world, i2 - 1, k1, j2)) {
                                            this.growVines(world, i2 - 1, k1, j2, 8);
                                        }

                                        if (rand.nextInt(4) == 0 && world.getBlock(i2 + 1, k1, j2).isAir(world, i2 + 1, k1, j2)) {
                                            this.growVines(world, i2 + 1, k1, j2, 2);
                                        }

                                        if (rand.nextInt(4) == 0 && world.getBlock(i2, k1, j2 - 1).isAir(world, i2, k1, j2 - 1)) {
                                            this.growVines(world, i2, k1, j2 - 1, 1);
                                        }

                                        if (rand.nextInt(4) == 0 && world.getBlock(i2, k1, j2 + 1).isAir(world, i2, k1, j2 + 1)) {
                                            this.growVines(world, i2, k1, j2 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        if (rand.nextInt(5) == 0 && l > 5) {
                            for (k1 = 0; k1 < 2; ++k1) {
                                for (i3 = 0; i3 < 4; ++i3) {
                                    if (rand.nextInt(4 - k1) == 0) {
                                        l1 = rand.nextInt(3);
                                        this.setBlockAndNotifyAdequately(world, x + Direction.offsetX[Direction.rotateOpposite[i3]], y + l - 5 + k1, z + Direction.offsetZ[Direction.rotateOpposite[i3]], Blocks.cocoa, l1 << 2 | i3);
                                    }
                                }
                            }
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

    private void growVines(World p_76529_1_, int x, int y, int z, int p_76529_5_) {
        this.setBlockAndNotifyAdequately(p_76529_1_, x, y, z, Blocks.vine, p_76529_5_);
        int i1 = 4;

        while (true) {
            --y;
            if (!p_76529_1_.getBlock(x, y, z).isAir(p_76529_1_, x, y, z) || i1 <= 0) {
                return;
            }

            this.setBlockAndNotifyAdequately(p_76529_1_, x, y, z, Blocks.vine, p_76529_5_);
            --i1;
        }
    }
}