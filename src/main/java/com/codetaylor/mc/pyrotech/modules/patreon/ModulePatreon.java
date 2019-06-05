package com.codetaylor.mc.pyrotech.modules.patreon;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.patreon.data.*;
import com.codetaylor.mc.pyrotech.modules.patreon.data.EffectDataHotfoot;
import com.google.gson.GsonBuilder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class ModulePatreon
    extends ModuleBase {

  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModulePatreon.class.getSimpleName());

  public ModulePatreon() {

    super(0, MOD_ID);
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

    new EffectDataLoader(
        MOD_ID,
        //new UrlEffectDataJsonProvider("url"),
        new StringEffectDataJsonProvider(
            "{\"effects\":[{\"uuid\":\"46562dd7-ada9-4af8-b88c-3a0f2d3e8860\",\"effect\":\"hotfoot\",\"params\":{}}]}"
        ),
        new GsonEffectDataJsonAdapter(
            new GsonBuilder()
                .registerTypeAdapter(
                    EffectDataBase.class,
                    new EffectDataGsonAdapter(
                        new HashMap<String, IEffectDataFactory>() {{
                          this.put("hotfoot", EffectDataHotfoot::new);
                        }}
                    )
                )
                .create()
        )
    ).loadEffects();
  }
}
