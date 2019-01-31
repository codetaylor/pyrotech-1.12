package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.KilnStoneRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnStoneRecipe> registry) {

    // Clay Bucket
    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.register(new KilnStoneRecipe(
          new ItemStack(ModuleBucket.Items.BUCKET_CLAY),
          Ingredient.fromStacks(new ItemStack(ModuleBucket.Items.BUCKET_CLAY_UNFIRED)),
          Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleCore.MOD_ID, "bucket_clay"));
    }

  }
}