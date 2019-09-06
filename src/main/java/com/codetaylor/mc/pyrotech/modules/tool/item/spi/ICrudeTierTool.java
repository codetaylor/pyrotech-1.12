package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

public interface ICrudeTierTool
    extends ITierTool {

  @Override
  default String getToolTierName() {

    return "crude";
  }

}
