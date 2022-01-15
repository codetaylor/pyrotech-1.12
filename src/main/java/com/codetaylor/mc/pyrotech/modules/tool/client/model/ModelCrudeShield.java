package com.codetaylor.mc.pyrotech.modules.tool.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCrudeShield
    extends ModelBase {

  public ModelRenderer plate;
  public ModelRenderer handle;

  public ModelCrudeShield() {

    this.textureWidth = 32;
    this.textureHeight = 32;
    this.plate = new ModelRenderer(this, 0, 0);
    this.plate.addBox(-5, -5, -2, 10, 10, 1, 0);
    this.handle = new ModelRenderer(this, 0, 11);
    this.handle.addBox(-1, -3, -1, 2, 6, 6, 0);
  }

  public void render() {

    this.plate.render(0.0625f);
    this.handle.render(0.0625f);
  }
}
