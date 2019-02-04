package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<StoneKilnRecipe> registry) {

    // Clay Bucket
    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.register(new StoneKilnRecipe(
          new ItemStack(ModuleBucket.Items.BUCKET_CLAY),
          Ingredient.fromStacks(new ItemStack(ModuleBucket.Items.BUCKET_CLAY_UNFIRED)),
          Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleBucket.MOD_ID, "bucket_clay"));
    }

  }
}