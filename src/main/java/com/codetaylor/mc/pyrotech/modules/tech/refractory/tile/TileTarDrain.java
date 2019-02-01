package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarDrain;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarTankBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileTarDrain
    extends TileTarTankBase {

  public TileTarDrain() {

    super();
    this.fluidTank.setCanFill(false);
  }

  @Override
  protected int getTankCapacity() {

    return ModuleTechRefractoryConfig.TAR_DRAIN.TAR_DRAIN_CAPACITY;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      EnumFacing tileFacing = this.world.getBlockState(this.pos).getValue(BlockTarDrain.FACING);

      return facing == tileFacing
          || facing == tileFacing.getOpposite();
    }

    return false;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      EnumFacing tileFacing = this.world.getBlockState(this.pos).getValue(BlockTarDrain.FACING);

      if (facing == tileFacing
          || facing == tileFacing.getOpposite()) {
        //noinspection unchecked
        return (T) this.fluidTank;
      }
    }

    return null;
  }

  @Override
  protected List<BlockPos> getCollectionSourcePositions(World world, BlockPos origin) {

    IBlockState blockState = world.getBlockState(origin);

    if (blockState.getBlock() != ModuleTechRefractory.Blocks.TAR_DRAIN) {
      return Collections.emptyList();
    }

    EnumFacing facing = blockState.getValue(BlockTarDrain.FACING).getOpposite();
    BlockPos offset = origin.offset(facing, 2);
    List<BlockPos> result = new ArrayList<>(9);

    for (int x = -1; x <= 1; x++) {

      for (int z = -1; z <= 1; z++) {
        result.add(offset.add(x, 0, z));
      }
    }

    return result;
  }

  @Nullable
  @Override
  protected FluidTank getCollectionSourceFluidTank(@Nullable TileEntity tileEntity) {

    if (tileEntity instanceof TileTarCollector) {
      return ((TileTarCollector) tileEntity).getFluidTank();

    } else if (tileEntity instanceof TileActivePile) {
      return ((TileActivePile) tileEntity).getFluidTank();
    }

    return null;
  }

}
