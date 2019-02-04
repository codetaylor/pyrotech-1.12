package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    final ItemMaterial itemMaterial = new ItemMaterial();
    registry.registerItem(itemMaterial, ItemMaterial.NAME);
    registry.registerItem(new ItemMulch(), ItemMulch.NAME);

    registry.registerItem(new ItemAppleBaked(), ItemAppleBaked.NAME);
    registry.registerItem(new ItemBurnedFood(), ItemBurnedFood.NAME);

    registry.registerItem(new ItemCrudeHammer(), ItemCrudeHammer.NAME);
    registry.registerItem(new ItemStoneHammer(), ItemStoneHammer.NAME);
    registry.registerItem(new ItemBoneHammer(), ItemBoneHammer.NAME);
    registry.registerItem(new ItemFlintHammer(), ItemFlintHammer.NAME);
    registry.registerItem(new ItemIronHammer(), ItemIronHammer.NAME);
    registry.registerItem(new ItemDiamondHammer(), ItemDiamondHammer.NAME);

    registry.registerItemRegistrationStrategy(forgeRegistry -> {

      OreDictHelper.register(
          "twine",
          new ItemStack(Items.STRING),
          new ItemStack(itemMaterial, 1, ItemMaterial.EnumType.TWINE.getMeta())
      );

      OreDictHelper.register(
          "stickStone",
          new ItemStack(itemMaterial, 1, ItemMaterial.EnumType.STICK_STONE.getMeta())
      );

      OreDictHelper.register(
          "dustLimestone",
          new ItemStack(itemMaterial, 1, ItemMaterial.EnumType.DUST_LIMESTONE.getMeta())
      );

      OreDictHelper.register(
          "rock",
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.STONE.getMeta()),
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.DIORITE.getMeta()),
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.GRANITE.getMeta()),
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.ANDESITE.getMeta()),
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.SANDSTONE.getMeta()),
          new ItemStack(Item.getItemFromBlock(ModuleCore.Blocks.ROCK), 1, BlockRock.EnumType.LIMESTONE.getMeta())
      );

      OreDictHelper.register(
          "mulchNitrogen",
          new ItemStack(Items.MUTTON),
          new ItemStack(Items.RABBIT),
          new ItemStack(Items.ROTTEN_FLESH),
          new ItemStack(Items.CHICKEN),
          new ItemStack(Items.BEEF),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
          new ItemStack(Items.PORKCHOP),
          new ItemStack(Items.DYE, 1, 15)
      );
    });
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleCore.Items.MULCH,

          ModuleCore.Items.APPLE_BAKED,
          ModuleCore.Items.BURNED_FOOD,

          ModuleCore.Items.CRUDE_HAMMER,
          ModuleCore.Items.STONE_HAMMER,
          ModuleCore.Items.FLINT_HAMMER,
          ModuleCore.Items.BONE_HAMMER,
          ModuleCore.Items.IRON_HAMMER,
          ModuleCore.Items.DIAMOND_HAMMER
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
