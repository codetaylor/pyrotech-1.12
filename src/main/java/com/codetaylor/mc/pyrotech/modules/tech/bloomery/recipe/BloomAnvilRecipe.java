package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.ExperienceHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BloomAnvilRecipe
    extends AnvilRecipe
    implements AnvilRecipe.IExtendedRecipe<BloomAnvilRecipe> {

  private final BloomeryRecipeBase<?> bloomeryRecipe;

  public BloomAnvilRecipe(ItemStack output, Ingredient input, int hits, EnumType type, EnumTier[] tiers, BloomeryRecipeBase<?> bloomeryRecipe) {

    super(output, input, hits, type, tiers);
    this.bloomeryRecipe = bloomeryRecipe;
  }

  public BloomeryRecipeBase<?> getBloomeryRecipe() {

    return this.bloomeryRecipe;
  }

  @Override
  public boolean matches(ItemStack input, EnumTier tier, EnumType type) {

    if (type != null && this.getType() != type) {
      return false;
    }

    if (input.getItem() != ModuleTechBloomery.Items.BLOOM) {
      return false;
    }

    NBTTagCompound inputTag = input.getTagCompound();

    if (inputTag == null) {
      return false;
    }

    NBTTagCompound inputTileTag = inputTag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);

    if (inputTileTag.getSize() == 0) {
      return false;
    }

    String recipeId = inputTileTag.getString("recipeId");

    if (recipeId.isEmpty()) {
      return false;
    }

    //noinspection ConstantConditions
    return this.getRegistryName().toString().equals(recipeId);
  }

  @Override
  public void applyDamage(World world, TileAnvilBase tile) {

    if (tile.useDurability()) {
      int durabilityUntilNextDamage = tile.getDurabilityUntilNextDamage() - 1;

      if (RandomHelper.random().nextFloat() < tile.getBloomAnvilExtraDamageChance()) {
        durabilityUntilNextDamage -= tile.getBloomAnvilExtraDamagePerHit();
      }

      tile.setDurabilityUntilNextDamage(durabilityUntilNextDamage);
    }
    BloomHelper.trySpawnFire(world, tile.getPos(), RandomHelper.random(), ModuleTechBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_ON_HIT_IN_ANVIL);
  }

  @Override
  public float getModifiedRecipeProgressIncrement(float increment, BlockPos anvilPos, Vec3d hammerPos, ItemStack hammerItemStack, @Nullable EntityPlayer player) {

    return (float) (increment * BloomHelper.calculateHammerPower(anvilPos, hammerPos, hammerItemStack, player));
  }

  @Override
  public List<ItemStack> onRecipeCompleted(TileAnvilBase tile, World world, ItemStackHandler stackHandler, BloomAnvilRecipe recipe, ItemStack toolItemStack) {

    List<ItemStack> result = new ArrayList<>();

    float extraProgress = tile.getRecipeProgress() - 1;

    result.add(this.onRecipeCompleted(tile, world, stackHandler, toolItemStack));

    while (extraProgress >= 1
        && !stackHandler.getStackInSlot(0).isEmpty()) {
      extraProgress -= 1;

      result.add(this.onRecipeCompleted(tile, world, stackHandler, toolItemStack));
    }

    if (extraProgress > 0) {
      tile.setRecipeProgress(extraProgress);
    }

    return result;
  }

  protected ItemStack onRecipeCompleted(TileAnvilBase tile, World world, ItemStackHandler stackHandler, ItemStack toolItemStack) {

    // Spawn in the bloomery recipe output
    ItemStack result = this.bloomeryRecipe.getRandomOutput(toolItemStack);

    // Reduce the integrity of the bloom
    ItemStack bloom = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), false);
    BlockBloom.ItemBlockBloom item = (BlockBloom.ItemBlockBloom) bloom.getItem();
    int integrity = item.getIntegrity(bloom);

    if (BloomHelper.shouldReduceIntegrity(toolItemStack, RandomHelper.random())) {
      integrity -= 1;
    }

    if (integrity > 0) {
      item.setIntegrity(bloom, integrity);
      stackHandler.insertItem(0, bloom, false);
    }

    // Spawn in the XP
    ExperienceHelper.spawnXp(world, 1, item.getExperiencePerComplete(bloom), tile.getPos());

    return result;
  }

  @Override
  public void onAnvilHitClient(World world, TileAnvilBase tile, float hitX, float hitY, float hitZ) {

    // Bloom particles

    ItemStack stackInSlot = tile.getStackHandler().getStackInSlot(0);

    if (stackInSlot.getItem() == ModuleTechBloomery.Items.BLOOM) {

      for (int i = 0; i < 8; ++i) {
        world.spawnParticle(EnumParticleTypes.LAVA, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  public void onAnvilDurabilityExpired(World world, TileAnvilBase tile, float hitX, float hitY, float hitZ) {

    ItemStack bloomStack = tile.getStackHandler().getStackInSlot(0).copy();
    boolean isBloom = (bloomStack.getItem() instanceof BlockBloom.ItemBlockBloom);
    BlockPos pos = tile.getPos();

    world.destroyBlock(pos, false);

    if (isBloom) {
      NBTTagCompound tagCompound = bloomStack.getTagCompound();

      if (tagCompound != null) {

        if (ModuleTechBloomery.Blocks.BLOOM.canPlaceBlockAt(world, pos)) {

          if (!world.isRemote) {
            world.setBlockState(pos, ModuleTechBloomery.Blocks.BLOOM.getDefaultState());
            TileBloom tileBloom = (TileBloom) world.getTileEntity(pos);

            if (tileBloom != null) {
              tileBloom.readFromNBT(tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG));
              tileBloom.setPos(pos);
              BlockHelper.notifyBlockUpdate(world, pos);
            }
          }

        } else {
          StackHelper.spawnStackOnTop(world, bloomStack, pos);
        }
      }

    } else {
      StackHelper.spawnStackOnTop(world, bloomStack, pos);
    }

  }
}
