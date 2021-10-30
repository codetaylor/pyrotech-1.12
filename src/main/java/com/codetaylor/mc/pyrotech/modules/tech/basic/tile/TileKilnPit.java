package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.inventory.LargeDynamicStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileBurnableBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.core.tile.IProgressProvider;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

import static com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit.VARIANT;

public class TileKilnPit
    extends TileBurnableBase
    implements ITickable,
    IProgressProvider,
    ITileInteractable {

  private static final int DEFAULT_TOTAL_BURN_TIME_TICKS = 1000;

  private final InputStackHandler stackHandler;
  private final OutputStackHandler outputStackHandler;
  private final LogStackHandler logStackHandler;

  private int totalBurnTimeTicks;
  private boolean active;
  private final TileDataFloat progress;

  // transient
  private final IInteraction<?>[] interactions;

  public TileKilnPit() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

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

    ModuleTechBasic.TILE_DATA_SERVICE.register(this, new ITileData[]{
        new TileDataItemStackHandler<>(this.stackHandler),
        new TileDataItemStackHandler<>(this.logStackHandler),
        new TileDataLargeItemStackHandler<>(this.outputStackHandler),
        this.progress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionThatch(),
        new InteractionLog(this, this.logStackHandler, 0),
        new InteractionLog(this, this.logStackHandler, 1),
        new InteractionLog(this, this.logStackHandler, 2),
        new Interaction(this, new ItemStackHandler[]{this.stackHandler, this.outputStackHandler})
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
    this.logStackHandler.setStackInSlot(0, ItemStack.EMPTY);
    this.logStackHandler.setStackInSlot(1, ItemStack.EMPTY);
    this.logStackHandler.setStackInSlot(2, ItemStack.EMPTY);
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
        int burnTimeTicks = Math.max(1, recipe.getTimeTicks());

        // y = (1-n)x + n

        double n = ModuleTechBasicConfig.PIT_KILN.VARIABLE_SPEED_MODIFIER;
        double x = (itemStack.getCount() == 1) ? 0 : (itemStack.getCount() - 1) / ((double) ModuleTechBasicConfig.PIT_KILN.MAX_STACK_SIZE - 1);
        double scalar = (1 - n) * x + n;

        this.setTotalBurnTimeTicks((int) (burnTimeTicks * scalar));
      }
    }
  }

  @Override
  protected void onUpdate() {

    if (this.world.isRainingAt(this.pos)) {
      // set back to wood state and douse fire
      IBlockState blockState = ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
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
    IBlockState blockState = ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
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
    IBlockState blockState = ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
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

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {

      for (Predicate<IBlockState> matcher : ModuleTechRefractory.Registries.REFRACTORY_BLOCK_LIST) {

        if (matcher.test(blockState)) {
          return true;
        }
      }
    }

    return false;
  }

  private void insertOutputItem(ItemStack output) {

    this.outputStackHandler.insertItem(output, false);
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

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_PIT_KILN;
  }

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  private static class InteractionThatch
      extends InteractionUseItemBase<TileKilnPit> {

    /* package */ InteractionThatch() {

      super(EnumFacing.VALUES, InteractionBounds.BLOCK);
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      //noinspection deprecation
      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    protected boolean allowInteraction(TileKilnPit tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (player.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModuleCore.Blocks.THATCH));
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
        world.setBlockState(hitPos, ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
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

  private static class InteractionLog
      extends InteractionItemStack<TileKilnPit> {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    private final TileKilnPit tile;

    public InteractionLog(TileKilnPit tile, ItemStackHandler stackHandler, int slot) {

      super(
          new ItemStackHandler[]{stackHandler},
          slot,
          EnumFacing.VALUES,
          new AxisAlignedBB(slot * ONE_THIRD, 2 * ONE_THIRD, 0, slot * ONE_THIRD + ONE_THIRD, 1, 1),
          new Transform(
              Transform.translate(slot * ONE_THIRD + ONE_SIXTH, 2 * ONE_THIRD + ONE_SIXTH, 0.5),
              Transform.rotate(1, 0, 0, 90),
              Transform.scale(ONE_THIRD, 1, ONE_THIRD)
          )
      );
      this.tile = tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return OreDictHelper.contains("logWood", itemStack);
    }

    @Override
    public boolean isEnabled() {

      World world = this.tile.getWorld();
      IBlockState blockState = world.getBlockState(this.tile.getPos());
      BlockKilnPit.EnumType type = blockState.getValue(BlockKilnPit.VARIANT);
      return type == BlockKilnPit.EnumType.THATCH
          || type == BlockKilnPit.EnumType.WOOD;
    }

    @Override
    protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      if (!world.isRemote) {
        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
      }

      ItemStackHandler stackHandler = this.stackHandlers[0];

      if (!stackHandler.getStackInSlot(0).isEmpty()
          && !stackHandler.getStackInSlot(1).isEmpty()
          && !stackHandler.getStackInSlot(2).isEmpty()) {

        if (!world.isRemote) {
          world.setBlockState(pos, ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
              .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.WOOD));
        }
      }
    }

    @Override
    protected void onExtract(EnumType type, World world, EntityPlayer player, BlockPos pos) {

      if (!world.isRemote
          && world.getBlockState(pos).getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.WOOD) {
        world.setBlockState(pos, ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
            .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.THATCH));
      }
    }
  }

  private static class Interaction
      extends InteractionItemStack<TileKilnPit> {

    private final TileKilnPit tile;

    public Interaction(TileKilnPit tile, ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, EnumFacing.VALUES, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 0.4, 0.5),
          Transform.rotate(),
          Transform.scale(0.5, 0.5, 0.5)
      ));
      this.tile = tile;
    }

    @Override
    public boolean isEnabled() {

      IBlockState blockState = this.tile.world.getBlockState(this.tile.pos);
      return blockState.getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.EMPTY;
    }

    @Override
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      if (!this.stackHandlers[0].getStackInSlot(0).isEmpty()) {
        super.renderSolidPassText(world, fontRenderer, yaw, offset, pos, blockState, partialTicks);
      }
    }

    @Override
    public AxisAlignedBB getInteractionBounds(World world, BlockPos pos, IBlockState blockState) {

      //noinspection deprecation
      return blockState.getBlock().getBoundingBox(blockState, world, pos);
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (KilnPitRecipe.getRecipe(itemStack) != null);
    }

    @Override
    protected boolean doExtract(EnumType type, World world, EntityPlayer player, BlockPos tilePos) {

      // Extract all slots in the output stack handler.

      if (!world.isRemote) {
        ItemStackHandler outputStackHandler = this.stackHandlers[1];

        int slots = outputStackHandler.getSlots();

        for (int i = 1; i < slots; i++) {
          ItemStack extractItem = outputStackHandler.extractItem(i, outputStackHandler.getStackInSlot(i).getCount(), false);

          if (!extractItem.isEmpty()) {
            StackHelper.addToInventoryOrSpawn(world, player, extractItem, tilePos, 1.0, false, (type == EnumType.MouseClick));
          }
        }
      }

      return super.doExtract(type, world, player, tilePos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private static class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InputStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return ModuleTechBasicConfig.PIT_KILN.MAX_STACK_SIZE;
    }
  }

  private static class OutputStackHandler
      extends LargeDynamicStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler() {

      super(9);
    }
  }

  private static class LogStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ LogStackHandler() {

      super(3);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }
}
