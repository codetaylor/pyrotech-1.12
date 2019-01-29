package com.codetaylor.mc.pyrotech.modules.bloomery.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.bloomery.client.particles.ParticleBloomeryDrip;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemIgniterBase;
import net.minecraft.block.Block;
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
import java.util.Random;

public class BlockBloomery
    extends BlockCombustionWorkerStoneBase {

  public static final String NAME = "bloomery";

  private static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(2.0 / 16.0, 0.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, 8.0 / 16.0, 14.0 / 16.0);

  private static final AxisAlignedBB[] AABB_BOTTOM = {
      AABBHelper.create(0, 0, 0, 16, 8, 16),
      AABBHelper.create(1, 8, 1, 15, 16, 15)
  };

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBloomery();
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (this.isTop(state)) {
      return AABB_TOP;
    }

    return super.getBoundingBox(state, source, pos);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

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

      return this.interact(IInteraction.EnumType.MouseClick, world, pos.down(), state, player, hand, facing, hitX, hitY, hitZ);

    } else {
      return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
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

  @Override
  public boolean isFullBlock(IBlockState state) {

    return false;
  }

  @Override
  public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing face) {

    if (this.isTop(state)) {

      return false;
    }

    return (face == EnumFacing.DOWN);
  }

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    if (!this.isTop(state)) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileBloomery) {

        if (((TileBloomery) tileEntity).isActive()) {
          return state.withProperty(BlockCombustionWorkerStoneBase.TYPE, EnumType.BottomLit);
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
}
