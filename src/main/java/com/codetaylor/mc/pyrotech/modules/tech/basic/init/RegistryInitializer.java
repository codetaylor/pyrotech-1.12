package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class RegistryInitializer {

  public static void createRegistries(RegistryEvent.NewRegistry event) {

    new RegistryBuilder<KilnPitRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "kiln_pit_recipe"))
        .setType(KilnPitRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<CrudeDryingRackRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "crude_drying_rack_recipe"))
        .setType(CrudeDryingRackRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<DryingRackRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "drying_rack_recipe"))
        .setType(DryingRackRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<ChoppingBlockRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "chopping_block_recipe"))
        .setType(ChoppingBlockRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<AnvilRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "granite_anvil_recipe"))
        .setType(AnvilRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<CompactingBinRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "compacting_bin_recipe"))
        .setType(CompactingBinRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<CampfireRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "campfire_recipe"))
        .setType(CampfireRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<WorktableRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "worktable_recipe"))
        .setType(WorktableRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<SoakingPotRecipe>()
        .setName(new ResourceLocation(ModuleTechBasic.MOD_ID, "soaking_pot_recipe"))
        .setType(SoakingPotRecipe.class)
        .allowModification()
        .create();

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechBasic.Registries.class,
        "KILN_PIT_RECIPE",
        GameRegistry.findRegistry(KilnPitRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "CRUDE_DRYING_RACK_RECIPE",
        GameRegistry.findRegistry(CrudeDryingRackRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "DRYING_RACK_RECIPE",
        GameRegistry.findRegistry(DryingRackRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "CHOPPING_BLOCK_RECIPE",
        GameRegistry.findRegistry(ChoppingBlockRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "ANVIL_RECIPE",
        GameRegistry.findRegistry(AnvilRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "COMPACTING_BIN_RECIPE",
        GameRegistry.findRegistry(CompactingBinRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "CAMPFIRE_RECIPE",
        GameRegistry.findRegistry(CampfireRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "WORKTABLE_RECIPE",
        GameRegistry.findRegistry(WorktableRecipe.class)
    );
    injector.inject(
        ModuleTechBasic.Registries.class,
        "SOAKING_POT_RECIPE",
        GameRegistry.findRegistry(SoakingPotRecipe.class)
    );
  }

  private RegistryInitializer() {
    //
  }

}
