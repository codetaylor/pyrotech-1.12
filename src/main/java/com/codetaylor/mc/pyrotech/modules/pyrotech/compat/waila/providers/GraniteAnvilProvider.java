package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileGraniteAnvil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.util.BloomHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class GraniteAnvilProvider
    extends BodyProviderAdapter {

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

    if (tileEntity instanceof TileGraniteAnvil) {

      TileGraniteAnvil tile;
      tile = (TileGraniteAnvil) tileEntity;

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        GraniteAnvilRecipe recipe = GraniteAnvilRecipe.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();

          if (!recipeOutput.isEmpty()) {

            if (recipe instanceof GraniteAnvilRecipe.BloomAnvilRecipe) {
              recipeOutput.setCount(1);
            }

            tooltip.add(WailaUtil.getStackRenderString(input) +
                WailaUtil.getProgressRenderString((int) (100 * progress), 100) +
                WailaUtil.getStackRenderString(recipeOutput));
          }

          GraniteAnvilRecipe.EnumType recipeType = recipe.getType();

          if (recipeType == GraniteAnvilRecipe.EnumType.HAMMER) {
            String typeString = Util.translateFormatted("gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type.hammer");
            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type",
                typeString
            ));

          } else if (recipeType == GraniteAnvilRecipe.EnumType.PICKAXE) {
            String typeString = Util.translateFormatted("gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type.pickaxe");
            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type",
                typeString
            ));

          } else {
            throw new RuntimeException("Unknown recipe type: " + recipeType);
          }
        }

        if (recipe instanceof GraniteAnvilRecipe.BloomAnvilRecipe) {
          tooltip.add(TextFormatting.GOLD + input.getDisplayName());
          Item item = input.getItem();

          if (item instanceof BlockBloom.ItemBlockBloom) {
            BlockBloom.ItemBlockBloom bloom = (BlockBloom.ItemBlockBloom) item;

            int integrity = (int) ((bloom.getIntegrity(input) / (float) bloom.getMaxIntegrity(input)) * 100);

            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.bloom.integrity",
                integrity
            ));

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            int hammerPower = (int) (BloomHelper.calculateHammerPower(tile.getPos(), player) * 100);

            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.bloom.hammer.power",
                hammerPower
            ));
          }
        }

      }

    }

    return tooltip;
  }
}
