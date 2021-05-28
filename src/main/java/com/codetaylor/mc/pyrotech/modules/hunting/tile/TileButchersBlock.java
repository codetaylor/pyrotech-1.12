package com.codetaylor.mc.pyrotech.modules.hunting.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileButchersBlock
    extends TileEntityDataBase
    implements ITileInteractable {

  private final InputStackHandler inputStackHandler;

  private final IInteraction[] interactions;

  private AxisAlignedBB renderBounds;

  public TileButchersBlock() {

    super(ModuleCore.TILE_DATA_SERVICE);

    this.inputStackHandler = new InputStackHandler();

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InputInteraction(this.inputStackHandler)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputItemStack", this.inputStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputItemStack"));
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
  public IInteraction[] getInteractions() {

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
