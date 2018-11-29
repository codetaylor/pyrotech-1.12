package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockKilnStone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.athenaeum.network.tile.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnStoneRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileKilnStone
    extends TileNetworkedBase
    implements ITickable,
    IProgressProvider,
    ITileInteractable {

  private static final int DORMANT_COUNTER = 50;

  private FuelStackHandler fuelStackHandler;
  private StackHandler stackHandler;
  private OutputStackHandler outputStackHandler;

  private TileDataInteger remainingRecipeTimeTicks;
  private TileDataInteger remainingBurnTimeTicks;

  private int dormantCounter;

  private IInteraction[] interactions;

  public TileKilnStone() {

    // --- Init ---

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.dormantCounter = DORMANT_COUNTER;

    // --- Stack Handlers ---

    this.stackHandler = new StackHandler(1);
    this.stackHandler.addObserver((handler, slot) -> {
      this.recalculateRemainingTime(handler.getStackInSlot(slot));
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler(9);
    this.outputStackHandler.addObserver((handler, slot) -> {
      this.dormantCounter = DORMANT_COUNTER;
      this.markDirty();
    });

    this.fuelStackHandler = new FuelStackHandler(1);
    this.fuelStackHandler.addObserver((handler, slot) -> {
      // force the burn time to update with the fuel change
      this.remainingBurnTimeTicks.forceUpdate();
      this.markDirty();
    });

    // --- Network ---

    this.remainingRecipeTimeTicks = new TileDataInteger(0, 20);
    this.remainingBurnTimeTicks = new TileDataInteger(0, 20);

    this.registerTileData(new ITileData[]{
        new TileDataItemStackHandler<>(this.stackHandler),
        new TileDataItemStackHandler<>(this.outputStackHandler),
        new TileDataItemStackHandler<>(this.fuelStackHandler),
        this.remainingRecipeTimeTicks,
        this.remainingBurnTimeTicks
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionUseFlintAndSteel(),
        new Interaction(new ItemStackHandler[]{
            TileKilnStone.this.stackHandler,
            TileKilnStone.this.outputStackHandler
        }),
        new InteractionFuel(new ItemStackHandler[]{
            TileKilnStone.this.fuelStackHandler
        })
    };
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate(List<ITileData> data) {

    super.onTileDataUpdate(data);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getFuelStackHandler() {

    return this.fuelStackHandler;
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public int getRemainingBurnTimeTicks() {

    return this.remainingBurnTimeTicks.getValue();
  }

  public void setRemainingBurnTimeTicks(int value) {

    this.remainingBurnTimeTicks.setValue(value);

    if (this.remainingBurnTimeTicks.isDirty()) {
      this.markDirty();
    }
  }

  public int getRemainingRecipeTimeTicks() {

    return this.remainingRecipeTimeTicks.getValue();
  }

  public void setRemainingRecipeTimeTicks(int value) {

    this.remainingRecipeTimeTicks.setValue(value);

    if (this.remainingRecipeTimeTicks.isDirty()) {
      this.markDirty();
    }
  }

  public void setActive(boolean value) {

    IBlockState blockState = this.world.getBlockState(this.pos);
    boolean active = this.isActive();

    if (value && !active) {

      if (this.hasFuel()
        /*&& !this.stackHandler.getStackInSlot(0).isEmpty()*/) {
        blockState = blockState.withProperty(BlockKilnStone.TYPE, BlockKilnStone.EnumType.BottomLit);
        this.world.setBlockState(this.pos, blockState, 3);
//        BlockHelper.notifyBlockUpdate(this.world, this.pos);
      }

    } else if (!value && active) {

      blockState = blockState.withProperty(BlockKilnStone.TYPE, BlockKilnStone.EnumType.Bottom);
      this.world.setBlockState(this.pos, blockState, 3);
//      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }
  }

  public boolean isActive() {

    return (this.world.getBlockState(this.pos).getValue(BlockKilnStone.TYPE) == BlockKilnStone.EnumType.BottomLit);
  }

  public boolean isFiring() {

    return this.hasFuel()
        && !this.stackHandler.getStackInSlot(0).isEmpty();
  }

  private boolean hasFuel() {

    return this.getRemainingBurnTimeTicks() > 0
        || !this.fuelStackHandler.getStackInSlot(0).isEmpty();
  }

  @Override
  public float getProgress() {

    ItemStack itemStack = this.getStackHandler().getStackInSlot(0);

    if (itemStack.isEmpty()) {
      return 0;
    }

    KilnStoneRecipe recipe = KilnStoneRecipe.getRecipe(itemStack);

    if (recipe == null) {
      // Should never happen because we filter items on input.
      return 0;
    }

    return 1f - (this.getRemainingRecipeTimeTicks() / (float) recipe.getTimeTicks());
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      if (facing == EnumFacing.UP) {
        //noinspection unchecked
        return (T) this.stackHandler;

      } else if (facing == EnumFacing.DOWN) {
        //noinspection unchecked
        return (T) this.outputStackHandler;

      } else {
        //noinspection unchecked
        return (T) this.fuelStackHandler;
      }
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.isFiring()) {
      this.dormantCounter = DORMANT_COUNTER;

    } else if (ModulePyrotechConfig.STONE_KILN.KEEP_HEAT
        && this.dormantCounter > 0) {

      this.dormantCounter -= 1;
    }

    if (this.dormantCounter == 0) {
      this.setActive(false);
    }

    if (!this.isActive()) {
      return;
    }

    this.setRemainingBurnTimeTicks(this.getRemainingBurnTimeTicks() - 1);

    if (this.getRemainingBurnTimeTicks() <= 0) {

      // consume fuel and add burn time
      this.setRemainingBurnTimeTicks(Math.max(0, this.getRemainingBurnTimeTicks()));
      ItemStack itemStack = this.getFuelStackHandler().extractItem(0, 1, false);

      if (!itemStack.isEmpty()) {
        this.setRemainingBurnTimeTicks(this.getRemainingBurnTimeTicks() + StackHelper.getItemBurnTime(itemStack));

      } else {
        this.setActive(false);
      }
    }

    if (this.isActive()
        && this.getRemainingRecipeTimeTicks() > 0) {

      this.setRemainingRecipeTimeTicks(this.getRemainingRecipeTimeTicks() - 1);

      if (this.getRemainingRecipeTimeTicks() == 0) {
        this.onComplete();
      }
    }
  }

  private void onComplete() {

    // set stack handler items to recipe result

    ItemStack input = this.stackHandler.getStackInSlot(0);
    KilnStoneRecipe recipe = KilnStoneRecipe.getRecipe(input);

    if (recipe != null) {
      ItemStack output = recipe.getOutput();
      output.setCount(1);
      this.stackHandler.setStackInSlot(0, ItemStack.EMPTY);

      ItemStack[] failureItems = recipe.getFailureItems();
      float failureChance = recipe.getFailureChance();

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
  }

  private void insertOutputItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.outputStackHandler.insertItem(i, output, false);
    }
  }

  private void recalculateRemainingTime(ItemStack itemStack) {
    // Recalculate remaining recipe time.

    if (itemStack.isEmpty()) {
      this.setRemainingRecipeTimeTicks(0);

    } else {
      KilnStoneRecipe recipe = KilnStoneRecipe.getRecipe(itemStack);

      if (recipe != null) {
        this.setRemainingRecipeTimeTicks(recipe.getTimeTicks());

      } else {
        this.setRemainingRecipeTimeTicks(0);
      }
    }

    this.dormantCounter = DORMANT_COUNTER;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("fuelStackHandler", this.fuelStackHandler.serializeNBT());
    compound.setInteger("remainingRecipeTimeTicks", this.remainingRecipeTimeTicks.getValue());
    compound.setInteger("remainingBurnTimeTicks", this.remainingBurnTimeTicks.getValue());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));
    this.remainingRecipeTimeTicks.setValue(compound.getInteger("remainingRecipeTimeTicks"));
    this.remainingBurnTimeTicks.setValue(compound.getInteger("remainingBurnTimeTicks"));
  }

  public void dropContents() {

    ItemStackHandler stackHandler = this.getStackHandler();
    ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    stackHandler = this.getOutputStackHandler();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      itemStack = stackHandler.extractItem(i, stackHandler.getStackInSlot(i).getCount(), false);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
    }

    stackHandler = this.getFuelStackHandler();
    itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    BlockHelper.notifyBlockUpdate(this.world, this.pos);
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

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(Properties.FACING_HORIZONTAL);
  }

  @Override
  public boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    BlockPos blockPos = this.getPos();

    return blockPos.getX() == pos.getX()
        && blockPos.getY() + 1 == pos.getY()
        && blockPos.getZ() == pos.getZ();
  }

  private class Interaction
      extends InteractionItemStack<TileKilnStone> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.NORTH},
          new AxisAlignedBB(1f / 16f, 1, 1f / 16f, 15f / 16f, 24f / 16f, 15f / 16f),
          new Transform(
              Transform.translate(0.5, 1.2, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (KilnStoneRecipe.getRecipe(itemStack) != null);
    }

    @Override
    protected boolean doExtract(World world, EntityPlayer player, BlockPos tilePos) {

      // Extract all slots in the output stack handler.

      ItemStackHandler outputStackHandler = this.stackHandlers[1];

      int slots = outputStackHandler.getSlots();

      for (int i = 1; i < slots; i++) {
        ItemStack extractItem = outputStackHandler.extractItem(i, outputStackHandler.getStackInSlot(i).getCount(), world.isRemote);

        if (!extractItem.isEmpty()
            && !world.isRemote) {
          StackHelper.addToInventoryOrSpawn(world, player, extractItem, tilePos);
        }
      }

      return super.doExtract(world, player, tilePos);
    }
  }

  private class InteractionFuel
      extends InteractionItemStack<TileKilnStone> {

    /* package */ InteractionFuel(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.NORTH},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 0.2, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return StackHelper.isFuel(itemStack);
    }
  }

  private class InteractionUseFlintAndSteel
      extends InteractionUseItemBase<TileKilnStone> {

    /* package */ InteractionUseFlintAndSteel() {

      super(new EnumFacing[]{EnumFacing.NORTH}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileKilnStone tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL);
    }

    @Override
    protected boolean doInteraction(TileKilnStone tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {
        tile.setActive(true);

        world.playSound(
            null,
            hitPos,
            SoundEvents.ITEM_FLINTANDSTEEL_USE,
            SoundCategory.BLOCKS,
            1.0F,
            Util.RANDOM.nextFloat() * 0.4F + 0.8F
        );
      }

      return true;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class StackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ StackHandler(int size) {

      super(size);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return MathHelper.clamp(ModulePyrotechConfig.STONE_KILN.INPUT_SLOT_SIZE, 1, 64);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-recipe items.

      KilnStoneRecipe recipe = KilnStoneRecipe.getRecipe(stack);

      if (recipe == null
          || !TileKilnStone.this.getOutputStackHandler().getStackInSlot(0).isEmpty()) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  private class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler(int size) {

      super(size);
    }
  }

  private class FuelStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ FuelStackHandler(int size) {

      super(size);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return MathHelper.clamp(ModulePyrotechConfig.STONE_KILN.FUEL_SLOT_SIZE, 1, 64);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-fuel items.

      if (!StackHelper.isFuel(stack)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

  }
}
