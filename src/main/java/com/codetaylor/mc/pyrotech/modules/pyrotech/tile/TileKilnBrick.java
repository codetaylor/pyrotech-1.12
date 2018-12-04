package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockKilnBrick;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
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

public class TileKilnBrick
    extends TileEntity
    implements ITickable,
    IProgressProvider,
    ITileInteractable {

  private static final int DORMANT_COUNTER = 50;

  private ItemStackHandler fuelStackHandler;
  private ItemStackHandler stackHandler;
  private ItemStackHandler outputStackHandler;
  private int remainingRecipeTimeTicks;
  private int remainingBurnTimeTicks;
  private int dormantCounter;

  // transient
  private int ticksSinceLastClientSync;

  private IInteraction[] interactions;

  public TileKilnBrick() {

    this.dormantCounter = DORMANT_COUNTER;

    this.stackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return MathHelper.clamp(ModulePyrotechConfig.BRICK_KILN.INPUT_SLOT_SIZE, 1, 64);
      }

      @Override
      protected void onContentsChanged(int slot) {

        // Recalculate remaining time.

        ItemStack itemStack = this.getStackInSlot(slot);

        if (itemStack.isEmpty()) {
          TileKilnBrick.this.remainingRecipeTimeTicks = 0;

        } else {
          KilnBrickRecipe recipe = KilnBrickRecipe.getRecipe(itemStack);

          if (recipe != null) {
            TileKilnBrick.this.remainingRecipeTimeTicks = recipe.getTimeTicks();

          } else {
            TileKilnBrick.this.remainingRecipeTimeTicks = 0;
          }
        }

        TileKilnBrick.this.dormantCounter = DORMANT_COUNTER;
        TileKilnBrick.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileKilnBrick.this.world, TileKilnBrick.this.pos);
      }

      @Nonnull
      @Override
      public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

        // Filter out non-recipe items.

        KilnBrickRecipe recipe = KilnBrickRecipe.getRecipe(stack);

        if (recipe == null
            || !TileKilnBrick.this.getOutputStackHandler().getStackInSlot(0).isEmpty()) {
          return stack;
        }

        return super.insertItem(slot, stack, simulate);
      }
    };

    this.outputStackHandler = new ItemStackHandler(9) {

      @Override
      protected void onContentsChanged(int slot) {

        TileKilnBrick.this.dormantCounter = DORMANT_COUNTER;
        TileKilnBrick.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileKilnBrick.this.world, TileKilnBrick.this.pos);
      }
    };

    this.fuelStackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return MathHelper.clamp(ModulePyrotechConfig.BRICK_KILN.FUEL_SLOT_SIZE, 1, 64);
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileKilnBrick.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileKilnBrick.this.world, TileKilnBrick.this.pos);
      }

      @Nonnull
      @Override
      public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

        // Filter out non-fuel items.

        if (!StackHelper.isFuel(stack)) {
          return stack;
        }

        ItemStack itemStack = super.insertItem(slot, stack, simulate);
        BlockHelper.notifyBlockUpdate(TileKilnBrick.this.world, TileKilnBrick.this.pos);
        return itemStack;
      }
    };

    this.interactions = new IInteraction[]{
        new InteractionUseFlintAndSteel(),
        new Interaction(new ItemStackHandler[]{
            TileKilnBrick.this.stackHandler,
            TileKilnBrick.this.outputStackHandler
        }),
        new InteractionFuel(new ItemStackHandler[]{
            TileKilnBrick.this.fuelStackHandler
        })
    };
  }

  public int getRemainingBurnTimeTicks() {

    return this.remainingBurnTimeTicks;
  }

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

  public ItemStackHandler getFuelStackHandler() {

    return this.fuelStackHandler;
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public void setActive(boolean value) {

    IBlockState blockState = this.world.getBlockState(this.pos);
    boolean active = this.isActive();

    if (value && !active) {

      if (this.hasFuel()
        /*&& !this.stackHandler.getStackInSlot(0).isEmpty()*/) {
        blockState = blockState.withProperty(BlockKilnBrick.TYPE, BlockKilnBrick.EnumType.BottomLit);
        this.world.setBlockState(this.pos, blockState, 3);
      }

    } else if (!value && active) {

      blockState = blockState.withProperty(BlockKilnBrick.TYPE, BlockKilnBrick.EnumType.Bottom);
      this.world.setBlockState(this.pos, blockState, 3);
    }
  }

  public boolean isActive() {

    return (this.world.getBlockState(this.pos).getValue(BlockKilnBrick.TYPE) == BlockKilnBrick.EnumType.BottomLit);
  }

  public boolean isFiring() {

    return this.hasFuel()
        && !this.stackHandler.getStackInSlot(0).isEmpty();
  }

  private boolean hasFuel() {

    return this.remainingBurnTimeTicks > 0
        || !this.fuelStackHandler.getStackInSlot(0).isEmpty();
  }

  @Override
  public float getProgress() {

//    if (!this.isActive()) {
//      return 0;
//    }

    ItemStack itemStack = this.getStackHandler().getStackInSlot(0);

    if (itemStack.isEmpty()) {
      return 0;
    }

    KilnBrickRecipe recipe = KilnBrickRecipe.getRecipe(itemStack);

    if (recipe == null) {
      // Should never happen because we filter items on input.
      return 0;
    }

    return 1f - (this.remainingRecipeTimeTicks / (float) recipe.getTimeTicks());
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    this.markDirty();

    if (this.isFiring()) {
      this.dormantCounter = DORMANT_COUNTER;

    } else if (ModulePyrotechConfig.BRICK_KILN.KEEP_HEAT
        && this.dormantCounter > 0) {

      this.dormantCounter -= 1;
    }

    if (this.dormantCounter == 0) {
      this.setActive(false);
    }

    if (!this.isActive()) {
      return;
    }

    boolean forceUpdate = false;

    this.remainingBurnTimeTicks -= 1;

    if (this.remainingBurnTimeTicks <= 0) {

      // consume fuel and add burn time
      this.remainingBurnTimeTicks = Math.max(0, this.remainingBurnTimeTicks);
      ItemStack itemStack = this.getFuelStackHandler().extractItem(0, 1, false);

      if (!itemStack.isEmpty()) {
        this.remainingBurnTimeTicks += StackHelper.getItemBurnTime(itemStack);

      } else {
        this.setActive(false);
      }

      forceUpdate = true;
    }

    if (this.isActive()
        && this.remainingRecipeTimeTicks > 0) {

      this.remainingRecipeTimeTicks -= 1;

      if (this.remainingRecipeTimeTicks == 0) {
        this.onComplete();
      }

      forceUpdate = true;
    }

    this.ticksSinceLastClientSync += 1;

    if (this.ticksSinceLastClientSync >= 20
        || forceUpdate) {
      this.ticksSinceLastClientSync = 0;
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }

    //this.markDirty();
  }

  private void onComplete() {

    // set stack handler items to recipe result

    ItemStack input = this.stackHandler.getStackInSlot(0);
    KilnPitRecipe recipe = KilnPitRecipe.getRecipe(input);

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

    /*if (ModuleCharcoalConfig.BRICK_KILN.KEEP_HEAT) {
      this.setActive(false);
    }*/
  }

  private void insertOutputItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.outputStackHandler.insertItem(i, output, false);
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("fuelStackHandler", this.fuelStackHandler.serializeNBT());
    compound.setInteger("remainingRecipeTimeTicks", this.remainingRecipeTimeTicks);
    compound.setInteger("remainingBurnTimeTicks", this.remainingBurnTimeTicks);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));
    this.remainingRecipeTimeTicks = compound.getInteger("remainingRecipeTimeTicks");
    this.remainingBurnTimeTicks = compound.getInteger("remainingBurnTimeTicks");
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

  public void spawnAllItemsOnTop() {

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

    return blockState.getValue(BlockKilnBrick.FACING);
  }

  @Override
  public boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    BlockPos blockPos = this.getPos();

    return blockPos.getX() == pos.getX()
        && blockPos.getY() + 1 == pos.getY()
        && blockPos.getZ() == pos.getZ();
  }

  private class Interaction
      extends InteractionItemStack<TileKilnBrick> {

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

      return (KilnBrickRecipe.getRecipe(itemStack) != null);
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
      extends InteractionItemStack<TileKilnBrick> {

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
      extends InteractionUseItemBase<TileKilnBrick> {

    /* package */ InteractionUseFlintAndSteel() {

      super(new EnumFacing[]{EnumFacing.NORTH}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileKilnBrick tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL);
    }

    @Override
    protected boolean doInteraction(TileKilnBrick tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

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

}
