package com.codetaylor.mc.pyrotech.library.patreon.data;

import java.io.Reader;
import java.util.Optional;

public interface IEffectDataJsonProvider {

  Optional<Reader> getEffectDataJson();
}
