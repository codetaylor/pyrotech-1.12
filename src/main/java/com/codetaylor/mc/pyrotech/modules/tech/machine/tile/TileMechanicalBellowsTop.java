package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileMechanicalBellowsTop
    extends TileCogWorkerBase {

  private final TickCounter pushTickCounter;

  private boolean pushing;

  public TileMechanicalBellowsTop() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.pushTickCounter = new TickCounter(ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_DOWN_TICKS);

    this.pushing = false;
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  protected boolean isValidCog(ItemStack itemStack) {

    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.isValidCog(itemStack.getItem().getRegistryName());
  }

  @Override
  protected int getUpdateIntervalTicks() {

    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_DOWN_TICKS
        + ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_UP_TICKS;
  }

  public boolean isPushing() {

    return this.pushing;
  }

  // ---------------------------------------------------------------------------
  // - Cog
  // ---------------------------------------------------------------------------

  @Override
  protected Transform getCogInteractionTransform() {

    return new Transform(
        Transform.translate(0.5, 7.0 / 16.0, 17.0 / 16.0),
        Transform.rotate(),
        Transform.scale(0.75, 0.75, 2.00)
    );
  }

  @Override
  protected AxisAlignedBB getCogInteractionBounds() {

    return AABBHelper.create(0, 0, 12, 16, 16, 16);
  }

  // ---------------------------------------------------------------------------
  // - Worker
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    super.update();

    if (this.pushing
        && this.pushTickCounter.increment()) {
      this.pushing = false;
    }
  }

  @Override
  protected int doWork(ItemStack cog) {

    this.pushing = true;
    SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f, RandomHelper.random().nextFloat() * 0.2f + 0.8f);
    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.COG_DAMAGE;
  }

  @Override
  protected boolean isPowered() {

    return this.world.isBlockPowered(this.pos)
        || this.world.isBlockPowered(this.pos.down());
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_MECHANICAL_BELLOWS;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_BELLOWS) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return super.getTileFacing(world, pos, blockState);
  }
}
