package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineItemInItemOutProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CombustionMachineProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
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

public class SawmillProvider
    extends BodyProviderAdapter
    implements CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay {

  private final CombustionMachineProviderDelegateBase<CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay, TileCombustionWorkerStoneItemInItemOutBase, MachineRecipeItemInItemOutBase> delegate;

  private List<String> tooltip;
  private TileSawmillBase tile;

  public SawmillProvider() {

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

    if (tileEntity instanceof TileSawmillBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileSawmillBase tile = null;

      if (tileEntity instanceof TileSawmillBase) {
        tile = (TileSawmillBase) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileSawmillBase) {
          tile = (TileSawmillBase) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      this.tile = tile;
      this.tooltip = tooltip;
      this.delegate.display(tile);
      this.tile = null;
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInItemOutBase recipe, int progress, int maxProgress) {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    StringBuilder renderString = new StringBuilder();
    renderString.append(WailaUtil.getStackRenderString(input));

    if (!blade.isEmpty()) {
      renderString.append(WailaUtil.getStackRenderString(blade));
    }

    if (!fuel.isEmpty()) {
      renderString.append(WailaUtil.getStackRenderString(fuel));
    }

    if (recipe != null) {
      ItemStack recipeOutput = recipe.getOutput();
      recipeOutput.setCount(recipeOutput.getCount() * input.getCount());
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

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    StringBuilder renderString = new StringBuilder();

    if (!blade.isEmpty()) {
      renderString.append(WailaUtil.getStackRenderString(blade));
    }

    for (int i = 0; i < outputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void optionalNoInputNoOutput() {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    if (!blade.isEmpty()) {
      this.tooltip.add(WailaUtil.getStackRenderString(blade));
    }
  }

  @Override
  public void optionalPreBurnTimeInfo() {

    ItemStackHandler bladeStackHandler = this.tile.getBladeStackHandler();
    ItemStack blade = bladeStackHandler.getStackInSlot(0);

    if (!blade.isEmpty()) {
      this.tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechMachine.MOD_ID + ".waila.sawmill.blade",
          blade.getItem().getItemStackDisplayName(blade)
      ));
    }
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
