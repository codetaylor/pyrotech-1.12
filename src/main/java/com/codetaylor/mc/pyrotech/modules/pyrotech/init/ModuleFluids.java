package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.fluid.BlockFluidCoalTar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.fluid.BlockFluidWoodTar;
import com.google.common.base.Preconditions;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModuleFluids {

  private static final List<Fluid> ALL_FLUID_LIST = new ArrayList<>();
  private static final List<IFluidBlock> MOD_FLUID_BLOCK_LIST = new ArrayList<>();

  public static final Fluid WOOD_TAR = ModuleFluids.createFluid(
      "wood_tar",
      true,
      fluid -> fluid.setDensity(3000).setViscosity(6000),
      BlockFluidWoodTar::new
  );

  public static final Fluid COAL_TAR = ModuleFluids.createFluid(
      "coal_tar",
      true,
      fluid -> fluid.setDensity(3000).setViscosity(6000),
      BlockFluidCoalTar::new
  );

  private static <T extends Block & IFluidBlock> Fluid createFluid(
      String name,
      boolean hasFlowIcon,
      Consumer<Fluid> fluidPropertyApplier,
      Function<Fluid, T> blockFactory
  ) {

    String texturePrefix = ModulePyrotech.MOD_ID + ":" + "blocks/fluid_";
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

  @Mod.EventBusSubscriber(modid = ModulePyrotech.MOD_ID)
  public static class RegistrationHandler {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {

      final IForgeRegistry<Block> registry = event.getRegistry();

      for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCK_LIST) {
        Block block = (Block) fluidBlock;
        block.setRegistryName(ModulePyrotech.MOD_ID, "fluid." + fluidBlock.getFluid().getName());
        block.setUnlocalizedName(fluidBlock.getFluid().getUnlocalizedName());
        block.setCreativeTab(ModulePyrotech.CREATIVE_TAB);
        registry.register(block);
      }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerItems(final RegistryEvent.Register<Item> event) {

      final IForgeRegistry<Item> registry = event.getRegistry();

      for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCK_LIST) {
        final Block block = (Block) fluidBlock;
        final ItemBlock itemBlock = new ItemBlock(block);
        final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName());
        itemBlock.setRegistryName(registryName);
        registry.register(itemBlock);
      }

      ModuleFluids.registerFluidContainers();
    }
  }

  @Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModulePyrotech.MOD_ID)
  public static class ClientRegistrationHandler {

    @SubscribeEvent
    public static void registerFluidModels(ModelRegistryEvent event) {

      ModuleFluids.MOD_FLUID_BLOCK_LIST.forEach(ClientRegistrationHandler::registerFluidModel);
    }

    private static void registerFluidModel(IFluidBlock fluidBlock) {

      final Item item = Item.getItemFromBlock((Block) fluidBlock);
      assert item != Items.AIR;

      ModelBakery.registerItemVariants(item);

      final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
          ModulePyrotech.MOD_ID + ":" + "fluid",
          fluidBlock.getFluid().getName()
      );

      ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);

      ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {

        @Override
        protected ModelResourceLocation getModelResourceLocation(final IBlockState p_178132_1_) {

          return modelResourceLocation;
        }
      });

    }

  }

  private static void registerFluidContainers() {

    for (final Fluid fluid : ALL_FLUID_LIST) {
      FluidRegistry.addBucketForFluid(fluid);
    }
  }
}
