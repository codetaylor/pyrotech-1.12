package com.codetaylor.mc.pyrotech.library.blockrenderer;

import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Function;

public class ModBulkRenderItemSupplier
    implements
    IBulkRenderItemSupplier {

  private static final Logger LOGGER = LogManager.getLogger(ModBulkRenderItemSupplier.class);

  private final String modId;
  private final Function<ItemStack, RenderItemData> renderItemDataFunction;

  public ModBulkRenderItemSupplier(String modId, Function<ItemStack, RenderItemData> renderItemDataFunction) {

    this.modId = modId;
    this.renderItemDataFunction = renderItemDataFunction;
  }

  @Override
  public List<RenderItemData> get() {

    List<RenderItemData> result = Lists.newArrayList();

    for (ResourceLocation resourceLocation : Item.REGISTRY.getKeys()) {

      if (resourceLocation != null
          && this.modId.equals(resourceLocation.getResourceDomain())) {

        NonNullList<ItemStack> results = NonNullList.create();
        Item item = Item.REGISTRY.getObject(resourceLocation);

        if (item == null) {
          LOGGER.warn("Registry is missing item for " + resourceLocation);

        } else {

          try {
            CreativeTabs creativeTab = item.getCreativeTab();

            if (creativeTab != null) {
              item.getSubItems(creativeTab, results);

            } else {
              LOGGER.warn("Item has no creative tab, skipping item for " + resourceLocation);
            }

          } catch (Throwable t) {
            LOGGER.warn("Failed to get renderable items for " + resourceLocation, t);
          }
        }

        for (ItemStack itemStack : results) {
          result.add(this.renderItemDataFunction.apply(itemStack));
        }
      }
    }

    return result;
  }
}
