package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.library.FluidInitializerRegistry;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.block.fluid.BlockFluidTannin;

public final class FluidInitializer {

  private static FluidInitializerRegistry REGISTRY;

  public static void onRegister(Registry registry) {

    REGISTRY = new FluidInitializerRegistry(ModuleHunting.MOD_ID, ModuleHunting.CREATIVE_TAB);

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleHunting.Fluids.class,
        "TANNIN",
        REGISTRY.createFluid(
            "tannin",
            true,
            fluid -> fluid.setDensity(1000).setViscosity(2000),
            BlockFluidTannin::new
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
