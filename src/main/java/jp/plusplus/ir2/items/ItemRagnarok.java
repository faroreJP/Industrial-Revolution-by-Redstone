package jp.plusplus.ir2.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.ProxyClient;
import jp.plusplus.ir2.packet.IR2PacketHandler;
import jp.plusplus.ir2.packet.MessageTheEnd;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandWeather;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/18.
 */
public class ItemRagnarok extends ItemSword{
    private Random r=new Random();
    public static ArrayList<String> mobNames=new ArrayList<String>();

    public ItemRagnarok(ToolMaterial par2EnumToolMaterial){
        super(par2EnumToolMaterial);
        setUnlocalizedName("IR2ragnarok");
        setTextureName(IR2.MODID + ":ragnarok");
        setCreativeTab(IR2.tabIR2);
        setNoRepair();

        if(mobNames.isEmpty()){
            mobNames.add("Zombie");
            mobNames.add("Skeleton");
            mobNames.add("Spider");
            mobNames.add("Creeper");
        }
    }


    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            //ragnarok
            if (r.nextFloat() < 0.5f) {
                EntityPlayer ep = ((EntityPlayer) entity);
                ep.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("info.ragnarok.1")));

                TheEnd(ep.worldObj, ep);
                IR2PacketHandler.INSTANCE.sendToDimension(new MessageTheEnd(), ep.worldObj.provider.dimensionId);
            }
        }
        return super.hitEntity(itemStack, par2EntityLivingBase, entity);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (IR2.enableDescription) {
            par3List.add(ChatFormatting.RED + I18n.format("info.ragnarok.0"));
            par3List.add(I18n.format("info.artifact.0"));
        }
    }
    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2){
        return false;
    }

    private boolean spawnEntityLiving(String name, World world, double x, double y, double z){
        EntityLiving entity = (EntityLiving) EntityList.createEntityByName(name, world);
        if (entity == null) return false;
        entity.onSpawnWithEgg(null);


        boolean flag=false;
        for(int i=0;i<30;i++) {
            double x1 = x + 0.5 + (r.nextDouble() - r.nextDouble()) * 15;
            double y1 = y+r.nextInt(3);
            double z1 = z + 0.5 + (r.nextDouble() - r.nextDouble()) * 15;
            float a = r.nextFloat() * 360.0F;
            entity.setLocationAndAngles(x1, y1, z1, a, 0);
            //if (!entity.getCanSpawnHere()) continue;

            world.spawnEntityInWorld(entity);
            entity.spawnExplosionParticle();
            entity.playLivingSound();
            flag=true;
            break;
        }

        if(flag){
            if(entity instanceof EntityCreeper) {
                entity.getDataWatcher().updateObject(17, new Byte((byte)1));
            }
            return true;
        }
        return false;
    }
    public void TheEnd(World w, EntityPlayer ep){
        WorldInfo info = w.getWorldInfo();
        info.setWorldTime(15000);
        //info.setRaining(true);
        //info.setRainTime(12000);
        //info.setThundering(true);
        //info.setThunderTime(12000);
        //w.setRainStrength(1.0f);
        //w.setThunderStrength(1.0f);
        w.addWeatherEffect(new EntityLightningBolt(w, ep.posX + r.nextInt(16)-8, ep.posY, ep.posZ + r.nextInt(16)-8));
        w.addWeatherEffect(new EntityLightningBolt(w, ep.posX + r.nextInt(16)-8, ep.posY, ep.posZ + r.nextInt(16)-8));
        w.addWeatherEffect(new EntityLightningBolt(w, ep.posX + r.nextInt(16)-8, ep.posY, ep.posZ + r.nextInt(16)-8));

        if(!w.isRemote){

            //敵のスポーン
            int mobC = 6 + r.nextInt(6 + 1);
            int count = 0;
            int max = mobNames.size();
            for (int i = 0; i < 100 && count < mobC; i++) {
                int n = r.nextInt(max);
                if (spawnEntityLiving(mobNames.get(n), w, ep.posX, ep.posY, ep.posZ)) {
                    count++;
                }
            }

            //炎ブロックの配置
            mobC=20+r.nextInt(8);
            count=0;
            int x,y,z;
            for(int i=0;i<100 && count<mobC; i++){
                x= MathHelper.floor_double(ep.posX)+r.nextInt(16)-8;
                y= MathHelper.floor_double(ep.posY)+r.nextInt(4)-2;
                z= MathHelper.floor_double(ep.posZ)+r.nextInt(16)-8;

                if(w.getBlock(x,y,z).isReplaceable(w,x,y,z) && w.setBlock(x,y,z, Blocks.fire)){
                    count++;
                }
            }
        }

    }
}
