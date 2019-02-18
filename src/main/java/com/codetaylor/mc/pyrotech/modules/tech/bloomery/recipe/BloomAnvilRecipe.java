package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class BloomAnvilRecipe
    extends AnvilRecipe
    implements AnvilRecipe.IExtendedRecipe<BloomAnvilRecipe> {

  private final BloomeryRecipe bloomeryRecipe;

  public BloomAnvilRecipe(ItemStack output, Ingredient input, int hits, EnumType type, BloomeryRecipe bloomeryRecipe) {

    super(output, input, hits, type);
    this.bloomeryRecipe = bloomeryRecipe;
  }

  public BloomeryRecipe getBloomeryRecipe() {

    return this.bloomeryRecipe;
  }

  @Override
  public boolean matches(ItemStack input) {

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

    tile.setDurabilityUntilNextDamage(tile.getDurabilityUntilNextDamage() - tile.getBloomAnvilDamagePerHit());
    BloomHelper.trySpawnFire(world, tile.getPos(), RandomHelper.random(), ModuleTechBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_ON_HIT_IN_ANVIL);
  }

  @Override
  public float getModifiedRecipeProgressIncrement(float increment, TileAnvilBase tile, EntityPlayer player) {

    return (float) (increment * BloomHelper.calculateHammerPower(tile.getPos(), player));
  }

  @Override
  public void onRecipeCompleted(TileAnvilBase tile, World world, ItemStackHandler stackHandler, BloomAnvilRecipe recipe, EntityPlayer player) {

    float extraProgress = tile.getRecipeProgress() - 1;

    // Spawn in the bloomery recipe output
    BloomeryRecipe bloomeryRecipe = recipe.getBloomeryRecipe();
    StackHelper.spawnStackOnTop(world, bloomeryRecipe.getRandomOutput(player), tile.getPos(), 0);

    // Reduce the integrity of the bloom
    ItemStack bloom = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), false);
    BlockBloom.ItemBlockBloom item = (BlockBloom.ItemBlockBloom) bloom.getItem();
    int integrity = item.getIntegrity(bloom);

    if (BloomHelper.shouldReduceIntegrity(player, RandomHelper.random())) {
      integrity -= 1;
    }

    if (integrity > 0) {
      item.setIntegrity(bloom, integrity);
      stackHandler.insertItem(0, bloom, false);
    }

    if (extraProgress > 0) {
      tile.setRecipeProgress(extraProgress);
    }
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

}
