package com.codetaylor.mc.pyrotech.modules.storage.block.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockTankBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
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
