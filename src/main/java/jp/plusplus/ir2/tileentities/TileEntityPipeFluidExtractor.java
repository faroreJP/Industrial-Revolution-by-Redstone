package jp.plusplus.ir2.tileentities;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by plusplus_F on 2015/02/07.
 */
public class TileEntityPipeFluidExtractor extends TileEntityPipeFluid {
    protected int extractTicks;

    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        extractTicks=nbt.getInteger("ExtractTicks");
    }
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setInteger("ExtractTicks", extractTicks);
    }


    @Override
    public void updateEntity() {
        if(getOutputRSS(-1)>0 &&  transportDelay==0){
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
            worldObj.notifyBlockChange(xCoord,yCoord,zCoord,getBlockType());
        }
        super.updateEntity();

        if(!worldObj.isRemote){
            if(getOutputRSS(-1)>0){
                if(extractTicks<=0){
                    extractPacket2();
                    extractTicks=20;
                }
                extractTicks--;
            }
        }
    }
    protected void extractPacket2(){
        ForgeDirection dir=ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
        TileEntity entity=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
        if(entity==null) return;

        int x = entity.xCoord, y = entity.yCoord, z = entity.zCoord;
        worldObj.markBlockForUpdate(x,y,z);
        worldObj.notifyBlockChange(x,y,z,entity.getBlockType());

        if(entity instanceof IFluidHandler){
            ForgeDirection opp=dir.getOpposite();
            IFluidHandler fh=(IFluidHandler)entity;
            FluidTankInfo[] info=fh.getTankInfo(opp);
            for(int i=0;i<info.length;i++) {
                if(info[i].fluid==null) continue;

                Fluid f=info[i].fluid.getFluid();
                if(f==null) continue;

                if (fh.canDrain(opp, f)){
                    FluidStack get=fh.drain(opp, new FluidStack(f, 1000), true);

                    if(get!=null && get.amount>0) {
                        PacketFluidStack packet = new PacketFluidStack(get);
                        packet.setDirection(dir.ordinal() ^ 1);
                        packet.setReverse(true);
                        offerPacket(packet);
                        entity.markDirty();

                        break;
                    }
                }
            }
        }
    }

    @Override
    public PipeType getPipeType() {
        return PipeType.EXTRACTOR;
    }
}
