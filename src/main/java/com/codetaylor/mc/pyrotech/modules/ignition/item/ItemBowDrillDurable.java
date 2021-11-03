package com.codetaylor.mc.pyrotech.modules.ignition.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemBowDrillDurable
    extends ItemIgniterBase {

  public static final String NAME = "bow_drill_durable";

  public ItemBowDrillDurable() {

    this.setMaxDamage(ModuleIgnitionConfig.IGNITERS.DURABLE_BOW_DRILL_DURABILITY);
    this.setMaxStackSize(1);
    this.addPropertyOverride(new ResourceLocation("empty"), new IItemPropertyGetter() {

      @SideOnly(Side.CLIENT)
      public float apply(@Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {

        if (entity == null) {
          return 0;

        } else {
          return (stack.getItemDamage() == stack.getMaxDamage()) ? 1 : 0;
        }
      }
    });
  }

  @Override
  protected boolean canUse(ItemStack stack) {

    return (stack.getItemDamage() < stack.getMaxDamage());
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
