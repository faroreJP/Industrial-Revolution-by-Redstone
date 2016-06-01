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
 * Created by plusplus_F on 2015/09/18.
 */
public class ItemCanYamatoni extends ItemFood {
    public ItemCanYamatoni() {
        super(8, 1.0f, false);
        setUnlocalizedName("IR2canYamatoni");
        setTextureName(IR2.MODID+":canYamatoni");
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
}
