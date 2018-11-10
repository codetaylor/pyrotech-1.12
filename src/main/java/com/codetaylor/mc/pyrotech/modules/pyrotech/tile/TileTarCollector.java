package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TileTarCollector
    extends TileTarTankBase {

  private static final int DEFAULT_TICKS_TO_EXTINGUISH = 100;

  private boolean burning;
  private int burnTicksRemaining;
  private int extinguishTicksRemaining;

  public TileTarCollector() {

    super();
    this.burning = false;
    this.extinguishTicksRemaining = DEFAULT_TICKS_TO_EXTINGUISH;
  }

  public void setBurning(boolean burning) {

    this.burning = burning;

    if (!this.world.isRemote) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }
  }

  public boolean isBurning() {

    return this.burning;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        && facing == EnumFacing.UP);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        && facing == EnumFacing.UP) {
      //noinspection unchecked
      return (T) this.fluidTank;
    }

    return super.getCapability(capability, facing);
  }

  @Override
  protected List<BlockPos> getCollectionSourcePositions(World world, BlockPos origin) {

    return Collections.emptyList();
  }

  @Override
  protected int getTankCapacity() {

    return ModulePyrotechConfig.GENERAL.TAR_COLLECTOR_CAPACITY;
  }

  @Nullable
  @Override
  protected FluidTank getCollectionSourceFluidTank(@Nullable TileEntity tileEntity) {

    if (tileEntity instanceof TileActivePile) {
      return ((TileActivePile) tileEntity).getFluidTank();
    }

    return null;
  }

  @Override
  public void update() {

    super.update();

    if (this.world.isRemote) {

      if (this.isBurning()) {
        this.spawnParticles();
      }

      return;
    }

    if (this.isBurning()) { // should be burning

      if (this.shouldExtinguish()) {
        this.extinguishTicksRemaining -= 1;

        if (this.extinguishTicksRemaining <= 0) {
          this.burnTicksRemaining = 0;
          this.setBurning(false);
          this.removeFire(this.world, this.pos.up());
          this.extinguishTicksRemaining = DEFAULT_TICKS_TO_EXTINGUISH;
          return;
        }

      } else {
        this.extinguishTicksRemaining = DEFAULT_TICKS_TO_EXTINGUISH;
      }

      this.burnTicksRemaining -= 1;

      for (EnumFacing facing : EnumFacing.VALUES) {
        this.tryCatchFire(this.world, this.pos.offset(facing.getOpposite()), 100, Util.RANDOM, facing);
      }

      FluidStack fluidStack = this.fluidTank.getFluid();

      if (this.burnTicksRemaining <= 0) {
        // If we have no burn ticks remaining, check if we can replenish using fuel.
        this.tryConsumeFuel(fluidStack);
      }

      if (this.burnTicksRemaining > 0) {
        // If we still have burn ticks remaining, assert fire.
        this.assertFire(this.world, this.pos.up());

      } else {
        // Otherwise, we should not be on fire.
        //this.douseFire(this.world, up);
        this.setBurning(false);
      }
    }
  }

  private void removeFire(World world, BlockPos up) {

    if (world.getBlockState(up).getBlock() == Blocks.FIRE) {
      world.setBlockToAir(up);
    }
  }

  private boolean shouldExtinguish() {

    BlockPos up = this.pos.up();
    IBlockState blockState = this.world.getBlockState(up);

    return blockState.isSideSolid(this.world, up, EnumFacing.DOWN)
        && !blockState.getBlock().isFlammable(this.world, up, EnumFacing.DOWN);
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticles() {

    double centerX = this.pos.getX() + 0.5;
    double centerY = this.pos.getY() + 1;
    double centerZ = this.pos.getZ() + 0.5;

    for (int i = 0; i < ModulePyrotechConfig.CLIENT.BURNING_COLLECTOR_SMOKE_PARTICLES; i++) {
      spawnParticles(centerX, centerY, centerZ, EnumParticleTypes.SMOKE_LARGE);
    }

    if (this.world.getBlockState(this.pos.up()).getBlock() == Blocks.FIRE) {
      spawnParticles(centerX, centerY, centerZ, EnumParticleTypes.LAVA);
    }
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticles(double centerX, double centerY, double centerZ, EnumParticleTypes particleType) {

    this.world.spawnParticle(
        particleType,
        centerX + (Util.RANDOM.nextDouble() - 0.5),
        centerY,
        centerZ + (Util.RANDOM.nextDouble() - 0.5),
        0.0D,
        0.1D + (Util.RANDOM.nextFloat() * 2 - 1) * 0.05,
        0.0D
    );
  }

  private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, EnumFacing face) {

    int i = worldIn.getBlockState(pos).getBlock().getFlammability(worldIn, pos, face);

    if (random.nextInt(chance) < i) {
      IBlockState iblockstate = worldIn.getBlockState(pos);

      if (random.nextInt(10) < 5 && !worldIn.isRainingAt(pos)) {
        int j = random.nextInt(5) / 4;

        if (j > 15) {
          j = 15;
        }

        worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, j), 3);
      } else {
        worldIn.setBlockToAir(pos);
      }

      if (iblockstate.getBlock() == Blocks.TNT) {
        Blocks.TNT.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, Boolean.TRUE));
      }
    }

    TileEntity tileEntity = this.world.getTileEntity(pos);

    if (tileEntity instanceof TileTarCollector) {

      TileTarCollector collector = (TileTarCollector) tileEntity;
      if (!collector.isBurning()
          && collector.isFlammable()) {
        collector.setBurning(true);
      }
    }
  }

  private void tryConsumeFuel(FluidStack fluidStack) {

    if (fluidStack == null) {
      return;
    }

    int burnTicksPerMilliBucket = this.getBurnTicksPerMilliBucket(fluidStack.getFluid());

    if (burnTicksPerMilliBucket > 0) {
      // We can replenish fuel.
      this.burnTicksRemaining = burnTicksPerMilliBucket;
      this.fluidTank.drainInternal(1, true);
    }
  }

  private void assertFire(World world, BlockPos pos) {

    if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
      return;
    }

    if (Util.canSetFire(this.world, pos)) {
      this.world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
    }
  }

  public boolean isFlammable() {

    FluidStack fluidStack = this.fluidTank.getFluid();

    if (fluidStack == null) {
      return false;
    }

    return this.getBurnTicksPerMilliBucket(fluidStack.getFluid()) > 0;
  }

  private int getBurnTicksPerMilliBucket(Fluid fluid) {

    Integer value = ModulePyrotechConfig.REFRACTORY.FLUID_BURN_TICKS.get(fluid.getName());

    if (value == null) {
      return 0;
    }

    return value;
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setBoolean("burning", this.burning);
    compound.setInteger("burnTicksRemaining", this.burnTicksRemaining);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.burning = compound.getBoolean("burning");
    this.burnTicksRemaining = compound.getInteger("burnTicksRemaining");
  }
}
