package jp.plusplus.ir2.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.model.ModelCrystalUnit;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ItemCrystalUnit extends Item {
    private static final ResourceLocation rlCU=new ResourceLocation(IR2.MODID+":textures/models/CrystalUnit.png");
    protected ModelCrystalUnit model;

    /**
     * 最大出力RSS
     */
    public short rss;
    /**
     * 最大出力Hz
     */
    public short frequency;
    /**
     * 最大ダメージ値
     */
    public double maxDamageNBT;

    /**
     * 振動子を初期化する。
     * @param rss 最大出力RSS
     * @param freq 最大出力Hz
     * @param sec 最大出力Hz時の稼動時間(秒)
     */
    public ItemCrystalUnit(short rss, short freq, int sec){
        setCreativeTab(IR2.tabIR2);
        setMaxStackSize(1);
        setNoRepair();
        setMaxDamage(100);

        this.rss=rss;
        frequency=freq;
        maxDamageNBT=20.0*sec*100.0;
    }

    //---------------------いじるべきとこ----------------------------------

    /**
     * パルスジェネレータでの描画用テクスチャを返す
     * @return
     */
    public ResourceLocation getResourceLocation(){
        return rlCU;
    }

    /**
     * パルスジェネレータでの描画用モデルを返す
     * @return
     */
    public ModelCrystalUnit getModel(){
        if(model==null){
            if(this== ItemCore.crystalUnit) model=new ModelCrystalUnit(0);
            else if(this==ItemCore.crystalUnitDouble)  model=new ModelCrystalUnit(1);
            else if(this==ItemCore.crystalUnitEnder)  model=new ModelCrystalUnit(2);
            else if(this==ItemCore.crystalUnitAdv)  model=new ModelCrystalUnit(3);
            else if(this==ItemCore.crystalUnitVillager)  model=new ModelCrystalUnit(4);
            else if(this==ItemCore.crystalUnitCreeper)  model=new ModelCrystalUnit(5);
        }
        return model;
    }

    //---------------------------------------------------------------------------------

    @Override
    public Item setUnlocalizedName(String name){
        return super.setUnlocalizedName("IR2"+name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        if(IR2.enableDescription) {
            list.add(I18n.format("info.crystalUnit.0"));
        }
        if(IR2.enableDescriptionOfRating) {
            list.add(EnumChatFormatting.RED + ("Output " + rss + "RSS, " + frequency + "Hz Max"));
        }
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return getDamageNBT(stack) > 0;
    }
    public double getDamageNBT(ItemStack itemStack){
        if(!itemStack.hasTagCompound()){
            NBTTagCompound tag=new NBTTagCompound();
            tag.setDouble("k-Damage", 0);
            itemStack.setTagCompound(tag);
        }

        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getDouble("k-Damage");
    }
    public ItemStack setDamageNBT(ItemStack itemStack, double d){
        if(itemStack.hasTagCompound()) {
            if(d>=maxDamageNBT) return null;

            NBTTagCompound nbt = itemStack.getTagCompound();
            nbt.setDouble("k-Damage", d);
        }
        return itemStack;
    }
    @Deprecated
    @Override
    public int getDisplayDamage(ItemStack stack) {
        return (int)(100*getDamageNBT(stack)/maxDamageNBT);
    }
    @Override
    public int getDamage(ItemStack stack){
        return (int)(100*getDamageNBT(stack)/maxDamageNBT);
    }
}
