package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.*;

public class PotionInitializer {

  public static void onRegister(Registry registry) {

    registry.registerPotion(new PotionComfort(), PotionComfort.NAME);
    registry.registerPotion(new PotionWellFed(), PotionWellFed.NAME);
    registry.registerPotion(new PotionResting(), PotionResting.NAME);
    registry.registerPotion(new PotionWellRested(), PotionWellRested.NAME);
    registry.registerPotion(new PotionFocused(), PotionFocused.NAME);
  }
}
