package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockPyroberryBush;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class LivingEntityUseShearsEventHandler {

  @SubscribeEvent
  public static void on(PlayerInteractEvent.RightClickBlock event) {

    EntityPlayer player = event.getEntityPlayer();
    World world = player.world;

    if (world.isRemote) {
      return;
    }

    BlockPos pos = event.getPos();
    ItemStack itemStack = event.getItemStack();
    Item item = itemStack.getItem();

    if (item instanceof ItemShears) {
      IBlockState blockState = world.getBlockState(pos);
      Block block = blockState.getBlock();

      if (block == ModuleCore.Blocks.PYROBERRY_BUSH) {
        ((BlockPyroberryBush) block).onSheared(world, pos, blockState, itemStack, player);
      }
    }
  }
}
