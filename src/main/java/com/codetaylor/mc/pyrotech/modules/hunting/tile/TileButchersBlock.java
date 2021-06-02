package com.codetaylor.mc.pyrotech.modules.hunting.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemBlockCarcass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileButchersBlock
    extends TileEntityDataBase
    implements ITileInteractable {

  private final InputStackHandler inputStackHandler;
  private final TileDataFloat currentProgress;
  private final TileDataFloat totalProgress;

  private final IInteraction<?>[] interactions;

  private AxisAlignedBB renderBounds;

  public TileButchersBlock() {

    super(ModuleCore.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.currentProgress = new TileDataFloat(0);
    this.totalProgress = new TileDataFloat(0);

    this.inputStackHandler = new InputStackHandler();
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.resetProgress();
      this.markDirty();
    });

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler),
        this.currentProgress,
        this.totalProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InputInteraction(this.inputStackHandler),
        new InteractionCarcass<TileButchersBlock>(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK, new InteractionInteractionCarcassDelegate(this))
    };
  }

  private void resetProgress() {

    int progressRequired = ModuleHuntingConfig.BUTCHERS_BLOCK.TOTAL_PROGRESS_REQUIRED;
    float adjustment = RandomHelper.random().nextFloat() * 0.2f - 0.1f;
    this.currentProgress.set(Math.max(1, progressRequired + progressRequired * adjustment));
    this.totalProgress.set(this.currentProgress.get());
  }

  // ---------------------------------------------------------------------------
  // - ICarcassAccessor
  // ---------------------------------------------------------------------------

  private static class InteractionInteractionCarcassDelegate
      implements InteractionCarcass.IInteractionCarcassDelegate {

    private final TileButchersBlock tile;

    public InteractionInteractionCarcassDelegate(TileButchersBlock tile) {

      this.tile = tile;
    }

    @Override
    public boolean canUseWithHungerLevel(int playerFoodLevel) {

      return !this.tile.inputStackHandler.getStackInSlot(0).isEmpty()
          && (playerFoodLevel < ModuleHuntingConfig.BUTCHERS_BLOCK.MINIMUM_HUNGER_TO_USE);
    }

    @Override
    public boolean canUseWithHeldItem(String registryName) {

      return !this.tile.inputStackHandler.getStackInSlot(0).isEmpty()
          && ArrayHelper.contains(ModuleHuntingConfig.BUTCHERS_BLOCK.ALLOWED_KNIVES, registryName);
    }

    @Override
    public void doExhaustion(EntityPlayer player) {

      if (ModuleHuntingConfig.BUTCHERS_BLOCK.EXHAUSTION_COST_PER_KNIFE_USE > 0) {
        player.addExhaustion((float) ModuleHuntingConfig.BUTCHERS_BLOCK.EXHAUSTION_COST_PER_KNIFE_USE);
      }
    }

    @Override
    public int getItemEfficiency(String registryName) {

      return ModuleHuntingConfig.BUTCHERS_BLOCK.KNIFE_EFFICIENCY.getOrDefault(registryName, 1);
    }

    @Override
    public void setCurrentProgress(float value) {

      this.tile.currentProgress.set(value);
    }

    @Override
    public float getCurrentProgress() {

      return this.tile.currentProgress.get();
    }

    @Override
    public ItemStack extractItem() {

      ItemStack itemStack = this.tile.inputStackHandler.getStackInSlot(0);
      ItemStackHandler itemStackHandler = ItemBlockCarcass.getItemStackHandler(itemStack);

      if (itemStackHandler == null) {
        return ItemStack.EMPTY;
      }

      int slot = this.tile.getFirstNonEmptySlot(itemStackHandler);
      ItemStack result = itemStackHandler.extractItem(slot, 1, false);
      ItemBlockCarcass.updateItemStackHandler(itemStack, itemStackHandler);
      return result;
    }

    @Override
    public BlockPos getPosition() {

      return this.tile.getPos();
    }

    @Override
    public boolean isEmpty() {

      ItemStack itemStack = this.tile.inputStackHandler.getStackInSlot(0);

      if (itemStack.isEmpty()) {
        return true;
      }

      ItemStackHandler itemStackHandler = ItemBlockCarcass.getItemStackHandler(itemStack);

      if (itemStackHandler == null) {
        return true;
      }

      return (this.tile.getFirstNonEmptySlot(itemStackHandler) == -1);
    }

    @Override
    public void destroyCarcass() {

      this.tile.inputStackHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public void resetProgress() {

      this.tile.resetProgress();
    }
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  private int getFirstNonEmptySlot(ItemStackHandler stackHandler) {

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        return i;
      }
    }
    return -1;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    if (this.renderBounds == null) {
      this.renderBounds = new AxisAlignedBB(this.getPos()).expand(0, 0.5, 0);
    }

    return this.renderBounds;
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

    return ModuleHuntingConfig.STAGES_BUTCHERS_BLOCK;
  }

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  public static class InputInteraction
      extends InteractionItemStack<TileButchersBlock> {

    public InputInteraction(ItemStackHandler stackHandler) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.BLOCK, new Transform(
          Transform.translate(0.5, 1.375, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return !itemStack.isEmpty()
          && itemStack.getItem() instanceof ItemBlock
          && ((ItemBlock) itemStack.getItem()).getBlock() == ModuleHunting.Blocks.CARCASS;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private static class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate) {

      if (!itemStack.isEmpty()
          && itemStack.getItem() instanceof ItemBlock
          && ((ItemBlock) itemStack.getItem()).getBlock() == ModuleHunting.Blocks.CARCASS) {
        return super.insertItem(slot, itemStack, simulate);
      }

      return itemStack;
    }
  }
}
