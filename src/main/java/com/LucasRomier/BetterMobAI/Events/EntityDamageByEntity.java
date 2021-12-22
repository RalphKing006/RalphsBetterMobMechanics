package com.LucasRomier.BetterMobAI.Events;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterSkeleton;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterWitch;
import java.util.Objects;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntity implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    Entity damaged = event.getEntity();
    boolean b = false;
    for (String s : MobAI.settings.configuration.getStringList("BetterMobs")) {
      if (s.equals(damager.getType().toString()) || damager.getType().equals(EntityType.PLAYER)) {
        b = true;
        break;
      }
      if (damager instanceof Projectile && ((Projectile) damager).getShooter() != null && ((Projectile) damager).getShooter() instanceof LivingEntity && ((LivingEntity) ((Projectile) damager).getShooter()).getType().equals(EntityType.PLAYER)) {
        b = true;
        break;
      }
    }
    boolean world = false;
    for (String s : MobAI.settings.configuration.getStringList("Worlds")) {
      if (s.equals(((World) Objects.<World>requireNonNull(damager.getLocation().getWorld())).getName())) {
        world = true;
        break;
      }
    }
    if (b && world) {
      if (damaged.getType().equals(EntityType.WITCH)) {
        BetterWitch betterWitch = new BetterWitch((Witch) damaged);
        betterWitch.protect();
      }
      if (damager.getType().equals(EntityType.ZOMBIE)) {
        if (!((CraftZombie) damager).getHandle().isBaby()) {
          event.setCancelled(true);
        }
      } else if (damager.getType().equals(EntityType.ARROW)) {
        ProjectileSource source = ((Projectile) damager).getShooter();
        if (source instanceof LivingEntity && damaged.getType().equals(EntityType.PLAYER)) {
          event.setDamage(event.getDamage() / 2.0D);
          if (BetterSkeleton.lastArrow.containsKey(damaged.getUniqueId().toString())) {
            if (((String) BetterSkeleton.lastArrow.get(damaged.getUniqueId().toString())).equalsIgnoreCase("Poisoned")) {
              ((LivingEntity) damaged).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
            } else if (((String) BetterSkeleton.lastArrow.get(damaged.getUniqueId().toString())).equalsIgnoreCase("Nailing")) {
              ((LivingEntity) damaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5));
            } else if (((String) BetterSkeleton.lastArrow.get(damaged.getUniqueId().toString())).equalsIgnoreCase("Burning")) {
              damaged.setFireTicks(100);
            }
          }
        }
      } else if (damager.getType().equals(EntityType.SPIDER) || damager.getType().equals(EntityType.CAVE_SPIDER)) {
        event.setCancelled(true);
      } else if (damager.getType().equals(EntityType.ENDERMAN)) {
        event.setCancelled(true);
      } else if (damager.getType().equals(EntityType.WITCH)) {
        event.setCancelled(true);
      } else if (damager.getType().equals(EntityType.SKELETON)) {
        if (((CraftSkeleton) damager).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
          event.setCancelled(true);
        }
      } else if (damager.getType().equals(EntityType.ENDER_DRAGON)) {
        event.setCancelled(true);
      }
    }
  }
}
