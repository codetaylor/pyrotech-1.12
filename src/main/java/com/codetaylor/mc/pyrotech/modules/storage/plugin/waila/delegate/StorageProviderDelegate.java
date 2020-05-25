package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate;

import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.athenaeum.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class StorageProviderDelegate
    extends ProviderDelegateBase<StorageProviderDelegate.IStorageDisplay, TileEntity> {

  public StorageProviderDelegate(IStorageDisplay display) {

    super(display);
  }

  @Override
  public void display(TileEntity tile) {

    this.display(tile, null);
  }

  public void display(TileEntity tile, @Nullable EntityPlayer player) {

    if (player == null) {
      return;
    }

    int distance = 5;
    Vec3d posVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
    RayTraceResult rayTraceResult = tile.getWorld().rayTraceBlocks(posVec, posVec.add(player.getLookVec().scale(distance)), false);

    if (rayTraceResult == null) {
      return;
    }

    if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
      return;
    }

    if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {
      InteractionRayTraceData.List list = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (InteractionRayTraceData data : list) {
        IInteraction interaction = data.getInteraction();

        if (interaction.isEnabled()
            && interaction instanceof IInteractionItemStack) {
          ItemStack stackInSlot = ((IInteractionItemStack) interaction).getStackInSlot();

          if (!stackInSlot.isEmpty()) {
            this.display.setItem(stackInSlot);
            this.display.setItemLabel(stackInSlot);
          }
        }
      }
    }
  }

  public interface IStorageDisplay {

    void setItem(ItemStack itemStack);

    void setItemLabel(ItemStack itemStack);
  }
}
