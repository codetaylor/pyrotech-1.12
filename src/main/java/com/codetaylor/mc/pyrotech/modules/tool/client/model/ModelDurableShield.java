package com.codetaylor.mc.pyrotech.modules.tool.client.model;

import com.codetaylor.mc.pyrotech.library.IModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelDurableShield
    extends ModelBase
    implements IModelRenderer {

  public ModelRenderer plate;
  public ModelRenderer handle;

  public ModelDurableShield() {

    this.textureWidth = 64;
    this.textureHeight = 64;
    this.plate = new ModelRenderer(this, 0, 0);
    this.plate.addBox(-6, -11, -2, 12, 21, 1, 0);
    this.handle = new ModelRenderer(this, 26, 0);
    this.handle.addBox(-1, -3, -1, 2, 6, 6, 0);
  }

  @Override
  public void render() {

    this.plate.render(0.0625f);
    this.handle.render(0.0625f);
  }
}
