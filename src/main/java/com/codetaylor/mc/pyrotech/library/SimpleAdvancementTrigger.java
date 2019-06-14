package com.codetaylor.mc.pyrotech.library;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleAdvancementTrigger
    implements ICriterionTrigger {

  private final ResourceLocation resourceLocation;
  private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

  public SimpleAdvancementTrigger(ResourceLocation resourceLocation) {

    super();
    this.resourceLocation = resourceLocation;
  }

  @Nonnull
  @Override
  public ResourceLocation getId() {

    return this.resourceLocation;
  }

  @Override
  public void addListener(@Nonnull PlayerAdvancements playerAdvancements, @Nonnull ICriterionTrigger.Listener listener) {

    SimpleAdvancementTrigger.Listeners listeners = this.listeners.get(playerAdvancements);

    if (listeners == null) {
      listeners = new SimpleAdvancementTrigger.Listeners(playerAdvancements);
      this.listeners.put(playerAdvancements, listeners);
    }

    listeners.add(listener);
  }

  @Override
  public void removeListener(@Nonnull PlayerAdvancements playerAdvancements, @Nonnull ICriterionTrigger.Listener listener) {

    SimpleAdvancementTrigger.Listeners listeners = this.listeners.get(playerAdvancements);

    if (listeners != null) {
      listeners.remove(listener);

      if (listeners.isEmpty()) {
        this.listeners.remove(playerAdvancements);
      }
    }
  }

  @Override
  public void removeAllListeners(@Nonnull PlayerAdvancements playerAdvancements) {

    this.listeners.remove(playerAdvancements);
  }

  @Nonnull
  @Override
  public SimpleAdvancementTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {

    return new SimpleAdvancementTrigger.Instance(this.getId());
  }

  public void trigger(EntityPlayerMP player) {

    PlayerAdvancements advancements = player.getAdvancements();
    SimpleAdvancementTrigger.Listeners listeners = this.listeners.get(advancements);

    if (listeners != null) {
      listeners.trigger();
    }
  }

  public static class Instance
      extends AbstractCriterionInstance {

    public Instance(ResourceLocation parRL) {

      super(parRL);
    }
  }

  static class Listeners {

    private final PlayerAdvancements playerAdvancements;
    private final Set<Listener> listeners = Sets.newHashSet();

    public Listeners(PlayerAdvancements playerAdvancementsIn) {

      this.playerAdvancements = playerAdvancementsIn;
    }

    public boolean isEmpty() {

      return this.listeners.isEmpty();
    }

    public void add(ICriterionTrigger.Listener listener) {

      this.listeners.add(listener);
    }

    public void remove(ICriterionTrigger.Listener listener) {

      this.listeners.remove(listener);
    }

    public void trigger() {

      List<Listener> list = null;

      for (ICriterionTrigger.Listener listener : this.listeners) {

        if (list == null) {
          list = Lists.newArrayList();
        }

        list.add(listener);
      }

      if (list != null) {

        for (ICriterionTrigger.Listener listener : list) {
          listener.grantCriterion(this.playerAdvancements);
        }
      }
    }
  }
}