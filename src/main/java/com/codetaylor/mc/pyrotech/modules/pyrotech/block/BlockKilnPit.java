package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnPit;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockKilnPit
    extends Block
    implements IBlockVariant<BlockKilnPit.EnumType> {

  public static final String NAME = "kiln_pit";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  public static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
  public static final AxisAlignedBB AABB_THATCH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 10D / 16D, 1.0D);

  public BlockKilnPit() {

    super(Material.WOOD);
    this.setHardness(0.6f);
    this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.EMPTY));
  }

  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {

    switch (state.getValue(VARIANT)) {
      case EMPTY:
        return SoundType.GROUND;
      case THATCH:
        return SoundType.PLANT;
      case WOOD:
        return SoundType.WOOD;
      case ACTIVE:
        return SoundType.WOOD;
      case COMPLETE:
        return SoundType.SAND;
      default:
        return SoundType.WOOD;
    }
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (state.getValue(VARIANT) == EnumType.EMPTY) {
      return AABB_EMPTY;

    } else if (state.getValue(VARIANT) == EnumType.THATCH) {
      return AABB_THATCH;
    }

    return FULL_BLOCK_AABB;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    EnumType type = state.getValue(VARIANT);
    return type == EnumType.WOOD || type == EnumType.ACTIVE || type == EnumType.COMPLETE;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    EnumType type = state.getValue(VARIANT);

    if (face == EnumFacing.DOWN) {
      return !(type == EnumType.EMPTY);

    } else {
      return this.isFullBlock(state);
    }
  }

  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    EnumType type = state.getValue(VARIANT);

    if (face == EnumFacing.DOWN) {
      return !(type == EnumType.EMPTY);

    } else {
      return this.isFullBlock(state);
    }
  }

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return world.getBlockState(pos).getValue(VARIANT) == EnumType.ACTIVE;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT);
  }

  @Nonnull
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

    return EnumType.EMPTY.getMeta();
  }

  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileKilnPit) {
      ((TileKilnPit) tileEntity).setNeedStructureValidation();
    }
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 0;
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

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileKilnPit();
  }

  @Override
  public boolean onBlockActivated(
      World world,
      BlockPos pos,
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileKilnPit)) {
      return false;
    }

    TileKilnPit tileKiln = (TileKilnPit) tileEntity;
    ItemStack heldItem = player.getHeldItem(hand);

    switch (state.getValue(VARIANT)) {

      case EMPTY:

        if (heldItem.getItem() == Item.getItemFromBlock(ModuleBlocks.THATCH)) {

          if (tileKiln.getStackHandler().getStackInSlot(0).isEmpty()) {
            return false;
          }

          if (world.isRemote) {
            return true;
          }

          // If the item in the player's hand is thatch, change the state of the
          // kiln to thatch.
          heldItem.setCount(heldItem.getCount() - 1);
          world.setBlockState(pos, this.getDefaultState().withProperty(VARIANT, EnumType.THATCH));
          world.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1, 1);
          return true;

        } else {

          if (world.isRemote) {
            return true;
          }

          KilnPitRecipe recipe = KilnPitRecipe.getRecipe(heldItem);

          if (recipe != null) {

            // If the item in the player's hand is valid input for a kiln recipe,
            // calculate the burn time and place the item into the kiln. The burn
            // time is reduced for each adjacent refractory block.

            float modifier = (float) (1.0f - tileKiln.countAdjacentRefractoryBlocks() * ModulePyrotechConfig.PIT_KILN.REFRACTORY_BLOCK_TIME_BONUS);
            int modifiedBurnTime = (int) (recipe.getTimeTicks() * modifier);
            int burnTimeTicks = Math.max(1, modifiedBurnTime);

            ItemStackHandler stackHandler = tileKiln.getStackHandler();
            tileKiln.setTotalBurnTimeTicks(burnTimeTicks);
            player.setHeldItem(hand, stackHandler.insertItem(0, heldItem, false));
            world.notifyBlockUpdate(pos, state, state, 2);
            return true;
          }

          // Pop all the kiln contents into the world.
          // This will happen when the block is destroyed.

          world.destroyBlock(pos, true);
          return true;
        }

      case THATCH:

        // If the player is holding enough ore:logWood, place the wood and set the
        // kiln state to wood.

        if (heldItem.getCount() < 3) {
          return false;
        }

        int logWood = OreDictionary.getOreID("logWood");
        int[] oreIDs = OreDictionary.getOreIDs(heldItem);

        for (int oreID : oreIDs) {

          if (oreID == logWood) {

            if (world.isRemote) {
              return true;
            }

            heldItem.setCount(heldItem.getCount() - 3);
            tileKiln.getLogStackHandler()
                .insertItem(0, new ItemStack(heldItem.getItem(), 3, heldItem.getMetadata()), false);
            world.setBlockState(pos, this.getDefaultState().withProperty(VARIANT, EnumType.WOOD));
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
            BlockHelper.notifyBlockUpdate(world, pos);
            return true;
          }
        }

      default:
        return false;
    }
  }

  @Nonnull
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean canSilkHarvest(
      World world, BlockPos pos, IBlockState state, EntityPlayer player
  ) {

    return false;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileKilnPit) {
      TileKilnPit tileKiln = (TileKilnPit) tileEntity;
      ItemStackHandler stackHandler = tileKiln.getStackHandler();
      StackHelper.spawnStackOnTop(worldIn, stackHandler.getStackInSlot(0), pos);
      stackHandler = tileKiln.getOutputStackHandler();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        StackHelper.spawnStackOnTop(worldIn, stackHandler.getStackInSlot(i), pos);
      }

      // Pop the used wood into the world.
      if (state.getValue(VARIANT) == EnumType.WOOD) {
        stackHandler = tileKiln.getLogStackHandler();
        StackHelper.spawnStackOnTop(worldIn, stackHandler.getStackInSlot(0), pos);
      }
    }

    super.breakBlock(worldIn, pos, state);
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.COMPLETE
        || type == EnumType.ACTIVE) {
      return 0;
    }

    return super.quantityDropped(state, fortune, random);
  }

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.WOOD) {
      drops.add(new ItemStack(ModuleBlocks.THATCH, 1, 0));

    } else if (type == EnumType.THATCH) {
      drops.add(new ItemStack(ModuleBlocks.THATCH, 1, 0));
    }

    super.getDrops(drops, world, pos, state, fortune);
  }

  public enum EnumType
      implements IVariant {

    EMPTY(0, "empty"),
    THATCH(1, "thatch"),
    WOOD(2, "wood"),
    ACTIVE(3, "active"),
    COMPLETE(4, "complete");

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
