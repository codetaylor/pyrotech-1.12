package com.codetaylor.mc.pyrotech.modules.hunting.item.spi;

import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntitySpear;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ItemSpearBase
    extends Item {

  public ItemSpearBase() {

    this.setMaxStackSize(1);
    this.setMaxDamage(this.getDurability());
    this.addPropertyOverride(new ResourceLocation("charging"), new IItemPropertyGetter() {

      @SideOnly(Side.CLIENT)
      public float apply(@Nonnull ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entity) {

        return entity != null && entity.isHandActive() && entity.getActiveItemStack() == itemStack ? 1 : 0;
      }
    });
  }

  protected abstract int getDurability();

  protected abstract double getVelocityScalar();

  protected abstract double getInaccuracy();

  protected abstract double getThrownDamage();

  @Override
  public int getMaxItemUseDuration(@Nonnull ItemStack itemStack) {

    return 72000;
  }

  @Nonnull
  @Override
  public EnumAction getItemUseAction(@Nonnull ItemStack itemStack) {

    return EnumAction.BOW;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack itemStack = player.getHeldItem(hand);
    player.setActiveHand(hand);
    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
  }

  protected float getVelocity(int charge) {

    float velocity = (float) charge / 20f;
    velocity = (velocity * velocity + velocity * 2f) / 3f;

    if (velocity > 1) {
      velocity = 1;
    }

    return velocity;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityLivingBase entityLiving, int timeLeft) {

    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityLiving;
      int i = this.getMaxItemUseDuration(itemStack) - timeLeft;

      if (i < 0) {
        return;
      }

      float velocity = this.getVelocity(i);

      if (velocity >= 0.1) {

        if (!world.isRemote) {

          itemStack.damageItem(1, player);

          EntitySpear entitySpear = new EntitySpear(world, player);
          entitySpear.setDamage(this.getThrownDamage());
          entitySpear.setItemStack(itemStack.copy());
          entitySpear.shoot(player, player.rotationPitch, player.rotationYaw, 0, (float) (velocity * this.getVelocityScalar()), (float) this.getInaccuracy());

          if (velocity == 1) {
            entitySpear.setIsCritical(true);
          }

          int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemStack);

          if (j > 0) {
            entitySpear.setDamage(entitySpear.getDamage() + (double) j * 0.5 + 0.5);
          }

          int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemStack);

          if (k > 0) {
            entitySpear.setKnockbackStrength(k);
          }

          if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0) {
            entitySpear.setFire(100);
          }

          if (player.capabilities.isCreativeMode) {
            entitySpear.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
          }

          world.spawnEntity(entitySpear);
        }

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

        player.inventory.deleteStack(itemStack);

        player.addStat(StatList.getObjectUseStats(this));
      }

    }
  }
}
