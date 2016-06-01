package jp.plusplus.ir2;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInterModComms;
import jp.plusplus.ir2.api.IR3RecipeAPI;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by plusplus_F on 2015/05/31.
 */
public class IMCEventReceiver {
    private static HashMap<String, Integer> buildingIds=new HashMap<String, Integer>();
    private static boolean buildingInit;

    public static void receive(FMLInterModComms.IMCEvent event){
        Iterator<FMLInterModComms.IMCMessage> it=event.getMessages().iterator();
        while(it.hasNext()){
            FMLInterModComms.IMCMessage m=it.next();
            if(m.key.equals("exp")) HandleEXP(event, m);
            else if(m.key.equals("crusher")) HandleCrasher(event, m);
            else if(m.key.equals("extractor")) HandleExtractor(event, m);
            else if(m.key.equals("loom")) HandleLoom(event, m);
            else if(m.key.equals("dyer")) HandleDyer(event, m);
            else if(m.key.equals("harvester")) HandleHarvester(event, m);
            else if(m.key.equals("wood")) HandleWood(event, m);
            else if(m.key.equals("leave")) HandleLeave(event, m);
            else if(m.key.equals("fisher")) HandleFisher(event, m);
            else if(m.key.equals("ocean")) HandleOceanFisher(event, m);
            else if(m.key.equals("composition")) HandleComposition(event, m);
            else{
                FMLLog.warning("Invalid IMCMassage from " + m.getSender());
            }
        }
    }

    public static void HandleEXP(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            float exp=0;

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(tag.hasKey("Output")) exp=tag.getFloat("Output");
            if(m!=null && exp>0){
                flag=true;
                Recipes.addEXP(m, exp);
                //ItemRenderer
                //ItemArmor
                //EntityRenderer
                //RenderingRegistry.addNewArmourRendererPrefix()
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register EXP with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleCrasher(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            Object[] p=null;
            int size=-1;

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(tag.hasKey("Size")) size=tag.getInteger("Size");
            if(size>0 && size<3) {
                p = new Object[2 * size];
                for (int i = 0; i < size; i++) {
                    p[2 * i] = new Integer(tag.getInteger("Probability" + i));
                    p[2 * i + 1] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output" + i));
                }
            }

            if(m!=null && size>0 && p!=null){
                flag=true;
                Recipes.addCrushing(m, p);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Crusher recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleExtractor(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            int amount=0;

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(tag.hasKey("Output")) amount=tag.getInteger("Output");
            if(m!=null && amount > 0) {
                flag=true;
                Recipes.addExtracting(m, amount);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Extractor recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleLoom(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            ItemStack p=null;

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(tag.hasKey("Output")) p=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output"));
            if(m!=null && p!=null) {
                flag=true;
                Recipes.addWeaving(m, p);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Loom recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleDyer(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(m!=null) {
                flag=true;
                Recipes.addDying(m.getItem());
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Dyer recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleWood(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;
        if(message.isItemStackMessage()){
            Item item=message.getItemStackValue().getItem();
            if(item instanceof ItemBlock){
                flag=true;
                Recipes.addWood(((ItemBlock) item).field_150939_a);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Wood with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleLeave(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;
        if(message.isItemStackMessage()){
            Item item=message.getItemStackValue().getItem();
            if(item instanceof ItemBlock) {
                flag = true;
                Recipes.addLeave(((ItemBlock) item).field_150939_a);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Leave with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleHarvester(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isItemStackMessage()){
            Item item=message.getItemStackValue().getItem();
            if(item instanceof ItemBlock) {
                flag = true;
                Recipes.addHarvestTarget(((ItemBlock) item).field_150939_a, message.getItemStackValue().getItemDamage());
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Harvester Target with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleFisher(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            int amount=0;

            if(tag.hasKey("Output")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output"));
            if(tag.hasKey("Weight")) amount=tag.getInteger("Weight");
            if(m!=null && amount > 0) {
                flag=true;
                Recipes.addFishing(m, amount);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Fisher recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleOceanFisher(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){
        boolean flag=false;

        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            int amount=0;

            if(tag.hasKey("Output")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output"));
            if(tag.hasKey("Weight")) amount=tag.getInteger("Weight");
            if(m!=null && amount > 0) {
                flag=true;
                Recipes.addFishingSea(m, amount);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Ocean Fisher recipe with IMCMassage from " + message.getSender());
        }
    }
    public static void HandleComposition(FMLInterModComms.IMCEvent event, FMLInterModComms.IMCMessage message){

        if(!buildingInit){
            buildingInit=true;
            buildingIds.put("other", IR3RecipeAPI.COMPOSITION_OTHER);
            buildingIds.put("plant", IR3RecipeAPI.COMPOSITION_PLANT);
            buildingIds.put("tool", IR3RecipeAPI.COMPOSITION_TOOL);
            buildingIds.put("food", IR3RecipeAPI.COMPOSITION_FOOD);
            buildingIds.put("ore", IR3RecipeAPI.COMPOSITION_ORE);
            buildingIds.put("magic", IR3RecipeAPI.COMPOSITION_MAGIC);
        }

        boolean flag=false;
        if(message.isNBTMessage()){
            NBTTagCompound tag = message.getNBTValue();
            ItemStack m=null;
            int weight=0;
            int value=0;
            String id="";

            if(tag.hasKey("Input")) m=ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
            if(tag.hasKey("Weight")) weight=tag.getInteger("Weight");
            if(tag.hasKey("Value")) value=tag.getInteger("Value");
            if(tag.hasKey("ID")) id=tag.getString("ID");
            if(m!=null && weight > 0 && value>0 && id.length()>0) {
                flag=true;

                if(!buildingIds.containsKey(id)) buildingIds.put(id, Recipes.getUniqueIdForBuilding(id));
                Recipes.addBuildingItem(buildingIds.get(id), m, weight, value);
            }
        }

        if(!flag){
            FMLLog.warning("Failed to register new Composition recipe with IMCMassage from " + message.getSender());
        }
    }
}
