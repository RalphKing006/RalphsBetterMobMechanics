package com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses;

import com.LucasRomier.BetterMobAI.API.Reflection;
import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.player.EntityHuman;
import optic_fusion1.bmm.mob.attack.bosses.giant.GiantAttack;
import optic_fusion1.bmm.mob.attack.bosses.giant.GiantEarthquakeAttack;
import optic_fusion1.bmm.mob.attack.bosses.giant.GiantLightningStriker;
import optic_fusion1.bmm.mob.attack.bosses.giant.GiantThrowBoulderAttack;
import optic_fusion1.bmm.mob.attack.bosses.giant.GiantThrowMiniZombie;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftGiant;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class BetterGiant extends BetterMob {

  private static List<BetterGiant> betterGiants = new ArrayList<>();
  private List<Zombie> zombies = new ArrayList<>();
  private GiantAttack nextAttack;
  private EntityGiantZombie entityGiantZombie;
  private int cyclesToWait = 0;
  private int protect;
  private int main;

  public BetterGiant(Giant giant) {
    super("Giant", (LivingEntity) giant);
    entityGiantZombie = ((CraftGiant) entity).getHandle();
    clearGoals();
    defaultZombieGoals();
    registerAttacks();
    mainLoop();
    doProtection();
    betterGiants.add(this);
  }

  private void registerAttacks() {
    addAttack(new GiantEarthquakeAttack("EarthquakeAttack", this));
    addAttack(new GiantLightningStriker("LightningStrikerAttack", this));
    addAttack(new GiantThrowBoulderAttack("ThrowBoulderAttack", this));
    addAttack(new GiantThrowMiniZombie("ThrowMiniZombie", this));
  }

  public static void init() {
    for (World world : Bukkit.getWorlds()) {
      for (Entity entity : world.getEntities()) {
        if (entity.getType().equals(EntityType.GIANT)) {
          new BetterGiant((Giant) entity);
        }
      }
    }
  }

  public static void stop() {
    for (BetterGiant betterGiant : betterGiants) {
      for (Iterator<Zombie> iterator = betterGiant.zombies.iterator(); iterator.hasNext();) {
        ((Zombie) iterator.next()).remove();
        iterator.remove();
      }
    }
  }

  private void mainLoop() {
    main = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
      public void run() {
        if (entity.isDead()) {
          Iterator<Zombie> iterator = zombies.iterator();
          while (iterator.hasNext()) {
            ((Zombie) iterator.next()).remove();
            iterator.remove();
            Zombie zombie = (Zombie) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
            zombie.setBaby(true);
          }
          Bukkit.getScheduler().cancelTask(main);
        }
        if (nearby().size() > 0) {
          if (nextAttack != null) {
            getRandomAttack().run(null);
            cyclesToWait = MobAI.settings.configuration.getInt("Giant." + nextAttack.getName() + ".NextAttackDelay");
            nextAttack = null;
          } else if (!isBusy()) {
            if (cyclesToWait <= 0) {
              randomAttack();
            } else {
              for (Entity e : entity.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                if (e.getType().equals(EntityType.PLAYER)) {
                  Vector vector = entity.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                  e.setVelocity(vector.multiply(-1).setY(1));
                }
              }
              cyclesToWait--;
            }
          }
        }
      }
    }, 0L, 20L);
  }

  private void randomAttack() {
    nextAttack = (GiantAttack) getRandomAttack();
    while (zombies.isEmpty() && nextAttack instanceof GiantThrowMiniZombie) {
      nextAttack = (GiantAttack) getRandomAttack();
    }
  }

  public double distance2D(Location location) {
    return Math.sqrt(Math.pow(entity.getLocation().getX() - location.getX(), 2.0D) + Math.pow(entity.getLocation().getZ() - location.getZ(), 2.0D));
  }

  public Location inFront() {
    Vector vec = entity.getLocation().toVector();
    Vector dir = entity.getLocation().getDirection();
    vec = vec.add(dir.multiply(3.0D));
    return vec.toLocation(entity.getWorld());
  }

  public void throwArm() {
    PacketPlayOutAnimation packetPlayOutAnimation = new PacketPlayOutAnimation(entityGiantZombie, 0);
    sendPacket((Packet) packetPlayOutAnimation);
  }

  public List<Player> nearby() {
    List<Player> list = new ArrayList<>();
    for (Entity nearbyEntity : entity.getNearbyEntities(100.0D, 100.0D, 100.0D)) {
      if (nearbyEntity.getType().equals(EntityType.PLAYER)) {
        Player target = (Player) nearbyEntity;
        if (target.getGameMode().equals(GameMode.ADVENTURE) || target.getGameMode().equals(GameMode.SURVIVAL)) {
          list.add(target);
        }
      }
    }
    return list;
  }

  public Player nearest() {
    double distance = Double.MAX_VALUE;
    Player target = null;
    for (Player p : nearby()) {
      double dist = entity.getLocation().distance(p.getLocation());
      if (dist < distance) {
        distance = dist;
        target = p;
      }
    }
    return target;
  }

  private void defaultZombieGoals() {
    entityGiantZombie.bP.a(7, (PathfinderGoal) new PathfinderGoalRandomStrollLand((EntityCreature) entityGiantZombie, 0.5D));
    entityGiantZombie.bP.a(8, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) entityGiantZombie, EntityHuman.class, 8.0F));
    entityGiantZombie.bP.a(8, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) entityGiantZombie));
  }

  private void clearGoals() {
    HashSet<?> goalD = (HashSet) Reflection.getClassFieldObject("d", PathfinderGoalSelector.class, entityGiantZombie.bP);
    if (goalD != null) {
      goalD.clear();
    }
    HashSet<?> targetD = (HashSet) Reflection.getClassFieldObject("d", PathfinderGoalSelector.class, entityGiantZombie.bQ);
    if (targetD != null) {
      targetD.clear();
    }
  }

  private void clearGoals(Entity entity) {
    EntityMonster e = (EntityMonster) ((CraftEntity) entity).getHandle();
    HashSet<?> goalD = (HashSet) Reflection.getClassFieldObject("d", PathfinderGoalSelector.class, e.bP);
    assert goalD != null;
    goalD.clear();
    HashSet<?> targetD = (HashSet) Reflection.getClassFieldObject("d", PathfinderGoalSelector.class, e.bQ);
    assert targetD != null;
    targetD.clear();
  }

  @Override
  public void doProtection() {
    for (int i = 0; i < 15.0D; i++) {
      double x1 = entity.getLocation().getX() + 5.0D * Math.cos(Math.toRadians((36 * i)));
      double x3 = entity.getLocation().getZ() + 5.0D * Math.sin(Math.toRadians((36 * i)));
      Location location = new Location(entity.getWorld(), x1, entity.getLocation().getY() + 8.0D, x3);
      Zombie zombie = (Zombie) entity.getWorld().spawnEntity(location, EntityType.ZOMBIE);
      clearGoals((Entity) zombie);
      zombie.setVelocity(new Vector(0, 0, 0));
      zombie.setAI(false);
      zombie.setBaby(true);
      ((CraftLivingEntity) zombie).setMaxHealth(MobAI.settings.configuration.getDouble("Giant.MiniZombieHealth"));
      zombie.setHealth(MobAI.settings.configuration.getDouble("Giant.MiniZombieHealth"));
      zombies.add(zombie);
    }
    protect = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
      double angle = 0.0D;

      public void run() {
        if (angle > 360.0D) {
          angle -= 360.0D;
        }
        if (!entity.isDead() && zombies.size() > 2) {
          double pos = 0.0D;
          double angleSize = 360.0D / zombies.size();
          for (Iterator<Zombie> iterator = zombies.iterator(); iterator.hasNext();) {
            Zombie zombie = iterator.next();
            if (zombie.isDead()) {
              iterator.remove();
            } else {
              double x1 = entity.getLocation().getX() + 5.0D * Math.cos(Math.toRadians(angleSize * pos + angle));
              double x3 = entity.getLocation().getZ() + 5.0D * Math.sin(Math.toRadians(angleSize * pos + angle));
              Location location = new Location(entity.getWorld(), x1, entity.getLocation().getY() + 8.0D, x3);
              zombie.teleport(location);
            }
            pos++;
          }
        } else {
          for (Iterator<Zombie> iterator = zombies.iterator(); iterator.hasNext();) {
            ((Zombie) iterator.next()).remove();
            iterator.remove();
            Zombie zombie = (Zombie) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
            zombie.setBaby(true);
          }
          Bukkit.getScheduler().cancelTask(protect);
        }
        angle += 4.0D;
      }
    }, 0L, 2L);
  }

  public void addZombie(Zombie zombie) {
    zombies.add(zombie);
  }

  public List<Zombie> getZombies() {
    return zombies;
  }
}
