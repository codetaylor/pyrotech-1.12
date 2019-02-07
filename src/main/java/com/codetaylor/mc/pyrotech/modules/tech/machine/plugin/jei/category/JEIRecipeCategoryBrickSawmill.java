package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.spi.JEIRecipeCategorySawmillBase;
import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryBrickSawmill
    extends JEIRecipeCategorySawmillBase {

  public static final String UID = ModuleTechMachine.MOD_ID + ".brick.mill";

  private static final String TITLE_KEY = "gui." + ModuleTechMachine.MOD_ID + ".jei.category.sawmill.brick";

  public JEIRecipeCategoryBrickSawmill(IGuiHelper guiHelper) {

    super(guiHelper);
  }

  @Override
  protected String getTitleKey() {

    return TITLE_KEY;
  }

  @Nonnull
  @Override
  public String getUid() {

    return UID;
  }
}
