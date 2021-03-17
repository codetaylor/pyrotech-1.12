package com.codetaylor.mc.pyrotech.modules.core.entity;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EntityPyroberryCocktail
    extends EntityThrowable {

  public static final String NAME = "pyroberry_cocktail";

  // serialization
  @SuppressWarnings("unused")
  public EntityPyroberryCocktail(World world) {

    super(world);
  }

  public EntityPyroberryCocktail(World world, EntityLivingBase thrower) {

    super(world, thrower);
  }

  @SideOnly(Side.CLIENT)
  public void handleStatusUpdate(byte id) {

    if (id == 3) {

      for (int i = 0; i < 8; ++i) {
        this.world.spawnParticle(
            EnumParticleTypes.ITEM_CRACK,
            this.posX, this.posY, this.posZ,
            ((double) this.rand.nextFloat() - 0.5) * 0.08, ((double) this.rand.nextFloat() - 0.5) * 0.08, ((double) this.rand.nextFloat() - 0.5) * 0.08,
            Item.getIdFromItem(ModuleCore.Items.PYROBERRY_COCKTAIL)
        );
        this.world.spawnParticle(
            EnumParticleTypes.EXPLOSION_LARGE,
            this.posX, this.posY, this.posZ,
            ((double) this.rand.nextFloat() - 0.5) * 0.08, 0, ((double) this.rand.nextFloat() - 0.5) * 0.08
        );
        this.world.spawnParticle(
            EnumParticleTypes.SMOKE_LARGE,
            this.rand.nextFloat() * 0.5 + this.posX, this.posY, this.rand.nextFloat() * 0.5 + this.posZ,
            0, ((double) this.rand.nextFloat()) * 0.08, 0
        );
      }
    }
  }

  @Override
  protected void onImpact(@Nonnull RayTraceResult result) {

    if (result.entityHit != null) {
      result.entityHit.setFire(ModuleCoreConfig.PYROBERRY_COCKTAIL.ENTITY_FIRE_DURATION_SECONDS);
    }

    if (!this.world.isRemote) {
      BlockPos blockPos = result.getBlockPos();

      //noinspection ConstantConditions
      if (blockPos == null) {
        blockPos = result.entityHit.getPosition();
      }

      this.world.playSound(null, blockPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 1);
      this.world.playSound(null, blockPos, SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.PLAYERS, 1, 1);
      this.world.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 0.5f, 1);

      if (ModuleCoreConfig.PYROBERRY_COCKTAIL.FIRE_RANGE > 0) {
        float chance = (float) MathHelper.clamp(ModuleCoreConfig.PYROBERRY_COCKTAIL.FIRE_CHANCE, 0, 1);

        BlockHelper.forBlocksInRange(this.world, blockPos, ModuleCoreConfig.PYROBERRY_COCKTAIL.FIRE_RANGE, (w, p, bs) -> {
          Block block = w.getBlockState(p).getBlock();
          Block blockDown = w.getBlockState(p.down()).getBlock();

          if ((block == Blocks.AIR || block.isReplaceable(w, p))
              && blockDown != Blocks.AIR
              && RandomHelper.random().nextFloat() < chance) {
            this.world.setBlockState(p, Blocks.FIRE.getDefaultState(), 3);
          }

          return true;
        });
      }

      this.world.setEntityState(this, (byte) 3);
      this.setDead();
    }
  }
}
