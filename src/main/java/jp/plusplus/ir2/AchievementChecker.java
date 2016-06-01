package jp.plusplus.ir2;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.api.BlockMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemCore;
import jp.plusplus.ir2.items.ItemMachine;
import jp.plusplus.ir2.tileentities.MultiBlockPosition;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/02/19.
 * もともとは実績関係のクラスだったけど新しく作るのが面倒なんで他のイベントもここに置く事にしました。ごめんね☆
 */
public class AchievementChecker {
    private static ItemStack cTin;
    private static AchievementChecker instance;

    public static Achievement getCasingStone;

    public static Achievement getCasingObsidian;
    public static Achievement getAdvancedFurnace;

    public static Achievement getGeneratorVLF;
    public static Achievement getGeneratorVHF;
    public static Achievement challengeToLimit;

    public static Achievement getCrystalUnit;
    public static Achievement getCrystalUnitVillager;
    public static Achievement getCrystalUnitAdv;
    public static Achievement erste;

    public static Achievement getKnittingWool;
    public static Achievement getConductorTin;
    public static Achievement getPrimeMoverWood;

    public static Achievement aLotOfGear;

    private AchievementChecker(){
    }
    private AchievementChecker instance(){
        return instance;
    }

    public static void init(){
        cTin=new ItemStack(ItemCore.conductor, 1, 0);
        instance=new AchievementChecker();

        getCasingStone=new Achievement(IR2.MODID+":getCasingStone", "getCasingStone", 0, 0, BlockCore.casingStone, null).registerStat();

        getCasingObsidian=new Achievement(IR2.MODID+":getCasingObsidian", "getCasingObsidian", -1, 2, BlockCore.casingObsidian, getCasingStone).registerStat();
        getAdvancedFurnace=new Achievement(IR2.MODID+":getAdvancedFurnace", "getAdvancedFurnace", -1, 4, BlockCore.machineFurnaceAdv, getCasingObsidian).registerStat();
        getGeneratorVHF=new Achievement(IR2.MODID+":getGeneratorVHF", "getGeneratorVHF", -3, 3, BlockCore.generatorVHF, getCasingObsidian).registerStat();
        challengeToLimit=new Achievement(IR2.MODID+":challengeToLimit", "challengeToLimit", -4, 1, BlockCore.amplifier, getGeneratorVHF).registerStat();
        challengeToLimit.setSpecial();

        getGeneratorVLF=new Achievement(IR2.MODID+":getGeneratorVLF", "getGeneratorVLF", -2, 0, BlockCore.generatorVLF, getCasingStone).registerStat();

        getCrystalUnit=new Achievement(IR2.MODID+":getCrystalUnit", "getCrystalUnit", 0, -2, ItemCore.crystalUnit, getCasingStone).registerStat();
        getCrystalUnitVillager=new Achievement(IR2.MODID+":getCrystalUnitVillager", "getCrystalUnitVillager", -2, -2, ItemCore.crystalUnitVillager, getCrystalUnit).registerStat();
        getCrystalUnitAdv=new Achievement(IR2.MODID+":getCrystalUnitAdv", "getCrystalUnitAdv", 2, -3, ItemCore.crystalUnitAdv, getCrystalUnit).registerStat();
        erste=new Achievement(IR2.MODID+":erste", "erste", 1,-5, ItemCore.knifeMP, null/*getCrystalUnitAdv*/).registerStat();
        //erste=new Achievement(IR2.MODID+":erste", "erste", 1,-5, ItemCore.knifeMP, null);
        erste.setSpecial();

        getKnittingWool=new Achievement(IR2.MODID+":getKnittingWool", "getKnittingWool", 2, 0, ItemCore.knittingWool, getCasingStone).registerStat();
        getConductorTin=new Achievement(IR2.MODID+":getConductorTin", "getConductorTin", 4, 1, new ItemStack(ItemCore.conductor,1,0), getKnittingWool).registerStat();
        getPrimeMoverWood =new Achievement(IR2.MODID+":getPrimeMoverWood", "getPrimeMoverWood", 6, 0, ItemCore.primeMoverWood, getConductorTin).registerStat();

        aLotOfGear=new Achievement(IR2.MODID+":aLotOfGear", "aLotOfGear", 4,-2, ItemCore.gearIron, null).registerStat();
        aLotOfGear.setSpecial();

        Achievement[] page={getCasingStone,
                getGeneratorVLF,
                getCasingObsidian,getAdvancedFurnace,getGeneratorVHF,challengeToLimit,
                getKnittingWool,getConductorTin,getPrimeMoverWood,
                getCrystalUnit,getCrystalUnitVillager,getCrystalUnitAdv,erste,
                aLotOfGear};
        AchievementPage.registerAchievementPage(new AchievementPage("IR3", page));
    }

    public static void register(){
        //FMLLog.severe("client?");
        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            /*
            if(!event.world.isRemote){
                FMLLog.severe("called server.");
            }
            else{
                FMLLog.severe("called client.");
            }
            */

            Iterator<EntityPlayer> it=event.world.playerEntities.iterator();
            while(it.hasNext()) {
                //EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                EntityPlayer player = it.next();
                if (player == null) return;

                InventoryPlayer inv = player.inventory;

                int[] gearCount = {0, 0, 0};
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack itemStack = inv.getStackInSlot(i);
                    if (itemStack == null) continue;

                    Item item = itemStack.getItem();

                    if (item == ItemCore.knittingWool) player.triggerAchievement(getKnittingWool);
                    if (itemStack.isItemEqual(cTin)) player.triggerAchievement(getConductorTin);
                    else if (item == ItemCore.crystalUnitVillager) player.triggerAchievement(getCrystalUnitVillager);
                    else if (item == ItemCore.gearWood) gearCount[0] += itemStack.stackSize;
                    else if (item == ItemCore.gearStone) gearCount[1] += itemStack.stackSize;
                    else if (item == ItemCore.gearIron) gearCount[2] += itemStack.stackSize;
                }

                if (gearCount[0] >= 64 && gearCount[1] >= 64 && gearCount[2] >= 64) {
                    player.triggerAchievement(aLotOfGear);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        //FMLLog.severe("Crafted:" + event.crafting+","+event.player.getEntityWorld().isRemote);
        EntityPlayer player = event.player;

        /*
        Minecraft mc=Minecraft.getMinecraft();
        if(mc==null){
            FMLLog.severe("oops");
            return;
        }
        EntityPlayer player=mc.thePlayer;
        if(player==null){
            FMLLog.severe("what?");
            return;
        }
        */

        Item item=event.crafting.getItem();
        if (item == Item.getItemFromBlock(BlockCore.casingStone))                player.triggerAchievement(getCasingStone);
        else if (item == Item.getItemFromBlock(BlockCore.casingObsidian))       player.triggerAchievement(getCasingObsidian);
        else if(item == Item.getItemFromBlock(BlockCore.machineFurnaceAdv))     player.triggerAchievement(getAdvancedFurnace);
        else if(item == Item.getItemFromBlock(BlockCore.generatorVHF))          player.triggerAchievement(getGeneratorVHF);
        else if(item == Item.getItemFromBlock(BlockCore.generatorVLF))          player.triggerAchievement(getGeneratorVLF);
        else if(item == ItemCore.primeMoverWood)                                player.triggerAchievement(getPrimeMoverWood);
        else if(item == ItemCore.crystalUnit)                                   player.triggerAchievement(getCrystalUnit);
        else if(item == ItemCore.crystalUnitAdv)                                player.triggerAchievement(getCrystalUnitAdv);
    }

    @SubscribeEvent
    public void onAttacked(LivingDeathEvent e){
        //FMLLog.severe(Minecraft.getMinecraft().theWorld.isRemote + "," + e.target);
        //FMLLog.severe(Minecraft.getMinecraft().theWorld.isRemote + "," + e.source.getDamageType());

        //IR2.logger.info("attacked!");

        if(e.entity.worldObj==null) return;
        if(!e.entity.worldObj.isRemote) return;
        if(!(e.source.getEntity() instanceof EntityPlayer)) return;
        //IR2.logger.info("3");
        if(!(e.entityLiving instanceof IMob))   return;
        //IR2.logger.info("4");

        EntityPlayer p=(EntityPlayer)e.source.getEntity();
        if(p==null)			return;
        //IR2.logger.info("5");

        ItemStack itemStack=p.getCurrentEquippedItem();
        if(itemStack ==null)	return;
        //IR2.logger.info("6");
        if(itemStack.getItem()== ItemCore.knifeMP){
            p.triggerAchievement(AchievementChecker.erste);
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onSwitchTexture(TextureStitchEvent.Pre e){
        //FMLLog.severe("called registering");
        if(IR2.proxy instanceof ProxyClient) {
            if (e.map.getTextureType() == 0) {
                BlockCore.fluidMilk.setIcons(e.map.registerIcon(IR2.MODID + ":fluidMilk"));
                BlockCore.fluidRedstone.setIcons(e.map.registerIcon(IR2.MODID + ":fluidRedstone"));
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRender(RenderHandEvent event){
        EntityPlayer player= Minecraft.getMinecraft().thePlayer;
        if(player!=null){
            //----------------------------------------------------------------------------------------------------
            // 手にしているアイテムがマルチブロック機械か
            //----------------------------------------------------------------------------------------------------
            ItemStack itemStack=player.getCurrentEquippedItem();
            if(itemStack==null || !(itemStack.getItem() instanceof ItemMachine)) return;
            if(!(((ItemMachine) itemStack.getItem()).field_150939_a instanceof BlockMachineBase)) return;

            BlockMachineBase bm=(BlockMachineBase)((ItemMachine) itemStack.getItem()).field_150939_a;
            if(bm.multiWidth ==1 &&  bm.multiHeight == 1 && bm.multiDepth == 1) return;

            //----------------------------------------------------------------------------------------------------
            // プレイヤーからブロック設置時の向きを得る
            //----------------------------------------------------------------------------------------------------
            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            int side=2;
            if (l == 0) side=3;
            else if (l == 1) side=4;
            else if (l == 2) side=2;
            else if (l == 3) side=5;

            //----------------------------------------------------------------------------------------------------
            // ブロックにカーソルが触れている場合、ブロック設置座標を得る
            //----------------------------------------------------------------------------------------------------
            MovingObjectPosition mop=Minecraft.getMinecraft().objectMouseOver;
            if(mop==null || mop.typeOfHit!= MovingObjectPosition.MovingObjectType.BLOCK) return;
            ForgeDirection rayDir=ForgeDirection.getOrientation(mop.sideHit);
            LinkedList<MultiBlockPosition> list=bm.getMultiBlockPositions(mop.blockX+rayDir.offsetX, mop.blockY+rayDir.offsetY, mop.blockZ+rayDir.offsetZ, side);
            list.add(new MultiBlockPosition(mop.blockX+rayDir.offsetX, mop.blockY+rayDir.offsetY, mop.blockZ+rayDir.offsetZ, 0));

            //----------------------------------------------------------------------------------------------------
            // 描画
            //----------------------------------------------------------------------------------------------------
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.partialTicks;
            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.partialTicks;
            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.partialTicks;
            float f1 = 0.002F;

            World world=Minecraft.getMinecraft().theWorld;
            Block casing=bm.getCasingBlockType();

            //マルチブロック
            for(MultiBlockPosition pos : list){
                AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x+1, pos.y+1, pos.z+1).expand(f1, f1, f1).getOffsetBoundingBox(-d0, -d1, -d2);

                //-----------色の決定---------
                int col=0x00ffff;
                if(pos==list.getLast()){
                    //機械マスの場合、何かあれば赤
                    Block b=world.getBlock(pos.x, pos.y, pos.z);
                    if(!b.isReplaceable(world, pos.x, pos.y, pos.z)){
                        col=0xff0000;
                    }
                }
                else{
                    //筐体マスの場合、筐体でなかったり、接続済みの筐体は赤くなる
                    Block b=world.getBlock(pos.x, pos.y, pos.z);
                    if(!b.isReplaceable(world, pos.x, pos.y, pos.z)){
                        if(b==casing){
                            TileEntityMultiBlock te=(TileEntityMultiBlock)world.getTileEntity(pos.x, pos.y, pos.z);
                            if(te.getMachineCore()!=null){
                                col=0xff0000;
                            }
                        }
                        else{
                            col=0xff0000;
                        }
                    }
                }

                RenderGlobal.drawOutlinedBoundingBox(aabb, col);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);

        }

        //event.context.drawSelectionBox();
        //event.context.drawSelectionBox();
    }
}
