package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompostBinRecipe;
import com.google.common.base.Preconditions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class CompostBinRecipesAdd {

  public static final int MULCH_RECIPE_OUTPUT_COUNT = 4;

  private static IForgeRegistryModifiable<CompostBinRecipe> REGISTRY;

  public static void apply(IForgeRegistryModifiable<CompostBinRecipe> registry) {

    REGISTRY = registry;

    // minecraft
    registerMulchRecipe(new ItemStack(Items.WHEAT));
    registerMulchRecipe(new ItemStack(Items.WHEAT_SEEDS));
    registerMulchRecipe(new ItemStack(Items.BEETROOT_SEEDS));
    registerMulchRecipe(new ItemStack(Items.MELON_SEEDS));
    registerMulchRecipe(new ItemStack(Items.PUMPKIN_SEEDS));
    registerMulchRecipe(new ItemStack(Items.STICK));
    registerMulchRecipe(new ItemStack(Items.STRING));
    registerMulchRecipe(new ItemStack(Items.SLIME_BALL));
    registerMulchRecipe(new ItemStack(Items.REEDS));
    registerMulchRecipe(new ItemStack(Items.PAPER));
    registerMulchRecipe(new ItemStack(Items.FEATHER));
    registerMulchRecipe(new ItemStack(Items.EGG));
    registerMulchRecipe(new ItemStack(Items.NETHER_WART));

    registerMulchRecipe(new ItemStack(Blocks.PUMPKIN));
    registerMulchRecipe(new ItemStack(Blocks.WEB));
    registerMulchRecipe(new ItemStack(Blocks.DEADBUSH));
    registerMulchRecipe(new ItemStack(Blocks.BROWN_MUSHROOM));
    registerMulchRecipe(new ItemStack(Blocks.RED_MUSHROOM));
    registerMulchRecipe(new ItemStack(Blocks.CACTUS));
    registerMulchRecipe(new ItemStack(Blocks.VINE));
    registerMulchRecipe(new ItemStack(Blocks.WATERLILY));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES, 1, 0));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES, 1, 2));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES, 1, 3));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES2, 1, 0));
    registerMulchRecipe(new ItemStack(Blocks.LEAVES2, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 3));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4));
    registerMulchRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5));
    registerMulchRecipe(new ItemStack(Blocks.YELLOW_FLOWER));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 0));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 2));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 3));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 4));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 5));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 6));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 7));
    registerMulchRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 8));
    registerMulchRecipe(new ItemStack(Blocks.TALLGRASS, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.TALLGRASS, 1, 2));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 0));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 1));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 2));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 3));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 4));
    registerMulchRecipe(new ItemStack(Blocks.SAPLING, 1, 5));

    // pyrotech
    registerMulchRecipe(BlockRock.EnumType.WOOD_CHIPS.asStack());

    registerMulchRecipe(new ItemStack(ModuleTechBasic.Items.TINDER), 2);
    registerMulchRecipe(new ItemStack(ModuleTechBasic.Blocks.KILN_PIT), 2);

    registerMulchRecipe(ItemMaterial.EnumType.PIT_ASH.asStack());
    registerMulchRecipe(ItemMaterial.EnumType.PLANT_FIBERS.asStack());
    registerMulchRecipe(ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack());
    registerMulchRecipe(ItemMaterial.EnumType.TWINE.asStack());
    registerMulchRecipe(ItemMaterial.EnumType.BOARD.asStack());
    registerMulchRecipe(ItemMaterial.EnumType.STRAW.asStack());

    for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {

      if (item instanceof ItemFood) {

        if (item.getHasSubtypes()) {
          NonNullList<ItemStack> items = NonNullList.create();
          item.getSubItems(CreativeTabs.SEARCH, items);

          for (ItemStack itemStack : items) {
            registerMulchRecipe(itemStack);
          }

        } else {
          registerMulchRecipe(new ItemStack(item));
        }
      }
    }

    REGISTRY = null;
  }

  private static void registerMulchRecipe(ItemStack input) {

    registerRecipe(getMulchItem(), input);
  }

  private static void registerMulchRecipe(ItemStack input, int compostValue) {

    registerRecipe(getMulchItem(), input, compostValue);
  }

  private static ItemStack getMulchItem() {

    return new ItemStack(ModuleCore.Items.MULCH, MULCH_RECIPE_OUTPUT_COUNT);
  }

  private static void registerRecipe(ItemStack output, ItemStack input, int compostValue) {

    CompostBinRecipe compostBinRecipe;
    compostBinRecipe = new CompostBinRecipe(output, input, compostValue);
    registerRecipe(input, compostBinRecipe);
  }

  private static void registerRecipe(ItemStack output, ItemStack input) {

    CompostBinRecipe compostBinRecipe = new CompostBinRecipe(output, input);
    registerRecipe(input, compostBinRecipe);
  }

  private static void registerRecipe(ItemStack input, CompostBinRecipe compostBinRecipe) {

    Item inputItem = input.getItem();
    ResourceLocation registryName = inputItem.getRegistryName();
    Preconditions.checkNotNull(registryName);
    String resourcePath = registryName.getResourcePath();
    compostBinRecipe.setRegistryName(ModuleTechBasic.MOD_ID, resourcePath + "_" + input.getMetadata());
    REGISTRY.register(compostBinRecipe);
  }
}