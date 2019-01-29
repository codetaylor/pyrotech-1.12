package com.codetaylor.mc.pyrotech.modules.tech.bloomery.block;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPileBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.network.SCPacketParticleLava;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TilePileSlag;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockPileSlag
    extends BlockPileBase {

  public static final String NAME = "pile_slag";

  public static final PropertyBool MOLTEN = PropertyBool.create("molten");

  public BlockPileSlag() {

    super(Material.ROCK);
    this.setHardness(1.75f);
    this.setResistance(40);
    this.setHarvestLevel("pickaxe", 1);
    this.setSoundType(SoundType.STONE);
    this.setTickRandomly(true);

    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(BlockPileBase.LEVEL, 8)
        .withProperty(BlockPileSlag.MOLTEN, false));
  }

  @Override
  protected ItemStack getDrop(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TilePileSlag) {
      return ((TilePileSlag) tileEntity).getStackHandler().extractItem(0, 1, false);
    }

    return ItemStack.EMPTY;
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getBlock() == this
        && state.getValue(BlockPileSlag.MOLTEN)) {
      return 6;
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public int getLightValue(IBlockState state) {

    if (state.getBlock() == this
        && state.getValue(BlockPileSlag.MOLTEN)) {
      return 6;
    }

    return super.getLightValue(state);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (state.getBlock() == this
        && state.getValue(BlockPileSlag.MOLTEN)) {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TilePileSlag) {
        long lastMolten = ((TilePileSlag) tileEntity).getLastMolten();

        if (world.getTotalWorldTime() - lastMolten > 5 * 60 * 20) {
          world.setBlockState(pos, state.withProperty(BlockPileSlag.MOLTEN, false));
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (state.getBlock() == this
        && state.getValue(BlockPileSlag.MOLTEN)) {

      BlockPos posUp = pos.up();
      IBlockState blockStateUp = world.getBlockState(posUp);

      if (!world.isAirBlock(posUp)
          && blockStateUp.getBlock().isNormalCube(blockStateUp, world, posUp)) {
        return;
      }

      int level = state.getValue(BlockPileSlag.LEVEL);

      double x = (double) pos.getX() + 0.5;
      double y = (double) pos.getY() + ((level - 1) / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
      double z = (double) pos.getZ() + 0.5;

      for (int i = 0; i < 4; i++) {
        double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
        double offsetY = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
        double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
        world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);

        if (rand.nextDouble() < 0.05) {
          world.spawnParticle(EnumParticleTypes.LAVA, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
          world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    IBlockState blockState = world.getBlockState(pos);

    if (blockState.getBlock() == this
        && blockState.getValue(BlockPileSlag.MOLTEN)
        && ModuleBloomeryConfig.SLAG.MOLTEN_WALK_DAMAGE > 0
        && !entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModuleBloomeryConfig.SLAG.MOLTEN_WALK_DAMAGE);
    }

    super.onEntityWalk(world, pos, entity);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    if (state.getBlock() == this) {

      if (!world.isRemote
          && state.getValue(BlockPileSlag.MOLTEN)) {
        int level = state.getValue(BlockPileBase.LEVEL);
        int dimension = world.provider.getDimension();
        ModuleBloomery.PACKET_SERVICE.sendToAllAround(new SCPacketParticleLava(pos, level), dimension, pos);

        if (ModuleBloomeryConfig.SLAG.HARVESTING_PLAYER_FIRE_DURATION_SECONDS > 0
            && ModuleBloomeryConfig.SLAG.HARVESTING_PLAYER_FIRE_CHANCE > 0
            && RandomHelper.random().nextDouble() < ModuleBloomeryConfig.SLAG.HARVESTING_PLAYER_FIRE_CHANCE) {
          player.setFire(ModuleBloomeryConfig.SLAG.HARVESTING_PLAYER_FIRE_DURATION_SECONDS);
        }
      }
    }

    super.harvestBlock(world, player, pos, state, te, stack);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    super.breakBlock(world, pos, state);
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TilePileSlag();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, BlockPileBase.LEVEL, BlockPileSlag.MOLTEN);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(BlockPileBase.LEVEL, (meta & 7) + 1)
        .withProperty(BlockPileSlag.MOLTEN, ((meta >> 3) & 1) == 1);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(BlockPileBase.LEVEL) - 1)
        | ((state.getValue(BlockPileSlag.MOLTEN) ? 1 : 0) << 3);
  }

  public static class ItemBlockPileSlag
      extends ItemBlock {

    public ItemBlockPileSlag(BlockPileSlag block) {

      super(block);
    }

    @Nonnull
    @Override
    public BlockPileSlag getBlock() {

      return (BlockPileSlag) super.getBlock();
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {

      Properties properties = ModuleBloomery.Blocks.GENERATED_PILE_SLAG.get(this.getBlock());
      ItemStack slagItem;

      if (properties == null) {
        slagItem = new ItemStack(ModuleBloomery.Items.SLAG);

      } else {
        slagItem = new ItemStack(properties.slagItem);
      }

      boolean result = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

      if (result) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TilePileSlag) {
          TilePileSlag.StackHandler stackHandler = ((TilePileSlag) tileEntity).getStackHandler();

          for (int i = 0; i < 8; i++) {
            stackHandler.insertItem(i, slagItem.copy(), false);
          }
        }
      }

      return result;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {

      if (stack.getItem() == this) {

        Properties properties = ModuleBloomery.Blocks.GENERATED_PILE_SLAG.get(this.getBlock());

        if (properties != null) {

          String langKey = properties.langKey + ".name";

          if (I18n.canTranslate(langKey)) {
            String translatedLangKey = I18n.translateToLocal(langKey);
            return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
          }
        }
      }

      return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }
  }

  public static class Properties {

    @Nullable
    public final String langKey;
    public final int color;
    public final ItemSlag slagItem;

    public Properties(@Nullable String langKey, int color, ItemSlag slagItem) {

      this.langKey = langKey;
      this.color = color;
      this.slagItem = slagItem;
    }
  }
}
