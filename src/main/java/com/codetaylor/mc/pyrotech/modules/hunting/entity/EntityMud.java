package com.codetaylor.mc.pyrotech.modules.hunting.entity;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.network.SCPacketParticleMud;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityMud
    extends EntitySlime {

  public static final String NAME = "pyrotech.mud";

  private static final ResourceLocation LOOT_TABLE = new ResourceLocation("pyrotech", "entities/mud");

  public EntityMud(World world) {

    super(world);
  }

  @Nonnull
  @Override
  protected EntityMud createInstance() {

    return new EntityMud(this.world);
  }

  @Override
  protected Item getDropItem() {

    return null;
  }

  @Nullable
  @Override
  protected ResourceLocation getLootTable() {

    return this.getSlimeSize() == 1 ? LOOT_TABLE : LootTableList.EMPTY;
  }

  @Override
  protected boolean spawnCustomParticles() {

    if (this.world.isRemote) {
      return true;
    }

    // This is fired when the entity lands, we can hook it for the mud.
    this.spawnMud(this.getPosition());

    SCPacketParticleMud packet = new SCPacketParticleMud(this.posX, this.posY, this.posZ, this.getSlimeSize());
    ModuleHunting.PACKET_SERVICE.sendToAllAround(packet, this.dimension, this.posX, this.posY, this.posZ);
    return true;
  }

  private void spawnMud(BlockPos pos) {

    BlockHelper.forBlocksInRange(this.world, pos, 1 + RandomHelper.random().nextInt(this.getSlimeSize()), (w, p, bs) -> {
      Block block = bs.getBlock();

      if (block == Blocks.AIR ||
          (
              block.isReplaceable(this.world, p)
                  && !(block instanceof IFluidBlock)
                  && (block != FluidRegistry.WATER.getBlock())
                  && (block != FluidRegistry.LAVA.getBlock())
          )
      ) {

        BlockPos downPos = p.down();
        IBlockState downState = w.getBlockState(downPos);

        for (int i = 0; i < this.getSlimeSize(); i++) {

          //noinspection deprecation
          if (downState.getBlock().isTopSolid(downState)
              && RandomHelper.random().nextFloat() < 0.5) {

            if (block == ModuleCore.Blocks.MUD_LAYER) {
              Material material = downState.getMaterial();

              if (RandomHelper.random().nextFloat() < 0.25 &&
                  (
                      material == Material.GROUND
                          || material == Material.GRASS
                  )
              ) {
                w.setBlockState(downPos, ModuleCore.Blocks.MUD.getDefaultState(), 1 | 2);
              }

            } else if (block == ModuleCore.Blocks.ROCK && bs.getValue(BlockRock.VARIANT) == BlockRock.EnumType.MUD) {
              w.setBlockState(p, ModuleCore.Blocks.MUD_LAYER.getDefaultState(), 1 | 2);

            } else {
              w.setBlockState(p, ModuleCore.Blocks.ROCK.getDefaultState().withProperty(BlockRock.VARIANT, BlockRock.EnumType.MUD), 1 | 2);
            }

            break;
          }
        }
      }
      return true; // continue executing
    });
  }
}
