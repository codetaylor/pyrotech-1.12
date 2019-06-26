package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

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
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.BRICK_SAWMILL.FUEL_BURN_TIME_MODIFIER;
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
  protected boolean shouldDamageBlades() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.DAMAGE_BLADES;
  }

  @Override
  public double getEntityDamageFromBlade() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.ENTITY_DAMAGE_FROM_BLADE;
  }

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.BRICK_SAWMILL.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.BRICK_SAWMILL.AIRFLOW_DRAG_MODIFIER;
  }

  @Override
  protected boolean isValidSawmillBlade(ItemStack itemStack) {

    ResourceLocation registryName = itemStack.getItem().getRegistryName();

    if (registryName == null) {
      return false;
    }

    return ArrayHelper.contains(ModuleTechMachineConfig.BRICK_SAWMILL.SAWMILL_BLADES, registryName.toString());
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_BRICK_SAWMILL;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public BrickSawmillRecipe getRecipe(ItemStack itemStack) {

    return BrickSawmillRecipe.getRecipe(itemStack, this.getBlade());
  }

}
