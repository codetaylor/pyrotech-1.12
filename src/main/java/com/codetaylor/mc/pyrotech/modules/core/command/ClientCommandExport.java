package com.codetaylor.mc.pyrotech.modules.core.command;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;

public class ClientCommandExport
    extends CommandBase
    implements IClientCommand {

  @Override
  public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {

    return false;
  }

  @Nonnull
  @Override
  public String getName() {

    return "pt_export";
  }

  @Nonnull
  @Override
  public String getUsage(@Nonnull ICommandSender sender) {

    return "pt_export";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {

    int size = 512;

    if (args.length > 0) {

      try {
        size = Integer.valueOf(args[0]);

      } catch (Exception e) {
        throw new CommandException("", e);
      }
    }

    ModuleCore.Utils.BLOCK_RENDERER.render(size);
  }
}
