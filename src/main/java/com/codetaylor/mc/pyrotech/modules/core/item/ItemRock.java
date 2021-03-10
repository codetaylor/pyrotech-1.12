package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityRock;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemRockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemRock
    extends ItemRockBase {

  public ItemRock(Block block) {

    super(block);
    this.setHasSubtypes(true);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "tile." + ModuleCore.MOD_ID + "." + BlockRock.EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Override
  public int getMetadata(int damage) {

    return damage;
  }

  @Override
  protected EntityThrowable createThrowable(World world, EntityPlayer player, int meta) {

    return new EntityRock(world, player, meta);
  }
}
