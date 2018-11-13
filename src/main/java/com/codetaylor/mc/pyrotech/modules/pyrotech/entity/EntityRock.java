package com.codetaylor.mc.pyrotech.modules.pyrotech.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityRock
    extends EntityThrowable {

  public static final String NAME = "rock";

  private static final DataParameter<Integer> META = EntityDataManager.createKey(EntityRock.class, DataSerializers.VARINT);
  private int meta;

  public EntityRock(World world) {

    super(world);
  }

  public EntityRock(World world, int meta) {

    super(world);
    this.dataManager.set(META, meta);
    this.meta = meta;
  }

  public EntityRock(World world, double x, double y, double z, int meta) {

    super(world, x, y, z);
    this.dataManager.set(META, meta);
    this.meta = meta;
  }

  public EntityRock(World world, EntityLivingBase throwerIn, int meta) {

    super(world, throwerIn);
    this.dataManager.set(META, meta);
    this.meta = meta;
  }

  public int getMeta() {

    return this.meta;
  }

  @Override
  protected void onImpact(@Nonnull RayTraceResult result) {

    if (result.entityHit != null) {
      result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1);
    }

    if (!this.world.isRemote) {
      this.world.setEntityState(this, (byte) 3);
      this.setDead();
    }
  }

  @Override
  protected void entityInit() {

    super.entityInit();
    this.dataManager.register(META, this.meta);
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {

    if (this.world.isRemote) {

      if (META.equals(key)) {
        this.meta = this.dataManager.get(META);
      }
    }

    super.notifyDataManagerChange(key);
  }
}
