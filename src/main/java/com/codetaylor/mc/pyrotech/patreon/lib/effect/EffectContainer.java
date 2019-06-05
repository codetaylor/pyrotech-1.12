package com.codetaylor.mc.pyrotech.patreon.lib.effect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds all effects for all mods.
 */
public class EffectContainer {

  public static final Map<String, Boolean> LOADING_FINISHED = new ConcurrentHashMap<>();
  public static final Map<String, List<EffectBase>> EFFECTS = new ConcurrentHashMap<>();
}
