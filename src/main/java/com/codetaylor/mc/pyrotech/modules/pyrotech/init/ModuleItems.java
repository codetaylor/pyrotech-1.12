package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRockGrass;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("WeakerAccess")
public class ModuleItems {

  public static final ItemMaterial MATERIAL = new ItemMaterial();
  public static final Item QUICKLIME = new ItemQuicklime();
  public static final ItemIgniterBase BOW_DRILL = new ItemBowDrill();
  public static final ItemIgniterBase FLINT_AND_TINDER = new ItemFlintAndTinder();
  public static final ItemDoor REFRACTORY_DOOR = new ItemDoor(ModuleBlocks.REFRACTORY_DOOR);
  public static final ItemTinder TINDER = new ItemTinder();
  public static final ItemRock ROCK = new ItemRock(ModuleBlocks.ROCK);
  public static final ItemRockGrass ROCK_GRASS = new ItemRockGrass(ModuleBlocks.ROCK_GRASS);

  public static final ItemCrudeAxe CRUDE_AXE = new ItemCrudeAxe();
  public static final ItemCrudeHoe CRUDE_HOE = new ItemCrudeHoe();
  public static final ItemCrudePickaxe CRUDE_PICKAXE = new ItemCrudePickaxe();
  public static final ItemCrudeShovel CRUDE_SHOVEL = new ItemCrudeShovel();

  public static final ItemBoneAxe BONE_AXE = new ItemBoneAxe();
  public static final ItemBoneHoe BONE_HOE = new ItemBoneHoe();
  public static final ItemBonePickaxe BONE_PICKAXE = new ItemBonePickaxe();
  public static final ItemBoneShovel BONE_SHOVEL = new ItemBoneShovel();

  public static final ItemFlintAxe FLINT_AXE = new ItemFlintAxe();
  public static final ItemFlintHoe FLINT_HOE = new ItemFlintHoe();
  public static final ItemFlintPickaxe FLINT_PICKAXE = new ItemFlintPickaxe();
  public static final ItemFlintShovel FLINT_SHOVEL = new ItemFlintShovel();

  public static void onRegister(Registry registry) {

    registry.registerItem(ModuleItems.MATERIAL, ItemMaterial.NAME);
    registry.registerItem(ModuleItems.QUICKLIME, ItemQuicklime.NAME);
    registry.registerItem(ModuleItems.BOW_DRILL, ItemBowDrill.NAME);
    registry.registerItem(ModuleItems.FLINT_AND_TINDER, ItemFlintAndTinder.NAME);
    registry.registerItem(ModuleItems.REFRACTORY_DOOR, ModuleBlocks.REFRACTORY_DOOR.getRegistryName());
    registry.registerItem(new ItemBlock(ModuleBlocks.KILN_PIT), ModuleBlocks.KILN_PIT.getRegistryName());
    registry.registerItem(ModuleItems.TINDER, ItemTinder.NAME);
    registry.registerItem(new ItemBlock(ModuleBlocks.CAMPFIRE), BlockCampfire.NAME);
    registry.registerItem(ModuleItems.ROCK, BlockRock.NAME);
    registry.registerItem(ModuleItems.ROCK_GRASS, BlockRockGrass.NAME);

    registry.registerItem(ModuleItems.CRUDE_AXE, ItemCrudeAxe.NAME);
    registry.registerItem(ModuleItems.CRUDE_HOE, ItemCrudeHoe.NAME);
    registry.registerItem(ModuleItems.CRUDE_PICKAXE, ItemCrudePickaxe.NAME);
    registry.registerItem(ModuleItems.CRUDE_SHOVEL, ItemCrudeShovel.NAME);

    registry.registerItem(ModuleItems.BONE_AXE, ItemBoneAxe.NAME);
    registry.registerItem(ModuleItems.BONE_HOE, ItemBoneHoe.NAME);
    registry.registerItem(ModuleItems.BONE_PICKAXE, ItemBonePickaxe.NAME);
    registry.registerItem(ModuleItems.BONE_SHOVEL, ItemBoneShovel.NAME);

    registry.registerItem(ModuleItems.FLINT_AXE, ItemFlintAxe.NAME);
    registry.registerItem(ModuleItems.FLINT_HOE, ItemFlintHoe.NAME);
    registry.registerItem(ModuleItems.FLINT_PICKAXE, ItemFlintPickaxe.NAME);
    registry.registerItem(ModuleItems.FLINT_SHOVEL, ItemFlintShovel.NAME);

    registry.registerItemRegistrationStrategy(forgeRegistry -> {

      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.STONE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta()));
      OreDictionary.registerOre("rock", new ItemStack(ROCK, 1, BlockRock.EnumType.SANDSTONE.getMeta()));

      OreDictionary.registerOre("twine", new ItemStack(MATERIAL, 1, ItemMaterial.EnumType.TWINE.getMeta()));
      OreDictionary.registerOre("twine", new ItemStack(Items.STRING));
    });
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleItems.QUICKLIME,
          ModuleItems.BOW_DRILL,
          ModuleItems.FLINT_AND_TINDER,
          ModuleItems.REFRACTORY_DOOR,
          ModuleItems.TINDER,
          ModuleItems.ROCK_GRASS,

          ModuleItems.CRUDE_AXE,
          ModuleItems.CRUDE_HOE,
          ModuleItems.CRUDE_PICKAXE,
          ModuleItems.CRUDE_SHOVEL,

          ModuleItems.BONE_AXE,
          ModuleItems.BONE_HOE,
          ModuleItems.BONE_PICKAXE,
          ModuleItems.BONE_SHOVEL,

          ModuleItems.FLINT_AXE,
          ModuleItems.FLINT_HOE,
          ModuleItems.FLINT_PICKAXE,
          ModuleItems.FLINT_SHOVEL
      );

      // Rock
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModulePyrotech.MOD_ID,
          ModuleBlocks.ROCK,
          BlockRock.VARIANT
      );

      ModelRegistrationHelper.registerBlockItemModel(
          ModuleBlocks.CAMPFIRE.getDefaultState()
              .withProperty(BlockCampfire.VARIANT, BlockCampfire.EnumType.LIT)
              .withProperty(BlockCampfire.WOOD, 8)
      );

      ModelRegistrationHelper.registerVariantItemModels(
          ModuleItems.MATERIAL,
          "variant",
          ItemMaterial.EnumType.values()
      );
    });
  }
}
