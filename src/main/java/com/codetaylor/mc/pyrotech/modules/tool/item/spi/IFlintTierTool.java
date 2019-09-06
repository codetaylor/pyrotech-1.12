package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

public interface IFlintTierTool
    extends ITierTool {

  @Override
  default String getToolTierName() {

    return "flint";
  }

}
