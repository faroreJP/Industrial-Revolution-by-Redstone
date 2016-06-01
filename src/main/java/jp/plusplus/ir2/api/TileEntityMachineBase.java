package jp.plusplus.ir2.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.tileentities.IDirectional;
import jp.plusplus.ir2.tileentities.IStateChangeable;
import jp.plusplus.ir2.tileentities.MultiBlockPosition;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by plusplus_F on 2015/02/01.
 * RS機械の基底クラス。ブロックと同じくひどい実装だ。
 * RS機械追加の際は、無理にこのクラスの全てを解読する必要は無い。
 * このクラスを継承し、void work()とboolean canWork()をオーバーライドすれば最低限のものはできるはずだ。
 */
public class TileEntityMachineBase extends TileEntity implements IConductor,IDirectional,IStateChangeable {
    public static final int PROGRESS_SCALE=10000;

    public int progress;                   //進捗
    public int renderingTicks;        //描画用Ticks 最大値はPROGRESS_SCALE*workAmount
    public byte side;                    //機械がどこを向いているか
    public byte sideCarrying;         //機械の搬出面
    public boolean isPowered;         //バニラRS入力を受けているか
    public boolean lastCanWork;       //canWork()の結果のキャッシュみたいなもん
    public short noneProgressTicks;     //機械のprogressが増えなかったtick数
    public boolean hasUpdatedCanWork;   //lastCanWorkが更新されたか

    public int workAmount;      // 仕事に必要なカウント (入力周波数)*(1度の作業にかかる秒数) を代入する
    public byte connectState;   //接続状態。最下位からそれぞれのbitが各方向に対応:00EWSNUD
    public short rss;           //入力されてるRSS
    public short frequency;     //入力されてる周波数

    protected LinkedList<MultiBlockPosition> multiBlocks; //接続ブロックのリスト


    //-----------------------------------------------------------
    //           最低限書く必要のあるところ
    //-----------------------------------------------------------

    /**
     * 機械が行う仕事を記述する。
     * ここで製錬やらなにやらの処理を書く。
     * work()はcanWork()==trueが成り立つ状態でのみ呼ばれる
     * work()の後はmarkDirty()が呼ばれる
     */
    protected void work(){}

    /**
     * 機械が動作できるかの判定を行う。
     * 入力信号の定格チェックは事前に行われており、
     * ここでやるべきことは入力信号以外で機械が動作するりのに必要な条件の確認
     * @return
     */
    public boolean canWork(){
        return false;
    }

    /**
     * 機械が動作できる状態にあるかを返す。
     * @return trueのとき、大体動作している
     */
    public boolean isWorking(){
        BlockMachineBase bm=(BlockMachineBase)getBlockType();
        return lastCanWork && rss>=bm.getMinRSS() && frequency>=bm.getMinFrequency();
    }

    /**
     * 使うな
     * @return
     */
    public String getGuiFileName(){
        return "processor.png";
    }

    /**
     * GUIでの文字色を返す
     * @return
     */
    public int getStringColor(){
        return 0x404040;
    }

    /**
     * ContainerProcessor,GuiProcessorを用いる場合、完成品スロットが経験値を発生させるかどうかを返す
     * @return 完成品スロットあり
     */
    public boolean hasSlotWithEXP(){ return true; }

    /**
     * 搬出面を持つかを返す
     * @return 搬出面アリ
     */
    public boolean hasCarryingSide(){ return true; }


    //-----------------------------------------------------------
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        progress             =   par1NBTTagCompound.getInteger("Progress");
        renderingTicks      =   par1NBTTagCompound.getInteger("RenderingTicks");
        side                  =  par1NBTTagCompound.getByte("Side");
        sideCarrying         =  par1NBTTagCompound.getByte("SideCarrying");
        isPowered            =   par1NBTTagCompound.getBoolean("IsPowered");
        noneProgressTicks   =   par1NBTTagCompound.getShort("NoneProgressTicks");
        workAmount          =   par1NBTTagCompound.getInteger("WorkAmount");
        connectState        =   par1NBTTagCompound.getByte("ConnectState");
        rss                   =  par1NBTTagCompound.getShort("RSS");
        frequency            =  par1NBTTagCompound.getShort("Frequency");
        lastCanWork         =   par1NBTTagCompound.getBoolean("LastCanWork");
        hasUpdatedCanWork  =   par1NBTTagCompound.getBoolean("HasUpdatedCanWork");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("Progress", progress);
        par1NBTTagCompound.setInteger("RenderingTicks", renderingTicks);
        par1NBTTagCompound.setByte("Side", side);
        par1NBTTagCompound.setByte("SideCarrying", sideCarrying);
        par1NBTTagCompound.setBoolean("IsPowered", isPowered);
        par1NBTTagCompound.setShort("NoneProgressTicks", noneProgressTicks);
        par1NBTTagCompound.setInteger("WorkAmount", workAmount);
        par1NBTTagCompound.setByte("ConnectState", connectState);
        par1NBTTagCompound.setShort("RSS", rss);
        par1NBTTagCompound.setShort("Frequency", frequency);
        par1NBTTagCompound.setBoolean("LastCanWork", lastCanWork);
        par1NBTTagCompound.setBoolean("HasUpdatedCanWork", hasUpdatedCanWork);
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

    //-----------------------------------------------------------
    public LinkedList<MultiBlockPosition> getMultiBlocks(){
        if(multiBlocks!=null) return multiBlocks;

        LinkedList<MultiBlockPosition> ret=new LinkedList<MultiBlockPosition>();
        Block b=getBlockType();
        if(b instanceof BlockMachineBase){
            BlockMachineBase mb=(BlockMachineBase)b;
            ret=mb.getMultiBlockPositions(worldObj, xCoord, yCoord, zCoord);
        }
        multiBlocks=ret;
        return ret;
    }

    //-----------------------------------------------------------
    public void markUpdateCanWorkState(){
        hasUpdatedCanWork=false;
    }

    //-----------------------------------------------------------
    @Override
    public void updateEntity() {
        /*
        short preRSS = rss;
        updateConnectState();
        if (connectState == 0) {
            rss = 0;
            frequency = 0;
        } else {
            updateInput();
        }
        */

        //--------------------------仕事可能状態の更新--------------------------
        if (!hasUpdatedCanWork) {
            boolean b = canWork();

            if (b != lastCanWork) {
                if (!worldObj.isRemote)
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 2);

                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            lastCanWork = b;
            hasUpdatedCanWork = true;

            //FMLLog.info("updated state:"+lastCanWork);
        }

        //----------------------------描画用カウンタの更新--------------------------------------
        if (isWorking()) {
            int w = workAmount * PROGRESS_SCALE;
            renderingTicks += (int) (frequency * PROGRESS_SCALE / 20.0);
            if (renderingTicks > w) {
                renderingTicks = w;
            }
        }

        //---------------------------RS信号入力による仕事の更新------------------------------
        if (worldObj.isRemote) return;
        Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
        int m = this.blockMetadata;
        if (b != null && b instanceof BlockMachineBase) {
            BlockMachineBase bm = (BlockMachineBase) b;
            if (rss > bm.getMaxRSS()) {
                //爆発処理
                if (IR2.machinesExplode) {
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                    worldObj.createExplosion(null, xCoord, yCoord, zCoord, 2.25f, false);
                } else {
                    ArrayList<ItemStack> items = b.getDrops(worldObj, xCoord, yCoord, zCoord, m, 0);
                    for (int i = 0; i < items.size(); i++) {
                        ItemStack itemstack = items.get(i);

                        float f = bm.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = bm.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = bm.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0) {
                            int k1 = bm.rand.nextInt(21) + 10;

                            if (k1 > itemstack.stackSize) {
                                k1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= k1;
                            EntityItem entityitem = new EntityItem(worldObj, (double) ((float) xCoord + f), (double) ((float) yCoord + f1), (double) ((float) zCoord + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()) {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double) ((float) bm.rand.nextGaussian() * f3);
                            entityitem.motionY = (double) ((float) bm.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double) ((float) bm.rand.nextGaussian() * f3);
                            worldObj.spawnEntityInWorld(entityitem);
                        }
                    }

                    worldObj.func_147480_a(xCoord, yCoord, zCoord, false);
                }
            } else if (lastCanWork) {
                //パルスでの動作
                if (rss >= bm.getMinRSS() && frequency > 0 && frequency >= bm.getMinFrequency() && frequency <= bm.getMaxFrequency()) {
                    workByRedstonePulse();
                }
            }
        }

        //-----------------------------RS入力がされていない場合の進捗の減少処理------------------------------
        if (progress > 0 && rss == 0 && !isPowered) {
            noneProgressTicks++;
            if (noneProgressTicks > 20 * 5) {
                int minus = PROGRESS_SCALE / 20;

                b = worldObj.getBlock(xCoord, yCoord, zCoord);
                if (b instanceof BlockMachineBase) {
                    minus = PROGRESS_SCALE * ((BlockMachineBase) b).getMinFrequency() / 20;
                }

                progress -= minus;
                if (progress < 0) progress = 0;
                if (progress == 0) renderingTicks = 0;
            }
        } else {
            noneProgressTicks = 0;
        }
    }
    private void updateConnectState(){
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


        //       接続ブロックからの入力
        //-------------------------------------------------
        for(MultiBlockPosition pos : getMultiBlocks()){
            TileEntity entity=worldObj.getTileEntity(pos.x, pos.y, pos.z);
            if(entity instanceof TileEntityMultiBlock){
                TileEntityMultiBlock te=(TileEntityMultiBlock)entity;

                if(inputRSS<te.rss)	{
                    inputRSS=te.rss;
                    if(inputFrequency==0 || inputFrequency>te.freq)	inputFrequency=te.freq;
                }
            }
        }

        /*
        //       入力データの更新
        //-------------------------------------------------
        if(inputRSS>0 || inputFrequency>0){
            short mr=getMaxRSS(), mf=getMaxFrequency();
            if(inputFrequency>mf){
                inputFrequency=0;
            }
        }
        */

        rss=inputRSS;
        frequency=inputFrequency;
    }

    @Override
    public void markDirty(){
        markUpdateCanWorkState();
        super.markDirty();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        Block block=getBlockType();
        if(block instanceof BlockMachineBase){
            BlockMachineBase b=(BlockMachineBase)block;
            int r=Math.max(b.multiWidth, b.multiDepth);
            return AxisAlignedBB.getBoundingBox(xCoord-r, yCoord, zCoord-r, xCoord+r+1, yCoord+b.multiHeight+1, zCoord+r+1);
        }

        return super.getRenderBoundingBox();
    }

    //-----------------------------------------------------------
    public void workByRedstoneSignal(){
        if(lastCanWork){
            int w=getScaledWorkAmount();
            progress+=PROGRESS_SCALE;
            renderingTicks+=PROGRESS_SCALE;
            if(progress>=w){
                work();
                if(hasUpdatedCanWork) markUpdateCanWorkState();
                progress-=w;
                renderingTicks=progress;
                //worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 2);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }
    public void workByRedstonePulse() {
        boolean flag=false;
        int w=getScaledWorkAmount();
        int ww=(int) (frequency * PROGRESS_SCALE / 20.0);
        progress += ww;
        //renderingTicks+=ww;
        while(progress >= w){
            work();
            if(hasUpdatedCanWork) markUpdateCanWorkState();
            progress-=w;
            //renderingTicks-=w;
            flag=true;
        }
        if(flag) {
            renderingTicks=progress;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public int getProgressScaled(int par1){
        //return this.progress/PROGRESS_SCALE*par1/workAmount;
        return (int)(par1*(float)progress/getScaledWorkAmount());
    }
    public int getScaledWorkAmount(){
        return workAmount*PROGRESS_SCALE;
    }
    public float getRenderingTicksRate(){ return (float)renderingTicks/getScaledWorkAmount(); }

    @Override
    public boolean change() {
        byte pc=connectState;
        short pr=rss, pf=frequency;

        rss=frequency=0;

        int con = 0;
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            TileEntity entity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

            if (entity instanceof IConductor) {
                con = (con | (1 << i));
                IConductor ic=(IConductor)entity;

                short tr=ic.getOutputRSS(i);
                short tf=ic.getOutputFrequency(i);
                if(rss<tr){
                    rss=tr;
                    frequency=tf;
                }
                else if(rss==tr && tf<frequency){
                    frequency=tf;
                }
            }
        }
        connectState = (byte) con;

        //       接続ブロックからの入力
        //-------------------------------------------------
        for(MultiBlockPosition pos : getMultiBlocks()){
            TileEntity entity=worldObj.getTileEntity(pos.x, pos.y, pos.z);
            if(entity instanceof TileEntityMultiBlock){
                TileEntityMultiBlock te=(TileEntityMultiBlock)entity;

                short tr=te.rss;
                short tf=te.freq;
                if(rss<tr){
                    rss=tr;
                    frequency=tf;
                }
                else if(rss==tr && tf<frequency){
                    frequency=tf;
                }
            }
        }

        return connectState!=pc || pr!=rss || pf!=frequency;
    }

    //-------------------------IConductor------------------------
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
        Block b= worldObj.getBlock(xCoord, yCoord, zCoord);
        if(!worldObj.isAirBlock(xCoord, yCoord, zCoord) && b instanceof BlockMachineBase){
            return (short)((BlockMachineBase)b).getMaxRSS();
        }
        return 0;
    }
    @Override
    public short getMaxFrequency() {
        Block b= worldObj.getBlock(xCoord, yCoord, zCoord);
        if(!worldObj.isAirBlock(xCoord, yCoord, zCoord) && b instanceof BlockMachineBase){
            return (short)((BlockMachineBase)b).getMaxFrequency();
        }
        return 0;
    }
    @Override
    public boolean canConnect(int side){
        return this.side!=side;
    }

    //-----------------------IDirectional-------------------------------
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
        return side==s;
    }

}
