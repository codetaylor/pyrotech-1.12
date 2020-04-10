package com.codetaylor.mc.pyrotech.modules.patreon;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.patreon.data.*;
import com.codetaylor.mc.pyrotech.modules.patreon.data.EffectDataHotfoot;
import com.codetaylor.mc.pyrotech.modules.patreon.data.EffectDataHothead;
import com.google.gson.GsonBuilder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.HashMap;

public class ModulePatreon
    extends ModuleBase {

  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  private static final String PATREON_EFFECTS_JSON_URL = "http://www.codetaylor.com/patreon/pyrotech-effects.json";

  public ModulePatreon() {

    super(0, MOD_ID);
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

    try {
      new EffectDataLoader(
          MOD_ID,
          new UrlEffectDataJsonProvider(
              PATREON_EFFECTS_JSON_URL
          ),
//          new StringEffectDataJsonProvider(
//              new String(Files.readAllBytes(Paths.get("patreon-test.json")))
//          ),
          new GsonEffectDataJsonAdapter(
              new GsonBuilder()
                  .registerTypeAdapter(
                      EffectDataBase.class,
                      new EffectDataGsonAdapter(
                          new HashMap<String, IEffectDataFactory>() {{
                            this.put("hotfoot", EffectDataHotfoot::new);
                            this.put("hothead", EffectDataHothead::new);
                          }}
                      )
                  )
                  .create()
          )
      ).loadEffects();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
