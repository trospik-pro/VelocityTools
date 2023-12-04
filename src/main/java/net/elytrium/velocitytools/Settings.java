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

package net.elytrium.velocitytools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.elytrium.java.commons.config.YamlConfig;
import net.elytrium.java.commons.mc.serialization.Serializers;

public class Settings extends YamlConfig {

  @Ignore
  public static final Settings IMP = new Settings();

  @Final
  public String VERSION = BuildConfig.VERSION;

  @Comment({
      "Available serializers:",
      "LEGACY_AMPERSAND - \"&c&lExample &c&9Text\".",
      "LEGACY_SECTION - \"§c§lExample §c§9Text\".",
      "MINIMESSAGE - \"<bold><red>Example</red> <blue>Text</blue></bold>\". (https://webui.adventure.kyori.net/)",
      "GSON - \"[{\"text\":\"Example\",\"bold\":true,\"color\":\"red\"},{\"text\":\" \",\"bold\":true},{\"text\":\"Text\",\"bold\":true,\"color\":\"blue\"}]\". (https://minecraft.tools/en/json_text.php/)",
      "GSON_COLOR_DOWNSAMPLING - Same as GSON, but uses downsampling."
  })
  public Serializers SERIALIZER = Serializers.LEGACY_AMPERSAND;

  @Create
  public MAIN MAIN;
  @Create
  public COMMANDS COMMANDS;
  @Create
  public TOOLS TOOLS;

  public static class MAIN {

    @Comment({
        "VelocityTools will consume more RAM if this option is enabled, but compatibility with other plugins will be better",
        "Enable it if you have a plugin installed that bypasses compression (e.g. Geyser)"
    })
    public boolean SAVE_UNCOMPRESSED_PACKETS = true;
  }

  @Comment({
      "Don't use \\n, use {NL} for new line. Ampersand (&) color codes are supported too.\n",
      "",
      "Permissions:",
      "  │",
      "  └── Commands:",
      "      │",
      "      ├── /velocitytools reload",
      "      │   └── velocitytools.admin.reload",
      "      ├── /alert",
      "      │   └── velocitytools.command.alert",
      "      ├── /find",
      "      │   └── velocitytools.command.find",
      "      ├── /send",
      "      │   └── velocitytools.command.send",
      "      └── /hub",
      "          ├── velocitytools.command.hub",
      "          └── velocitytools.command.hub.bypass.<servername> (disabled-servers bypass permission)",
      ""
  })
  public static class COMMANDS {

    @Create
    public ALERT ALERT;
    @Create
    public FIND FIND;
    @Create
    public SEND SEND;
    @Create
    public HUB HUB;

    public static class ALERT {

      public boolean ENABLED = true;
      public String PREFIX = "&8[&4Alert&8] &r{0}";
      public String MESSAGE_NEEDED = "&cYou must supply the message.";
      public String EMPTY_PROXY = "&cNo one is connected to this proxy!";
    }

    public static class FIND {

      public boolean ENABLED = true;
      public String USERNAME_NEEDED = "&cYou must supply the username.";
      public String PLAYER_ONLINE_AT = "&6{0} &fis online at &6{1}";
      public String PLAYER_NOT_ONLINE = "&6{0} &fis not online.";
    }

    public static class SEND {

      public boolean ENABLED = true;
      public String CONSOLE = "CONSOLE";
      public String NOT_ENOUGH_ARGUMENTS = "&fNot enough arguments. Usage: &6/send <server|player|all|current> <target>";
      @Comment("Set to \"\" to disable.")
      public String YOU_GOT_SUMMONED = "&fSummoned to &6{0} &fby &6{1}";
      public String PLAYER_NOT_ONLINE = "&6{0} &fis not online.";
      public String CALLBACK = "&aAttempting to send {0} players to {1}";
      public String NOT_ON_SERVER = "&cError: You are not on server.";
    }

    public static class HUB {

      public boolean ENABLED = true;
      public Map<String, List<String>> SERVERS = Map.ofEntries(
          Map.entry("763", List.of("lb1")),
          Map.entry("764", List.of("lb2"))
      );
      @Comment("Set to \"\" to disable.")
      public String YOU_GOT_MOVED = "&aYou have been moved to a hub!";
      public String DISABLED_SERVER = "&cYou cannot use this command here.";
      public List<String> DISABLED_SERVERS = List.of("foo", "bar");
      public List<String> ALIASES = List.of("hub", "lobby");
    }
  }

  public static class TOOLS {

    @Comment("Hides the Legacy Ping message.")
    public boolean DISABLE_LEGACY_PING = true;
    @Comment("Hides the \"... provided invalid protocol ...\" message. Helps with some types of attacks. (https://media.discordapp.net/attachments/868930650537857024/921383075454259300/unknown.png)")
    public boolean DISABLE_INVALID_PROTOCOL = true;

    @Create
    public PROTOCOL_BLOCKER PROTOCOL_BLOCKER;
    @Create
    public BRAND_CHANGER BRAND_CHANGER;
    @Create
    public HOSTNAMES_MANAGER HOSTNAMES_MANAGER;

    public static class PROTOCOL_BLOCKER {

      public boolean BLOCK_JOIN = true;
      public boolean BLOCK_PING = false;
      @Comment("If true, all protocols except those listed below will be blocked.")
      public boolean WHITELIST = false;
      @Comment({
          "You can set either a protocol number here (e.g. '340' for 1.12.2) or a Minecraft version below (e.g. '1.12.2')",
          "You can find a list of protocols here: https://wiki.vg/Protocol_version_numbers",
      })
      public List<Integer> PROTOCOLS = List.of(9999, 9998);
      @Comment({
          "List of versions:",
          "1.7.2, 1.7.6, 1.8, 1.9, 1.9.1, 1.9.2, 1.9.4, 1.10, 1.11, 1.11.1, 1.12, 1.12.1, 1.12.2,",
          "1.13, 1.13.1, 1.13.2, 1.14, 1.14.1, 1.14.2, 1.14.3, 1.14.4, 1.15, 1.15.1, 1.15.2,",
          "1.16, 1.16.1, 1.16.2, 1.16.3, 1.16.4, 1.17, 1.17.1, 1.18, 1.18.2, 1.19, 1.19.1, 1.19.3, 1.19.4, 1.20, LATEST",
      })
      public List<String> VERSIONS = List.of("1.7.2");
      public String MINIMUM_VERSION = "1.7.2";
      public String MAXIMUM_VERSION = "LATEST";
      @Comment("For \"block-ping\" option.")
      public String BRAND = "Version is not supported!";
      @Comment("For \"block-ping\", set to \"\" to disable.")
      public String MOTD = "&cVersion is not supported!{NL}&ePlease, join with Minecraft 1.12.2 or newer.";
      @Comment("For \"block-joining\" option.")
      public String KICK_REASON = "&cYour version is unsupported!";
    }

    public static class BRAND_CHANGER {

      public boolean REWRITE_IN_PING = true;
      public boolean REWRITE_IN_GAME = true;
      public String PING_BRAND = "YourServer 1.12.2-1.20.1";
      @Comment("For ping.")
      public boolean SHOW_ALWAYS = false;
      @Comment("{0} - Original server brand (e.g. Paper).")
      public String IN_GAME_BRAND = "YourServer ({0})";
    }

    @Comment("Doesn't work with srv records.")
    public static class HOSTNAMES_MANAGER {

      public boolean BLOCK_JOIN = false;
      public boolean BLOCK_PING = false;
      @Comment("Connections IP logging.")
      public boolean DEBUG = false;
      @Comment("For \"debug\" option.")
      public boolean SHOW_BLOCKED_ONLY = false;
      @Comment("For \"block-joining\" option, set to \"\" to show the default reason.")
      public String KICK_REASON = "&cPlease, don't connect to the direct ip!{NL}Use example.com";
      public boolean WHITELIST = true;
      @Comment("IP Addresses starting with \"127.\" or equal to \"localhost\" will be blocked.")
      public boolean BLOCK_LOCAL_ADDRESSES = false;
      @Comment("DoMaIn.net will be similar to domain.net.")
      public boolean IGNORE_CASE = true;
      public List<String> HOSTNAMES = List.of("your-domain.net", "your-domain.com");
      @Comment("List of IP addresses that will bypass this check.")
      public List<String> IGNORED_IPS = List.of("79.555.*", "228.1337.*");
    }
  }
}
