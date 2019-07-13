package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.AnvilProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AnvilProvider
    implements IProbeInfoProvider,
    AnvilProviderDelegate.IAnvilDisplay {

  private final AnvilProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public AnvilProvider(AnvilRecipe.EnumTier tier) {

    this.delegate = new AnvilProviderDelegate(tier, this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileAnvilBase) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileAnvilBase) tileEntity, player);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }

  @Override
  public void setRecipeType(String langKey, String typeLangKey) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, typeLangKey));
  }

  @Override
  public void setBloomName(@Nullable TextFormatting textFormatting, ItemStack input) {

    this.probeInfo.element(new PluginTOP.ElementItemLabel(textFormatting, input));
  }

  @Override
  public void setIntegrity(String langKey, int integrity) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, integrity));
  }

  @Override
  public void setHammerPower(@Nullable TextFormatting textFormatting, String langKey, int hammerPower) {

    if (textFormatting == null) {
      this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, hammerPower));

    } else {
      this.probeInfo.element(new PluginTOP.ElementTextLocalized(TextFormatting.RED, langKey, hammerPower));
    }
  }
}