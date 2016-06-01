package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/22.
 */
public class ItemOxygen extends ItemFood{
    public int oxygen;

    public ItemOxygen(int ox) {
        super(1, 0.f, false);
        setUnlocalizedName("IR2canOxygen");
        setTextureName(IR2.MODID + ":canOxygen");
        setCreativeTab(IR2.tabIR2);
        oxygen=ox;
    }
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(IR2.enableDescription) {
            par3List.add(I18n.format("info.canOxygen.0"));
        }
    }
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(player.isInWater()) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }
        return stack;
    }
    @Override
    public boolean onItemUse(ItemStack p_onItemUse_1_, EntityPlayer p_onItemUse_2_, World p_onItemUse_3_, int p_onItemUse_4_, int p_onItemUse_5_, int p_onItemUse_6_, int p_onItemUse_7_, float p_onItemUse_8_, float p_onItemUse_9_, float p_onItemUse_10_) {
        return false;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            int air=player.getAir() + oxygen;
            if(air>300) air=300;
            player.setAir(air);

            //使用後は汎用缶に戻る
            if(!player.capabilities.isCreativeMode){
                ItemStack get=new ItemStack(ItemCore.can);
                if (!player.inventory.addItemStackToInventory(get)){
                    player.entityDropItem(get, 1);
                }
                player.inventory.markDirty();
            }
        }
        return super.onEaten(stack, world, player);
    }
    @Override
    public EnumAction getItemUseAction(ItemStack p_getItemUseAction_1_) {
        return EnumAction.drink;
    }

}
