package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCrudeFishingRod
    extends ItemFishingRod {

  public static final String NAME = "crude_fishing_rod";

  public ItemCrudeFishingRod() {

    this.setMaxDamage(ModuleToolConfig.CRUDE_FISHING_ROD_DURABILITY);
  }

  @Override
  public int getItemEnchantability() {

    return 0;
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

    return false;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack itemstack = player.getHeldItem(hand);

    if (player.fishEntity != null) {
      int i = player.fishEntity.handleHookRetraction();

      if (!world.isRemote) {

        double breakChance = (itemstack.getItemDamage() / (double) itemstack.getMaxDamage()) * ModuleToolConfig.CRUDE_FISHING_ROD_BREAK_CHANCE;
        System.out.println(breakChance);

        if (RandomHelper.random().nextDouble() < breakChance) {
          itemstack.damageItem(ModuleToolConfig.CRUDE_FISHING_ROD_DURABILITY + 1, player);

        } else {
          itemstack.damageItem(i, player);
        }
      }

      player.swingArm(hand);
      world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    } else {
      world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

      if (!world.isRemote) {
        EntityFishHook entityfishhook = new EntityFishHook(world, player);
        int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

        if (j > 0) {
          entityfishhook.setLureSpeed(j);
        }

        int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

        if (k > 0) {
          entityfishhook.setLuck(k);
        }

        world.spawnEntity(entityfishhook);
      }

      player.swingArm(hand);
      StatBase stat = StatList.getObjectUseStats(this);

      if (stat != null) {
        player.addStat(stat);
      }
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
