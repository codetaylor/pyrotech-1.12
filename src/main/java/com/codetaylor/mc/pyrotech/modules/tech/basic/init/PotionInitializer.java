package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionComfort;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionResting;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionWellFed;

public class PotionInitializer {

  public static void onRegister(Registry registry) {

    registry.registerPotion(new PotionComfort(), PotionComfort.NAME);
    registry.registerPotion(new PotionWellFed(), PotionWellFed.NAME);
    registry.registerPotion(new PotionResting(), PotionResting.NAME);
  }
}
