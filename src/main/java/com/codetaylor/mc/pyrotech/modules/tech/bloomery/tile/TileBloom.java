package com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile;

import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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

  private String recipeId;
  private String langKey;
  private TileDataFloat recipeProgress;
  private TileDataInteger integrity;

  private IInteraction[] interactions;
  private int maxIntegrity;

  public TileBloom() {

    super(ModuleBloomery.TILE_DATA_SERVICE);

    this.recipeProgress = new TileDataFloat(0);
    this.integrity = new TileDataInteger(0);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        this.recipeProgress,
        this.integrity
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionItem(),
        new InteractionHit()
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void setRecipeId(String recipeId) {

    this.recipeId = recipeId;
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

  public int getIntegrity() {

    return this.integrity.get();
  }

  public void setMaxIntegrity(int maxIntegrity) {

    this.maxIntegrity = maxIntegrity;
    this.integrity.set(maxIntegrity);
  }

  public float getRecipeProgress() {

    return this.recipeProgress.get();
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    BloomHelper.writeToNBT(compound, this.maxIntegrity, this.integrity.get(), this.recipeId, this.langKey);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.maxIntegrity = compound.getInteger("maxIntegrity");
    this.integrity.set(compound.getInteger("integrity"));

    if (compound.hasKey("recipeId")) {
      this.recipeId = compound.getString("recipeId");
    }

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

  private class InteractionItem
      extends InteractionUseItemBase<TileBloom> {

    /* package */ InteractionItem() {

      super(EnumFacing.VALUES, BlockBloom.AABB);
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {
      // TODO: remove
    }
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
      return ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(resourceLocation) > -1;
    }

    @Override
    protected boolean doInteraction(TileBloom tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Server logic

        if (player.getFoodStats().getFoodLevel() < ModuleBloomeryConfig.BLOOM.MINIMUM_HUNGER_TO_USE) {
          return false;
        }

        if (ModuleBloomeryConfig.BLOOM.EXHAUSTION_COST_PER_HIT > 0) {
          player.addExhaustion((float) ModuleBloomeryConfig.BLOOM.EXHAUSTION_COST_PER_HIT);
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

        BloomHelper.trySpawnFire(world, tile.getPos(), RandomHelper.random(), ModuleBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_ON_HIT_RAW);

        if (tile.recipeProgress.get() < 1) {
          int hits = Math.max(1, ModuleBloomeryConfig.BLOOM.HAMMER_HITS_REQUIRED);
          float recipeProgressIncrement = (float) ((1f / hits) * BloomHelper.calculateHammerPower(tile.getPos(), player));
          tile.recipeProgress.set(tile.recipeProgress.get() + recipeProgressIncrement);
        }

        if (tile.recipeProgress.get() >= 0.9999) {
          tile.integrity.add(-1);
          BloomeryRecipe recipe = ModuleBloomery.Registries.BLOOMERY_RECIPE.getValue(new ResourceLocation(tile.recipeId));

          if (recipe != null) {
            ItemStack output = recipe.getRandomOutput();
            StackHelper.spawnStackOnTop(world, output, tile.getPos(), 0);
          }

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

          if (ModuleBloomeryConfig.BLOOM.BREAKS_BLOCKS) {

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
        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.LAVA, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D);
        }
      }

      return true;
    }
  }
}
