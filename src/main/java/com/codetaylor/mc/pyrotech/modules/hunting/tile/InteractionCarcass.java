package com.codetaylor.mc.pyrotech.modules.hunting.tile;

import com.codetaylor.mc.athenaeum.interaction.spi.ITileInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketNoHunger;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InteractionCarcass<T extends TileEntity & ITileInteractable>
    extends InteractionUseItemBase<T> {

  public interface IInteractionCarcassDelegate {

    boolean canUseWithHungerLevel(int playerFoodLevel);

    boolean canUseWithHeldItem(String registryName);

    void doExhaustion(EntityPlayer player);

    int getItemEfficiency(String registryName);

    void setCurrentProgress(float value);

    float getCurrentProgress();

    void resetProgress();

    BlockPos getPosition();

    ItemStack extractItem();

    boolean isEmpty();

    void destroyCarcass();
  }

  private final IInteractionCarcassDelegate delegate;

  public InteractionCarcass(
      EnumFacing[] facings,
      AxisAlignedBB bounds,
      IInteractionCarcassDelegate delegate
  ) {

    super(facings, bounds);
    this.delegate = delegate;
  }

  @Override
  protected boolean allowInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (this.delegate.canUseWithHungerLevel(player.getFoodStats().getFoodLevel())) {

      if (!world.isRemote) {
        ModuleTechBasic.PACKET_SERVICE.sendTo(new SCPacketNoHunger(), (EntityPlayerMP) player);
      }
      return false;
    }

    ItemStack heldItemStack = player.getHeldItem(hand);
    Item heldItem = heldItemStack.getItem();

    ResourceLocation resourceLocation = heldItem.getRegistryName();

    if (resourceLocation == null) {
      return false;
    }

    String registryName = resourceLocation.toString();
    return this.delegate.canUseWithHeldItem(registryName);
  }

  @Override
  protected boolean doInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (!world.isRemote) {

      // Charge exhaustion.
      this.delegate.doExhaustion(player);

      // Play sound for chop.
      world.playSound(
          null,
          player.posX,
          player.posY,
          player.posZ,
          SoundEvents.BLOCK_SLIME_PLACE,
          SoundCategory.BLOCKS,
          0.75f,
          (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
      );

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();

      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      String registryName = resourceLocation.toString();
      int efficiency = this.delegate.getItemEfficiency(registryName);

      // Advance the progress.
      this.delegate.setCurrentProgress(this.delegate.getCurrentProgress() - efficiency);

      if (this.delegate.getCurrentProgress() <= 0) {
        // Check progress, drop item, reset progress or destroy carcass.

        ItemStack itemStack = this.delegate.extractItem();

        if (!itemStack.isEmpty()) {
          StackHelper.spawnStackOnTop(world, itemStack, this.delegate.getPosition());
        }

        if (this.delegate.isEmpty()) {
          this.delegate.destroyCarcass();

        } else {
          this.delegate.resetProgress();
        }
      }

    } else {

      // Client particles

      IBlockState blockState = ModuleHunting.Blocks.CARCASS.getDefaultState();

      for (int i = 0; i < 8; ++i) {
        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.delegate.getPosition().getX() + hitX, this.delegate.getPosition().getY() + hitY, this.delegate.getPosition().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
      }

    }

    return true;
  }
}
