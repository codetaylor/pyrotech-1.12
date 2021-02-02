package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.item.*;
import com.google.common.base.Preconditions;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemHide(), ItemHide.NAME_PIG);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SHEEP_SHEARED);
    registry.registerItem(new ItemHide(), ItemHide.NAME_WASHED);
    registry.registerItem(new ItemHide(), ItemHide.NAME_SMALL_WASHED);

    registry.registerItem(new ItemHideScraped(), ItemHideScraped.NAME);
    registry.registerItem(new ItemHideSmallScraped(), ItemHideSmallScraped.NAME);

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

    registry.registerItem(new ItemHuntingKnife(EnumMaterial.BONE.getToolMaterial(), ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("bone")), ItemHuntingKnife.BONE_NAME);
    registry.registerItem(new ItemHuntingKnife(EnumMaterial.FLINT.getToolMaterial(), ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("flint")), ItemHuntingKnife.FLINT_NAME);
    registry.registerItem(new ItemHuntingKnife(Item.ToolMaterial.STONE, ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("stone")), ItemHuntingKnife.STONE_NAME);
    registry.registerItem(new ItemHuntingKnife(Item.ToolMaterial.IRON, ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("iron")), ItemHuntingKnife.IRON_NAME);
    registry.registerItem(new ItemHuntingKnife(Item.ToolMaterial.GOLD, ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("gold")), ItemHuntingKnife.GOLD_NAME);
    registry.registerItem(new ItemHuntingKnife(Item.ToolMaterial.DIAMOND, ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("diamond")), ItemHuntingKnife.DIAMOND_NAME);
    registry.registerItem(new ItemHuntingKnife(Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN), ModuleHuntingConfig.HUNTING_KNIFE_DURABILITY.get("obsidian")), ItemHuntingKnife.OBSIDIAN_NAME);

    registry.registerItem(new ItemLeatherSheet(), ItemLeatherSheet.NAME);
    registry.registerItem(new ItemLeatherStrap(), ItemLeatherStrap.NAME);
    registry.registerItem(new ItemLeatherCord(), ItemLeatherCord.NAME);
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
          ModuleHunting.Items.PELT_SHEEP_BLACK,
          ModuleHunting.Items.BONE_HUNTING_KNIFE,
          ModuleHunting.Items.FLINT_HUNTING_KNIFE,
          ModuleHunting.Items.STONE_HUNTING_KNIFE,
          ModuleHunting.Items.IRON_HUNTING_KNIFE,
          ModuleHunting.Items.GOLD_HUNTING_KNIFE,
          ModuleHunting.Items.DIAMOND_HUNTING_KNIFE,
          ModuleHunting.Items.OBSIDIAN_HUNTING_KNIFE,
          ModuleHunting.Items.HIDE_WASHED,
          ModuleHunting.Items.HIDE_SMALL_WASHED,
          ModuleHunting.Items.HIDE_SCRAPED,
          ModuleHunting.Items.HIDE_SMALL_SCRAPED,
          ModuleHunting.Items.LEATHER_SHEET,
          ModuleHunting.Items.LEATHER_STRAP,
          ModuleHunting.Items.LEATHER_CORD
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
