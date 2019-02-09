package com.codetaylor.mc.pyrotech.modules.tool.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import com.codetaylor.mc.pyrotech.modules.tool.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemCrudeAxe(), ItemCrudeAxe.NAME);
    registry.registerItem(new ItemCrudeHoe(), ItemCrudeHoe.NAME);
    registry.registerItem(new ItemCrudePickaxe(), ItemCrudePickaxe.NAME);
    registry.registerItem(new ItemCrudeShovel(), ItemCrudeShovel.NAME);
    registry.registerItem(new ItemCrudeFishingRod(), ItemCrudeFishingRod.NAME);

    registry.registerItem(new ItemBoneAxe(), ItemBoneAxe.NAME);
    registry.registerItem(new ItemBoneHoe(), ItemBoneHoe.NAME);
    registry.registerItem(new ItemBonePickaxe(), ItemBonePickaxe.NAME);
    registry.registerItem(new ItemBoneShovel(), ItemBoneShovel.NAME);
    registry.registerItem(new ItemBoneSword(), ItemBoneSword.NAME);

    registry.registerItem(new ItemFlintAxe(), ItemFlintAxe.NAME);
    registry.registerItem(new ItemFlintHoe(), ItemFlintHoe.NAME);
    registry.registerItem(new ItemFlintPickaxe(), ItemFlintPickaxe.NAME);
    registry.registerItem(new ItemFlintShovel(), ItemFlintShovel.NAME);
    registry.registerItem(new ItemFlintSword(), ItemFlintSword.NAME);

    registry.registerItem(new ItemUnfiredClayShears(), ItemUnfiredClayShears.NAME);
    registry.registerItem(new ItemClayShears(), ItemClayShears.NAME);
    registry.registerItem(new ItemStoneShears(), ItemStoneShears.NAME);
    registry.registerItem(new ItemBoneShears(), ItemBoneShears.NAME);
    registry.registerItem(new ItemFlintShears(), ItemFlintShears.NAME);
    registry.registerItem(new ItemDiamondShears(), ItemDiamondShears.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          "tool",
          ModuleTool.Items.CRUDE_AXE,
          ModuleTool.Items.CRUDE_HOE,
          ModuleTool.Items.CRUDE_PICKAXE,
          ModuleTool.Items.CRUDE_SHOVEL,
          ModuleTool.Items.CRUDE_FISHING_ROD,

          ModuleTool.Items.BONE_AXE,
          ModuleTool.Items.BONE_HOE,
          ModuleTool.Items.BONE_PICKAXE,
          ModuleTool.Items.BONE_SHOVEL,
          ModuleTool.Items.BONE_SWORD,

          ModuleTool.Items.FLINT_AXE,
          ModuleTool.Items.FLINT_HOE,
          ModuleTool.Items.FLINT_PICKAXE,
          ModuleTool.Items.FLINT_SHOVEL,
          ModuleTool.Items.FLINT_SWORD,

          ModuleTool.Items.UNFIRED_CLAY_SHEARS,
          ModuleTool.Items.CLAY_SHEARS,
          ModuleTool.Items.STONE_SHEARS,
          ModuleTool.Items.BONE_SHEARS,
          ModuleTool.Items.FLINT_SHEARS,
          ModuleTool.Items.DIAMOND_SHEARS
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
