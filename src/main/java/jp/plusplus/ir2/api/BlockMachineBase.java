package jp.plusplus.ir2.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.ir2.IR2;
import jp.plusplus.ir2.api.IWrenchHandler;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemWrench;
import jp.plusplus.ir2.packet.IR2PacketHandler;
import jp.plusplus.ir2.packet.MessageUpdateStateChangeable;
import jp.plusplus.ir2.tileentities.MultiBlockPosition;
import jp.plusplus.ir2.api.TileEntityMachineBase;
import jp.plusplus.ir2.tileentities.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/02/01.
 * RS機械のブロック全ての基底クラス
 * 実装がかなり煩雑になっている。感覚で読んでほしい。
 *
 * 利用する場合は、とりあえずRender周りの設定と、マルチブロック関係の値にだけ気をつければ・・・
 */
public class BlockMachineBase extends BlockContainer implements IWrenchHandler {
    /**
     * 説明文のUnlocalizedな名前
     */
    public String infoName;
    /**
     * 説明文の行数 0だと表示されない
     */
    public int infoRow;
    /**
     * 筐体の種類 "Stone" or "Obsidian"
     */
    protected String casingName;

    protected IIcon topIcon;
    protected IIcon bottomIcon;
    protected IIcon sideIcon;
    protected IIcon carryIcon;

    /**
     * 入力定格(0<)
     */
    protected short minRSS;
    /**
     * 入力定格(0<)
     */
    protected short minFrequency;
    /**
     * 入力定格(minRSS<=)
     */
    protected short maxRSS;
    /**
     * 入力定格(minFrequency<=)
     */
    protected short maxFrequency;

    /**
     * マルチブロックの数
     * これらの中に1より大きい値がある場合、その機械はマルチブロックとして扱われる
     */
    public int multiWidth=1, multiHeight=1, multiDepth=1;

    /**
     * いろいろ使う
     */
    public Random rand;

    /**
     * RS機械ブロックを初期化する。
     * @param iName 説明文のUnlocalizedな名前
     * @param iRow 説明文の行数。0なら説明文は表示されない(0<=)
     * @param cName 筐体の種類(Stone or Obsidian)
     */
    protected BlockMachineBase(String iName, int iRow, String cName) {
        super(Material.rock);
        infoName=iName;
        infoRow=iRow;
        casingName=cName;

        setCreativeTab(IR2.tabIR2);
        setStepSound(Block.soundTypeStone);

        if(cName.equals("Obsidian")){
            setHardness(50.0f);
            setResistance(6000.0f);
            setHarvestLevel("pickaxe", 3);
            minRSS=32;
            minFrequency=32;
            //maxRSS=256;
            //maxFrequency=256;
            maxRSS=512;
            maxFrequency=512;
        }
        else{
            setHardness(3.5f);
            setResistance(17.5f);
            setHarvestLevel("pickaxe", 0);
            minRSS=1;
            minFrequency=8;
            maxRSS=32;
            maxFrequency=32;
        }

        rand=new Random();
    }

    /**
     * 機械が右クリックされた場合に呼ばれる。基本の処理はGUIを開く。
     * ここをoverrideするとGUIを持たない機械とか作れる。
     * 引数はonBlockActivateママだがworld.isRemoteは常にfalse
     * @param par1World
     * @param x
     * @param y
     * @param z
     * @param par5EntityPlayer
     */
    public void openGUI(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer){
        par5EntityPlayer.openGui(IR2.instance, -1, par1World, x,y,z);
    }

    @Override
    public Block setBlockName(String name){
        return super.setBlockName("IR2"+name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return null;
    }

    public Block getCasingBlockType(){
        return casingName.equals("Obsidian")? BlockCore.casingObsidian:BlockCore.casingStone;
    }

    //接続ブロックの座標リストを得る
    public LinkedList<MultiBlockPosition> getMultiBlockPositions(World w, int x, int y, int z){
        //TileEntityチェック
        TileEntity entity=w.getTileEntity(x,y,z);
        if(!(entity instanceof TileEntityMachineBase)){
            IR2.logger.error("TileEntity is null!");
            return new LinkedList<MultiBlockPosition>();
        }

        TileEntityMachineBase te=(TileEntityMachineBase)entity;
        return getMultiBlockPositions(x,y,z,te.side^1);
    }
    public LinkedList<MultiBlockPosition> getMultiBlockPositions(int x, int y, int z, int side){
        LinkedList<MultiBlockPosition> ret=new LinkedList<MultiBlockPosition>();

        int sx,sy,sz,ss;
        switch (side){
            //北向き
            case 3:
                for(int i=0;i<multiWidth;i++){
                    sx=(multiWidth-1)/2-i;
                    for(int k=0;k<multiDepth;k++){
                        sz=k;
                        for(int n=0;n<multiHeight;n++){
                            sy=n;
                            if(sx==0 && sz==0 && sy==0) continue;

                            //ss
                            if(sy>0 && sx==0 && sz==0) ss=0;
                            else if(sx==0) ss=2;
                            else ss=(sx>0?5:4);

                            ret.add(new MultiBlockPosition(x + sx, y + sy, z + sz, ss));
                        }
                    }
                }
                break;

            //南向き
            case 2:
                for(int i=0;i<multiWidth;i++){
                    sx=-(multiWidth-1)/2+i;
                    for(int k=0;k<multiDepth;k++){
                        sz=-k;
                        for(int n=0;n<multiHeight;n++){
                            sy=n;
                            if(sx==0 && sz==0 && sy==0) continue;

                            //ss
                            if(sy>0 && sx==0 && sz==0) ss=0;
                            else if(sx==0) ss=3;
                            else ss=(sx>0?5:4);

                            ret.add(new MultiBlockPosition(x + sx, y + sy, z + sz, ss));
                        }
                    }
                }
                break;

            //西向き
            case 4:
                for(int i=0;i<multiWidth;i++){
                    sz=(multiWidth-1)/2-i;
                    for(int k=0;k<multiDepth;k++){
                        sx=-k;
                        for(int n=0;n<multiHeight;n++){
                            sy=n;
                            if(sx==0 && sz==0 && sy==0) continue;

                            //ss
                            if(sy>0 && sx==0 && sz==0) ss=0;
                            else if(sz==0) ss=4;
                            else ss=(sz>0?2:3);

                            ret.add(new MultiBlockPosition(x + sx, y + sy, z + sz, ss));
                        }
                    }
                }
                break;

            //東向き
            case 5:
                for(int i=0;i<multiWidth;i++){
                    sz=-(multiWidth-1)/2+i;
                    for(int k=0;k<multiDepth;k++){
                        sx=k;
                        for(int n=0;n<multiHeight;n++){
                            sy=n;
                            if(sx==0 && sz==0 && sy==0) continue;

                            //ss
                            if(sy>0 && sx==0 && sz==0) ss=0;
                            else if(sz==0) ss=5;
                            else ss=(sz>0?2:3);

                            ret.add(new MultiBlockPosition(x + sx, y + sy, z + sz, ss));
                        }
                    }
                }
                break;

            default:
                IR2.logger.error("Invalid Side:"+side);
                //break;
        }

        return ret;
    }

    //接続ブロックに自分の情報を登録する
    public void setMachineDataToMultiBlock(World w, int x, int y, int z, int side) {
        LinkedList<MultiBlockPosition> blocks = getMultiBlockPositions(x, y, z, side);
        for (MultiBlockPosition pos : blocks) {
            w.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, pos.y > y ? 12 : 4, 2);
            setMachineData(w, pos.x, pos.y, pos.z, x, y, z);
        }
    }
    protected void setMachineData(World w, int x, int y, int z, int mx, int my, int mz){
        TileEntity te=w.getTileEntity(x,y,z);
        if(te instanceof TileEntityMultiBlock){
            ((TileEntityMultiBlock) te).setMachineInfo(mx, my, mz);
        }
    }
    //接続ブロックから自分の情報を削除する
    public void deleteMachineDataFromMultiBlock(World w, int x, int y, int z, int side){
        LinkedList<MultiBlockPosition> blocks = getMultiBlockPositions(x, y, z, side^1);
        for (MultiBlockPosition pos : blocks) {
            w.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, 0, 2);
            setMachineData(w, pos.x, pos.y, pos.z, x, y, z);
        }
    }
    protected void deleteMachineData(World w, int x, int y, int z){
        TileEntity te=w.getTileEntity(x,y,z);
        if(te instanceof TileEntityMultiBlock){
            ((TileEntityMultiBlock) te).deleteMachineInfo();
        }
    }

    //接続ブロックがあるか確認する
    public boolean checkMultiBlock(World w, int x, int y, int z, int side){
        Block b=getCasingBlockType();

        //step1.指定の筐体がその数だけインベントリ内に存在するか確認する

        int meta;
        LinkedList<MultiBlockPosition> blocks=getMultiBlockPositions(x,y,z,side);
        for(MultiBlockPosition pos : blocks){
            if(w.getBlock(pos.x, pos.y, pos.z)!=b) return false;
            meta=w.getBlockMetadata(pos.x, pos.y, pos.z);

            //接続済みの場合、接続先を確認して自身でなければfalse
            if((meta&4)!=0){
                TileEntity te=w.getTileEntity(pos.x, pos.y, pos.z);
                if(te instanceof TileEntityMultiBlock && ((TileEntityMultiBlock) te).getMachineCore()!=null){
                    TileEntityMultiBlock tm=(TileEntityMultiBlock)te;

                    if(tm.machineX!=x || tm.machineY!=y || tm.machineZ!=z) return false;
                }
            }
        }

        return true;
    }

    public short getMinRSS(){
        return minRSS;
    }
    public short getMinFrequency(){
        return minFrequency;
    }
    public short getMaxRSS(){
        return maxRSS;
    }
    public short getMaxFrequency(){
        return maxFrequency;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        topIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Top");
        bottomIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Bottom");
        carryIcon = par1IconRegister.registerIcon(IR2.MODID+":machineSide");
        if(multiWidth>1 || multiHeight>1 || multiDepth>1){
            sideIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Front");
            blockIcon=par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Front");
        }
        else{
            sideIcon = par1IconRegister.registerIcon(IR2.MODID+":casing"+casingName+"Side");
            blockIcon=par1IconRegister.registerIcon(getTextureName());
        }
        //super.registerBlockIcons(par1IconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if(par1 == 1)           return topIcon;
        else if(par1==0)        return bottomIcon;
        else if(par1==par2)     return blockIcon;
        else if(par1==-1)       return carryIcon;
        return sideIcon;
    }

    @Override
    public int getRenderType(){
        return IR2.renderDirectionalId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
        if(par1World.getTileEntity(par2, par3, par4)==null){
            return false;
        }

        if(ItemWrench.isWrench(par5EntityPlayer.getHeldItem())) return false;

        if (par1World.isRemote){
            return true;
        }
        else{
            openGUI(par1World, par2, par3, par4, par5EntityPlayer);
            return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(world.isRemote) return;

        if(minFrequency==1){
            TileEntity tileEntity=world.getTileEntity(x,y,z);
            if(tileEntity instanceof TileEntityMachineBase){
                TileEntityMachineBase e=(TileEntityMachineBase)tileEntity;

                if(world.isBlockIndirectlyGettingPowered(x,y,z)) {
                    if(!e.isPowered){
                        e.workByRedstoneSignal();
                        e.isPowered=true;
                    }
                }
                else{
                    e.isPowered=false;
                }
            }
        }

        //マルチ条件を満たしているか判定し、アレなら自身を破壊
        if(multiWidth>1 && multiHeight>1 && multiDepth>1){
            boolean flag=false;
            if(checkMultiBlock(world,x,y,z,2)) flag=true;
            else if(checkMultiBlock(world,x,y,z,3)) flag=true;
            else if(checkMultiBlock(world,x,y,z,4)) flag=true;
            else if(checkMultiBlock(world,x,y,z,5)) flag=true;

            if(!flag){
                world.func_147480_a(x, y, z, true);
            }
        }

        TileEntityMachineBase te=(TileEntityMachineBase)world.getTileEntity(x,y,z);
        if(te!=null && te.side!=0 && te.side!=1 && te.change()){
            IR2PacketHandler.INSTANCE.sendToDimension(new MessageUpdateStateChangeable(te), world.provider.dimensionId);
            world.markBlockForUpdate(x, y, z);
            te.markDirty();

            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }
    }

    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random){
        TileEntity e1=par1World.getTileEntity(par2, par3, par4);
        if(e1==null){
            return;
        }
        if(e1 instanceof TileEntityMachineBase){
            TileEntityMachineBase tileEntity =  (TileEntityMachineBase)e1;
            if(tileEntity.lastCanWork && (tileEntity.isPowered || tileEntity.rss>=getMinRSS())){
                createParticle(par1World, par2, par3, par4);
            }
        }

    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
        //向きの情報を設定する。
        TileEntity te=w.getTileEntity(x, y, z);
        if(te!=null && te instanceof TileEntityMachineBase) {
            TileEntityMachineBase t = (TileEntityMachineBase) te;

            if (multiHeight > 1 || multiWidth > 1 || multiDepth > 1) {
                //マルチブロックの場合
                if (multiWidth > 1 && multiWidth % 2 == 1) {
                    //機械が中心にくる場合
                    //プレイヤーを優先して向きを決定する
                    int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                    if (checkMultiBlock(w, x, y, z, 2) || checkMultiBlock(w, x, y, z, 3)) {
                        if (l == 2) t.side = 3;
                        else if (l == 0) t.side = 2;
                        else t.side = 3;
                    } else if (checkMultiBlock(w, x, y, z, 4) || checkMultiBlock(w, x, y, z, 5)) {
                        if (l == 3) t.side = 4;
                        else if (l == 1) t.side = 5;
                        else t.side = 5;
                    }
                } else if (multiWidth > 1 || multiDepth > 1) {
                    if (checkMultiBlock(w, x, y, z, 2)) t.side = 3;
                    if (checkMultiBlock(w, x, y, z, 3)) t.side = 2;
                    if (checkMultiBlock(w, x, y, z, 4)) t.side = 5;
                    if (checkMultiBlock(w, x, y, z, 5)) t.side = 4;
                } else {
                    //横幅のないマルチブロック
                    int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                    if (l == 0) t.side = 2;
                    else if (l == 1) t.side = 5;
                    else if (l == 2) t.side = 3;
                    else if (l == 3) t.side = 4;
                }

                if (!checkMultiBlock(w, x, y, z, t.side ^ 1)) {
                    //マルチブロック要件を満たしているか確認し、ダメなら破壊
                    w.func_147480_a(x, y, z, true);
                    return;
                }
                setMachineDataToMultiBlock(w, x, y, z, t.side ^ 1);

            } else {
                //シングルの場合
                int l = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                if (l == 0) t.side = 2;
                else if (l == 1) t.side = 5;
                else if (l == 2) t.side = 3;
                else if (l == 3) t.side = 4;
            }

            w.setBlockMetadataWithNotify(x, y, z, t.side, 2);
            w.notifyBlockOfNeighborChange(x, y, z, this);
        }
    }
    protected void createParticle(World par1World, int par2, int par3, int par4){
        TileEntity t=par1World.getTileEntity(par2, par3, par4);
        if(t==null || !(t instanceof TileEntityMachineBase)){
            return;
        }

        int l = ((TileEntityMachineBase)t).side;
        float f = (float)par2 + 0.5F;
        float f1 = (float)par3 + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
        float f2 = (float)par4 + 0.5F;
        float f3 = 0.52F;
        float f4 = rand.nextFloat() * 0.6F - 0.3F;

        if (l == 4){
            par1World.spawnParticle("reddust", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 5){
            par1World.spawnParticle("reddust", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2){
            par1World.spawnParticle("reddust", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3){
            par1World.spawnParticle("reddust", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
        }
    }
    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntity tileentity = par1World.getTileEntity(x, y, z);

        if(tileentity==null){
            super.breakBlock(par1World, x, y, z, block, par6);
            return;
        }

        //インベントリを持つ機械の場合、中身を全てこぼす
        if(tileentity instanceof ISidedInventory){
            ISidedInventory inv=(ISidedInventory)tileentity;

            for (int j1 = 0; j1 < inv.getSizeInventory(); j1++){
                ItemStack itemstack = inv.getStackInSlot(j1);

                if (itemstack != null){
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0){
                        int k1 = this.rand.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize){
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        EntityItem entityitem = new EntityItem(par1World, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        par1World.spawnEntityInWorld(entityitem);
                    }
                }
            }
            //par1World.func_96440_m(x, y, z, block);
        }

        //隣接ブロックの削除
        if(tileentity instanceof TileEntityMachineBase && (multiWidth>1 || multiDepth>1 || multiHeight > 1)) {
            deleteMachineDataFromMultiBlock(par1World, x,y,z, ((TileEntityMachineBase) tileentity).side);
        }

        if (!par1World.isRemote) {
            par1World.notifyBlockOfNeighborChange(x, y, z, this);
            par1World.notifyBlockOfNeighborChange(x - 1, y, z, this);
            par1World.notifyBlockOfNeighborChange(x + 1, y, z, this);
            par1World.notifyBlockOfNeighborChange(x, y - 1, z, this);
            par1World.notifyBlockOfNeighborChange(x, y + 1, z, this);
            par1World.notifyBlockOfNeighborChange(x, y, z - 1, this);
            par1World.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }

        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            world.notifyBlockOfNeighborChange(x, y, z, this);
            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World w, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean wrench(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
        Block b=world.getBlock(x,y,z);

        //搬出面の設定 ... 搬出面以外をクリックした場合はここで処理が終わる
        TileEntity entity=world.getTileEntity(x,y,z);
        if(entity!=null && entity instanceof TileEntityMachineBase){
            TileEntityMachineBase tm=(TileEntityMachineBase)entity;
            if(tm.hasCarryingSide() && (int)tm.sideCarrying!=side && (int)tm.side!=side){
                world.markBlockForUpdate(x,y,z);
                tm.sideCarrying = (byte) side;
                return true;
            }
        }

        //機械の撤去
        if(world.isRemote) return true;
        ArrayList<ItemStack> items = b.getDrops(world, x, y, z, world.getBlockMetadata(x,y,z), 0);
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemstack = items.get(i);

            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

            while (itemstack.stackSize > 0) {
                int k1 = world.rand.nextInt(21) + 10;

                if (k1 > itemstack.stackSize) {
                    k1 = itemstack.stackSize;
                }

                itemstack.stackSize -= k1;
                EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);
                world.spawnEntityInWorld(entityitem);
            }
        }

        world.func_147480_a(x, y, z, false);
        return true;
    }
}
