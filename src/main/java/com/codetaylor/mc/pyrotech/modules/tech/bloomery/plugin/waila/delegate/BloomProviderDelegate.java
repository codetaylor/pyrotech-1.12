package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class BloomProviderDelegate
    extends ProviderDelegateBase<BloomProviderDelegate.IBloomDisplay, TileBloom> {

  public BloomProviderDelegate(IBloomDisplay display) {

    super(display);
  }

  @Override
  public void display(TileBloom tile) {

    throw new UnsupportedOperationException();
  }

  public void display(TileBloom tile, EntityPlayer player) {

    int integrity = (int) ((tile.getIntegrity() / (float) tile.getMaxIntegrity()) * 100);
    this.display.setIntegrity("gui." + ModuleTechBloomery.MOD_ID + ".waila.bloom.integrity", integrity);

    int recipeProgress = (int) (tile.getRecipeProgress() * 100);
    this.display.setHammered("gui." + ModuleTechBloomery.MOD_ID + ".waila.bloom.hammered", recipeProgress);

    int hammerPower = (int) (BloomHelper.calculateHammerPower(tile.getPos(), player) * 100);

    if (hammerPower > 0) {
      this.display.setHammerPower(
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloom.hammer.power",
          hammerPower
      );

    } else {
      this.display.setHammerPower(
          TextFormatting.RED,
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloom.hammer.power",
          hammerPower
      );
    }
  }

  public interface IBloomDisplay {

    void setIntegrity(String langKey, int integrity);

    void setHammered(String langKey, int recipeProgress);

    void setHammerPower(String langKey, int hammerPower);

    void setHammerPower(TextFormatting formatting, String langKey, int hammerPower);
  }
}
