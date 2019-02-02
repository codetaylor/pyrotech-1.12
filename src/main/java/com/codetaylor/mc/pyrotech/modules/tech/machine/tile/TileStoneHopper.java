package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileStoneHopper
    extends TileEntity
    implements ITickable {

  private TickCounter updateTickCounter;

  public TileStoneHopper() {

    this.updateTickCounter = new TickCounter(20);
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.updateTickCounter != null
        && this.updateTickCounter.increment()) {
      
      TileEntity tileSource = this.world.getTileEntity(this.pos.up());
      IBlockState blockState = this.world.getBlockState(this.pos);
      EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
      TileEntity tileTarget = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

      if (tileSource == null || tileTarget == null) {
        return;
      }

      IItemHandler handlerSource = tileSource.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
      IItemHandler handlerTarget = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

      if (handlerSource == null || handlerTarget == null) {
        return;
      }

      int amount = 1;

      for (int slotSource = 0; slotSource < handlerSource.getSlots(); slotSource++) {

        boolean canTransfer = false;

        {
          ItemStack stackSource = handlerSource.extractItem(slotSource, amount, true);
          int initialCount = stackSource.getCount();

          if (stackSource != ItemStack.EMPTY) {

            for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
              stackSource = handlerTarget.insertItem(slotTarget, stackSource, true);

              if (stackSource.isEmpty()) {
                canTransfer = true;
                break;

              } else if (stackSource.getCount() != initialCount) {
                canTransfer = true;
              }
            }
          }
        }

        if (canTransfer) {
          ItemStack stackSource = handlerSource.extractItem(slotSource, amount, false);

          for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
            stackSource = handlerTarget.insertItem(slotTarget, stackSource, false);

            if (stackSource.isEmpty()) {
              break;
            }
          }

          break;
        }
      }
    }
  }
}
