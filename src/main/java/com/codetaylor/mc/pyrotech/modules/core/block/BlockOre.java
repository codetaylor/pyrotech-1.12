package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockOre
    extends Block
    implements IBlockVariant<BlockOre.EnumType> {

  public static final String NAME = "ore";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  public BlockOre() {

    super(Material.ROCK);
    this.setHardness(3.0F);
    this.setResistance(5.0F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
    this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.FOSSIL_ORE));
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    if (state.getValue(VARIANT) == EnumType.FOSSIL_ORE) {
      return 0;
    }

    return this.getMetaFromState(state);
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    if (state.getValue(VARIANT) == EnumType.FOSSIL_ORE) {
      return random.nextInt(3) + 1 + fortune;
    }

    return super.quantityDropped(state, fortune, random);
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    if (state.getValue(VARIANT) == EnumType.FOSSIL_ORE) {
      return Items.BONE;
    }

    return super.getItemDropped(state, rand, fortune);
  }

  @Override
  public void getSubBlocks(
      CreativeTabs tab,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : EnumType.values()) {
      list.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Nonnull
  @Override
  public String getModelName(ItemStack stack) {

    return EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    FOSSIL_ORE(0, "fossil_ore");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final int meta;
    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}
