package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/09/18.
 */
public class TileEntityShop extends TileEntity implements IConductor, IDirectional {
    public byte connectState;   //接続状態。最下位からそれぞれのbitが各方向に対応:00EWSNUD
    public short rss;           //入力されてるRSS
    public short frequency;     //入力されてる周波数
    public byte side;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        connectState        =   par1NBTTagCompound.getByte("ConnectState");
        rss                   =  par1NBTTagCompound.getShort("RSS");
        frequency            =  par1NBTTagCompound.getShort("Frequency");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("ConnectState", connectState);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", frequency);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound tag=new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        this.readFromNBT(pkt.func_148857_g());
    }

    //---------------------------------------------------------------------

    public boolean canWork(){
        return rss>0 && frequency>0;
    }

    @Override
    public void updateEntity(){
        updateInput();
    }

    public void updateConnectState(){
        byte dir[]={5,3,4,2};
        int con=0;
        int x,y,z;
        // left,right,front,back
        y=yCoord;
        for(int i=0;i<4;i++){
            int[] shift={1,0,-1,0};
            x=xCoord+shift[i];
            z=zCoord+shift[(i+3)%4];
            if(!worldObj.isAirBlock(x, y, z)){
                TileEntity e=worldObj.getTileEntity(x, y, z);
                if(e!=null && e instanceof IConductor && side!=dir[i]){
                    con=(con|(1<<i));
                }
            }
        }
        //top,bottom
        x=xCoord;
        z=zCoord;
        for(int i=0;i<2;i++){
            y=yCoord-1+2*i;
            if(!worldObj.isAirBlock(x, y, z)){
                TileEntity e=worldObj.getTileEntity(x, y, z);
                if(e!=null && e instanceof IConductor){
                    con=(con|(1<<(4+i)));
                }
            }
        }
        connectState = (byte)con;
    }
    private void updateInput(){
        short inputRSS=0, inputFrequency=0;

        //         ケーブルからの入力
        //------------------------------------------------
        if((connectState&1)!=0){
            //east
            TileEntity e=worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(4);
                short cf=c.getOutputFrequency(4);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }
        if((connectState&2)!=0){
            //south
            TileEntity e=worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(2);
                short cf=c.getOutputFrequency(2);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }
        if((connectState&4)!=0){
            //west
            TileEntity e=worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(5);
                short cf=c.getOutputFrequency(5);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }
        if((connectState&8)!=0){
            //north
            TileEntity e=worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(3);
                short cf=c.getOutputFrequency(3);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }
        if((connectState&16)!=0){
            //bottom
            TileEntity e=worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(1);
                short cf=c.getOutputFrequency(1);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }
        if((connectState&32)!=0){
            //top
            TileEntity e=worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if(e!=null && e instanceof IConductor){
                IConductor c=(IConductor)e;
                short cr=c.getOutputRSS(0);
                short cf=c.getOutputFrequency(0);
                if(inputRSS<cr)			inputRSS=cr;
                if(inputFrequency<cf)	inputFrequency=cf;
            }
        }

        rss=inputRSS;
        frequency=inputFrequency;
    }


    //----------------------------- IConductor------------------------------
    @Override
    public short getOutputRSS(int direction) {
        return 0;
    }
    @Override
    public short getOutputFrequency(int direction) {
        return 0;
    }
    @Override
    public short getMaxRSS() {
        return 2048;
    }
    @Override
    public short getMaxFrequency() {
        return 2048;
    }
    @Override
    public boolean canConnect(int side) {
        return this.side!=side;
    }

    //------------------------------------------------------------

    @Override
    public byte getSide() {
        return side;
    }

    @Override
    public void setSide(byte s) {
        side=s;
    }

    @Override
    public boolean isFront(byte s) {
        return s==side;
    }
}
