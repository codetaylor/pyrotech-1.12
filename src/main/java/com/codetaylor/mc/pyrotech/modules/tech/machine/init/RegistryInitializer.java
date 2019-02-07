package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class RegistryInitializer {

  public static void createRegistries(RegistryEvent.NewRegistry event) {

    // --- Stone

    new RegistryBuilder<StoneKilnRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "kiln_stone_recipe"))
        .setType(StoneKilnRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneSawmillRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "mill_stone_recipe"))
        .setType(StoneSawmillRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneCrucibleRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "crucible_stone_recipe"))
        .setType(StoneCrucibleRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneOvenRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "oven_stone_recipe"))
        .setType(StoneOvenRecipe.class)
        .allowModification()
        .create();

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechMachine.Registries.class,
        "KILN_STONE_RECIPE",
        GameRegistry.findRegistry(StoneKilnRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "MILL_STONE_RECIPE",
        GameRegistry.findRegistry(StoneSawmillRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "OVEN_STONE_RECIPE",
        GameRegistry.findRegistry(StoneOvenRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "CRUCIBLE_STONE_RECIPE",
        GameRegistry.findRegistry(StoneCrucibleRecipe.class)
    );
  }

  private RegistryInitializer() {
    //
  }
}
