package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickSawmillRecipe;
import net.minecraft.item.ItemStack;

public class TileBrickSawmill
    extends TileSawmillBase<BrickSawmillRecipe> {

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.ALLOW_AUTOMATION;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.FUEL_SLOT_SIZE;
  }

  @Override
  protected double getWoodChipsChance() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.WOOD_CHIPS_CHANCE;
  }

  @Override
  protected boolean shouldDamageBlades() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.DAMAGE_BLADES;
  }

  @Override
  public double getEntityDamageFromBlade() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.ENTITY_DAMAGE_FROM_BLADE;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public BrickSawmillRecipe getRecipe(ItemStack itemStack) {

    return BrickSawmillRecipe.getRecipe(itemStack, this.getBlade());
  }

}
