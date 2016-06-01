package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/18.
 */
public class ItemKnife extends ItemSword{
    protected int maxDamageNBT;

    public ItemKnife(ToolMaterial par2EnumToolMaterial){
        super(par2EnumToolMaterial);
        setUnlocalizedName("IR2knife");
        setTextureName(IR2.MODID + ":knifeActive");
        setCreativeTab(IR2.tabIR2);
        setNoRepair();
        maxDamageNBT=par2EnumToolMaterial.getMaxUses();
        setMaxDamage(maxDamageNBT);
    }

    protected int getDamageNBT(ItemStack itemStack){
        if(!itemStack.hasTagCompound()){
            NBTTagCompound tag=new NBTTagCompound();
            tag.setInteger("k-Damage", 0);
            itemStack.setTagCompound(tag);
        }

        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getInteger("k-Damage");
    }
    protected void setDamageNBT(ItemStack itemStack, int d){
        if(itemStack.hasTagCompound()) {
            if(d>maxDamageNBT) d=maxDamageNBT;

            NBTTagCompound nbt = itemStack.getTagCompound();
            nbt.setInteger("k-Damage", d);
        }
    }
    protected void onBreakNBT(ItemStack itemStack, World world, EntityPlayer entity, int slot, boolean held){
        entity.renderBrokenItemStack(itemStack);
        entity.destroyCurrentEquippedItem();
    }


    @Override
    public EnumAction getItemUseAction(ItemStack p_getItemUseAction_1_) {
        return EnumAction.none;
    }


    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase entity){
        if(entity instanceof EntityPlayer) {
            if (!((EntityPlayer)entity).capabilities.isCreativeMode &&  itemStack.hasTagCompound()) {
                int d = getDamageNBT(itemStack) + 20;
                setDamageNBT(itemStack, d);
            }
        }
        return true;
    }
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean held) {
        if(world.isRemote || !held)   return;

        if(entity instanceof EntityPlayer) {
            EntityPlayer p=(EntityPlayer)entity;

            if(p.capabilities.isCreativeMode)   return;

            int d=getDamageNBT(itemStack)+1;
            setDamageNBT(itemStack, d);

            if(d>=maxDamageNBT){
                onBreakNBT(itemStack, world, p, slot, held);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(IR2.enableDescription) {
            par3List.add(I18n.format("info.knife.0"));
        }
    }
    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2){
        return false;
    }
    @Override
    public boolean isDamaged(ItemStack stack) {
        return getDamageNBT(stack) > 0;
    }
    @Deprecated
    @Override
    public int getDisplayDamage(ItemStack stack) {
        return getDamageNBT(stack);
    }
}
