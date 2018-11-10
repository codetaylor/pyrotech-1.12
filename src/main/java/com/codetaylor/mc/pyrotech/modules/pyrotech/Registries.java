package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Registries {

  public static final IForgeRegistry<KilnPitRecipe> KILN_PIT_RECIPE;
  public static final IForgeRegistry<KilnBrickRecipe> KILN_BRICK_RECIPE;
  public static final IForgeRegistry<PitBurnRecipe> BURN_RECIPE;

  public static final List<Predicate<IBlockState>> REFRACTORY_BLOCK_LIST;
  public static final List<Predicate<IBlockState>> COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST;

  static {
    KILN_PIT_RECIPE = GameRegistry.findRegistry(KilnPitRecipe.class);
    KILN_BRICK_RECIPE = GameRegistry.findRegistry(KilnBrickRecipe.class);
    BURN_RECIPE = GameRegistry.findRegistry(PitBurnRecipe.class);

    REFRACTORY_BLOCK_LIST = new ArrayList<>();
    COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST = new ArrayList<>();
  }

  private Registries() {
    //
  }

}
