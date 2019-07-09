package com.codetaylor.mc.pyrotech.modules.tech.bloomery.util;

import com.codetaylor.mc.athenaeum.util.*;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class BloomHelper {

  public static boolean shouldReduceIntegrity(EntityPlayer player, Random random) {

    ItemStack heldItemMainhand = player.getHeldItemMainhand();
    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItemMainhand);
    Integer fortuneLevel = enchantments.get(Enchantments.FORTUNE);

    if (fortuneLevel != null) {

      double[] chanceArray = ModuleTechBloomeryConfig.BLOOM.CHANCE_TO_NOT_CONSUME_BLOOM_INTEGRITY_PER_FORTUNE_LEVEL;
      double chance = MathHelper.clamp(ArrayHelper.getOrLast(chanceArray, fortuneLevel), 0, 1);

      if (random.nextFloat() < chance) {
        return false;
      }
    }

    return true;
  }

  public static double calculateHammerPower(BlockPos pos, EntityPlayer player) {

    ItemStack heldItem = player.getHeldItemMainhand();
    int hammerHarvestLevel = ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(heldItem.getItem().getRegistryName());

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

    double[] modifier = ModuleTechBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_PER_HARVEST_LEVEL;
    result *= ArrayHelper.getOrLast(modifier, hammerHarvestLevel);

    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItem);
    Integer efficiencyLevel = enchantments.get(Enchantments.EFFICIENCY);

    if (efficiencyLevel != null) {
      double[] bonus = ModuleTechBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_PER_EFFICIENCY_LEVEL;
      result *= ArrayHelper.getOrLast(bonus, efficiencyLevel - 1);
    }

    PotionEffect effectStrength = player.getActivePotionEffect(MobEffects.STRENGTH);

    if (effectStrength != null) {
      result *= ModuleTechBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_FOR_STRENGTH_EFFECT;
    }

    PotionEffect effectWeakness = player.getActivePotionEffect(MobEffects.WEAKNESS);

    if (effectWeakness != null) {
      result *= ModuleTechBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_FOR_WEAKNESS_EFFECT;
    }

    PotionEffect effectMiningFatigue = player.getActivePotionEffect(MobEffects.MINING_FATIGUE);

    if (effectMiningFatigue != null) {
      result *= ModuleTechBloomeryConfig.BLOOM.HAMMER_POWER_MODIFIER_FOR_MINING_FATIGUE_EFFECT;
    }

    return Math.max(0, result);
  }

  public static ItemStack createBloomAsItemStack(int maxIntegrity, @Nullable String recipeId, @Nullable String langKey) {

    return createBloomAsItemStack(new ItemStack(ModuleTechBloomery.Blocks.BLOOM), maxIntegrity, maxIntegrity, recipeId, langKey);
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

    return BloomHelper.toItemStack(tile, new ItemStack(ModuleTechBloomery.Blocks.BLOOM));
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

    if (rand.nextFloat() < chance) {

      EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, false);

      if (closestPlayer != null) {
        BlockPos position = closestPlayer.getPosition();
        IBlockState blockState = world.getBlockState(position.down());

        if (!world.isAirBlock(position)
            || !blockState.getBlock().isSideSolid(blockState, world, pos, EnumFacing.UP)) {

          closestPlayer.setFire(rand.nextFloat() < 0.5 ? 1 : 2);

          int damage = rand.nextInt(5);

          if (damage > 0) {
            closestPlayer.attackEntityFrom(DamageSource.HOT_FLOOR, damage);
          }
        }
      }

      BlockHelper.forBlocksInCubeShuffled(world, pos, 2, 1, 2, (w, p, bs) -> {

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

  public static String getCompareString(ItemStack itemStack) {

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);
    NBTTagCompound tileTag = compound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
    return tileTag.getString("recipeId");
  }
}
