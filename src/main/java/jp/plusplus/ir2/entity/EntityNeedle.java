package jp.plusplus.ir2.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/09/08.
 */
public class EntityNeedle extends Entity implements IProjectile {

    /* 地中判定に使うもの */
    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected Block inTile;
    protected int inData;
    protected boolean inGround;

    /* この弾を撃ったエンティティ */
    public Entity shootingEntity;

    /* 地中・空中にいる時間 */
    protected int ticksInGround;
    protected int ticksInAir;

    /* ダメージの大きさ */
    protected double damage = 4.0D;

    /* ノックバックの大きさ */
    protected int knockbackStrength = 1;

    public EntityNeedle(World par1World) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.damage = 2.5D;
    }

    /**
     * 発射する弾を生成・初期パラメータの定義をする。
     *
     * @param par1World            :このワールド
     * @param shooter :弾源となるエンティティ。このModの場合、弾を撃ったプレイヤーがここに入る
     * @param speed                :弾の速度計算に使われる値
     * @param speed2               :弾の速度計算に使われる値2
     * @param adjustX              :プレイヤーから見て水平方向に、発射する弾をずらす(複数発射するときなどに使用する)
     * @param adjustZ              :プレイヤーから見て前後方向に弾をずらす
     * @param adjustY              :プレイヤーから見て上下方向に弾をずらす
     */
    public EntityNeedle(World par1World, EntityLivingBase shooter, float speed, float speed2,
                              float adjustX, float adjustZ, float adjustY) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);

        //初期状態での向きの決定
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);

        //位置の調整
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F)-adjustX;
        this.posY -= 0.10000000149011612D-adjustY;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F)-adjustZ;
        this.setPosition(this.posX, this.posY, this.posZ);
        /*
        this.posX += -(double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * (1.0F + adjustZ))
                - (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * adjustX);
        this.posY += 0.05000000149011612D + adjustY;
        this.posZ += (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * (1.0F + adjustZ))
                - (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * adjustX);
        this.setPosition(this.posX, this.posY, this.posZ);
        */

        //初速度
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) *
                MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) *
                MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 4.0f, speed2);
    }

    /*dataWatcherを利用したサーバ・クライアント間の同期処理だと思う*/
    protected void entityInit() {

    }

    /*
     * IProjectileで実装が必要なメソッド。
     * ディスペンサーによる発射メソッドなどで使用されている。
     */
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
        //速度の大きさを1にする
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double) f2;
        par3 /= (double) f2;
        par5 /= (double) f2;
        //ぶらす
        par1 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        par3 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        par5 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        //かける
        par1 *= (double) par7;
        par3 *= (double) par7;
        par5 *= (double) par7;
        //決定
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    /*
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @SideOnly(Side.CLIENT)
    /*
     * Sets the velocity to the args. Args: x, y, z
     * 速度の処理。クライアント・サーバ間での同期処理にて利用されている。
     */
    public void setVelocity(double par1, double par3, double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /*
     * Tick毎に呼ばれる更新処理。
     * 速度の更新、衝突判定などをここで行う。
     */
    public void onUpdate() {
        super.onUpdate();

        //-------------------------------------------------------------------------------------------------------------
        //直前のパラメータと新パラメータを一致させているところ。
        //また、速度に応じてエンティティの向きを調整し、常に進行方向に前面が向くようにしている。
        //-------------------------------------------------------------------------------------------------------------
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        //-------------------------------------------------------------------------------------------------------------
        // ブロックとの当たり判定
        //-------------------------------------------------------------------------------------------------------------
        Block hit=worldObj.getBlock(xTile, yTile, zTile); //激突したブロックを確認している
        if (hit.getMaterial()!= Material.air) {
            //空気じゃないブロックに当たった時
            hit.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = hit.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            //当たり判定に接触しているかどうか
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.inGround) {
            //空気じゃないブロックに当たった
            Block hit2=worldObj.getBlock(xTile, yTile, zTile);
            int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            /* 前のTickに確認した埋まりブロックのIDとメタを照合している。違ったら埋まり状態を解除、一致したら埋まり状態を継続。
            /* 埋まり状態2tick継続でこのエンティティを消す
             */
            if (hit2 == this.inTile && k == this.inData) {
                ++this.ticksInGround;
                //ブロック貫通の場合、20tick（1秒間）はブロック中にあっても消えないようになる。
                int limit = 2;

                if (this.ticksInGround > limit) {
                    this.setDead();
                }
            } else {
                //埋まり状態の解除処理
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.1F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.1F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.1F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            //埋まってない時。速度の更新。
            ++this.ticksInAir;
            //ブロックとの衝突判定
            Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY +
                    this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
            vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            //ブロックに当たった
            if (movingobjectposition != null) {
                vec31 =  Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            //Entityとの衝突判定。
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;
            boolean isVillager = false;

            //1ブロック分の範囲内にいるエンティティ全てに対して繰り返す
            for (l = 0; l < list.size(); ++l) {
                Entity entity1 = (Entity) list.get(l);

                //すりぬける
                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            //エンティティに当たった
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            /* 当たったエンティティそれそれについての判定部分。
             * ここでmovingobjectposition = nullにすることで特定の種類のエンティティに当たらないようにできる。*/
            if (movingobjectposition != null && movingobjectposition.entityHit != null) {
                if (movingobjectposition.entityHit instanceof EntityPlayer) {
                    //プレイヤーに当たった時
                    EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                    if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer &&
                            !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                        //PvPが許可されていないと当たらない
                        movingobjectposition = null;
                    } else if (entityplayer == this.shootingEntity) {
                        //対象が撃った本人の場合も当たらない
                        movingobjectposition = null;
                    }
                }
            }

            float f2;
            float f3;

            //当たったあとの処理
            if (movingobjectposition != null) {
                //エンティティに当たった
                if (movingobjectposition.entityHit != null) {
                    int i1 = MathHelper.ceiling_double_int((double) this.damage);
                    //0~2程度の乱数値を上乗せ
                    i1 += this.rand.nextInt(3);

                    DamageSource damagesource = null;

                    //別メソッドでダメージソースを確認
                    damagesource = this.thisDamageSource(this.shootingEntity);

                        //村人以外なら、ダメージを与える処理を呼ぶ
                        if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) i1)) {
                            //ダメージを与えることに成功したら以下の処理を行う
                            if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                                EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                                //ノックバック
                                if (this.knockbackStrength > 0) {
                                    f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                    if (f3 > 0.0F) {
                                        movingobjectposition.entityHit.addVelocity(this.motionX *
                                                (double) this.knockbackStrength * 0.6000000238418579D / (double) f3, 0.1D, this.motionZ *
                                                (double) this.knockbackStrength * 0.6000000238418579D / (double) f3);
                                    }
                                } else {
                                    movingobjectposition.entityHit.hurtResistantTime = 0;
                                }

                                //Thornのエンチャント効果で反撃を受ける
                                /*
                                if (this.shootingEntity != null) {
                                    EnchantmentThorns.thorns.func_151368_a(this.shootingEntity, entitylivingbase, getDamage());
                                }
                                */

                                /*
                                //マルチプレイ時に、両者がプレイヤーだった時のパケット送信処理
                                if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity &&
                                        movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new Packet(6, 0));
                                }
                                */
                            }

                            //ここでヒット時の効果音がなる
                            this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                            //当たったあと、弾を消去する。エンティティ貫通がONの弾種はそのまま残す。
                            if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                                this.setDead();
                            }
                        }


                } else{
                    //エンティティには当たってない。ブロックに当たった。
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY +
                            this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;

                    if (this.inTile.getMaterial()!=Material.air) {
                        inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }

            //改めてポジションに速度を加算。向きも更新。
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / Math.PI);

            while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
                this.prevRotationPitch -= 360.0F;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            //徐々に減速する
            float f4 = 0.99F;

            //重力落下
            //落下速度は別メソッドで設定している。デフォルトでは0.0F。
            f1 = this.fallSpeed();

            //水中に有る
            if (this.isInWater()) {
                //泡パーティクルが出る
                for (int j1 = 0; j1 < 4; ++j1) {
                    f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f3, this.posY - this.motionY *
                            (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX, this.motionY, this.motionZ);
                }

                //減速も大きくなる
                f4 = 0.8F;
            }

            this.motionX *= (double) f4;
            this.motionY *= (double) f4;
            this.motionZ *= (double) f4;
            this.motionY -= (double) f1;

            //一定以上遅くなったら消える
            if (this.worldObj.isRemote && this.motionX * this.motionX + this.motionZ * this.motionZ < 0.001D) {
                this.setDead();
            }


            this.setPosition(this.posX, this.posY, this.posZ);
            //this.doBlockCollisions();
            this.func_145775_I();
        }
    }

    /*
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setShort("xTile", (short) this.xTile);
        par1NBTTagCompound.setShort("yTile", (short) this.yTile);
        par1NBTTagCompound.setShort("zTile", (short) this.zTile);
        if(inTile!=null){
            par1NBTTagCompound.setString("inTile", this.inTile.getUnlocalizedName());
        }
        else{
            par1NBTTagCompound.setString("inTile", "(null)");
        }
        par1NBTTagCompound.setByte("inData", (byte) this.inData);
        par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        par1NBTTagCompound.setDouble("damage", this.damage);
    }

    /*
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.xTile = par1NBTTagCompound.getShort("xTile");
        this.yTile = par1NBTTagCompound.getShort("yTile");
        this.zTile = par1NBTTagCompound.getShort("zTile");

        String s=par1NBTTagCompound.getString("inTile");
        if(!s.equals("(null)")){
            inTile=Block.getBlockFromName(s);
        }
        else{
            inTile=null;
        }
        this.inData = par1NBTTagCompound.getByte("inData") & 255;
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

        if (par1NBTTagCompound.hasKey("damage")) {
            this.damage = par1NBTTagCompound.getDouble("damage");
        }
    }

    /*
     * プレイヤーと衝突した時のメソッド。今回は何もしない
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {

    }

    /*
     * ブロックに対し、上を歩いたかという判定の対象になるか、というEntityクラスのメソッド。
     * 耕地を荒らしたりするのに使う。
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public void setDamage(double par1) {
        this.damage = par1;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int par1) {
        this.knockbackStrength = par1;
    }

    public boolean canAttackWithItem() {
        return false;
    }

    /**
     * 以下、当MOD用のパラメータ定義部分
     */

    /* 落下速度 */
    public float fallSpeed() {
        return 0.0F;
    }

    /* ダメージソースのタイプ */
    public DamageSource thisDamageSource(Entity entity) {
        //発射元のEntityがnullだった場合の対策を含む。
        return entity != null ? EntityDamageSource.causeIndirectMagicDamage(entity, this) : DamageSource.magic;
    }


}