package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static final ItemTinder TINDER = new ItemTinder();
  public static final ItemMarshmallow MARSHMALLOW = new ItemMarshmallow();
  public static final ItemMarshmallowRoasted MARSHMALLOW_ROASTED = new ItemMarshmallowRoasted();
  public static final ItemMarshmallowStick MARSHMALLOW_STICK = new ItemMarshmallowStick();
  public static final ItemMarshmallowStickEmpty MARSHMALLOW_STICK_EMPTY = new ItemMarshmallowStickEmpty();
  public static final ItemMarshmallowBurned MARSHMALLOW_BURNED = new ItemMarshmallowBurned();

  public static void onRegister(Registry registry) {

    registry.registerItem(TINDER, ItemTinder.NAME);
    registry.registerItem(MARSHMALLOW, ItemMarshmallow.NAME);
    registry.registerItem(MARSHMALLOW_ROASTED, ItemMarshmallowRoasted.NAME);
    registry.registerItem(MARSHMALLOW_STICK, new ResourceLocation(ModuleTechBasic.MOD_ID, ItemMarshmallowStick.NAME), true);
    registry.registerItem(MARSHMALLOW_STICK_EMPTY, ItemMarshmallowStickEmpty.NAME);
    registry.registerItem(MARSHMALLOW_BURNED, ItemMarshmallowBurned.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ItemInitializer.TINDER,
          ItemInitializer.MARSHMALLOW,
          ItemInitializer.MARSHMALLOW_STICK,
          ItemInitializer.MARSHMALLOW_STICK_EMPTY,
          ItemInitializer.MARSHMALLOW_ROASTED,
          ItemInitializer.MARSHMALLOW_BURNED
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
