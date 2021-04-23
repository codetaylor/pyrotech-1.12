package com.codetaylor.mc.pyrotech.modules.hunting.entity;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpear;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import com.codetaylor.mc.pyrotech.modules.hunting.network.SCPacketCapabilitySyncSpear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntitySpear
    extends EntityArrow {

  public static final String NAME = "spear";

  private static final DataParameter<ItemStack> DATA_ITEM_STACK = EntityDataManager.createKey(EntitySpear.class, DataSerializers.ITEM_STACK);

  private ItemStack itemStack;

  public EntitySpear(World worldIn) {

    super(worldIn);
  }

  public EntitySpear(World worldIn, double x, double y, double z) {

    super(worldIn, x, y, z);
  }

  public EntitySpear(World worldIn, EntityLivingBase shooter) {

    super(worldIn, shooter);
  }

  @Nonnull
  @Override
  protected ItemStack getArrowStack() {

    return (this.itemStack == null) ? ItemStack.EMPTY : this.itemStack;
  }

  public void setItemStack(ItemStack itemStack) {

    this.itemStack = itemStack;
    this.dataManager.set(DATA_ITEM_STACK, itemStack);
  }

  public ItemStack getItemStack() {

    return this.dataManager.get(DATA_ITEM_STACK);
  }

  @Override
  protected void entityInit() {

    this.dataManager.register(DATA_ITEM_STACK, ItemStack.EMPTY);
    super.entityInit();
  }

  @Override
  public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    super.writeEntityToNBT(compound);
    compound.setTag("itemStack", this.itemStack.serializeNBT());
  }

  @Override
  public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    super.readEntityFromNBT(compound);
    NBTTagCompound itemStack = (NBTTagCompound) compound.getTag("itemStack");
    this.setItemStack(new ItemStack(itemStack));
  }

  @Override
  protected void onHit(@Nonnull RayTraceResult raytraceResult) {

    Entity entityHit = raytraceResult.entityHit;

    if (entityHit != null && !this.world.isRemote) {

      if (entityHit instanceof EntityLivingBase && !entityHit.getIsInvulnerable()) {

        ISpearEntityData data = CapabilitySpear.get((EntityLivingBase) entityHit);

        if (data != null) {
          data.addItemStack(this.getItemStack());
          ModuleHunting.PACKET_SERVICE.sendToAllAround(new SCPacketCapabilitySyncSpear(entityHit.getEntityId(), data.getItemStackCount()), entityHit.dimension, entityHit.posX, entityHit.posY, entityHit.posZ);
        }

      } else {
        this.entityDropItem(this.getItemStack(), 0);
      }
    }

    super.onHit(raytraceResult);
  }
}
