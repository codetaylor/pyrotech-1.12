package com.codetaylor.mc.pyrotech.modules.ignition.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
import com.codetaylor.mc.pyrotech.modules.ignition.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemBowDrill(), ItemBowDrill.NAME);
    registry.registerItem(new ItemBowDrillDurable(), ItemBowDrillDurable.NAME);
    registry.registerItem(new ItemBowDrillDurableSpindle(), ItemBowDrillDurableSpindle.NAME);
    registry.registerItem(new ItemFlintAndTinder(), ItemFlintAndTinder.NAME);
    registry.registerItem(new ItemMatchstick(), ItemMatchstick.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleIgnition.Items.BOW_DRILL,
          ModuleIgnition.Items.BOW_DRILL_DURABLE,
          ModuleIgnition.Items.BOW_DRILL_DURABLE_SPINDLE,
          ModuleIgnition.Items.FLINT_AND_TINDER,
          ModuleIgnition.Items.MATCHSTICK
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
