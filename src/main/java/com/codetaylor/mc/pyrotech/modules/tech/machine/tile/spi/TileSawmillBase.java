package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.MillInteractionBladeRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBaseSawmill;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
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
import java.util.Collections;
import java.util.List;

public abstract class TileSawmillBase<E extends MachineRecipeBaseSawmill<E>>
    extends TileCombustionWorkerStoneItemInItemOutBase<E> {

  private BladeStackHandler bladeStackHandler;
  private int remainingWoodChips;

  public TileSawmillBase() {

    this.bladeStackHandler = new BladeStackHandler(this);
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

  protected ItemStack getBlade() {

    return this.bladeStackHandler.getStackInSlot(0);
  }

  protected abstract boolean isValidSawmillBlade(ItemStack itemStack);

  protected abstract boolean shouldDamageBlades();

  public abstract double getEntityDamageFromBlade();

  public abstract E getRecipe(ItemStack itemStack);

  // ---------------------------------------------------------------------------
  // - Contents
  // ---------------------------------------------------------------------------

  @Override
  public void dropContents() {

    super.dropContents();
    StackHelper.spawnStackHandlerContentsOnTop(this.world, this.bladeStackHandler, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  @Override
  protected void recalculateRemainingTime(ItemStack itemStack) {

    super.recalculateRemainingTime(itemStack);

    if (itemStack.isEmpty()) {
      this.remainingWoodChips = 0;

    } else {
      E recipe = this.getRecipe(itemStack);

      if (recipe != null) {
        this.remainingWoodChips = recipe.getWoodChips();

      } else {
        this.remainingWoodChips = 0;
      }
    }
  }

  @Override
  protected List<ItemStack> getRecipeOutput(E recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(copy.getCount() * input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }

  @Override
  protected void reduceRecipeTime() {

    super.reduceRecipeTime();

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    E recipe = this.getRecipe(input);

    if (recipe != null) {
      int woodChips = recipe.getWoodChips();

      if (woodChips > 0) {
        float nextInterval = (woodChips - this.remainingWoodChips) * (1f / woodChips) + (1f / woodChips) * 0.5f;

        if (this.workerGetProgress(0) >= nextInterval) {
          this.remainingWoodChips -= 1;
          this.trySpawnWoodChips();
        }
      }
    }
  }

  private void trySpawnWoodChips() {

    List<BlockPos> candidates = new ArrayList<>();

    BlockHelper.forBlocksInCube(this.world, this.getPos(), 1, 1, 1, (w, p, bs) -> {

      if (w.isAirBlock(p)
          && ModuleCore.Blocks.ROCK.canPlaceBlockAt(w, p)
          || (bs.getBlock() == ModuleCore.Blocks.ROCK && bs.getValue(BlockRock.VARIANT) == BlockRock.EnumType.WOOD_CHIPS)
          || bs.getBlock() == ModuleCore.Blocks.PILE_WOOD_CHIPS) {

        candidates.add(p);
      }

      return true;
    });

    if (candidates.size() > 0) {
      Collections.shuffle(candidates);

      BlockPos pos = candidates.get(0);

      IBlockState blockState = this.world.getBlockState(pos);
      Block block = blockState.getBlock();

      if (block == ModuleCore.Blocks.ROCK
          && blockState.getValue(BlockRock.VARIANT) == BlockRock.EnumType.WOOD_CHIPS) {

        // If wood chips already exist, start a pile.
        this.world.setBlockState(pos, ModuleCore.Blocks.PILE_WOOD_CHIPS.setLevel(ModuleCore.Blocks.PILE_WOOD_CHIPS.getDefaultState(), 1));

        // Adjust entity height.
        AxisAlignedBB boundingBox = block.getBoundingBox(blockState, this.world, pos);
        AxisAlignedBB offsetBoundingBox = new AxisAlignedBB(boundingBox.minX, 1.0 - boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, 1.0, boundingBox.maxZ).offset(pos);

        for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(null, offsetBoundingBox)) {
          entity.setPositionAndUpdate(entity.posX, entity.posY + (2.0 / 16.0) + 0.001D, entity.posZ);
        }

      } else if (block == ModuleCore.Blocks.PILE_WOOD_CHIPS) {
        int level = ModuleCore.Blocks.PILE_WOOD_CHIPS.getLevel(blockState);

        if (level < 8) {
          this.world.setBlockState(pos, ModuleCore.Blocks.PILE_WOOD_CHIPS.setLevel(ModuleCore.Blocks.PILE_WOOD_CHIPS.getDefaultState(), level + 1));

          // Adjust entity height.
          AxisAlignedBB boundingBox = block.getBoundingBox(blockState, this.world, pos);
          AxisAlignedBB offsetBoundingBox = new AxisAlignedBB(boundingBox.minX, 1.0 - boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, 1.0, boundingBox.maxZ).offset(pos);

          for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(null, offsetBoundingBox)) {
            entity.setPositionAndUpdate(entity.posX, entity.posY + (2.0 / 16.0) + 0.001D, entity.posZ);
          }
        }

      } else {

        this.world.setBlockState(pos, ModuleCore.Blocks.ROCK.getDefaultState()
            .withProperty(BlockRock.VARIANT, BlockRock.EnumType.WOOD_CHIPS));
      }
    }
  }

  @Override
  protected void onRecipeComplete() {

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);

    if (!this.shouldDamageBlades()) {
      super.onRecipeComplete();

    } else {

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
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.bladeStackHandler.deserializeNBT(compound.getCompoundTag("bladeStackHandler"));
    this.remainingWoodChips = compound.getInteger("remainingWoodChips");
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("bladeStackHandler", this.bladeStackHandler.serializeNBT());
    compound.setInteger("remainingWoodChips", this.remainingWoodChips);
    return compound;
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
      extends InteractionItemStack<TileSawmillBase> {

    private final TileSawmillBase tile;

    /* package */ InteractionBlade(TileSawmillBase tile, ItemStackHandler[] stackHandlers) {

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
    protected void onExtract(EnumType type, World world, EntityPlayer player, BlockPos pos) {

      super.onExtract(type, world, player, pos);

      if (this.tile.workerIsActive()
          && this.tile.getEntityDamageFromBlade() > 0) {
        player.attackEntityFrom(DamageSource.GENERIC, (float) this.tile.getEntityDamageFromBlade());
      }
    }

    @Override
    protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(type, itemStack, world, player, pos);

      if (this.tile.workerIsActive()
          && this.tile.getEntityDamageFromBlade() > 0) {
        player.attackEntityFrom(DamageSource.GENERIC, (float) this.tile.getEntityDamageFromBlade());
      }
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return this.tile.isValidSawmillBlade(itemStack);
    }

    public TileSawmillBase getTile() {

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

    private final TileSawmillBase<E> tile;

    /* package */ BladeStackHandler(TileSawmillBase<E> tile) {

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
      if (!this.tile.isValidSawmillBlade(stack)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

}
