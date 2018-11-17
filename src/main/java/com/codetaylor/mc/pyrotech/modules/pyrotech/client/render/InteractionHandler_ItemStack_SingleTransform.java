package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

public class InteractionHandlerItemStack_SingleTransform
    extends InteractionHandlerItemStackBase {

  private final Transforms transforms;

  public InteractionHandlerItemStack_SingleTransform(ItemStackHandler[] stackHandlers, int slot, EnumFacing[] sides, AxisAlignedBB bounds, Transforms transforms) {

    super(stackHandlers, slot, sides, bounds);

    this.transforms = transforms;
  }

  @Override
  public Transforms getTransforms(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack) {

    return this.transforms;
  }

  public static class Transforms {

    public final Vec3d translation;
    public final Quaternion rotation;
    public final Vec3d scale;

    public Transforms(Vec3d translation, Quaternion rotation, Vec3d scale) {

      this.translation = translation;
      this.rotation = rotation;
      this.scale = scale;
    }
  }

}
