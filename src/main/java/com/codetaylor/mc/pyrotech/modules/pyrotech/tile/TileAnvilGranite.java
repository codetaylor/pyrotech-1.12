package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockAnvilBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TileAnvilGranite
    extends TileAnvilBase {

  @Override
  public int getBloomAnvilDamagePerHit() {

    return ModulePyrotechConfig.GRANITE_ANVIL.BLOOM_DAMAGE_PER_HIT;
  }

  @Override
  protected int getHitsPerDamage() {

    return ModulePyrotechConfig.GRANITE_ANVIL.HITS_PER_DAMAGE;
  }

  @Override
  protected double getExhaustionCostPerCraftComplete() {

    return ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_CRAFT_COMPLETE;
  }

  @Override
  protected double getExhaustionCostPerHit() {

    return ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_HIT;
  }

  @Override
  protected int getHammerHitReduction(ResourceLocation resourceLocation) {

    return ModulePyrotechConfig.ANVIL_COMMON.getHammerHitReduction(resourceLocation);
  }

  @Override
  protected String[] getPickaxeWhitelist() {

    return ModulePyrotechConfig.ANVIL_COMMON.PICKAXE_WHITELIST;
  }

  @Override
  protected String[] getPickaxeBlacklist() {

    return ModulePyrotechConfig.ANVIL_COMMON.PICKAXE_BLACKLIST;
  }

  @Override
  protected int getMinimumHungerToUse() {

    return ModulePyrotechConfig.GRANITE_ANVIL.MINIMUM_HUNGER_TO_USE;
  }

  @Nonnull
  @Override
  protected BlockAnvilBase getBlock() {

    return ModuleBlocks.ANVIL_GRANITE;
  }
}
