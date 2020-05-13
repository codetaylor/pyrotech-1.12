package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMarshmallow
    extends ItemFood {

  public static final String NAME = "marshmallow";

  public ItemMarshmallow() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);
    this.setAlwaysEdible();
  }

  public ItemMarshmallow(int amount, float saturation, boolean isWolfFood) {

    super(amount, saturation, isWolfFood);
  }

  public static void setRoastedAtTimestamp(ItemStack itemStack, long timestamp) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    tag.setLong("RoastedAtTimestamp", timestamp);
    itemStack.setTagCompound(tag);
  }

  public static long getRoastedAtTimestamp(ItemStack itemStack) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);

    if (!tag.hasKey("RoastedAtTimestamp")) {
      return 0;
    }

    return tag.getLong("RoastedAtTimestamp");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipFlag) {

    super.addInformation(stack, world, tooltip, tooltipFlag);

    if (world != null && stack.getItem() == ModuleTechBasic.Items.MARSHMALLOW_ROASTED) {
      long roastedAtTimestamp = ItemMarshmallow.getRoastedAtTimestamp(stack);
      double potency = ItemMarshmallow.calculatePotency(world, roastedAtTimestamp);
      int displayPotency = (int) Math.round(potency * 100);
      tooltip.add(TextFormatting.GRAY + I18n.format("gui.pyrotech.tooltip.potency", displayPotency));
    }
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER;
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION;
  }

  protected int getEffectDurationTicks(World world, long roastedAtTimestamp) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  protected int getMaxEffectDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MAX_MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {

    ItemMarshmallow.applyMarshmallowEffects(this.getEffectDurationTicks(world, ItemMarshmallow.getRoastedAtTimestamp(stack)), this.getMaxEffectDurationTicks(), entityLiving, this.getEffect(), this.stackEffect());

    if (entityLiving instanceof EntityPlayer) {
      this.setCooldownOnMarshmallows((EntityPlayer) entityLiving);
    }

    return super.onItemUseFinish(stack, world, entityLiving);
  }

  protected Potion getEffect() {

    return MobEffects.SPEED;
  }

  protected boolean stackEffect() {

    return true;
  }

  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY, 10);
    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
  }

  public static void applyMarshmallowEffects(int durationTicks, int maxDurationTicks, EntityLivingBase entityLiving, Potion effect, boolean stackEffect) {

    if (durationTicks > 0) {

      int duration = durationTicks;

      PotionEffect activePotionEffect = entityLiving.getActivePotionEffect(effect);

      if (stackEffect && activePotionEffect != null) {
        duration = Math.min(maxDurationTicks, duration + activePotionEffect.getDuration());
      }

      entityLiving.addPotionEffect(new PotionEffect(effect, duration));
    }
  }

  public static double calculatePotency(World world, long roastedAtTimestamp) {

    return Math.max(0, 1 - ((world.getTotalWorldTime() - roastedAtTimestamp) / (double) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_EFFECT_POTENCY_DURATION_TICKS)) + 1;
  }
}
