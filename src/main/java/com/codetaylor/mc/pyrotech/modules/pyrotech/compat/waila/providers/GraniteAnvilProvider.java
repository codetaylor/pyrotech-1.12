package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileAnvilBase;
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

    if (tileEntity instanceof TileAnvilBase) {

      TileAnvilBase tile;
      tile = (TileAnvilBase) tileEntity;

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        AnvilRecipe recipe = AnvilRecipe.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();

          if (!recipeOutput.isEmpty()) {

            if (recipe instanceof BloomAnvilRecipe) {
              recipeOutput.setCount(1);
            }

            tooltip.add(WailaUtil.getStackRenderString(input) +
                WailaUtil.getProgressRenderString((int) (100 * progress), 100) +
                WailaUtil.getStackRenderString(recipeOutput));
          }

          AnvilRecipe.EnumType recipeType = recipe.getType();

          if (recipeType == AnvilRecipe.EnumType.HAMMER) {
            String typeString = Util.translateFormatted("gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type.hammer");
            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type",
                typeString
            ));

          } else if (recipeType == AnvilRecipe.EnumType.PICKAXE) {
            String typeString = Util.translateFormatted("gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type.pickaxe");
            tooltip.add(Util.translateFormatted(
                "gui." + ModulePyrotech.MOD_ID + ".waila.anvil.recipe.type",
                typeString
            ));

          } else {
            throw new RuntimeException("Unknown recipe type: " + recipeType);
          }
        }

        if (recipe instanceof BloomAnvilRecipe) {
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

            if (hammerPower > 0) {
              tooltip.add(Util.translateFormatted(
                  "gui." + ModulePyrotech.MOD_ID + ".waila.bloom.hammer.power",
                  hammerPower
              ));
            }
          }
        }

      }

    }

    return tooltip;
  }
}
