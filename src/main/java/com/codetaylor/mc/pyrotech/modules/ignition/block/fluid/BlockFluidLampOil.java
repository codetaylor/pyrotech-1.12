package com.codetaylor.mc.pyrotech.modules.ignition.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidLampOil
    extends BlockFluidClassic {

  public BlockFluidLampOil(Fluid fluid) {

    super(fluid, Material.WATER);
  }

}