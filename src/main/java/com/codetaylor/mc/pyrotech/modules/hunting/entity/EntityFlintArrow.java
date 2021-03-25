package com.codetaylor.mc.pyrotech.modules.hunting.entity;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityFlintArrow
    extends EntityArrowBase {

  public static final String NAME = "flint_arrow";

  public EntityFlintArrow(World world) {

    super(world);
  }

  public EntityFlintArrow(World world, double x, double y, double z) {

    super(world, x, y, z);
  }

  public EntityFlintArrow(World world, EntityLivingBase shooter) {

    super(world, shooter);
  }

  @Override
  protected double getBreakOnHitChance() {

    return ModuleHuntingConfig.FLINT_ARROW.BREAK_ON_HIT_CHANCE;
  }

  @Override
  protected ItemStack getMaterialItemStack() {

    return ItemMaterial.EnumType.FLINT_SHARD.asStack();
  }

  @Nonnull
  @Override
  protected ItemStack getArrowStack() {

    return new ItemStack(ModuleHunting.Items.FLINT_ARROW);
  }

  @Override
  protected double getMaterialDropChance() {

    return ModuleHuntingConfig.FLINT_ARROW.MATERIAL_DROP_CHANCE;
  }
}
