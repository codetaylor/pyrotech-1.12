package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class RegistryInitializer {

  public static void createRegistries(RegistryEvent.NewRegistry event) {

    RegistryInitializer.registerStoneMachineRecipeRegistries();
    RegistryInitializer.registerBrickMachineRecipeRegistries();

    // MECHANICAL_COMPACTING_BIN_RECIPES
    new RegistryBuilder<MechanicalCompactingBinRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "mechanical_compacting_bin_recipe"))
        .setType(MechanicalCompactingBinRecipe.class)
        .allowModification()
        .create();

    Injector injector = new Injector();

    injector.inject(
        ModuleTechMachine.Registries.class,
        "MECHANICAL_COMPACTING_BIN_RECIPES",
        GameRegistry.findRegistry(MechanicalCompactingBinRecipe.class)
    );

  }

  protected static void registerStoneMachineRecipeRegistries() {

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
        "STONE_KILN_RECIPES",
        GameRegistry.findRegistry(StoneKilnRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "STONE_SAWMILL_RECIPES",
        GameRegistry.findRegistry(StoneSawmillRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "STONE_OVEN_RECIPES",
        GameRegistry.findRegistry(StoneOvenRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "STONE_CRUCIBLE_RECIPES",
        GameRegistry.findRegistry(StoneCrucibleRecipe.class)
    );
  }

  protected static void registerBrickMachineRecipeRegistries() {

    // --- Brick

    new RegistryBuilder<BrickKilnRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "kiln_brick_recipe"))
        .setType(BrickKilnRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<BrickSawmillRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "mill_brick_recipe"))
        .setType(BrickSawmillRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<BrickCrucibleRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "crucible_brick_recipe"))
        .setType(BrickCrucibleRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<BrickOvenRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "oven_brick_recipe"))
        .setType(BrickOvenRecipe.class)
        .allowModification()
        .create();

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechMachine.Registries.class,
        "BRICK_KILN_RECIPES",
        GameRegistry.findRegistry(BrickKilnRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "BRICK_SAWMILL_RECIPES",
        GameRegistry.findRegistry(BrickSawmillRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "BRICK_OVEN_RECIPES",
        GameRegistry.findRegistry(BrickOvenRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "BRICK_CRUCIBLE_RECIPES",
        GameRegistry.findRegistry(BrickCrucibleRecipe.class)
    );
  }

  private RegistryInitializer() {
    //
  }
}
