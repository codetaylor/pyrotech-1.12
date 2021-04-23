package com.codetaylor.mc.pyrotech.modules.hunting.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpearEntityData
    implements ISpearEntityData,
    Capability.IStorage<ISpearEntityData> {

  private final List<ItemStack> itemStacks;
  private int itemStackCount;
  private int seed;

  public SpearEntityData() {

    this.itemStacks = new ArrayList<>(1);
  }

  @Override
  public void setSeed(int seed) {

    this.seed = seed;
  }

  @Override
  public int getSeed() {

    return this.seed;
  }

  @Override
  public void setItemStackCount(int count) {

    this.itemStackCount = count;
  }

  @Override
  public int getItemStackCount() {

    return this.itemStackCount;
  }

  @Override
  public List<ItemStack> getItemStacks(List<ItemStack> result) {

    result.addAll(this.itemStacks);
    return result;
  }

  @Override
  public void clearItemStacks() {

    this.itemStacks.clear();
    this.itemStackCount = 0;
  }

  @Override
  public void addItemStack(ItemStack itemStack) {

    this.itemStacks.add(itemStack.copy());
    this.itemStackCount = this.itemStacks.size();
  }

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<ISpearEntityData> capability, ISpearEntityData instance, EnumFacing side) {

    NBTTagCompound result = new NBTTagCompound();

    {
      NBTTagList tag = new NBTTagList();
      result.setTag("itemStacks", tag);

      for (ItemStack itemStack : this.itemStacks) {
        tag.appendTag(itemStack.serializeNBT());
      }
    }

    result.setInteger("seed", this.seed);

    return result;
  }

  @Override
  public void readNBT(Capability<ISpearEntityData> capability, ISpearEntityData instance, EnumFacing side, NBTBase nbt) {

    NBTTagCompound tag = (NBTTagCompound) nbt;

    NBTTagList itemStacks = tag.getTagList("itemStacks", Constants.NBT.TAG_COMPOUND);

    for (int i = 0; i < itemStacks.tagCount(); i++) {
      this.addItemStack(new ItemStack(itemStacks.getCompoundTagAt(i)));
    }

    this.seed = tag.getInteger("seed");
  }
}
