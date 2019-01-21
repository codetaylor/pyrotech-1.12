package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.BloomeryFuelRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemTongs;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.ITileContainer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class TileBloomery
    extends TileNetBase
    implements ITileInteractable,
    ITickable,
    ITileContainer {

  private static final AxisAlignedBB INTERACTION_BOUNDS_TOP = new AxisAlignedBB(2f / 16f, 1, 2f / 16f, 14f / 16f, 24f / 16f, 14f / 16f);

  private InputStackHandler inputStackHandler;
  private OutputStackHandler outputStackHandler;
  private FuelStackHandler fuelStackHandler;

  private TileDataItemStackHandler<InputStackHandler> tileDataInputStackHandler;

  private TileDataFloat recipeProgress;
  private TileDataFloat speed;
  private TileDataBoolean active;
  private int burnTime;
  private int lastBurnTime;
  private BloomeryRecipe currentRecipe;

  private IInteraction[] interactions;

  public TileBloomery() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.inputStackHandler = new InputStackHandler();
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.updateRecipe();
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler();
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.fuelStackHandler = new FuelStackHandler(this);
    this.fuelStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
    });

    this.recipeProgress = new TileDataFloat(0, 20);
    this.speed = new TileDataFloat(0);
    this.active = new TileDataBoolean(false);

    // --- Network ---

    this.tileDataInputStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataInputStackHandler,
        new TileDataItemStackHandler<>(this.outputStackHandler),
        new TileDataItemStackHandler<>(this.fuelStackHandler),
        this.recipeProgress,
        this.speed,
        this.active
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionTongs(
            this.getInputInteractionBoundsTop()
        ),
        new InteractionUseFlintAndSteel(
            this.getInputInteractionBoundsTop()
        ),
        new InteractionInput(
            new ItemStackHandler[]{
                this.inputStackHandler,
                this.outputStackHandler
            },
            this.getInputInteractionBoundsTop(),
            () -> (this.fuelStackHandler.getTotalItemCount() == 0 && !this.isActive())
        ),
        new InteractionFuel(
            this,
            this.fuelStackHandler,
            this.getInputInteractionBoundsTop(),
            () -> (!this.inputStackHandler.getStackInSlot(0).isEmpty())
        )
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void setActive() {

    if (!this.active.get()
        && !this.inputStackHandler.getStackInSlot(0).isEmpty()
        && this.burnTime > 0) {
      this.active.set(true);
      this.fuelStackHandler.clearStacks();
    }
  }

  public boolean isActive() {

    return this.active.get();
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public FuelStackHandler getFuelStackHandler() {

    return this.fuelStackHandler;
  }

  public BloomeryRecipe getCurrentRecipe() {

    return this.currentRecipe;
  }

  public float getSpeed() {

    return this.speed.get();
  }

  protected int getMaxBurnTime() {

    // TODO: Config
    // 32,000 is the coal coke block burn time.
    return 32000 * 4;
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  private void updateRecipe() {

    this.currentRecipe = BloomeryRecipe.getRecipe(this.inputStackHandler.getStackInSlot(0));
  }

  private void updateSpeed() {

    float linearSpeed = this.burnTime / (float) this.getMaxBurnTime();
    float speed = 2 * (float) Math.pow(linearSpeed, 0.5);
    this.speed.set(speed);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.lastBurnTime != this.burnTime) {
      this.lastBurnTime = this.burnTime;
      this.updateSpeed();
    }

    if (this.isActive()) {

      if (this.currentRecipe == null) {
        this.burnTime = 0;
        this.recipeProgress.set(0);
        this.active.set(false);
        return;
      }

      float increment = (1.0f / this.currentRecipe.getTimeTicks()) * this.speed.get();

      if (this.recipeProgress.add(increment) >= 0.9999) {
        this.burnTime = 0;
        this.recipeProgress.set(0);
        this.active.set(false);

        // Create the bloom itemstack with nbt
        ItemStack output = this.currentRecipe.getUniqueBloomFromOutput();

        // Swap the items
        this.inputStackHandler.extractItem(0, 1, false);
        this.outputStackHandler.insertItem(0, output, false);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataInputStackHandler.isDirty()) {
      this.updateRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("fuelStackHandler", this.fuelStackHandler.serializeNBT());
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    compound.setBoolean("active", this.active.get());
    compound.setFloat("speed", this.speed.get());
    compound.setInteger("burnTime", this.burnTime);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
    this.active.set(compound.getBoolean("active"));
    this.speed.set(compound.getFloat("speed"));
    this.burnTime = compound.getInteger("burnTime");
    this.updateRecipe();
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  @Override
  public void dropContents() {

    StackHelper.spawnStackHandlerSlotContentsOnTop(this.world, this.inputStackHandler, 0, this.pos);
    StackHelper.spawnStackHandlerSlotContentsOnTop(this.world, this.outputStackHandler, 0, this.pos);
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.fuelStackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    BlockPos blockPos = this.getPos();

    return blockPos.getX() == pos.getX()
        && blockPos.getY() + 1 == pos.getY()
        && blockPos.getZ() == pos.getZ();
  }

  private class InteractionTongs
      extends InteractionUseItemBase<TileBloomery> {

    public InteractionTongs(AxisAlignedBB bounds) {

      super(new EnumFacing[]{EnumFacing.UP}, bounds);
    }

    @Override
    protected boolean allowInteraction(TileBloomery tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      return (heldItem.getItem() == ModuleItems.TONGS)
          && !tile.outputStackHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    protected boolean doInteraction(TileBloomery tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack bloomStack = tile.outputStackHandler.extractItem(0, 1, false);
      ItemStack heldItem = player.getHeldItemMainhand();
      ItemStack tongs = ItemTongs.getFilledItemStack(heldItem, bloomStack);

      heldItem.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(player, tongs, player.inventory.currentItem);

      return true;
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

      // This is a no-op because we only want the tongs to take damage
      // when a bloom is placed, not retrieved.
    }
  }

  private class InteractionUseFlintAndSteel
      extends InteractionUseItemBase<TileBloomery> {

    /* package */ InteractionUseFlintAndSteel(AxisAlignedBB interactionBounds) {

      super(new EnumFacing[]{EnumFacing.UP}, interactionBounds);
    }

    @Override
    protected boolean allowInteraction(TileBloomery tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL)
          && (!tile.isActive());
    }

    @Override
    protected boolean doInteraction(TileBloomery tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {
        tile.setActive();

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

  private class InteractionInput
      extends InteractionItemStack<TileBloomery> {

    private final BooleanSupplier isEnabled;

    public InteractionInput(ItemStackHandler[] stackHandlers, AxisAlignedBB interactionBounds, BooleanSupplier isEnabled) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.UP},
          interactionBounds,
          new Transform(
              Transform.translate(0.5, 5.0 / 16.0, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
      this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {

      return this.isEnabled.getAsBoolean();
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (BloomeryRecipe.getRecipe(itemStack) != null);
    }

    @Override
    public boolean interact(EnumType type, TileBloomery tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return super.interact(type, tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
    }
  }

  public class InteractionFuel
      extends InteractionItemStack<TileBloomery> {

    private final TileBloomery tile;
    private final FuelStackHandler fuelStackHandler;
    private final BooleanSupplier isEnabled;

    public InteractionFuel(TileBloomery tile, FuelStackHandler fuelStackHandler, AxisAlignedBB interactionBounds, BooleanSupplier isEnabled) {

      super(
          new ItemStackHandler[]{fuelStackHandler},
          0,
          new EnumFacing[]{EnumFacing.UP},
          interactionBounds,
          new Transform(
              Transform.translate(0.5, 5.0 / 16.0, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
      this.tile = tile;
      this.fuelStackHandler = fuelStackHandler;
      this.isEnabled = isEnabled;
    }

    public FuelStackHandler getFuelStackHandler() {

      return this.fuelStackHandler;
    }

    public TileBloomery getTile() {

      return this.tile;
    }

    @Override
    public boolean isEnabled() {

      return this.isEnabled.getAsBoolean();
    }

    @Override
    public ItemStack getStackInSlot() {

      return super.getStackInSlot();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      BloomeryFuelRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      //
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return false;
    }
  }

  private AxisAlignedBB getInputInteractionBoundsTop() {

    return INTERACTION_BOUNDS_TOP;
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (BloomeryRecipe.getRecipe(stack) == null) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  public class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }

  public class FuelStackHandler
      extends DynamicStackHandler {

    private final TileBloomery tile;

    /* package */ FuelStackHandler(TileBloomery tile) {

      super(1);
      this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Only allow solid fuel items if the input stack handler is not empty.

      if (this.tile.inputStackHandler.getStackInSlot(0).isEmpty()
          || stack.getItem().hasContainerItem(stack)) {
        return stack;
      }

      int fuelBurnTimeSingle = StackHelper.getItemBurnTime(stack);

      if (fuelBurnTimeSingle <= 0) {
        return stack;
      }

      int max = this.tile.getMaxBurnTime();
      int fuelBurnTimeTotal = fuelBurnTimeSingle * stack.getCount();

      if (this.tile.burnTime >= max) {
        return stack; // There's no room for insert, fail

      } else {

        if (this.tile.burnTime + fuelBurnTimeTotal <= max) {
          // There's enough room for all items in the stack. If not a simulation
          // Increase the burn time. Only do the insertion if the machine is
          // not active.

          if (!simulate) {
            this.tile.burnTime += fuelBurnTimeTotal;

            if (!this.tile.isActive()) {
              this.insertItem(stack, false);
            }
          }

          return ItemStack.EMPTY;

        } else {
          // Trim the input stack down to size and, if this isn't a simulation,
          // increase the burn time. Only do the insertion if the machine is
          // not active.

          ItemStack toInsert = stack.copy();
          int insertCount = Math.min(toInsert.getCount(), (int) ((max - this.tile.burnTime) / (float) fuelBurnTimeSingle));
          toInsert.setCount(insertCount);

          if (!simulate) {
            this.tile.burnTime += insertCount * fuelBurnTimeSingle;

            if (!this.tile.isActive()) {
              this.insertItem(toInsert, false);
            }
          }

          ItemStack toReturn = stack.copy();
          toReturn.setCount(toReturn.getCount() - insertCount);
          return toReturn;
        }
      }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      // Find the first non-empty slot with the highest index and extract from
      // that slot. If this isn't a simulation and the machine isn't active,
      // reduce the burn time.

      // If the machine is active, we don't want to reduce the burn time because
      // this method is called to remove any fuel that is allowed to be inserted
      // when the machine is active.

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          ItemStack extractItem = super.extractItem(i, amount, true);

          if (!simulate) {

            if (!this.tile.isActive()) {
              this.tile.burnTime -= StackHelper.getItemBurnTime(extractItem) * extractItem.getCount();
            }
            super.extractItem(i, amount, false);
          }

          return extractItem;
        }
      }

      return ItemStack.EMPTY;
    }
  }

}
