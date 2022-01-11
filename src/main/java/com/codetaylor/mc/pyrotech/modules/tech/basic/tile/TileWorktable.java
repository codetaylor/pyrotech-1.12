package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.GameStages;
import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Quaternion;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketNoHunger;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleProgress;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileWorktable
    extends TileEntityDataBase
    implements ITileInteractable {

  private InventoryWrapper inventoryWrapper;
  private TileDataItemStackHandler<InputStackHandler> inputTileDataItemStackHandler;
  private InputStackHandler inputStackHandler;
  private ShelfStackHandler shelfStackHandler;
  private TileDataInteger remainingDurability;

  private TileDataFloat recipeProgress;

  private IInteraction[] interactions;

  private WorktableRecipe recipe;
  private ResourceLocation retainedRecipe;

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
        this.recipeProgress,
        this.remainingDurability
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

  public int getRemainingDurability() {

    return this.remainingDurability.get();
  }

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public WorktableRecipe getWorktableRecipe() {

    return this.recipe;
  }

  public IRecipe getRecipe() {

    if (this.recipe == null) {
      return null;

    } else {
      return this.recipe.getRecipe();
    }
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

  public int getDurability() {

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

  private void setRetainedRecipe(ResourceLocation resourceLocation) {

    this.retainedRecipe = resourceLocation;
  }

  public InventoryWrapper getInventoryWrapper() {

    return this.inventoryWrapper;
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

    this.recipe = WorktableRecipe.getRecipe(this.inventoryWrapper, this.world);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  protected void setWorldCreate(World world) {

    this.world = world;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.shelfStackHandler.deserializeNBT(compound.getCompoundTag("shelfStackHandler"));
    this.remainingDurability.set(compound.getInteger("remainingDurability"));

    if (compound.hasKey("retainedRecipe")) {
      this.retainedRecipe = new ResourceLocation(compound.getString("retainedRecipe"));
    }

    this.updateRecipe();
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("shelfStackHandler", this.shelfStackHandler.serializeNBT());
    compound.setInteger("remainingDurability", this.remainingDurability.get());

    if (this.retainedRecipe != null) {
      compound.setString("retainedRecipe", this.retainedRecipe.toString());
    }

    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_WORKTABLE;
  }

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

      ItemStack heldItemStack = player.getHeldItem(hand);

      if (heldItemStack.isEmpty()
          && player.isSneaking()
          && ModuleTechBasicConfig.WORKTABLE_COMMON.ALLOW_RECIPE_CLEAR) {
        return true;

      } else if (heldItemStack.isEmpty()) {
        return false;
      }

      Item item = heldItemStack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      if (registryName == null) {
        return false;
      }

      WorktableRecipe recipe = tile.getWorktableRecipe();

      boolean sneaking = player.isSneaking();

      if (sneaking) {
        // Player is sneaking, allow only hammers for recipe repeat.
        return ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(registryName) > -1;

      } else {

        if (recipe == null) {
          return false;

        } else if (!recipe.getRecipe().getCraftingResult(this.wrapper).isEmpty()
            && this.isValidTool(player, item, registryName, recipe)) {

          if (player.getFoodStats().getFoodLevel() < tile.getMinimumHungerToUse()) {

            if (!world.isRemote) {
              ModuleTechBasic.PACKET_SERVICE.sendTo(new SCPacketNoHunger(), (EntityPlayerMP) player);
            }
            return false;
          }

          return true;
        }
      }

      return false;
    }

    private boolean isValidTool(EntityPlayer player, Item item, ResourceLocation registryName, WorktableRecipe recipe) {

      if (Loader.isModLoaded("gamestages")) {
        Stages stages = recipe.getStages();

        if (!GameStages.allowed(player, stages)) {
          return false;
        }
      }

      List<Item> toolList = recipe.getToolList();

      if (toolList.isEmpty()) {
        return ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(registryName) > -1;

      } else {
        return toolList.contains(item);
      }
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

      // We apply our own item damage on recipe completion.
    }

    @Override
    protected boolean doInteraction(TileWorktable tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (player.isSneaking()) {

        if (heldItem.isEmpty()) {
          // The heldItem will only be empty if the recipe clear feature is
          // enabled in the config and the player is sneaking. This is checked
          // in the allowInteraction method.

          // Remove all stuffs from crafting grid
          this.doRecipeClear(tile, world, player);

        } else if (ModuleTechBasicConfig.WORKTABLE_COMMON.ALLOW_RECIPE_REPEAT) {
          // Repeat the last recipe
          this.doRecipeRepeat(tile, player, heldItem);
        }

      } else {
        // Use the tool to advance the recipe progress.
        this.doRecipeProgress(tile, world, hitPos, player, hitX, hitY, hitZ, heldItem);
      }

      return true;
    }

    private void doRecipeClear(TileWorktable tile, World world, EntityPlayer player) {

      int slots = tile.inputStackHandler.getSlots();

      for (int i = 0; i < slots; i++) {
        int slotLimit = tile.inputStackHandler.getSlotLimit(i);
        ItemStack itemStack = tile.inputStackHandler.extractItem(i, slotLimit, false);
        StackHelper.addToInventoryOrSpawn(world, player, itemStack, tile.getPos(), 1, false, true);
      }
    }

    private void doRecipeRepeat(TileWorktable tile, EntityPlayer player, ItemStack heldItem) {

      WorktableRecipe existingRecipe = tile.getWorktableRecipe();
      IRecipe iRecipe;

      if (existingRecipe != null) {
        iRecipe = existingRecipe.getRecipe();

      } else {

        if (tile.retainedRecipe == null) {
          return;
        }

        WorktableRecipe retainedRecipe = WorktableRecipe.getRecipe(tile.retainedRecipe);

        if (retainedRecipe == null) {
          return;
        }

        iRecipe = retainedRecipe.getRecipe();
      }

      NonNullList<Ingredient> ingredients = iRecipe.getIngredients();
      ItemStackHandler inputStackHandler = tile.getInputStackHandler();
      List<ItemStack> itemStackList = new ArrayList<>(ingredients.size());

      // Gather ingredients from the player's inventory and hotbar.

      for (Ingredient ingredient : ingredients) {

        if (ingredient.apply(ItemStack.EMPTY)) {
          itemStackList.add(ItemStack.EMPTY);

        } else {

          for (ItemStack itemStack : player.inventory.mainInventory) {

            if (ingredient.apply(itemStack)) {
              ItemStack copy = itemStack.copy();
              copy.setCount(1);
              itemStackList.add(copy);
              itemStack.shrink(1);
              break;
            }
          }
        }
      }

      // If the player doesn't have all the items, return gathered items to the
      // player and abort.

      if (ingredients.size() != itemStackList.size()) {

        for (ItemStack itemStack : itemStackList) {
          player.addItemStackToInventory(itemStack);
        }

        return;
      }

      // Check if the table can take another recipe's worth of inputs.

      boolean tableHasRoom = true;

      for (int i = 0; i < itemStackList.size(); i++) {
        ItemStack remainingItemStack = inputStackHandler.insertItem(i, itemStackList.get(i), true);

        if (!remainingItemStack.isEmpty()) {
          tableHasRoom = false;
          break;
        }
      }

      // If the table doesn't have room, return gathered items to the player
      // and abort.

      if (!tableHasRoom) {

        for (ItemStack itemStack : itemStackList) {
          player.addItemStackToInventory(itemStack);
        }

        return;
      }

      // Finally, insert the gathered items.

      for (int i = 0; i < itemStackList.size(); i++) {
        inputStackHandler.insertItem(i, itemStackList.get(i), false);
      }

      // Damage the held item.

      int toolDamage = ModuleTechBasicConfig.WORKTABLE_COMMON.RECIPE_REPEAT_TOOL_DAMAGE;

      if (!tile.world.isRemote && toolDamage > 0) {
        heldItem.attemptDamageItem(toolDamage, RandomHelper.random(), (EntityPlayerMP) player);
      }
    }

    private void doRecipeProgress(TileWorktable tile, World world, BlockPos hitPos, EntityPlayer player, float hitX, float hitY, float hitZ, ItemStack heldItem) {

      IRecipe recipe = tile.getRecipe();
      WorktableRecipe worktableRecipe = tile.getWorktableRecipe();

      if (recipe == null) {
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

          ModuleCore.PACKET_SERVICE.sendToAllAround(
              new SCPacketParticleProgress(hitPos.getX() + 0.5, hitPos.getY() + 1, hitPos.getZ() + 0.5, 2),
              world.provider.getDimension(),
              hitPos
          );

          if (tile.recipeProgress.get() >= 0.9999) {
            tile.recipeProgress.set(0);

            tile.setRetainedRecipe(worktableRecipe.getRegistryName());

            ItemStack result = recipe.getCraftingResult(this.wrapper).copy();
            FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, this.wrapper);
            NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(this.wrapper);

            if (!remainingItems.isEmpty()) {

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
            }

            StackHelper.spawnStackOnTop(world, result, tile.getPos(), 0.75);

            int toolDamagePerCraft = tile.getToolDamagePerCraft();

            if (!worktableRecipe.getToolList().isEmpty()) {
              toolDamagePerCraft = worktableRecipe.getToolDamage();
            }

            if (toolDamagePerCraft > 0) {
              heldItem.damageItem(toolDamagePerCraft, player);
            }

            if (tile.usesDurability()
                && tile.remainingDurability.add(-1) == 0) {

              tile.dropContents();
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

      if (WorktableRecipe.hasRecipeWithTool(item)) {
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
