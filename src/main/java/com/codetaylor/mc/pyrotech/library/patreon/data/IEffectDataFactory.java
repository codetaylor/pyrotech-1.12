package com.codetaylor.mc.pyrotech.library.patreon.data;

import java.util.UUID;

public interface IEffectDataFactory<E extends EffectDataBase> {

  E createEffectData(UUID uuid);

}
