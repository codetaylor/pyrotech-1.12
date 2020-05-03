package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallow;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallowRoasted;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallowStick;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemTinder;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static final ItemTinder TINDER = new ItemTinder();
  public static final ItemMarshmallow MARSHMALLOW = new ItemMarshmallow();
  public static final ItemMarshmallowRoasted MARSHMALLOW_ROASTED = new ItemMarshmallowRoasted();
  public static final ItemMarshmallowStick MARSHMALLOW_STICK = new ItemMarshmallowStick();

  public static void onRegister(Registry registry) {

    registry.registerItem(TINDER, ItemTinder.NAME);
    registry.registerItem(MARSHMALLOW, ItemMarshmallow.NAME);
    registry.registerItem(MARSHMALLOW_ROASTED, ItemMarshmallow.NAME_ROASTED);
    registry.registerItem(MARSHMALLOW_STICK, ItemMarshmallowStick.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ItemInitializer.TINDER,
          ItemInitializer.MARSHMALLOW,
          ItemInitializer.MARSHMALLOW_STICK,
          ItemInitializer.MARSHMALLOW_ROASTED
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
