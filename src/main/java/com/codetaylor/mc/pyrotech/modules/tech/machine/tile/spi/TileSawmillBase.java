package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
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

  private final BladeStackHandler bladeStackHandler;
  private final TileDataBoolean recipeComplete;

  private int counterIdleSound;

  public TileSawmillBase() {

    this.bladeStackHandler = new BladeStackHandler(this);
    this.bladeStackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.recipeComplete = new TileDataBoolean(false);

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.bladeStackHandler),
        this.recipeComplete
    });

    // --- Interactions ---

    this.addInteractions(new IInteraction[]{
        new InteractionBlade<>(this, new ItemStackHandler[]{this.bladeStackHandler})
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
  protected List<ItemStack> getRecipeOutput(E recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    outputItemStacks.add(copy);
    return outputItemStacks;
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
        this.world.setBlockState(pos, ModuleCore.Blocks.PILE_WOOD_CHIPS.setLevel(ModuleCore.Blocks.PILE_WOOD_CHIPS.getDefaultState(), 2));

        // Adjust entity height.
        //noinspection deprecation
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
          //noinspection deprecation
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

    this.recipeComplete.set(true);

    // The call to this method changes the stack in slot 0 and the passed
    // parameter must not be factored out into a local variable or the
    // remaining time will not be properly set after the last item is
    // processed.
    this.onRecipeComplete(this.getInputStackHandler().getStackInSlot(0));
    this.recalculateRemainingTime(this.getInputStackHandler().getStackInSlot(0));

    if (this.shouldDamageBlades()) {
      ItemStack blade = this.bladeStackHandler.extractItem(0, 1, false);

      if (!ModuleTechMachineConfig.isSawbladeIndestructible(blade.getItem())
          && blade.attemptDamageItem(1, this.world.rand, null)) {
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

  private void onRecipeComplete(ItemStack input) {

    E recipe = this.getRecipe(input);

    if (recipe != null) {
      ItemStack copy = input.copy();
      copy.setCount(copy.getCount() - 1);
      this.getInputStackHandler().setStackInSlot(0, copy);

      List<ItemStack> outputItems = this.getRecipeOutput(recipe, input, new ArrayList<>());

      for (ItemStack outputItem : outputItems) {
        this.getOutputStackHandler().insertItem(outputItem, false);
      }

      int woodChips = recipe.getWoodChips();

      if (woodChips > 0) {

        for (int i = 0; i < woodChips; i++) {
          this.trySpawnWoodChips();
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public boolean workerDoWork() {

    this.recipeComplete.set(false);

    return super.workerDoWork();
  }

  @Override
  public void onTileDataUpdate() {

    super.onTileDataUpdate();

    if (ModuleTechMachineConfig.SAWMILL_SOUNDS.RECIPE_COMPLETE_SOUND_ENABLED
        && this.recipeComplete.isDirty()
        && this.recipeComplete.get()) {

      double volume = ModuleTechMachineConfig.SAWMILL_SOUNDS.RECIPE_COMPLETE_SOUND_VOLUME;
      double pitch = (RandomHelper.random().nextDouble() * 2 - 1) * 0.05;
      double select = RandomHelper.random().nextDouble();

      if (this.getInputStackHandler().getStackInSlot(0).isEmpty()) {
        this.world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
            ModuleTechMachine.Sounds.SAWMILL_ACTIVE, SoundCategory.BLOCKS, (float) volume, (float) (1 + pitch), false
        );

      } else if (select < 0.49) {
        this.world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
            ModuleTechMachine.Sounds.SAWMILL_ACTIVE_SHORT_A, SoundCategory.BLOCKS, (float) volume, (float) (1 + pitch), false
        );

      } else if (select < 0.98) {
        this.world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
            ModuleTechMachine.Sounds.SAWMILL_ACTIVE_SHORT_B, SoundCategory.BLOCKS, (float) volume, (float) (1 + pitch), false
        );
      }
    }
  }

  @Override
  public void update() {

    super.update();

    if (this.world.isRemote
        && ModuleTechMachineConfig.SAWMILL_SOUNDS.IDLE_SOUND_ENABLED) {

      if (this.workerIsActive()
          && !this.getBlade().isEmpty()) {

        int idleSoundLengthTicks = 40;

        // is burning fuel and has a blade
        this.counterIdleSound -= 1;

        if (this.counterIdleSound <= 0) {
          this.counterIdleSound = idleSoundLengthTicks;
          double volume = ModuleTechMachineConfig.SAWMILL_SOUNDS.IDLE_SOUND_VOLUME;

          this.world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
              ModuleTechMachine.Sounds.SAWMILL_IDLE, SoundCategory.BLOCKS, (float) volume, 1, false
          );
        }

      } else {
        this.counterIdleSound = 0;
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
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("bladeStackHandler", this.bladeStackHandler.serializeNBT());
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

  public static class InteractionBlade<E extends MachineRecipeBaseSawmill<E>>
      extends InteractionItemStack<TileSawmillBase<E>> {

    private final TileSawmillBase<E> tile;

    /* package */ InteractionBlade(TileSawmillBase<E> tile, ItemStackHandler[] stackHandlers) {

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

    public TileSawmillBase<E> getTile() {

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
