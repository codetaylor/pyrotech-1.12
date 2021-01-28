package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemHide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemHide(), ItemHide.NAME_COW);
    registry.registerItem(new ItemHide(), ItemHide.NAME_MOOSHROOM);
    registry.registerItem(new ItemHide(), ItemHide.NAME_POLAR_BEAR);
    registry.registerItem(new ItemHide(), ItemHide.NAME_PIG);
    registry.registerItem(new ItemHide(), ItemHide.NAME_BAT);
    registry.registerItem(new ItemHide(), ItemHide.NAME_HORSE);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_SHEARED);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_YELLOW);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_WHITE);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_GRAY_LIGHT);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_RED);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_PURPLE);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_PINK);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_ORANGE);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_MAGENTA);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_LIME);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_BLUE_LIGHT);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_GREEN);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_GRAY);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_CYAN);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_BROWN);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_BLUE);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_BLACK);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleHunting.Items.HIDE_COW,
          ModuleHunting.Items.HIDE_MOOSHROOM,
          ModuleHunting.Items.HIDE_POLAR_BEAR,
          ModuleHunting.Items.HIDE_PIG,
          ModuleHunting.Items.HIDE_BAT,
          ModuleHunting.Items.HIDE_HORSE,
          ModuleHunting.Items.HIDE_SHEEP_SHEARED,
          ModuleHunting.Items.HIDE_SHEEP_YELLOW,
          ModuleHunting.Items.HIDE_SHEEP_WHITE,
          ModuleHunting.Items.HIDE_SHEEP_GRAY_LIGHT,
          ModuleHunting.Items.HIDE_SHEEP_RED,
          ModuleHunting.Items.HIDE_SHEEP_PURPLE,
          ModuleHunting.Items.HIDE_SHEEP_PINK,
          ModuleHunting.Items.HIDE_SHEEP_ORANGE,
          ModuleHunting.Items.HIDE_SHEEP_MAGENTA,
          ModuleHunting.Items.HIDE_SHEEP_LIME,
          ModuleHunting.Items.HIDE_SHEEP_BLUE_LIGHT,
          ModuleHunting.Items.HIDE_SHEEP_GREEN,
          ModuleHunting.Items.HIDE_SHEEP_GRAY,
          ModuleHunting.Items.HIDE_SHEEP_CYAN,
          ModuleHunting.Items.HIDE_SHEEP_BROWN,
          ModuleHunting.Items.HIDE_SHEEP_BLUE,
          ModuleHunting.Items.HIDE_SHEEP_BLACK
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
