package com.codetaylor.mc.pyrotech.modules.hunting.event;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpear;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class LivingDeathEventHandler {

  @SubscribeEvent
  public void on(LivingDeathEvent event) {

    EntityLivingBase entity = event.getEntityLiving();
    World world = entity.world;

    if (world.isRemote) {
      return;
    }

    ISpearEntityData data = CapabilitySpear.get(entity);

    if (data != null && data.getItemStackCount() > 0) {

      for (ItemStack itemStack : data.getItemStacks(new ArrayList<>())) {
        StackHelper.spawnStack(world, itemStack, entity.posX, entity.posY, entity.posZ);
      }
    }
  }
}