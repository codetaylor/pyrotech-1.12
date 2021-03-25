package com.codetaylor.mc.pyrotech.modules.hunting.entity;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class EntityArrowBase
    extends EntityArrow {

  public EntityArrowBase(World worldIn) {

    super(worldIn);
  }

  public EntityArrowBase(World worldIn, double x, double y, double z) {

    super(worldIn, x, y, z);
  }

  public EntityArrowBase(World worldIn, EntityLivingBase shooter) {

    super(worldIn, shooter);
  }

  @Override
  protected void onHit(@Nonnull RayTraceResult raytraceResult) {

    super.onHit(raytraceResult);

    if (this.inGround && !this.world.isRemote) {

      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      System.out.println(f);

      double breakOnHitChance = 1 - MathHelper.clamp(this.getBreakOnHitChance(), 0, 1);

      if (RandomHelper.random().nextFloat() * f >= breakOnHitChance) {
        this.setDead();
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 1);

        ItemStack[] drops = new ItemStack[]{
            new ItemStack(Items.STICK),
            this.getMaterialItemStack(),
            ItemMaterial.EnumType.FLETCHING.asStack()
        };

        for (ItemStack drop : drops) {

          if (RandomHelper.random().nextFloat() < this.getMaterialDropChance()) {
            EntityItem entityItem = new EntityItem(
                this.world,
                this.posX,
                this.posY,
                this.posZ,
                drop
            );
            entityItem.motionX = 0;
            entityItem.motionY = 0.1;
            entityItem.motionZ = 0;

            this.world.spawnEntity(entityItem);
          }
        }
      }
    }
  }

  protected abstract double getBreakOnHitChance();

  protected abstract ItemStack getMaterialItemStack();

  protected abstract double getMaterialDropChance();
}
