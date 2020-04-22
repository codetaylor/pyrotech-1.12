package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class PotionResting
    extends PotionCampfireBase {

  public static final String NAME = "resting";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/resting.png"
  );

  private static final ResourceLocation TEXTURE_2 = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/resting2.png"
  );

  private static final ResourceLocation TEXTURE_3 = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/resting3.png"
  );

  public PotionResting() {

    super(false, Color.BLACK.getRGB());
  }

  @Override
  protected ResourceLocation getTexture() {

    return TEXTURE;
  }

  public static void addEffect(EntityPlayer player) {

    PotionResting.addEffect(player, 0);
  }

  private static void addEffect(EntityPlayer player, int tier) {

    player.addPotionEffect(new PotionEffect(ModuleTechBasic.Potions.RESTING, Short.MAX_VALUE, tier, true, true));
  }

  @Override
  public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {

    if (!entity.world.isRemote && entity instanceof EntityPlayer) {

      int regenRate = Math.max(1, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_REGEN_INTERVAL_TICKS);
      int levelUpRate = Math.max(1, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_LEVEL_UP_INTERVAL_TICKS);

      PotionEffect potionEffect = entity.getActivePotionEffect(ModuleTechBasic.Potions.RESTING);

      if (potionEffect != null) {
        int duration = potionEffect.getDuration();
        this.doHeal(entity, regenRate, duration);

        if ((Short.MAX_VALUE - duration + 1) % levelUpRate == 0) {

          if (potionEffect.getAmplifier() < 2) {
            PotionResting.addEffect((EntityPlayer) entity, potionEffect.getAmplifier() + 1);

            if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
              String message = "Upgraded resting effect to level " + (potionEffect.getAmplifier() + 1) + " after " + (Short.MAX_VALUE - duration + 1) + " ticks";
              ModuleCore.LOGGER.debug(message);
              entity.sendMessage(new TextComponentString(message));
            }

          } else if (potionEffect.getAmplifier() == 2
              && ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_RESTED_EFFECT_ENABLED
            /*&& !entity.getActivePotionMap().containsKey(ModuleTechBasic.Potions.WELL_RESTED)*/) {
            int wellRestedDuration = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_RESTED_DURATION_TICKS);

            if (wellRestedDuration > 0) {
              entity.addPotionEffect(new PotionEffect(ModuleTechBasic.Potions.WELL_RESTED, wellRestedDuration, 0, false, true));

              if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
                String message = "Upgraded Resting effect to Well Rested after " + (Short.MAX_VALUE - duration + 1) + " ticks";
                ModuleCore.LOGGER.debug(message);
                entity.sendMessage(new TextComponentString(message));
              }
            }
          }
        }
      }
    }
  }

  private void doHeal(@Nonnull EntityLivingBase entity, int regenRate, int duration) {

    if ((regenRate == 1) || ((Short.MAX_VALUE - duration + 1) % regenRate == 0)) {

      if (entity.getHealth() < entity.getMaxHealth()) {
        int amount = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_REGEN_HALF_HEARTS);

        if (amount > 0) {
          entity.heal(amount);

          if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {
            String message = "Resting effect healed";
            ModuleCore.LOGGER.debug(message);
            entity.sendMessage(new TextComponentString(message));
          }
        }
      }
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    return true;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderInventoryEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z) {

    Minecraft minecraft = Minecraft.getMinecraft();

    if (minecraft.currentScreen != null) {

      if (effect.getAmplifier() == 0) {
        minecraft.getTextureManager().bindTexture(TEXTURE);

      } else if (effect.getAmplifier() == 1) {
        minecraft.getTextureManager().bindTexture(TEXTURE_2);

      } else if (effect.getAmplifier() == 2) {
        minecraft.getTextureManager().bindTexture(TEXTURE_3);
      }

      Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);

      if (this.shouldRenderProgressBar(effect)) {
        {
          int left = x + 6;
          int top = y + 8 + 17;
          int right = left + 108;
          int bottom = top + 2;
          Gui.drawRect(left, top, right, bottom, Color.BLACK.getRGB());
        }
        {
          int left = x + 6;
          int top = y + 8 + 17;
          int levelUpRate = Math.max(1, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_LEVEL_UP_INTERVAL_TICKS);
          float percentage = ((Short.MAX_VALUE - effect.getDuration()) % levelUpRate) / (float) levelUpRate;
          int right = (int) Math.max(1, left + (108 * percentage));
          int bottom = top + 1;
          Gui.drawRect(left, top, right, bottom, Color.GREEN.getRGB());
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderHUDEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {

    Minecraft minecraft = Minecraft.getMinecraft();

    if (effect.getAmplifier() == 0) {
      minecraft.getTextureManager().bindTexture(TEXTURE);

    } else if (effect.getAmplifier() == 1) {
      minecraft.getTextureManager().bindTexture(TEXTURE_2);

    } else if (effect.getAmplifier() == 2) {
      minecraft.getTextureManager().bindTexture(TEXTURE_3);
    }

    Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);

    if (this.shouldRenderProgressBar(effect)) {
      {
        int left = x + 3;
        int top = y + 20;
        int right = left + 18;
        int bottom = top + 2;
        Gui.drawRect(left, top, right, bottom, Color.BLACK.getRGB());
      }
      {
        int left = x + 3;
        int top = y + 20;
        int levelUpRate = Math.max(1, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_LEVEL_UP_INTERVAL_TICKS);
        float percentage = ((Short.MAX_VALUE - effect.getDuration()) % levelUpRate) / (float) levelUpRate;
        int right = (int) Math.max(1, left + (18 * percentage));
        int bottom = top + 1;

        Gui.drawRect(left, top, right, bottom, Color.GREEN.getRGB());
      }
    }
  }

  @SideOnly(Side.CLIENT)
  private boolean shouldRenderProgressBar(@Nonnull PotionEffect effect) {

//    EntityPlayerSP player = Minecraft.getMinecraft().player;
//    return effect.getAmplifier() < 2 || !player.getActivePotionMap().containsKey(ModuleTechBasic.Potions.WELL_RESTED);
    return true;
  }

  @Nonnull
  @Override
  public List<ItemStack> getCurativeItems() {

    return Collections.emptyList();
  }
}
