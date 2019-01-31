package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.entity.EntityRock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemRock
    extends ItemBlock {

  public ItemRock(Block block) {

    super(block);
    this.setHasSubtypes(true);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "tile." + ModuleCore.MOD_ID + "." + BlockRock.EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack itemstack = player.getHeldItem(hand);

    if (!player.isCreative()) {
      itemstack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (!world.isRemote) {
      EntityRock entity = new EntityRock(world, player, itemstack.getMetadata());
      entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
      world.spawnEntity(entity);
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
  }

  @Override
  public int getMetadata(int damage) {

    return damage;
  }
}
