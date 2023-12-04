package net.elytrium.velocitytools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.proxy.command.builtin.CommandMessages;
import net.elytrium.velocitytools.Settings;
import net.kyori.adventure.text.Component;

public class PlayerChooseInitialServerListener {
    private final ProxyServer proxy;

    public PlayerChooseInitialServerListener(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onInitialServer(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        String protocolVersion = String.valueOf(player.getProtocolVersion().getProtocol());

        String serverName;
        int serversSize = Settings.IMP.COMMANDS.HUB.SERVERS.get(protocolVersion).size();
        int serverCounter = serversSize;

        if (serversSize > 1) {
            if (++serverCounter >= serversSize) {
                serverCounter = 0;
            }

            serverName = Settings.IMP.COMMANDS.HUB.SERVERS.get(protocolVersion).get(serverCounter);
        } else {
            serverName = Settings.IMP.COMMANDS.HUB.SERVERS.get(protocolVersion).get(0);
        }

        RegisteredServer toConnect = this.proxy.getServer(serverName).orElse(null);
        if (toConnect == null) {
            return;
        }

        event.setInitialServer(toConnect);
    }
}
