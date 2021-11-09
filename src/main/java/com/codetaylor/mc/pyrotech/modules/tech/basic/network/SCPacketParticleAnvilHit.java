package com.codetaylor.mc.pyrotech.modules.tech.basic.network;

import com.codetaylor.mc.athenaeum.spi.packet.PacketBlockPosBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class SCPacketParticleAnvilHit
    extends PacketBlockPosBase<SCPacketParticleAnvilHit> {

  private float hitX;
  private float hitY;
  private float hitZ;

  @SuppressWarnings("unused")
  public SCPacketParticleAnvilHit() {
    // serialization
  }

  public SCPacketParticleAnvilHit(BlockPos pos, float hitX, float hitY, float hitZ) {

    super(pos);
    this.hitX = hitX;
    this.hitY = hitY;
    this.hitZ = hitZ;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    buf.writeFloat(this.hitX);
    buf.writeFloat(this.hitY);
    buf.writeFloat(this.hitZ);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    this.hitX = buf.readFloat();
    this.hitY = buf.readFloat();
    this.hitZ = buf.readFloat();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleAnvilHit message, MessageContext ctx) {

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;
    WorldClient world = minecraft.world;
    BlockPos pos = message.blockPos;
    float hitX = message.hitX;
    float hitY = message.hitY;
    float hitZ = message.hitZ;

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileAnvilBase) {
      TileAnvilBase tile = (TileAnvilBase) tileEntity;
      IBlockState blockState = world.getBlockState(pos);

      for (int i = 0; i < 8; ++i) {
        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, tile.getPos().getX() + hitX, tile.getPos().getY() + hitY, tile.getPos().getZ() + hitZ, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
      }

      ItemStackHandler stackHandler = tile.getStackHandler();
      ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getSlotLimit(0), true);
      AnvilRecipe.EnumType type = AnvilRecipe.getTypeFromItemStack(player.getHeldItemMainhand());
      AnvilRecipe recipe = AnvilRecipe.getRecipe(itemStack, tile.getRecipeTier(), type);

      if (recipe instanceof AnvilRecipe.IExtendedRecipe) {
        ((AnvilRecipe.IExtendedRecipe) recipe).onAnvilHitClient(world, tile, hitX, hitY, hitZ);
      }
    }

    return null;
  }
}
