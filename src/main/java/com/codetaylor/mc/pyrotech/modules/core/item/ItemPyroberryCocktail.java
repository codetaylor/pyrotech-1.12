package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityPyroberryCocktail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemPyroberryCocktail
    extends Item {

  public static final String NAME = "pyroberry_cocktail";

  public ItemPyroberryCocktail() {

    this.setMaxStackSize(1);
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack itemStack = player.getHeldItem(hand);

    if (!player.isCreative()) {
      itemStack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (ModuleCoreConfig.PYROBERRY_COCKTAIL.THROW_COOLDOWN_TICKS > 0) {
      player.getCooldownTracker().setCooldown(this, ModuleCoreConfig.PYROBERRY_COCKTAIL.THROW_COOLDOWN_TICKS);
      player.getCooldownTracker().setCooldown(ModuleCore.Items.ROCK_GRASS, ModuleCoreConfig.PYROBERRY_COCKTAIL.THROW_COOLDOWN_TICKS);
    }

    if (!world.isRemote) {
      EntityThrowable entity = new EntityPyroberryCocktail(world, player);
      float pitch = (float) ModuleCoreConfig.PYROBERRY_COCKTAIL.PITCH;
      float velocity = (float) Math.max(0, ModuleCoreConfig.PYROBERRY_COCKTAIL.VELOCITY);
      float inaccuracy = (float) Math.max(1, ModuleCoreConfig.PYROBERRY_COCKTAIL.INACCURACY);
      entity.shoot(player, player.rotationPitch, player.rotationYaw, pitch, velocity, inaccuracy);
      world.spawnEntity(entity);
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
  }
}
