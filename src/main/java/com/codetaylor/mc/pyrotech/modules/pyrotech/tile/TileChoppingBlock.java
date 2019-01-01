package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockChoppingBlock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.ChoppingBlockRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileChoppingBlock
    extends TileNetBase
    implements ITileInteractable {

  private InputStackHandler stackHandler;
  private TileDataFloat recipeProgress;

  private int sawdust;
  private int durabilityUntilNextDamage;

  private IInteraction[] interactions;
  private AxisAlignedBB renderBounds;

  public TileChoppingBlock() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.stackHandler = new InputStackHandler();
    this.stackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.markDirty();
    });

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.stackHandler),
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new Interaction(new ItemStackHandler[]{this.stackHandler}),
        new InteractionShovel(),
        new InteractionChop()
    };
    this.durabilityUntilNextDamage = ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_PER_DAMAGE;
  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void setSawdust(int sawdust) {

    // This requires a full update because it is used for actual state.

    this.sawdust = Math.max(0, Math.min(5, sawdust));
    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  public int getSawdust() {

    return this.sawdust;
  }

  public void setDamage(int damage) {

    this.world.setBlockState(this.pos, ModuleBlocks.CHOPPING_BLOCK.getDefaultState()
        .withProperty(BlockChoppingBlock.DAMAGE, damage), 3);
  }

  public int getDamage() {

    return this.world.getBlockState(this.pos).getValue(BlockChoppingBlock.DAMAGE);
  }

  public void setDurabilityUntilNextDamage(int durabilityUntilNextDamage) {

    // TODO: Network
    // This doesn't require a full update.

    this.durabilityUntilNextDamage = durabilityUntilNextDamage;
    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  public int getDurabilityUntilNextDamage() {

    return this.durabilityUntilNextDamage;
  }

  public void setRecipeProgress(float recipeProgress) {

    this.recipeProgress.set(recipeProgress);
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setInteger("sawdust", this.sawdust);
    compound.setInteger("durabilityUntilNextDamage", this.durabilityUntilNextDamage);
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.sawdust = compound.getInteger("sawdust");
    this.durabilityUntilNextDamage = compound.getInteger("durabilityUntilNextDamage");
    this.recipeProgress.set(compound.getFloat("recipeProgress"));
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
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class Interaction
      extends InteractionItemStack<TileChoppingBlock> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, new EnumFacing[]{EnumFacing.UP}, BlockChoppingBlock.AABB, new Transform(
          Transform.translate(0.5, 0.75, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (ChoppingBlockRecipe.getRecipe(itemStack) != null);
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
            0.5f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );
      }
    }
  }

  private class InteractionShovel
      extends InteractionUseItemBase<TileChoppingBlock> {

    /* package */ InteractionShovel() {

      super(EnumFacing.VALUES, BlockChoppingBlock.AABB);
    }

    @Override
    protected boolean allowInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItemStack = player.getHeldItem(hand);

      return tile.getSawdust() > 0
          && heldItemStack.getItem().getToolClasses(heldItemStack).contains("shovel");
    }

    @Override
    protected boolean doInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (!world.isRemote) {
        tile.setSawdust(tile.getSawdust() - 1);
        StackHelper.spawnStackOnTop(world, new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta()), hitPos, 0);
        heldItem.damageItem(1, player);
        world.playSound(null, hitPos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1, 1);
      }

      return true;
    }
  }

  private class InteractionChop
      extends InteractionUseItemBase<TileChoppingBlock> {

    /* package */ InteractionChop() {

      super(new EnumFacing[]{EnumFacing.UP}, BlockChoppingBlock.AABB);
    }

    @Override
    protected boolean allowInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();

      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      String registryName = resourceLocation.toString();

      if (heldItem.getToolClasses(heldItemStack).contains("axe")) {
        return !ArrayHelper.contains(ModulePyrotechConfig.CHOPPING_BLOCK.AXE_BLACKLIST, registryName);

      } else {
        return ArrayHelper.contains(ModulePyrotechConfig.CHOPPING_BLOCK.AXE_WHITELIST, registryName);
      }
    }

    @Override
    protected boolean doInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Server logic

        // Decrement the chopping block's damage and reset the chops
        // remaining until next damage. If the damage reaches the threshold,
        // destroy the block and drop its contents.

        if (tile.getDurabilityUntilNextDamage() <= 1) {

          tile.setDurabilityUntilNextDamage(ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_PER_DAMAGE);

          if (tile.getDamage() + 1 < 6) {
            tile.setDamage(tile.getDamage() + 1);

          } else {
            StackHelper.spawnStackHandlerContentsOnTop(world, tile.getStackHandler(), tile.getPos());
            world.destroyBlock(tile.getPos(), false);
            return true;
          }
        }

        // Play sound for chop. The place sound sounds better.
        world.playSound(
            null,
            player.posX,
            player.posY,
            player.posZ,
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundCategory.BLOCKS,
            0.75f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );

        { // START: Wood Chips

          // 2x the chance to place chips on the block.
          if (tile.getSawdust() < 5
              && Math.random() < ModulePyrotechConfig.CHOPPING_BLOCK.WOOD_CHIPS_CHANCE * 2) {

            tile.setSawdust(tile.getSawdust() + 1);
          }

          List<BlockPos> candidates = new ArrayList<>();

          // 1/2 the chance to place chips around the block.
          if (Math.random() < ModulePyrotechConfig.CHOPPING_BLOCK.WOOD_CHIPS_CHANCE * 0.5) {
            BlockHelper.forBlocksInCube(world, tile.getPos(), 1, 1, 1, (w, p, bs) -> {

              if (w.isAirBlock(p)
                  && ModuleBlocks.ROCK.canPlaceBlockAt(w, p)
                  && bs.getBlock() != ModuleBlocks.ROCK) {

                candidates.add(p);
              }

              return true;
            });
          }

          if (candidates.size() > 0) {
            Collections.shuffle(candidates);

            world.setBlockState(candidates.get(0), ModuleBlocks.ROCK.getDefaultState()
                .withProperty(BlockRock.VARIANT, BlockRock.EnumType.WOOD_CHIPS));
          }
        } // END: Wood Chips

        // Decrement the durability until next damage and progress or
        // complete the recipe.

        tile.setDurabilityUntilNextDamage(tile.getDurabilityUntilNextDamage() - 1);

        ItemStack heldItem = player.getHeldItem(hand);
        int harvestLevel = heldItem.getItem().getHarvestLevel(heldItem, "axe", player, null);

        ItemStackHandler stackHandler = tile.getStackHandler();
        ItemStack itemStack = stackHandler.getStackInSlot(0);
        ChoppingBlockRecipe recipe = ChoppingBlockRecipe.getRecipe(itemStack);

        if (recipe != null) {

          if (tile.getRecipeProgress() < 1) {

            // Check the recipe's harvest level and advance recipe progress.

            int[] chops = recipe.getChops();

            if (chops.length > 0) {
              float increment = 1f / ArrayHelper.getOrLast(chops, harvestLevel);
              tile.setRecipeProgress(tile.getRecipeProgress() + increment);
            }
          }

          if (tile.getRecipeProgress() >= 0.9999) {
            stackHandler.extractItem(0, stackHandler.getSlotLimit(0), false);
            ItemStack output = recipe.getOutput();
            int[] quantities = recipe.getQuantities();

            if (quantities.length > 0) {
              int quantity = ArrayHelper.getOrLast(quantities, harvestLevel);
              output.setCount(quantity);
            }

            StackHelper.spawnStackOnTop(world, output, tile.getPos(), 0);

            world.playSound(
                player,
                player.posX,
                player.posY,
                player.posZ,
                SoundEvents.BLOCK_WOOD_BREAK,
                SoundCategory.BLOCKS,
                1,
                (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
            );

            tile.markDirty();
            BlockHelper.notifyBlockUpdate(world, tile.getPos());
          }
        }

      } else {

        // Client particles

        IBlockState blockState = ModuleBlocks.CHOPPING_BLOCK.getDefaultState();

        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
        }
      }

      return true;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }

}
