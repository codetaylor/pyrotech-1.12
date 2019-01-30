package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.pyrotech.library.util.FloodFill;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileActivePile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Predicate;

public class IgnitionHandler {

  public static final int BLOCK_IGNITION_LIMIT = 27;

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {

    ItemStack itemStack = event.getItemStack();
    BlockPos pos = event.getPos();
    World world = event.getWorld();

    Item item = itemStack.getItem();

    if (item == Items.FLINT_AND_STEEL) {

      if (IgnitionHandler.igniteBlocks(world, pos)) {
        world.playSound(
            null,
            pos,
            SoundEvents.ITEM_FLINTANDSTEEL_USE,
            SoundCategory.BLOCKS,
            1.0F,
            Util.RANDOM.nextFloat() * 0.4F + 0.8F
        );

        event.setUseItem(Event.Result.ALLOW);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event) {

    World world = event.getWorld();
    BlockPos pos = event.getPos();

    if (world.isRemote) {
      return;
    }

    Block fireBlock = world.getBlockState(pos).getBlock();

    if (event.isCanceled()
        || fireBlock != Blocks.FIRE) {
      return;
    }

    for (EnumFacing facing : event.getNotifiedSides()) {
      IgnitionHandler.igniteBlocks(world, pos.offset(facing));
    }
  }

  public static boolean igniteBlocks(World world, BlockPos pos) {

    IBlockState blockState = world.getBlockState(pos);
    PitBurnRecipe recipe = PitBurnRecipe.getRecipe(blockState);
    boolean result = false;

    if (recipe != null) {

      Predicate<IBlockState> predicate = recipe.getInputMatcher();

      result = FloodFill.apply(
          world,
          pos,
          (w, p) -> predicate.test(w.getBlockState(p)),
          (w, p) -> {
            w.setBlockState(p, ModuleBlocks.ACTIVE_PILE.getDefaultState());
            TileEntity tileEntity = w.getTileEntity(p);

            if (tileEntity instanceof TileActivePile) {
              ((TileActivePile) tileEntity).setRecipe(recipe);
            }
          },
          BLOCK_IGNITION_LIMIT
      );
    }

    return result;
  }

}
