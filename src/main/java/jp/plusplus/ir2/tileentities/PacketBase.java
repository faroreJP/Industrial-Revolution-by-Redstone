package jp.plusplus.ir2.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by plusplus_F on 2015/07/10.
 * パイプ内を通るパケットの基底
 */
public class PacketBase {
    public static final int TICK_SCALE=1000;
    public static final int TICK_MAX=10*TICK_SCALE;

    protected int direction; // front
    protected int ticks;
    protected boolean reverse;

    public PacketBase(){
        direction=-1;
    }
    public PacketBase(NBTTagCompound nbt){
        readFromNBT(nbt);
    }

    public boolean update(float scale){
        ticks+=(int)(scale*TICK_SCALE);
        return ticks>=TICK_MAX;
    }

    public int getDirection(){
        return direction;
    }
    public void setDirection(int dir){
        direction=dir;
    }
    public int whereFrom(){
        return direction^1;
    }

    public boolean isReversed(){
        return reverse;
    }
    public void setReverse(boolean flag){
        reverse=flag;
    }

    public float getTicksRate(){
        return (float)ticks/TICK_MAX;
    }
    public int getTicks(){ return ticks; }
    public void setTicks(int t){
        if(t<0) t=0;
        if(t>TICK_MAX) t=TICK_MAX;
        ticks=t;
    }

    public void writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("Direction", direction);
        nbt.setBoolean("IsReversed", reverse);
        nbt.setInteger("PacketTicks", ticks);
    }
    public void readFromNBT(NBTTagCompound nbt){
        direction=nbt.getInteger("Direction");
        reverse=nbt.getBoolean("IsReversed");
        ticks=nbt.getInteger("PacketTicks");
    }

}
