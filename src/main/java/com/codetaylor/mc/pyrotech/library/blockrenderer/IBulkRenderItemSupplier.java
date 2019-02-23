package com.codetaylor.mc.pyrotech.library.blockrenderer;

import java.util.List;
import java.util.function.Supplier;

public interface IBulkRenderItemSupplier
    extends Supplier<List<RenderItemData>> {

  @Override
  List<RenderItemData> get();
}
