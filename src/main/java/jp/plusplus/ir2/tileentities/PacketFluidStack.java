package jp.plusplus.ir2.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by plusplus_F on 2015/05/23.
 * フリュードスタックのパケット
 */
public class PacketFluidStack extends PacketBase{
    private FluidStack fluidStack;

    public PacketFluidStack(FluidStack fluid) {
        fluidStack=fluid;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }


    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);

        if(fluidStack!=null){
            NBTTagCompound tag=new NBTTagCompound();
            fluidStack.writeToNBT(tag);
            nbt.setTag("Fluid", tag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);

        if(nbt.hasKey("Fluid")){
            fluidStack= FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
        }
    }
}
