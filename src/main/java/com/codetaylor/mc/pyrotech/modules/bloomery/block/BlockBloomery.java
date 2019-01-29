package com.codetaylor.mc.pyrotech.modules.bloomery.block;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.library.spi.tile.ITileContainer;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.bloomery.client.particles.ParticleBloomeryDrip;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemIgniterBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockBloomery
    extends Block
    implements IBlockInteractable {

  public static final String NAME = "bloomery";

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  private static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(2.0 / 16.0, 0.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, 8.0 / 16.0, 14.0 / 16.0);

  private static final AxisAlignedBB[] AABB_BOTTOM = {
      AABBHelper.create(0, 0, 0, 16, 8, 16),
      AABBHelper.create(1, 8, 1, 15, 16, 15)
  };

  public BlockBloomery() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHardness(2);
    this.setHarvestLevel("pickaxe", 0);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.Bottom)
    );
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public boolean isTop(IBlockState state) {

    return state.getValue(TYPE) == EnumType.Top;
  }

  // ---------------------------------------------------------------------------
  // - Spatial
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (this.isTop(state)) {
      return AABB_TOP;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @Override
  public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing face) {

    if (this.isTop(state)) {

      return false;
    }

    return (face == EnumFacing.DOWN);
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return !this.isTop(state);
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

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    if (this.isTop(state)) {
      return 0;
    }

    return super.quantityDropped(state, fortune, random);
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

    if (this.isTop(state)) {
      ItemStack heldItem = player.getHeldItemMainhand();

      if (heldItem.getItem() instanceof ItemIgniterBase) {
        return false;
      }
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean interact(IInteraction.EnumType type, World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (this.isTop(state)) {
      return IBlockInteractable.super.interact(type, world, pos.down(), state, player, hand, facing, hitX, hitY + 1, hitZ);
    }

    return IBlockInteractable.super.interact(type, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBloomery) {
      ((TileBloomery) tileEntity).updateAirflow();
    }
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    if (ModuleBloomeryConfig.BLOOMERY.ENTITY_WALK_BURN_DAMAGE > 0
        && this.isTop(world.getBlockState(pos))) {

      TileEntity tileEntity = world.getTileEntity(pos.down());

      if (tileEntity instanceof TileBloomery
          && ((TileBloomery) tileEntity).isActive()) {

        if (!entity.isImmuneToFire()
            && entity instanceof EntityLivingBase
            && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
          entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModuleBloomeryConfig.BLOOMERY.ENTITY_WALK_BURN_DAMAGE);
          entity.setFire(4);
        }
      }
    }

    super.onEntityWalk(world, pos, entity);
  }

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    RayTraceResult result = super.collisionRayTrace(blockState, world, pos, start, end);

    if (this.isTop(blockState)) {
      return this.interactionRayTrace(result, blockState, world, pos.down(), start, end);

    } else {

      boolean hit = this.rayTrace(pos, start, end, AABB_BOTTOM[0]) != null
          || this.rayTrace(pos, start, end, AABB_BOTTOM[1]) != null;

      if (hit) {
        return this.interactionRayTrace(result, blockState, world, pos, start, end);

      } else {
        return null;
      }
    }
  }

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

    if (this.isTop(state)) {

      BlockPos down = pos.down();

      if (world.getBlockState(down).getBlock() == this) {
        TileEntity tileEntity = world.getTileEntity(down);

        if (tileEntity instanceof ITileContainer) {
          ITileContainer tile = (ITileContainer) tileEntity;
          tile.dropContents();
        }

        StackHelper.spawnStackOnTop(world, new ItemStack(this), down);
        world.setBlockToAir(down);
      }

    } else {

      BlockPos up = pos.up();

      if (world.getBlockState(up).getBlock() == this) {
        world.setBlockToAir(up);
      }

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof ITileContainer) {
        ITileContainer tile = (ITileContainer) tileEntity;
        tile.dropContents();
      }
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

    if (!this.isTop(state)) {

      BlockPos up = pos.up();

      if (super.canPlaceBlockAt(world, up)) {
        EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
        world.setBlockState(up, this.getDefaultState()
            .withProperty(Properties.FACING_HORIZONTAL, facing)
            .withProperty(TYPE, EnumType.Top));
      }
    }
  }

  @Override
  public boolean canSilkHarvest(
      World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player
  ) {

    return false;
  }

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return super.canPlaceBlockAt(world, pos)
        && super.canPlaceBlockAt(world, pos.up());
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (this.getActualState(state, world, pos).getValue(TYPE) == EnumType.BottomLit) {
      return 9;
    }

    return super.getLightValue(state, world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    if (!this.isTop(state)) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileBloomery) {

        if (((TileBloomery) tileEntity).isActive()) {
          return state.withProperty(TYPE, EnumType.BottomLit);
        }
      }
    }

    return super.getActualState(state, world, pos);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (this.isTop(state)) {

      TileEntity tileEntity = world.getTileEntity(pos.down());

      if (tileEntity instanceof TileBloomery
          && ((TileBloomery) tileEntity).isActive()) {

        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + (4.0 / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
        double z = (double) pos.getZ() + 0.5;

        if (rand.nextDouble() < 0.1) {
          world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }

        for (int i = 0; i < 8; i++) {
          double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          double offsetY = 0.25 + (rand.nextDouble() * 2.0 - 1.0) * 0.25;
          double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
        }

        for (int i = 0; i < 4; i++) {
          double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }

        for (int i = 0; i < 4; i++) {
          double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }

        if (RandomHelper.random().nextFloat() < 0.05) {
          double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
          world.spawnParticle(EnumParticleTypes.LAVA, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }
      }

    } else {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileBloomery
          && ((TileBloomery) tileEntity).isActive()) {

        EnumFacing enumfacing = state.getValue(Properties.FACING_HORIZONTAL);
        double offsetY = rand.nextDouble() * 6.0 / 16.0;
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + offsetY;
        double z = (double) pos.getZ() + 0.5;
        double randomOffset = rand.nextDouble() * 0.4 - 0.2;

        if (rand.nextDouble() < 0.1) {
          world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }

        double offset = 0.55;
        double lavaOffset = 0.075;
        double dripChance = 0.25;

        switch (enumfacing) {

          case WEST:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - offset, y, z + randomOffset, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, x - offset, y, z + randomOffset, 0, 0, 0);

            if (rand.nextFloat() < dripChance) {
              world.spawnParticle(EnumParticleTypes.FLAME, x - offset, y, z + randomOffset, 0, 0, 0);
              Minecraft.getMinecraft().effectRenderer.addEffect(
                  ParticleBloomeryDrip.createParticle(world, x - offset - lavaOffset, y - offsetY, z)
              );
            }
            break;

          case EAST:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + offset, y, z + randomOffset, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, x + offset, y, z + randomOffset, 0, 0, 0);

            if (rand.nextFloat() < dripChance) {
              world.spawnParticle(EnumParticleTypes.FLAME, x + offset, y, z + randomOffset, 0, -0.1, 0);
              Minecraft.getMinecraft().effectRenderer.addEffect(
                  ParticleBloomeryDrip.createParticle(world, x + offset + lavaOffset, y - offsetY, z)
              );
            }
            break;

          case NORTH:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomOffset, y, z - offset, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, x + randomOffset, y, z - offset, 0, 0, 0);

            if (rand.nextFloat() < dripChance) {
              world.spawnParticle(EnumParticleTypes.FLAME, x + randomOffset, y, z - offset, 0, -0.1, 0);
              Minecraft.getMinecraft().effectRenderer.addEffect(
                  ParticleBloomeryDrip.createParticle(world, x, y - offsetY, z - offset - lavaOffset)
              );
            }

            break;

          case SOUTH:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + randomOffset, y, z + offset, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, x + randomOffset, y, z + offset, 0, 0, 0);

            if (rand.nextFloat() < dripChance) {
              world.spawnParticle(EnumParticleTypes.FLAME, x + randomOffset, y, z + offset, 0, -0.1, 0);
              Minecraft.getMinecraft().effectRenderer.addEffect(
                  ParticleBloomeryDrip.createParticle(world, x, y - offsetY, z + offset + lavaOffset)
              );
            }
        }
      }
    }
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
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    if (this.isTop(state)) {
      return new TileBloomery.Top();

    } else {
      return new TileBloomery();
    }
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, TYPE);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // N, S, W, E
    // 0, 1, 2, 3 = facing, no top
    // 4, 5, 6, 7 = facing, top

    int type = ((meta >> 2) & 3);
    int facingIndex = (meta & 3) + 2;

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.VALUES[facingIndex])
        .withProperty(TYPE, EnumType.fromMeta(type));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
    EnumType type = state.getValue(TYPE);

    int meta = facing.getIndex() - 2;
    meta |= (type.getMeta() << 2);
    return meta;
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(
      @Nonnull World world,
      @Nonnull BlockPos pos,
      @Nonnull EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      @Nonnull EntityLivingBase placer,
      EnumHand hand
  ) {

    EnumFacing opposite = placer.getHorizontalFacing().getOpposite();
    return this.getDefaultState().withProperty(Properties.FACING_HORIZONTAL, opposite);
  }

  public enum EnumType
      implements IVariant {

    Top(0, "top"),
    Bottom(1, "bottom"),
    BottomLit(2, "bottom_lit");

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
