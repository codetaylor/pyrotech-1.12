package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockBarrel
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "barrel";
  public static final String NAME_SEALED = "barrel_sealed";

  private static boolean keepInventory;

  private final boolean sealed;

  public BlockBarrel(boolean sealed) {

    super(Material.WOOD);
    this.sealed = sealed;
    this.setHardness(1.0f);
    this.setResistance(3.0f);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);
  }

  public boolean isSealed() {

    return this.sealed;
  }

  public static void setState(boolean sealed, World world, BlockPos pos) {

    TileEntity tileentity = world.getTileEntity(pos);
    keepInventory = true;

    if (sealed) {
      world.setBlockState(pos, ModuleTechBasic.Blocks.BARREL_SEALED.getDefaultState(), 3);

    } else {
      world.setBlockState(pos, ModuleTechBasic.Blocks.BARREL.getDefaultState(), 3);
    }

    keepInventory = false;

    if (tileentity != null) {
      tileentity.validate();
      world.setTileEntity(pos, tileentity);
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (!keepInventory && !this.isSealed()) {
      TileEntity tile = world.getTileEntity(pos);

      if (tile instanceof TileBarrel) {
        TileBarrel tileBarrel = (TileBarrel) tile;
        StackHelper.spawnStackHandlerContentsOnTop(world, tileBarrel.getInputStackHandler(), pos);
        StackHelper.spawnStackHandlerContentsOnTop(world, tileBarrel.getLidStackHandler(), pos);
      }
    }

    super.breakBlock(world, pos, state);
    world.removeTileEntity(pos);
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    if (!this.isSealed()) {
      return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote && this.isSealed()) {
      world.setBlockToAir(pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {

    if (!this.isSealed()) {
      super.getDrops(drops, world, pos, state, fortune);
      return;
    }

    // Serialize the TE into the item dropped.
    // Called before #breakBlock

    drops.add(StackHelper.createItemStackFromTileEntity(
        this,
        1,
        0,
        world.getTileEntity(pos)
    ));
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileBarrel();
  }

  // ---------------------------------------------------------------------------
  // - Creative Menu
  // ---------------------------------------------------------------------------

  @Override
  public CreativeTabs getCreativeTabToDisplayOn() {

    return (this.isSealed()) ? null : super.getCreativeTabToDisplayOn();
  }

  // ---------------------------------------------------------------------------
  // - Tooltip
  // ---------------------------------------------------------------------------

  @Override
  public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag) {

    NBTTagCompound stackTag = stack.getTagCompound();

    if (stackTag == null) {
      return;

    } else {

      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
          || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

        if (stackTag.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {
          NBTTagCompound tileTag = stackTag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);

          if (tileTag.hasKey("inputFluidTank")) {
            NBTTagCompound tankTag = tileTag.getCompoundTag("inputFluidTank");

            if (!tankTag.hasKey("Empty")
                || (tankTag.hasKey("Amount")
                && tankTag.getInteger("Amount") > 0)) {
              FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tankTag);

              if (fluidStack != null) {
                String localizedName = fluidStack.getLocalizedName();
                int amount = fluidStack.amount;
                int capacity = 1000;
                tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid", localizedName, amount, capacity));
              }
            }
          }

          if (tileTag.hasKey("inputStackHandler")) {
            TileBarrel.InputStackHandler stackHandler = new TileBarrel.InputStackHandler(4);
            stackHandler.deserializeNBT(tileTag.getCompoundTag("inputStackHandler"));

            for (int i = 0; i < stackHandler.getSlots(); i++) {
              ItemStack itemStack = stackHandler.getStackInSlot(i);

              if (!itemStack.isEmpty()) {
                String name = itemStack.getItem().getItemStackDisplayName(itemStack);
                tooltip.add(" - " + Reference.Tooltip.COLOR_EXTENDED_INFO + name);
              }
            }
          }
        }

      } else {
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
      }
    }

    boolean hotFluids = false;
    tooltip.add((hotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids." + hotFluids));
  }

  // ---------------------------------------------------------------------------
  // - Sealed Barrel Item
  // ---------------------------------------------------------------------------

  public static class ItemBlockBarrelSealed
      extends ItemBlock {

    public ItemBlockBarrelSealed(Block block) {

      super(block);
      this.setMaxStackSize(1);
    }
  }

}
