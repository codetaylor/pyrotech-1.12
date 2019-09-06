package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

public interface IBoneTierTool
    extends ITierTool {

  @Override
  default String getToolTierName() {

    return "bone";
  }

}
