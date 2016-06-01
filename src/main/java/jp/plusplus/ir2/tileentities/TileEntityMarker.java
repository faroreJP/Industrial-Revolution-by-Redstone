package jp.plusplus.ir2.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by plusplus_F on 2015/09/10.
 */
public class TileEntityMarker extends TileEntity {
    public static final int MAX_RANGE=16;
    protected boolean hasParent;
    protected MultiBlockPosition parentPos;

    //範囲 マイナスもあるよ
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        sizeX=par1NBTTagCompound.getByte("SizeX");
        sizeY=par1NBTTagCompound.getByte("SizeY");
        sizeZ=par1NBTTagCompound.getByte("SizeZ");

        hasParent=par1NBTTagCompound.getBoolean("HasParent");
        if(hasParent){
            parentPos=new MultiBlockPosition();
            parentPos.readFromNBT(par1NBTTagCompound.getCompoundTag("ParentPos"));
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("SizeX", (byte) sizeX);
        par1NBTTagCompound.setByte("SizeY", (byte)sizeY);
        par1NBTTagCompound.setByte("SizeZ", (byte)sizeZ);

        par1NBTTagCompound.setBoolean("HasParent", hasParent);
        if(hasParent){
            NBTTagCompound nbt=new NBTTagCompound();
            parentPos.writeToNBT(nbt);
            par1NBTTagCompound.setTag("ParentPos", nbt);
        }
    }

    public TileEntityMarker getParent(){
        TileEntity te=worldObj.getTileEntity(parentPos.x, parentPos.y, parentPos.z);
        if(te instanceof TileEntityMarker){
            return (TileEntityMarker)te;
        }
        return null;
    }

    /**
     * 座標のマーカーに親マーカーを登録する
     * @param x
     * @param y
     * @param z
     * @return 可否
     */
    public boolean setParent(int x, int y, int z){
        TileEntity t=worldObj.getTileEntity(x,y,z);
        if(t instanceof TileEntityMarker){
            TileEntityMarker te=(TileEntityMarker)t;

            if(te.hasParent) return false;

            te.hasParent=true;
            te.parentPos=new MultiBlockPosition(xCoord, yCoord, zCoord, -1);
            return true;
        }

        return false;
    }

    /**
     * 現在の座標をもとになんか相方を探す
     */
    public void setConnection() {
        if(hasParent){
            //親がいる場合、親の処理をする
            getParent().setConnection();
            return;
        }

        //一度全ての接続を解除する
        removeConnection();
        int x = xCoord, y = yCoord, z = zCoord;

        //------------------------- xの+,-方向を探す ----------------------------
        boolean foundX = false;
        for (int i = 0; i < MAX_RANGE; i++) {
            if (getBlockType() == worldObj.getBlock(x + i, y, z) && setParent(x + i, y, z)) {
                foundX = true;
                sizeX = i;
                break;
            }
        }
        if (!foundX) {
            for (int i = 0; i < MAX_RANGE; i++) {
                if (getBlockType() == worldObj.getBlock(x - i, y, z) && setParent(x - i, y, z)) {
                    foundX = true;
                    sizeX = -i;
                    break;
                }
            }
        }

        //---------------------- yの+,-方向を探す ---------------------------
        boolean foundY = false;
        for (int i = 0; i < MAX_RANGE; i++) {
            if (getBlockType() == worldObj.getBlock(x, y + i, z) && setParent(x, y + i, z)) {
                foundY = true;
                sizeY = i;
                break;
            }
        }

        //--------------------- zの+,-方向を探す --------------------------
        boolean foundZ = false;
        for (int i = 0; i < MAX_RANGE; i++) {
            if (getBlockType() == worldObj.getBlock(x, y, z + i) && setParent(x, y, z + i)) {
                foundZ = true;
                sizeZ = i;
                break;
            }
        }
        if (!foundZ) {
            for (int i = 0; i < MAX_RANGE; i++) {
                if (getBlockType() == worldObj.getBlock(x, y, z - i) && setParent(x, y, z - i)) {
                    foundZ = true;
                    sizeZ = -i;
                    break;
                }
            }
        }

        //---------------------- なんかあったら更新しとく ------------------------
        if (foundX || foundY || foundZ) {
            markDirty();
        }
    }

    /**
     * 親側から全ての子との接続を解除する
     */
    void removeConnection(){
        if(sizeX!=0){
            TileEntity te=worldObj.getTileEntity(xCoord+sizeX, yCoord, zCoord);
            if(te instanceof TileEntityMarker){
                ((TileEntityMarker) te).hasParent=false;
                te.markDirty();
            }
            sizeX=0;
        }
        if(sizeY!=0){
            TileEntity te=worldObj.getTileEntity(xCoord, yCoord+sizeY, zCoord);
            if(te instanceof TileEntityMarker){
                ((TileEntityMarker) te).hasParent=false;
                te.markDirty();
            }
            sizeY=0;
        }
        if(sizeZ!=0){
            TileEntity te=worldObj.getTileEntity(xCoord, yCoord, zCoord+sizeZ);
            if(te instanceof TileEntityMarker){
                ((TileEntityMarker) te).hasParent=false;
                te.markDirty();
            }
            sizeZ=0;
        }
    }

    /**
     * マーカーが示している範囲を得る
     * @return
     */
    public AxisAlignedBB getMarkingRange(){
        if(hasParent){
            //親がいる場合、親から取得
            TileEntity te=worldObj.getTileEntity(parentPos.x, parentPos.y, parentPos.z);
            if(te instanceof TileEntityMarker) return ((TileEntityMarker) te).getMarkingRange();
            else return null;
        }
        else if(sizeX!=0 && sizeZ!=0){
            int minX,minY,minZ, maxX,maxY,maxZ;

            minX=(sizeX<0?xCoord+sizeX:xCoord);
            maxX=(sizeX>0?xCoord+sizeX:xCoord);

            minZ=(sizeZ<0?zCoord+sizeZ:zCoord);
            maxZ=(sizeZ>0?zCoord+sizeZ:zCoord);

            if(sizeY!=0){
                minY=1;
                maxY=yCoord;
            }
            else{
                minY=(sizeY<0?yCoord+sizeY:yCoord);
                maxY=(sizeY>0?yCoord+sizeY:yCoord);
            }
            return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        else return null;
    }

}
