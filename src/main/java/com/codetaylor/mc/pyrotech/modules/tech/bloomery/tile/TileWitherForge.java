package com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile;

import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.WitherForgeRecipe;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileWitherForge
    extends TileBloomery {

  private int soundTickCounter;

  @Override
  public int getMaxFuelCount() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.FUEL_CAPACITY_ITEMS;
  }

  @Override
  public int getMaxBurnTime() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.FUEL_CAPACITY_BURN_TIME;
  }

  @Override
  public boolean hasSpeedCap() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.HAS_SPEED_CAP;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechBloomeryConfig.WITHER_FORGE.AIRFLOW_DRAG_MODIFIER;
  }

  @Override
  protected float getAirflowModifier() {

    return (float) ModuleTechBloomeryConfig.WITHER_FORGE.AIRFLOW_MODIFIER;
  }

  @Override
  protected double getAshConversionChance() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.ASH_CONVERSION_CHANCE;
  }

  @Override
  public int getMaxAshCapacity() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.MAX_ASH_CAPACITY;
  }

  @Override
  protected double getSpeedScalar() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.SPEED_SCALAR;
  }

  @Override
  protected double getSpecialFuelBurnTimeModifier(ItemStack stack) {

    return ModuleTechBloomeryConfig.WITHER_FORGE.getSpecialFuelBurnTimeModifier(stack);
  }

  @Override
  protected int getInputCapacity() {

    return Math.min(64, Math.max(0, ModuleTechBloomeryConfig.WITHER_FORGE.CAPACITY));
  }

  @Override
  protected boolean dropSlagWhenBroken() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.DROP_SLAG_WHEN_BROKEN;
  }

  @Override
  protected List<BlockPos> getAirflowCheckPositions() {

    IBlockState tileBlockState = this.world.getBlockState(this.pos);
    EnumFacing tileFacing = this.getTileFacing(this.world, this.pos, tileBlockState);
    BlockPos offset = this.pos.offset(tileFacing);

    return Lists.newArrayList(
        offset,
        this.pos.offset(FacingHelper.rotateFacingCW(tileFacing)),
        this.pos.offset(FacingHelper.rotateFacingCW(tileFacing, 3))
    );
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechBloomery.Blocks.WITHER_FORGE) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return EnumFacing.NORTH;
  }

  @Override
  protected void createSlag() {

    int count = this.getInputStackHandler().getStackInSlot(0).getCount();
    EnumFacing facing = this.getTileFacing(this.world, this.pos, this.world.getBlockState(this.pos));

    BlockPos[] positions = {
        this.pos.offset(facing),
        this.pos.offset(FacingHelper.rotateFacingCW(facing)),
        this.pos.offset(FacingHelper.rotateFacingCW(facing, 3))
    };

    for (int i = 0; i < count; i++) {
      int index = this.world.rand.nextInt(positions.length);
      this.createSlag(positions[index]);
    }
  }

  @Override
  public void update() {

    super.update();

    if (this.world.isRemote) {
      Random rand = RandomHelper.random();

      if (this.isActive()) {

        IBlockState state = this.world.getBlockState(this.pos);
        EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

        if (rand.nextDouble() < 0.5) {
          this.spawnDrip(rand, FacingHelper.rotateFacingCW(facing));
        }

        if (rand.nextDouble() < 0.5) {
          this.spawnDrip(rand, FacingHelper.rotateFacingCW(facing, 3));
        }
      }

    } else { // server

      if (ModuleTechBloomeryConfig.WITHER_FORGE.ENABLE_SCARY_SOUNDS) {

        if (this.isActive()) {
          this.soundTickCounter -= 1;

          if (this.soundTickCounter <= 0) {
            Random random = RandomHelper.random();
            int variance = (int) ((random.nextFloat() * 2 - 1) * ModuleTechBloomeryConfig.WITHER_FORGE.SCARY_SOUND_INTERVAL_VARIANCE_TICKS);
            this.soundTickCounter = variance + ModuleTechBloomeryConfig.WITHER_FORGE.SCARY_SOUND_INTERVAL_TICKS;

            WeightedPicker<SoundEvent> picker = new WeightedPicker<>();
            picker.add(1, SoundEvents.ENTITY_GHAST_SCREAM);
            picker.add(5, SoundEvents.ENTITY_GHAST_AMBIENT);
            picker.add(5, SoundEvents.ENTITY_GHAST_WARN);
            picker.add(5, SoundEvents.ENTITY_WITHER_SKELETON_HURT);
            picker.add(5, SoundEvents.ENTITY_WITHER_HURT);
            picker.add(10, SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT);
            picker.add(25, SoundEvents.ENTITY_WITHER_AMBIENT);
            picker.add(50, SoundEvents.ENTITY_WITHER_SKELETON_STEP);

            SoundHelper.playSoundServer(
                this.world,
                this.pos,
                picker.get(),
                SoundCategory.BLOCKS,
                random.nextFloat() * 0.2f + 0.4f,
                0.4f + random.nextFloat() * 1.0f
            );
          }

        } else {
          this.soundTickCounter = 0;
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  @Override
  protected WitherForgeRecipe getRecipe(ItemStack stackInSlot) {

    return WitherForgeRecipe.getRecipe(stackInSlot);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBloomeryConfig.STAGES_WITHER_FORGE;
  }
}
