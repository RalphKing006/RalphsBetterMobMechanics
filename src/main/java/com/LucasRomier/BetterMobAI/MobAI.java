package com.LucasRomier.BetterMobAI;

import com.LucasRomier.BetterMobAI.API.Settings;
import com.LucasRomier.BetterMobAI.Events.EntityDamageByEntity;
import com.LucasRomier.BetterMobAI.Events.EntityTarget;
import com.LucasRomier.BetterMobAI.Events.ExplosionEvents;
import com.LucasRomier.BetterMobAI.Events.PlayerMove;
import com.LucasRomier.BetterMobAI.Events.PotionSplash;
import com.LucasRomier.BetterMobAI.Events.ProjectileEvents;
import com.LucasRomier.BetterMobAI.Events.SpawnEvents;
import com.LucasRomier.BetterMobAI.Events.TeleportEvents;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses.BetterGiant;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterSkeleton;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MobAI extends JavaPlugin {

  public static Settings settings;

  private static MobAI instance;

  public static MobAI getInstance() {
    return instance;
  }

  public void onEnable() {
    instance = this;
    settings = new Settings();
    PluginManager manager = Bukkit.getPluginManager();
    manager.registerEvents((Listener) new EntityTarget(), (Plugin) this);
    manager.registerEvents((Listener) new EntityDamageByEntity(), (Plugin) this);
    manager.registerEvents((Listener) new ExplosionEvents(), (Plugin) this);
    manager.registerEvents((Listener) new ProjectileEvents(), (Plugin) this);
    manager.registerEvents((Listener) new PotionSplash(), (Plugin) this);
    manager.registerEvents((Listener) new TeleportEvents(), (Plugin) this);
    manager.registerEvents((Listener) new SpawnEvents(), (Plugin) this);
    manager.registerEvents((Listener) new PlayerMove(), (Plugin) this);
    BetterSkeleton.lastArrow = new HashMap<>();
    BetterMob.busyEntities = new HashMap<>();
    BetterMob.protectedEntities = new HashMap<>();
    System.out.println("[" + getDescription().getName() + "] Current worlds: " + settings.configuration.getStringList("Worlds"));
    System.out.println("[" + getDescription().getName() + "] Current mobs: " + settings.configuration.getStringList("BetterMobs"));
    new MetricsLite((Plugin) this, 12647);
    new Updater(this, Integer.valueOf(81086), false);
    BetterGiant.init();
  }

  public void onDisable() {
    BetterGiant.stop();
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("BetterMobAI")) {
      if (args.length > 0) {
        if (args[0].equalsIgnoreCase("reload")) {
          if (!(sender instanceof org.bukkit.entity.Player) || sender.hasPermission("BetterMobAI.reload")) {
            sender.sendMessage(ChatColor.DARK_GREEN + "Better MobAI reloaded!");
            settings = new Settings();
          } else if (!sender.hasPermission("BetterMobAI.reload")) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have the permission!");
          }
        }
      } else {
        sender.sendMessage(ChatColor.DARK_RED + ">>Better MobAI<<");
        sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + getDescription().getVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Authors: " + getDescription().getAuthors());
        sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + getDescription().getWebsite());
      }
    }
    return false;
  }
}
