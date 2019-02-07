package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
import net.minecraft.item.ItemStack;

public class TileStoneSawmill
    extends TileSawmillBase<StoneSawmillRecipe> {

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.STONE_SAWMILL.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.STONE_SAWMILL.FUEL_BURN_TIME_MODIFIER;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.STONE_SAWMILL.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.STONE_SAWMILL.FUEL_SLOT_SIZE;
  }

  @Override
  protected double getWoodChipsChance() {

    return ModuleTechMachineConfig.STONE_SAWMILL.WOOD_CHIPS_CHANCE;
  }

  @Override
  protected boolean shouldDamageBlades() {

    return ModuleTechMachineConfig.STONE_SAWMILL.DAMAGE_BLADES;
  }

  @Override
  public double getEntityDamageFromBlade() {

    return ModuleTechMachineConfig.STONE_SAWMILL.ENTITY_DAMAGE_FROM_BLADE;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public StoneSawmillRecipe getRecipe(ItemStack itemStack) {

    return StoneSawmillRecipe.getRecipe(itemStack, this.getBlade());
  }

}
