package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemBowDrill;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemFlintAndTinder;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemMatchstick;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRockGrass;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("WeakerAccess")
public final class ModuleItems {

  public static final ItemMaterial MATERIAL = new ItemMaterial();
  public static final ItemBowDrill BOW_DRILL = new ItemBowDrill();
  public static final ItemFlintAndTinder FLINT_AND_TINDER = new ItemFlintAndTinder();
  public static final ItemMatchstick MATCHSTICK = new ItemMatchstick();
  public static final ItemDoor REFRACTORY_DOOR = new ItemDoor(ModuleBlocks.REFRACTORY_DOOR);
  public static final ItemRock ROCK = new ItemRock(ModuleBlocks.ROCK);
  public static final ItemRockGrass ROCK_GRASS = new ItemRockGrass(ModuleBlocks.ROCK_GRASS);
  public static final ItemMulch MULCH = new ItemMulch();

  public static final ItemAppleBaked APPLE_BAKED = new ItemAppleBaked();
  public static final ItemBurnedFood BURNED_FOOD = new ItemBurnedFood();

  public static final ItemBoneHammer BONE_HAMMER = new ItemBoneHammer();
  public static final ItemDiamondHammer DIAMOND_HAMMER = new ItemDiamondHammer();
  public static final ItemFlintHammer FLINT_HAMMER = new ItemFlintHammer();
  public static final ItemIronHammer IRON_HAMMER = new ItemIronHammer();
  public static final ItemStoneHammer STONE_HAMMER = new ItemStoneHammer();
  public static final ItemCrudeHammer CRUDE_HAMMER = new ItemCrudeHammer();

  public static void onRegister(Registry registry) {

    registry.registerItem(ModuleItems.MATERIAL, ItemMaterial.NAME);
    registry.registerItem(ModuleItems.BOW_DRILL, ItemBowDrill.NAME);
    registry.registerItem(ModuleItems.FLINT_AND_TINDER, ItemFlintAndTinder.NAME);
    registry.registerItem(ModuleItems.MATCHSTICK, ItemMatchstick.NAME);
    registry.registerItem(ModuleItems.REFRACTORY_DOOR, ModuleBlocks.REFRACTORY_DOOR.getRegistryName());
    registry.registerItem(ModuleItems.ROCK, BlockRock.NAME);
    registry.registerItem(ModuleItems.ROCK_GRASS, BlockRockGrass.NAME);
    registry.registerItem(ModuleItems.MULCH, ItemMulch.NAME);

    registry.registerItem(ModuleItems.APPLE_BAKED, ItemAppleBaked.NAME);
    registry.registerItem(ModuleItems.BURNED_FOOD, ItemBurnedFood.NAME);

    registry.registerItem(ModuleItems.CRUDE_HAMMER, ItemCrudeHammer.NAME);
    registry.registerItem(ModuleItems.STONE_HAMMER, ItemStoneHammer.NAME);
    registry.registerItem(ModuleItems.BONE_HAMMER, ItemBoneHammer.NAME);
    registry.registerItem(ModuleItems.FLINT_HAMMER, ItemFlintHammer.NAME);
    registry.registerItem(ModuleItems.IRON_HAMMER, ItemIronHammer.NAME);
    registry.registerItem(ModuleItems.DIAMOND_HAMMER, ItemDiamondHammer.NAME);

    registry.registerItemRegistrationStrategy(forgeRegistry -> {

      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.STONE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.SANDSTONE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.LIMESTONE.getMeta()));

      OreDictionary.registerOre("twine", ItemMaterial.EnumType.TWINE.asStack());
      OreDictionary.registerOre("twine", new ItemStack(Items.STRING));

      OreDictionary.registerOre("nuggetIron", ItemMaterial.EnumType.IRON_SHARD.asStack());

      OreDictionary.registerOre("stickStone", ItemMaterial.EnumType.STICK_STONE.asStack());
    });
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleItems.BOW_DRILL,
          ModuleItems.FLINT_AND_TINDER,
          ModuleItems.MATCHSTICK,
          ModuleItems.REFRACTORY_DOOR,
          ModuleItems.ROCK_GRASS,
          ModuleItems.MULCH,

          ModuleItems.APPLE_BAKED,
          ModuleItems.BURNED_FOOD,

          ModuleItems.CRUDE_HAMMER,
          ModuleItems.STONE_HAMMER,
          ModuleItems.FLINT_HAMMER,
          ModuleItems.BONE_HAMMER,
          ModuleItems.IRON_HAMMER,
          ModuleItems.DIAMOND_HAMMER
      );

      // Rock
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModulePyrotech.MOD_ID,
          ModuleBlocks.ROCK,
          BlockRock.VARIANT
      );

      // Material
      ModelRegistrationHelper.registerVariantItemModels(
          ModuleItems.MATERIAL,
          "variant",
          ItemMaterial.EnumType.values()
      );
    });
  }

  private ModuleItems() {
    //
  }
}
