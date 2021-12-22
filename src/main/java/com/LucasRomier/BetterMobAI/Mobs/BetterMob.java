package com.LucasRomier.BetterMobAI.Mobs;

import com.LucasRomier.BetterMobAI.API.Reflection;
import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Nether.BetterBlaze;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterZombie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftCreature;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;

public class BetterMob implements Reflection {

  private String name;
  public static Map<UUID, Boolean> busyEntities;
  public static Map<UUID, Boolean> protectedEntities;
  private final Map<String, Attack> attacks = new HashMap<>();
  protected LivingEntity entity;
  protected Random random;
  private int protect;
  private int taskId;

  public BetterMob(String name, LivingEntity entity) {
    this.name = name;
    this.entity = entity;
    this.random = new Random();
  }

  public static void setBusy(Entity entity, boolean busy) {
    busyEntities.put(entity.getUniqueId(), busy);
  }

  public static boolean isBusy(Entity entity) {
    if (busyEntities.containsKey(entity.getUniqueId())) {
      return (busyEntities.get(entity.getUniqueId()));
    }
    return false;
  }

  public boolean isBusy() {
    if (busyEntities.containsKey(this.entity.getUniqueId())) {
      return (busyEntities.get(this.entity.getUniqueId()));
    }
    return false;
  }

  public void setBusy(boolean busy) {
    busyEntities.put(this.entity.getUniqueId(), busy);
  }

  public void moveTo(Location location, float speed) {
    ((CraftCreature) this.entity).getHandle().getNavigation().a(location.getX(), location.getY() + 1.0D, location.getZ(), speed);
  }

  public boolean track(Location location, float speed, double distance) {
    if (!this.entity.isDead()) {
      moveTo(location, speed);
      double distance2D = Math.sqrt(Math.pow(location.getX() - this.entity.getLocation().getX(), 2.0D)
              + Math.pow(location.getZ() - this.entity.getLocation().getZ(), 2.0D));
      return (distance2D <= distance);
    }
    return true;
  }

  public void protect() {
    if (protectedEntities.containsKey(this.entity.getUniqueId())) {
      if (!(protectedEntities.get(this.entity.getUniqueId()))) {
        protectedEntities.put(this.entity.getUniqueId(), Boolean.TRUE);
        doProtection();
      }
    } else {
      protectedEntities.put(this.entity.getUniqueId(), Boolean.TRUE);
      doProtection();
    }
  }

  public void doProtection() {
    boolean b = false;
    for (String s : MobAI.settings.configuration.getStringList("BetterMobs")) {
      if (s.equals(this.entity.getType().toString())) {
        b = true;
        break;
      }
    }
    boolean world = false;
    for (String s : MobAI.settings.configuration.getStringList("Worlds")) {
      if (this.entity.getLocation().getWorld() != null && s.equals(this.entity.getLocation().getWorld().getName())) {
        world = true;
        break;
      }
    }
    if (world && b) {
      final List<BetterMob> entities = new ArrayList<>();
      final Map<UUID, double[]> offset = (Map) new HashMap<>();
      for (Entity e : this.entity.getNearbyEntities(10.0D, 10.0D, 10.0D)) {
        if (e instanceof LivingEntity) {
          b = false;
          for (String s : MobAI.settings.configuration.getStringList("BetterMobs")) {
            if (s.equals(this.entity.getType().toString())) {
              b = true;
              break;
            }
          }
          if (b) {
            if (e.getType() == EntityType.ZOMBIE) {
              BetterZombie betterZombie = new BetterZombie((Zombie) e);
              if (!betterZombie.isBusy() && entities.size() <= 15) {
                betterZombie.setBusy(true);
                entities.add(betterZombie);
                offset.put(e.getUniqueId(), null);
              }
            } else if (e.getType() == EntityType.BLAZE) {
              BetterBlaze betterBlaze = new BetterBlaze((Blaze) e);
              if (!betterBlaze.isBusy() && entities.size() <= 15) {
                betterBlaze.setBusy(true);
                entities.add(betterBlaze);
                offset.put(e.getUniqueId(), null);
              }
            }
          }
        }
      }
      this.protect = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        double p = 0.0D;

        double r = 5.0D;

        int k = 0;

        public void run() {
          if (entities.size() > 2) {
            for (int i = 0; i < entities.size(); i++) {
              double x1 = 3.0D * Math.cos(((i + 1 + this.k) * 2) * Math.PI / entities.size());
              double x3 = 3.0D * Math.sin(((i + 1 + this.k) * 2) * Math.PI / entities.size());
              offset.remove(((BetterMob) entities.get(i)).entity.getUniqueId());
              offset.put(((BetterMob) entities.get(i)).entity.getUniqueId(), new double[]{x1, x3});
            }
          }
          if (entities.size() > 2) {
            this.k++;
            if (this.k > entities.size()) {
              this.k = 0;
            }
          }
          if (!BetterMob.this.entity.isDead() && entities.size() > 2) {
            this.p += 0.3141592653589793D;
            for (double theta = 0.0D; theta < 6.283185307179586D; theta += 0.07853981633974483D) {
              double x = this.r * Math.cos(theta) * Math.sin(this.p);
              double y = this.r * Math.cos(this.p) + 1.5D;
              double z = this.r * Math.sin(theta) * Math.sin(this.p);
              BetterMob.this.entity.getWorld().spawnParticle(Particle.FLAME, BetterMob.this.entity
                      .getLocation().clone().add(x, y, z), 0, 0.0D, 0.0D, 0.0D, 1.0D);
            }
            Iterator<BetterMob> iterator = entities.iterator();
            while (iterator.hasNext()) {
              BetterMob betterMob = iterator.next();
              if (betterMob.entity.isDead()) {
                iterator.remove();
                offset.remove(betterMob.entity.getUniqueId());
                continue;
              }
              double x1 = ((double[]) offset.get(betterMob.entity.getUniqueId()))[0];
              double x3 = ((double[]) offset.get(betterMob.entity.getUniqueId()))[1];
              Location location = BetterMob.this.entity.getLocation().clone().add(x1, 0.0D, x3);
              if (location.getWorld() != null) {
                location = location.getWorld().getHighestBlockAt(location).getLocation();
                if (!betterMob.track(location, 2.0F, 8.0D)) {
                  betterMob.entity.teleport(location);
                }
                for (Entity e : betterMob.entity.getNearbyEntities(0.7D, 0.7D, 0.7D)) {
                  if (e.getType().equals(EntityType.PLAYER)) {
                    ((Player) e).damage(8.0D);
                  }
                }
              }
            }
          } else {
            BetterMob.protectedEntities.put(BetterMob.this.entity.getUniqueId(), Boolean.FALSE);
            Bukkit.getScheduler().cancelTask(BetterMob.this.protect);
          }
        }
      }, 0L, 4L);
    }
  }

  public LivingEntity getEntity() {
    return entity;
  }

  public void addAttack(Attack attack) {
    attacks.putIfAbsent(attack.getName(), attack);
  }

  public Attack getAttack(String name) {
    return attacks.get(name);
  }

  public Attack getRandomAttack() {
    return (Attack) attacks.values().toArray()[ThreadLocalRandom.current().nextInt(attacks.size())];
  }

  public void runRandomAttack(Player target, int delay) {
    setBusy(true);
    Bukkit.getScheduler().scheduleSyncDelayedTask(MobAI.getInstance(), () -> {
      getRandomAttack().run(target);
    }, 20 * delay);
  }

  public void trackAndKill(Player target) {
    if (isBusy()) {
      return;
    }
    setBusy(true);
    taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
      boolean b = track(target.getLocation(), (float) MobAI.settings.configuration.getDouble(name + ".TrackingSpeed"), 10.0D);

      @Override
      public void run() {
        GameMode gameMode = target.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                || !target.isOnline() || entity.isDead()) {
          setBusy(false);
          Bukkit.getScheduler().cancelTask(taskId);
          return;
        }
        if (!b) {
          b = track(target.getLocation(), (float) MobAI.settings.configuration.getDouble(name + ".TrackingSpeed"), 10.0D);
        } else {
          track(entity.getLocation(), 0.0F, 0.0D);
          setBusy(false);
          runRandomAttack(target, 0);
          Bukkit.getScheduler().cancelTask(taskId);
        }
      }
    }, 0L, 5L);
  }

}
