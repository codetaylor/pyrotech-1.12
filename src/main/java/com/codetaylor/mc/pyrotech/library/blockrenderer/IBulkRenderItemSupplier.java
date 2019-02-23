package com.codetaylor.mc.pyrotech.library.blockrenderer;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public interface IBulkRenderItemSupplier
    extends Supplier<List<ItemStack>> {

  @Override
  List<ItemStack> get();
}
