package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/05/23.
 */
public class ItemCanSoilent extends ItemFood {
    public ItemCanSoilent() {
        super(12, 0.1f, false);
        setPotionEffect(Potion.hunger.id, 30, 3, 0.75F);
        setUnlocalizedName("IR2canSoilent");
        setTextureName(IR2.MODID+":canSoilent");
        setCreativeTab(IR2.tabIR2);
    }

    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
        super.onFoodEaten(itemStack, world, player);

        if(!player.capabilities.isCreativeMode){
            ItemStack get=new ItemStack(ItemCore.can);
            if (!player.inventory.addItemStackToInventory(get)){
                player.entityDropItem(get, 1);
            }
            player.inventory.markDirty();
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(I18n.format("info.IR2soilent.0"));
    }
}
