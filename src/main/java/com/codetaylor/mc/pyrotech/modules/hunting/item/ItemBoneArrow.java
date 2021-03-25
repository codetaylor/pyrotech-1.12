package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityBoneArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemBoneArrow
    extends ItemArrow {

  public static final String NAME = "bone_arrow";

  public ItemBoneArrow() {

    this.setMaxStackSize(ModuleHuntingConfig.BONE_ARROW.STACK_SIZE);
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter) {

    EntityBoneArrow entity = new EntityBoneArrow(world, shooter);
    entity.setDamage(ModuleHuntingConfig.BONE_ARROW.DAMAGE);
    return entity;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {

    return false;
  }
}
