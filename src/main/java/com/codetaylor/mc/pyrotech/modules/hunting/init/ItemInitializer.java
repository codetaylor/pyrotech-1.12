package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemHide;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemPelt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemHide(), ItemHide.NAME_PIG);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_SHEARED);

    registry.registerItem(new ItemPelt(), ItemPelt.NAME_COW);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_MOOSHROOM);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_POLAR_BEAR);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_BAT);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_HORSE);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_YELLOW);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_WHITE);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_GRAY_LIGHT);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_RED);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_PURPLE);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_PINK);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_ORANGE);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_MAGENTA);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_LIME);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_BLUE_LIGHT);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_GREEN);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_GRAY);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_CYAN);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_BROWN);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_BLUE);
    registry.registerItem(new ItemPelt(), ItemPelt.NAME_SHEEP_BLACK);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleHunting.Items.HIDE_PIG,
          ModuleHunting.Items.HIDE_SHEEP_SHEARED,
          ModuleHunting.Items.PELT_COW,
          ModuleHunting.Items.PELT_MOOSHROOM,
          ModuleHunting.Items.PELT_POLAR_BEAR,
          ModuleHunting.Items.PELT_BAT,
          ModuleHunting.Items.PELT_HORSE,
          ModuleHunting.Items.PELT_SHEEP_YELLOW,
          ModuleHunting.Items.PELT_SHEEP_WHITE,
          ModuleHunting.Items.PELT_SHEEP_GRAY_LIGHT,
          ModuleHunting.Items.PELT_SHEEP_RED,
          ModuleHunting.Items.PELT_SHEEP_PURPLE,
          ModuleHunting.Items.PELT_SHEEP_PINK,
          ModuleHunting.Items.PELT_SHEEP_ORANGE,
          ModuleHunting.Items.PELT_SHEEP_MAGENTA,
          ModuleHunting.Items.PELT_SHEEP_LIME,
          ModuleHunting.Items.PELT_SHEEP_BLUE_LIGHT,
          ModuleHunting.Items.PELT_SHEEP_GREEN,
          ModuleHunting.Items.PELT_SHEEP_GRAY,
          ModuleHunting.Items.PELT_SHEEP_CYAN,
          ModuleHunting.Items.PELT_SHEEP_BROWN,
          ModuleHunting.Items.PELT_SHEEP_BLUE,
          ModuleHunting.Items.PELT_SHEEP_BLACK
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
