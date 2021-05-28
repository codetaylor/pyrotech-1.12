package com.codetaylor.mc.pyrotech.modules.hunting.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionBounds;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketNoHunger;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCarcass
    extends TileEntityDataBase
    implements ITileInteractable {

  private final StackHandler stackHandler;
  private final TileDataFloat currentProgress;
  private final TileDataFloat totalProgress;

  private final IInteraction[] interactions;

  public TileCarcass() {

    super(ModuleCore.TILE_DATA_SERVICE);

    // --- Initialize ---

    this.currentProgress = new TileDataFloat(0);
    this.totalProgress = new TileDataFloat(0);

    this.stackHandler = new StackHandler();
    this.stackHandler.addObserver((handler, slot) -> {
      this.initializeProgress();
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
        new Interaction()
    };

    this.initializeProgress();
  }

  private void initializeProgress() {

    int progressRequired = ModuleHuntingConfig.CARCASS.TOTAL_PROGRESS_REQUIRED;
    float adjustment = RandomHelper.random().nextFloat() * 0.2f - 0.1f;
    this.currentProgress.set(Math.max(1, progressRequired + progressRequired * adjustment));
    this.totalProgress.set(this.currentProgress.get());
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
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private static class Interaction
      extends InteractionUseItemBase<TileCarcass> {

    public Interaction() {

      super(EnumFacing.VALUES, InteractionBounds.BLOCK);
    }

    @Override
    protected boolean allowInteraction(TileCarcass tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (player.getFoodStats().getFoodLevel() < ModuleHuntingConfig.CARCASS.MINIMUM_HUNGER_TO_USE) {

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
      return ArrayHelper.contains(ModuleHuntingConfig.CARCASS.ALLOWED_HUNTING_KNIVES, registryName);
    }

    @Override
    protected boolean doInteraction(TileCarcass tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Charge exhaustion.
        if (ModuleHuntingConfig.CARCASS.EXHAUSTION_COST_PER_KNIFE_USE > 0) {
          player.addExhaustion((float) ModuleHuntingConfig.CARCASS.EXHAUSTION_COST_PER_KNIFE_USE);
        }

        // Play sound for chop.
        world.playSound(
            null,
            player.posX,
            player.posY,
            player.posZ,
            SoundEvents.BLOCK_SLIME_PLACE,
            SoundCategory.BLOCKS,
            0.75f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );

        ItemStack heldItemStack = player.getHeldItem(hand);
        Item heldItem = heldItemStack.getItem();

        ResourceLocation resourceLocation = heldItem.getRegistryName();

        if (resourceLocation == null) {
          return false;
        }

        String registryName = resourceLocation.toString();
        int efficiency = ModuleHuntingConfig.CARCASS.HUNTING_KNIFE_EFFICIENCY.getOrDefault(registryName, 1);

        // Advance the progress.
        tile.currentProgress.set(tile.currentProgress.get() - efficiency);

        if (tile.currentProgress.get() <= 0) {
          // Check progress, drop item, reset progress or destroy carcass.

          int slot = tile.getFirstNonEmptySlot();
          ItemStack itemStack = tile.stackHandler.extractItem(slot, 1, false);

          if (!itemStack.isEmpty()) {
            StackHelper.spawnStackOnTop(world, itemStack, tile.pos);
          }

          if (tile.stackHandler.getTotalItemCount() == 0) {
            world.destroyBlock(tile.pos, false);

          } else {
            tile.initializeProgress();
          }
        }

      } else {

        // Client particles

        IBlockState blockState = ModuleHunting.Blocks.CARCASS.getDefaultState();

        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
        }

      }

      return true;
    }
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
