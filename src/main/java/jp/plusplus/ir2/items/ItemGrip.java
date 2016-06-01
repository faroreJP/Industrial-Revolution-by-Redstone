package jp.plusplus.ir2.items;

import jp.plusplus.ir2.IR2;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/18.
 */
public class ItemGrip extends ItemSword {

    public ItemGrip(ToolMaterial par2EnumToolMaterial){
        super(par2EnumToolMaterial);
        setUnlocalizedName("IR2knifeGrip");
        setTextureName(IR2.MODID + ":knifeGrip");
        setCreativeTab(IR2.tabIR2);
        setNoRepair();
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_getItemUseAction_1_) {
        return EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        InventoryPlayer inv=player.inventory;
        int size=inv.getSizeInventory();
        for(int i=0;i<size;i++){
            ItemStack item=inv.getStackInSlot(i);
            if(item==null) continue;

            if(item.getItem()==ItemCore.knifeBlade){
                item.stackSize--;
                if(item.stackSize<=0){
                    inv.setInventorySlotContents(i, null);
                }
                return new ItemStack(ItemCore.knifeMP);
            }
        }

        return itemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(IR2.enableDescription) {
            par3List.add(I18n.format("info.knifeGrip.0"));
        }
    }
}
