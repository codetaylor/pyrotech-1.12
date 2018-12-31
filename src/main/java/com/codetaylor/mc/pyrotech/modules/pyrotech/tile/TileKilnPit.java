package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileBurnableBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

import static com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockKilnPit.VARIANT;

public class TileKilnPit
    extends TileBurnableBase
    implements ITickable,
    IProgressProvider,
    ITileInteractable {

  private static final int DEFAULT_TOTAL_BURN_TIME_TICKS = 1000;

  private InputStackHandler stackHandler;
  private OutputStackHandler outputStackHandler;
  private LogStackHandler logStackHandler;

  private int totalBurnTimeTicks;
  private boolean active;
  private TileDataFloat progress;

  // transient
  private IInteraction[] interactions;

  public TileKilnPit() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.stackHandler = new InputStackHandler();
    this.stackHandler.addObserver((handler, slot) -> {
      this.updateBurnTime(handler.getStackInSlot(slot));
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler();

    this.logStackHandler = new LogStackHandler();

    this.totalBurnTimeTicks = DEFAULT_TOTAL_BURN_TIME_TICKS;

    this.setNeedStructureValidation();
    this.reset();

    // --- Network ---

    this.progress = new TileDataFloat(0, 20);

    ModulePyrotech.TILE_DATA_SERVICE.register(this, new ITileData[]{
        new TileDataItemStackHandler<>(this.stackHandler),
        new TileDataItemStackHandler<>(this.outputStackHandler),
        this.progress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionThatch(),
        new InteractionLog(this.logStackHandler),
        new Interaction(new ItemStackHandler[]{this.stackHandler, this.outputStackHandler})
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

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
    this.markDirty();
  }

  private void setTotalBurnTimeTicks(int totalBurnTimeTicks) {

    this.totalBurnTimeTicks = totalBurnTimeTicks;
    this.reset();
    this.markDirty();
  }

  @Override
  public float getProgress() {

    return this.progress.get();
  }

  @Override
  protected boolean isActive() {

    return this.active;
  }

  @Override
  protected int getTotalBurnTimeTicks() {

    return this.totalBurnTimeTicks;
  }

  @Override
  protected int getBurnStages() {

    return 1;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  private float calculateProgress() {

    if (!this.isActive()) {
      return 0;
    }

    int totalBurnTimeTicks = this.getTotalBurnTimeTicks();
    int totalStages = this.getBurnStages();
    int burnTimePerStage = totalBurnTimeTicks / totalStages;
    float progress = ((this.remainingStages - 1) * burnTimePerStage + this.burnTimeTicksPerStage) / (float) totalBurnTimeTicks;

    return 1f - progress;
  }

  private void updateBurnTime(ItemStack itemStack) {

    if (!itemStack.isEmpty()) {
      KilnPitRecipe recipe = KilnPitRecipe.getRecipe(itemStack);

      if (recipe != null) {
        float modifier = (float) (1.0f - this.countAdjacentRefractoryBlocks() * ModulePyrotechConfig.PIT_KILN.REFRACTORY_BLOCK_TIME_BONUS);
        int modifiedBurnTime = (int) (recipe.getTimeTicks() * modifier);
        int burnTimeTicks = Math.max(1, modifiedBurnTime);
        this.setTotalBurnTimeTicks(burnTimeTicks);
      }
    }
  }

  @Override
  protected void onUpdate() {

    if (this.world.isRainingAt(this.pos)) {
      // set back to wood state and douse fire
      IBlockState blockState = ModuleBlocks.KILN_PIT.getDefaultState()
          .withProperty(VARIANT, BlockKilnPit.EnumType.WOOD);
      this.world.setBlockState(this.pos, blockState);

      BlockPos up = this.pos.up();

      if (this.world.getBlockState(up).getBlock() == Blocks.FIRE) {
        this.world.setBlockToAir(up);
      }

      this.setActive(false);
    }

    this.progress.set(this.calculateProgress());
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
      this.markDirty();
    }

    this.setActive(false);
    IBlockState blockState = ModuleBlocks.KILN_PIT.getDefaultState()
        .withProperty(VARIANT, BlockKilnPit.EnumType.COMPLETE);
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

    if (selfBlockState.getValue(VARIANT) != BlockKilnPit.EnumType.WOOD
        && selfBlockState.getValue(VARIANT) != BlockKilnPit.EnumType.ACTIVE) {
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
        .withProperty(VARIANT, BlockKilnPit.EnumType.COMPLETE);
    this.world.setBlockState(this.pos, blockState);
    this.world.setBlockToAir(this.pos.up());
  }

  private int countAdjacentRefractoryBlocks() {

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

    for (Predicate<IBlockState> matcher : ModulePyrotechRegistries.REFRACTORY_BLOCK_LIST) {

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

  // ---------------------------------------------------------------------------
  // - Persistence
  // ---------------------------------------------------------------------------

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

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

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
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Miscellaneous
  // ---------------------------------------------------------------------------

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

  private class InteractionThatch
      extends InteractionUseItemBase<TileKilnPit> {

    /* package */ InteractionThatch() {

      super(EnumFacing.VALUES, InteractionBounds.BLOCK);
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    protected boolean allowInteraction(TileKilnPit tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModuleBlocks.THATCH));
    }

    @Override
    protected boolean doInteraction(TileKilnPit tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (world.getBlockState(hitPos).getValue(VARIANT) != BlockKilnPit.EnumType.EMPTY) {
        return false;
      }

      // If the item in the player's hand is thatch, change the state of the
      // kiln to thatch.

      if (!world.isRemote) {
        ItemStack heldItem = player.getHeldItemMainhand();
        heldItem.setCount(heldItem.getCount() - 1);
        world.setBlockState(hitPos, ModuleBlocks.KILN_PIT.getDefaultState()
            .withProperty(VARIANT, BlockKilnPit.EnumType.THATCH));
        world.playSound(null, hitPos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1, 1);
      }

      return true;
    }

    @Override
    protected int getItemDamage(ItemStack itemStack) {

      return 0;
    }
  }

  private class InteractionLog
      extends InteractionBase<TileKilnPit> {

    private final ItemStackHandler logStackHandler;

    /* package */ InteractionLog(ItemStackHandler logStackHandler) {

      super(EnumFacing.VALUES, InteractionBounds.BLOCK);
      this.logStackHandler = logStackHandler;
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    public boolean interact(Type type, TileKilnPit tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (world.getBlockState(hitPos).getValue(VARIANT) != BlockKilnPit.EnumType.THATCH) {
        return false;
      }

      // If the player is holding enough ore:logWood, place the wood and set the
      // kiln state to wood.

      ItemStack heldItem = player.getHeldItemMainhand();

      if (heldItem.getCount() < 3) {
        return false;
      }

      if (OreDictHelper.contains("logWood", heldItem)) {

        if (!world.isRemote) {
          heldItem.setCount(heldItem.getCount() - 3);
          this.logStackHandler
              .insertItem(0, new ItemStack(heldItem.getItem(), 3, heldItem.getMetadata()), false);
          world.setBlockState(tile.getPos(), ModuleBlocks.KILN_PIT.getDefaultState()
              .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.WOOD));
          world.playSound(null, tile.getPos(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
        }

        return true;
      }

      return false;
    }
  }

  private class Interaction
      extends InteractionItemStack<TileKilnPit> {

    public Interaction(ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, EnumFacing.VALUES, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 0.4, 0.5),
          Transform.rotate(),
          Transform.scale(0.5, 0.5, 0.5)
      ));
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (KilnPitRecipe.getRecipe(itemStack) != null);
    }

    @Override
    protected boolean doExtract(Type type, World world, EntityPlayer player, BlockPos tilePos) {

      // Extract all slots in the output stack handler.

      if (!world.isRemote) {
        ItemStackHandler outputStackHandler = this.stackHandlers[1];

        int slots = outputStackHandler.getSlots();

        for (int i = 1; i < slots; i++) {
          ItemStack extractItem = outputStackHandler.extractItem(i, outputStackHandler.getStackInSlot(i).getCount(), false);

          if (!extractItem.isEmpty()) {
            StackHelper.addToInventoryOrSpawn(world, player, extractItem, tilePos);
          }
        }
      }

      return super.doExtract(type, world, player, tilePos);
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

      // TODO: Config Option
      return 8;
    }
  }

  private class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(9);
    }
  }

  private class LogStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ LogStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 3;
    }
  }
}
