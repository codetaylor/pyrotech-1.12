package com.codetaylor.mc.pyrotech.modules.ignition.block;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.block.spi.BlockTorchBase;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileTorchFiber;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class BlockTorchFiber
    extends BlockTorchBase {

  public static final String NAME = "torch_fiber";

  @Override
  public TileEntity createTileEntity() {

    return new TileTorchFiber();
  }

  @Override
  protected void getLitDrops(NonNullList<ItemStack> drops) {

    if (Math.random() > 0.5) {
      drops.add(new ItemStack(Items.STICK));

    } else {
      drops.add(ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack());
    }
  }

  @Override
  protected int getLightValue() {

    return MathHelper.clamp(ModuleIgnitionConfig.FIBER_TORCH.LIGHT_VALUE, 0, 15);
  }

  @Override
  protected int getFireDamage() {

    return MathHelper.clamp(ModuleIgnitionConfig.FIBER_TORCH.FIRE_DAMAGE, 0, Integer.MAX_VALUE);
  }
}
