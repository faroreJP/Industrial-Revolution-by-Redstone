package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/18.
 */
public class ItemKnifeMP extends ItemKnife{
    public ItemKnifeMP(ToolMaterial par2EnumToolMaterial){
        super(par2EnumToolMaterial);
        setUnlocalizedName("IR2knifeMP");
        setTextureName(IR2.MODID + ":knifeMPActive");
    }
    @Override
    protected void onBreakNBT(ItemStack itemStack, World world, EntityPlayer entity, int slot, boolean held){
        entity.inventory.setInventorySlotContents(slot, new ItemStack(ItemCore.knifeGrip));
    }

}
