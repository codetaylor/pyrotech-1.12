package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.InteractionTripHammerToolRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileTripHammer
    extends TileCogWorkerBase {

  private final ToolStackHandler toolStackHandler;
  private final TileDataItemStackHandler<ToolStackHandler> tileDataToolStackHandler;

  @SideOnly(Side.CLIENT)
  private double remainingToolAnimationTime;

  @SideOnly(Side.CLIENT)
  private double totalToolAnimationTime;

  public TileTripHammer() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.toolStackHandler = new ToolStackHandler(this);
    this.toolStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    // --- Network ---

    this.tileDataToolStackHandler = new TileDataItemStackHandler<>(this.toolStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataToolStackHandler
    });

    // --- Interactions ---

    this.addInteractions(new IInteraction[]{
        new InteractionTool(this, this.toolStackHandler)
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  public double getRemainingToolAnimationTime() {

    return this.remainingToolAnimationTime;
  }

  @SideOnly(Side.CLIENT)
  public void setRemainingToolAnimationTime(double remainingToolAnimationTime) {

    this.remainingToolAnimationTime = remainingToolAnimationTime;
  }

  @SideOnly(Side.CLIENT)
  public double getTotalToolAnimationTime() {

    return this.totalToolAnimationTime;
  }

  /**
   * Returns true if any of the anvils are configured to be able to use the
   * given tool.
   *
   * @param itemStack the tool
   * @return true if valid tool
   */
  private boolean isValidTool(ItemStack itemStack) {

    return (AnvilRecipe.getTypeFromItemStack(itemStack) != null);
  }

  public ItemStackHandler getToolStackHandler() {

    return this.toolStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Cog
  // ---------------------------------------------------------------------------

  @Override
  protected boolean isValidCog(ItemStack itemStack) {

    return (ModuleTechMachineConfig.TRIP_HAMMER.isValidCog(itemStack.getItem().getRegistryName()));
  }

  @Override
  protected int getUpdateIntervalTicks() {

    return Math.max(ModuleTechMachineConfig.TRIP_HAMMER.INTERVAL_TICKS, 40);
  }

  @Override
  protected boolean isPowered() {

    return this.world.isBlockPowered(this.pos);
  }

  @Override
  protected Transform getCogInteractionTransform() {

    return new Transform(
        Transform.translate(0.0 / 16.0, 8.0 / 16.0, 0.5),
        Transform.rotate(0, 1, 0, 90),
        Transform.scale(0.75, 0.75, 2.00)
    );
  }

  @Override
  protected AxisAlignedBB getCogInteractionBounds() {

    return AABBHelper.create(0, 0, 0, 2, 16, 16);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  protected int doWork(ItemStack cog) {

    // If there's no anvil, don't do work.

    IBlockState blockState = this.world.getBlockState(this.pos);
    EnumFacing tileFacing = this.getTileFacing(this.world, this.pos, blockState);
    BlockPos anvilPos = this.pos.offset(tileFacing);
    TileEntity tileEntity = this.world.getTileEntity(anvilPos);

    if (!(tileEntity instanceof TileAnvilBase)) {
      return -1;
    }

    // If the trip hammer is missing a proper tool, don't do work.

    ItemStack toolItemStack = this.toolStackHandler.getStackInSlot(0);
    AnvilRecipe.EnumType type = AnvilRecipe.getTypeFromItemStack(toolItemStack);

    if (toolItemStack.isEmpty() || type == null) {
      return -1;
    }

    // If the anvil doesn't have an item, don't do work.

    TileAnvilBase tileAnvil = (TileAnvilBase) tileEntity;
    ItemStack inputItemStack = tileAnvil.getStackHandler().getStackInSlot(0);

    if (inputItemStack.isEmpty()) {
      return -1;
    }

    // If there's no recipe for the given input and tool, don't do work.

    AnvilRecipe recipe = AnvilRecipe.getRecipe(inputItemStack, tileAnvil.getRecipeTier(), type);

    if (recipe == null) {
      return -1;
    }

    List<ItemStack> itemStackList = tileAnvil.doInteraction(toolItemStack, null, 0.5f, 6f / 16f, 0.5f);

    if (toolItemStack.attemptDamageItem(1, RandomHelper.random(), null)) {
      // Tool has broken
      this.world.playSound(
          null,
          this.pos.getX(),
          this.pos.getY(),
          this.pos.getZ(),
          SoundEvents.ENTITY_ITEM_BREAK,
          SoundCategory.BLOCKS,
          0.75f,
          (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
      );
      toolItemStack.shrink(1);
      this.toolStackHandler.setStackInSlot(0, toolItemStack);
    }

    if (itemStackList.isEmpty()) {
      // Anvil has broken
      return 1;
    }

    EnumFacing storageFacing = FacingHelper.rotateFacingCW(tileFacing);
    BlockPos storagePos = this.pos.offset(storageFacing);
    TileEntity tileStorage = this.world.getTileEntity(storagePos);

    if (tileStorage == null) {

      for (ItemStack itemStack : itemStackList) {
        StackHelper.spawnStackOnTop(this.world, itemStack, anvilPos, 0);
      }

    } else {
      IItemHandler capability = tileStorage.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, storageFacing.getOpposite());

      if (capability == null) {

        for (ItemStack itemStack : itemStackList) {
          StackHelper.spawnStackOnTop(this.world, itemStack, anvilPos, 0);
        }

      } else {

        for (ItemStack itemStack : itemStackList) {

          for (int i = 0; i < capability.getSlots(); i++) {
            itemStack = capability.insertItem(i, itemStack, false);

            if (itemStack.isEmpty()) {
              break;
            }
          }

          if (!itemStack.isEmpty()) {
            StackHelper.spawnStackOnTop(this.world, itemStack, anvilPos, 0);
          }
        }
      }
    }

    return 1; // Cog Damage
  }

  // ---------------------------------------------------------------------------
  // - Client Animation
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  @Override
  protected void onClientAnimationTriggered(ClientRenderData data) {

    super.onClientAnimationTriggered(data);
    this.totalToolAnimationTime = Math.max(this.getUpdateIntervalTicks(), 80);
    this.remainingToolAnimationTime = this.totalToolAnimationTime;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("toolStackHandler", this.toolStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.toolStackHandler.deserializeNBT(compound.getCompoundTag("toolStackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate() {

    super.onTileDataUpdate();

    if (this.tileDataToolStackHandler.isDirty()
        && this.toolStackHandler.getStackInSlot(0).isEmpty()) {
      this.remainingToolAnimationTime = -1;
    }
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_TRIP_HAMMER;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.TRIP_HAMMER) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return super.getTileFacing(world, pos, blockState);
  }

  public static class InteractionTool
      extends InteractionItemStack<TileTripHammer> {

    private final TileTripHammer tile;

    /* package */ InteractionTool(TileTripHammer tile, ItemStackHandler stackHandler) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          new EnumFacing[]{EnumFacing.NORTH, EnumFacing.UP},
          InteractionBounds.BLOCK,
          new Transform(
              Transform.translate(0.5, 1.25, 0.25),
              Transform.rotate(0, 1, 0, 270).multLocal(Transform.rotate(0, 0, 1, 45)),
              Transform.scale(1, 1, 1)
          )
      );

      this.tile = tile;
    }

    public TileTripHammer getTile() {

      return this.tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (this.tile.isValidTool(itemStack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      InteractionTripHammerToolRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return InteractionTripHammerToolRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Handlers
  // ---------------------------------------------------------------------------

  private static class ToolStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final TileTripHammer tile;

    public ToolStackHandler(TileTripHammer tile) {

      super(1);
      this.tile = tile;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-blade items.
      if (!this.tile.isValidTool(stack)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

}
