package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.interaction.api.Quaternion;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.library.util.ParticleHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TileDryingRackCrude
    extends TileDryingRackBase
    implements ITileInteractable {

  private IInteraction[] interactionHandlers;
  private AxisAlignedBB renderBounds;

  public TileDryingRackCrude() {

    super(1);

    this.interactionHandlers = new IInteraction[]{
        new TileDryingRackCrude.Interaction(
            new ItemStackHandler[]{this.inputStackHandler, this.outputStackHandler}
        )
    };
  }

  @Override
  protected ModuleTechBasicConfig.DryingRackConditionalModifiers getConditionalModifiers() {

    return ModuleTechBasicConfig.CRUDE_DRYING_RACK.CONDITIONAL_MODIFIERS;
  }

  @Override
  protected int getSlotCount() {

    return 1;
  }

  @Override
  protected float getMultiplicativeSpeedModifier() {

    return (float) ModuleTechBasicConfig.CRUDE_DRYING_RACK.SPEED_MODIFIER;
  }

  @Override
  protected Map<String, Float> getBiomeSpeeds() {

    return ModuleTechBasicConfig.CrudeDryingRack.BIOME_MODIFIERS;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    if (this.renderBounds == null) {
      this.renderBounds = new AxisAlignedBB(this.getPos());
    }

    return this.renderBounds;
  }

  @Override
  public void update() {

    super.update();

    if (this.world.isRemote
        && ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
        && this.getSpeed() > 0
        && this.hasInput()
        && this.world.getTotalWorldTime() % 40 == 0) {
      this.displayProgressParticles();
    }
  }

  private void displayProgressParticles() {

    EnumFacing tileFacing = this.getTileFacing(this.world, this.pos, this.world.getBlockState(this.pos));

    switch (tileFacing) {

      case NORTH:
        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.5, this.pos.getY() + 0.4, this.pos.getZ() + 0.125 + 0.125,
            0.125, 0.125, 0.0625
        );
        break;

      case SOUTH:
        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.5, this.pos.getY() + 0.4, this.pos.getZ() + 0.875 - 0.125,
            0.125, 0.125, 0.0625
        );
        break;

      case EAST:
        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.825 - 0.125, this.pos.getY() + 0.4, this.pos.getZ() + 0.5,
            0.0625, 0.125, 0.125
        );
        break;

      case WEST:
        ParticleHelper.spawnProgressParticlesClient(
            1,
            this.pos.getX() + 0.125 + 0.125, this.pos.getY() + 0.4, this.pos.getZ() + 0.5,
            0.0625, 0.125, 0.125
        );
        break;
    }
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_DRYING_RACK_CRUDE;
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactionHandlers;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechBasic.Blocks.DRYING_RACK) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  private class Interaction
      extends InteractionItemStack<TileDryingRackCrude> {

    public Interaction(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          EnumFacing.VALUES,
          BlockDryingRack.AABB_NORTH,
          new Transform(
              new Vec3d(0.5, 0.5, 0.15),
              new Quaternion(),
              new Vec3d(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    protected int getInsertionIndex(TileDryingRackCrude tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!this.stackHandlers[0].getStackInSlot(this.slot).isEmpty()) {
        // This will prevent an item being placed in handler 1 while handler 0 is occupied.
        return 0;

      } else if (!this.stackHandlers[1].getStackInSlot(this.slot).isEmpty()) {
        // This will prevent an item being placed in handler 0 while handler 1 is occupied.
        return 1;
      }

      return (DryingRackRecipe.getRecipe(player.getHeldItemMainhand()) != null) ? 0 : 1;
    }
  }

}
