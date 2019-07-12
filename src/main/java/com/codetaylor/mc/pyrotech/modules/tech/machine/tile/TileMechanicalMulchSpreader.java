package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMulch;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.MechanicalMulchSpreaderInteractionMulchRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class TileMechanicalMulchSpreader
    extends TileCogWorkerBase
    implements ITileInteractable {

  private MulchStackHandler mulchStackHandler;
  private int[] cogData;

  public TileMechanicalMulchSpreader() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.mulchStackHandler = new MulchStackHandler(this.getCapacity(), this::isValidMulch);
    this.cogData = new int[2];

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.mulchStackHandler)
    });

    // --- Interactions ---

    this.addInteractions(new IInteraction[]{
        new InteractionMulch(this, this.mulchStackHandler, this::isValidMulch)
    });
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && facing == EnumFacing.UP;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.hasCapability(capability, facing)) {
      //noinspection unchecked
      return (T) this.mulchStackHandler;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public MulchStackHandler getMulchStackHandler() {

    return this.mulchStackHandler;
  }

  private boolean isValidMulch(ItemStack itemStack) {

    return (itemStack.getItem() == ModuleCore.Items.MULCH);
  }

  public int getCapacity() {

    return ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.CAPACITY;
  }

  @Override
  protected boolean isValidCog(ItemStack itemStack) {

    ResourceLocation registryName = itemStack.getItem().getRegistryName();
    int[] result = new int[2];
    return (ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.getCogData(registryName, result)[0] != -1);
  }

  @Override
  protected int getUpdateIntervalTicks() {

    return ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.WORK_INTERVAL_TICKS;
  }

  private int[] getCogData(ItemStack cog) {

    return ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.getCogData(cog.getItem().getRegistryName(), this.cogData);
  }

  // ---------------------------------------------------------------------------
  // - Work
  // ---------------------------------------------------------------------------

  @Override
  protected int doWork(ItemStack cog) {

    ItemStack mulchStack = this.mulchStackHandler.getStackInSlot(0);

    if (mulchStack.isEmpty()) {
      return 0;
    }

    int[] cogData = this.getCogData(cog);
    int cogRange = cogData[0];
    final int[] cogAttempts = {Math.min(cogData[1], mulchStack.getCount())};
    final int[] placedMulch = {0};

    IBlockState blockState = this.world.getBlockState(this.pos);
    EnumFacing facing = ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER.getFacing(blockState);
    BlockPos origin = this.pos.offset(facing, cogRange + 1).down();

    BlockHelper.forBlocksInCubeShuffled(this.world, origin, cogRange, 0, cogRange, (w, p, bs) -> {

      if (ItemMulch.canMulch(bs)) {
        this.mulchStackHandler.extractItem(0, 1, false);
        w.setBlockState(p, ModuleCore.Blocks.FARMLAND_MULCHED.getDefaultState());
        SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS);
        placedMulch[0] += 1;
      }

      cogAttempts[0] -= 1;

      return (cogAttempts[0] > 0);
    });

    if (ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.COG_DAMAGE_TYPE == ModuleTechMachineConfig.MechanicalMulchSpreader.EnumCogDamageType.PerItem) {
      return Math.max(1, placedMulch[0]); // cog damage

    } else {
      return 1;
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.mulchStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.mulchStackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_MECHANICAL_MULCHER;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  @Override
  public boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER) {

      BlockPos blockPos = this.getPos();
      EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
      blockPos = blockPos.offset(facing.getOpposite());

      return blockPos.getX() == pos.getX()
          && blockPos.getY() == pos.getY()
          && blockPos.getZ() == pos.getZ();
    }

    return false;
  }

  @Override
  protected Transform getCogInteractionTransform() {

    return new Transform(
        Transform.translate(0.5, 8.0 / 16.0, 21.0 / 16.0),
        Transform.rotate(),
        Transform.scale(0.75, 0.75, 2.00)
    );
  }

  @Override
  protected AxisAlignedBB getCogInteractionBounds() {

    return AABBHelper.create(0, 0, 16, 16, 16, 20);
  }

  public static class InteractionMulch
      extends InteractionItemStack<TileStash> {

    private final TileMechanicalMulchSpreader tile;
    private final Predicate<ItemStack> filter;

    /* package */ InteractionMulch(TileMechanicalMulchSpreader tile, ItemStackHandler stackHandler, Predicate<ItemStack> filter) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 1.0, 0.5),
          Transform.rotate(),
          Transform.scale(0.5, 0.5, 0.5)
      ));
      this.tile = tile;
      this.filter = filter;
    }

    public TileMechanicalMulchSpreader getTile() {

      return this.tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return this.filter.test(itemStack);
    }

    @Override
    public Vec3d getTextOffset(EnumFacing tileFacing, EnumFacing playerHorizontalFacing, EnumFacing sideHit) {

      return new Vec3d(0, 0.1, 0);
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      MechanicalMulchSpreaderInteractionMulchRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      MechanicalMulchSpreaderInteractionMulchRenderer.INSTANCE.renderSolidPassText(this, world, fontRenderer, yaw, offset, pos, blockState, partialTicks);
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return MechanicalMulchSpreaderInteractionMulchRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public static class MulchStackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    private final int maxStacks;
    private final Predicate<ItemStack> filter;

    /* protected */ MulchStackHandler(int maxStacks, Predicate<ItemStack> filter) {

      super(1);
      this.maxStacks = maxStacks;
      this.filter = filter;
    }

    @Override
    public int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return stack.getMaxStackSize() * this.maxStacks;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (!this.filter.test(stack)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }
}
