package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class PatchouliClientHelper {

  public static void openBook(World world, EntityPlayer player, ResourceLocation resourceLocation, ResourceLocation entryLocation, int page) {

    PatchouliAPI.instance.openBookGUI((EntityPlayerMP) player, resourceLocation);
    SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

    if (sfx != null) {
      world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
    }

    Book book = BookRegistry.INSTANCE.books.get(resourceLocation);
    BookEntry entry = null;

    for (ResourceLocation location : book.contents.entries.keySet()) {

      if (entryLocation.equals(location)) {
        entry = book.contents.entries.get(location);
        break;
      }
    }

    if (entry != null) {

      if (!entry.isLocked()) {
        GuiBook curr = book.contents.getCurrentGui();
        book.contents.currentGui = new GuiBookEntry(book, entry, page);
        player.swingArm(EnumHand.MAIN_HAND);

        if (curr instanceof GuiBookEntry) {
          GuiBookEntry currEntry = (GuiBookEntry) curr;
          if (currEntry.getEntry() == entry && currEntry.getPage() == page) {
            return;
          }
        }

        book.contents.guiStack.push(curr);
      }

    }
  }

}
