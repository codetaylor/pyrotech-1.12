package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.MillInteractionBladeRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMillBlade;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.MillStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileMillStone
    extends TileCombustionWorkerStoneItemInItemOutBase<MillStoneRecipe> {

  private BladeStackHandler bladeStackHandler;

  public TileMillStone() {

    this.bladeStackHandler = new BladeStackHandler();
    this.bladeStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
    });

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.bladeStackHandler)
    });

    // --- Interactions ---

    this.addInteractions(new IInteraction[]{
        new InteractionBlade(this, new ItemStackHandler[]{this.bladeStackHandler})
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public BladeStackHandler getBladeStackHandler() {

    return this.bladeStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.bladeStackHandler.deserializeNBT(compound.getCompoundTag("bladeStackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("bladeStackHandler", this.bladeStackHandler.serializeNBT());
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Tile Combustion Worker Stone
  // ---------------------------------------------------------------------------

  @Override
  public void dropContents() {

    super.dropContents();
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.bladeStackHandler, this.pos);
  }

  @Override
  public MillStoneRecipe getRecipe(ItemStack itemStack) {

    return MillStoneRecipe.getRecipe(itemStack, this.bladeStackHandler.getStackInSlot(0));
  }

  @Override
  protected List<ItemStack> getRecipeOutput(MillStoneRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(copy.getCount() * input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModulePyrotechConfig.STONE_MILL.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModulePyrotechConfig.STONE_MILL.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModulePyrotechConfig.STONE_MILL.FUEL_SLOT_SIZE;
  }

  @Override
  protected void onRecipeComplete() {

    if (!ModulePyrotechConfig.STONE_MILL.DAMAGE_BLADES) {
      super.onRecipeComplete();

    } else {
      ItemStack input = this.getInputStackHandler().getStackInSlot(0);

      super.onRecipeComplete();

      ItemStack blade = this.bladeStackHandler.extractItem(0, 1, false);

      if (blade.attemptDamageItem(input.getCount(), this.world.rand, null)) {
        this.world.playSound(
            null,
            this.pos,
            SoundEvents.ENTITY_ITEM_BREAK,
            SoundCategory.BLOCKS,
            1.0F,
            Util.RANDOM.nextFloat() * 0.4F + 0.8F
        );

      } else {
        this.bladeStackHandler.insertItem(0, blade, false);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.UP};
  }

  @Override
  protected AxisAlignedBB getInputInteractionBoundsTop() {

    return new AxisAlignedBB(1f / 16f, 1, 1f / 16f, 15f / 16f, 20f / 16f, 15f / 16f);
  }

  public class InteractionBlade
      extends InteractionItemStack<TileMillStone> {

    private final TileMillStone tile;

    /* package */ InteractionBlade(TileMillStone tile, ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.UP},
          new AxisAlignedBB(1f / 16f, 1, 1f / 16f, 15f / 16f, 20f / 16f, 15f / 16f),
          new Transform(
              Transform.translate(0.5, 1, 0.5),
              Transform.rotate(0, 1, 0, 90),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
      this.tile = tile;
    }

    @Override
    protected void onExtract(World world, EntityPlayer player, BlockPos pos) {

      super.onExtract(world, player, pos);

      if (this.tile.workerIsActive()
          && ModulePyrotechConfig.STONE_MILL.ENTITY_DAMAGE_FROM_BLADE > 0) {
        player.attackEntityFrom(DamageSource.GENERIC, ModulePyrotechConfig.STONE_MILL.ENTITY_DAMAGE_FROM_BLADE);
      }
    }

    @Override
    protected void onInsert(ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(itemStack, world, player, pos);

      if (this.tile.workerIsActive()
          && ModulePyrotechConfig.STONE_MILL.ENTITY_DAMAGE_FROM_BLADE > 0) {
        player.attackEntityFrom(DamageSource.GENERIC, ModulePyrotechConfig.STONE_MILL.ENTITY_DAMAGE_FROM_BLADE);
      }
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (itemStack.getItem() instanceof ItemMillBlade);
    }

    public TileMillStone getTile() {

      return this.tile;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      MillInteractionBladeRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return MillInteractionBladeRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class BladeStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ BladeStackHandler() {

      super(1);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-blade items.

      if (!(stack.getItem() instanceof ItemMillBlade)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

}
