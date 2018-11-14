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
  public void getSubItems(
      CreativeTabs tab, NonNullList<ItemStack> items
  ) {

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

    PIT_ASH("pit_ash", 0),
    COAL_COKE("coal_coke", 1),
    STRAW("straw", 2),
    FLINT_CLAY_BALL("flint_clay_ball", 3),
    REFRACTORY_CLAY_BALL("refractory_clay_ball", 4),
    REFRACTORY_BRICK("refractory_brick", 5),
    POTTERY_FRAGMENTS("pottery_fragments", 6),
    POTTERY_SHARD("pottery_shard", 7),
    SLAKED_LIME("slaked_lime", 8),
    UNFIRED_REFRACTORY_BRICK("unfired_refractory_brick", 9),
    FLINT_SHARD("flint_shard", 10),
    BONE_SHARD("bone_shard", 11);

    private static final EnumType[] META_LOOKUP = Stream
        .of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final String name;
    private final int meta;

    EnumType(String name, int meta) {

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
