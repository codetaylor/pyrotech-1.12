package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockChoppingBlock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.ChoppingBlockRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileChoppingBlock
    extends TileEntity
    implements ITileInteractable {

  private ItemStackHandler stackHandler;
  private int sawdust;
  private int chopsRemaining;
  private IInteraction[] interactions;

  // transient
  private long lastChopTick;

  public TileChoppingBlock() {

    this.stackHandler = new InputStackHandler();
    this.interactions = new IInteraction[]{
        new Interaction(new ItemStackHandler[]{this.stackHandler}),
        new InteractionChop()
    };
    this.chopsRemaining = 10; // TODO: Config Value
  }

  private class InputStackHandler
      extends ItemStackHandler {

    public InputStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {

      super.onContentsChanged(slot);
      TileChoppingBlock.this.markDirty();
      BlockHelper.notifyBlockUpdate(TileChoppingBlock.this.world, TileChoppingBlock.this.pos);
    }
  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public void setSawdust(int sawdust) {

    this.sawdust = Math.max(0, Math.min(5, sawdust));
    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  public int getSawdust() {

    return this.sawdust;
  }

  public void setDamage(int damage) {

    this.world.setBlockState(this.pos, ModuleBlocks.CHOPPING_BLOCK.getDefaultState()
        .withProperty(BlockChoppingBlock.DAMAGE, damage), 3);
  }

  public int getDamage() {

    return this.world.getBlockState(this.pos).getValue(BlockChoppingBlock.DAMAGE);
  }

  public void setChopsRemaining(int chopsRemaining) {

    this.chopsRemaining = chopsRemaining;
    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  public int getChopsRemaining() {

    return this.chopsRemaining;
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setInteger("sawdust", this.sawdust);
    compound.setInteger("chopsRemaining", this.chopsRemaining);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.sawdust = compound.getInteger("sawdust");
    this.chopsRemaining = compound.getInteger("chopsRemaining");
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class Interaction
      extends InteractionItemStack<TileChoppingBlock> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers) {

      super(stackHandlers, 0, new EnumFacing[]{EnumFacing.UP}, InteractionBounds.INFINITE, new Transform(
          Transform.translate(0.5, 0.625, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (ChoppingBlockRecipe.getRecipe(itemStack) != null);
    }

    @Override
    public boolean allowInteractionWithHand(EnumHand hand) {

      // TODO: remove
      return super.allowInteractionWithHand(hand);
    }
  }

  private class InteractionChop
      extends InteractionUseItemBase<TileChoppingBlock> {

    /* package */ InteractionChop() {

      super(new EnumFacing[]{EnumFacing.UP}, InteractionBounds.INFINITE);
    }

    @Override
    protected boolean allowInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItem(hand);
      return heldItem.getItem().getToolClasses(heldItem).contains("axe");
    }

    @Override
    protected boolean doInteraction(TileChoppingBlock tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      long worldTime = world.getTotalWorldTime();

      if (worldTime - tile.lastChopTick < 5) {
        return false;

      } else {
        tile.lastChopTick = worldTime;
      }

      if (!world.isRemote) {

        if (tile.getChopsRemaining() <= 1) {

          // TODO: Config Value
          tile.setChopsRemaining(10);

          if (tile.getDamage() + 1 < 6) {
            tile.setDamage(tile.getDamage() + 1);

            // TODO: sout
            System.out.println("Damage: " + tile.getDamage());

          } else {
            StackHelper.spawnStackHandlerContentsOnTop(world, tile.getStackHandler(), tile.getPos());
            world.destroyBlock(tile.getPos(), false);
          }

        } else {
          tile.setChopsRemaining(tile.getChopsRemaining() - 1);

          // TODO: sout
          System.out.println("Chops: " + tile.getChopsRemaining());
        }

      }

      return true;
    }
  }
}
