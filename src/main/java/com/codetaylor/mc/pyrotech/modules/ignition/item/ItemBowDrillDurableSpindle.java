package com.codetaylor.mc.pyrotech.modules.ignition.item;

import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemBowDrillDurableSpindle
    extends ItemIgniterBase {

  public static final String NAME = "bow_drill_durable_spindle";

  public ItemBowDrillDurableSpindle() {

    this.setMaxDamage(ModuleIgnitionConfig.IGNITERS.DURABLE_BOW_DRILL_DURABILITY);
    this.setMaxStackSize(1);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName() {

    return "item.pyrotech.bow.drill.durable";
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(@Nonnull ItemStack stack) {

    return this.getUnlocalizedName();
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase player) {

    ItemStack result = super.onItemUseFinish(stack, world, player);

    if (result.isEmpty() || result.getItemDamage() == result.getMaxDamage()) {

      if (!world.isRemote) {
        SoundHelper.playSoundServer(world, player.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
      }

      return new ItemStack(ModuleIgnition.Items.BOW_DRILL_DURABLE);
    }

    return result;
  }

  @Override
  public int getMaxItemUseDuration(@Nonnull ItemStack stack) {

    return ModuleIgnitionConfig.IGNITERS.DURABLE_BOW_DRILL_USE_DURATION_TICKS;
  }

  @Override
  protected void damageItem(@Nonnull ItemStack stack, EntityLivingBase player) {

    if (stack.getItemDamage() == stack.getMaxDamage()) {
      return;
    }

    if (player instanceof EntityPlayer
        && !((EntityPlayer) player).isCreative()) {
      stack.damageItem(1, player);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (ModuleCoreConfig.CLIENT.SHOW_DURABILITY_TOOLTIPS && this.getDamage(stack) == 0) {
      //noinspection deprecation
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
