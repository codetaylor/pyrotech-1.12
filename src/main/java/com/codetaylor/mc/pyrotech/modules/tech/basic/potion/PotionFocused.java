package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.CapabilityFocused;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;

public class PotionFocused
    extends PotionCampfireBase {

  public static final String NAME = "focused";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/focused.png"
  );

  public PotionFocused() {

    super(Color.BLACK.getRGB());
  }

  @Override
  protected ResourceLocation getTexture() {

    return TEXTURE;
  }

  @Override
  public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {

    entity.addPotionEffect(new PotionEffect(ModuleTechBasic.Potions.FOCUSED, Short.MAX_VALUE, 0, false, true));
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    return duration % 20 == 0;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderInventoryEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z) {

    super.renderInventoryEffect(effect, gui, x, y, z);

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
      double percentage = CapabilityFocused.get(Minecraft.getMinecraft().player).getRemainingBonus() / ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_MAXIMUM_ACCUMULATED_BONUS;
      int right = (int) Math.max(left + 1, left + (108 * percentage));
      int bottom = top + 1;
      Gui.drawRect(left, top, right, bottom, Color.GREEN.getRGB());
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderHUDEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {

    super.renderHUDEffect(effect, gui, x, y, z, alpha);

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
      double percentage = CapabilityFocused.get(Minecraft.getMinecraft().player).getRemainingBonus() / ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_MAXIMUM_ACCUMULATED_BONUS;
      int right = (int) Math.max(left + 1, left + (18 * percentage));
      int bottom = top + 1;

      Gui.drawRect(left, top, right, bottom, Color.GREEN.getRGB());
    }
  }
}
