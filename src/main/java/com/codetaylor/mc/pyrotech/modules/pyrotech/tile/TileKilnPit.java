package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.Registries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.library.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class TileKilnPit
    extends TileBurnableBase
    implements ITickable,
    IProgressProvider {

  private static final int DEFAULT_TOTAL_BURN_TIME_TICKS = 1000;

  private ItemStackHandler logStackHandler;
  private ItemStackHandler stackHandler;
  private ItemStackHandler outputStackHandler;
  private int totalBurnTimeTicks;
  private boolean active;

  // transient
  private EntityItem entityItem;
  private int ticksSinceLastClientSync;

  public TileKilnPit() {

    this.stackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 8;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileKilnPit.this.entityItem = null;
      }
    };

    this.outputStackHandler = new ItemStackHandler(9);

    this.logStackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 3;
      }
    };

    this.totalBurnTimeTicks = DEFAULT_TOTAL_BURN_TIME_TICKS;

    this.setNeedStructureValidation();
    this.reset();
  }

  public ItemStackHandler getLogStackHandler() {

    return this.logStackHandler;
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public void setActive(boolean active) {

    this.active = active;
  }

  public void setTotalBurnTimeTicks(int totalBurnTimeTicks) {

    this.totalBurnTimeTicks = totalBurnTimeTicks;
    this.reset();
  }

  public EntityItem getEntityItem() {

    if (this.entityItem == null) {
      ItemStack stackInSlot = this.stackHandler.getStackInSlot(0);
      this.entityItem = new EntityItem(this.world);
      this.entityItem.setItem(stackInSlot);
    }

    return this.entityItem;
  }

  @Override
  public float getProgress() {

    if (!this.isActive()) {
      return 0;
    }

    int totalBurnTimeTicks = this.getTotalBurnTimeTicks();
    int totalStages = this.getBurnStages();
    int burnTimePerStage = totalBurnTimeTicks / totalStages;
    float progress = ((this.remainingStages - 1) * burnTimePerStage + this.burnTimeTicksPerStage) / (float) totalBurnTimeTicks;

    return 1f - progress;
  }

  @Override
  protected boolean isActive() {

    return this.active;
  }

  @Override
  protected void onUpdate() {

    this.ticksSinceLastClientSync += 1;

    if (this.ticksSinceLastClientSync >= 20) {
      this.ticksSinceLastClientSync = 0;
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }

    if (this.world.isRainingAt(this.pos)) {
      // set back to wood state and douse fire
      IBlockState blockState = ModuleBlocks.KILN_PIT.getDefaultState()
          .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.WOOD);
      this.world.setBlockState(this.pos, blockState);

      BlockPos up = this.pos.up();

      if (this.world.getBlockState(up).getBlock() == Blocks.FIRE) {
        this.world.setBlockToAir(up);
      }

      this.setActive(false);
    }
  }

  @Override
  protected void onUpdateValid() {

    // set the block above to fire if the kiln is active

    BlockPos up = this.pos.up();
    IBlockState blockState = this.world.getBlockState(up);
    Block block = blockState.getBlock();

    if (block != Blocks.FIRE) {

      if (block.isAir(blockState, this.world, up)
          || block.isReplaceable(this.world, up)) {
        this.world.setBlockState(up, Blocks.FIRE.getDefaultState());
      }
    }
  }

  @Override
  protected void onUpdateInvalid() {

    // reset the burn timer
    this.reset();
  }

  @Override
  protected void onInvalidDelayExpired() {

    // set blockstate to complete
    // add failure items or ash
    // clear fire block above if it exists

    ItemStack input = this.stackHandler.getStackInSlot(0);
    KilnPitRecipe recipe = KilnPitRecipe.getRecipe(input);

    if (!input.isEmpty()
        && recipe != null) {
      ItemStack[] failureItems = recipe.getFailureItems();

      if (failureItems.length > 0) {

        for (int i = 0; i < input.getCount(); i++) {
          ItemStack failureItemStack = failureItems[Util.RANDOM.nextInt(failureItems.length)].copy();
          failureItemStack.setCount(1);
          this.insertOutputItem(failureItemStack);
        }

      } else {
        this.insertOutputItem(ItemMaterial.EnumType.PIT_ASH.asStack(input.getCount()));
      }

      ItemStack output = recipe.getOutput();
      output.setCount(input.getCount());
      this.stackHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    this.setActive(false);
    IBlockState blockState = ModuleBlocks.KILN_PIT.getDefaultState()
        .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.COMPLETE);
    this.world.setBlockState(this.pos, blockState);
    this.world.setBlockToAir(this.pos.up());
  }

  @Override
  protected void onBurnStageComplete() {
    //
  }

  @Override
  protected boolean isStructureValid() {

    IBlockState selfBlockState = this.world.getBlockState(this.pos);

    if (selfBlockState.getValue(BlockKilnPit.VARIANT) != BlockKilnPit.EnumType.WOOD
        && selfBlockState.getValue(BlockKilnPit.VARIANT) != BlockKilnPit.EnumType.ACTIVE) {
      return false;
    }

    BlockPos up = this.pos.up();
    IBlockState upBlockState = this.world.getBlockState(up);
    Block upBlock = upBlockState.getBlock();

    if (!upBlock.isAir(upBlockState, this.world, up)
        && !upBlock.isReplaceable(this.world, up)
        && upBlock != Blocks.FIRE) {
      return false;
    }

    for (EnumFacing facing : EnumFacing.HORIZONTALS) {

      BlockPos offset = this.pos.offset(facing);

      if (!this.isValidStructureBlock(this.world, offset, this.world.getBlockState(offset), facing.getOpposite())) {
        return false;
      }
    }

    return this.isValidStructureBlock(
        this.world,
        this.pos.down(),
        this.world.getBlockState(this.pos.down()),
        EnumFacing.UP
    );
  }

  @Override
  protected boolean isValidStructureBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    if (this.isRefractoryBlock(blockState)) {
      return true;
    }

    return super.isValidStructureBlock(world, pos, blockState, facing);
  }

  @Override
  protected void onAllBurnStagesComplete() {

    // replace kiln block with complete variant
    // set stack handler items to recipe result

    ItemStack input = this.stackHandler.getStackInSlot(0);
    KilnPitRecipe recipe = KilnPitRecipe.getRecipe(input);

    if (recipe != null) {
      ItemStack output = recipe.getOutput();
      output.setCount(1);
      this.stackHandler.setStackInSlot(0, ItemStack.EMPTY);

      ItemStack[] failureItems = recipe.getFailureItems();
      float failureChance = recipe.getFailureChance();
      failureChance *= (1f - this.countAdjacentRefractoryBlocks() / 5f);

      for (int i = 0; i < input.getCount(); i++) {

        if (Util.RANDOM.nextFloat() < failureChance) {

          if (failureItems.length > 0) {
            ItemStack failureItemStack = failureItems[Util.RANDOM.nextInt(failureItems.length)].copy();
            failureItemStack.setCount(1);
            this.insertOutputItem(failureItemStack);

          } else {
            this.insertOutputItem(ItemMaterial.EnumType.PIT_ASH.asStack(input.getCount()));
          }

        } else {
          this.insertOutputItem(output.copy());
        }
      }
    }

    int ashCount = Util.RANDOM.nextInt(3) + 1;
    this.insertOutputItem(ItemMaterial.EnumType.PIT_ASH.asStack(ashCount));

    this.setActive(false);
    IBlockState blockState = ModuleBlocks.KILN_PIT.getDefaultState()
        .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.COMPLETE);
    this.world.setBlockState(this.pos, blockState);
    this.world.setBlockToAir(this.pos.up());
  }

  public int countAdjacentRefractoryBlocks() {

    int result = 0;

    for (EnumFacing facing : EnumFacing.HORIZONTALS) {

      BlockPos offset = this.pos.offset(facing);

      if (this.isRefractoryBlock(this.world.getBlockState(offset))) {
        result += 1;
      }
    }

    if (this.isRefractoryBlock(this.world.getBlockState(this.pos.down()))) {
      result += 1;
    }

    return result;
  }

  private boolean isRefractoryBlock(IBlockState blockState) {

    for (Predicate<IBlockState> matcher : Registries.REFRACTORY_BLOCK_LIST) {

      if (matcher.test(blockState)) {
        return true;
      }
    }

    return false;
  }

  private void insertOutputItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.outputStackHandler.insertItem(i, output, false);
    }
  }

  @Override
  protected int getTotalBurnTimeTicks() {

    return this.totalBurnTimeTicks;
  }

  @Override
  protected int getBurnStages() {

    return 1;
  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("logStackHandler", this.logStackHandler.serializeNBT());
    compound.setInteger("totalBurnTimeTicks", this.totalBurnTimeTicks);
    compound.setBoolean("active", this.active);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.logStackHandler.deserializeNBT(compound.getCompoundTag("logStackHandler"));
    this.totalBurnTimeTicks = compound.getInteger("totalBurnTimeTicks");
    this.active = compound.getBoolean("active");
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
  }
}
