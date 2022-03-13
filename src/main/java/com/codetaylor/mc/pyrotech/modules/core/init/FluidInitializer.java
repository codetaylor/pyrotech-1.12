package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.library.FluidInitializerRegistry;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.fluid.BlockFluidClay;
import com.codetaylor.mc.pyrotech.modules.core.block.fluid.BlockFluidFreckleberryWine;
import com.codetaylor.mc.pyrotech.modules.core.block.fluid.BlockFluidGloamberryWine;
import com.codetaylor.mc.pyrotech.modules.core.block.fluid.BlockFluidPyroberryWine;

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
            "liquid_clay",
            true,
            fluid -> fluid.setDensity(6000).setViscosity(12000),
            BlockFluidClay::new
        )
    );

    injector.inject(
        ModuleCore.Fluids.class,
        "PYROBERRY_WINE",
        REGISTRY.createFluid(
            "pyroberry_wine",
            true,
            fluid -> fluid.setDensity(1000).setViscosity(1000),
            BlockFluidPyroberryWine::new
        )
    );

    injector.inject(
        ModuleCore.Fluids.class,
        "GLOAMBERRY_WINE",
        REGISTRY.createFluid(
            "gloamberry_wine",
            true,
            fluid -> fluid.setDensity(1000).setViscosity(1000),
            BlockFluidGloamberryWine::new
        )
    );

    injector.inject(
        ModuleCore.Fluids.class,
        "FRECKLEBERRY_WINE",
        REGISTRY.createFluid(
            "freckleberry_wine",
            true,
            fluid -> fluid.setDensity(1000).setViscosity(1000),
            BlockFluidFreckleberryWine::new
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
