package com.codetaylor.mc.pyrotech;

import com.codetaylor.mc.pyrotech.internal.IPyrotechAPI;
import com.codetaylor.mc.pyrotech.internal.PyrotechAPI_NoOp;
import net.minecraft.item.Item;

public class PyrotechAPI {

  /**
   * Register a hammer for use with the mod. This is required to ensure
   * Pyrotech recognizes your hammer without having to add it to the config.
   *
   * @param item         the hammer to register
   * @param harvestLevel the hammer's harvest level
   */
  public static void registerHammer(Item item, int harvestLevel) {

    API.registerHammer(item, harvestLevel);
  }

  /**
   * For internal use only.
   * <p>
   * This field is injected with the correct api during the mod's construction,
   * replacing the no-op instance the field is initialized with.
   * <p>
   * Stay away.
   */
  private static final IPyrotechAPI API = new PyrotechAPI_NoOp();

}
