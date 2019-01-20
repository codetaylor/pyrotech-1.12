package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class BloomeryRecipesAdd {

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    int defaultBurnTimeTicks = 12 * 60 * 20;
    float defaultFailureChance = 0.33f;

    // Iron Nugget
    registry.register(new BloomeryRecipe(
        new ItemStack(Items.IRON_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            // TODO: slag
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "iron_nugget"));
  }
}