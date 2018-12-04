package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileWoodRack
    extends TileNetBase
    implements ITileInteractable {

  private StackHandler stackHandler;

  private IInteraction[] interactions;

  public TileWoodRack() {

    // --- Init ---

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.stackHandler = new StackHandler(9);
    this.stackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.registerTileData(new ITileData[]{
        new TileDataItemStackHandler<>(this.stackHandler)
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 0),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 1),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 2),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 3),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 4),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 5),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 6),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 7),
        new Interaction(new ItemStackHandler[]{this.stackHandler}, 8)
    };
  }

  public void dropContents() {

    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.stackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public StackHandler getStackHandler() {

    return this.stackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setTag("stackHandler", this.stackHandler.serializeNBT());

    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private static class Interaction
      extends InteractionItemStack {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double ONE_SIXTH = 1.0 / 6.0;

    public Interaction(ItemStackHandler[] stackHandlers, int slot) {

      super(stackHandlers, slot, new EnumFacing[]{EnumFacing.UP}, Interaction.createInteractionBounds(slot), Interaction.createTransform(slot));
    }

    private static Transform createTransform(int slot) {

      int x = slot % 3;
      int z = slot / 3;

      return new Transform(
          Transform.translate(x * ONE_THIRD + ONE_SIXTH, 8f / 16f, z * ONE_THIRD + ONE_SIXTH),
          Transform.rotate(),
          Transform.scale(ONE_THIRD, 12.0 / 16.0, ONE_THIRD)
      );
    }

    private static AxisAlignedBB createInteractionBounds(int slot) {

      int x = slot % 3;
      int z = slot / 3;

      return new AxisAlignedBB(x * ONE_THIRD, 2f / 16f, z * ONE_THIRD, x * ONE_THIRD + ONE_THIRD, 14f / 16f, z * ONE_THIRD + ONE_THIRD);
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return OreDictHelper.contains("logWood", itemStack);
    }

    @Override
    protected void onInsert(ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(itemStack, world, player, pos);

      if (!world.isRemote) {
        world.playSound(
            null,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundCategory.BLOCKS,
            0.75f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );
      }
    }
  }

  private class StackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public StackHandler(int size) {

      super(size);
    }
  }
}
