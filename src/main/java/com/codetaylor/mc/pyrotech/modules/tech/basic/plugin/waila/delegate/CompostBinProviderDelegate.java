package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompostBin;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

public class CompostBinProviderDelegate
    extends ProviderDelegateBase<CompostBinProviderDelegate.ICompostBinDisplay, TileCompostBin> {

  private float[] progress = new float[4];

  public CompostBinProviderDelegate(ICompostBinDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCompostBin tile) {

    ItemStack currentRecipeOutput = tile.getCurrentRecipeOutput();

    if (currentRecipeOutput.isEmpty()) {
      return;
    }

    Arrays.fill(this.progress, 0);
    this.progress = tile.getMostCompleteActiveLayerProgress(this.progress);
    TileCompostBin.InputStackHandler inputStackHandler = tile.getInputStackHandler();
    TileCompostBin.OutputStackHandler outputStackHandler = tile.getOutputStackHandler();

    for (int i = this.progress.length - 1; i >= 0; i--) {

      if (this.progress[i] > 0) {
        this.display.setRecipeProgress(currentRecipeOutput, (int) (100 * this.progress[i]), 100);

        if (!Minecraft.getMinecraft().player.isSneaking()) {
          break;
        }
      }
    }

    String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.moisture.level";

    if (tile.getMoistureLevel() > 0.1) {
      this.display.setMoistureLevel(null, langKey, (int) (tile.getMoistureLevel() * 100));

    } else {
      this.display.setMoistureLevel(TextFormatting.RED, langKey, (int) (tile.getMoistureLevel() * 100));
    }

    if (inputStackHandler.getTotalItemCount() > 0 || outputStackHandler.getTotalItemCount() > 0) {
      ItemStack stackedOutput = tile.getCurrentRecipeOutput().copy();
      stackedOutput.setCount(outputStackHandler.getTotalItemCount());
      this.display.setContents(inputStackHandler, BlockRock.EnumType.DIRT.asStack(tile.getStoredCompostValue()), stackedOutput);
    }
  }

  public interface ICompostBinDisplay {

    void setContents(ItemStackHandler inputStackHandler, ItemStack storedCompostValue, ItemStack outputStackHandler);

    void setRecipeProgress(ItemStack output, int progress, int maxProgress);

    void setMoistureLevel(@Nullable TextFormatting formatting, String langKey, int moistureLevel);
  }
}
