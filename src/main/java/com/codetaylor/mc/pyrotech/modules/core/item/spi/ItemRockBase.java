package com.codetaylor.mc.pyrotech.modules.core.item.spi;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityRock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ItemRockBase
    extends ItemBlock {

  public ItemRockBase(Block block) {

    super(block);
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    if (!ModuleCoreConfig.ROCKS.THROW_ENABLED) {
      return super.onItemRightClick(world, player, hand);
    }

    ItemStack itemStack = player.getHeldItem(hand);

    if (!player.isCreative()) {
      itemStack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS > 0) {
      player.getCooldownTracker().setCooldown(this, ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS);
      player.getCooldownTracker().setCooldown(ModuleCore.Items.ROCK_GRASS, ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS);
    }

    if (!world.isRemote) {
      EntityThrowable entity = this.createThrowable(world, player, itemStack.getMetadata());
      float pitch = (float) ModuleCoreConfig.ROCKS.PITCH;
      float velocity = (float) Math.max(0, ModuleCoreConfig.ROCKS.VELOCITY);
      float inaccuracy = (float) Math.max(1, ModuleCoreConfig.ROCKS.INACCURACY);
      entity.shoot(player, player.rotationPitch, player.rotationYaw, pitch, velocity, inaccuracy);
      world.spawnEntity(entity);
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
  }

  protected abstract EntityThrowable createThrowable(World world, EntityPlayer player, int meta);
}
