package jp.plusplus.ir2.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.container.*;
import jp.plusplus.ir2.tileentities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/01.
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id== IR2.GUI_ID_BAG){
            return new ContainerBag(player.inventory);
        }

        //-------------------------------------------------------
        if(!world.blockExists(x,y,z))    return null;
        TileEntity e=world.getTileEntity(x,y,z);
        if(e instanceof TileEntityTank){
            return new ContainerTank(player, (TileEntityTank)e);
        }
        if(e instanceof TileEntityPipeSorting){
            return new ContainerPipeSorting(player, (TileEntityPipeSorting)e);
        }
        if(e instanceof TileEntitySyntheticFurnace){
            return new ContainerSyntheticFurnace(player, (TileEntitySyntheticFurnace)e);
        }
        if(e instanceof TileEntityAlloySmelterRusty){
            return new ContainerAlloySmelterRusty(player, (TileEntityAlloySmelterRusty)e);
        }
        if(e instanceof TileEntityFurnaceAdvanced){
            return new ContainerFurnaceAdvanced(player, (TileEntityFurnaceAdvanced)e);
        }
        if(e instanceof TileEntityTransmitter){
            return new ContainerTransmitter(player, (TileEntityTransmitter)e);
        }
        if(e instanceof TileEntityAmplifier){
            return new ContainerAmplifier(player, (TileEntityAmplifier)e);
        }
        if(e instanceof TileEntityAlloySmelter){
            return new ContainerAlloySmelter(player, (TileEntityAlloySmelter)e);
        }
        if(e instanceof TileEntityAutoCrafter){
            return new ContainerAutoCrafter(player, (TileEntityAutoCrafter)e);
        }
        if(e instanceof TileEntityDyer){
            return new ContainerDyer(player, (TileEntityDyer)e);
        }
        if(e instanceof TileEntityCrusher){
            return new ContainerCrusher(player, (TileEntityCrusher)e);
        }
        if(e instanceof TileEntityFountain){
            return new ContainerFountain(player, (TileEntityFountain)e);
        }
        if(e instanceof TileEntitySmoker){
            return new ContainerSmoker(player, (TileEntitySmoker)e);
        }
        if(e instanceof TileEntityMixer){
            return new ContainerMixer(player, (TileEntityMixer)e);
        }
        if(e instanceof TileEntityCheeseMaker){
            return new ContainerCheeseMaker(player, (TileEntityCheeseMaker)e);
        }
        if(e instanceof TileEntityPump){
            return new ContainerPump(player, (TileEntityPump)e);
        }
        //
        if(e instanceof TileEntityCollector){
            return new ContainerCollector(player, (TileEntityCollector)e);
        }
        if(e instanceof TileEntityMachineBase){
            return new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityGenerator){
            return new ContainerGenerator(player, (TileEntityGenerator)e);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id==IR2.GUI_ID_BAG){
            return new GuiBag(player.inventory);
        }

        //-------------------------------------------------------
        if(!world.blockExists(x,y,z))    return null;

        TileEntity e=world.getTileEntity(x,y,z);
        if(e instanceof TileEntityTank){
            return new GuiTank(new ContainerTank(player, (TileEntityTank)e), (TileEntityTank)e);
        }
        if(e instanceof TileEntityPipeSorting){
            return new GuiPipeSorting(new ContainerPipeSorting(player, (TileEntityPipeSorting)e));
        }
        if(e instanceof TileEntitySyntheticFurnace){
            return new GuiSyntheticFurnace(player, (TileEntitySyntheticFurnace)e);
        }
        if(e instanceof TileEntityAlloySmelterRusty){
            return new GuiAlloySmelterRusty(new ContainerAlloySmelterRusty(player, (TileEntityAlloySmelterRusty)e), (TileEntityAlloySmelterRusty)e);
        }
        if(e instanceof TileEntitySpinning){
            return new GuiSpinning(new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityFurnaceAdvanced){
            return new GuiFurnaceAdvanced(new ContainerFurnaceAdvanced(player, (TileEntityFurnaceAdvanced)e), (TileEntityFurnaceAdvanced)e);
        }
        if(e instanceof TileEntityFurnace){
            return new GuiFurnaceRS(new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityLoom){
            return new GuiLoom(new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityExtractor){
            return new GuiExtractor(new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityTransmitter){
            return new GuiTransmitter(player, (TileEntityTransmitter)e);
        }
        if(e instanceof TileEntityAmplifier){
            return new GuiAmplifier(player, (TileEntityAmplifier)e);
        }
        if(e instanceof TileEntityAlloySmelter){
            return new GuiAlloySmelter(new ContainerAlloySmelter(player, (TileEntityAlloySmelter)e), (TileEntityAlloySmelter)e);
        }
        if(e instanceof TileEntityAutoCrafter){
            return new GuiAutoCrafter(new ContainerAutoCrafter(player, (TileEntityAutoCrafter)e), (TileEntityAutoCrafter)e);
        }
        if(e instanceof TileEntityDyer){
            return new GuiDyer(new ContainerDyer(player, (TileEntityDyer)e), (TileEntityDyer)e);
        }
        if(e instanceof TileEntityCrusher){
            return new GuiCrusher(new ContainerCrusher(player, (TileEntityCrusher)e), (TileEntityCrusher)e);
        }
        if(e instanceof TileEntityFountain){
            return new GuiFountain(new ContainerFountain(player, (TileEntityFountain)e), (TileEntityFountain)e);
        }
        if(e instanceof TileEntitySmoker){
            return new GuiSmoker(new ContainerSmoker(player, (TileEntitySmoker)e), (TileEntitySmoker)e);
        }
        if(e instanceof TileEntityMixer){
            return new GuiMixer(new ContainerMixer(player, (TileEntityMixer)e), (TileEntityMixer)e);
        }
        if(e instanceof TileEntityCheeseMaker){
            return new GuiCheeseMaker(new ContainerCheeseMaker(player, (TileEntityCheeseMaker)e), (TileEntityCheeseMaker)e);
        }
        if(e instanceof TileEntityPump){
            return new GuiPump(new ContainerPump(player, (TileEntityPump)e), (TileEntityPump)e);
        }
        //
        if(e instanceof TileEntityCollector){
            return new GuiCollector(new ContainerCollector(player, (TileEntityCollector)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityMachineBase){
            return new GuiProcessor(new ContainerProcessor(player, (TileEntityMachineBase)e, (ISidedInventory)e), (TileEntityMachineBase)e, (ISidedInventory)e);
        }
        if(e instanceof TileEntityGenerator){
            return new GuiGenerator(player, (TileEntityGenerator)e);
        }
        return null;
    }
}
