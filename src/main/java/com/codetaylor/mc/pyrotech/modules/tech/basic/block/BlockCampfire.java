package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentIgniterBlock;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleLava;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemIgniterBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallowStick;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallowStickEmpty;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileSoakingPot;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockCampfire
    extends BlockPartialBase
    implements IBlockInteractable,
    IBlockIgnitableWithIgniterItem,
    IBlockIgnitableAdjacentIgniterBlock {

  public static final String NAME = "campfire";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  public static final PropertyInteger ASH = PropertyInteger.create("ash", 0, 8);

  public static final AxisAlignedBB AABB_FULL = new AxisAlignedBB(0, 0, 0, 1, 6f / 16f, 1);
  private static final AxisAlignedBB AABB_TINDER = new AxisAlignedBB(4f / 16f, 0, 4f / 16f, 12f / 16f, 5f / 16f, 12f / 16f);
  private static final AxisAlignedBB AABB_ASH_A = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 1f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_B = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 2f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_C = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 3f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_D = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 4f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_E = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 5f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_F = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 6f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_G = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 7f / 16f, 14f / 16f);
  private static final AxisAlignedBB AABB_ASH_H = new AxisAlignedBB(2f / 16f, 0, 2f / 16f, 14f / 16f, 8f / 16f, 14f / 16f);

  public BlockCampfire() {

    super(Material.WOOD);
    this.setHardness(0.5f);
  }

  // ---------------------------------------------------------------------------
  // - Ignition
  // ---------------------------------------------------------------------------

  @Override
  public void igniteWithIgniterItem(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {
      ((TileCampfire) tileEntity).workerSetActive(true);
    }
  }

  @Override
  public void igniteWithAdjacentIgniterBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    this.igniteWithIgniterItem(world, pos, blockState, facing);
  }

  // ---------------------------------------------------------------------------
  // - Sound
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileCampfire)) {
      return SoundType.PLANT;
    }

    IBlockState actualState = this.getActualState(state, world, pos);

    if (((TileCampfire) tileEntity).getFuelRemaining() > 0) {
      return SoundType.WOOD;

    } else if (actualState.getValue(VARIANT) == BlockCampfire.EnumType.ASH) {
      return SoundType.SAND;

    } else {
      return SoundType.PLANT;
    }
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    IBlockState actualState = this.getActualState(state, world, pos);

    if (actualState.getValue(VARIANT) == BlockCampfire.EnumType.LIT) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileCampfire) {
        int fuelRemaining = ((TileCampfire) tileEntity).getFuelRemaining();

        if (fuelRemaining == 0) {
          return 0;
        }

        float lightPercentage = fuelRemaining / 8f;
        int max = Math.min(15, Math.max(ModuleTechBasicConfig.CAMPFIRE.MAXIMUM_LIGHT_LEVEL, ModuleTechBasicConfig.CAMPFIRE.MINIMUM_LIGHT_LEVEL));
        int min = Math.max(0, Math.min(ModuleTechBasicConfig.CAMPFIRE.MAXIMUM_LIGHT_LEVEL, ModuleTechBasicConfig.CAMPFIRE.MINIMUM_LIGHT_LEVEL));
        return (int) MathHelper.clamp((max - min) * lightPercentage + min, 0, 15);
      }
    }

    return super.getLightValue(state, world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    return false;
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire
        && ((TileCampfire) tileEntity).workerIsActive()) {

      double x = (double) pos.getX() + 0.5;
      double y = (double) pos.getY() + (4.0 / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
      double z = (double) pos.getZ() + 0.5;

      if (rand.nextDouble() < 0.1) {
        world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
      }

      for (int i = 0; i < 4; i++) {
        double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
        double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
      }

      if (!((TileCampfire) tileEntity).getOutputStackHandler().getStackInSlot(0).isEmpty()) {

        for (int i = 0; i < 8; i++) {
          double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    TileEntity tileEntity = source.getTileEntity(pos);

    if (!(tileEntity instanceof TileCampfire)) {
      return NULL_AABB;
    }

    IBlockState actualState = this.getActualState(state, source, pos);

    if (((TileCampfire) tileEntity).getFuelRemaining() > 0) {
      return AABB_FULL;

    } else {

      switch (actualState.getValue(ASH)) {
        default:
        case 0:
          return (actualState.getValue(VARIANT) == BlockCampfire.EnumType.ASH) ? AABB_ASH_A : AABB_TINDER;
        case 1:
          return AABB_ASH_A;
        case 2:
          return AABB_ASH_B;
        case 3:
          return AABB_ASH_C;
        case 4:
          return AABB_ASH_D;
        case 5:
          return AABB_ASH_E;
        case 6:
          return AABB_ASH_F;
        case 7:
          return AABB_ASH_G;
        case 8:
          return AABB_ASH_H;
      }
    }

  }

  // ---------------------------------------------------------------------------
  // - Fire
  // ---------------------------------------------------------------------------

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return (this.getActualState(world.getBlockState(pos), world, pos).getValue(VARIANT) == BlockCampfire.EnumType.LIT);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public String getHarvestTool(IBlockState state) {

    return "shovel";
  }

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItemMainhand();

    if (heldItem.getItem() instanceof ItemIgniterBase) {
      return false;
    }

    if (hand == EnumHand.MAIN_HAND && (heldItem.getItem() instanceof ItemMarshmallowStick || heldItem.getItem() instanceof ItemMarshmallowStickEmpty)) {
      return false;
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileCampfire)) {
      return false;
    }

    TileCampfire campfire = (TileCampfire) tileEntity;

    if (campfire.isDead()) {
      return false;
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    if (ModuleTechBasicConfig.CAMPFIRE.ENTITY_WALK_BURN_DAMAGE > 0
        && !entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)
        && this.getActualState(world.getBlockState(pos), world, pos).getValue(VARIANT) == BlockCampfire.EnumType.LIT) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModuleTechBasicConfig.CAMPFIRE.ENTITY_WALK_BURN_DAMAGE);
    }

    super.onEntityWalk(world, pos, entity);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {
      ((TileCampfire) tileEntity).dropContents();
    }

    IBlockState blockState = world.getBlockState(pos);

    if (blockState.getBlock() == this
        && blockState.getValue(VARIANT) == EnumType.LIT) {
      int level = 4;
      int dimension = world.provider.getDimension();
      ModuleTechBasic.PACKET_SERVICE.sendToAllAround(new SCPacketParticleLava(pos, level), dimension, pos);
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {

    return 0;
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return world.isSideSolid(pos.down(), EnumFacing.UP)
        && super.canPlaceBlockAt(world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileCampfire();
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    if (world.isRemote) {
      return;
    }

    if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
      world.destroyBlock(pos, false);
    }

    if (world.getTileEntity(pos.up()) instanceof TileSoakingPot) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileCampfire) {
        TileCampfire tileCampfire = (TileCampfire) tileEntity;
        tileCampfire.dropInput();
        tileCampfire.dropOutput();
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT, ASH);
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

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {
      TileCampfire tileCampfire = (TileCampfire) tileEntity;
      int ashLevel = tileCampfire.getAshLevel();
      return state.withProperty(ASH, ashLevel);
    }

    return super.getActualState(state, world, pos);
  }

  public enum EnumType
      implements IVariant {

    NORMAL(0, "normal"),
    LIT(1, "lit"),
    ASH(2, "ash"),
    ITEM(3, "item");

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

    @Nonnull
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
