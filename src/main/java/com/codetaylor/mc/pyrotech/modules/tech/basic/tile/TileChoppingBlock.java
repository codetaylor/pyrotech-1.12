package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketNoHunger;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleProgress;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockChoppingBlock;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.ChoppingBlockRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileChoppingBlock
    extends TileEntityDataBase
    implements ITileInteractable {

  private InputStackHandler stackHandler;
  private TileDataFloat recipeProgress;

  private int sawdust;
  private int durabilityUntilNextDamage;

  private IInteraction[] interactions;
  private AxisAlignedBB renderBounds;

  public TileChoppingBlock() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

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
    this.durabilityUntilNextDamage = ModuleTechBasicConfig.CHOPPING_BLOCK.CHOPS_PER_DAMAGE;
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

    this.world.setBlockState(this.pos, ModuleTechBasic.Blocks.CHOPPING_BLOCK.getDefaultState()
        .withProperty(BlockChoppingBlock.DAMAGE, damage), 3);
  }

  public int getDamage() {

    return this.world.getBlockState(this.pos).getValue(BlockChoppingBlock.DAMAGE);
  }

  private void setDurabilityUntilNextDamage(int durabilityUntilNextDamage) {

    this.durabilityUntilNextDamage = durabilityUntilNextDamage;
    this.markDirty();
  }

  private int getDurabilityUntilNextDamage() {

    return this.durabilityUntilNextDamage;
  }

  private void setRecipeProgress(float recipeProgress) {

    this.recipeProgress.set(recipeProgress);
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return this.allowAutomation()
        && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()
        && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      //noinspection unchecked
      return (T) this.stackHandler;
    }

    return null;
  }

  protected boolean allowAutomation() {

    return ModuleTechBasicConfig.CHOPPING_BLOCK.ALLOW_AUTOMATION;
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

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_CHOPPING_BLOCK;
  }

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
    protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(type, itemStack, world, player, pos);

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
          && (heldItemStack.isEmpty()
          || heldItemStack.getItem().getToolClasses(heldItemStack).contains("shovel"));
    }

    @Override
    protected boolean doInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (!world.isRemote) {
        int sawdust = tile.getSawdust();

        if (ModuleCoreConfig.TWEAKS.REQUIRE_SHOVEL_TO_PICKUP_WOOD_CHIPS) {

          if (!heldItem.isEmpty()) {
            ItemStack itemStack = new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta());
            StackHelper.spawnStackOnTop(world, itemStack, hitPos, 0);
            heldItem.damageItem(1, player);
          }

          tile.setSawdust(sawdust - 1);

        } else {
          tile.setSawdust(sawdust - 1);
          ItemStack itemStack = new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta());
          StackHelper.spawnStackOnTop(world, itemStack, hitPos, 0);
        }

        world.playSound(null, hitPos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1, 1);

        if (ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_SHOVEL_SCOOP > 0) {
          player.addExhaustion((float) ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_SHOVEL_SCOOP);
        }
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

      if (player.getFoodStats().getFoodLevel() < ModuleTechBasicConfig.CHOPPING_BLOCK.MINIMUM_HUNGER_TO_USE) {

        if (!world.isRemote) {
          ModuleTechBasic.PACKET_SERVICE.sendTo(new SCPacketNoHunger(), (EntityPlayerMP) player);
        }
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();

      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      String registryName = resourceLocation.toString();

      if (heldItem.getToolClasses(heldItemStack).contains("axe")) {
        return !ArrayHelper.contains(ModuleTechBasicConfig.CHOPPING_BLOCK.AXE_BLACKLIST, registryName);

      } else {
        return ArrayHelper.contains(ModuleTechBasicConfig.CHOPPING_BLOCK.AXE_WHITELIST, registryName);
      }
    }

    @Override
    protected boolean doInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Server logic

        if (ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_AXE_CHOP > 0) {
          player.addExhaustion((float) ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_AXE_CHOP);
        }

        // Decrement the chopping block's damage and reset the chops
        // remaining until next damage. If the damage reaches the threshold,
        // destroy the block and drop its contents.

        if (ModuleTechBasicConfig.CHOPPING_BLOCK.USES_DURABILITY
            && tile.getDurabilityUntilNextDamage() <= 1) {

          tile.setDurabilityUntilNextDamage(ModuleTechBasicConfig.CHOPPING_BLOCK.CHOPS_PER_DAMAGE);

          if (tile.getDamage() + 1 < 6) {
            tile.setDamage(tile.getDamage() + 1);

          } else {
            StackHelper.spawnStackHandlerContentsOnTop(world, tile.getStackHandler(), tile.getPos());
            ItemStack itemStack = new ItemStack(ModuleCore.Blocks.ROCK, tile.getSawdust(), BlockRock.EnumType.WOOD_CHIPS.getMeta());
            StackHelper.spawnStackOnTop(world, itemStack, hitPos, 0);
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
              && Math.random() < ModuleTechBasicConfig.CHOPPING_BLOCK.WOOD_CHIPS_CHANCE * 2) {

            tile.setSawdust(tile.getSawdust() + 1);
          }

          List<BlockPos> candidates = new ArrayList<>();

          // 1/2 the chance to place chips around the block.
          if (Math.random() < ModuleTechBasicConfig.CHOPPING_BLOCK.WOOD_CHIPS_CHANCE * 0.5) {
            BlockHelper.forBlocksInCube(world, tile.getPos(), 1, 1, 1, (w, p, bs) -> {

              if (w.isAirBlock(p)
                  && ModuleCore.Blocks.ROCK.canPlaceBlockAt(w, p)
                  && bs.getBlock() != ModuleCore.Blocks.ROCK) {

                candidates.add(p);
              }

              return true;
            });
          }

          if (candidates.size() > 0) {
            Collections.shuffle(candidates);

            world.setBlockState(candidates.get(0), ModuleCore.Blocks.ROCK.getDefaultState()
                .withProperty(BlockRock.VARIANT, BlockRock.EnumType.WOOD_CHIPS));
          }
        } // END: Wood Chips

        // Decrement the durability until next damage and progress or
        // complete the recipe.

        if (ModuleTechBasicConfig.CHOPPING_BLOCK.USES_DURABILITY) {
          tile.setDurabilityUntilNextDamage(tile.getDurabilityUntilNextDamage() - 1);
        }

        ItemStack heldItem = player.getHeldItem(hand);
        int harvestLevel = heldItem.getItem().getHarvestLevel(heldItem, "axe", player, null);

        if (harvestLevel < 0) {
          harvestLevel = 0;
        }

        ItemStackHandler stackHandler = tile.getStackHandler();
        ItemStack itemStack = stackHandler.getStackInSlot(0);
        ChoppingBlockRecipe recipe = ChoppingBlockRecipe.getRecipe(itemStack);

        if (recipe != null) {

          ModuleCore.PACKET_SERVICE.sendToAllAround(
              new SCPacketParticleProgress(hitPos.getX() + 0.5, hitPos.getY() + 1.0, hitPos.getZ() + 0.5, 2),
              world.provider.getDimension(),
              hitPos
          );

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

            if (ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_CRAFT_COMPLETE > 0) {
              player.addExhaustion((float) ModuleTechBasicConfig.CHOPPING_BLOCK.EXHAUSTION_COST_PER_CRAFT_COMPLETE);
            }

            tile.markDirty();
            BlockHelper.notifyBlockUpdate(world, tile.getPos());
          }
        }

      } else {

        // Client particles

        IBlockState blockState = ModuleTechBasic.Blocks.CHOPPING_BLOCK.getDefaultState();

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

    /* package */ InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (stack.isEmpty()
          || ChoppingBlockRecipe.getRecipe(stack) == null) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

}
