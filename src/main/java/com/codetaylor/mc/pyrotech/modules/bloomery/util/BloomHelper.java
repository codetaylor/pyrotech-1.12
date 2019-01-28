package com.codetaylor.mc.pyrotech.modules.bloomery.util;

import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.bloomery.item.ItemTongsEmptyBase;
import com.codetaylor.mc.pyrotech.modules.bloomery.item.ItemTongsFullBase;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BloomHelper {

  public static double calculateHammerPower(BlockPos pos, EntityPlayer player) {

    ItemStack heldItem = player.getHeldItemMainhand();
    int hammerHarvestLevel = ModulePyrotechConfig.ANVIL_COMMON.getHammerHarvestLevel(heldItem.getItem().getRegistryName());

    if (hammerHarvestLevel == -1) {
      return 0;
    }

    double originX = pos.getX() + 0.5;
    double originY = pos.getY() + 0.25;
    double originZ = pos.getZ() + 0.5;

    double playerX = player.posX;
    double playerY = player.posY + player.getEyeHeight() * 0.5;
    double playerZ = player.posZ;

    double distance = DistanceHelper.getDistance(originX, originY, originZ, playerX, playerY, playerZ);
    double offsetX = 1;
    double clampedDistance = Math.max(offsetX, distance);
    double result = Math.max(0, 1 - (1.0 / 4.0) * Math.pow(clampedDistance - offsetX, 3));

    if (result > 0.985) {
      result = 1;
    }

    result *= ArrayHelper.getOrLast(ModuleBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_PER_HARVEST_LEVEL, hammerHarvestLevel);

    return Math.max(0, result);
  }

  public static ItemStack createBloomAsItemStack(int maxIntegrity, @Nullable String recipeId, @Nullable String langKey) {

    return createBloomAsItemStack(new ItemStack(ModuleBloomery.Blocks.BLOOM), maxIntegrity, maxIntegrity, recipeId, langKey);
  }

  public static ItemStack createBloomAsItemStack(ItemStack itemStack, int maxIntegrity, int integrity, @Nullable String recipeId, @Nullable String langKey) {

    NBTTagCompound tileTag = writeToNBT(new NBTTagCompound(), maxIntegrity, integrity, recipeId, langKey);
    return createBloomAsItemStack(itemStack, tileTag);
  }

  public static ItemStack createBloomAsItemStack(ItemStack itemStack, NBTTagCompound tileTag) {

    NBTTagCompound itemTag = StackHelper.getTagSafe(itemStack);
    itemTag.setTag(StackHelper.BLOCK_ENTITY_TAG, tileTag);
    return itemStack;
  }

  public static ItemStack toItemStack(TileBloom tile) {

    return BloomHelper.toItemStack(tile, new ItemStack(ModuleBloomery.Blocks.BLOOM));
  }

  public static ItemStack toItemStack(TileBloom tile, ItemStack itemStack) {

    return StackHelper.writeTileEntityToItemStack(tile, itemStack);
  }

  public static NBTTagCompound writeToNBT(NBTTagCompound compound, int maxIntegrity, int integrity, @Nullable String recipeId, @Nullable String langKey) {

    compound.setInteger("maxIntegrity", maxIntegrity);
    compound.setInteger("integrity", integrity);

    if (recipeId != null) {
      compound.setString("recipeId", recipeId);
    }

    if (langKey != null) {
      compound.setString("langKey", langKey);
    }

    return compound;
  }

  public static void trySpawnFire(World world, BlockPos pos, Random rand, double chance) {

    if (rand.nextDouble() < chance) {
      BlockHelper.forBlocksInCubeShuffled(world, pos, 1, 1, 1, (w, p, bs) -> {

        if (w.isAirBlock(p)) {

          BlockPos down = p.down();
          IBlockState blockState = w.getBlockState(down);

          if (blockState.isSideSolid(w, down, EnumFacing.UP)) {
            w.setBlockState(p, Blocks.FIRE.getDefaultState(), 1 | 2);
            return false;
          }
        }

        return true;
      });
    }
  }

  /**
   * Creates a new filled tongs item and merges NBT data from the empty tongs
   * and the bloom into the new item.
   * <p>
   * Does not apply damage.
   * Does not modify input stack.
   *
   * @param emptyTongs the empty tongs
   * @param bloom      the bloom
   * @return a new filled tongs item stack
   */
  public static ItemStack createItemTongsFull(ItemStack emptyTongs, ItemStack bloom) {

    Item item = emptyTongs.getItem();

    if (!(item instanceof ItemTongsEmptyBase)) {
      return emptyTongs;
    }

    ItemTongsEmptyBase tongs = (ItemTongsEmptyBase) item;
    ItemStack itemStack = new ItemStack(tongs.getItemTongsFull(), 1, emptyTongs.getMetadata());

    NBTTagCompound tag = new NBTTagCompound();

    if (bloom.getTagCompound() != null) {
      tag.merge(bloom.getTagCompound());
    }

    if (emptyTongs.getTagCompound() != null) {
      tag.merge(emptyTongs.getTagCompound());
    }

    itemStack.setTagCompound(tag);

    return itemStack;
  }

  /**
   * Returns the empty version of the full tongs passed in, with damage. If the
   * tongs are destroyed as a result of the damage, an empty itemstack is
   * returned instead.
   * <p>
   * Does not modify input stack.
   *
   * @param toEmpty the full tongs itemstack
   * @return the empty version of the full tongs passed in
   */
  public static ItemStack createItemTongsEmpty(ItemStack toEmpty) {

    NBTTagCompound tagCompound = toEmpty.getTagCompound();

    if (tagCompound == null) {
      return toEmpty;
    }

    Item item = toEmpty.getItem();

    if (!(item instanceof ItemTongsFullBase)) {
      return toEmpty;
    }

    if (toEmpty.attemptDamageItem(1, RandomHelper.random(), null)) {
      return ItemStack.EMPTY;
    }

    ItemTongsFullBase tongs = (ItemTongsFullBase) item;
    ItemStack itemStack = new ItemStack(tongs.getItemTongsEmpty(), 1, toEmpty.getMetadata());
    NBTTagCompound copy = tagCompound.copy();
    copy.removeTag(StackHelper.BLOCK_ENTITY_TAG);
    itemStack.setTagCompound(copy);

    return itemStack;
  }
}
