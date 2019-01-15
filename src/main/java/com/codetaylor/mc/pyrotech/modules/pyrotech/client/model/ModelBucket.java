package com.codetaylor.mc.pyrotech.modules.pyrotech.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class ModelBucket
    implements IModel {

  private final IModel model;
  private final ResourceLocation baseLocation;
  private final ResourceLocation liquidLocation;
  private final ResourceLocation coverLocation;
  private final Fluid fluid;
  private final boolean flipGas;

  public ModelBucket() {

    this.model = ModelDynBucket.MODEL;
    this.baseLocation = null;
    this.liquidLocation = null;
    this.coverLocation = null;
    this.fluid = null;
    this.flipGas = false;
  }

  public ModelBucket(ResourceLocation baseLocation, ResourceLocation liquidLocation, ResourceLocation coverLocation, Fluid fluid, boolean flipGas) {

    this.model = new ModelDynBucket(baseLocation, liquidLocation, coverLocation, fluid, flipGas);
    this.baseLocation = baseLocation;
    this.liquidLocation = liquidLocation;
    this.coverLocation = coverLocation;
    this.fluid = fluid;
    this.flipGas = flipGas;
  }

  @Override
  public IModel process(ImmutableMap<String, String> customData) {

    return null;
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

    return this.model.bake(state, format, bakedTextureGetter);
  }

  @Nonnull
  @Override
  public Collection<ResourceLocation> getDependencies() {

    return Collections.emptyList();
  }

  @Nonnull
  @Override
  public Collection<ResourceLocation> getTextures() {

    return this.model.getTextures();
  }

  @Nonnull
  @Override
  public IModelState getDefaultState() {

    return this.model.getDefaultState();
  }
}
