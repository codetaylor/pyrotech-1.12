package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.ITileData;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.ITileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.TileDataContainerBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.data.TileDataItemStackHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileWoodRack
    extends TileDataContainerBase
    implements ITileInteractable {

  private TileDataItemStackHandler<StackHandler> dataStackHandler;
  private StackHandler stackHandler;

  private IInteraction[] interactions;

  public TileWoodRack() {

    this.stackHandler = new StackHandler(9);
    this.stackHandler.addObserver((handler, slot) -> this.markDirty());

    this.dataStackHandler = new TileDataItemStackHandler<>(this.stackHandler);
    ModulePyrotech.TILE_DATA_SERVICE.register(this);

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

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate(ITileData data) {
    //
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

  private class Interaction
      extends InteractionItemStack {

    public Interaction(ItemStackHandler[] stackHandlers, int slot) {

      super(stackHandlers, slot, EnumFacing.VALUES, InteractionBounds.INFINITE, new Transform(
          Transform.translate(),
          Transform.rotate(),
          Transform.scale()
      ));
    }

    @Override
    public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, float partialTicks) {

      int x = this.slot & 1;
      int y = (this.slot >> 1) & 1;

      return new Transform(
          Transform.translate(4f / 16f + (x * 8f / 16f), 6f / 16f + (y * 8f / 16f), 0.5),
          Transform.rotate(1, 0, 0, 90),
          Transform.scale(8f / 16f, 1, 8f / 16f)
      );
    }
  }

  private class StackHandler
      extends LIFOStackHandler
      implements ITileDataItemStackHandler {

    public StackHandler(int size) {

      super(size);
    }
  }
}
