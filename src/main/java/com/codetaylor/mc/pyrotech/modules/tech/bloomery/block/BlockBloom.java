package com.codetaylor.mc.pyrotech.modules.tech.bloomery.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.minecraft.block.BlockFalling.canFallThrough;

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
  // - Creative Menu
  // ---------------------------------------------------------------------------

  @Override
  public CreativeTabs getCreativeTabToDisplayOn() {

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    return 9;
  }

  @Override
  public int getLightValue(IBlockState state) {

    return 9;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    if (ModuleTechBloomeryConfig.BLOOM.ENTITY_WALK_DAMAGE > 0
        && !entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModuleTechBloomeryConfig.BLOOM.ENTITY_WALK_DAMAGE);
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

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    BlockPos down = pos.down();
    IBlockState blockState = world.getBlockState(down);

    return super.canPlaceBlockAt(world, pos)
        && blockState.getBlock().isSideSolid(blockState, world, down, EnumFacing.UP);
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

      if (tile.getIntegrity() > 0) {
        drops.add(BloomHelper.toItemStack(tile));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    this.checkFire(world, pos, rand);
    this.checkFall(world, pos);
  }

  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    double x = (double) pos.getX() + 0.5;
    double y = (double) pos.getY() + (4.0 / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
    double z = (double) pos.getZ() + 0.5;

    if (rand.nextDouble() < 0.1) {
      world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
    }

    for (int i = 0; i < 4; i++) {
      double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetY = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Falling
  // ---------------------------------------------------------------------------

  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

    world.scheduleUpdate(pos, this, this.tickRate(world));
  }

  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    world.scheduleUpdate(pos, this, this.tickRate(world));
  }

  @Override
  public int tickRate(World world) {

    return 2;
  }

  private void checkFall(World world, BlockPos pos) {

    // From BlockFalling#checkFallable

    if ((world.isAirBlock(pos.down()) || BlockFalling.canFallThrough(world.getBlockState(pos.down()))) && pos.getY() >= 0) {

      if (!BlockFalling.fallInstantly && world.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {

        if (!world.isRemote) {
          TileEntity tileEntity = world.getTileEntity(pos);

          if (tileEntity != null) {
            EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos)) {

              @Nullable
              @Override
              public EntityItem entityDropItem(ItemStack stack, float offsetY) {

                if (stack.isEmpty()) {
                  return null;

                } else {

                  if (this.tileEntityData != null) {
                    BloomHelper.createBloomAsItemStack(stack, this.tileEntityData);
                  }

                  EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double) offsetY, this.posZ, stack);
                  entityitem.setDefaultPickupDelay();

                  if (captureDrops) {
                    this.capturedDrops.add(entityitem);

                  } else {
                    this.world.spawnEntity(entityitem);
                  }

                  return entityitem;
                }
              }
            };
            entityfallingblock.tileEntityData = tileEntity.writeToNBT(new NBTTagCompound());
            world.spawnEntity(entityfallingblock);
          }
        }

      } else {
        IBlockState state = world.getBlockState(pos);
        world.setBlockToAir(pos);
        BlockPos blockpos;

        for (blockpos = pos.down(); (world.isAirBlock(blockpos) || canFallThrough(world.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
          ;
        }

        if (blockpos.getY() > 0) {
          world.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Fire
  // ---------------------------------------------------------------------------

  private void checkFire(World world, BlockPos pos, Random rand) {

    int age = 0;
    int humidityModifier = 0;

    if (world.isBlockinHighHumidity(pos)) {
      humidityModifier = -50;
    }

    this.tryCatchFire(world, pos.east(), 300 + humidityModifier, rand, age, EnumFacing.WEST);
    this.tryCatchFire(world, pos.west(), 300 + humidityModifier, rand, age, EnumFacing.EAST);
    this.tryCatchFire(world, pos.down(), 50, rand, age, EnumFacing.UP);
    this.tryCatchFire(world, pos.up(), 250 + humidityModifier, rand, age, EnumFacing.DOWN);
    this.tryCatchFire(world, pos.north(), 300 + humidityModifier, rand, age, EnumFacing.SOUTH);
    this.tryCatchFire(world, pos.south(), 300 + humidityModifier, rand, age, EnumFacing.NORTH);

    int flammability = world.getBlockState(pos.down()).getBlock().getFlammability(world, pos, EnumFacing.UP);

    if (flammability > 0) {
      world.setBlockState(pos.down(), Blocks.FIRE.getDefaultState(), 1 | 2);
    }

    BloomHelper.trySpawnFire(world, pos, rand, ModuleTechBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_RANDOM);
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
      this.setMaxStackSize(1);
    }

    public int getMaxIntegrity(ItemStack itemStack) {

      return this.getTileInteger(itemStack, "maxIntegrity");
    }

    public int getIntegrity(ItemStack itemStack) {

      return this.getTileInteger(itemStack, "integrity");
    }

    public void setIntegrity(ItemStack itemStack, int integrity) {

      if (itemStack.getItem() == this) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound != null) {
          NBTTagCompound tileTag = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
          tileTag.setInteger("integrity", integrity);
        }
      }
    }

    private int getTileInteger(ItemStack itemStack, String key) {

      if (itemStack.getItem() == this) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound != null) {
          NBTTagCompound tileTag = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
          return tileTag.getInteger(key);
        }
      }

      return 0;
    }

    @Nullable
    public String getRecipeId(ItemStack itemStack) {

      if (itemStack.getItem() == this) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound != null) {
          NBTTagCompound tileTag = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
          return tileTag.getString("recipeId");
        }
      }

      return null;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {

      return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity entity, ItemStack itemstack) {

      EntityItemBloom entityItemBloom = new EntityItemBloom(world, entity.posX, entity.posY, entity.posZ, itemstack);

      entityItemBloom.motionX = entity.motionX;
      entityItemBloom.motionY = entity.motionY;
      entityItemBloom.motionZ = entity.motionZ;

      entityItemBloom.setPickupDelay(40);

      return entityItemBloom;
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
        String[] langKeys = teCompound.getString("langKey").split(";");

        if (langKeys.length == 1) {
          String langKey = langKeys[0] + ".name";

          if (I18n.canTranslate(langKey)) {
            String translatedLangKey = I18n.translateToLocal(langKey);
            return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
          }

        } else if (langKeys.length > 1) {

          String translatedLangKey = null;

          for (int i = 0; i < langKeys.length; i++) {
            String langKey = langKeys[i] + ".name";

            if (I18n.canTranslate(langKey)) {

              if (translatedLangKey == null) {
                translatedLangKey = I18n.translateToLocal(langKey);

              } else {
                translatedLangKey = I18n.translateToLocalFormatted(langKey, translatedLangKey);
              }
            }
          }

          if (translatedLangKey != null) {
            return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
          }
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

      float playerDamagePerSecond = (float) ModuleTechBloomeryConfig.BLOOM.FIRE_DAMAGE_PER_SECOND;

      if (playerDamagePerSecond > 0) {
        entity.attackEntityFrom(DamageSource.IN_FIRE, playerDamagePerSecond);
        entity.setFire(1);
      }
    }

    public static class EntityItemBloom
        extends EntityItem {

      public static final String NAME = "pyrotech.EntityItemBloom";

      @SuppressWarnings("unused")
      public EntityItemBloom(World world) {

        // Serialization
        super(world);
        this.setNoDespawn();
        this.setEntityInvulnerable(true);
        this.isImmuneToFire = true;
      }

      /* package */ EntityItemBloom(World world, double x, double y, double z, ItemStack stack) {

        super(world, x, y, z, stack);
        this.setNoDespawn();
        this.setEntityInvulnerable(true);
        this.isImmuneToFire = true;
      }

      @Override
      public void onUpdate() {

        super.onUpdate();
      }

      @Override
      public void onEntityUpdate() {

        super.onEntityUpdate();

        if (this.world != null
            && this.world.isRemote
            && this.world.getTotalWorldTime() % 5 == 0) {

          double x = this.posX;
          double y = this.posY + (4.0 / 16.0) + (this.rand.nextDouble() * 2.0 / 16.0);
          double z = this.posZ;

          if (this.rand.nextDouble() < 0.1) {
            this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
          }

          double offsetX = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          double offsetY = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          double offsetZ = (this.rand.nextDouble() * 2.0 - 1.0) * 0.1;
          this.world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
        }
      }
    }

  }

}
