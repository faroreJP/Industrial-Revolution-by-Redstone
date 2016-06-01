package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.blocks.*;
import jp.plusplus.ir2.tileentities.MultiBlockPosition;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ItemMachine extends ItemBlock {

    public ItemMachine(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (field_150939_a instanceof BlockMachineBase) {
            BlockMachineBase b = (BlockMachineBase) field_150939_a;

            if (IR2.enableDescription) {
                par3List.add(I18n.format("info.machine.0"));
                if (b.multiWidth > 1 || b.multiHeight > 1 || b.multiDepth > 1) {
                    par3List.add(I18n.format("info.machine.1"));
                }

                for (int i = 0; i < b.infoRow; i++) {
                    par3List.add(I18n.format("info." + b.infoName + "." + i));
                }
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input " + b.getMinRSS() + "RSS, " + b.getMinFrequency() + "Hz Min / " + b.getMaxRSS() + "RSS, " + b.getMaxFrequency() + "Hz Max"));
                if (b.multiWidth > 1 || b.multiDepth > 1 || b.multiHeight > 1)
                    par3List.add("Size:W" + b.multiWidth + ",D" + b.multiDepth + ",H" + b.multiHeight);
            }
            //par3List.add(EnumChatFormatting.RED + ("      " + b.getMaxRSS() + "RSS, " + b.getMaxFrequency() + "Hz Max"));
        } else if (field_150939_a instanceof BlockAlloySmelterRusty) {
            if (IR2.enableDescription) {
                for (int i = 0; i < 5; i++) {
                    par3List.add(I18n.format("info.alloySmelterRusty." + i));
                }
            }
        } else if (field_150939_a == BlockCore.modulator) {
            if (IR2.enableDescription) {
                for (int i = 0; i < 2; i++) {
                    par3List.add(I18n.format("info.modulator." + i));
                }
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input 1RSS, 1Hz Min / 2048RSS, 2048Hz Max"));
                par3List.add(EnumChatFormatting.RED + ("Output 2048RSS, 2048Hz Max"));
            }
        } else if (field_150939_a == BlockCore.amplifier) {
            if (IR2.enableDescription) {
                for (int i = 0; i < 2; i++) {
                    par3List.add(I18n.format("info.amplifier." + i));
                }
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input 1RSS, 1Hz Min / 2048RSS, 2048Hz Max"));
                par3List.add(EnumChatFormatting.RED + ("Output 2048RSS, 2048Hz Max"));
            }
        } else if (field_150939_a == BlockCore.relay) {
            if (IR2.enableDescription) {
                for (int i = 0; i < 1; i++) {
                    par3List.add(I18n.format("info.relay." + i));
                }
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input 1RSS, 1Hz Min / 2048RSS, 2048Hz Max"));
                par3List.add(EnumChatFormatting.RED + ("Output 2048RSS, 2048Hz Max"));
            }
        } else if (field_150939_a == BlockCore.regulator) {
            if (IR2.enableDescription) {
                for (int i = 0; i < 2; i++) {
                    par3List.add(I18n.format("info.regulator." + i));
                }
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input 1RSS, 1Hz Min / 2048RSS, 2048Hz Max"));
                par3List.add(EnumChatFormatting.RED + ("Output 15RSS, 0Hz Max"));
            }
        } else if (field_150939_a == BlockCore.machineShop) {
            if (IR2.enableDescription) {
                par3List.add(I18n.format("info.IR2shop." + 0));
            }
            if (IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Input 1RSS, 1Hz Min / 2048RSS, 2048Hz Max"));
            }
        } else if (field_150939_a == BlockCore.unifier) {
            par3List.add(I18n.format("info.IR2unifier.0"));
        } else if (field_150939_a instanceof BlockTank) {
            par3List.add(I18n.format("info.IR2" + ((BlockTank) field_150939_a).infoName + ".0"));
        }

    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int sideA, float hitX, float hitY, float hitZ, int metadata) {
        //IR2.logger.info("called");

        boolean isMulti = false;
        if (field_150939_a instanceof BlockMachineBase) {
            BlockMachineBase bm = (BlockMachineBase) field_150939_a;
            isMulti = bm.multiWidth > 1 || bm.multiHeight > 1 || bm.multiDepth > 1;
        }
        if (!isMulti) {
            //IR2.logger.info("block is not multi block");
            return super.placeBlockAt(stack, player, world, x, y, z, sideA, hitX, hitY, hitZ, metadata);
        }

        //-------------------------------------------------------------------------
        //              マルチブロック機械
        //-------------------------------------------------------------------------
        //step.0 向きの決定
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int side=2;
        if (l == 0) side=3;
        else if (l == 1) side=4;
        else if (l == 2) side=2;
        else if (l == 3) side=5;

        BlockMachineBase bm = (BlockMachineBase) field_150939_a;
        LinkedList<MultiBlockPosition> blocks = bm.getMultiBlockPositions(x, y, z, side);
        Block casing = bm.getCasingBlockType();
        Item itemCasing = Item.getItemFromBlock(casing);
        int cc = 0;

        //step.2 その座標に筐体は置けるか、必要な筐体のカウント
        boolean impossible = false;
        for (MultiBlockPosition pos : blocks) {
            if (!casing.canPlaceBlockAt(world, pos.x, pos.y, pos.z)) {
                //置けない場合、何処とも接続してない同じブロックか確認
                Block bbb = world.getBlock(pos.x, pos.y, pos.z);
                if (bbb == casing) {
                    TileEntityMultiBlock te = (TileEntityMultiBlock) world.getTileEntity(pos.x, pos.y, pos.z);
                    if (te.getMachineCore() != null) {
                        impossible = true;
                        break;
                    }
                } else {
                    impossible = true;
                    break;
                }
            } else {
                cc++;
            }
        }
        if (impossible) {
            //IR2.logger.info("placing is impossible");
            return false;
        }


        //step.1 インベントリ内の筐体が足りているか
        int ccb = cc;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack itemStack = player.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() == itemCasing) {
                ccb -= itemStack.stackSize;
                if (ccb <= 0) {
                    break;
                }
            }
        }
        if (ccb > 0) {
            //IR2.logger.info("not enough casings");
            return false;
        }


        //step.3 筐体を置く
        for (MultiBlockPosition pos : blocks) {
            if (casing.canPlaceBlockAt(world, pos.x, pos.y, pos.z)) {
                world.setBlock(pos.x, pos.y, pos.z, casing);
                //IR2.logger.info("placed at:"+pos.x+","+pos.y+","+pos.z);
            }
        }


        //step.4 筐体を消費する
        if (!player.capabilities.isCreativeMode) {
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack itemStack = player.inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() == itemCasing) {
                    if (itemStack.stackSize >= cc) {
                        itemStack.stackSize -= cc;
                        if (itemStack.stackSize <= 0) {
                            player.inventory.setInventorySlotContents(i, null);
                        }
                        cc = 0;
                    } else {
                        cc -= itemStack.stackSize;
                        player.inventory.setInventorySlotContents(i, null);
                    }
                    if (cc <= 0) {
                        break;
                    }
                }
            }
        }

        //step.5 機械を設置する
        if (!world.setBlock(x, y, z, field_150939_a, metadata, 3)) {
            //IR2.logger.info("wtf");
            return false;
        }

        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }

        //IR2.logger.info("placed");
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        //IR2.logger.info("used");

        Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
        if (!block.isReplaceable(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
            if (p_77648_7_ == 0) {
                --p_77648_5_;
            }

            if (p_77648_7_ == 1) {
                ++p_77648_5_;
            }

            if (p_77648_7_ == 2) {
                --p_77648_6_;
            }

            if (p_77648_7_ == 3) {
                ++p_77648_6_;
            }

            if (p_77648_7_ == 4) {
                --p_77648_4_;
            }

            if (p_77648_7_ == 5) {
                ++p_77648_4_;
            }
        }

        if (p_77648_1_.stackSize == 0) {
            //IR2.logger.info("0");
            return false;
        } else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)) {
            //IR2.logger.info("1");
            return false;
        } else if (p_77648_5_ == 255 && this.field_150939_a.getMaterial().isSolid()) {
            //IR2.logger.info("2");
            return false;
        } else if (p_77648_3_.canPlaceEntityOnSide(this.field_150939_a, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, p_77648_2_, p_77648_1_)) {
            int i1 = this.getMetadata(p_77648_1_.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);

            //IR2.logger.info("try");
            if (placeBlockAt(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1)) {
                p_77648_3_.playSoundEffect((double) ((float) p_77648_4_ + 0.5F), (double) ((float) p_77648_5_ + 0.5F), (double) ((float) p_77648_6_ + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --p_77648_1_.stackSize;
            }

            return true;
        } else {
            //IR2.logger.info("-1");
            return false;
        }
    }
}
