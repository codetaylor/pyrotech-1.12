package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemTongs;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockBloom
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final AxisAlignedBB AABB = AABBHelper.create(4, 0, 4, 12, 8, 12);

  public static final String NAME = "bloom";

  public BlockBloom() {

    super(Material.ROCK);
    this.setHardness(7.5f);
    this.setResistance(30.0f);
    this.setHarvestLevel("pickaxe", 1);
    this.setTickRandomly(true);
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    return 9;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    if (ModulePyrotechConfig.BLOOM.ENTITY_WALK_DAMAGE > 0
        && !entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModulePyrotechConfig.BLOOM.ENTITY_WALK_DAMAGE);
    }

    super.onEntityWalk(world, pos, entity);
  }

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = playerIn.getHeldItemMainhand();

    if (heldItem.getItem() instanceof ItemTongs) {
      return false;
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote) {
      world.setBlockToAir(pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {

    // Check to see if the TE still contains items and
    // serialize the TE into the item dropped.

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBloom) {
      TileBloom tile = (TileBloom) tileEntity;
      DynamicStackHandler stackHandler = tile.getStackHandler();
      ItemStack firstNonEmptyItemStack = stackHandler.getFirstNonEmptyItemStack();

      if (!firstNonEmptyItemStack.isEmpty()) {
        drops.add(TileBloom.toItemStack(tile));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    int age = 0;
    int humidityModifier = 0;

    if (world.isBlockinHighHumidity(pos)) {
      humidityModifier = -50;
    }

    this.tryCatchFire(world, pos.east(), 300 + humidityModifier, rand, age, EnumFacing.WEST);
    this.tryCatchFire(world, pos.west(), 300 + humidityModifier, rand, age, EnumFacing.EAST);
    this.tryCatchFire(world, pos.down(), 250 + humidityModifier, rand, age, EnumFacing.UP);
    this.tryCatchFire(world, pos.up(), 250 + humidityModifier, rand, age, EnumFacing.DOWN);
    this.tryCatchFire(world, pos.north(), 300 + humidityModifier, rand, age, EnumFacing.SOUTH);
    this.tryCatchFire(world, pos.south(), 300 + humidityModifier, rand, age, EnumFacing.NORTH);

    BlockHelper.forBlocksInCube(world, pos, 1, 1, 1, (w, p, bs) -> {

      if (w.isAirBlock(p)) {

        BlockPos down = p.down();
        IBlockState blockState = w.getBlockState(down);

        if (blockState.isSideSolid(w, down, EnumFacing.UP)) {
          w.setBlockState(p, Blocks.FIRE.getDefaultState(), 1 | 2);
          return false;
        }
      }

      return true;
    });
  }

  private void tryCatchFire(World world, BlockPos pos, int chance, Random random, int age, EnumFacing face) {

    // From BlockFire#tryCatchFire

    int flammability = world.getBlockState(pos).getBlock().getFlammability(world, pos, face);

    if (random.nextInt(chance) < flammability) {
      IBlockState blockState = world.getBlockState(pos);

      if (random.nextInt(age + 10) < 5 && !world.isRainingAt(pos)) {
        int j = age + random.nextInt(5) / 4;

        if (j > 15) {
          j = 15;
        }

        world.setBlockState(pos, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, j), 3);
      }

      if (blockState.getBlock() == Blocks.TNT) {
        Blocks.TNT.onBlockDestroyedByPlayer(world, pos, blockState.withProperty(BlockTNT.EXPLODE, true));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileBloom();
  }

  // ---------------------------------------------------------------------------
  // - Item
  // ---------------------------------------------------------------------------

  public static class ItemBlockBloom
      extends ItemBlock {

    public ItemBlockBloom(Block block) {

      super(block);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {

      if (stack.getItem() != this) {
        return super.getItemStackDisplayName(stack);
      }

      NBTTagCompound tagCompound = stack.getTagCompound();

      if (tagCompound != null
          && tagCompound.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {

        NBTTagCompound teCompound = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
        String langKey = teCompound.getString("langKey") + ".name";

        if (I18n.canTranslate(langKey)) {
          String translatedLangKey = I18n.translateToLocal(langKey);
          return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
        }
      }

      return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

      if (world.isRemote) {
        return;
      }

      if (world.getTotalWorldTime() % 20 != 0) {
        return;
      }

      float playerDamagePerSecond = (float) ModulePyrotechConfig.BLOOM.FIRE_DAMAGE_PER_SECOND;

      if (playerDamagePerSecond > 0) {
        entity.attackEntityFrom(DamageSource.IN_FIRE, playerDamagePerSecond);
        entity.setFire(1);
      }
    }

  }

}
