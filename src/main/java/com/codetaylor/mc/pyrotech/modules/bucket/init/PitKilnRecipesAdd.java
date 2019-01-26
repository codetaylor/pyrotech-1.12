package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class PitKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnPitRecipe> registry) {

    // Clay Bucket
    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.register(new KilnPitRecipe(
          new ItemStack(ModuleBucket.Items.BUCKET_CLAY),
          Ingredient.fromStacks(new ItemStack(ModuleBucket.Items.BUCKET_CLAY_UNFIRED)),
          Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModulePyrotech.MOD_ID, "bucket_clay"));
    }
  }
}