package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.library.spi.tile.TileBurnableBase;
import com.codetaylor.mc.pyrotech.library.util.FloodFill;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRefractoryDoor;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarCollectorBase;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TileActivePile
    extends TileBurnableBase {

  private static final int DEFAULT_TOTAL_BURN_TIME_TICKS = 1000;
  private static final int DEFAULT_BURN_STAGES = 1;
  private static final int MAX_FLUID_PUSH_DEPTH = 3;

  private final FluidTank fluidTank;
  private ResourceLocation recipeKey;
  private final ItemStackHandler output;

  public TileActivePile() {

    super(ModuleCore.TILE_DATA_SERVICE);
    this.fluidTank = new FluidTank(this.getMaxFluidLevel());
    this.output = new ItemStackHandler(9);
  }

  public FluidTank getFluidTank() {

    return this.fluidTank;
  }

  public ItemStackHandler getOutput() {

    return this.output;
  }

  public void setRecipe(PitBurnRecipe recipe) {

    this.recipeKey = recipe.getRegistryName();

    // Call this to recalculate the current burn time after setting the recipe.
    this.reset();

    // Call this for the same reason.
    this.remainingStages = this.getBurnStages();
  }

  @Override
  protected int getTotalBurnTimeTicks() {

    PitBurnRecipe recipe = ModuleTechRefractory.Registries.BURN_RECIPE.getValue(this.recipeKey);

    if (recipe != null) {
      double duration = recipe.getTimeTicks();

      if (recipe.requiresRefractoryBlocks()) {
        duration *= ModuleTechRefractoryConfig.REFRACTORY.REFRACTORY_RECIPE_DURATION_MODIFIER;
      }
      return (int) Math.max(1, duration);

    } else {
      return DEFAULT_TOTAL_BURN_TIME_TICKS;
    }
  }

  @Override
  protected int getBurnStages() {

    PitBurnRecipe recipe = ModuleTechRefractory.Registries.BURN_RECIPE.getValue(this.recipeKey);

    if (recipe != null) {
      return recipe.getBurnStages();

    } else {
      return DEFAULT_BURN_STAGES;
    }
  }

  protected int getMaxFluidLevel() {

    return ModuleTechRefractoryConfig.REFRACTORY.ACTIVE_PILE_MAX_FLUID_CAPACITY;
  }

  @Override
  protected boolean isActive() {

    return true;
  }

  @Override
  protected void onUpdate() {
    //
  }

  @Override
  protected void onUpdateValid() {
    //
  }

  @Override
  protected void onUpdateInvalid() {

    for (EnumFacing facing : EnumFacing.VALUES) {
      BlockPos offset = this.pos.offset(facing);
      IBlockState blockState = this.world.getBlockState(offset);
      Block block = blockState.getBlock();

      if (block.isAir(blockState, this.world, offset) ||
          block.isReplaceable(this.world, offset)) {
        this.world.setBlockState(offset, Blocks.FIRE.getDefaultState());
      }
    }
  }

  @Override
  protected void onBurnStageComplete() {

    PitBurnRecipe recipe = ModuleTechRefractory.Registries.BURN_RECIPE.getValue(this.recipeKey);

    if (recipe == null) {
      return;
    }

    // --- Fill Fluid ---

    FluidStack fluidStack = recipe.getFluidProduced();

    if (fluidStack != null) {
      FluidStack fluidProduced = fluidStack.copy();

      if (fluidProduced.amount > 0) {
        this.fluidTank.fill(fluidProduced, true);
      }
    }

    // --- Push Fluid Down ---

    if (this.fluidTank.getFluidAmount() > 0) {

      for (int i = MAX_FLUID_PUSH_DEPTH; i >= 1; i--) {

        TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(EnumFacing.DOWN, i));

        if (tileEntity instanceof TileActivePile) {
          TileActivePile down = (TileActivePile) tileEntity;
          this.fluidTank.drain(down.fluidTank.fillInternal(this.fluidTank.getFluid(), true), true);

        } else if (tileEntity instanceof TileTarCollectorBase) {
          TileTarCollectorBase down = (TileTarCollectorBase) tileEntity;
          this.fluidTank.drain(down.getFluidTank().fillInternal(this.fluidTank.getFluid(), true), true);
        }

        if (this.fluidTank.getFluidAmount() == 0) {
          break;
        }
      }
    }

    // --- Items ---

    ItemStack output = recipe.getOutput();
    float failureChance = recipe.getFailureChance();
    ItemStack[] failureItems = recipe.getFailureItems();

    if (recipe.doesFluidLevelAffectFailureChance()) {
      failureChance += (1 - failureChance) * (this.fluidTank.getFluidAmount() / (float) this.fluidTank.getCapacity());
    }

    if (!recipe.requiresRefractoryBlocks()) {
      // Modify failure chance if in refractory

      boolean[] isValidRefractory = {true};

      FloodFill.apply(this.world, this.pos,
          (w, p) -> w.getBlockState(p).getBlock() == ModuleTechRefractory.Blocks.ACTIVE_PILE
              || w.getBlockState(p).getBlock() == ModuleTechRefractory.Blocks.PIT_ASH_BLOCK,
          (w, p) -> {

            for (EnumFacing facing : EnumFacing.VALUES) {
              BlockPos offset = p.offset(facing);
              IBlockState blockState = w.getBlockState(offset);
              //noinspection deprecation
              blockState = blockState.getBlock().getActualState(blockState, w, offset);

              boolean isValid = false;

              for (Predicate<IBlockState> predicate : ModuleTechRefractory.Registries.REFRACTORY_BLOCK_LIST) {

                if (predicate.test(blockState)) {
                  isValid = true;
                  break;
                }
              }

              if (!isValid && this.isValidDoor(blockState, facing, ModuleCore.Blocks.REFRACTORY_DOOR)) {
                isValid = true;
              }

              if (!isValid) {
                isValidRefractory[0] = false;
                return false;
              }
            }
            return true;
          },
          MathHelper.clamp(ModuleTechRefractoryConfig.GENERAL.MAXIMUM_BURN_SIZE_BLOCKS, 1, 512)
      );

      if (isValidRefractory[0]) {
        failureChance *= Math.max(0, ModuleTechRefractoryConfig.REFRACTORY.REFRACTORY_FAILURE_MODIFIER);
      }
    }

    failureChance = MathHelper.clamp(
        failureChance,
        (float) ModuleTechRefractoryConfig.REFRACTORY.MIN_FAILURE_CHANCE,
        (float) ModuleTechRefractoryConfig.REFRACTORY.MAX_FAILURE_CHANCE
    );

    if (Util.RANDOM.nextFloat() < failureChance) {

      if (failureItems.length > 0) {
        this.insertItem(failureItems[Util.RANDOM.nextInt(failureItems.length)].copy());
      }

    } else {
      this.insertItem(output.copy());
    }
  }

  private void insertItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.output.insertItem(i, output, false);
    }
  }

  @Override
  protected void onAllBurnStagesComplete() {

    List<ItemStack> itemStackList = new ArrayList<>(this.output.getSlots());

    for (int i = 0; i < this.output.getSlots(); i++) {
      ItemStack stackInSlot = this.output.getStackInSlot(i);
      this.output.setStackInSlot(i, ItemStack.EMPTY);

      if (!stackInSlot.isEmpty()) {
        itemStackList.add(stackInSlot);
      }
    }

    IBlockState state = ModuleTechRefractory.Blocks.PIT_ASH_BLOCK.getDefaultState();
    this.world.setBlockState(this.pos, state);

    /*
      2021-06-05
      https://github.com/codetaylor/pyrotech-1.12/issues/213

      Solution from Sledgehammer by Alex Thomson:

        The BulkBlockCapture in Sponge's PhaseTracker causes the call setBlockState to get delayed,
        This delay causes the call getTileEntity to return inconsistent results.

        The fix implemented is to re-schedule the logic on the next tick.
     */

    MinecraftServer minecraftServer = this.world.getMinecraftServer();

    if (minecraftServer != null) {
      // Runs the logic at the start of the next tick.
      // addScheduledTask won't work as it executes the task immediately if it's scheduled while on the Main Thread.
      minecraftServer.futureTaskQueue.add(ListenableFutureTask.create(() -> this.insertItems(this.pos, itemStackList), null));
    }
  }

  private void insertItems(BlockPos pos, List<ItemStack> itemStackList) {

    if (itemStackList.isEmpty()) {
      return;
    }

    TileEntity tileEntity = this.world.getTileEntity(pos);

    if (tileEntity instanceof TilePitAsh) {
      TilePitAsh tilePitAsh = (TilePitAsh) tileEntity;

      for (ItemStack itemStack : itemStackList) {
        tilePitAsh.insertItem(itemStack);
      }
    }
  }

  @Override
  protected void onInvalidDelayExpired() {

    this.world.setBlockState(this.pos, Blocks.FIRE.getDefaultState());
  }

  @Override
  protected boolean isValidStructureBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    blockState = blockState.getActualState(world, pos);
    PitBurnRecipe recipe = ModuleTechRefractory.Registries.BURN_RECIPE.getValue(this.recipeKey);

    if (recipe == null) {
      return false;
    }

    for (Predicate<IBlockState> matcher : ModuleTechRefractory.Registries.REFRACTORY_BLOCK_LIST) {

      if (matcher.test(blockState)) {
        return true;
      }
    }

    if (recipe.requiresRefractoryBlocks()) {
      return this.isValidDoor(blockState, facing, ModuleCore.Blocks.REFRACTORY_DOOR);

    } else {

      if (this.isValidDoor(blockState, facing, ModuleCore.Blocks.REFRACTORY_DOOR)
          || this.isValidDoor(blockState, facing, ModuleCore.Blocks.STONE_DOOR)) {
        return true;
      }

      return super.isValidStructureBlock(world, pos, blockState, facing);
    }
  }

  private boolean isValidDoor(IBlockState blockState, EnumFacing facing, BlockDoor door) {

    if (facing == EnumFacing.UP
        || facing == EnumFacing.DOWN) {
      return false;
    }

    if (blockState.getBlock() == door) {

      if (!blockState.getValue(BlockRefractoryDoor.OPEN)
          && blockState.getValue(BlockRefractoryDoor.FACING) == facing) {
        return true;

      } else if (blockState.getValue(BlockRefractoryDoor.OPEN)
          && blockState.getValue(BlockRefractoryDoor.HINGE) == BlockDoor.EnumHingePosition.LEFT
          && blockState.getValue(BlockRefractoryDoor.FACING) == facing.rotateYCCW()) {
        return true;

      } else {
        return blockState.getValue(BlockRefractoryDoor.OPEN)
            && blockState.getValue(BlockRefractoryDoor.HINGE) == BlockDoor.EnumHingePosition.RIGHT
            && blockState.getValue(BlockRefractoryDoor.FACING) == facing.rotateY();
      }
    }
    return false;
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("fluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
    compound.setString("recipeKey", this.recipeKey.toString());
    compound.setTag("output", this.output.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.fluidTank.readFromNBT(compound.getCompoundTag("fluidTank"));
    this.recipeKey = new ResourceLocation(compound.getString("recipeKey"));
    this.output.deserializeNBT(compound.getCompoundTag("output"));
  }

}
