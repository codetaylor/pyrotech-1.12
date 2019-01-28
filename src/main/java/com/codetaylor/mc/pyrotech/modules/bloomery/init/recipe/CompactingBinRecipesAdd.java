package com.codetaylor.mc.pyrotech.modules.bloomery.init.recipe;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.bloomery.item.ItemSlag;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
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
      ResourceLocation slagBlockRegistryName = slagBlock.getRegistryName();
      ResourceLocation slagItemRegistryName = slagItem.getRegistryName();

      if (slagBlockRegistryName != null
          && slagItemRegistryName != null) {
        String prefix = alphanumeric(slagBlockRegistryName);
        String suffix = alphanumeric(slagItemRegistryName);
        registry.register(new CompactingBinRecipe(
            new ItemStack(slagBlock),
            Ingredient.fromStacks(new ItemStack(slagItem)),
            8
        ).setRegistryName(prefix + "_from_" + suffix));
      }
    }
  }

  private static String alphanumeric(ResourceLocation input) {

    return input.toString().replaceAll("[^A-Za-z0-9_]", "_");
  }

}
