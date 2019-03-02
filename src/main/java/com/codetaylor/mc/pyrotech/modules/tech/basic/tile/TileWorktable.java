package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileWorktable
    extends TileNetBase
    implements ITileInteractable {

  private InventoryWrapper inventoryWrapper;
  private TileDataItemStackHandler<InputStackHandler> inputTileDataItemStackHandler;
  private InputStackHandler inputStackHandler;
  private ShelfStackHandler shelfStackHandler;
  private TileDataInteger remainingDurability;

  private TileDataFloat recipeProgress;

  private IInteraction[] interactions;

  private IRecipe recipe;

  public TileWorktable() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.inventoryWrapper = new InventoryWrapper(this);

    this.inputStackHandler = new InputStackHandler(this.getGridMaxStackSize());
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.updateRecipe();
      this.markDirty();
    });

    this.shelfStackHandler = new ShelfStackHandler(this.getShelfMaxStackSize());
    this.shelfStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.recipeProgress = new TileDataFloat(0);

    this.remainingDurability = new TileDataInteger(this.getDurability());

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
      int x = 2 - (i % 3);
      int z = 2 - (i / 3);
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

  protected int getGridMaxStackSize() {

    return ModuleTechBasicConfig.WORKTABLE.GRID_MAX_STACK_SIZE;
  }

  protected int getShelfMaxStackSize() {

    return ModuleTechBasicConfig.WORKTABLE.SHELF_MAX_STACK_SIZE;
  }

  protected int getToolDamagePerCraft() {

    return ModuleTechBasicConfig.WORKTABLE.TOOL_DAMAGE_PER_CRAFT;
  }

  protected boolean usesDurability() {

    return ModuleTechBasicConfig.WORKTABLE.USES_DURABILITY;
  }

  protected int getDurability() {

    return ModuleTechBasicConfig.WORKTABLE.DURABILITY;
  }

  protected int getHitsPerCraft() {

    return ModuleTechBasicConfig.WORKTABLE.HITS_PER_CRAFT;
  }

  protected int getMinimumHungerToUse() {

    return ModuleTechBasicConfig.WORKTABLE.MINIMUM_HUNGER_TO_USE;
  }

  protected double getExhaustionCostPerHit() {

    return ModuleTechBasicConfig.WORKTABLE.EXHAUSTION_COST_PER_HIT;
  }

  protected double getExhaustionCostPerCraftComplete() {

    return ModuleTechBasicConfig.WORKTABLE.EXHAUSTION_COST_PER_CRAFT_COMPLETE;
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

    WorktableRecipe recipe = WorktableRecipe.getRecipe(this.inventoryWrapper, this.world);

    if (recipe == null) {
      this.recipe = null;

    } else {
      this.recipe = recipe.getRecipe();
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.shelfStackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
    this.remainingDurability.set(compound.getInteger("remainingDurability"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("shelfStackHandler", this.shelfStackHandler.serializeNBT());
    compound.setInteger("remainingDurability", this.remainingDurability.get());

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

    if (blockState.getBlock() == ModuleTechBasic.Blocks.WORKTABLE
        || blockState.getBlock() == ModuleTechBasic.Blocks.WORKTABLE_STONE) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  private class InteractionHammer
      extends InteractionUseItemBase<TileWorktable> {

    private InventoryWrapper wrapper;

    /* package */ InteractionHammer(InventoryWrapper inventoryWrapper) {

      super(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK);
      this.wrapper = inventoryWrapper;
    }

    @Override
    protected boolean allowInteraction(TileWorktable tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (player.getFoodStats().getFoodLevel() < tile.getMinimumHungerToUse()) {
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item item = heldItemStack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      if (registryName == null) {
        return false;
      }

      return ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(registryName) > -1;
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

      // We apply our own item damage on recipe completion.
    }

    @Override
    protected boolean doInteraction(TileWorktable tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      IRecipe recipe = tile.getRecipe();

      if (tile.getRecipe() == null) {
        tile.updateRecipe();
        recipe = tile.getRecipe();
      }

      if (!world.isRemote) {
        world.playSound(null, hitPos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1, 1);

        if (recipe != null) {
          tile.recipeProgress.add(1f / tile.getHitsPerCraft());

          if (tile.getExhaustionCostPerHit() > 0) {
            player.addExhaustion((float) tile.getExhaustionCostPerHit());
          }

          if (tile.recipeProgress.get() >= 0.9999) {
            tile.recipeProgress.set(0);

            ItemStack result = recipe.getRecipeOutput().copy();
            FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, this.wrapper);
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

            StackHelper.spawnStackOnTop(world, result, tile.getPos(), 0.75);

            int toolDamagePerCraft = tile.getToolDamagePerCraft();

            if (toolDamagePerCraft > 0) {
              heldItem.damageItem(toolDamagePerCraft, player);
            }

            if (tile.usesDurability()
                && tile.remainingDurability.add(-1) == 0) {

              world.destroyBlock(tile.pos, false);
              world.playSound(
                  null,
                  tile.pos,
                  SoundEvents.ENTITY_ITEM_BREAK,
                  SoundCategory.BLOCKS,
                  1.0F,
                  Util.RANDOM.nextFloat() * 0.4F + 0.8F
              );
            }

            if (tile.getExhaustionCostPerCraftComplete() > 0) {
              player.addExhaustion((float) tile.getExhaustionCostPerCraftComplete());
            }
          }
        }

      } else {

        int stateId = tile.getBlockStateIdForParticles();

        for (int i = 0; i < 2; ++i) {
          world.spawnParticle(
              EnumParticleTypes.BLOCK_CRACK,
              tile.pos.getX() + hitX + (tile.world.rand.nextFloat() * 2 - 1) * 0.1,
              tile.pos.getY() + hitY + 0.1,
              tile.pos.getZ() + hitZ + (tile.world.rand.nextFloat() * 2 - 1) * 0.1,
              0.0D,
              0.0D,
              0.0D,
              stateId
          );
        }
      }

      return true;
    }
  }

  protected int getBlockStateIdForParticles() {

    IBlockState state = Blocks.PLANKS.getDefaultState()
        .withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
    return Block.getStateId(state);
  }

  private class InputInteraction
      extends InteractionItemStack<TileWorktable> {

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
              Transform.translate(x * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025, 15f / 16f + (1f / 32f), z * (ONE_THIRD - 0.025) + ONE_SIXTH + 0.025),
              Transform.rotate(new Quaternion[]{
                  Transform.rotate(0, 1, 0, 180),
                  Transform.rotate(1, 0, 0, -90)
              }),
              Transform.scale(0.20, 0.20, 0.20)
          )
      );
    }

    @Override
    public Vec3d getTextOffset(EnumFacing tileFacing, EnumFacing playerHorizontalFacing, EnumFacing sideHit) {

      return this.textOffset;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      Item item = itemStack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      if (registryName == null) {
        return false;
      }

      return ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(registryName) == -1;
    }

    @Override
    protected int getInsertItemCount(EnumType type, ItemStack itemStack) {

      return 1;
    }
  }

  private class ShelfInteraction
      extends InteractionItemStack<TileWorktable> {

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

    private final int maxStackSize;

    /* package */ InputStackHandler(int maxStackSize) {

      super(9);
      this.maxStackSize = maxStackSize;
    }

    @Override
    public int getSlotLimit(int slot) {

      return this.maxStackSize;
    }
  }

  private class ShelfStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final int maxStackSize;

    /* package */ ShelfStackHandler(int maxStackSize) {

      super(3);
      this.maxStackSize = maxStackSize;
    }

    @Override
    public int getSlotLimit(int slot) {

      return this.maxStackSize;
    }
  }

  // ---------------------------------------------------------------------------
  // - Inventory
  // ---------------------------------------------------------------------------

  private class InventoryWrapper
      extends InventoryCrafting {

    private final TileWorktable tile;

    /* package */ InventoryWrapper(TileWorktable tile) {

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

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {

      this.tile.inputStackHandler.setStackInSlot(index, stack);
    }

    @Nonnull
    @Override
    public ItemStack getStackInRowAndColumn(int x, int y) {

      if (x >= 0 && x < 3 && y >= 0 && y < 3) {
        int index = x + y * 3;
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
