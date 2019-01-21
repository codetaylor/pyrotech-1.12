package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileNetBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileBloom
    extends TileNetBase
    implements ITileInteractable {

  private String langKey;
  private DynamicStackHandler stackHandler = new DynamicStackHandler(1);
  private TileDataFloat recipeProgress;

  private IInteraction[] interactions;
  private int maxIntegrity;

  public TileBloom() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionHit()
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public DynamicStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public void setLangKey(String langKey) {

    this.langKey = langKey;
  }

  public String getLangKey() {

    return this.langKey;
  }

  public int getMaxIntegrity() {

    return this.maxIntegrity;
  }

  public void setMaxIntegrity(int maxIntegrity) {

    this.maxIntegrity = maxIntegrity;
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  public static TileBloom fromItemStack(ItemStack itemStack) {

    return StackHelper.readTileEntityFromItemStack(new TileBloom(), itemStack);
  }

  public static ItemStack toItemStack(TileBloom tile) {

    return StackHelper.writeTileEntityToItemStack(tile, new ItemStack(ModuleBlocks.BLOOM));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setInteger("maxIntegrity", this.maxIntegrity);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());

    if (this.langKey != null) {
      compound.setString("langKey", this.langKey);
    }

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.maxIntegrity = compound.getInteger("maxIntegrity");
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));

    if (compound.hasKey("langKey")) {
      this.langKey = compound.getString("langKey");
    }
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class InteractionHit
      extends InteractionUseItemBase<TileBloom> {

    /* package */ InteractionHit() {

      super(EnumFacing.VALUES, BlockBloom.AABB);
    }

    @Override
    protected boolean allowInteraction(TileBloom tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();

      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      // is held item hammer?
      return ModulePyrotechConfig.GRANITE_ANVIL.getHammerHitReduction(resourceLocation) > -1;
    }

    @Override
    protected boolean doInteraction(TileBloom tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Server logic

        if (player.getFoodStats().getFoodLevel() < ModulePyrotechConfig.BLOOM.MINIMUM_HUNGER_TO_USE) {
          return false;
        }

        if (ModulePyrotechConfig.BLOOM.EXHAUSTION_COST_PER_HIT > 0) {
          player.addExhaustion((float) ModulePyrotechConfig.BLOOM.EXHAUSTION_COST_PER_HIT);
        }

        // Play sound for hit.
        world.playSound(
            null,
            player.posX,
            player.posY,
            player.posZ,
            SoundEvents.BLOCK_STONE_HIT,
            SoundCategory.BLOCKS,
            0.75f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );

        DynamicStackHandler stackHandler = tile.getStackHandler();

        if (tile.recipeProgress.get() < 1) {
          ItemStack heldItemMainHand = player.getHeldItemMainhand();
          Item item = heldItemMainHand.getItem();
          int hitReduction;
          hitReduction = ModulePyrotechConfig.GRANITE_ANVIL.getHammerHitReduction(item.getRegistryName());

          int hits = Math.max(1, ModulePyrotechConfig.BLOOM.HAMMER_HITS_REQUIRED - hitReduction);
          tile.recipeProgress.set(tile.recipeProgress.get() + 1f / hits);
        }

        if (tile.recipeProgress.get() >= 0.9999) {
          ItemStack output = stackHandler.extractRandomItem(false, RandomHelper.random());
          StackHelper.spawnStackOnTop(world, output, tile.getPos(), 0);

          world.playSound(
              player,
              player.posX,
              player.posY,
              player.posZ,
              SoundEvents.BLOCK_STONE_BREAK,
              SoundCategory.BLOCKS,
              1,
              (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
          );

          world.destroyBlock(tile.getPos(), true);

          if (ModulePyrotechConfig.BLOOM.BREAKS_BLOCKS) {

            // Check and destroy block below the bloom.

            BlockPos posDown = tile.getPos().down();
            IBlockState blockStateDown = world.getBlockState(posDown);
            Block blockDown = blockStateDown.getBlock();
            float blockDownHardness = blockDown.getBlockHardness(blockStateDown, world, posDown);

            if (blockDownHardness >= 0) {

              // With this:
              // 1 - (x/60)^(1/8),
              // obsidian has roughly a 2.25% chance to break
              // and average blocks with a hardness of 2 have roughly a 30% chance to break

              float clampedBlockDownHardness = MathHelper.clamp(blockDownHardness, 0, 50);
              float breakChance = (float) (1f - Math.pow(clampedBlockDownHardness / 60, 0.125f));

              if (RandomHelper.random().nextDouble() < breakChance) {
                world.destroyBlock(posDown, false);
              }
            }
          }
        }

      } else {

        // Client particles

        IBlockState blockState = Blocks.FIRE.getDefaultState();

        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
        }
      }

      return true;
    }
  }
}
