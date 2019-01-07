package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileWorktable;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class StorageItemStackProvider
    extends BodyProviderAdapter {

  private String craftingTableRenderString;

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (!config.getConfig(WailaRegistrar.CONFIG_PROGRESS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();
    RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

    if (rayTraceResult == null) {
      return tooltip;
    }

    if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
      return tooltip;
    }

    if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {
      InteractionRayTraceData.List list = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (InteractionRayTraceData data : list) {
        IInteraction interaction = data.getInteraction();

        if (interaction.isEnabled()
            && interaction instanceof IInteractionItemStack) {
          ItemStack stackInSlot = ((IInteractionItemStack) interaction).getStackInSlot();

          if (!stackInSlot.isEmpty()) {
            tooltip.add(WailaUtil.getStackRenderString(stackInSlot));
            tooltip.add(stackInSlot.getItem().getItemStackDisplayName(stackInSlot));
          }
        }
      }

    }

    if (tileEntity instanceof TileWorktable) {

      TileWorktable tile;
      tile = (TileWorktable) tileEntity;

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      boolean notEmpty = false;

      for (int i = 0; i < 9; i++) {

        if (!stackHandler.getStackInSlot(i).isEmpty()) {
          notEmpty = true;
          break;
        }
      }

      if (notEmpty) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();

        IRecipe recipe = tile.getRecipe();

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getRecipeOutput();

          if (!recipeOutput.isEmpty()) {

            if (this.craftingTableRenderString == null) {
              this.craftingTableRenderString = WailaUtil.getStackRenderString(new ItemStack(Blocks.CRAFTING_TABLE));
            }

            renderString.append(this.craftingTableRenderString);
            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }
        }

        tooltip.add(renderString.toString());

      }

    }

    return tooltip;
  }
}
