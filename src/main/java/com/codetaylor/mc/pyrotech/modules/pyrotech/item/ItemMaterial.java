package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public class ItemMaterial
    extends Item {

  public static final String NAME = "material";

  public ItemMaterial() {

    this.setHasSubtypes(true);
  }

  @Override
  public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {

    if (this.isInCreativeTab(tab)) {

      for (EnumType type : EnumType.values()) {
        items.add(new ItemStack(this, 1, type.getMeta()));
      }
    }
    super.getSubItems(tab, items);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    String name = EnumType.fromMeta(stack.getMetadata()).getName().replaceAll("_", ".");
    return "item." + ModulePyrotech.MOD_ID + "." + name;
  }

  public enum EnumType
      implements IVariant {

    PIT_ASH(0, "pit_ash"),
    COAL_COKE(1, "coal_coke"),
    STRAW(2, "straw"),
    FLINT_CLAY_BALL(3, "flint_clay_ball"),
    REFRACTORY_CLAY_BALL(4, "refractory_clay_ball"),
    REFRACTORY_BRICK(5, "refractory_brick"),
    POTTERY_FRAGMENTS(6, "pottery_fragments"),
    POTTERY_SHARD(7, "pottery_shard"),
    SLAKED_LIME(8, "slaked_lime"),
    UNFIRED_REFRACTORY_BRICK(9, "unfired_refractory_brick"),
    FLINT_SHARD(10, "flint_shard"),
    BONE_SHARD(11, "bone_shard"),
    PLANT_FIBERS(12, "plant_fibers"),
    PLANT_FIBERS_DRIED(13, "plant_fibers_dried"),
    TWINE(14, "twine"),
    CHARCOAL_FLAKES(15, "charcoal_flakes"),
    BRICK_STONE(16, "brick_stone"),
    CLAY_LUMP(17, "clay_lump"),
    DIAMOND_SHARD(18, "diamond_shard"),
    IRON_SHARD(19, "iron_shard"),
    BOARD(20, "board"),
    COAL_PIECES(21, "coal_pieces"),
    QUICKLIME(22, "quicklime"),
    BOARD_TARRED(23, "board_tarred"),
    UNFIRED_BRICK(24, "unfired_brick"),
    BUCKET_CLAY_UNFIRED(25, "bucket_clay_unfired");

    private static final EnumType[] META_LOOKUP = Stream
        .of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final String name;
    private final int meta;

    EnumType(int meta, String name) {

      this.name = name;
      this.meta = meta;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public ItemStack asStack() {

      return this.asStack(1);
    }

    public ItemStack asStack(int amount) {

      return new ItemStack(ModuleItems.MATERIAL, amount, this.meta);
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta > META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }
}
