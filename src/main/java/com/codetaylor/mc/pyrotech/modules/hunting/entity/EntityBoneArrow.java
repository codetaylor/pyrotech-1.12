package com.codetaylor.mc.pyrotech.modules.hunting.entity;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityBoneArrow
    extends EntityArrowBase {

  public static final String NAME = "bone_arrow";

  public EntityBoneArrow(World world) {

    super(world);
  }

  public EntityBoneArrow(World world, double x, double y, double z) {

    super(world, x, y, z);
  }

  public EntityBoneArrow(World world, EntityLivingBase shooter) {

    super(world, shooter);
  }

  @Override
  protected double getBreakOnHitChance() {

    return ModuleHuntingConfig.BONE_ARROW.BREAK_ON_HIT_CHANCE;
  }

  @Override
  protected ItemStack getMaterialItemStack() {

    return ItemMaterial.EnumType.BONE_SHARD.asStack();
  }

  @Nonnull
  @Override
  protected ItemStack getArrowStack() {

    return new ItemStack(ModuleHunting.Items.BONE_ARROW);
  }

  @Override
  protected double getMaterialDropChance() {

    return ModuleHuntingConfig.BONE_ARROW.MATERIAL_DROP_CHANCE;
  }
}
