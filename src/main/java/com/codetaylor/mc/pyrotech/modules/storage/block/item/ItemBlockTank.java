package com.codetaylor.mc.pyrotech.modules.storage.block.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockTankBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemBlockTank
    extends ItemBlock {

  private final BlockTankBase block;

  public ItemBlockTank(BlockTankBase block) {

    super(block);
    this.block = block;
  }

  @Override
  public boolean hasContainerItem(@Nonnull ItemStack stack) {

    return true;
  }

  @Nonnull
  @Override
  public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {

    // Create a copy such that the game can't mess with it
    ItemStack result = new ItemStack(this);
    result.setTagCompound(ItemBlockTank.getTankTagSafe(itemStack));
    IFluidHandlerItem fluidHandler = result.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

    if (fluidHandler != null) {
      fluidHandler.drain(1000, true);
    }

    return result;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {

    if (!world.setBlockState(pos, newState, 11)) {
      return false;
    }

    IBlockState state = world.getBlockState(pos);

    if (state.getBlock() == this.block) {
      setTileEntityNBT(world, player, pos, stack);
      this.block.onBlockPlacedBy(world, pos, state, player, stack, side);

      if (player instanceof EntityPlayerMP) {
        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
      }
    }

    return true;
  }

  @ParametersAreNonnullByDefault
  public static void writeTank(FluidTank tank, ItemStack itemStack) {

    tank.writeToNBT(ItemBlockTank.getTankTagSafe(itemStack));
  }

  @ParametersAreNonnullByDefault
  public static void readTank(FluidTank tank, ItemStack itemStack) {

    tank.readFromNBT(ItemBlockTank.getTankTagSafe(itemStack));
  }

  @Nullable
  public static NBTTagCompound getTankTag(@Nonnull ItemStack itemStack) {

    return itemStack.getTagCompound();
  }

  @Nonnull
  public static NBTTagCompound getTankTagSafe(@Nonnull ItemStack itemStack) {

    return StackHelper.getTagSafe(itemStack);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {

    return new TankFluidCapability(stack, this.block.getCapacity());
  }

  private static class TankFluidCapability
      extends FluidHandlerItemStack {

    public TankFluidCapability(@Nonnull ItemStack container, int capacity) {

      super(container, capacity);
    }

    @Override
    protected void setFluid(FluidStack fluid) {

      NBTTagCompound tankTag = ItemBlockTank.getTankTagSafe(this.container);
      tankTag.removeTag("Empty");
      fluid.writeToNBT(tankTag);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {

      NBTTagCompound tankTag = ItemBlockTank.getTankTag(this.container);

      if (tankTag == null || tankTag.hasKey("Empty")) {
        return null;
      }

      return FluidStack.loadFluidStackFromNBT(tankTag);
    }

    @Override
    protected void setContainerToEmpty() {

      NBTTagCompound tankTag = ItemBlockTank.getTankTagSafe(this.container);
      tankTag.setString("Empty", "");
      tankTag.removeTag("FluidName");
      tankTag.removeTag("Amount");
      tankTag.removeTag("Tag");
    }
  }
}
