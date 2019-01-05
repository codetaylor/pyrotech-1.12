package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.CompactingBinOutputInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TileCompactingBin
    extends TileNetBase
    implements ITileInteractable {

  private InputStackHandler inputStackHandler;
  private StoredInputStackHandler storedInputStackHandler;
  private OutputStackHandler outputStackHandler;
  private TileDataFloat recipeProgress;

  private IInteraction[] interactions;

  public TileCompactingBin() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.inputStackHandler = new InputStackHandler(this);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.markDirty();
    });

    this.storedInputStackHandler = new StoredInputStackHandler();
    this.storedInputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.outputStackHandler = new OutputStackHandler();
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.outputStackHandler),
        new TileDataItemStackHandler<>(this.storedInputStackHandler),
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new Interaction(new ItemStackHandler[]{this.inputStackHandler}),
        new InteractionShovel(),
        new InteractionOutput(this, this.outputStackHandler)
    };
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

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  private void setRecipeProgress(float recipeProgress) {

    this.recipeProgress.set(recipeProgress);
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public ItemStackHandler getStoredInputStackHandler() {

    return this.storedInputStackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("storedInputStackHandler", this.storedInputStackHandler.serializeNBT());
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.storedInputStackHandler.deserializeNBT(compound.getCompoundTag("storedInputStackHandler"));
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class Interaction
      extends InteractionItemStack<TileCompactingBin> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 1.0, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (CompactingBinRecipe.getRecipe(itemStack) != null);
    }

    @Override
    protected void onInsert(ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(itemStack, world, player, pos);

      if (!world.isRemote) {
        world.playSound(
            null,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundCategory.BLOCKS,
            0.5f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );
      }
    }
  }

  private class InteractionShovel
      extends InteractionUseItemBase<TileCompactingBin> {

    /* package */ InteractionShovel() {

      super(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileCompactingBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (tile.getOutputStackHandler().getStackInSlot(0).isEmpty()) {
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();
      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      String registryName = resourceLocation.toString();

      if (heldItem.getToolClasses(heldItemStack).contains("shovel")) {
        return !ArrayHelper.contains(ModulePyrotechConfig.COMPACTING_BIN.SHOVEL_BLACKLIST, registryName);

      } else {
        return ArrayHelper.contains(ModulePyrotechConfig.COMPACTING_BIN.SHOVEL_WHITELIST, registryName);
      }
    }

    @Override
    protected boolean doInteraction(TileCompactingBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (!world.isRemote) {
        ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
        ItemStack itemStack = outputStackHandler.extractItem(0, 1, false);
        StackHelper.spawnStackOnTop(world, itemStack, hitPos, 1.0);
        heldItem.damageItem(1, player);
        world.playSound(null, hitPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
      }

      return true;
    }
  }

  public class InteractionOutput
      extends InteractionItemStack<TileCompactingBin> {

    private final TileCompactingBin tile;

    public InteractionOutput(
        TileCompactingBin tile,
        ItemStackHandler stackHandler
    ) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          new EnumFacing[0],
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 0.5, 0.5),
              Transform.rotate(),
              Transform.scale(12.0 / 16.0, 1.0 / 16.0, 12.0 / 16.0)
          )
      );
      this.tile = tile;
    }

    public TileCompactingBin getTile() {

      return this.tile;
    }

    @Override
    public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, float partialTicks) {

      Transform transform = super.getTransform(world, pos, blockState, itemStack, partialTicks);
      float maxOutputStacks = ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY;
      float count = this.tile.getOutputStackHandler().getStackInSlot(0).getCount();
      float totalProgress = (count + this.tile.getRecipeProgress()) / maxOutputStacks;

      return new Transform(
          Transform.translate(0.5, totalProgress * (14.0 / 16.0) + (1.0 / 16.0), 0.5),
          transform.rotation,
          transform.scale
      );
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CompactingBinOutputInteractionRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final TileCompactingBin tile;

    /* package */ InputStackHandler(TileCompactingBin tile) {

      super(1);
      this.tile = tile;
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // The output stack handler has two slots:
      // 0 - actual output
      // 1 - buffered, next output

      CompactingBinRecipe recipe = CompactingBinRecipe.getRecipe(stack);

      if (recipe == null) {
        return stack; // item has no recipe, fail
      }

      ItemStack output = recipe.getOutput();
      ItemStackHandler outputStackHandler = this.tile.getOutputStackHandler();
      int inserted = 0;

      for (int i = 0; i < stack.getCount(); i++) {

        if ((!outputStackHandler.getStackInSlot(0).isEmpty() && !outputStackHandler.insertItem(0, output, true).isEmpty())
            || (!outputStackHandler.getStackInSlot(1).isEmpty() && !outputStackHandler.insertItem(1, output, true).isEmpty())) {
          break; // recipe result cannot be stacked in output, fail
        }

        if (!simulate) {
          this.tile.recipeProgress.add(recipe.getAmount() / 100f);
          inserted += 1;

          ItemStack toStore = stack.copy();
          toStore.setCount(1);
          this.tile.storedInputStackHandler.insertItem(toStore, false);

          if (outputStackHandler.getStackInSlot(1).isEmpty()) {
            outputStackHandler.insertItem(1, output.copy(), false);
          }

          if (this.tile.recipeProgress.get() >= 0.9999
              && !outputStackHandler.getStackInSlot(1).isEmpty()) {
            this.tile.recipeProgress.set(0);
            this.tile.storedInputStackHandler.clearStacks();
            outputStackHandler.extractItem(1, outputStackHandler.getSlotLimit(1), false);
            outputStackHandler.insertItem(0, output.copy(), false);
          }
        }
      }

      if (inserted > 0) {
        return StackHelper.decrease(stack, inserted, false);
      }

      return stack;
    }
  }

  private class OutputStackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(2);
    }

    @Override
    public int getSlotLimit(int slot) {

      return ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY;
    }
  }

  private class StoredInputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public StoredInputStackHandler() {

      super(1);
      this.stacks = new ItemStackList(1);
    }

    @Override
    public void setSize(int size) {

      this.stacks = new ItemStackList(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      while (this.getSlots() - 1 < slot) {
        this.stacks.add(ItemStack.EMPTY);
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      if (slot > this.getSlots() - 1) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

      while (this.getSlots() - 1 < slot) {
        this.stacks.add(ItemStack.EMPTY);
      }

      super.setStackInSlot(slot, stack);
    }

    public void insertItem(ItemStack itemStack, boolean simulate) {

      ItemStack remaining = itemStack;
      int i = 0;

      while (!remaining.isEmpty()) {
        remaining = this.insertItem(i, remaining, simulate);
        i += 1;

        if (remaining.isEmpty()) {
          break;
        }
      }
    }

    public void clearStacks() {

      for (int i = 0; i < this.getSlots(); i++) {
        this.extractItem(i, this.getSlotLimit(i), false);
      }
    }

    private class ItemStackList
        extends NonNullList<ItemStack> {

      public ItemStackList(int size) {

        super(new ArrayList<>(size), ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
          this.add(ItemStack.EMPTY);
        }
      }
    }
  }

}
