package com.codetaylor.mc.pyrotech.modules.tech.refractory.util;

import com.codetaylor.mc.pyrotech.library.util.FloodFill;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileActivePile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public final class RefractoryIgnitionHelper {

  public static boolean igniteBlocks(World world, BlockPos pos) {

    IBlockState blockState = world.getBlockState(pos);
    PitBurnRecipe recipe = PitBurnRecipe.getRecipe(blockState);
    boolean result = false;

    if (recipe != null) {

      Predicate<IBlockState> predicate = recipe.getInputMatcher();

      result = FloodFill.apply(
          world,
          pos,
          (w, p) -> predicate.test(w.getBlockState(p)),
          (w, p) -> {
            w.setBlockState(p, ModuleTechRefractory.Blocks.ACTIVE_PILE.getDefaultState());
            TileEntity tileEntity = w.getTileEntity(p);

            if (tileEntity instanceof TileActivePile) {
              ((TileActivePile) tileEntity).setRecipe(recipe);
            }
            return true;
          },
          MathHelper.clamp(ModuleTechRefractoryConfig.GENERAL.MAXIMUM_BURN_SIZE_BLOCKS, 1, 512)
      );
    }

    return result;
  }

  private RefractoryIgnitionHelper() {
    //
  }
}
