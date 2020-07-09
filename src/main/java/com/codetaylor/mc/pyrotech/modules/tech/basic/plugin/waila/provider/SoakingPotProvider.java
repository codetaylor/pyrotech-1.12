package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.SoakingPotProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileSoakingPot;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SoakingPotProvider
    extends BodyProviderAdapter
    implements SoakingPotProviderDelegate.ISoakingPotDisplay {

  private final SoakingPotProviderDelegate delegate;

  private List<String> tooltip;

  public SoakingPotProvider() {

    delegate = new SoakingPotProviderDelegate(this);
  }

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileSoakingPot) {
      this.tooltip = tooltip;
      this.delegate.display((TileSoakingPot) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input)
        + WailaUtil.getProgressRenderString(progress, maxProgress)
        + WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }

  @Override
  public void setOutputItem(ItemStack outputStack) {

    this.tooltip.add(WailaUtil.getStackRenderString(outputStack));
  }

  @Override
  public void setFluid(FluidStack fluidStack, int capacity) {

    String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.tank.fluid";
    String localizedFluidName = fluidStack.getLocalizedName();
    this.tooltip.add(Util.translateFormatted(langKey, localizedFluidName, fluidStack.amount, capacity));
  }

  @Override
  public void setCampfireRequired() {

    String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.campfire.required";
    this.tooltip.add(TextFormatting.RED + Util.translateFormatted(langKey));
  }
}
