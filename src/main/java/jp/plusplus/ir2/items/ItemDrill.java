package jp.plusplus.ir2.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/07/24.
 */
public class ItemDrill extends ItemPickaxe {
    public static final String TAG_DAMAGE="NbtDamage";
    public static final String TAG_DAMAGE_MAX="NbtDamageMax";
    public static final String TAG_SPEED="DamagePerTick";
    public static final String TAG_UNIT="CrystaUnit";
    public static final String TAG_HAS="HasCrystal";

    private IIcon[] icons;

    protected ItemDrill(ToolMaterial p_i45347_1_) {
        super(p_i45347_1_);
        setUnlocalizedName("IR2drill");
        setTextureName(IR2.MODID + ":drill");
        setCreativeTab(IR2.tabIR2);
        setNoRepair();
        setMaxDamage(100);
        setHarvestLevel("pickaxe", 3);
    }

    public static ItemStack getDrill(Item item, int meta){
        ItemStack t=new ItemStack(item, 1, meta);

        if(meta==0) t.addEnchantment(Enchantment.silkTouch, 1);
        else if(meta==1) t.addEnchantment(Enchantment.fortune, 3);

        return t;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int i=0;i<2;i++) par3List.add(getDrill(par1, i));
    }

    @Override
    public void registerIcons(IIconRegister register){
        icons=new IIcon[2];
        for(int i=0;i<icons.length;i++) icons[i]=register.registerIcon(IR2.MODID+":drill"+i);
    }
    @Override
    public IIcon getIconFromDamage(int meta){
        return icons[meta];
    }


    //-----------------------------------------------------
    //         NBTで耐久値とか管理するよーん
    //-----------------------------------------------------
    public void createNullNBT(ItemStack itemStack){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setBoolean(TAG_HAS, false);
        nbt.setDouble(TAG_DAMAGE, 100);
        nbt.setDouble(TAG_DAMAGE_MAX, 100);
        nbt.setBoolean("Unbreakable", true);
        itemStack.setTagCompound(nbt);

        int meta=itemStack.getItemDamage();
        if(meta==0) itemStack.addEnchantment(Enchantment.silkTouch, 1);
        else if(meta==1) itemStack.addEnchantment(Enchantment.fortune, 3);

    }
    protected double getDamageNBT(ItemStack itemStack){
        if(!itemStack.hasTagCompound()){
            createNullNBT(itemStack);
        }

        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getDouble(TAG_DAMAGE);
    }
    protected boolean damageNBT(ItemStack itemStack){
        if(itemStack.hasTagCompound()) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if(nbt.getBoolean(TAG_HAS)) {
                double dd = nbt.getDouble(TAG_DAMAGE) + nbt.getDouble(TAG_SPEED);
                double dm = nbt.getDouble(TAG_DAMAGE_MAX);
                nbt.setDouble(TAG_DAMAGE, dd);
                if (dd > dm) {
                    nbt.setDouble(TAG_DAMAGE, dm);
                    return true;
                }
            }

            return false;
        }
        return false;
    }
    protected boolean damageNBT(ItemStack itemStack, int x){
        if(itemStack.hasTagCompound()) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if(nbt.getBoolean(TAG_HAS)) {
                double dd = nbt.getDouble(TAG_DAMAGE) + x*nbt.getDouble(TAG_SPEED);
                double dm = nbt.getDouble(TAG_DAMAGE_MAX);
                nbt.setDouble(TAG_DAMAGE, dd);
                if (dd > dm) {
                    nbt.setDouble(TAG_DAMAGE, dm);
                    return true;
                }
            }

            return false;
        }
        return false;
    }
    protected void onBreakNBT(ItemStack itemStack, World world, EntityPlayer entity, int slot, boolean held){
        //unitを取り除く
        removeCrystalUnit(itemStack);
    }
    protected boolean setCrystalUnit(ItemStack drill, ItemStack unit){
        getDamageNBT(drill); //nbtの生成
        NBTTagCompound nbt=drill.getTagCompound();
        if(nbt.getBoolean(TAG_HAS)) return false;

        NBTTagCompound newNbt=new NBTTagCompound();

        NBTTagCompound tag=new NBTTagCompound();
        unit.writeToNBT(tag);
        newNbt.setTag(TAG_UNIT, tag);
        newNbt.setBoolean(TAG_HAS, true);

        // Damageは振動子と同一
        ItemCrystalUnit icu=(ItemCrystalUnit)unit.getItem();
        newNbt.setDouble(TAG_DAMAGE, icu.getDamageNBT(unit));
        newNbt.setDouble(TAG_DAMAGE_MAX, icu.maxDamageNBT);
        newNbt.setDouble(TAG_SPEED, 100*64.0/icu.frequency);
        //newNbt.setBoolean("Unbreakable", false);

        drill.setTagCompound(newNbt);

        int meta=drill.getItemDamage();
        if(meta==0) drill.addEnchantment(Enchantment.silkTouch, 1);
        else if(meta==1) drill.addEnchantment(Enchantment.fortune, 3);

        return true;
    }
    protected ItemStack removeCrystalUnit(ItemStack drill){
        if(!drill.hasTagCompound()) return null;
        NBTTagCompound nbt=drill.getTagCompound();
        if(!nbt.getBoolean(TAG_HAS)) return null;

        //読み込んでダメージ設定
        ItemStack unit=ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(TAG_UNIT));
        ItemCrystalUnit icu=(ItemCrystalUnit)unit.getItem();
        icu.setDamageNBT(unit, nbt.getDouble(TAG_DAMAGE));

        //
        createNullNBT(drill);

        return unit;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_getItemUseAction_1_) {
        return EnumAction.none;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean held) {
        if(world.isRemote || !held)   return;

        if(entity instanceof EntityPlayer) {
            EntityPlayer p=(EntityPlayer)entity;

            if(p.capabilities.isCreativeMode)   return;

            if(itemStack.hasTagCompound()){
                NBTTagCompound nbt=itemStack.getTagCompound();
                if(nbt.getDouble(TAG_DAMAGE)>=nbt.getDouble(TAG_DAMAGE_MAX)){
                    onBreakNBT(itemStack, world, p, slot, held);
                }
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        //unitのセットとか。
        if(!itemStack.hasTagCompound()) createNullNBT(itemStack);

        if(itemStack.getTagCompound().getBoolean(TAG_HAS)){
            //リムーブ
            ItemStack unit=removeCrystalUnit(itemStack);
            player.inventory.addItemStackToInventory(unit);
            player.inventory.markDirty();
        }
        else{
            //セット
            InventoryPlayer ip=player.inventory;
            for(int i=0;i<ip.getSizeInventory();i++){
                ItemStack slot=ip.getStackInSlot(i);
                if(slot==null || !(slot.getItem() instanceof ItemCrystalUnit)) continue;

                // 出力周波数が64Hz未満のものは使用できない。
                ItemCrystalUnit icu=(ItemCrystalUnit)slot.getItem();
                if(icu.frequency<64) continue;

                if(setCrystalUnit(itemStack, slot)) {
                    ip.setInventorySlotContents(i, null);
                    ip.markDirty();
                    break;
                }
            }
        }

        return itemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        if(IR2.enableDescription) {
            par3List.add(I18n.format("info.drill.0"));
            par3List.add(I18n.format("info.drill.1"));
        }
        if(IR2.enableDescriptionOfRating){
            par3List.add(ChatFormatting.RED+"Input 64Hz Min");
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
        double d=getDamageNBT(stack);
        double dm=stack.getTagCompound().getDouble(TAG_DAMAGE_MAX);

        return (int)(getMaxDamage()*(d/dm));
    }

    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
        damageNBT(p_77644_1_, 40);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
        if ((double) p_150894_3_.getBlockHardness(p_150894_2_, p_150894_4_, p_150894_5_, p_150894_6_) != 0.0D) {
            damageNBT(p_150894_1_, 20);
        }
        return true;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(!toolClass.equals("pickaxe")) return -1;
        if(!stack.hasTagCompound()) return -1;
        NBTTagCompound nbt=stack.getTagCompound();
        if(nbt.getBoolean(TAG_HAS)){
            IR2.logger.info("has");
            return toolMaterial.getHarvestLevel();
        }
        return -1;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if(!stack.hasTagCompound()) return 0.1f;
        NBTTagCompound nbt=stack.getTagCompound();
        if(nbt.getBoolean(TAG_HAS)){
            IR2.logger.info("has");
            return super.getDigSpeed(stack, block, meta);
        }
        return 0.1f;
    }

}
