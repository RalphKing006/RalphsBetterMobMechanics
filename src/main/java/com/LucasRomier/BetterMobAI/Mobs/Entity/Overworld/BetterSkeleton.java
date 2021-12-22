package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import java.util.Map;
import optic_fusion1.bmm.mob.attack.Attack;
import optic_fusion1.bmm.mob.attack.overworld.skeleton.SkeletonArrowRainAttack;
import org.bukkit.Bukkit;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;

public class BetterSkeleton extends BetterMob {

  public static Map<String, String> lastArrow;

  public BetterSkeleton(Skeleton skeleton) {
    super("Skeleton", (LivingEntity) skeleton);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new SkeletonArrowRainAttack("ArrowRainAttack", this));
  }

  public void rideNearestSpider() {
    if (MobAI.settings.configuration.getBoolean("Skeleton.GetARide.Disable")) {
      return;
    }
    int radius = MobAI.settings.configuration.getInt("Skeleton.GetARide.Radius");
    for (Entity nearbyEntity : getEntity().getNearbyEntities(radius, radius, radius)) {
      if (nearbyEntity instanceof LivingEntity) {
        if (nearbyEntity.getType() == EntityType.SPIDER || nearbyEntity.getType() == EntityType.CAVE_SPIDER) {
          LivingEntity livingEntity = (LivingEntity) nearbyEntity;
          if (livingEntity.getPassengers().isEmpty()) {
            livingEntity.addPassenger(getEntity());
            if (nearbyEntity.getType() == EntityType.SPIDER) {
              BetterSpider spider = new BetterSpider((Spider) nearbyEntity);
              spider.setBusy(true);
              break;
            }
            BetterCaveSpider spider = new BetterCaveSpider((CaveSpider) nearbyEntity);
            spider.setBusy(true);
          }
        }
      }
    }
  }

  @Override
  public void runRandomAttack(Player target, int delay) {
    setBusy(true);
    Bukkit.getScheduler().scheduleSyncDelayedTask(MobAI.getInstance(), () -> {
      rideNearestSpider();
      getRandomAttack().run(target);
    }, 20 * delay);
  }

}
