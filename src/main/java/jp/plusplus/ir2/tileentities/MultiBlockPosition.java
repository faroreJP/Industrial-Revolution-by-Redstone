package jp.plusplus.ir2.tileentities;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by plusplus_F on 2015/07/03.
 * 接続ブロックの座標データ
 */
public class MultiBlockPosition {
    public int x,y,z,side;
    public boolean noSide;

    public MultiBlockPosition(){}
    public MultiBlockPosition(int x, int y, int z, int side){
        this.x=x;
        this.y=y;
        this.z=z;
        this.side=side;
    }
    public MultiBlockPosition(int x, int y, int z, int side, boolean no){
        this.x=x;
        this.y=y;
        this.z=z;
        this.side=side;
        noSide=no;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof MultiBlockPosition)) return false;
        MultiBlockPosition mbp=(MultiBlockPosition)o;
        return x==mbp.x && y==mbp.y && z==mbp.z && ((noSide && mbp.noSide) || side==mbp.side);
    }

    public void writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("x", x);
        nbt.setInteger("y", y);
        nbt.setInteger("z", z);
        nbt.setInteger("side", side);
        nbt.setBoolean("noSide", noSide);
    }
    public void readFromNBT(NBTTagCompound nbt){
        x=nbt.getInteger("x");
        y=nbt.getInteger("y");
        z=nbt.getInteger("z");
        side=nbt.getInteger("side");
        noSide=nbt.getBoolean("noSide");
    }
}
