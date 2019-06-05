package com.codetaylor.mc.pyrotech.library.patreon.data;

import java.io.Reader;
import java.util.Optional;

public interface IEffectDataJsonAdapter {

  Optional<EffectDataList> adaptJson(Reader in);
}
