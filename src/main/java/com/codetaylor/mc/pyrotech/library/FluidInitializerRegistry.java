package com.codetaylor.mc.pyrotech.library;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.registry.strategy.IClientModelRegistrationStrategy;
import com.codetaylor.mc.athenaeum.registry.strategy.IForgeRegistryEventRegistrationStrategy;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FluidInitializerRegistry {

  private final List<Fluid> allFluidList = new ArrayList<>();
  private final List<IFluidBlock> modFluidBlockList = new ArrayList<>();
  private final String modId;
  private final CreativeTabs creativeTab;

  public FluidInitializerRegistry(String modId, CreativeTabs creativeTab) {

    this.modId = modId;
    this.creativeTab = creativeTab;
  }

  public List<Fluid> getAllFluidList() {

    return this.allFluidList;
  }

  public List<IFluidBlock> getModFluidBlockList() {

    return this.modFluidBlockList;
  }

  public <T extends Block & IFluidBlock> Fluid createFluid(
      String name,
      boolean hasFlowIcon,
      Consumer<Fluid> fluidPropertyApplier,
      Function<Fluid, T> blockFactory
  ) {

    String texturePrefix = this.modId + ":" + "blocks/fluid_";
    final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
    final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

    Fluid fluid = new Fluid(name, still, flowing);
    final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

    if (useOwnFluid) {
      fluidPropertyApplier.accept(fluid);
      modFluidBlockList.add(blockFactory.apply(fluid));

    } else {
      fluid = FluidRegistry.getFluid(name);
    }

    allFluidList.add(fluid);

    return fluid;
  }

  public void registerClientModelRegistrationStrategies(Registry registry) {

    registry.registerClientModelRegistrationStrategy(new FluidClientModelRegistrationStrategy(
        this.modId,
        this.getModFluidBlockList()
    ));
  }

  public void registerRegistrationStrategies(Registry registry) {

    registry.registerBlockRegistrationStrategy(new FluidInitializerRegistry.FluidBlockRegistrationStrategy(
        this.modId,
        this.creativeTab,
        this.getModFluidBlockList()
    ));

    registry.registerItemRegistrationStrategy(new FluidInitializerRegistry.FluidItemRegistrationStrategy(
        this.getModFluidBlockList(),
        this.getAllFluidList()
    ));
  }

  public static class FluidBlockRegistrationStrategy
      implements IForgeRegistryEventRegistrationStrategy<Block> {

    private final String modId;
    private final List<IFluidBlock> modFluidBlockList;
    private final CreativeTabs creativeTab;

    public FluidBlockRegistrationStrategy(String modId, CreativeTabs creativeTab, List<IFluidBlock> modFluidBlockList) {

      this.modId = modId;
      this.modFluidBlockList = modFluidBlockList;
      this.creativeTab = creativeTab;
    }

    @Override
    public void register(IForgeRegistry<Block> registry) {

      for (final IFluidBlock fluidBlock : this.modFluidBlockList) {
        Block block = (Block) fluidBlock;
        block.setRegistryName(this.modId, "fluid." + fluidBlock.getFluid().getName());
        block.setUnlocalizedName(fluidBlock.getFluid().getUnlocalizedName());
        block.setCreativeTab(this.creativeTab);
        registry.register(block);
      }
    }
  }

  public static class FluidItemRegistrationStrategy
      implements IForgeRegistryEventRegistrationStrategy<Item> {

    private final List<IFluidBlock> modFluidBlockList;
    private final List<Fluid> allFluidList;

    public FluidItemRegistrationStrategy(List<IFluidBlock> modFluidBlockList, List<Fluid> allFluidList) {

      this.modFluidBlockList = modFluidBlockList;
      this.allFluidList = allFluidList;
    }

    @Override
    public void register(IForgeRegistry<Item> registry) {

      for (final IFluidBlock fluidBlock : this.modFluidBlockList) {
        final Block block = (Block) fluidBlock;
        final ItemBlock itemBlock = new ItemBlock(block);
        final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName());
        itemBlock.setRegistryName(registryName);
        registry.register(itemBlock);
      }

      for (final Fluid fluid : this.allFluidList) {
        FluidRegistry.addBucketForFluid(fluid);
      }
    }
  }

  public static class FluidClientModelRegistrationStrategy
      implements IClientModelRegistrationStrategy {

    private final String modId;
    private final List<IFluidBlock> modFluidBlockList;

    public FluidClientModelRegistrationStrategy(String modId, List<IFluidBlock> modFluidBlockList) {

      this.modId = modId;
      this.modFluidBlockList = modFluidBlockList;
    }

    @Override
    public void register() {

      for (IFluidBlock fluidBlock : this.modFluidBlockList) {
        final Item item = Item.getItemFromBlock((Block) fluidBlock);
        assert item != Items.AIR;

        ModelBakery.registerItemVariants(item);

        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
            this.modId + ":" + "fluid",
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
    }
  }

}
