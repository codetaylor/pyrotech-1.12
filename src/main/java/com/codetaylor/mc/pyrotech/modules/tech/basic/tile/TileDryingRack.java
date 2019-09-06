package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
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
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.spi.DryingRackRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TileDryingRack
    extends TileDryingRackBase
    implements ITileInteractable {

  private IInteraction[] interactions;

  public TileDryingRack() {

    super(4);

    this.interactions = new IInteraction[4];

    for (int slot = 0; slot < 4; slot++) {

      final int qX = (slot & 1);
      final int qZ = ((slot >> 1) & 1);

      this.interactions[slot] = new TileDryingRack.Interaction(
          new ItemStackHandler[]{
              this.inputStackHandler,
              this.outputStackHandler
          },
          slot,
          qX,
          qZ
      );

    }

  }

  @Override
  public DryingRackRecipeBase getRecipe(ItemStack itemStack) {

    return DryingRackRecipe.getRecipe(itemStack);
  }

  @Override
  protected ModuleTechBasicConfig.DryingRackConditionalModifiers getConditionalModifiers() {

    return ModuleTechBasicConfig.DRYING_RACK.CONDITIONAL_MODIFIERS;
  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getMultiplicativeSpeedModifier() {

    return (float) ModuleTechBasicConfig.DRYING_RACK.SPEED_MODIFIER;
  }

  @Override
  protected Map<String, Float> getBiomeSpeeds() {

    return ModuleTechBasicConfig.DryingRack.BIOME_MODIFIERS;
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechBasic.Blocks.DRYING_RACK) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return ITileInteractable.super.getTileFacing(world, pos, blockState);
  }

  @Override
  public void update() {

    super.update();

    if (ModuleTechBasicConfig.DRYING_RACK.USE_AS_LADDER) {
      IBlockState blockState = this.world.getBlockState(this.pos);
      IBlockState blockStateUp = this.world.getBlockState(this.pos.up());
      IBlockState blockStateDown = this.world.getBlockState(this.pos.down());

      if (blockState.getValue(BlockDryingRack.VARIANT) == BlockDryingRack.EnumType.NORMAL
          && ((blockStateUp.getBlock() == ModuleTechBasic.Blocks.DRYING_RACK
          && blockStateUp.getValue(BlockDryingRack.VARIANT) == BlockDryingRack.EnumType.NORMAL)
          || (blockStateDown.getBlock() == ModuleTechBasic.Blocks.DRYING_RACK
          && blockStateDown.getValue(BlockDryingRack.VARIANT) == BlockDryingRack.EnumType.NORMAL))) {
        this.tryClimb();
      }
    }

    if (this.world.isRemote
        && ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
        && this.getSpeed() > 0
        && this.hasInput()
        && this.world.getTotalWorldTime() % 40 == 0) {
      ParticleHelper.spawnProgressParticlesClient(1, this.pos.getX() + 0.5, this.pos.getY() + 0.75, this.pos.getZ() + 0.5, 0.5, 0.15, 0.5);
    }
  }

  private void tryClimb() {

    List<EntityPlayer> playerList = this.world
        .getEntitiesWithinAABB(EntityPlayer.class, AABBHelper.create(0, 0, 0, 0, 0, 0)
            .offset(0.5, 0.5, 0.5)
            .offset(this.pos)
            .grow(0.6, 0.2, 0.6));

    if (!playerList.isEmpty()) {
      double climbSpeed = ModuleTechBasicConfig.DRYING_RACK.CLIMB_SPEED;

      for (EntityPlayer player : playerList) {

        if (player.isSneaking()) {
          player.motionY = 0.0D;

        } else if (player.collidedHorizontally) {

          if (player.moveForward > 0.0F && player.motionY < climbSpeed) {
            player.motionY = climbSpeed;
          }
        } else if (player.motionY < -climbSpeed) {
          player.motionY = -climbSpeed;
        }

        player.fallDistance = 0;
      }
    }
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_DRYING_RACK;
  }

  private class Interaction
      extends InteractionItemStack<TileDryingRack> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers, int slot, double x, double z) {

      super(
          stackHandlers,
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * 0.5, 0, z * 0.5, x * 0.5 + 0.5, 12f / 16f, z * 0.5 + 0.5),
          new Transform(
              Transform.translate(x * 0.375 + 0.25 + 0.0625, 0.75 + 0.03125, z * 0.375 + 0.25 + 0.0625),
              Transform.rotate(1, 0, 0, -90),
              Transform.scale(0.25, 0.25, 0.25)
          )
      );
    }

    @Override
    protected int getInsertionIndex(TileDryingRack tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

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
