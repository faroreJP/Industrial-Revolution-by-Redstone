package jp.plusplus.ir2.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.entity.EntityNeedle;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/09/08.
 */
public class ItemNeedleInjector extends Item {
    public ItemNeedleInjector() {
        setUnlocalizedName("IR2needleInjector");
        setTextureName(IR2.MODID + ":needleInjector");
        setCreativeTab(IR2.tabIR2);
        setFull3D();
        setMaxStackSize(1);
        setMaxDamage(250);
        setNoRepair();

        //onItemRightClick()
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        //--------------------------------------
        // インベントリ内のニードルを消費する
        //--------------------------------------
        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack) > 0;
        if(!flag){
            boolean found=false;
            InventoryPlayer inv=player.inventory;
            for(int i=0;i<inv.getSizeInventory();i++){
                ItemStack ndl=inv.getStackInSlot(i);
                if(ndl==null || ndl.getItem()!=ItemCore.needle) continue;

                found=true;
                ndl.stackSize--;
                if(ndl.stackSize<=0) inv.setInventorySlotContents(i, null);
                inv.markDirty();
                break;
            }

            if(!found) return itemStack;
        }

        //--------------------------------------
        // ニードルを射出する
        //--------------------------------------
        float angle=player.rotationYawHead / 180.0F * (float) Math.PI+(float)Math.PI/2.0f;
        float cos= MathHelper.cos(angle);
        float sin= MathHelper.cos(angle);
        if(!world.isRemote){
            for(int i=0;i<4;i++) {
                EntityNeedle bullet = new EntityNeedle(world, player, 0.8F, 25.0F, 0,0,0);
                bullet.setDamage(1.5);
                world.spawnEntityInWorld(bullet);
            }
        }

        //--------------------------------------
        // ダメージ
        //--------------------------------------
        if(!player.capabilities.isCreativeMode) {
            itemStack.damageItem(1, player);
        }

        return itemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (IR2.enableDescription) {
            par3List.add(ChatFormatting.GRAY + I18n.format("info.needleInjector.0"));
            par3List.add(ChatFormatting.RED + I18n.format("info.needleInjector.1"));
        }
    }
}
