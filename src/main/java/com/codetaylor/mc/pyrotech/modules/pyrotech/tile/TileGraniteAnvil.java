package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockGraniteAnvil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemTongsBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemTongsFullBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileGraniteAnvil
    extends TileNetBase
    implements ITileInteractable {

  private final TileDataItemStackHandler<InputStackHandler> tileDataItemStackHandler;
  private InputStackHandler stackHandler;
  private TileDataFloat recipeProgress;

  private int durabilityUntilNextDamage;

  private IInteraction[] interactions;
  private AxisAlignedBB renderBounds;

  public TileGraniteAnvil() {

    super(ModulePyrotech.TILE_DATA_SERVICE);

    this.stackHandler = new InputStackHandler();
    this.stackHandler.addObserver((handler, slot) -> {
      this.recipeProgress.set(0);
      this.markDirty();
    });

    this.recipeProgress = new TileDataFloat(0);

    // --- Network ---

    this.tileDataItemStackHandler = new TileDataItemStackHandler<>(this.stackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataItemStackHandler,
        this.recipeProgress
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionTongs(),
        new Interaction(new ItemStackHandler[]{this.stackHandler}),
        new InteractionHit()
    };
    this.durabilityUntilNextDamage = ModulePyrotechConfig.GRANITE_ANVIL.HITS_PER_DAMAGE;
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

  public void setDamage(int damage) {

    this.world.setBlockState(this.pos, ModuleBlocks.GRANITE_ANVIL.getDefaultState()
        .withProperty(BlockGraniteAnvil.DAMAGE, damage), 3);
  }

  public int getDamage() {

    return this.world.getBlockState(this.pos).getValue(BlockGraniteAnvil.DAMAGE);
  }

  public void setDurabilityUntilNextDamage(int durabilityUntilNextDamage) {

    this.durabilityUntilNextDamage = durabilityUntilNextDamage;
    this.markDirty();
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
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataItemStackHandler.isDirty()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setInteger("durabilityUntilNextDamage", this.durabilityUntilNextDamage);
    compound.setFloat("recipeProgress", this.recipeProgress.get());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
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

  private class InteractionTongs
      extends InteractionUseItemBase<TileGraniteAnvil> {

    /* package */ InteractionTongs() {

      super(new EnumFacing[]{EnumFacing.UP}, BlockGraniteAnvil.AABB);
    }

    @Override
    protected boolean allowInteraction(TileGraniteAnvil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack stackInSlot = tile.stackHandler.getStackInSlot(0);
      ItemStack heldItem = player.getHeldItemMainhand();

      if (stackInSlot.isEmpty()) {
        return (heldItem.getItem() instanceof ItemTongsFullBase);

      } else {

        if (stackInSlot.getItem() == Item.getItemFromBlock(ModuleBlocks.BLOOM)) {
          return (heldItem.getItem() instanceof ItemTongsBase);
        }
      }

      return false;
    }

    @Override
    protected boolean doInteraction(TileGraniteAnvil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (heldItem.getItem() instanceof ItemTongsBase) {
        return this.doInteractionTongsEmpty(tile, player, heldItem);

      } else {
        return this.doInteractionTongsFull(tile, player, heldItem);
      }
    }

    private boolean doInteractionTongsEmpty(TileGraniteAnvil tile, EntityPlayer player, ItemStack tongsStack) {

      ItemStack bloomStack = tile.stackHandler.extractItem(0, 1, false);
      ItemStack fullTongsStack = ItemTongsBase.getFilledItemStack(tongsStack, bloomStack);
      tongsStack.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(player, fullTongsStack, player.inventory.currentItem);
      return true;
    }

    private boolean doInteractionTongsFull(TileGraniteAnvil tile, EntityPlayer player, ItemStack tongsStack) {

      NBTTagCompound tagCompound = tongsStack.getTagCompound();

      if (tagCompound == null) {
        return false;
      }

      NBTTagCompound tileTag = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
      ItemStack bloomStack = TileBloom.createBloomAsItemStack(new ItemStack(ModuleBlocks.BLOOM), tileTag);
      tile.stackHandler.insertItem(0, bloomStack, false);
      ItemStack emptyTongsStack = ItemTongsFullBase.getEmptyItemStack(tongsStack);
      tongsStack.shrink(1);

      if (!emptyTongsStack.isEmpty()) {
        ItemHandlerHelper.giveItemToPlayer(player, emptyTongsStack, player.inventory.currentItem);
      }

      return true;
    }

    @Override
    protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

      // This is a no-op because the tong damage is applied elsewhere.
    }
  }

  private class Interaction
      extends InteractionItemStack<TileGraniteAnvil> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, new EnumFacing[]{EnumFacing.UP}, BlockGraniteAnvil.AABB, new Transform(
          Transform.translate(0.5, 0.75, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (GraniteAnvilRecipe.getRecipe(itemStack) != null);
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

  private class InteractionHit
      extends InteractionUseItemBase<TileGraniteAnvil> {

    /* package */ InteractionHit() {

      super(new EnumFacing[]{EnumFacing.UP}, BlockGraniteAnvil.AABB);
    }

    @Override
    protected boolean allowInteraction(TileGraniteAnvil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (player.getFoodStats().getFoodLevel() < ModulePyrotechConfig.GRANITE_ANVIL.MINIMUM_HUNGER_TO_USE) {
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();

      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      ItemStackHandler stackHandler = tile.getStackHandler();
      ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), true);
      GraniteAnvilRecipe recipe = GraniteAnvilRecipe.getRecipe(itemStack);

      if (recipe == null) {
        return false;
      }

      if (heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
        // held item is pickaxe
        if (!ArrayHelper.contains(ModulePyrotechConfig.GRANITE_ANVIL.PICKAXE_BLACKLIST, resourceLocation.toString())) {
          return (recipe.getType() == GraniteAnvilRecipe.EnumType.PICKAXE);
        }

      } else if (ArrayHelper.contains(ModulePyrotechConfig.GRANITE_ANVIL.PICKAXE_WHITELIST, resourceLocation.toString())) {
        // held item is pickaxe
        return (recipe.getType() == GraniteAnvilRecipe.EnumType.PICKAXE);

      } else if (ModulePyrotechConfig.GRANITE_ANVIL.getHammerHitReduction(resourceLocation) > -1) {
        // held item is hammer
        return (recipe.getType() == GraniteAnvilRecipe.EnumType.HAMMER);
      }

      return false;
    }

    @Override
    protected boolean doInteraction(TileGraniteAnvil tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {

        // Server logic

        if (ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_HIT > 0) {
          player.addExhaustion((float) ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_HIT);
        }

        // Decrement the tile's damage and reset the hits
        // remaining until next damage. If the damage reaches the threshold,
        // destroy the block and drop its contents.

        if (tile.getDurabilityUntilNextDamage() <= 1) {

          tile.setDurabilityUntilNextDamage(ModulePyrotechConfig.GRANITE_ANVIL.HITS_PER_DAMAGE);

          if (tile.getDamage() + 1 < 4) {
            tile.setDamage(tile.getDamage() + 1);

          } else {
            StackHelper.spawnStackHandlerContentsOnTop(world, tile.getStackHandler(), tile.getPos());
            world.destroyBlock(tile.getPos(), false);
            return true;
          }
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

        // Decrement the durability until next damage and progress or
        // complete the recipe.

        tile.setDurabilityUntilNextDamage(tile.getDurabilityUntilNextDamage() - 1);

        ItemStackHandler stackHandler = tile.getStackHandler();
        ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), true);
        GraniteAnvilRecipe recipe = GraniteAnvilRecipe.getRecipe(itemStack);

        if (recipe != null) {
          boolean isBloomRecipe = (recipe instanceof GraniteAnvilRecipe.BloomAnvilRecipe);

          if (isBloomRecipe) {
            BlockBloom.trySpawnFire(world, tile.getPos(), RandomHelper.random());
          }

          if (tile.getRecipeProgress() < 1) {
            ItemStack heldItemMainHand = player.getHeldItemMainhand();
            Item item = heldItemMainHand.getItem();
            int hitReduction;

            if (item.getToolClasses(heldItemMainHand).contains("pickaxe")) {
              hitReduction = item.getHarvestLevel(heldItemMainHand, "pickaxe", player, null);

            } else {
              hitReduction = ModulePyrotechConfig.GRANITE_ANVIL.getHammerHitReduction(item.getRegistryName());
            }

            int hits = Math.max(1, recipe.getHits() - hitReduction);
            tile.setRecipeProgress(tile.getRecipeProgress() + 1f / hits);
          }

          if (tile.getRecipeProgress() >= 0.9999) {

            if (isBloomRecipe) {

              // Spawn in the bloomery recipe output
              BloomeryRecipe bloomeryRecipe = ((GraniteAnvilRecipe.BloomAnvilRecipe) recipe).getBloomeryRecipe();
              StackHelper.spawnStackOnTop(world, bloomeryRecipe.getRandomOutput(), tile.getPos(), 0);

              // Reduce the integrity of the bloom
              ItemStack bloom = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), false);
              BlockBloom.ItemBlockBloom item = (BlockBloom.ItemBlockBloom) bloom.getItem();
              int integrity = item.getIntegrity(bloom) - 1;

              if (integrity > 0) {
                item.setIntegrity(bloom, integrity);
                stackHandler.insertItem(0, bloom, false);
              }

            } else {
              stackHandler.extractItem(0, stackHandler.getSlotLimit(0), false);
              StackHelper.spawnStackOnTop(world, recipe.getOutput(), tile.getPos(), 0);
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

            if (ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_CRAFT_COMPLETE > 0) {
              player.addExhaustion((float) ModulePyrotechConfig.GRANITE_ANVIL.EXHAUSTION_COST_PER_CRAFT_COMPLETE);
            }

            tile.markDirty();
            BlockHelper.notifyBlockUpdate(world, tile.getPos());
          }
        }

      } else {

        // Client particles

        IBlockState blockState = ModuleBlocks.GRANITE_ANVIL.getDefaultState();

        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
        }

        // Bloom particles

        ItemStack stackInSlot = tile.stackHandler.getStackInSlot(0);

        if (stackInSlot.getItem() == Item.getItemFromBlock(ModuleBlocks.BLOOM)) {

          for (int i = 0; i < 8; ++i) {
            world.spawnParticle(EnumParticleTypes.LAVA, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D);
          }
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
