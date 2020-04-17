package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

public class PotionComfort
    extends Potion {

  public static final String NAME = "comfort";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/comfort.png"
  );

  public PotionComfort() {

    super(false, Color.BLACK.getRGB());
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {

    if (mc.currentScreen != null) {
      mc.getTextureManager().bindTexture(TEXTURE);
      Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {

    mc.getTextureManager().bindTexture(TEXTURE);
    Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
  }

  @Override
  public void performEffect(EntityLivingBase entity, int amplifier) {

    if (entity.getHealth() < entity.getMaxHealth()) {
      entity.heal(1.0F);
    }
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {

    //
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    int rate = 50 >> amplifier;

    if (rate > 0) {
      return duration % rate == 0;

    } else {
      return true;
    }
  }
}
