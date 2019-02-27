package com.codetaylor.mc.pyrotech;

import net.minecraft.util.text.TextFormatting;

public class Reference {

  public static final String MOD_ID = "pyrotech";
  public static final String VERSION = "@@VERSION@@";
  public static final String NAME = "Pyrotech";

  public static final boolean IS_DEV = VERSION.equals("@@" + "VERSION" + "@@");

  public static class PitKiln {

    public static final int DEFAULT_BURN_TIME_TICKS = 14 * 60 * 20;
    public static final float DEFAULT_FAILURE_CHANCE = 0.33f;
  }

  public static class StoneKiln {

    public static final int DEFAULT_BURN_TIME_TICKS = 7 * 60 * 20;
    public static final float DEFAULT_FAILURE_CHANCE = 0.05f;
  }

  public static class Tooltip {

    public static final TextFormatting COLOR_EXTENDED_INFO = TextFormatting.GOLD;
    public static final TextFormatting COLOR_EXTENDED_INFO_HIGHLIGHT = TextFormatting.YELLOW;
  }

}
