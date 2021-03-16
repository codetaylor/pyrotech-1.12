package com.codetaylor.mc.pyrotech.modules.core.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidPyroberryWine
    extends BlockFluidClassic {

  public BlockFluidPyroberryWine(Fluid fluid) {

    super(fluid, Material.WATER);
  }
}