package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PotionCampfireBase
    extends Potion {

  public PotionCampfireBase(int liquidColor) {

    super(false, liquidColor);
    this.setBeneficial();
  }

  protected abstract ResourceLocation getTexture();

  @SideOnly(Side.CLIENT)
  @Override
  public void renderInventoryEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z) {

    Minecraft minecraft = Minecraft.getMinecraft();

    if (minecraft.currentScreen != null) {
      minecraft.getTextureManager().bindTexture(this.getTexture());
      Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderHUDEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {

    Minecraft minecraft = Minecraft.getMinecraft();
    minecraft.getTextureManager().bindTexture(this.getTexture());
    Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, @Nonnull EntityLivingBase entity, int amplifier, double health) {

    //
  }

  @Override
  public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {

    //
  }
}
