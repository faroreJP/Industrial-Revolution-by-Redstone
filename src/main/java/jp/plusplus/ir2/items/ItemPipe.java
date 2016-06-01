package jp.plusplus.ir2.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.blocks.BlockCable;
import jp.plusplus.ir2.blocks.BlockPipe;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class ItemPipe extends ItemBlock {
    private IIcon iconOverlay=null;

    public ItemPipe(Block par1) {
        super(par1);
        this.setMaxStackSize(64);
        setCreativeTab(IR2.tabIR2);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (field_150939_a instanceof BlockPipe) {
            BlockPipe b = (BlockPipe) field_150939_a;

            if(IR2.enableDescription) {
                for (int i = 0; i < b.infoRow; i++) {
                    par3List.add(I18n.format("info." + b.infoName + "." + i));
                }
            }

            if(IR2.enableDescriptionOfRating) {
                String s = b.maxRSS + "RSS " + b.maxFrequency + "Hz Max";
                par3List.add(EnumChatFormatting.RED + s);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        Block b=this.field_150939_a;
        if(b instanceof BlockPipe){
            BlockPipe pipe=(BlockPipe)b;
            this.itemIcon=par1IconRegister.registerIcon(IR2.MODID+":"+pipe.textureFileName);
            if(pipe.pipeType==0)    iconOverlay=itemIcon;
            if(pipe.pipeType==1)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeExtractor");
            if(pipe.pipeType==2)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeOneWay");
            if(pipe.pipeType==3)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeSorting");
            if(pipe.pipeType==4)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeFluid");
            if(pipe.pipeType==5)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeFluidExtractor");
            if(pipe.pipeType==6)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeVoid");
            if(pipe.pipeType==7)    iconOverlay=par1IconRegister.registerIcon(IR2.MODID+":pipeFluidVoid");
            //super.registerIcons(par1IconRegister);
        }
    }

    @Override
    public int getSpriteNumber(){
        return 1;
    }
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses(){
        return iconOverlay!=null;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2){
        if(iconOverlay==null)   return itemIcon;
        return par2 > 0 ? iconOverlay : itemIcon;
    }
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        BlockPipe bp=(BlockPipe)field_150939_a;

        if(bp.pipeType==0) return super.getItemStackDisplayName(p_77653_1_);

        String n="";
        if(bp.pipeType==1) n=StatCollector.translateToLocal("pipe.ir2.extract");
        else if(bp.pipeType==2) n=StatCollector.translateToLocal("pipe.ir2.oneway");
        else if(bp.pipeType==3) n=StatCollector.translateToLocal("pipe.ir2.sort");
        else if(bp.pipeType==4) n=StatCollector.translateToLocal("pipe.ir2.fluid");
        else if(bp.pipeType==5) n=StatCollector.translateToLocal("pipe.ir2.fluid")+","+StatCollector.translateToLocal("pipe.ir2.extract");
        else if(bp.pipeType==6) n=StatCollector.translateToLocal("pipe.ir2.void");
        else if(bp.pipeType==7) n=StatCollector.translateToLocal("pipe.ir2.fluid")+","+StatCollector.translateToLocal("pipe.ir2.void");
        return super.getItemStackDisplayName(p_77653_1_)+"("+n+")";
    }
}
