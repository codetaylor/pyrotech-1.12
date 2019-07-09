package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class AnvilProviderDelegate {

  private final AnvilRecipe.EnumTier tier;
  private final IAnvilDisplay display;

  public AnvilProviderDelegate(AnvilRecipe.EnumTier tier, IAnvilDisplay display) {

    this.tier = tier;
    this.display = display;
  }

  public void display(
      TileAnvilBase tile
  ) {

    float progress = tile.getRecipeProgress();
    ItemStackHandler stackHandler = tile.getStackHandler();
    ItemStack input = stackHandler.getStackInSlot(0);

    if (!input.isEmpty()) {

      // Display input item and recipe output.

      AnvilRecipe recipe = AnvilRecipe.getRecipe(input, this.tier);

      if (recipe != null) {
        ItemStack recipeOutput = recipe.getOutput();

        if (!recipeOutput.isEmpty()) {

          if (recipe instanceof BloomAnvilRecipe) {
            recipeOutput.setCount(1);
          }

          this.display.setRecipeProgress(input, recipeOutput, (int) (100 * progress), 100);
        }

        AnvilRecipe.EnumType recipeType = recipe.getType();
        String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.anvil.recipe.type";

        if (recipeType == AnvilRecipe.EnumType.HAMMER) {
          String typeLangKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.anvil.recipe.type.hammer";
          this.display.setRecipeType(langKey, typeLangKey);

        } else if (recipeType == AnvilRecipe.EnumType.PICKAXE) {
          String typeLangKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.anvil.recipe.type.pickaxe";
          this.display.setRecipeType(langKey, typeLangKey);

        } else {
          throw new RuntimeException("Unknown recipe type: " + recipeType);
        }
      }

      if (recipe instanceof BloomAnvilRecipe) {
        this.display.setBloomName(TextFormatting.GOLD, input);
        Item item = input.getItem();

        if (item instanceof BlockBloom.ItemBlockBloom) {
          BlockBloom.ItemBlockBloom bloom = (BlockBloom.ItemBlockBloom) item;

          {
            int integrity = (int) ((bloom.getIntegrity(input) / (float) bloom.getMaxIntegrity(input)) * 100);
            String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.bloom.integrity";
            this.display.setIntegrity(langKey, integrity);
          }

          {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            int hammerPower = (int) (BloomHelper.calculateHammerPower(tile.getPos(), player) * 100);
            String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.bloom.hammer.power";

            if (hammerPower > 0) {
              this.display.setHammerPower(null, langKey, hammerPower);

            } else {
              this.display.setHammerPower(TextFormatting.RED, langKey, hammerPower);
            }
          }
        }
      }
    }
  }

  public interface IAnvilDisplay {
  
    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setRecipeType(String langKey, String typeLangKey);

    void setBloomName(@Nullable TextFormatting textFormatting, ItemStack input);

    void setIntegrity(String langKey, int integrity);

    void setHammerPower(@Nullable TextFormatting textFormatting, String langKey, int hammerPower);
  }
}
