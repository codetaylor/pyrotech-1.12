package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemTinder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static final ItemTinder TINDER = new ItemTinder();

  public static void onRegister(Registry registry) {

    registry.registerItem(ItemInitializer.TINDER, ItemTinder.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ItemInitializer.TINDER
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
