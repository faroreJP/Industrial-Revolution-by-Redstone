package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.blocks.BlockCable;
import jp.plusplus.ir2.blocks.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/01/31.
 * ケーブル
 */
public class TileEntityCable extends TileEntity implements IConductor, IStateChangeable {
    /*
     * cable connection (old)
     * 00000000
     *   tbnwse
     *
     *   t:top
     *   b:bottom
     *   n:north
     *   w:west
     *   s:sorth
     *   e:east
     */
    public byte connectState; //接続情報、下位8bitが六方向に対応
    public byte connectStateToCable; //ケーブルとの接続情報
    protected byte connectStateDisable; //パイプの接続不可面
    public boolean connectMachineOne; //機械1つのみと繋がっているか
    public short rss;
    public short frequency;
    public boolean hasUpdated=false; //接続状態が更新されたか否か
    public int ticks; //なんだこのフィールドは。俺は知らん
    public int metal; //描画用、中心部の金属の色

    public TileEntityCable() {
        connectState = 0;
        rss = 0;
        frequency = 0;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        // ConnectState,RSS,Frequency
        connectState = par1NBTTagCompound.getByte("ConnectState");
        connectStateDisable=par1NBTTagCompound.getByte("ConnectStateDisable");
        connectStateToCable = par1NBTTagCompound.getByte("ConnectStateToCable");
        connectMachineOne=par1NBTTagCompound.getBoolean("ConnectMachineOne");
        rss = par1NBTTagCompound.getShort("RSS");
        frequency = par1NBTTagCompound.getShort("Frequency");
        hasUpdated=par1NBTTagCompound.getBoolean("HasUpdatedConnectState");
        metal=par1NBTTagCompound.getInteger("Metal");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        // ConnectState,RSS,Frequency
        par1NBTTagCompound.setByte("ConnectState", connectState);
        par1NBTTagCompound.setByte("ConnectStateDisable", connectStateDisable);
        par1NBTTagCompound.setByte("ConnectStateToCable", connectStateToCable);
        par1NBTTagCompound.setBoolean("ConnectMachineOne", connectMachineOne);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", frequency);
        par1NBTTagCompound.setBoolean("HasUpdatedConnectState", hasUpdated);
        par1NBTTagCompound.setInteger("Metal", metal);
    }

    public void markUpdateConnectingState(){
        hasUpdated=false;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
    @Override
    public void updateEntity() {
        /*
        rss = 0;
        frequency = 0;

        if(!hasUpdated){
            updateConnectState2();
            //Block b=worldObj.getBlock(xCoord, yCoord, zCoord);
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            hasUpdated=true;
        }
        //
        if (connectState != 0) {
            for (int i = 0; i < 6; i++) {
                if (((connectState >> i) & 0x1) != 0) {
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

                    if (entity instanceof IConductor) {
                        updateInput2((IConductor) entity, dir.ordinal());
                    }
                }
            }
        }
        */
    }

    @Override
    public boolean change() {
        int con = 0, con2=0;
        int cCount=0;
        int color = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

        //中心部の金属の色の決定
        Block b=worldObj.getBlock(xCoord,yCoord,zCoord);
        if(b== BlockCore.cableTin) metal=0;
        else if(b==BlockCore.cableCopper) metal=1;
        else if(b==BlockCore.cableIron) metal=2;
        else if(b==BlockCore.cableSilver) metal=3;
        else if(b==BlockCore.cableGold) metal=4;
        else if(b==BlockCore.cableAluminium) metal=5;
        else if(b==BlockCore.cableNickel) metal=6;

        short pr=rss, pf=frequency;
        byte pc=connectState, pc2=connectStateToCable;

        rss=frequency=0;

        //ケーブルの接続状態と入力を更新する
        for(int i=0;i<6;i++){
            if(((connectStateDisable>>i)&1)!=0) continue;

            boolean flag = false;
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof TileEntityPipeBase || entity instanceof TileEntityPipeFluid) {
                //パイプとは無条件に繋がる
                flag = true;
            } else if (entity instanceof TileEntityCable) {
                //ケーブルとは同じ色かどうか判定する
                int c = worldObj.getBlockMetadata(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                if(c==color){
                    flag = true;
                    con2=(con2|(1<<i));
                    cCount+=10;
                }
            } else if (entity instanceof IConductor) {
                //それ以外は接続可能か判定する
                flag = ((IConductor) entity).canConnect(dir.getOpposite().ordinal());
            }

            if (flag) {
                con = (con | (1 << i));
                cCount++;

                //ついでに入力も更新する
                if (entity instanceof IConductor) {
                    updateInput2((IConductor) entity, dir.ordinal());
                }
            }
        }

        connectState = (byte) con;
        connectStateToCable=(byte)con2;
        connectMachineOne=(cCount==1);

        return pr!=rss || pf!=frequency || pc!=connectState || pc2!=connectStateToCable;
    }

    public void updateConnectState2() {

        int con = 0, con2=0;
        int cCount=0;
        int color = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

        //中心部の金属の色の決定
        Block b=worldObj.getBlock(xCoord,yCoord,zCoord);
        if(b== BlockCore.cableTin) metal=0;
        else if(b==BlockCore.cableCopper) metal=1;
        else if(b==BlockCore.cableIron) metal=2;
        else if(b==BlockCore.cableSilver) metal=3;
        else if(b==BlockCore.cableGold) metal=4;
        else if(b==BlockCore.cableAluminium) metal=5;
        else if(b==BlockCore.cableNickel) metal=6;

        //接続情報の更新
        for (int i = 0; i < 6; i++) {
            if(((connectStateDisable>>i)&1)!=0) continue;

            boolean flag = false;
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof TileEntityPipeBase || entity instanceof TileEntityPipeFluid) {
                flag = true;
            } else if (entity instanceof TileEntityCable) {
                int c = worldObj.getBlockMetadata(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                if(c==color){
                    flag = true;
                    con2=(con2|(1<<i));
                    cCount+=10;
                }
            } else if (entity instanceof IConductor) {
                flag = ((IConductor) entity).canConnect(dir.getOpposite().ordinal());
            }

            if (flag) {
                con = (con | (1 << i));
                cCount++;
            }
        }
        connectState = (byte) con;
        connectStateToCable=(byte)con2;
        connectMachineOne=(cCount==1);
        //FMLLog.info("updated! con:"+connectState);
        //markDirty();
    }

    protected void updateInput2(IConductor entity, int dir) {
        short r = entity.getOutputRSS(dir ^ 1);
        short f = entity.getOutputFrequency(dir ^ 1);

        if (rss < r) { // 入力が現在のRSSより大きい場合、その信号で上書きされる
            rss = r;
            frequency = f;
        } else if (rss == r && frequency > f) { // 入力が同じRSSで、周波数が低い場合、周波数は低くなる
            frequency = f;
        }
    }

    @Override
    public short getOutputRSS(int direction) {
        if(rss>getMaxRSS()) return (short)(getMaxRSS()-1);
        if (rss > 1)        return (short) (rss - 1);
        return 0;
    }

    @Override
    public short getOutputFrequency(int direction) {
        if(rss<=1)  return 0;
        if(frequency>getMaxFrequency()) return 0;
        return frequency;
    }

    @Override
    public short getMaxRSS() {
        Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
        if (b instanceof BlockCable) {
            return ((BlockCable) b).maxRSS;
        }
        return 0;
    }

    @Override
    public short getMaxFrequency() {
        Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
        if (b instanceof BlockCable) {
            return ((BlockCable) b).maxFrequency;
        }
        return 0;
    }

    @Override
    public boolean canConnect(int side) {
        return ((connectStateDisable>>side)&1)==0;
    }


    public boolean hammer(ItemStack item, EntityPlayer player, World world, int side) {
        int con=connectStateDisable;
        for(int i=0;i<6;i++){

            if(i==side){
                //触れた面
                if((con&1)!=0){
                    connectStateDisable=(byte)((~(1<<i))&connectStateDisable);
                }
                else{
                    connectStateDisable=(byte)((1<<i)|connectStateDisable);
                }
            }
            con=(con>>1);
        }
        markUpdateConnectingState();
        return true;
    }
}
