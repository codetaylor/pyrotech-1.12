package com.codetaylor.mc.pyrotech.patreon.lib.data;

import java.util.UUID;

public interface IEffectDataFactory<E extends EffectDataBase> {

  E createEffectData(UUID uuid);

}
