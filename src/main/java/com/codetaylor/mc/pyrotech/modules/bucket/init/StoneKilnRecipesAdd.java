package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnStoneRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnStoneRecipe> registry) {

    int defaultBurnTimeTicks = 7 * 60 * 20;
    float defaultFailureChance = 0.05f;

    // Clay Bucket
    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.register(new KilnStoneRecipe(
          new ItemStack(ModuleBucket.Items.BUCKET_CLAY),
          Ingredient.fromStacks(new ItemStack(ModuleBucket.Items.BUCKET_CLAY_UNFIRED)),
          defaultBurnTimeTicks,
          defaultFailureChance,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModulePyrotech.MOD_ID, "bucket_clay"));
    }

  }
}