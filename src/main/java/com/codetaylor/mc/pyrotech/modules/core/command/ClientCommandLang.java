package com.codetaylor.mc.pyrotech.modules.core.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;

public class ClientCommandLang
    extends CommandBase
    implements IClientCommand {

  @Override
  public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {

    return false;
  }

  @Nonnull
  @Override
  public String getName() {

    return "ptlang";
  }

  @Nonnull
  @Override
  public String getUsage(@Nonnull ICommandSender sender) {

    return "ptlang";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;

    ItemStack heldItemMainhand = player.getHeldItemMainhand();

    if (!heldItemMainhand.isEmpty()) {
      String unlocalizedName = heldItemMainhand.getUnlocalizedName();
      sender.sendMessage(new TextComponentTranslation(unlocalizedName));
    }
  }
}
