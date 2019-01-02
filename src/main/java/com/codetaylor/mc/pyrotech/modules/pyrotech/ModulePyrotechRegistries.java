package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.*;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModulePyrotechRegistries {

  public static final IForgeRegistryModifiable<KilnPitRecipe> KILN_PIT_RECIPE;
  public static final IForgeRegistryModifiable<KilnStoneRecipe> KILN_STONE_RECIPE;
  public static final IForgeRegistryModifiable<MillStoneRecipe> MILL_STONE_RECIPE;
  public static final IForgeRegistryModifiable<CrucibleStoneRecipe> CRUCIBLE_STONE_RECIPE;
  public static final IForgeRegistryModifiable<PitBurnRecipe> BURN_RECIPE;
  public static final IForgeRegistryModifiable<DryingRackRecipe> DRYING_RACK_RECIPE;
  public static final IForgeRegistryModifiable<DryingRackCrudeRecipe> DRYING_RACK_CRUDE_RECIPE;
  public static final IForgeRegistryModifiable<ChoppingBlockRecipe> CHOPPING_BLOCK_RECIPE;
  public static final IForgeRegistryModifiable<GraniteAnvilRecipe> GRANITE_ANVIL_RECIPE;

  public static final List<Predicate<IBlockState>> REFRACTORY_BLOCK_LIST;
  public static final List<Predicate<IBlockState>> COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST;

  static {
    KILN_PIT_RECIPE = (IForgeRegistryModifiable<KilnPitRecipe>) GameRegistry.findRegistry(KilnPitRecipe.class);
    KILN_STONE_RECIPE = (IForgeRegistryModifiable<KilnStoneRecipe>) GameRegistry.findRegistry(KilnStoneRecipe.class);
    MILL_STONE_RECIPE = (IForgeRegistryModifiable<MillStoneRecipe>) GameRegistry.findRegistry(MillStoneRecipe.class);
    CRUCIBLE_STONE_RECIPE = (IForgeRegistryModifiable<CrucibleStoneRecipe>) GameRegistry.findRegistry(CrucibleStoneRecipe.class);
    BURN_RECIPE = (IForgeRegistryModifiable<PitBurnRecipe>) GameRegistry.findRegistry(PitBurnRecipe.class);
    DRYING_RACK_RECIPE = (IForgeRegistryModifiable<DryingRackRecipe>) GameRegistry.findRegistry(DryingRackRecipe.class);
    DRYING_RACK_CRUDE_RECIPE = (IForgeRegistryModifiable<DryingRackCrudeRecipe>) GameRegistry.findRegistry(DryingRackCrudeRecipe.class);
    CHOPPING_BLOCK_RECIPE = (IForgeRegistryModifiable<ChoppingBlockRecipe>) GameRegistry.findRegistry(ChoppingBlockRecipe.class);
    GRANITE_ANVIL_RECIPE = (IForgeRegistryModifiable<GraniteAnvilRecipe>) GameRegistry.findRegistry(GraniteAnvilRecipe.class);

    REFRACTORY_BLOCK_LIST = new ArrayList<>();
    COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST = new ArrayList<>();
  }

  private ModulePyrotechRegistries() {
    //
  }
}
