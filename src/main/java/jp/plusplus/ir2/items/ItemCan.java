package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Created by plusplus_F on 2015/05/21.
 */
public class ItemCan extends Item {
    public static int CAPACITY=500;

    public ItemCan(){
        setCreativeTab(IR2.tabIR2);
        setHasSubtypes(true);
        setMaxDamage(0);
        setTextureName(IR2.MODID + ":can");
        setUnlocalizedName("IR2can");
        setMaxStackSize(64);
        setContainerItem(this);
    }

    public static FluidStack getFluidStack(ItemStack itemStack){
        if(!itemStack.hasTagCompound()){
            NBTTagCompound tag=new NBTTagCompound();
            tag.setBoolean("IsFilled", false);
            itemStack.setTagCompound(tag);
        }

        NBTTagCompound nbt=itemStack.getTagCompound();
        if(!nbt.getBoolean("IsFilled")) return null;

        return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
    }
    public static void setFluidStack(ItemStack itemStack, FluidStack fluid){
        if(fluid==null || fluid.amount<=CAPACITY){
            NBTTagCompound tag=new NBTTagCompound();
            tag.setBoolean("IsFilled", false);
            itemStack.setTagCompound(tag);
            itemStack.setItemDamage(0);
        }
        else{
            NBTTagCompound tag=new NBTTagCompound();
            tag.setBoolean("IsFilled", true);

            NBTTagCompound f=new NBTTagCompound();
            fluid.writeToNBT(f);
            tag.setTag("Fluid", f);

            itemStack.setTagCompound(tag);
            itemStack.setItemDamage(FluidRegistry.getFluidID(fluid.getFluid().getName()));
        }
    }

    public boolean hasContainerItem(ItemStack itemStack){
        return itemStack.getItemDamage()!=0;
    }
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_hasEffect_1_) {
        return p_hasEffect_1_.getItemDamage()!=0;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        int id=par1ItemStack.getItemDamage();
        if(id==0) par3List.add("Empty:0mb");
        else{
            FluidStack f= FluidContainerRegistry.getFluidForFilledItem(par1ItemStack);
            par3List.add(f.getLocalizedName()+":"+CAPACITY+"mb");
        }
    }
}
