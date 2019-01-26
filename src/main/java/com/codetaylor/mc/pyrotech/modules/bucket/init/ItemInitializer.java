package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import com.codetaylor.mc.pyrotech.modules.bucket.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    if (ModuleBucketConfig.BUCKET_WOOD.ENABLED) {
      registry.registerItem(new ItemBucketWood(), ItemBucketWood.NAME);
    }

    if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
      registry.registerItem(new ItemBucketClay(), ItemBucketClay.NAME);
      registry.registerItem(new ItemBucketClayUnfired(), ItemBucketClayUnfired.NAME);
    }

    if (ModuleBucketConfig.BUCKET_STONE.ENABLED) {
      registry.registerItem(new ItemBucketStone(), ItemBucketStone.NAME);
    }
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      if (ModuleBucketConfig.BUCKET_WOOD.ENABLED) {
        ModelRegistrationHelper.registerItemModels(
            ModuleBucket.Items.BUCKET_WOOD
        );
        ModelRegistrationHelper.registerItemModel(
            ModuleBucket.Items.BUCKET_WOOD,
            ItemBucketBase.EnumType.MILK.getMeta(),
            ItemBucketBase.EnumType.MILK.getName()
        );
      }

      if (ModuleBucketConfig.BUCKET_CLAY.ENABLED) {
        ModelRegistrationHelper.registerItemModels(
            ModuleBucket.Items.BUCKET_CLAY,
            ModuleBucket.Items.BUCKET_CLAY_UNFIRED
        );
        ModelRegistrationHelper.registerItemModel(
            ModuleBucket.Items.BUCKET_CLAY,
            ItemBucketBase.EnumType.MILK.getMeta(),
            ItemBucketBase.EnumType.MILK.getName()
        );
      }

      if (ModuleBucketConfig.BUCKET_STONE.ENABLED) {
        ModelRegistrationHelper.registerItemModels(
            ModuleBucket.Items.BUCKET_STONE
        );
        ModelRegistrationHelper.registerItemModel(
            ModuleBucket.Items.BUCKET_STONE,
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
