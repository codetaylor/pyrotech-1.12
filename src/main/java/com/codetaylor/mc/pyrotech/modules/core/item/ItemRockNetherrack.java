package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityRockNetherrack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemRockNetherrack
    extends ItemBlock {

  public ItemRockNetherrack(Block block) {

    super(block);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "tile." + ModuleCore.MOD_ID + ".rock_netherrack";
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    if (!ModuleCoreConfig.ROCKS.THROW_ENABLED) {
      return super.onItemRightClick(world, player, hand);
    }

    ItemStack itemstack = player.getHeldItem(hand);

    if (!player.isCreative()) {
      itemstack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS > 0) {
      player.getCooldownTracker().setCooldown(this, ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS);
      player.getCooldownTracker().setCooldown(ModuleCore.Items.ROCK, ModuleCoreConfig.ROCKS.THROW_COOLDOWN_TICKS);
    }

    if (!world.isRemote) {
      EntityRockNetherrack entity = new EntityRockNetherrack(world, player, itemstack.getMetadata());
      entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
      world.spawnEntity(entity);
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
  }

}
