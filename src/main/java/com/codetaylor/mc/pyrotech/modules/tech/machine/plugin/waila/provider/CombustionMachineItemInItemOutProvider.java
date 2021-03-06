package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineItemInItemOutProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CombustionMachineItemInItemOutProvider
    extends BodyProviderAdapter
    implements CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay {

  private final CombustionMachineProviderDelegateBase<CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay, TileCombustionWorkerStoneItemInItemOutBase, MachineRecipeItemInItemOutBase> delegate;

  private List<String> tooltip;

  public CombustionMachineItemInItemOutProvider() {

    this.delegate = new CombustionMachineItemInItemOutProviderDelegate(this);
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

    if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileCombustionWorkerStoneItemInItemOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase) {
        tile = (TileCombustionWorkerStoneItemInItemOutBase) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileCombustionWorkerStoneItemInItemOutBase) {
          tile = (TileCombustionWorkerStoneItemInItemOutBase) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      this.tooltip = tooltip;
      this.delegate.display(tile);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInItemOutBase recipe, int progress, int maxProgress) {

    StringBuilder renderString = new StringBuilder();
    renderString.append(WailaUtil.getStackRenderString(input));

    if (!fuel.isEmpty()) {
      renderString.append(WailaUtil.getStackRenderString(fuel));
    }

    if (recipe != null) {
      ItemStack recipeOutput = recipe.getOutput();
      recipeOutput.setCount(input.getCount());
      renderString.append(WailaUtil.getProgressRenderString(progress, maxProgress));
      renderString.append(WailaUtil.getStackRenderString(recipeOutput));
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setRecipeDuration(String langKey, String duration) {

    this.tooltip.add(Util.translateFormatted(langKey, duration));
  }

  @Override
  public void setOutputItems(ItemStackHandler outputStackHandler) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < outputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setBurnTime(@Nullable TextFormatting formatting, String langKey, String burnTimeString) {

    String formattingString = (formatting != null) ? formatting.toString() : "";
    this.tooltip.add(formattingString + Util.translateFormatted(langKey, burnTimeString));
  }

  @Override
  public void setFuel(String langKey, ItemStack fuel, String count) {

    this.tooltip.add(Util.translateFormatted(langKey, fuel.getDisplayName(), count));
  }
}
