package com.codetaylor.mc.pyrotech.modules.tech.refractory.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.fluid.BlockFluidCoalTar;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.fluid.BlockFluidWoodTar;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FluidInitializer {

  private static final List<Fluid> ALL_FLUID_LIST = new ArrayList<>();
  private static final List<IFluidBlock> MOD_FLUID_BLOCK_LIST = new ArrayList<>();

  public static void onRegister(Registry registry) {

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechRefractory.Fluids.class,
        "WOOD_TAR",
        FluidInitializer.createFluid(
            "wood_tar",
            true,
            fluid -> fluid.setDensity(3000).setViscosity(6000),
            BlockFluidWoodTar::new
        )
    );

    injector.inject(
        ModuleTechRefractory.Fluids.class,
        "COAL_TAR",
        FluidInitializer.createFluid(
            "coal_tar",
            true,
            fluid -> fluid.setDensity(3000).setViscosity(6000),
            BlockFluidCoalTar::new
        )
    );

    registry.registerBlockRegistrationStrategy(blockRegistry -> {

      for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCK_LIST) {
        Block block = (Block) fluidBlock;
        block.setRegistryName(ModuleTechRefractory.MOD_ID, "fluid." + fluidBlock.getFluid().getName());
        block.setUnlocalizedName(fluidBlock.getFluid().getUnlocalizedName());
        block.setCreativeTab(ModuleTechRefractory.CREATIVE_TAB);
        blockRegistry.register(block);
      }
    });

    registry.registerItemRegistrationStrategy(itemRegistry -> {

      for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCK_LIST) {
        final Block block = (Block) fluidBlock;
        final ItemBlock itemBlock = new ItemBlock(block);
        final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName());
        itemBlock.setRegistryName(registryName);
        itemRegistry.register(itemBlock);
      }

      for (final Fluid fluid : ALL_FLUID_LIST) {
        FluidRegistry.addBucketForFluid(fluid);
      }
    });
  }

  private static <T extends Block & IFluidBlock> Fluid createFluid(
      String name,
      boolean hasFlowIcon,
      Consumer<Fluid> fluidPropertyApplier,
      Function<Fluid, T> blockFactory
  ) {

    String texturePrefix = ModuleTechRefractory.MOD_ID + ":" + "blocks/fluid_";
    final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
    final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

    Fluid fluid = new Fluid(name, still, flowing);
    final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

    if (useOwnFluid) {
      fluidPropertyApplier.accept(fluid);
      MOD_FLUID_BLOCK_LIST.add(blockFactory.apply(fluid));

    } else {
      fluid = FluidRegistry.getFluid(name);
    }

    ALL_FLUID_LIST.add(fluid);

    return fluid;
  }

  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {
      for (IFluidBlock fluidBlock : FluidInitializer.MOD_FLUID_BLOCK_LIST) {
        final Item item = Item.getItemFromBlock((Block) fluidBlock);
        assert item != Items.AIR;

        ModelBakery.registerItemVariants(item);

        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
            ModuleTechRefractory.MOD_ID + ":" + "fluid",
            fluidBlock.getFluid().getName()
        );

        ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);

        ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {

          @Nonnull
          @Override
          protected ModelResourceLocation getModelResourceLocation(@Nonnull final IBlockState blockState) {

            return modelResourceLocation;
          }
        });
      }
    });
  }

  private FluidInitializer() {
    //
  }
}
