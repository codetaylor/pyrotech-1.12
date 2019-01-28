package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
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
public final class ModuleItems {

  public static final ItemMaterial MATERIAL = new ItemMaterial();
  public static final ItemIgniterBase BOW_DRILL = new ItemBowDrill();
  public static final ItemIgniterBase FLINT_AND_TINDER = new ItemFlintAndTinder();
  public static final ItemDoor REFRACTORY_DOOR = new ItemDoor(ModuleBlocks.REFRACTORY_DOOR);
  public static final ItemTinder TINDER = new ItemTinder();
  public static final ItemRock ROCK = new ItemRock(ModuleBlocks.ROCK);
  public static final ItemRockGrass ROCK_GRASS = new ItemRockGrass(ModuleBlocks.ROCK_GRASS);
  public static final ItemMulch MULCH = new ItemMulch();

  public static final ItemAppleBaked APPLE_BAKED = new ItemAppleBaked();

  public static final ItemCrudeAxe CRUDE_AXE = new ItemCrudeAxe();
  public static final ItemCrudeHoe CRUDE_HOE = new ItemCrudeHoe();
  public static final ItemCrudePickaxe CRUDE_PICKAXE = new ItemCrudePickaxe();
  public static final ItemCrudeShovel CRUDE_SHOVEL = new ItemCrudeShovel();

  public static final ItemBoneAxe BONE_AXE = new ItemBoneAxe();
  public static final ItemBoneHoe BONE_HOE = new ItemBoneHoe();
  public static final ItemBonePickaxe BONE_PICKAXE = new ItemBonePickaxe();
  public static final ItemBoneShovel BONE_SHOVEL = new ItemBoneShovel();
  public static final ItemBoneSword BONE_SWORD = new ItemBoneSword();

  public static final ItemFlintAxe FLINT_AXE = new ItemFlintAxe();
  public static final ItemFlintHoe FLINT_HOE = new ItemFlintHoe();
  public static final ItemFlintPickaxe FLINT_PICKAXE = new ItemFlintPickaxe();
  public static final ItemFlintShovel FLINT_SHOVEL = new ItemFlintShovel();
  public static final ItemFlintSword FLINT_SWORD = new ItemFlintSword();

  public static final ItemBoneHammer BONE_HAMMER = new ItemBoneHammer();
  public static final ItemDiamondHammer DIAMOND_HAMMER = new ItemDiamondHammer();
  public static final ItemFlintHammer FLINT_HAMMER = new ItemFlintHammer();
  public static final ItemIronHammer IRON_HAMMER = new ItemIronHammer();
  public static final ItemStoneHammer STONE_HAMMER = new ItemStoneHammer();
  public static final ItemCrudeHammer CRUDE_HAMMER = new ItemCrudeHammer();

  public static final ItemMillBlade STONE_MILL_BLADE = new ItemMillBlade(Item.ToolMaterial.STONE.getMaxUses() / 2);
  public static final ItemMillBlade FLINT_MILL_BLADE = new ItemMillBlade(EnumMaterial.FLINT.getToolMaterial().getMaxUses());
  public static final ItemMillBlade BONE_MILL_BLADE = new ItemMillBlade(EnumMaterial.BONE.getToolMaterial().getMaxUses());
  public static final ItemMillBlade IRON_MILL_BLADE = new ItemMillBlade(Item.ToolMaterial.IRON.getMaxUses());
  public static final ItemMillBlade DIAMOND_MILL_BLADE = new ItemMillBlade(Item.ToolMaterial.DIAMOND.getMaxUses());

  public static void onRegister(Registry registry) {

    registry.registerItem(ModuleItems.STONE_MILL_BLADE, "mill_blade_stone");
    registry.registerItem(ModuleItems.FLINT_MILL_BLADE, "mill_blade_flint");
    registry.registerItem(ModuleItems.BONE_MILL_BLADE, "mill_blade_bone");
    registry.registerItem(ModuleItems.IRON_MILL_BLADE, "mill_blade_iron");
    registry.registerItem(ModuleItems.DIAMOND_MILL_BLADE, "mill_blade_diamond");

    registry.registerItem(ModuleItems.MATERIAL, ItemMaterial.NAME);
    registry.registerItem(ModuleItems.BOW_DRILL, ItemBowDrill.NAME);
    registry.registerItem(ModuleItems.FLINT_AND_TINDER, ItemFlintAndTinder.NAME);
    registry.registerItem(ModuleItems.REFRACTORY_DOOR, ModuleBlocks.REFRACTORY_DOOR.getRegistryName());
    registry.registerItem(new ItemBlock(ModuleBlocks.KILN_PIT), ModuleBlocks.KILN_PIT.getRegistryName());
    registry.registerItem(ModuleItems.TINDER, ItemTinder.NAME);
    registry.registerItem(new ItemBlock(ModuleBlocks.CAMPFIRE), BlockCampfire.NAME);
    registry.registerItem(ModuleItems.ROCK, BlockRock.NAME);
    registry.registerItem(ModuleItems.ROCK_GRASS, BlockRockGrass.NAME);
    registry.registerItem(ModuleItems.MULCH, ItemMulch.NAME);

    registry.registerItem(ModuleItems.APPLE_BAKED, ItemAppleBaked.NAME);

    registry.registerItem(ModuleItems.CRUDE_AXE, ItemCrudeAxe.NAME);
    registry.registerItem(ModuleItems.CRUDE_HOE, ItemCrudeHoe.NAME);
    registry.registerItem(ModuleItems.CRUDE_PICKAXE, ItemCrudePickaxe.NAME);
    registry.registerItem(ModuleItems.CRUDE_SHOVEL, ItemCrudeShovel.NAME);

    registry.registerItem(ModuleItems.BONE_AXE, ItemBoneAxe.NAME);
    registry.registerItem(ModuleItems.BONE_HOE, ItemBoneHoe.NAME);
    registry.registerItem(ModuleItems.BONE_PICKAXE, ItemBonePickaxe.NAME);
    registry.registerItem(ModuleItems.BONE_SHOVEL, ItemBoneShovel.NAME);
    registry.registerItem(ModuleItems.BONE_SWORD, ItemBoneSword.NAME);

    registry.registerItem(ModuleItems.FLINT_AXE, ItemFlintAxe.NAME);
    registry.registerItem(ModuleItems.FLINT_HOE, ItemFlintHoe.NAME);
    registry.registerItem(ModuleItems.FLINT_PICKAXE, ItemFlintPickaxe.NAME);
    registry.registerItem(ModuleItems.FLINT_SHOVEL, ItemFlintShovel.NAME);
    registry.registerItem(ModuleItems.FLINT_SWORD, ItemFlintSword.NAME);

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
          ModuleItems.REFRACTORY_DOOR,
          ModuleItems.TINDER,
          ModuleItems.ROCK_GRASS,
          ModuleItems.MULCH,

          ModuleItems.APPLE_BAKED,

          ModuleItems.CRUDE_AXE,
          ModuleItems.CRUDE_HOE,
          ModuleItems.CRUDE_PICKAXE,
          ModuleItems.CRUDE_SHOVEL,

          ModuleItems.BONE_AXE,
          ModuleItems.BONE_HOE,
          ModuleItems.BONE_PICKAXE,
          ModuleItems.BONE_SHOVEL,
          ModuleItems.BONE_SWORD,

          ModuleItems.FLINT_AXE,
          ModuleItems.FLINT_HOE,
          ModuleItems.FLINT_PICKAXE,
          ModuleItems.FLINT_SHOVEL,
          ModuleItems.FLINT_SWORD,

          ModuleItems.CRUDE_HAMMER,
          ModuleItems.STONE_HAMMER,
          ModuleItems.FLINT_HAMMER,
          ModuleItems.BONE_HAMMER,
          ModuleItems.IRON_HAMMER,
          ModuleItems.DIAMOND_HAMMER,

          ModuleItems.STONE_MILL_BLADE,
          ModuleItems.FLINT_MILL_BLADE,
          ModuleItems.BONE_MILL_BLADE,
          ModuleItems.IRON_MILL_BLADE,
          ModuleItems.DIAMOND_MILL_BLADE
      );

      // Rock
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModulePyrotech.MOD_ID,
          ModuleBlocks.ROCK,
          BlockRock.VARIANT
      );

      ModelRegistrationHelper.registerBlockItemModel(
          ModuleBlocks.CAMPFIRE.getDefaultState()
              .withProperty(BlockCampfire.VARIANT, BlockCampfire.EnumType.ITEM)
      );

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
