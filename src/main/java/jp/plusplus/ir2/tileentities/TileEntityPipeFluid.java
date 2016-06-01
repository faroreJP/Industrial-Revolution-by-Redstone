package jp.plusplus.ir2.tileentities;

import jp.plusplus.ir2.api.IConductor;
import jp.plusplus.ir2.api.IPipe;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.blocks.BlockPipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/05/23.
 * 流体パイプの基底。大体PipeBaseと同じ実装
 */
public class TileEntityPipeFluid extends TileEntityCable implements IPipe {
    public static final int PACKET_MAX_CAPACITY=32000;
    public static final int DRAW_TICKS_MAX=10;

    protected LinkedList<PacketFluidStack> fluidQueue;
    public byte connectStatePipe;
    protected short offerCount;
    protected short transportDelay=0;
    protected Random random;
    protected int nextDirectionIndex;
    protected LinkedList<Integer> connectDirections;
    protected int delay=0;
    protected int amountAll;
    protected int pIndex=-1;
    protected int pIndexCenter=-1;
    protected int[] drawTicks=new int[7];
    protected int[] drawTicksBuffer=new int[7];
    protected IIcon[] iconBuffer=new IIcon[7];

    public TileEntityPipeFluid(){
        fluidQueue=new LinkedList<PacketFluidStack>();
        random=new Random();
        connectDirections=new LinkedList<Integer>();
    }

    public void offerPacket(PacketFluidStack packet){
        if(packet!=null && packet.getFluidStack()!=null && packet.getFluidStack().amount>0) {
            fluidQueue.offer(packet);
            offerCount++;
            amountAll+=packet.getFluidStack().amount;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Fluid");
        PacketFluidStack[] fluidStacks=new PacketFluidStack[nbttaglist.tagCount()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<nbttaglist.tagCount()){
                fluidStacks[b0]=new PacketFluidStack(null);
                fluidStacks[b0].readFromNBT(nbt);
                /*
                fluidStacks[b0]=new PacketFluidStack(FluidStack.loadFluidStackFromNBT(nbt));
                fluidStacks[b0].setDirection(nbt.getByte("Direction"));
                */
            }
        }

        fluidQueue=new LinkedList<PacketFluidStack>();
        for(int i=0;i<fluidStacks.length;i++){
            fluidQueue.offer(fluidStacks[i]);
        }

        offerCount=par1NBTTagCompound.getShort("OfferCount");
        transportDelay=par1NBTTagCompound.getShort("TransportDelay");
        nextDirectionIndex =par1NBTTagCompound.getInteger("NextDirection");
        amountAll=par1NBTTagCompound.getInteger("AmountAll");
        for(int i=0;i<drawTicks.length;i++) drawTicks[i]=par1NBTTagCompound.getInteger("DrawTicks"+i);
        for(int i=0;i<drawTicks.length;i++) drawTicksBuffer[i]=par1NBTTagCompound.getInteger("DrawTicksBuffer"+i);
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        int size=fluidQueue.size();
        for(int i=0;i<size;i++){
            PacketFluidStack packet=fluidQueue.get(i);
            if(packet!=null && packet.getFluidStack()!=null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                packet.writeToNBT(nbt);
                /*
                packet.getFluidStack().writeToNBT(nbt);
                nbt.setInteger("Direction", packet.getDirection());
                */
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Fluid", nbttaglist);

        par1NBTTagCompound.setShort("OfferCount", offerCount);
        par1NBTTagCompound.setShort("TransportDelay", transportDelay);
        par1NBTTagCompound.setInteger("NextDirection", nextDirectionIndex);
        par1NBTTagCompound.setInteger("AmountAll", amountAll);
        for(int i=0;i<drawTicks.length;i++) par1NBTTagCompound.setInteger("DrawTicks"+i, drawTicks[i]);
        for(int i=0;i<drawTicks.length;i++) par1NBTTagCompound.setInteger("DrawTicksBuffer"+i, drawTicksBuffer[i]);
    }

    @Override
    public byte getConnectState() {
        return connectState;
    }

    @Override
    public byte getConnectStateDisable() {
        return connectStateDisable;
    }

    @Override
    public Iterator<PacketFluidStack> getPackets(){
        return fluidQueue.iterator();
    }

    @Override
    public int getTextureIndex() {
        if(pIndex!=-1) return pIndex;

        BlockPipe b=(BlockPipe)getBlockType();
        if(b.maxRSS== ((BlockPipe) BlockCore.pipeTin).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeTin).maxFrequency) pIndex=0;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeCopper).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeCopper).maxFrequency) pIndex=1;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeIron).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeIron).maxFrequency) pIndex=2;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeSilver).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeSilver).maxFrequency) pIndex=3;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeGold).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeGold).maxFrequency) pIndex=4;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeAluminium).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeAluminium).maxFrequency) pIndex=5;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeNickel).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeNickel).maxFrequency) pIndex=6;
        else pIndex=0;

        //10ずらすと流体パイプの位置になる
        pIndex+=10;

        return pIndex;
    }

    @Override
    public int getTextureCenterIndex() {
        if(pIndexCenter!=-1) return pIndexCenter;

        BlockPipe b=(BlockPipe)getBlockType();
        if(b.maxRSS== ((BlockPipe)BlockCore.pipeTin).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeTin).maxFrequency) pIndexCenter=0;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeCopper).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeCopper).maxFrequency) pIndexCenter=1;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeIron).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeIron).maxFrequency) pIndexCenter=2;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeSilver).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeSilver).maxFrequency) pIndexCenter=3;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeGold).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeGold).maxFrequency) pIndexCenter=4;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeAluminium).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeAluminium).maxFrequency) pIndexCenter=5;
        else if(b.maxRSS== ((BlockPipe)BlockCore.pipeNickel).maxRSS && b.maxFrequency== ((BlockPipe)BlockCore.pipeNickel).maxFrequency) pIndexCenter=6;
        else pIndexCenter=0;

        return pIndexCenter;
    }
    @Override
    public PipeType getPipeType() {
        return PipeType.NORMAL;
    }

    @Override
    public int getPipeSide() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        boolean flag=false;
        short r=getOutputRSS(-1), f=getOutputFrequency(-1);
        if(r>0) {
            Iterator<PacketFluidStack> it=getPackets();

            //各パケットに対する処理
            boolean[] drawFlag=new boolean[drawTicks.length];

            drawFlag[6]=it.hasNext();

            while(it.hasNext()){
                PacketFluidStack packet=it.next();
                if(packet==null) continue;

                if(packet.getDirection()!=-1) {
                    if (packet.isReversed()) drawFlag[packet.getDirection() ^ 1] = true;
                    else drawFlag[packet.getDirection()] = true;
                }

                if(packet.update(1.0f)){
                    if(packet.isReversed()){
                        packet.setTicks(packet.getTicks()-PacketBase.TICK_MAX);

                        //行き先がある場合のみいろいろ設定
                        int dir=decideDestination(packet);
                        if(dir!=-1){
                            packet.setReverse(false);
                            packet.setDirection(dir);
                        }
                        else onDestinationNotFound(it);
                    }
                    else{
                        //行き先へアイテムを送る
                        if(!worldObj.isRemote) transportPacket3(packet);
                        it.remove();
                    }
                    flag=true;
                }
            }
            if(flag){
                worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
                worldObj.notifyBlockChange(xCoord,yCoord,zCoord,getBlockType());
            }

            //描画周り
            int hMax=0;
            for(int i=0;i<drawTicks.length;i++){
                if(drawFlag[i]){
                    if(drawTicks[i]<DRAW_TICKS_MAX) drawTicks[i]++;
                    drawTicksBuffer[i]=20;
                }
                else{
                    if(drawTicksBuffer[i]>0) drawTicksBuffer[i]--;
                    else if(drawTicks[i]>0){
                        drawTicks[i]--;
                        if(drawTicks[i]==0) iconBuffer[i]=null;
                    }
                }
                if(hMax<drawTicks[i]) hMax=drawTicks[i];
            }
        }
    }

    public float getDrawTicksScaled(int index, float scale){
        return scale*drawTicks[index]/DRAW_TICKS_MAX;
    }

    @Override
    public void updateConnectState2(){
        int con=0;
        int con2=0;
        connectDirections.clear();

        for(int i=0;i<6;i++){
            if(((connectStateDisable>>i)&1)!=0) continue;

            boolean flag=false;
            ForgeDirection dir=ForgeDirection.getOrientation(i);
            TileEntity entity=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);

            if(entity instanceof TileEntityPipeFluid){
                //流体パイプとの接続判定
                flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                if(flag){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }
            else if(entity instanceof TileEntityCable){
                //パイプ・ケーブルとの接続判定
                //flag=((TileEntityCable) entity).canConnect(dir.getOpposite().ordinal());
                flag=true;
            }
            else if(entity instanceof IConductor){
                //その他
                flag=((IConductor)entity).canConnect(dir.getOpposite().ordinal());
            }

            if(entity instanceof IFluidHandler){
                //FluidHandlerとの判定
                boolean flag2=true;
                if(entity instanceof TileEntityMachineBase){
                    if(!((TileEntityMachineBase) entity).canConnect(dir.getOpposite().ordinal())){
                        flag2=false;
                    }
                }
                if(flag2){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }

            /*
            if(entity instanceof IFluidHandler || entity instanceof TileEntityPipeFluid){
                boolean flag2=true;
                if(entity instanceof TileEntityMachineBase){
                    if(!((TileEntityMachineBase) entity).canConnect(dir.getOpposite().ordinal())){
                        flag2=false;
                    }
                }
                if(flag2){
                    con2=(con2|(1<<i));
                    connectDirections.add(dir.ordinal());
                }
            }
            */

            if(flag){
                con = (con | (1 << i));
                updateInput2((IConductor) entity, dir.ordinal());
            }
        }
        connectState = (byte)(con|con2);
        connectStatePipe = (byte)con2;
    }
    protected  boolean transportPacket2() {
        if (fluidQueue.isEmpty()) return false;
        PacketFluidStack packet = fluidQueue.peek();
        if(packet==null){
            fluidQueue.poll();
            return false;
        }
        int conCount = connectDirections.size();

        if (conCount == 0) {
            return false;
        }
        nextDirectionIndex %= conCount;

        //decides destination
        int dir = -1;
        TileEntity destEntity = null;
        ForgeDirection destDir;
        int t  = connectDirections.get(nextDirectionIndex);

        destDir = ForgeDirection.getOrientation(t);
        destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);
        if (canTransportTo(destEntity, packet.getFluidStack(), t) && packet.whereFrom() != t) {
            dir = t;
        }
        else{
            for (int n = (nextDirectionIndex + 1) % conCount; n != nextDirectionIndex; n = (n + 1) % conCount) {
                t = connectDirections.get(n);
                destDir = ForgeDirection.getOrientation(t);
                destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);

                if (canTransportTo(destEntity, packet.getFluidStack(), t) && packet.whereFrom() != t) {
                    dir = t;
                    break;
                }
            }
            if(dir!=-1) {
                nextDirectionIndex = connectDirections.indexOf(dir);
            }
        }

        if (dir == -1) {
            nextDirectionIndex = 0;
            dir=packet.whereFrom();
        }

        if (!canTransportTo(destEntity, packet.getFluidStack(), dir)) {
            return false;
        }
        transportTo(destEntity, fluidQueue.poll(), dir);
        nextDirectionIndex++;
        return true;
    }
    protected int decideDestination(PacketFluidStack packet){
        int conCount = connectDirections.size();

        //来た道しかなかったら即失敗
        if (conCount <= 1) {
            return -1;
        }

        //インデックスが最大接続数を超えないように
        nextDirectionIndex %= conCount;

        //方向の決定
        int dir=-1;
        int t = connectDirections.get(nextDirectionIndex);
        ForgeDirection destDir = ForgeDirection.getOrientation(t);
        TileEntity destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);
        if (canTransportTo(destEntity, packet.getFluidStack(), t) && packet.whereFrom() != t) {
            dir = t;
        }
        else{
            //なんか複雑なループだけど要は最初のインデックス以外の接続先インデックス全てを見て回ってる
            for (int n = (nextDirectionIndex + 1) % conCount; n != nextDirectionIndex; n = (n + 1) % conCount) {
                t = connectDirections.get(n);
                destDir = ForgeDirection.getOrientation(t);
                destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);

                if (canTransportTo(destEntity, packet.getFluidStack(), t) && packet.whereFrom() != t) {
                    dir = t;
                    break;
                }
            }
            if(dir!=-1) {
                nextDirectionIndex = connectDirections.indexOf(dir);
            }
        }

        //仕上げ
        if(dir==-1) nextDirectionIndex = 0;
        nextDirectionIndex++;

        return dir;
    }
    protected  void transportPacket3(PacketFluidStack packet) {
        if(packet==null){
            return;
        }

        //搬送する
        ForgeDirection destDir = ForgeDirection.getOrientation(packet.getDirection());
        TileEntity destEntity = worldObj.getTileEntity(xCoord + destDir.offsetX, yCoord + destDir.offsetY, zCoord + destDir.offsetZ);
        transportTo(destEntity, packet, packet.getDirection());
    }

    // VOIDパイプ用
    protected void onDestinationNotFound(Iterator<PacketFluidStack> it){

    }

    public boolean fluidEquals(Fluid f){
        if(fluidQueue.isEmpty()) return true;
        return fluidQueue.getFirst().getFluidStack().getFluid()==f;
    }
    public IIcon getFluidIcon(int index) {
        if(iconBuffer[index]!=null) return iconBuffer[index];

        if (fluidQueue.isEmpty() || drawTicks[index]==0 || fluidQueue.getFirst()==null) return null;
        FluidStack fluid = fluidQueue.getFirst().getFluidStack();
        iconBuffer[index]=(fluid != null && fluid.getFluid() != null ? fluid.getFluid().getIcon() : null);
        return iconBuffer[index];
    }

    protected boolean canTransportTo(TileEntity entity, FluidStack item, int dir){
        if(item==null) return false;

        if(entity instanceof TileEntityPipeFluid){
            //流体パイプ・・・・・・キャパ以下でかつ流体が同じ場合のみ搬入可能
            TileEntityPipeFluid tp=(TileEntityPipeFluid)entity;
            boolean flag=tp.amountAll+item.amount<=PACKET_MAX_CAPACITY;
            return flag && tp.fluidEquals(item.getFluid());
        }
        else if(entity instanceof IFluidHandler){
            IFluidHandler fh=(IFluidHandler)entity;
            ForgeDirection fg=ForgeDirection.getOrientation(dir);
            boolean flag=(fh.canFill(fg, item.getFluid()) && fh.fill(fg, item, false)==item.amount);
            return flag;
        }
        return false;
    }
    protected void transportTo(TileEntity entity, PacketFluidStack packet, int dir){
        if(entity==null || packet==null || dir==-1) return;

        if(entity instanceof TileEntityPipeFluid) {
            TileEntityPipeFluid e = (TileEntityPipeFluid) entity;
            packet.setDirection(dir);
            packet.setReverse(true);
            packet.setTicks(0);
            e.offerPacket(packet);
            amountAll-=packet.getFluidStack().amount;
            //FMLLog.severe("transported");
            return;
        }

        int x = entity.xCoord, y = entity.yCoord, z = entity.zCoord;
        worldObj.markBlockForUpdate(x,y,z);
        worldObj.notifyBlockChange(x,y,z,entity.getBlockType());

        if(entity instanceof IFluidHandler) {
            FluidStack fluid = packet.getFluidStack();
            IFluidHandler fh = (IFluidHandler) entity;
            ForgeDirection d = ForgeDirection.getOrientation(dir);

            int put = fh.fill(d, fluid, false);
            if (put == fluid.amount) {
                fh.fill(d, fluid, true);
                amountAll -= fluid.amount;

                entity.markDirty();
            }
        }
    }
}
