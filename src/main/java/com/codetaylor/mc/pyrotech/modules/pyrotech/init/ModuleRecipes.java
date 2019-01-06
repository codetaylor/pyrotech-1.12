package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.recipe.CompoundIngredientPublic;
import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.*;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import scala.actors.threadpool.Arrays;

import static com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks.ROCK;

public final class ModuleRecipes {

  public static void onRegisterRecipes(IForgeRegistry<IRecipe> registry) {

    IForgeRegistryModifiable modifiableRegistry = (IForgeRegistryModifiable) registry;

    for (String resourceName : ModulePyrotechConfig.RECIPES.VANILLA_REMOVE) {
      modifiableRegistry.remove(new ResourceLocation(resourceName));
    }
  }

  public static void onRegisterFurnaceRecipes() {

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.ANDESITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.GRANITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.DIORITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.LIMESTONE.asStack(),
        new ItemStack(ModuleBlocks.LIMESTONE),
        0.1f
    );
  }

  public static void onRegisterCompactingBinRecipes(IForgeRegistryModifiable<CompactingBinRecipe> registry) {

    // Gravel
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRAVEL),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta())
        ),
        9
    ).setRegistryName(ModulePyrotech.MOD_ID, "gravel"));

    // Dirt
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.DIRT, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIRT.getMeta())
        ),
        9
    ).setRegistryName(ModulePyrotech.MOD_ID, "dirt"));

    // Sand
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SAND, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.SAND.getMeta())
        ),
        9
    ).setRegistryName(ModulePyrotech.MOD_ID, "sand"));

    // Grass
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRASS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK_GRASS, 1, 0)
        ),
        9
    ).setRegistryName(ModulePyrotech.MOD_ID, "grass"));

    // Clay
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.CLAY, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.CLAY_BALL, 1, 0)
        ),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "clay"));

    // Snow
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SNOW, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "snow"));

    // Bone Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.BONE_BLOCK, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.DYE, 1, 15)
        ),
        9
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_block"));
  }

  public static void onRegisterCrucibleStoneRecipes(IForgeRegistryModifiable<CrucibleStoneRecipe> registry) {

    // Lava
    //noinspection unchecked
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        new CompoundIngredientPublic(Arrays.asList(new Ingredient[]{
            new OreIngredient("stone"),
            new OreIngredient("cobblestone")
        })),
        2 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "lava_from_stone"));

    // Water from Ice
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 2000),
        Ingredient.fromStacks(
            new ItemStack(Blocks.ICE, 1, 0)
        ),
        60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_ice"));

    // Water from Snow
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 500),
        Ingredient.fromStacks(
            new ItemStack(Blocks.SNOW, 1, 0)
        ),
        15 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_snow"));

    // Water from Snowballs
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 125),
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ),
        15 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_snowballs"));

    // Water from Packed Ice
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 4000),
        Ingredient.fromStacks(
            new ItemStack(Blocks.PACKED_ICE, 1, 0)
        ),
        4 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_packed_ice"));
  }

  public static void onRegisterMillStoneRecipes(IForgeRegistryModifiable<MillStoneRecipe> registry) {

    // --- Planks ---

    registerMillStoneRecipe(registry, "planks_oak",
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0))
    );

    registerMillStoneRecipe(registry, "planks_spruce",
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1))
    );

    registerMillStoneRecipe(registry, "planks_birch",
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2))
    );

    registerMillStoneRecipe(registry, "planks_jungle",
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3))
    );

    registerMillStoneRecipe(registry, "planks_acacia",
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0))
    );

    registerMillStoneRecipe(registry, "planks_dark_oak",
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1))
    );

    // --- Slabs ---

    registerMillStoneRecipe(registry, "slab_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 0))
    );

    registerMillStoneRecipe(registry, "slab_spruce",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 1))
    );

    registerMillStoneRecipe(registry, "slab_birch",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 2))
    );

    registerMillStoneRecipe(registry, "slab_jungle",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 3))
    );

    registerMillStoneRecipe(registry, "slab_acacia",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 4))
    );

    registerMillStoneRecipe(registry, "slab_dark_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 5))
    );

    // --- Misc ---

    // board
    registerMillStoneRecipe(registry, "board",
        ItemMaterial.EnumType.BOARD.asStack(),
        new OreIngredient("slabWood")
    );
  }

  private static void registerMillStoneRecipe(IForgeRegistryModifiable<MillStoneRecipe> registry, String name, ItemStack output, Ingredient input) {

    output = output.copy();
    output.setCount(1);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.STONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE))
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_0"));

    output = output.copy();
    output.setCount(2);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 3 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.FLINT_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModuleItems.BONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE))
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_1"));

    output = output.copy();
    output.setCount(3);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 2 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE))
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_2"));

    output = output.copy();
    output.setCount(4);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE))
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_3"));
  }

  public static void onRegisterGraniteAnvilRecipes(IForgeRegistry<GraniteAnvilRecipe> registry) {

    // --- Pickaxe Recipes -----------------------------------------------------

    // Individual Stone Bricks
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.BRICK_STONE.asStack(2),
        Ingredient.fromStacks(
            new ItemStack(Blocks.STONE_SLAB, 1, 5)
        ),
        4,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "brick_stone"));

    // Slabs

    // Stone Brick Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 5),
        Ingredient.fromStacks(
            new ItemStack(Blocks.STONEBRICK)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_brick_slab"));

    // Cobblestone Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 3),
        Ingredient.fromStacks(
            new ItemStack(Blocks.COBBLESTONE)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobblestone_slab"));

    // Stone Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 0),
        Ingredient.fromStacks(
            new ItemStack(Blocks.STONE)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_slab"));

    // Sandstone Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 1),
        Ingredient.fromStacks(
            new ItemStack(Blocks.SANDSTONE, 1, 0),
            new ItemStack(Blocks.SANDSTONE, 1, 1),
            new ItemStack(Blocks.SANDSTONE, 1, 2)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "sandstone_slab"));

    // Red Sandstone Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB2, 2, 0),
        Ingredient.fromStacks(
            new ItemStack(Blocks.RED_SANDSTONE, 1, 0),
            new ItemStack(Blocks.RED_SANDSTONE, 1, 1),
            new ItemStack(Blocks.RED_SANDSTONE, 1, 2)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "red_sandstone_slab"));

    // Brick Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 4),
        Ingredient.fromStacks(
            new ItemStack(Blocks.BRICK_BLOCK)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "brick_slab"));

    // Nether Brick Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 6),
        Ingredient.fromStacks(
            new ItemStack(Blocks.NETHER_BRICK)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "nether_brick_slab"));

    // Quartz Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.STONE_SLAB, 2, 7),
        Ingredient.fromStacks(
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 0),
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 1),
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 2)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "quartz_slab"));

    // Purpur Slab
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.PURPUR_SLAB, 2, 0),
        Ingredient.fromStacks(
            new ItemStack(Blocks.PURPUR_BLOCK, 1, 0)
        ),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "purpur_slab"));

    // Flint Shard from Flint
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.FLINT_SHARD.asStack(3),
        Ingredient.fromStacks(new ItemStack(Items.FLINT, 1, 0)),
        4,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "flint_shard_from_flint"));

    // Bone Shard from Bone
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.BONE_SHARD.asStack(3),
        Ingredient.fromStacks(new ItemStack(Items.BONE, 1, 0)),
        4,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_shard_from_bone"));

    // Bone Shard from Block
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.BONE_SHARD.asStack(9),
        Ingredient.fromStacks(new ItemStack(Blocks.BONE_BLOCK, 1, 0)),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_shard_from_block"));

    // Charcoal Flakes from Charcoal
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(8),
        Ingredient.fromStacks(new ItemStack(Items.COAL, 1, 1)),
        4,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "charcoal_flakes"));

    // Coal Pieces from Coal
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.COAL_PIECES.asStack(8),
        Ingredient.fromStacks(new ItemStack(Items.COAL, 1, 0)),
        4,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_pieces"));

    // Iron Shard from Iron Ingot
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.IRON_SHARD.asStack(9),
        Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT, 1, 0)),
        8,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "iron_shard"));

    // Diamond Shard from Diamond
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.DIAMOND_SHARD.asStack(9),
        Ingredient.fromStacks(new ItemStack(Items.DIAMOND, 1, 0)),
        16,
        GraniteAnvilRecipe.EnumType.PICKAXE
    ).setRegistryName(ModulePyrotech.MOD_ID, "diamond_shard"));

    // --- Hammer Recipes ------------------------------------------------------

    // Bone Meal from Bone Shard
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Items.DYE, 1, 15),
        Ingredient.fromStacks(ItemMaterial.EnumType.BONE_SHARD.asStack()),
        4,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_shard"));

    // Bone Meal from Bone
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Items.DYE, 3, 15),
        Ingredient.fromStacks(new ItemStack(Items.BONE, 1, 0)),
        4,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_meal_from_bone"));

    // Bone Meal from Bone Block
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Items.DYE, 9, 15),
        Ingredient.fromStacks(new ItemStack(Blocks.BONE_BLOCK, 1, 0)),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_meal_from_bone_block"));

    // Stone to cobblestone
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Blocks.COBBLESTONE),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.getMetadata())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_to_cobbled"));

    // Andesite to cobbled andesite
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "andesite_to_cobbled"));

    // Diorite to cobbled diorite
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "diorite_to_cobbled"));

    // Granite to cobbled granite
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "granite_to_cobbled"));

    // Limestone to cobbled limestone
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.LIMESTONE.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.LIMESTONE)),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "limestone_to_cobbled"));

    // Cobblestone to rocks
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ROCK, 8, BlockRock.EnumType.STONE.getMeta()),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobblestone_to_rocks"));

    // Cobbled andesite to rocks
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ROCK, 8, BlockRock.EnumType.ANDESITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobbled_andesite_to_rocks"));

    // Cobbled diorite to rocks
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ROCK, 8, BlockRock.EnumType.DIORITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobbled_diorite_to_rocks"));

    // Cobbled granite to rocks
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ROCK, 8, BlockRock.EnumType.GRANITE.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobbled_granite_to_rocks"));

    // Cobbled limestone to rocks
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(ROCK, 8, BlockRock.EnumType.LIMESTONE.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.LIMESTONE.getMeta())),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "cobbled_limestone_to_rocks"));

    // Coal block to coal
    registry.register(new GraniteAnvilRecipe(
        new ItemStack(Items.COAL, 9),
        Ingredient.fromStacks(new ItemStack(Blocks.COAL_BLOCK)),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_block_to_coal"));

    // Coal coke block to coal coke
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.COAL_COKE.asStack(9),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COAL_COKE_BLOCK)),
        8,
        GraniteAnvilRecipe.EnumType.HAMMER
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_coke_block_to_coal_coke"));

  }

  public static void onRegisterChoppingBlockRecipes(IForgeRegistry<ChoppingBlockRecipe> registry) {

    // --- Planks ---

    // Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_oak"));

    // Spruce
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_spruce"));

    // Birch
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_birch"));

    // Jungle
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_jungle"));

    // Acacia
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_acacia"));

    // Dark Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_dark_oak"));

    // --- Slabs ---

    String[] slabs = new String[]{
        "slab_oak",
        "slab_spruce",
        "slab_birch",
        "slab_jungle",
        "slab_acacia",
        "slab_dark_oak"
    };

    for (int i = 0; i < slabs.length; i++) {
      registry.register(new ChoppingBlockRecipe(
          new ItemStack(Blocks.WOODEN_SLAB, 1, i),
          Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, i)),
          ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL,
          new int[]{1, 2, 2, 3}
      ).setRegistryName(ModulePyrotech.MOD_ID, slabs[i]));
    }
  }

  public static void onRegisterDryingRackRecipes(IForgeRegistry<DryingRackRecipe> registry) {

    // Straw
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.STRAW.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.WHEAT)),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "straw"));

    // Dried Plant Fibers
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        6 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried"));

    // Dried Plant Fibers
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        new OreIngredient("treeSapling"),
        6 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried_from_sapling"));
  }

  public static void onRegisterPitBurnRecipes(IForgeRegistry<PitBurnRecipe> registry) {

    // Coal
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleBlocks.LOG_PILE, OreDictionary.WILDCARD_VALUE),
        10,
        12 * 60 * 20,
        new FluidStack(ModuleFluids.WOOD_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        },
        false,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal"));

    // Coal Coke
    registry.register(new PitBurnRecipe(
        ItemMaterial.EnumType.COAL_COKE.asStack(),
        new BlockMetaMatcher(Blocks.COAL_BLOCK, OreDictionary.WILDCARD_VALUE),
        10,
        24 * 60 * 20,
        new FluidStack(ModuleFluids.COAL_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        },
        true,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_coke"));
  }

  public static void onRegisterKilnPitRecipes(IForgeRegistry<KilnPitRecipe> registry) {

    int defaultBurnTimeTicks = 14 * 60 * 20;
    float defaultFailureChance = 0.33f;

    // Charcoal Flakes
    registry.register(new KilnPitRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "charcoal_flakes"));

    // Quicklime
    registry.register(new KilnPitRecipe(
        new ItemStack(ModuleItems.QUICKLIME, 1, 0),
        Ingredient.fromStacks(new ItemStack(ROCK, 1, BlockRock.EnumType.LIMESTONE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "quicklime"));

    // Stone Slab
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 3, 0)
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_diorite"));

  }

  public static void onRegisterKilnStoneRecipe(IForgeRegistry<KilnStoneRecipe> registry) {

    int defaultBurnTimeTicks = 7 * 60 * 20;
    float defaultFailureChance = 0.05f;

    // Refractory Brick
    registry.register(new KilnStoneRecipe(
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack()),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "refractory_brick"));

    // Charcoal Flakes
    registry.register(new KilnStoneRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "charcoal_flakes"));

    // Quicklime
    registry.register(new KilnStoneRecipe(
        new ItemStack(ModuleItems.QUICKLIME, 1, 0),
        Ingredient.fromStacks(new ItemStack(ROCK, 1, BlockRock.EnumType.LIMESTONE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "quicklime"));

    // Stone Slab
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 3, 0)
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        defaultBurnTimeTicks,
        defaultFailureChance,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleItems.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "stone_diorite"));

  }

  public static void onRegisterDryingRackCrudeRecipes(IForgeRegistryModifiable<DryingRackCrudeRecipe> registry) {

    // Dried Plant Fibers
    registry.register(new DryingRackCrudeRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried"));

    // Dried Plant Fibers
    registry.register(new DryingRackCrudeRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        new OreIngredient("treeSapling"),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried_from_sapling"));
  }

  private ModuleRecipes() {
    //
  }
}
