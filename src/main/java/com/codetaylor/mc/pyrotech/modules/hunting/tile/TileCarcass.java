package com.codetaylor.mc.pyrotech.modules.hunting.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCarcass
    extends TileEntityDataBase
    implements ITileInteractable {

  private final StackHandler stackHandler;
  private final TileDataFloat currentProgress;
  private final TileDataFloat totalProgress;

  private final IInteraction<?>[] interactions;

  public TileCarcass() {

    super(ModuleCore.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.currentProgress = new TileDataFloat(0);
    this.totalProgress = new TileDataFloat(0);

    this.stackHandler = new StackHandler();
    this.stackHandler.addObserver((handler, slot) -> {
      this.resetProgress();
      this.markDirty();
    });

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.stackHandler),
        this.currentProgress,
        this.totalProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionCarcass<TileCarcass>(EnumFacing.VALUES, InteractionBounds.BLOCK, new InteractionInteractionCarcassDelegate(this))
    };

    this.resetProgress();
  }

  private void resetProgress() {

    int progressRequired = ModuleHuntingConfig.CARCASS.TOTAL_PROGRESS_REQUIRED;
    float adjustment = RandomHelper.random().nextFloat() * 0.2f - 0.1f;
    this.currentProgress.set(Math.max(1, progressRequired + progressRequired * adjustment));
    this.totalProgress.set(this.currentProgress.get());
  }

  // ---------------------------------------------------------------------------
  // - ICarcassAccessor
  // ---------------------------------------------------------------------------

  private static class InteractionInteractionCarcassDelegate
      implements InteractionCarcass.IInteractionCarcassDelegate {

    private final TileCarcass tile;

    public InteractionInteractionCarcassDelegate(TileCarcass tile) {

      this.tile = tile;
    }

    @Override
    public boolean canUseWithHungerLevel(int playerFoodLevel) {

      return playerFoodLevel < ModuleHuntingConfig.CARCASS.MINIMUM_HUNGER_TO_USE;
    }

    @Override
    public boolean canUseWithHeldItem(String registryName) {

      return ArrayHelper.contains(ModuleHuntingConfig.CARCASS.ALLOWED_KNIVES, registryName);
    }

    @Override
    public void doExhaustion(EntityPlayer player) {

      if (ModuleHuntingConfig.CARCASS.EXHAUSTION_COST_PER_KNIFE_USE > 0) {
        player.addExhaustion((float) ModuleHuntingConfig.CARCASS.EXHAUSTION_COST_PER_KNIFE_USE);
      }
    }

    @Override
    public int getItemEfficiency(String registryName) {

      return ModuleHuntingConfig.CARCASS.KNIFE_EFFICIENCY.getOrDefault(registryName, 1);
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

      int slot = this.tile.getFirstNonEmptySlot();
      return this.tile.stackHandler.extractItem(slot, 1, false);
    }

    @Override
    public BlockPos getPosition() {

      return this.tile.getPos();
    }

    @Override
    public boolean isEmpty() {

      return (this.tile.stackHandler.getTotalItemCount() == 0);
    }

    @Override
    public void destroyCarcass() {

      this.tile.world.destroyBlock(this.tile.getPos(), false);
    }

    @Override
    public void resetProgress() {

      this.tile.resetProgress();
    }
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public float getCurrentProgress() {

    return 1.0f - this.currentProgress.get() / this.totalProgress.get();
  }

  public ItemStack getNextItem() {

    ItemStack itemStack = this.stackHandler.getFirstNonEmptyItemStack().copy();
    itemStack.setCount(1);
    return itemStack;
  }

  private int getFirstNonEmptySlot() {

    for (int i = 0; i < this.stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = this.stackHandler.getStackInSlot(i);

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
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleHuntingConfig.STAGES_CARCASS;
  }

  @Override
  public IInteraction<?>[] getInteractions() {

    return this.interactions;
  }

  // ---------------------------------------------------------------------------
  // - Stack Handler
  // ---------------------------------------------------------------------------

  private static class StackHandler
      extends DynamicStackHandler
      implements ITileDataItemStackHandler {

    /* package */ StackHandler() {

      super(1);
    }
  }

}
