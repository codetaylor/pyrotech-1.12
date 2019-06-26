package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockMechanicalHopper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileStoneHopper
    extends TileCogWorkerBase {

  public TileStoneHopper() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  protected int getTransferAmount(ItemStack cog) {

    return ModuleTechMachineConfig.STONE_HOPPER.getCogTransferAmount(cog.getItem().getRegistryName());
  }

  @Override
  protected int getUpdateIntervalTicks() {

    return ModuleTechMachineConfig.STONE_HOPPER.TRANSFER_INTERVAL_TICKS;
  }

  @Override
  protected boolean isValidCog(ItemStack itemStack) {

    return (ModuleTechMachineConfig.STONE_HOPPER.getCogTransferAmount(itemStack.getItem().getRegistryName()) > -1);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  protected int doWork(ItemStack cog) {

    TileEntity tileSource = this.world.getTileEntity(this.pos.up());
    IBlockState blockState = this.world.getBlockState(this.pos);
    EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
    BlockMechanicalHopper.EnumType type = blockState.getValue(BlockMechanicalHopper.TYPE);

    if (type == BlockMechanicalHopper.EnumType.Down) {
      facing = EnumFacing.UP;
    }

    TileEntity tileTarget = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

    if (tileSource == null || tileTarget == null) {
      return -1;
    }

    IItemHandler handlerSource = tileSource.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
    IItemHandler handlerTarget = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

    if (handlerSource == null || handlerTarget == null) {
      return -1;
    }

    int amount = Math.min(cog.getMaxDamage() - cog.getItemDamage(), this.getTransferAmount(cog));
    int totalInserted = 0;

    for (int slotSource = 0; slotSource < handlerSource.getSlots(); slotSource++) {

      boolean canTransfer = false;

      {
        ItemStack stackSource = handlerSource.extractItem(slotSource, amount, true);
        int initialCount = stackSource.getCount();

        if (stackSource != ItemStack.EMPTY) {

          for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
            int before = stackSource.getCount();
            stackSource = handlerTarget.insertItem(slotTarget, stackSource, true);
            totalInserted += (before - stackSource.getCount());

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

        // --- Transfer

        ItemStack stackSource = handlerSource.extractItem(slotSource, totalInserted, false);
        int initialCount = stackSource.getCount();

        for (int slotTarget = 0; slotTarget < handlerTarget.getSlots(); slotTarget++) {
          stackSource = handlerTarget.insertItem(slotTarget, stackSource, false);

          if (stackSource.isEmpty()) {
            break;
          }
        }

        // --- Play Sound

        SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f);

        // --- Return the cog damage

        int cogDamage = 1;

        if (ModuleTechMachineConfig.STONE_HOPPER.COG_DAMAGE_TYPE == ModuleTechMachineConfig.StoneHopper.EnumCogDamageType.PerItem) {
          cogDamage = (initialCount - stackSource.getCount());
        }

        return cogDamage;
      }
    }

    return -1;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_MECHANICAL_HOPPER;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.STONE_HOPPER) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return super.getTileFacing(world, pos, blockState);
  }

}
