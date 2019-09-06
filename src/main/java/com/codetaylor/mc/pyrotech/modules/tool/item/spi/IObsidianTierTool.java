package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

public interface IObsidianTierTool
    extends ITierTool {

  @Override
  default String getToolTierName() {

    return "obsidian";
  }

}
