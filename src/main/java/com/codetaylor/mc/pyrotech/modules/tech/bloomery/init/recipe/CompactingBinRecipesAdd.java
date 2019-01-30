package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe;

import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

public class CompactingBinRecipesAdd {

  public static void apply(IForgeRegistry<CompactingBinRecipe> registry) {

    for (Map.Entry<BlockPileSlag, BlockPileSlag.Properties> entry : ModuleBloomery.Blocks.GENERATED_PILE_SLAG.entrySet()) {
      BlockPileSlag slagBlock = entry.getKey();
      ItemSlag slagItem = entry.getValue().slagItem;
      ResourceLocation slagBlockRegistryName = Preconditions.checkNotNull(slagBlock.getRegistryName());
      ResourceLocation slagItemRegistryName = Preconditions.checkNotNull(slagItem.getRegistryName());

      String prefix = alphanumeric(slagBlockRegistryName);
      String suffix = alphanumeric(slagItemRegistryName);
      registry.register(new CompactingBinRecipe(
          new ItemStack(slagBlock),
          Ingredient.fromStacks(new ItemStack(slagItem)),
          8
      ).setRegistryName(prefix + "_from_" + suffix));
    }

    ResourceLocation slagBlockRegistryName = Preconditions.checkNotNull(ModuleBloomery.Blocks.PILE_SLAG.getRegistryName());
    ResourceLocation slagItemRegistryName = Preconditions.checkNotNull(ModuleBloomery.Items.SLAG.getRegistryName());

    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleBloomery.Blocks.PILE_SLAG),
        Ingredient.fromStacks(new ItemStack(ModuleBloomery.Items.SLAG)),
        8
    ).setRegistryName(alphanumeric(slagBlockRegistryName) + "_from_" + alphanumeric(slagItemRegistryName)));

  }

  private static String alphanumeric(ResourceLocation input) {

    return input.toString().replaceAll("[^A-Za-z0-9_]", "_");
  }

}
