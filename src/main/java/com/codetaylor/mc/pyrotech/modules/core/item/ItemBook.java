package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.advancement.AdvancementTriggers;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.ModulePluginPatchouli;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.PatchouliHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBook
    extends Item {

  public static final String NAME = "book";

  public ItemBook() {

    this.setMaxStackSize(1);
  }

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

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModulePluginPatchouli.class)) {
          AdvancementTriggers.MOD_ITEM_TRIGGER.trigger((EntityPlayerMP) player);
          PatchouliHelper.openBook(world, player, resourceLocation);
          return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
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
      this.setNoDespawn();
      this.setEntityInvulnerable(true);
      this.isImmuneToFire = true;
    }

    public EntityItemBook(World world, double x, double y, double z, ItemStack stack) {

      super(world, x, y, z, stack);
      this.setNoDespawn();
      this.setEntityInvulnerable(true);
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
