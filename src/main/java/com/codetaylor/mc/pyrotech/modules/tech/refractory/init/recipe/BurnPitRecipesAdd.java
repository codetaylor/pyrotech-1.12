package com.codetaylor.mc.pyrotech.modules.tech.refractory.init.recipe;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipeBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class BurnPitRecipesAdd {

  public static void apply(IForgeRegistry<PitBurnRecipe> registry) {

    // Charcoal
    registry.register(
        new PitBurnRecipeBuilder(
            new ItemStack(Items.COAL, 1, 1),
            new BlockMetaMatcher(ModuleCore.Blocks.LOG_PILE, OreDictionary.WILDCARD_VALUE)
        )
            .setBurnStages(10)
            .setTotalBurnTimeTicks(8 * 60 * 20)
            .setFluidProduced(new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50))
            .setFailureChance(0.33f)
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack())
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(2))
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(6))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(8))
            .setRequiresRefractoryBlocks(false)
            .setFluidLevelAffectsFailureChance(true)
            .create(new ResourceLocation(ModuleTechRefractory.MOD_ID, "charcoal"))
    );

    // Charcoal from Pile of Wood Chips
    registry.register(
        new PitBurnRecipeBuilder(
            new ItemStack(Items.COAL, 1, 1),
            new BlockMetaMatcher(ModuleCore.Blocks.PILE_WOOD_CHIPS, 0)
        )
            .setBurnStages(4)
            .setTotalBurnTimeTicks(4 * 60 * 20)
            .setFluidProduced(new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50))
            .setFailureChance(0.33f)
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack())
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(2))
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(6))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(8))
            .setRequiresRefractoryBlocks(false)
            .setFluidLevelAffectsFailureChance(true)
            .create(new ResourceLocation(ModuleTechRefractory.MOD_ID, "charcoal_from_pile_wood_chips"))
    );

    // Coal Coke
    registry.register(
        new PitBurnRecipeBuilder(
            ItemMaterial.EnumType.COAL_COKE.asStack(),
            new BlockMetaMatcher(Blocks.COAL_BLOCK, OreDictionary.WILDCARD_VALUE)
        )
            .setBurnStages(10)
            .setTotalBurnTimeTicks(12 * 60 * 20)
            .setFluidProduced(new FluidStack(ModuleTechRefractory.Fluids.COAL_TAR, 50))
            .setFailureChance(0.15f)
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack())
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(2))
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.COAL_PIECES.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.COAL_PIECES.asStack(6))
            .addFailureItem(ItemMaterial.EnumType.COAL_PIECES.asStack(8))
            .setRequiresRefractoryBlocks(true)
            .setFluidLevelAffectsFailureChance(true)
            .create(new ResourceLocation(ModuleTechRefractory.MOD_ID, "coal_coke"))
    );

    // Charcoal from Wood Tar Block
    registry.register(
        new PitBurnRecipeBuilder(
            new ItemStack(Items.COAL, 1, 1),
            new BlockMetaMatcher(ModuleCore.Blocks.WOOD_TAR_BLOCK, OreDictionary.WILDCARD_VALUE)
        )
            .setBurnStages(10)
            .setTotalBurnTimeTicks(5 * 60 * 20)
            .setFluidProduced(null)
            .setFailureChance(0.15f)
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack())
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(2))
            .addFailureItem(ItemMaterial.EnumType.PIT_ASH.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(4))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(6))
            .addFailureItem(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(8))
            .setRequiresRefractoryBlocks(false)
            .setFluidLevelAffectsFailureChance(false)
            .create(new ResourceLocation(ModuleTechRefractory.MOD_ID, "charcoal_from_wood_tar_block"))
    );
  }
}