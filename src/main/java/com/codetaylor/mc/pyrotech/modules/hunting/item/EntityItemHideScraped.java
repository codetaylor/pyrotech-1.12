package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleProgress;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;

public class EntityItemHideScraped
    extends EntityItem {

  public static final String NAME = "pyrotech." + EntityItemHideScraped.class.getSimpleName();

  private int ticksInWater;
  private ItemStack transformedItem;

  @SuppressWarnings("unused")
  public EntityItemHideScraped(World world) {

    // serialization
    super(world);
    this.lifespan = Integer.MAX_VALUE;
  }

  public EntityItemHideScraped(World world, double x, double y, double z, ItemStack stack, ItemStack transformedItem) {

    super(world, x, y, z, stack);
    this.transformedItem = transformedItem;
    this.lifespan = Integer.MAX_VALUE;

    if (stack.hasTagCompound()) {
      NBTTagCompound tagCompound = stack.getTagCompound();

      if (tagCompound != null) {
        this.ticksInWater = tagCompound.getInteger("ticksInWater");
      }
    }
  }

  @Override
  public void onEntityUpdate() {

    super.onEntityUpdate();

    if (this.world == null) {
      return;
    }

    if (this.transformedItem == null) {
      return;
    }

    ItemStack itemStack = this.getItem();

    if (itemStack.getItem() == this.transformedItem.getItem()) {
      return;
    }

    if (!this.world.isRemote) {
      BlockPos pos = this.getPosition();
      IBlockState blockState = this.world.getBlockState(pos);
      Block block = blockState.getBlock();

      if ((block instanceof BlockFluidBase
          && ((BlockFluidBase) block).getFluid() == FluidRegistry.WATER)
          || block == Blocks.WATER) {

        this.ticksInWater += 1;

        if (this.ticksInWater % 40 == 0) {
          ModuleHunting.PACKET_SERVICE.sendToAllAround(
              new SCPacketParticleProgress(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 2),
              this.world.provider.getDimension(),
              pos
          );
        }

        if (this.ticksInWater % (10 * 20) == 0) {
          NBTTagCompound tagCompound;

          if (itemStack.hasTagCompound()) {
            tagCompound = itemStack.getTagCompound();

          } else {
            tagCompound = new NBTTagCompound();
          }

          //noinspection ConstantConditions
          tagCompound.setInteger("ticksInWater", this.ticksInWater);
          itemStack.setTagCompound(tagCompound);
        }
      }

      if (this.ticksInWater >= ModuleHuntingConfig.IN_WORLD_HIDE_SOAK_TICKS) {
        ItemStack copy = this.transformedItem.copy();
        copy.setCount(itemStack.getCount());
        this.setItem(copy);
      }
    }
  }

  @Override
  public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    super.writeEntityToNBT(compound);
    compound.setInteger("ticksInWater", this.ticksInWater);
    compound.setTag("transformedItem", this.transformedItem.serializeNBT());
  }

  @Override
  public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    super.readEntityFromNBT(compound);
    this.ticksInWater = compound.getInteger("ticksInWater");
    this.transformedItem = new ItemStack(compound.getCompoundTag("transformedItem"));
  }
}
