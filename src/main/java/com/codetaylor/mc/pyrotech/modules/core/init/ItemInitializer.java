package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    final ItemMaterial itemMaterial = new ItemMaterial();
    registry.registerItem(itemMaterial, ItemMaterial.NAME);
    registry.registerItem(new ItemMulch(), ItemMulch.NAME);

    registry.registerItem(new ItemAppleBaked(), ItemAppleBaked.NAME);
    registry.registerItem(new ItemCarrotRoasted(), ItemCarrotRoasted.NAME);
    registry.registerItem(new ItemEggRoasted(), ItemEggRoasted.NAME);
    registry.registerItem(new ItemMushroomBrownRoasted(), ItemMushroomBrownRoasted.NAME);
    registry.registerItem(new ItemMushroomRedRoasted(), ItemMushroomRedRoasted.NAME);
    registry.registerItem(new ItemBeetrootRoasted(), ItemBeetrootRoasted.NAME);
    registry.registerItem(new ItemBurnedFood(), ItemBurnedFood.NAME);
    registry.registerItem(new ItemStrangeTuber(), ItemStrangeTuber.NAME);
    registry.registerItem(new ItemPyroberryWine(), ItemPyroberryWine.NAME);
    registry.registerItem(new ItemGloamberryWine(), ItemGloamberryWine.NAME);
    registry.registerItem(new ItemFreckleberryWine(), ItemFreckleberryWine.NAME);
    registry.registerItem(new ItemTaintedMeat(), ItemTaintedMeat.NAME);

    registry.registerItem(new ItemCrudeHammer(), ItemCrudeHammer.NAME);
    registry.registerItem(new ItemStoneHammer(), ItemStoneHammer.NAME);
    registry.registerItem(new ItemBoneHammer(), ItemBoneHammer.NAME);
    registry.registerItem(new ItemBoneHammerDurable(), ItemBoneHammerDurable.NAME);
    registry.registerItem(new ItemFlintHammer(), ItemFlintHammer.NAME);
    registry.registerItem(new ItemFlintHammerDurable(), ItemFlintHammerDurable.NAME);
    registry.registerItem(new ItemIronHammer(), ItemIronHammer.NAME);
    registry.registerItem(new ItemGoldHammer(), ItemGoldHammer.NAME);
    registry.registerItem(new ItemDiamondHammer(), ItemDiamondHammer.NAME);
    registry.registerItem(new ItemObsidianHammer(), ItemObsidianHammer.NAME);

    registry.registerItem(new ItemBook(), ItemBook.NAME);

    registry.registerItem(new ItemPyroberrySeeds(), ItemPyroberrySeeds.NAME);
    registry.registerItem(new ItemPyroberries(), ItemPyroberries.NAME);
    registry.registerItem(new ItemPyroberryCocktail(), ItemPyroberryCocktail.NAME);

    registry.registerItem(new ItemGloamberrySeeds(), ItemGloamberrySeeds.NAME);
    registry.registerItem(new ItemGloamberries(), ItemGloamberries.NAME);

    registry.registerItem(new ItemFreckleberrySeeds(), ItemFreckleberrySeeds.NAME);
    registry.registerItem(new ItemFreckleberries(), ItemFreckleberries.NAME);

    registry.registerItem(new ItemFurnaceCore(), ItemFurnaceCore.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleCore.Items.MULCH,

          ModuleCore.Items.APPLE_BAKED,
          ModuleCore.Items.CARROT_ROASTED,
          ModuleCore.Items.EGG_ROASTED,
          ModuleCore.Items.MUSHROOM_BROWN_ROASTED,
          ModuleCore.Items.MUSHROOM_RED_ROASTED,
          ModuleCore.Items.BEETROOT_ROASTED,
          ModuleCore.Items.BURNED_FOOD,
          ModuleCore.Items.STRANGE_TUBER,

          ModuleCore.Items.CRUDE_HAMMER,
          ModuleCore.Items.STONE_HAMMER,
          ModuleCore.Items.FLINT_HAMMER,
          ModuleCore.Items.BONE_HAMMER,
          ModuleCore.Items.FLINT_HAMMER_DURABLE,
          ModuleCore.Items.BONE_HAMMER_DURABLE,
          ModuleCore.Items.IRON_HAMMER,
          ModuleCore.Items.GOLD_HAMMER,
          ModuleCore.Items.DIAMOND_HAMMER,
          ModuleCore.Items.OBSIDIAN_HAMMER,

          ModuleCore.Items.BOOK,

          ModuleCore.Items.PYROBERRY_SEEDS,
          ModuleCore.Items.PYROBERRIES,
          ModuleCore.Items.PYROBERRY_WINE,
          ModuleCore.Items.PYROBERRY_COCKTAIL,

          ModuleCore.Items.GLOAMBERRY_SEEDS,
          ModuleCore.Items.GLOAMBERRIES,
          ModuleCore.Items.GLOAMBERRY_WINE,

          ModuleCore.Items.FRECKLEBERRY_SEEDS,
          ModuleCore.Items.FRECKLEBERRIES,
          ModuleCore.Items.FRECKLEBERRY_WINE,

          ModuleCore.Items.TAINTED_MEAT,

          ModuleCore.Items.FURNACE_CORE
      );

      // Material
      ModelRegistrationHelper.registerVariantItemModels(
          ModuleCore.Items.MATERIAL,
          "variant",
          ItemMaterial.EnumType.values()
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
