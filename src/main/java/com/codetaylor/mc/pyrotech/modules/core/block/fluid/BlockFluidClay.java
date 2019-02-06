package com.codetaylor.mc.pyrotech.modules.core.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidClay
    extends BlockFluidClassic {

  public BlockFluidClay(Fluid fluid) {

    super(fluid, Material.WATER);
  }
}