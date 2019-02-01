package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.library.spi.tile.TileBurnableBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRefractoryDoor;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

  private FluidTank fluidTank;
  private ResourceLocation recipeKey;
  private ItemStackHandler output;

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
      return recipe.getTimeTicks();

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

        } else if (tileEntity instanceof TileTarCollector) {
          TileTarCollector down = (TileTarCollector) tileEntity;
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
    TileEntity tileEntity = this.world.getTileEntity(this.pos);

    if (tileEntity instanceof TilePitAsh) {
      TilePitAsh tilePitAsh = (TilePitAsh) tileEntity;

      for (ItemStack itemStack : itemStackList) {
        tilePitAsh.insertItem(itemStack);
      }
    }
  }

  @Override
  protected void onInvalidDelayExpired() {

    this.world.setBlockToAir(this.pos);
  }

  @Override
  protected boolean isValidStructureBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    blockState = blockState.getActualState(world, pos);
    PitBurnRecipe recipe = ModuleTechRefractory.Registries.BURN_RECIPE.getValue(this.recipeKey);

    if (recipe == null) {
      return false;
    }

    if (recipe.requiresRefractoryBlocks()) {

      for (Predicate<IBlockState> matcher : ModuleTechRefractory.Registries.REFRACTORY_BLOCK_LIST) {

        if (matcher.test(blockState)) {
          return true;
        }
      }

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

    if (blockState.getBlock() == door) {

      if (!blockState.getValue(BlockRefractoryDoor.OPEN)
          && blockState.getValue(BlockRefractoryDoor.FACING) == facing) {
        return true;

      } else if (blockState.getValue(BlockRefractoryDoor.OPEN)
          && blockState.getValue(BlockRefractoryDoor.HINGE) == BlockDoor.EnumHingePosition.LEFT
          && blockState.getValue(BlockRefractoryDoor.FACING) == facing.rotateYCCW()) {
        return true;

      } else if (blockState.getValue(BlockRefractoryDoor.OPEN)
          && blockState.getValue(BlockRefractoryDoor.HINGE) == BlockDoor.EnumHingePosition.RIGHT
          && blockState.getValue(BlockRefractoryDoor.FACING) == facing.rotateY()) {
        return true;
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
