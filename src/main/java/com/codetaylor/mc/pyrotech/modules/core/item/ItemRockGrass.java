package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityRockGrass;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemRockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemRockGrass
    extends ItemRockBase {

  public ItemRockGrass(Block block) {

    super(block);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(@Nonnull ItemStack stack) {

    return "tile." + ModuleCore.MOD_ID + ".rock_grass";
  }

  @Override
  protected EntityThrowable createThrowable(World world, EntityPlayer player, int meta) {

    return new EntityRockGrass(world, player, meta);
  }
}
