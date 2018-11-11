package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRack;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDryingRack
    extends Block {

  public static final String NAME = "drying_rack";

  public BlockDryingRack() {

    super(Material.WOOD);
    this.setHardness(1.0f);
    this.setResistance(5.0f);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (facing != EnumFacing.UP) {
      return false;
    }

    int x = (hitX < 0.5) ? 0 : 1;
    int y = (hitZ < 0.5) ? 0 : 1;
    int index = x | (y << 1);

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRack) {

      TileDryingRack dryingRack = (TileDryingRack) tileEntity;
      ItemStackHandler stackHandler = dryingRack.getStackHandler();
      ItemStackHandler outputStackHandler = dryingRack.getOutputStackHandler();
      ItemStack heldItemMainhand = player.getHeldItemMainhand();

      if (heldItemMainhand.isEmpty()) {

        // Remove input

        if (!stackHandler.getStackInSlot(index).isEmpty()) {

          ItemStack result = stackHandler.extractItem(index, 64, world.isRemote);

          if (!result.isEmpty()) {

            if (!world.isRemote) {
              StackHelper.spawnStackOnTop(world, result, pos);
            }

            return true;
          }
        }

        // Remove output

        if (!outputStackHandler.getStackInSlot(index).isEmpty()) {

          ItemStack result = outputStackHandler.extractItem(index, 64, world.isRemote);

          if (!result.isEmpty()) {

            if (!world.isRemote) {
              StackHelper.spawnStackOnTop(world, result, pos);
            }

            return true;
          }
        }

      } else {

        if (stackHandler.getStackInSlot(index).isEmpty()
            && outputStackHandler.getStackInSlot(index).isEmpty()) {

          // Insert item

          ItemStack itemStack = new ItemStack(heldItemMainhand.getItem(), 1, heldItemMainhand.getMetadata());

          DryingRackRecipe recipe = DryingRackRecipe.getRecipe(itemStack);

          if (recipe == null) {

            // The item doesn't have a recipe, place it in the output slot.

            ItemStack result = outputStackHandler.insertItem(index, itemStack, world.isRemote);

            if (result.isEmpty()) {

              if (!world.isRemote) {
                heldItemMainhand.setCount(heldItemMainhand.getCount() - 1);
              }

              return true;
            }

          } else {

            // The item has a recipe, place it in the input slot.

            ItemStack result = stackHandler.insertItem(index, itemStack, world.isRemote);

            if (result.isEmpty()) {

              if (!world.isRemote) {
                heldItemMainhand.setCount(heldItemMainhand.getCount() - 1);
              }

              return true;
            }
          }
        }
      }

    }

    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRack) {
      ((TileDryingRack) tileEntity).removeItems();
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 150;
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return world.isSideSolid(pos.down(), EnumFacing.UP)
        && super.canPlaceBlockAt(world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, BlockPos pos, EnumFacing side) {

    return false;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {

    return this.isFullBlock(state);
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return new AxisAlignedBB(0f / 16f, 0f / 16f, 0f / 16f, 16f / 16f, 12f / 16f, 16f / 16f);
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileDryingRack();
  }
}
