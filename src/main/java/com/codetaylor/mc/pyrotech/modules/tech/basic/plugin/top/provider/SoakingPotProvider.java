package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.SoakingPotProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileSoakingPot;
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
import net.minecraftforge.fluids.FluidStack;

public class SoakingPotProvider
    implements IProbeInfoProvider,
    SoakingPotProviderDelegate.ISoakingPotDisplay {

  private final SoakingPotProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public SoakingPotProvider() {

    this.delegate = new SoakingPotProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileSoakingPot) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileSoakingPot) tileEntity);
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
  public void setOutputItem(ItemStack outputStack) {

    this.probeInfo.item(outputStack);
  }

  @Override
  public void setFluid(FluidStack fluidStack, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTankLabel(null, fluidStack, capacity));
  }

  @Override
  public void setCampfireRequired() {

    String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.campfire.required";
    this.probeInfo.element(new PluginTOP.ElementTextLocalized(TextFormatting.RED, langKey));
  }
}
