package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.CampfireInteractionLogRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionUseItemToActivateWorker;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileCampfire
    extends TileNetWorkerBase
    implements ITickable,
    ITileInteractable {

  // --- Networked ---

  private InputStackHandler inputStackHandler;
  private OutputStackHandler outputStackHandler;
  private FuelStackHandler fuelStackHandler;

  private TileDataInteger ashLevel;
  private TileDataBoolean dead;
  private TileDataInteger burnTimeRemaining;

  // --- Server ---

  private int cookTime;
  private int cookTimeTotal;

  /**
   * Indicates if this has been lit, affects drops.
   */
  private boolean doused;

  /**
   * A counter used to extinguish the fire when raining.
   */
  private int rainTimeRemaining;

  private IInteraction[] interactions;

  public TileCampfire() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    // --- Init ---

    this.inputStackHandler = new InputStackHandler();
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.setCookTime(this.getCookTime(handler.getStackInSlot(slot)));
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler();
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.fuelStackHandler = new FuelStackHandler();
    this.fuelStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.ashLevel = new TileDataInteger(0);
    this.dead = new TileDataBoolean(false);
    this.burnTimeRemaining = new TileDataInteger(ModulePyrotechConfig.FUEL.TINDER_BURN_TIME_TICKS, 20);

    this.cookTime = -1;
    this.cookTimeTotal = -1;
    this.rainTimeRemaining = ModulePyrotechConfig.CAMPFIRE.TICKS_BEFORE_EXTINGUISHED;

    // --- Network ---

    this.registerTileData(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler),
        new TileDataItemStackHandler<>(this.outputStackHandler),
        new TileDataItemStackHandler<>(this.fuelStackHandler),
        this.ashLevel,
        this.dead,
        this.burnTimeRemaining
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new TileCampfire.InteractionFood(new ItemStackHandler[]{
            this.inputStackHandler,
            this.outputStackHandler
        }),
        new TileCampfire.InteractionShovel(),
        new InteractionUseItemToActivateWorker(Items.FLINT_AND_STEEL, EnumFacing.VALUES, BlockCampfire.AABB_FULL),
        new TileCampfire.InteractionLog(this)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public int getAshLevel() {

    return this.ashLevel.get();
  }

  private void setAshLevel(int ashLevel) {

    this.ashLevel.set(ashLevel);

    if (this.ashLevel.isDirty()) {
      this.markDirty();
    }
  }

  private void setCookTime(int cookTime) {

    this.cookTime = cookTime;
    this.cookTimeTotal = cookTime;
  }

  private int getCookTime(ItemStack stack) {

    return (stack.isEmpty()) ? -1 : ModulePyrotechConfig.CAMPFIRE.COOK_TIME_TICKS;
  }

  public BlockCampfire.EnumType getState() {

    if (this.workerIsActive()) {
      return BlockCampfire.EnumType.LIT;

    } else if (this.isDead()) {
      return BlockCampfire.EnumType.ASH;
    }

    return BlockCampfire.EnumType.NORMAL;
  }

  public int getFuelRemaining() {

    // 0 is no wood
    // [0, 8]

    int index = this.fuelStackHandler.getLastNonEmptyIndex();
    return (index + 1);
  }

  public boolean isDead() {

    return this.dead.get();
  }

  private void setDead() {

    this.dead.set(true);

    if (this.dead.isDirty()) {
      this.markDirty();
    }
  }

  @Override
  public void workerSetActive(boolean active) {

    if (this.isDead()) {
      return;
    }

    super.workerSetActive(active);
  }

  public int getRemainingBurnTimeTicks() {

    return this.burnTimeRemaining.get();
  }

  @Override
  public boolean workerIsActive() {

    return !this.isDead() && super.workerIsActive();
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public boolean workerRequiresFuel() {

    return (this.burnTimeRemaining.get() <= 0);
  }

  @Override
  public boolean workerConsumeFuel() {

    ItemStack itemStack = this.fuelStackHandler.extractItem(0, 1, false);

    if (!itemStack.isEmpty()) {
      this.burnTimeRemaining.set(ModulePyrotechConfig.CAMPFIRE.BURN_TIME_TICKS_PER_LOG);
      return true;
    }

    // No fuel remaining, deactivate, set dead, and drop any input or output
    // stacks.

    this.workerSetActive(false);
    this.setDead();

    ItemStack contents = this.inputStackHandler.extractItem(0, 64, false);

    if (!contents.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, contents, this.pos);
    }

    contents = this.outputStackHandler.extractItem(0, 64, false);

    if (!contents.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, contents, this.pos);
    }

    return false;
  }

  @Override
  protected float workerCalculateProgress() {

    if (this.cookTime < 0) {
      return 0;
    }

    return 1f - (this.cookTime / (float) this.cookTimeTotal);
  }

  @Override
  public boolean workerDoWork() {

    // Deactivate the worker if the ash level gets too high.
    if (this.ashLevel.get() == 8) {
      return false; // Deactivate the worker
    }

    // If the block underneath the campfire is flammable, there's a small chance
    // that it will turn into a fire. When this happens, the campfire will be
    // destroyed and pop its contents into the world.
    if (Math.random() < 0.05
        && this.world.getBlockState(this.pos.down()).getBlock().isFlammable(this.world, this.pos.down(), EnumFacing.UP)) {
      this.world.setBlockState(this.pos.down(), Blocks.FIRE.getDefaultState(), 3);
      return false; // Deactivate the worker
    }

    // If it's raining and the campfire is configured to be put out by rain,
    // decrement the rain time and check if the fire should be doused.
    if (ModulePyrotechConfig.CAMPFIRE.EXTINGUISHED_BY_RAIN) {

      if (this.world.isRainingAt(this.pos.up())) {

        if (this.rainTimeRemaining > 0) {
          this.rainTimeRemaining -= 1;
        }

        if (this.rainTimeRemaining == 0) {
          this.doused = true;
          return false; // Deactivate the worker
        }

      } else {
        // If it's not raining, reset the rain time counter.
        this.rainTimeRemaining = ModulePyrotechConfig.CAMPFIRE.TICKS_BEFORE_EXTINGUISHED;
      }
    }

    // Decrement the cook time and check for recipe completion.
    // Update worker progress.
    if (this.cookTime > 0) {
      this.cookTime -= 1;
    }

    if (this.cookTime == 0) {
      ItemStack itemStack = this.inputStackHandler.extractItem(0, 1, false);

      if (!itemStack.isEmpty()) {
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(itemStack).copy();
        this.outputStackHandler.insertItem(0, result, false);
      }
    }

    // Decrement the burn time remaining and randomly add ash.

    if (this.burnTimeRemaining.add(-1) <= 0) {

      if (Math.random() < ModulePyrotechConfig.CAMPFIRE.ASH_CHANCE) {
        this.ashLevel.add(1);
      }
    }

    return true;
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

    compound.setBoolean("dead", this.dead.get());
    compound.setInteger("ashLevel", this.ashLevel.get());
    compound.setInteger("burnTimeRemaining", this.burnTimeRemaining.get());

    compound.setInteger("cookTime", this.cookTime);
    compound.setInteger("cookTimeTotal", this.cookTimeTotal);
    compound.setBoolean("doused", this.doused);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));

    this.dead.set(compound.getBoolean("dead"));
    this.ashLevel.set(compound.getInteger("ashLevel"));
    this.burnTimeRemaining.set(compound.getInteger("burnTimeRemaining"));

    this.cookTime = compound.getInteger("cookTime");
    this.cookTimeTotal = compound.getInteger("cookTimeTotal");
    this.doused = compound.getBoolean("doused");
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    if (this.getState() == BlockCampfire.EnumType.ASH) {
      StackHelper.spawnStackOnTop(this.world, ItemMaterial.EnumType.PIT_ASH.asStack(1), this.pos, -0.125);

    } else if (!this.doused
        && this.getState() == BlockCampfire.EnumType.NORMAL) {
      StackHelper.spawnStackOnTop(this.world, new ItemStack(ModuleItems.TINDER), this.pos, -0.125);
    }

    ItemStack itemStack;

    for (int i = 0; i < this.fuelStackHandler.getSlots(); i++) {

      itemStack = this.fuelStackHandler.extractItem(i, 64, false);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, -0.125);

      } else {
        break;
      }
    }

    ItemStackHandler stackHandler = this.getInputStackHandler();
    itemStack = stackHandler.extractItem(0, 64, false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, -0.125);
    }

    stackHandler = this.getOutputStackHandler();
    itemStack = stackHandler.extractItem(0, 64, false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, -0.125);
    }

    if (this.ashLevel.get() > 0) {
      ItemStack ashStack = ItemMaterial.EnumType.PIT_ASH.asStack(this.ashLevel.get());
      StackHelper.spawnStackOnTop(this.world, ashStack, this.pos, -0.125);
    }

    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.ashLevel.isDirty()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }

    if (this.active.isDirty()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }

    if (this.dead.isDirty() && this.dead.get()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    // Required in both passes for the interactable TESR.
    return (pass == 0) || (pass == 1);
  }

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    return new AxisAlignedBB(this.getPos());
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class InteractionFood
      extends InteractionItemStack<TileCampfire> {

    /* package */ InteractionFood(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.UP},
          BlockCampfire.AABB_FULL,
          new Transform(
              Transform.translate(0.5, 0.5, 0.5),
              Transform.rotate(),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    public boolean isEnabled() {

      return !TileCampfire.this.isDead();
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (itemStack.getItem() instanceof ItemFood)
          && !FurnaceRecipes.instance().getSmeltingResult(itemStack).isEmpty();
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (player.isSneaking()) {
        return false;
      }

      return super.interact(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
    }
  }

  public static class InteractionLog
      extends InteractionBase<TileCampfire> {

    /**
     * Used to cache the last item checked for validation.
     */
    protected ItemStack lastItemChecked;

    /**
     * Used to cache if the last item checked was valid.
     */
    protected boolean lastItemValid;

    private final TileCampfire tile;

    /* package */ InteractionLog(TileCampfire tile) {

      super(EnumFacing.VALUES, BlockCampfire.AABB_FULL);
      this.tile = tile;
    }

    public int getLogCount() {

      int firstEmptyIndex = this.tile.fuelStackHandler.getFirstEmptyIndex();
      return firstEmptyIndex == -1 ? this.tile.fuelStackHandler.getSlots() : firstEmptyIndex;
    }

    public ItemStack getLog(int slot) {

      return this.tile.fuelStackHandler.getStackInSlot(slot);
    }

    public boolean isItemStackValid(ItemStack itemStack) {

      if (itemStack.isEmpty()) {
        return false;
      }

      if (this.lastItemChecked == null
          || this.lastItemChecked.getItem() != itemStack.getItem()
          || this.lastItemChecked.getMetadata() != itemStack.getMetadata()) {

        // Run the potentially expensive check.
        this.lastItemChecked = itemStack.copy();
        this.lastItemValid = this.doItemStackValidation(itemStack);
      }

      return this.lastItemValid;
    }

    private boolean doItemStackValidation(ItemStack itemStack) {

      return OreDictHelper.contains("logWood", itemStack);
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (heldItem.isEmpty()
          && player.isSneaking()) {

        // If the player is sneaking with an empty hand, remove logs and damage the player.

        ItemStack itemStack = tile.fuelStackHandler.extractItem(0, 1, world.isRemote);

        if (!itemStack.isEmpty()) {

          if (!world.isRemote) {

            if (Math.random() < ModulePyrotechConfig.CAMPFIRE.PLAYER_BURN_CHANCE) {

              if (!player.isImmuneToFire()
                  && !EnchantmentHelper.hasFrostWalkerEnchantment(player)
                  && ModuleBlocks.CAMPFIRE.getActualState(state, world, hitPos).getValue(BlockCampfire.VARIANT) == BlockCampfire.EnumType.LIT) {
                player.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModulePyrotechConfig.CAMPFIRE.PLAYER_BURN_DAMAGE);
              }
            }

            StackHelper.addToInventoryOrSpawn(world, player, itemStack, hitPos, -0.125);
          }

          return true;
        }

      } else if (!heldItem.isEmpty()
          && !player.isSneaking()) {

        // If the player is not sneaking with a full hand, attempt to add wood.

        if (this.isItemStackValid(heldItem)) {
          LIFOStackHandler fuelStackHandler = tile.fuelStackHandler;

          if (!world.isRemote) {
            int firstEmptyIndex = fuelStackHandler.getFirstEmptyIndex();

            if (firstEmptyIndex > -1) {
              fuelStackHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getMetadata()), false);
              world.playSound(null, hitPos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);

              if (!player.isCreative()) {
                heldItem.setCount(heldItem.getCount() - 1);
              }
            }
          }

          return true;
        }
      }

      return false;
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CampfireInteractionLogRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    public boolean forceRenderAdditivePassWhileSneaking() {

      return true;
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return CampfireInteractionLogRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  private class InteractionShovel
      extends InteractionBase<TileCampfire> {

    /* package */ InteractionShovel() {

      super(EnumFacing.VALUES, BlockCampfire.AABB_FULL);
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (tile.getAshLevel() > 0
          && heldItem.getItem().getToolClasses(heldItem).contains("shovel")) {

        if (!world.isRemote) {
          tile.setAshLevel(tile.getAshLevel() - 1);
          StackHelper.spawnStackOnTop(world, ItemMaterial.EnumType.PIT_ASH.asStack(), hitPos, 0);
          heldItem.damageItem(1, player);
          world.playSound(null, hitPos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1, 1);
        }

        return true;
      }

      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InputStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }

  private class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }

  private class FuelStackHandler
      extends LIFOStackHandler
      implements ITileDataItemStackHandler {

    /* package */ FuelStackHandler() {

      super(8);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }

}
