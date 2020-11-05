package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

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
  public double getFuelBurnTimeModifier(ItemStack fuel) {

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
  protected boolean shouldDamageBlades() {

    return ModuleTechMachineConfig.STONE_SAWMILL.DAMAGE_BLADES;
  }

  @Override
  public double getEntityDamageFromBlade() {

    return ModuleTechMachineConfig.STONE_SAWMILL.ENTITY_DAMAGE_FROM_BLADE;
  }

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.STONE_SAWMILL.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.STONE_SAWMILL.AIRFLOW_DRAG_MODIFIER;
  }

  @Override
  protected boolean isValidSawmillBlade(ItemStack itemStack) {

    ResourceLocation registryName = itemStack.getItem().getRegistryName();

    if (registryName == null) {
      return false;
    }

    return ArrayHelper.contains(ModuleTechMachineConfig.STONE_SAWMILL.SAWMILL_BLADES, registryName.toString());
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_STONE_SAWMILL;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public StoneSawmillRecipe getRecipe(ItemStack itemStack) {

    return StoneSawmillRecipe.getRecipe(itemStack, this.getBlade());
  }

}
