package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileKilnPit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class KilnPitProviderDelegate
    extends ProviderDelegateBase<KilnPitProviderDelegate.IPitKilnDisplay, TileKilnPit> {

  public KilnPitProviderDelegate(IPitKilnDisplay display) {

    super(display);
  }

  @Override
  public void display(TileKilnPit tile) {

    World world = tile.getWorld();
    BlockPos pos = tile.getPos();
    IBlockState blockState = world.getBlockState(pos);
    ItemStackHandler stackHandler = tile.getStackHandler();
    ItemStack input = stackHandler.getStackInSlot(0);
    KilnPitRecipe recipe = KilnPitRecipe.getRecipe(input);

    if (blockState.getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.COMPLETE) {
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      this.display.setOutputItems(outputStackHandler);

    } else if (recipe != null) {
      // the kiln's recipe isn't complete
      ItemStack output = recipe.getOutput();
      output.setCount(input.getCount() * output.getCount());
      float progress = tile.getProgress();
      this.display.setRecipeProgress(input, output, (int) (100 * progress), 100);
    }
  }

  public interface IPitKilnDisplay {

    void setOutputItems(ItemStackHandler stackHandler);

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);
  }
}
