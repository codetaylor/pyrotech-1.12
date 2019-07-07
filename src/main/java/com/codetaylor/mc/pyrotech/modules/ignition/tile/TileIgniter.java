package com.codetaylor.mc.pyrotech.modules.ignition.tile;

import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentIgniterBlock;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.util.RefractoryIgnitionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileIgniter
    extends TileEntityBase
    implements ITickable {

  private boolean isPowered;

  public void setPowered(boolean powered) {

    this.isPowered = powered;
  }

  @Override
  public void update() {

    if (this.world.isRemote
        || !this.isPowered) {
      return;
    }

    IBlockState blockState = this.world.getBlockState(this.pos);
    EnumFacing selfFacing = blockState.getValue(Properties.FACING_HORIZONTAL);
    EnumFacing selfFacingOpposite = selfFacing.getOpposite();

    BlockPos offset = this.pos.offset(selfFacingOpposite);
    IBlockState facingBlockState = this.world.getBlockState(offset);
    Block facingBlock = facingBlockState.getBlock();

    if (Util.canSetFire(this.world, offset)) {
      this.world.setBlockState(offset, Blocks.FIRE.getDefaultState(), 3);

    } else if (facingBlock instanceof IBlockIgnitableAdjacentIgniterBlock) {
      ((IBlockIgnitableAdjacentIgniterBlock) facingBlock).igniteWithAdjacentIgniterBlock(this.world, offset, facingBlockState, selfFacing);

    } else {
      RefractoryIgnitionHelper.igniteBlocks(this.world, offset);
    }
  }
}
