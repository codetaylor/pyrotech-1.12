package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class PotionCampfireBase
    extends Potion {

  public PotionCampfireBase(boolean isBadEffectIn, int liquidColorIn) {

    super(isBadEffectIn, liquidColorIn);
  }

  protected abstract ResourceLocation getTexture();

  @SideOnly(Side.CLIENT)
  @Override
  public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {

    if (mc.currentScreen != null) {
      mc.getTextureManager().bindTexture(this.getTexture());
      Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {

    mc.getTextureManager().bindTexture(this.getTexture());
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

  @Nonnull
  @Override
  public List<ItemStack> getCurativeItems() {

    return Collections.emptyList();
  }
}
