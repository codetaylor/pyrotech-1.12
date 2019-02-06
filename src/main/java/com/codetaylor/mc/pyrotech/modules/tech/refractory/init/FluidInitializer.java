package com.codetaylor.mc.pyrotech.modules.tech.refractory.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.library.FluidInitializerRegistry;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.fluid.BlockFluidCoalTar;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.fluid.BlockFluidWoodTar;

public final class FluidInitializer {

  private static FluidInitializerRegistry REGISTRY;

  public static void onRegister(Registry registry) {

    REGISTRY = new FluidInitializerRegistry(ModuleTechRefractory.MOD_ID, ModuleTechRefractory.CREATIVE_TAB);

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechRefractory.Fluids.class,
        "WOOD_TAR",
        REGISTRY.createFluid(
            "wood_tar",
            true,
            fluid -> fluid.setDensity(3000).setViscosity(6000),
            BlockFluidWoodTar::new
        )
    );

    injector.inject(
        ModuleTechRefractory.Fluids.class,
        "COAL_TAR",
        REGISTRY.createFluid(
            "coal_tar",
            true,
            fluid -> fluid.setDensity(3000).setViscosity(6000),
            BlockFluidCoalTar::new
        )
    );

    REGISTRY.registerRegistrationStrategies(registry);
  }

  public static void onClientRegister(Registry registry) {

    REGISTRY.registerClientModelRegistrationStrategies(registry);
  }

  private FluidInitializer() {
    //
  }
}
