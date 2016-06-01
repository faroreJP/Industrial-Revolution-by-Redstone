package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/03/09.
 */
public class ItemBag extends Item {
    public Random rand = new Random();

    public ItemBag() {
        setUnlocalizedName("IR2bag");
        setTextureName(IR2.MODID + ":bag");
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
    }

    public NBTTagCompound getNBT(ItemStack item) {
        if (!item.hasTagCompound()) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.setTagCompound(nbt);
        }

        return item.getTagCompound();
    }

    public void setColor(ItemStack item) {
        NBTTagCompound nbt = getNBT(item);
        nbt.setInteger("Color", rand.nextInt(0xffffff));
    }

    public int getColor(ItemStack item) {
        NBTTagCompound nbt = getNBT(item);
        return nbt.getInteger("Color");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            setColor(itemStack);
            itemStack.getTagCompound().setTag("Items", new NBTTagList());
            if (rand.nextFloat() < 0.5f) {
                itemStack.setItemDamage(1 + rand.nextInt(5));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        if (IR2.enableDescription) {
            list.add(I18n.format("info.bag.0"));
            if (itemStack.getItemDamage() > 0) {
                list.add(I18n.format("info.IR2words." + (itemStack.getItemDamage() - 1)));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        if(par1ItemStack.hasTagCompound()) {
            return (getColor(par1ItemStack) & 0xffffff);
        }
        else{
            return 0xffffff;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(IR2.instance, IR2.GUI_ID_BAG, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        return itemStack;
    }
}
