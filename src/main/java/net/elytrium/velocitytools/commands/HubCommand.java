/*
 * Copyright (C) 2021 - 2023 Elytrium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.velocitytools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.proxy.command.builtin.CommandMessages;
import java.util.List;
import java.util.Map;

import net.elytrium.velocitytools.Settings;
import net.elytrium.velocitytools.VelocityTools;
import net.kyori.adventure.text.Component;

public class HubCommand implements SimpleCommand {

  private final ProxyServer server;
  private final Map<String, List<String>> servers;
  private final Component disabledServer;
  private final List<String> disabledServers;
  private final String youGotMoved;
  private final Component youGotMovedComponent;

  public HubCommand(ProxyServer server) {
    this.server = server;
    this.servers = Settings.IMP.COMMANDS.HUB.SERVERS;
    this.disabledServers = Settings.IMP.COMMANDS.HUB.DISABLED_SERVERS;
    this.disabledServer = VelocityTools.getSerializer().deserialize(Settings.IMP.COMMANDS.HUB.DISABLED_SERVER);
    this.youGotMoved = Settings.IMP.COMMANDS.HUB.YOU_GOT_MOVED;
    this.youGotMovedComponent = VelocityTools.getSerializer().deserialize(this.youGotMoved);
  }

  @Override
  public void execute(SimpleCommand.Invocation invocation) {
    CommandSource source = invocation.source();
    if (!(source instanceof Player)) {
      source.sendMessage(CommandMessages.PLAYERS_ONLY);
      return;
    }

    Player player = (Player) source;
    String protocolVersion = String.valueOf(player.getProtocolVersion().getProtocol());

    String serverName;
    int serversSize = this.servers.get(protocolVersion).size();
    int serversCounter = serversSize;
    if (serversSize > 1) {
      if (++serversCounter >= serversSize) {
        serversCounter = 0;
      }

      serverName = this.servers.get(protocolVersion).get(serversCounter);
    } else {
      serverName = this.servers.get(protocolVersion).get(0);
    }
    RegisteredServer toConnect = this.server.getServer(serverName).orElse(null);
    if (toConnect == null) {
      source.sendMessage(CommandMessages.SERVER_DOES_NOT_EXIST.args(Component.text(serverName)));
      return;
    }

    player.getCurrentServer().ifPresent(serverConnection -> {
      String servername = serverConnection.getServer().getServerInfo().getName();

      if (this.disabledServers.stream().anyMatch(servername::equals) && !player.hasPermission("velocitytools.command.hub.bypass." + servername)) {
        player.sendMessage(this.disabledServer);
      } else {
        player.createConnectionRequest(toConnect).connectWithIndication()
            .thenAccept(isSuccessful -> {
              if (isSuccessful && !this.youGotMoved.isEmpty()) {
                player.sendMessage(this.youGotMovedComponent);
              }
            });
      }
    });
  }

  @Override
  public boolean hasPermission(SimpleCommand.Invocation invocation) {
    return invocation.source().hasPermission("velocitytools.command.hub");
  }
}
