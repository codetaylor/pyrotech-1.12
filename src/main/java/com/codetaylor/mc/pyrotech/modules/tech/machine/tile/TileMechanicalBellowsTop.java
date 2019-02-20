package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMechanicalBellowsTop
    extends TileCogWorkerBase {

  private boolean pushing;
  private TickCounter pushTickCounter;

  public TileMechanicalBellowsTop() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.pushing = false;
    this.pushTickCounter = new TickCounter(ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_DOWN_TICKS);
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
    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.COG_DAMAGE;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_BELLOWS) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return super.getTileFacing(world, pos, blockState);
  }
}
