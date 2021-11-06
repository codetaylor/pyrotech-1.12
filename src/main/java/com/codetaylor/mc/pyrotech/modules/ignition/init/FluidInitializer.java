package com.codetaylor.mc.pyrotech.modules.ignition.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.library.FluidInitializerRegistry;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.block.fluid.BlockFluidTannin;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;

public final class FluidInitializer {

  private static FluidInitializerRegistry REGISTRY;

  public static void onRegister(Registry registry) {

    REGISTRY = new FluidInitializerRegistry(ModuleIgnition.MOD_ID, ModuleIgnition.CREATIVE_TAB);

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleIgnition.Fluids.class,
        "LAMP_OIL",
        REGISTRY.createFluid(
            "lamp_oil",
            true,
            fluid -> fluid.setDensity(1000).setViscosity(1000),
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
