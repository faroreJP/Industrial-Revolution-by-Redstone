package jp.plusplus.ir2.items;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/03/02.
 */
public class ItemBookRecipe extends ItemEditableBook {
    public ItemBookRecipe() {
        setUnlocalizedName("IR2bookRecipe");
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
    }

    protected void writeNBT(ItemStack itemStack) {
        NBTTagCompound nbt = new NBTTagCompound();
        Recipes.WriteRecipesToNBT(nbt);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        writeNBT(par1ItemStack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        writeNBT(par1ItemStack);
        par1ItemStack.func_150996_a(Items.written_book);
        par1ItemStack=super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
        par1ItemStack.func_150996_a(ItemCore.bookRecipe);
        return par1ItemStack;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = Items.written_book.getIconFromDamage(0);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(EnumChatFormatting.GRAY + String.format(StatCollector.translateToLocalFormatted("book.byAuthor", new Object[]{"F(@plusplus_san)"}), new Object[0]));
    }
}