package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.util.TooltipHelper;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarDrain;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltipEventHandler {

  public static final List<ParseResult> VALID_REFRACTORY_ITEM_BLOCKS = new ArrayList<>();

  @SubscribeEvent
  public void on(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();
    Item item = itemStack.getItem();
    List<String> tooltip = event.getToolTip();

    if (item instanceof ItemBlock) {
      Block block = ((ItemBlock) item).getBlock();

      if (block instanceof BlockTarCollector) {
        this.addTarCollectorTooltip(itemStack, tooltip);

      } else if (block instanceof BlockTarDrain) {
        this.addTarDrainTooltip(itemStack, tooltip);
      }
    }

    for (ParseResult parseResult : VALID_REFRACTORY_ITEM_BLOCKS) {

      if (parseResult.matches(itemStack, true)) {
        this.addValidRefractoryBlock(tooltip);
        break;
      }
    }
  }

  private void addTarCollectorTooltip(ItemStack itemStack, List<String> tooltip) {

    if (itemStack.getMetadata() == BlockTarCollector.EnumType.STONE.getMeta()) {
      this.addCapacity(tooltip, ModuleTechRefractoryConfig.STONE_TAR_COLLECTOR.CAPACITY);
      this.addHotFluids(tooltip, ModuleTechRefractoryConfig.STONE_TAR_COLLECTOR.HOLDS_HOT_FLUIDS);

    } else if (itemStack.getMetadata() == BlockTarCollector.EnumType.BRICK.getMeta()) {
      this.addCapacity(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.CAPACITY);
      this.addHotFluids(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.HOLDS_HOT_FLUIDS);
    }
  }

  private void addTarDrainTooltip(ItemStack itemStack, List<String> tooltip) {

    if (itemStack.getMetadata() == BlockTarCollector.EnumType.STONE.getMeta()) {
      this.addCapacity(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.CAPACITY);
      this.addHotFluids(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.HOLDS_HOT_FLUIDS);
      this.addRange(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.RANGE);

    } else if (itemStack.getMetadata() == BlockTarCollector.EnumType.BRICK.getMeta()) {
      this.addCapacity(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.CAPACITY);
      this.addHotFluids(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.HOLDS_HOT_FLUIDS);
      this.addRange(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.RANGE);
    }
  }

  private void addValidRefractoryBlock(List<String> tooltip) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocal("gui.pyrotech.tooltip.refractory.valid"), 1);
  }

  private void addCapacity(List<String> tooltip, int capacity) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid.capacity", capacity), 1);
  }

  private void addHotFluids(List<String> tooltip, boolean holdsHotFluids) {

    TooltipHelper.addTooltip(tooltip, (holdsHotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocal("gui.pyrotech.tooltip.hot.fluids." + holdsHotFluids), 2);
  }

  private void addRange(List<String> tooltip, int range) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.drain", range * 2 + 1, range * 2 + 1), 3);
  }
}
