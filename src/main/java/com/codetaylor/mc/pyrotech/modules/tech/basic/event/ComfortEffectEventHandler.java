package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public final class ComfortEffectEventHandler {

  private static MethodHandle itemFood$alwaysEdibleGetter;

  static {

    try {

      itemFood$alwaysEdibleGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/item/ItemFood.alwaysEdible
          Name: e => field_77852_bZ => alwaysEdible
          Comment: "If this field is true, the food can be consumed even if the player don't need to eat."
          Side: BOTH
          AT: public net.minecraft.item.ItemFood field_77852_bZ # alwaysEdible
           */
          ObfuscationReflectionHelper.findField(ItemFood.class, "field_77852_bZ")
      );

    } catch (IllegalAccessException e) {
      ModuleCore.LOGGER.error("", e);
    }
  }

  private boolean isAlwaysEdible(ItemFood itemFood) {

    try {
      return (boolean) itemFood$alwaysEdibleGetter.invokeExact(itemFood);

    } catch (Throwable throwable) {
      ModuleCore.LOGGER.error("", throwable);
    }

    return false;
  }

  @SubscribeEvent
  public void on(PlayerInteractEvent.RightClickItem event) {

    ItemStack itemStack = event.getItemStack();
    Item item = itemStack.getItem();

    if (!(item instanceof ItemFood)) {
      // We're only interested if the item is food.
      return;
    }

    ItemFood itemFood = (ItemFood) item;

    if (this.isAlwaysEdible(itemFood)) {
      // We're only interested in food that isn't already always edible.
      return;
    }

    EntityLivingBase entityLiving = event.getEntityLiving();

    if (!(entityLiving instanceof EntityPlayer)) {
      // If the entity isn't a player, we're not interested.
      return;
    }

    EntityPlayer player = (EntityPlayer) entityLiving;

    if (player.canEat(false)) {
      // We're only interested in players that can't eat.
      return;
    }

    PotionEffect effect = player.getActivePotionEffect(ModuleTechBasic.Potions.COMFORT);

    if (effect == null) {
      // If the player doesn't have the appropriate effect, we're not interested.
      return;
    }

    World world = player.getEntityWorld();
    EnumHand hand = event.getHand();

    if (world.isRemote) {

      int itemStackCount = itemStack.getCount();
      ActionResult<ItemStack> actionResult = this.useFood(itemStack, world, player, hand);
      ItemStack resultItemStack = actionResult.getResult();

      if (resultItemStack != itemStack || resultItemStack.getCount() != itemStackCount) {
        player.setHeldItem(hand, resultItemStack);

        if (resultItemStack.isEmpty()) {
          net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemStack, hand);
        }
      }

    } else {
      int i = itemStack.getCount();
      int j = itemStack.getMetadata();
      ItemStack copyBeforeUse = itemStack.copy();
      ActionResult<ItemStack> actionResult = this.useFood(itemStack, world, player, hand);
      ItemStack resultItemStack = actionResult.getResult();

      player.setHeldItem(hand, resultItemStack);

      if (player.isCreative()) {
        resultItemStack.setCount(i);

        if (resultItemStack.isItemStackDamageable()) {
          resultItemStack.setItemDamage(j);
        }
      }

      if (resultItemStack.isEmpty()) {
        net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, hand);
        player.setHeldItem(hand, ItemStack.EMPTY);
      }

      if (!player.isHandActive()) {
        ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
      }
    }

    event.setCanceled(true);
  }

  private ActionResult<ItemStack> useFood(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {

    ActionResult<ItemStack> actionResult = itemStack.useItemRightClick(world, player, hand);

    if (actionResult.getType() == EnumActionResult.FAIL) {
      player.setActiveHand(hand);
      return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    return actionResult;
  }

  @SubscribeEvent
  public void on(LivingEntityUseItemEvent.Finish event) {

    EntityLivingBase entityLiving = event.getEntityLiving();

    if (entityLiving.getEntityWorld().isRemote) {
      return;
    }

    PotionEffect effect = entityLiving.getActivePotionEffect(ModuleTechBasic.Potions.COMFORT);

    if (effect == null) {
      // If the player doesn't have the appropriate effect, we're not interested.
      return;
    }

    if (entityLiving instanceof EntityPlayer) {
      ItemStack itemStack = event.getItem();
      Item item = itemStack.getItem();

      if (item instanceof ItemFood) {
        ItemFood itemFood = (ItemFood) item;
        int healAmount = (int) Math.max(0, itemFood.getHealAmount(itemStack) * ModuleTechBasicConfig.CAMPFIRE.COMFORT_HUNGER_MODIFIER);
        float saturationModifier = (float) Math.max(0, itemFood.getSaturationModifier(itemStack) * ModuleTechBasicConfig.CAMPFIRE.COMFORT_SATURATION_MODIFIER);
        FoodStats foodStats = ((EntityPlayer) entityLiving).getFoodStats();
        foodStats.addStats(healAmount, saturationModifier);
      }
    }
  }
}
