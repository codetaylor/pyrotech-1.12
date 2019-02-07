package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.spi.JEIRecipeCategoryCrucibleBase;
import mezz.jei.api.IGuiHelper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryBrickCrucible
    extends JEIRecipeCategoryCrucibleBase {

  public static final String UID = ModuleTechMachine.MOD_ID + ".brick.crucible";

  private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(ModuleTechMachine.MOD_ID, "textures/gui/jei10.png");
  private static final String TITLE_KEY = "gui." + ModuleTechMachine.MOD_ID + ".jei.category.crucible.brick";

  public JEIRecipeCategoryBrickCrucible(IGuiHelper guiHelper) {

    super(guiHelper);
  }

  @Override
  protected ResourceLocation getBackgroundResourceLocation() {

    return RESOURCE_LOCATION;
  }

  @Nonnull
  @Override
  public String getUid() {

    return UID;
  }

  @Override
  protected String getTitleKey() {

    return TITLE_KEY;
  }

  @Override
  protected int getOutputTankCapacity() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.OUTPUT_TANK_SIZE;
  }
}
