package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileCrafting
    extends TileNetBase
    implements ITileInteractable {

  private InventoryWrapper inventoryWrapper;
  private TileDataItemStackHandler<InputStackHandler> inputTileDataItemStackHandler;
  private InputStackHandler inputStackHandler;
  private ShelfStackHandler shelfStackHandler;

  private TileDataFloat recipeProgress;

  private IInteraction[] interactions;

  // Client only, used for waila and such
  private IRecipe recipe;

  public TileCrafting() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.inventoryWrapper = new InventoryWrapper(this);

    this.inputStackHandler = new InputStackHandler();
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.updateRecipe();
      this.markDirty();
    });

    this.shelfStackHandler = new ShelfStackHandler();
    this.shelfStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.inputTileDataItemStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.inputTileDataItemStackHandler,
        new TileDataItemStackHandler<>(this.shelfStackHandler),
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[12];

    List<IInteraction> interactionList = new ArrayList<>();

    interactionList.add(new InteractionHammer(this.inventoryWrapper));

    for (int i = 0; i < 9; i++) {
      int x = i % 3;
      int z = i / 3;
      interactionList.add(new InputInteraction(this.inputStackHandler, i, x, z));
    }

    for (int i = 0; i < 3; i++) {
      int x = i % 3;
      interactionList.add(new ShelfInteraction(this.shelfStackHandler, i, x));
    }

    this.interactions = interactionList.toArray(new IInteraction[0]);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public IRecipe getRecipe() {

    return this.recipe;
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.inputStackHandler, this.pos);
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.shelfStackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.inputTileDataItemStackHandler.isDirty()) {
      this.updateRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  private void updateRecipe() {

    this.recipe = CraftingManager.findMatchingRecipe(this.inventoryWrapper, world);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.shelfStackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("shelfStackHandler", this.shelfStackHandler.serializeNBT());

    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(Properties.FACING_HORIZONTAL);
  }

  private class InteractionHammer
      extends InteractionUseItemBase<TileCrafting> {

    private InventoryWrapper wrapper;

    /* package */ InteractionHammer(InventoryWrapper inventoryWrapper) {

      super(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK);
      this.wrapper = inventoryWrapper;
    }

    @Override
    protected boolean allowInteraction(TileCrafting tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item item = heldItemStack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      if (registryName == null) {
        return false;
      }

      return ArrayHelper.contains(ModulePyrotechConfig.CRAFTING.HAMMER_LIST, registryName.toString());
    }

    @Override
    protected boolean doInteraction(TileCrafting tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      IRecipe recipe = tile.getRecipe();

      if (tile.getRecipe() == null) {
        tile.updateRecipe();
        recipe = tile.getRecipe();
      }

      if (!world.isRemote) {
        heldItem.damageItem(1, player);
        world.playSound(null, hitPos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1, 1);

        if (recipe != null) {
          tile.recipeProgress.add(1f / ModulePyrotechConfig.CRAFTING.HITS_PER_CRAFT);

          if (tile.recipeProgress.get() >= 0.9999) {
            tile.recipeProgress.set(0);

            NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(this.wrapper);

            for (int slot = 0; slot < 9; slot++) {
              ItemStack remainingItemStack = remainingItems.get(slot);
              ItemStack stackInSlot = tile.inputStackHandler.getStackInSlot(slot);

              if (!remainingItemStack.isEmpty()) {

                if (remainingItemStack.getItem() != stackInSlot.getItem()) {
                  StackHelper.spawnStackOnTop(world, remainingItemStack, tile.getPos(), 0.75);
                  StackHelper.decreaseStackInSlot(tile.inputStackHandler, slot, 1, true);

                } else {
                  tile.inputStackHandler.setStackInSlot(slot, remainingItemStack);
                }

              } else {
                StackHelper.decreaseStackInSlot(tile.inputStackHandler, slot, 1, true);
              }
            }

            ItemStack result = recipe.getRecipeOutput().copy();
            StackHelper.spawnStackOnTop(world, result, tile.getPos(), 0.75);
          }
        }
      }

      return true;
    }
  }

  private class InputInteraction
      extends InteractionItemStack<TileCrafting> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;
    private final Vec3d textOffset = new Vec3d(0, 0.25, 0);

    /* package */ InputInteraction(ItemStackHandler stackHandler, int slot, double x, double z) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * ONE_THIRD, 14f / 16f, z * ONE_THIRD, x * ONE_THIRD + ONE_THIRD, 15f / 16f, z * ONE_THIRD + ONE_THIRD),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 15f / 16f, z * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025),
              Transform.rotate(new Quaternion[]{
                  Transform.rotate(0, 1, 0, 180),
                  Transform.rotate(1, 0, 0, -90)
              }),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }

    @Override
    public Vec3d getTextOffset() {

      return this.textOffset;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      Item item = itemStack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      if (registryName == null) {
        return false;
      }

      return !ArrayHelper.contains(ModulePyrotechConfig.CRAFTING.HAMMER_LIST, registryName.toString());
    }

    @Override
    protected int getInsertItemCount(EnumType type, ItemStack itemStack) {

      return 1;
    }
  }

  private class ShelfInteraction
      extends InteractionItemStack<TileCrafting> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    /* package */ ShelfInteraction(ItemStackHandler stackHandler, int slot, double x) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * ONE_THIRD, 0, 0, x * ONE_THIRD + ONE_THIRD, 5f / 16f, ONE_THIRD),
          new Transform(
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 5f / 16f, ONE_SIXTH + 0.025),
              Transform.rotate(new Quaternion[]{
                  Transform.rotate(0, 1, 0, 180),
                  Transform.rotate(1, 0, 0, -90)
              }),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InputStackHandler() {

      super(9);
    }
  }

  private class ShelfStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ ShelfStackHandler() {

      super(3);
    }
  }

  // ---------------------------------------------------------------------------
  // - Inventory
  // ---------------------------------------------------------------------------

  private class InventoryWrapper
      extends InventoryCrafting {

    private final TileCrafting tile;

    /* package */ InventoryWrapper(TileCrafting tile) {

      super(new Container() {

        @Override
        public boolean canInteractWith(@Nonnull EntityPlayer player) {

          return true;
        }
      }, 3, 3);

      this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {

      if (index >= this.getSizeInventory()) {
        return ItemStack.EMPTY;
      }

      return this.tile.inputStackHandler.getStackInSlot(index);
    }

    @Nonnull
    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {

      column = 2 - column;

      if (row >= 0 && row < 3 && column >= 0 && column < 3) {
        int index = row + column * 3;
        return this.tile.inputStackHandler.getStackInSlot(index);
      }

      return ItemStack.EMPTY;
    }

    @Override
    public void markDirty() {

      this.tile.markDirty();
    }
  }

}
