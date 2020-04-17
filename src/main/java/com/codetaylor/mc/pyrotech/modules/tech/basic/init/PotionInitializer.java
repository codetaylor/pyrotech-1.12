package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionComfort;

public class PotionInitializer {

  public static void onRegister(Registry registry) {

    registry.registerPotion(new PotionComfort(), PotionComfort.NAME);
  }
}
