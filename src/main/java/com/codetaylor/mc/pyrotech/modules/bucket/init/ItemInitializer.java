package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.bucket.item.ItemBucketBase;
import com.codetaylor.mc.pyrotech.modules.bucket.item.ItemBucketClay;
import com.codetaylor.mc.pyrotech.modules.bucket.item.ItemBucketStone;
import com.codetaylor.mc.pyrotech.modules.bucket.item.ItemBucketWood;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static final ItemBucketWood BUCKET_WOOD = new ItemBucketWood();
  public static final ItemBucketClay BUCKET_CLAY = new ItemBucketClay();
  public static final ItemBucketStone BUCKET_STONE = new ItemBucketStone();

  public static void onRegister(Registry registry) {

    if (ModuleBucketConfig.BUCKET_WOOD.ENABLED) {
      registry.registerItem(ItemInitializer.BUCKET_WOOD, ItemBucketWood.NAME);
    }

    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.registerItem(ItemInitializer.BUCKET_CLAY, ItemBucketClay.NAME);
    }

    if (ModuleBucketConfig.BUCKET_STONE.ENABLED) {
      registry.registerItem(ItemInitializer.BUCKET_STONE, ItemBucketStone.NAME);
    }
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      if (ModuleBucketConfig.BUCKET_WOOD.ENABLED) {
        ModelRegistrationHelper.registerItemModels(ItemInitializer.BUCKET_WOOD);
        ModelRegistrationHelper.registerItemModel(
            ItemInitializer.BUCKET_WOOD,
            ItemBucketBase.EnumType.MILK.getMeta(),
            ItemBucketBase.EnumType.MILK.getName()
        );
      }

      if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
        ModelRegistrationHelper.registerItemModels(ItemInitializer.BUCKET_CLAY);
        ModelRegistrationHelper.registerItemModel(
            ItemInitializer.BUCKET_CLAY,
            ItemBucketBase.EnumType.MILK.getMeta(),
            ItemBucketBase.EnumType.MILK.getName()
        );
      }

      if (ModuleBucketConfig.BUCKET_STONE.ENABLED) {
        ModelRegistrationHelper.registerItemModels(ItemInitializer.BUCKET_STONE);
        ModelRegistrationHelper.registerItemModel(
            ItemInitializer.BUCKET_STONE,
            ItemBucketBase.EnumType.MILK.getMeta(),
            ItemBucketBase.EnumType.MILK.getName()
        );
      }
    });
  }

  private ItemInitializer() {
    //
  }
}
