package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBook
    extends Item {

  public static final String NAME = "book";

  @Override
  public boolean hasCustomEntity(ItemStack stack) {

    return true;
  }

  @Nullable
  @Override
  public Entity createEntity(World world, Entity entity, ItemStack itemStack) {

    EntityItemBook entityItemBook = new EntityItemBook(world, entity.posX, entity.posY, entity.posZ, itemStack);

    entityItemBook.motionX = entity.motionX;
    entityItemBook.motionY = entity.motionY;
    entityItemBook.motionZ = entity.motionZ;

    entityItemBook.setPickupDelay(40);

    return entityItemBook;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ResourceLocation resourceLocation = new ResourceLocation("pyrotech", "book");

    if (!world.isRemote) {

      if (player instanceof EntityPlayerMP) {
        PatchouliAPI.instance.openBookGUI((EntityPlayerMP) player, resourceLocation);
        SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

        if (sfx != null) {
          world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  public static class EntityItemBook
      extends EntityItem {

    public static final String NAME = "pyrotech.EntityItemBook";

    // Serialization
    @SuppressWarnings("unused")
    public EntityItemBook(World world) {

      super(world);
    }

    public EntityItemBook(World world, double x, double y, double z, ItemStack stack) {

      super(world, x, y, z, stack);
      this.setNoDespawn();
      this.isImmuneToFire = true;
    }

    @Override
    public void onEntityUpdate() {

      super.onEntityUpdate();

      if (this.world != null
          && this.world.isRemote
          && this.world.getTotalWorldTime() % 5 == 0) {

        double x = this.posX;
        double y = this.posY + (6.0 / 16.0) + (this.rand.nextDouble() * 2.0 / 16.0);
        double z = this.posZ;

        if (this.rand.nextFloat() < 0.1) {
          this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }

        if (this.rand.nextFloat() < 0.5) {
          double offsetX = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          double offsetY = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          double offsetZ = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          this.world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
        }
      }
    }
  }

}
