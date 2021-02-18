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

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemTinder(), ItemTinder.NAME);
    registry.registerItem(new ItemMarshmallow(), ItemMarshmallow.NAME);
    registry.registerItem(new ItemMarshmallowRoasted(), ItemMarshmallowRoasted.NAME);
    registry.registerItem(new ItemMarshmallowStick(), new ResourceLocation(ModuleTechBasic.MOD_ID, ItemMarshmallowStick.NAME), true);
    registry.registerItem(new ItemMarshmallowStickEmpty(), ItemMarshmallowStickEmpty.NAME);
    registry.registerItem(new ItemMarshmallowBurned(), ItemMarshmallowBurned.NAME);
    registry.registerItem(new ItemBarrelLid(), ItemBarrelLid.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleTechBasic.Items.TINDER,
          ModuleTechBasic.Items.MARSHMALLOW,
          ModuleTechBasic.Items.MARSHMALLOW_STICK,
          ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY,
          ModuleTechBasic.Items.MARSHMALLOW_ROASTED,
          ModuleTechBasic.Items.MARSHMALLOW_BURNED,
          ModuleTechBasic.Items.BARREL_LID
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
