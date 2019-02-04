package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneHopper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.StoneHopperInteractionCogRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemCog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileStoneHopper
    extends TileNetBase
    implements ITickable,
    ITileInteractable {

  private CogStackHandler cogStackHandler;
  private TickCounter updateTickCounter;
  private TileDataBoolean transferred;
  private IInteraction[] interactions;
  private final TileDataItemStackHandler<CogStackHandler> tileDataItemStackHandler;

  @SideOnly(Side.CLIENT)
  private ClientRenderData clientRenderData = new ClientRenderData();

  public TileStoneHopper() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.updateTickCounter = new TickCounter(getTransferIntervalTicks());
    this.cogStackHandler = new CogStackHandler();
    this.cogStackHandler.addObserver((handler, slot) -> {
      this.updateTickCounter.reset();
      this.markDirty();
    });

    this.transferred = new TileDataBoolean(false);

    // --- Network ---

    this.tileDataItemStackHandler = new TileDataItemStackHandler<>(this.cogStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        tileDataItemStackHandler,
        this.transferred
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionCog(this, this.cogStackHandler)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  public ClientRenderData getClientRenderData() {

    return this.clientRenderData;
  }

  public CogStackHandler getCogStackHandler() {

    return this.cogStackHandler;
  }

  private int getTransferAmount(ItemStack cog) {

    return ModuleTechMachineConfig.STONE_HOPPER.getCogTransferAmount(cog.getItem().getRegistryName());
  }

  private int getTransferIntervalTicks() {

    return ModuleTechMachineConfig.STONE_HOPPER.TRANSFER_INTERVAL_TICKS;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("cogStackHandler", this.cogStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.cogStackHandler.deserializeNBT(compound.getCompoundTag("cogStackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate() {

    if (this.transferred.isDirty()
        && this.transferred.get()) {
      this.clientRenderData.totalAnimationTime = this.getTransferIntervalTicks();
      this.clientRenderData.remainingAnimationTime = this.clientRenderData.totalAnimationTime;
      this.clientRenderData.cogRotationStage = (this.clientRenderData.cogRotationStage + 1) % 8;
    }

    if (this.tileDataItemStackHandler.isDirty()
        && this.cogStackHandler.getStackInSlot(0).isEmpty()) {
      this.clientRenderData.remainingAnimationTime = -1;
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    ItemStack cog = this.cogStackHandler.getStackInSlot(0);

    if (cog.isEmpty()) {
      return;
    }

    if (this.updateTickCounter != null
        && this.updateTickCounter.increment()) {

      TileEntity tileSource = this.world.getTileEntity(this.pos.up());
      IBlockState blockState = this.world.getBlockState(this.pos);
      EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
      BlockStoneHopper.EnumType type = blockState.getValue(BlockStoneHopper.TYPE);

      if (type == BlockStoneHopper.EnumType.Down) {
        facing = EnumFacing.UP;
      }

      TileEntity tileTarget = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

      if (tileSource == null || tileTarget == null) {
        return;
      }

      IItemHandler handlerSource = tileSource.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
      IItemHandler handlerTarget = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

      if (handlerSource == null || handlerTarget == null) {
        return;
      }

      int amount = Math.min(cog.getMaxDamage() - cog.getItemDamage(), this.getTransferAmount(cog));
      int totalInserted = 0;

      for (int slotSource = 0; slotSource < handlerSource.getSlots(); slotSource++) {

        boolean canTransfer = false;

        {
          ItemStack stackSource = handlerSource.extractItem(slotSource, amount, true);
          int initialCount = stackSource.getCount();

          if (stackSource != ItemStack.EMPTY) {

            for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
              int before = stackSource.getCount();
              stackSource = handlerTarget.insertItem(slotTarget, stackSource, true);
              totalInserted += (before - stackSource.getCount());

              if (stackSource.isEmpty()) {
                canTransfer = true;
                break;

              } else if (stackSource.getCount() != initialCount) {
                canTransfer = true;
              }
            }
          }
        }

        if (canTransfer) {

          // --- Transfer

          ItemStack stackSource = handlerSource.extractItem(slotSource, totalInserted, false);
          int initialCount = stackSource.getCount();

          for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
            stackSource = handlerTarget.insertItem(slotTarget, stackSource, false);

            if (stackSource.isEmpty()) {
              break;
            }
          }

          // --- Play Sound

          SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f);

          // --- Damage Cog

          ItemStack actualCog = this.cogStackHandler.extractItem(0, 1, false);
          int cogDamage = 1;

          if (ModuleTechMachineConfig.STONE_HOPPER.COG_DAMAGE_TYPE == ModuleTechMachineConfig.StoneHopper.EnumCogDamageType.PerItem) {
            cogDamage = (initialCount - stackSource.getCount());
          }

          if (actualCog.attemptDamageItem(cogDamage, RandomHelper.random(), null)
              || actualCog.getItemDamage() == actualCog.getMaxDamage()) {

            SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS);

          } else {
            this.cogStackHandler.insertItem(0, actualCog, false);
          }

          this.transferred.set(true);
          break;

        } else {
          this.transferred.set(false);
        }
      }

    } else {
      this.transferred.set(false);
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.STONE_HOPPER) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  public class InteractionCog
      extends InteractionItemStack<TileStoneHopper> {

    private final TileStoneHopper tile;

    /* package */ InteractionCog(TileStoneHopper tile, ItemStackHandler stackHandler) {

      super(
          new ItemStackHandler[]{stackHandler},
          0,
          EnumFacing.VALUES,
          AABBHelper.create(0, 0, 0, 16, 16, 4),
          new Transform(
              Transform.translate(0.5, 7.0 / 16.0, 2.0 / 16.0),
              Transform.rotate(),
              Transform.scale(0.75, 0.75, 2.00)
          )
      );
      this.tile = tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (itemStack.getItem() instanceof ItemCog);
    }

    public TileStoneHopper getTile() {

      return this.tile;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      StoneHopperInteractionCogRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return StoneHopperInteractionCogRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  public class CogStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ CogStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-cog items.

      ResourceLocation registryName = stack.getItem().getRegistryName();

      if (registryName == null) {
        return stack;
      }

      int transferAmount = ModuleTechMachineConfig.STONE_HOPPER.getCogTransferAmount(registryName);

      if (transferAmount == -1) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  // ---------------------------------------------------------------------------
  // - Client Render Data
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  public static class ClientRenderData {

    public double remainingAnimationTime;
    public double totalAnimationTime;
    public int cogRotationStage;
  }
}
