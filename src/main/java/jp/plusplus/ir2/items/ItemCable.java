package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ItemCable extends ItemBlock {
    public static final String COLOR_NAMES[]={
            "Black",
            "Red",
            "Green",
            "Brown",
            "Blue",
            "Purple",
            "Cyan",
            "LightGrey",
            "Grey",
            "Pink",
            "LightGreen",
            "Yellow",
            "LightBlue",
            "Magenta",
            "Orange",
            "White"
    };
    public static final int[] COLOR_VALUE ={0x000000, 0xff0000, 0x009113, 0x552700, 0x2b00ff, 0xff00f7, 0x00afaf, 0xcdcdcd,
            0x787878, 0xffaaaa, 0x48ff37, 0xffea00, 0x00ffff, 0xd7008b, 0xff9500, 0xffffff};
    private IIcon iconOverlay;

    public ItemCable(Block par1) {
        super(par1);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.hasSubtypes = true;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(field_150939_a instanceof BlockCable){
            if(IR2.enableDescription) {
                par3List.add(I18n.format("info.cable.0"));
                par3List.add(I18n.format("info.cable.1"));
            }

            par3List.add("Color : "+ ItemDye.field_150923_a[par1ItemStack.getItemDamage()]);

            if(IR2.enableDescriptionOfRating) {
                BlockCable b = (BlockCable) field_150939_a;
                String s = b.maxRSS + "RSS " + b.maxFrequency + "Hz Max";
                par3List.add(EnumChatFormatting.RED + s);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1){
        //return icons[par1];
        return super.getIconFromDamage(par1);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
    @Override
    public int getSpriteNumber(){
        return 1;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        Block b=this.field_150939_a;
        if(b instanceof BlockCable){
            this.itemIcon=par1IconRegister.registerIcon(IR2.MODID+":"+((BlockCable)b).textureFileName);
            iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":"+"cableOverlay");
        }
    }
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses(){
        return true;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2){
        return par2 > 0 ? iconOverlay : itemIcon;
    }
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
        int d=par1ItemStack.getItemDamage();
        if(d<0 || d>= COLOR_VALUE.length)	d= COLOR_VALUE.length-1;
        return par2==0?0xffffff: COLOR_VALUE[d];
    }
}
