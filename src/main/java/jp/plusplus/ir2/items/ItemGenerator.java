package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ItemGenerator extends ItemBlock {
    public ItemGenerator(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(IR2.enableDescription) {
            par3List.add(I18n.format("info.generator.0"));
            par3List.add(I18n.format("info.generator.1"));
        }

        if(field_150939_a == BlockCore.generatorVLF){
            if(IR2.enableDescription) {
                par3List.add(I18n.format("info.generator.2"));
            }
            if(IR2.enableDescriptionOfRating) {
                par3List.add(EnumChatFormatting.RED + ("Output 16RSS, 4Hz Max"));
            }
        }
        else if(field_150939_a instanceof BlockGenerator){
            if(IR2.enableDescriptionOfRating) {
                BlockGenerator b = (BlockGenerator) field_150939_a;
                par3List.add(EnumChatFormatting.RED + ("Output " + b.getOutputRSS() + "RSS, " + b.getOutputFrequency() + "Hz Max"));
            }
        }
    }


}
