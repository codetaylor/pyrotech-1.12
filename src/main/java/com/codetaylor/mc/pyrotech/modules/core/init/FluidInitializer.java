package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.library.FluidInitializerRegistry;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.fluid.BlockFluidClay;

public final class FluidInitializer {

  private static FluidInitializerRegistry REGISTRY;

  public static void onRegister(Registry registry) {

    REGISTRY = new FluidInitializerRegistry(ModuleCore.MOD_ID, ModuleCore.CREATIVE_TAB);

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleCore.Fluids.class,
        "CLAY",
        REGISTRY.createFluid(
            "clay",
            true,
            fluid -> fluid.setDensity(6000).setViscosity(12000),
            BlockFluidClay::new
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
