package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class PitKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnPitRecipe> registry) {

    // Clay Bucket
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleBucket.class)) {
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
      ).setRegistryName(ModuleBucket.MOD_ID, "bucket_clay"));
    }

    // Refractory Bucket
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleBucket.class)) {
      registry.register(new KilnPitRecipe(
          new ItemStack(ModuleBucket.Items.BUCKET_REFRACTORY),
          Ingredient.fromStacks(new ItemStack(ModuleBucket.Items.BUCKET_REFRACTORY_UNFIRED)),
          Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleBucket.MOD_ID, "bucket_refractory"));
    }

    // Clay Shears
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTool.class)) {
      registry.register(new KilnPitRecipe(
          new ItemStack(ModuleTool.Items.CLAY_SHEARS),
          Ingredient.fromStacks(new ItemStack(ModuleTool.Items.UNFIRED_CLAY_SHEARS)),
          Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleTechBasic.MOD_ID, "clay_shears"));
    }

    // Brick
    registry.register(new KilnPitRecipe(
        new ItemStack(Items.BRICK),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_BRICK.asStack()),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "brick"));

    // Cob
    registry.register(new KilnPitRecipe(
        new ItemStack(ModuleCore.Blocks.COB_DRY),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COB_WET)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            new ItemStack(ModuleCore.Blocks.ROCK, 1, 4),
            new ItemStack(ModuleCore.Blocks.ROCK, 2, 4)
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "cob"));

    // Charcoal Flakes
    registry.register(new KilnPitRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "charcoal_flakes"));

    // Stone Slab
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 3, 0)
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        new OreIngredient("cobblestoneAndesite"),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        new OreIngredient("cobblestoneGranite"),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        new OreIngredient("cobblestoneDiorite"),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_diorite"));

    // Stone - Limestone
    registry.register(new KilnPitRecipe(
        new ItemStack(ModuleCore.Blocks.LIMESTONE),
        new OreIngredient("cobblestoneLimestone"),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.LIMESTONE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "limestone"));

    // Terracotta
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.HARDENED_CLAY),
        Ingredient.fromStacks(new ItemStack(Blocks.CLAY)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "hardened_clay"));

    List<ItemStack> glazedTerracotta = new ArrayList<>();
    glazedTerracotta.add(new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.GRAY_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.RED_GLAZED_TERRACOTTA));
    glazedTerracotta.add(new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA));

    for (int i = 0; i < glazedTerracotta.size(); i++) {
      ItemStack output = glazedTerracotta.get(i);

      // Terracotta
      registry.register(new KilnPitRecipe(
          output.copy(),
          Ingredient.fromStacks(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, i)),
          Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleTechBasic.MOD_ID, output.getItem().getRegistryName().getResourcePath()));
    }
  }
}