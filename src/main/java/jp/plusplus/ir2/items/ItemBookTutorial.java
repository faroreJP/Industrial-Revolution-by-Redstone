package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.Recipes;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/05/14.
 */
public class ItemBookTutorial extends ItemEditableBook {
    public ItemBookTutorial() {
        setUnlocalizedName("IR2bookTutorial");
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
    }

    protected void writeNBT(ItemStack itemStack) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("title", StatCollector.translateToLocal("book.IR2tutorial.title"));
        nbt.setString("author", "F(@plusplus_san)");

        NBTTagList pages=new NBTTagList();
        for(int i=0;i<=9;i++){
            pages.appendTag(new NBTTagString(StatCollector.translateToLocal("book.IR2tutorial.p" + i).replace('$', '\n')));
        }
        nbt.setTag("pages", pages);

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
        par1ItemStack.func_150996_a(ItemCore.bookTutorial);
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