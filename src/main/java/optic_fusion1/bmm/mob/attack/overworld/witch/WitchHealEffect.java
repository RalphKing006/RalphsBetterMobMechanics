package optic_fusion1.bmm.mob.attack.overworld.witch;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitchHealEffect extends Attack {

  private int taskId;

  public WitchHealEffect(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    getMob().getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 100, true));
    double health = getMob().getEntity().getHealth();
    taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
      double i = 0.0D;
      double r = 1.0D;
      int j = 0;

      @Override
      public void run() {
        if (getMob().getEntity().getHealth() < health || !target.isOnline() || target.isDead()) {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          getMob().runRandomAttack(target, 0);
          Bukkit.getScheduler().cancelTask(taskId);
        } else if (this.j < 20) {
          if (this.i > 3.0D) {
            this.i = 0.0D;
          }
          this.i += 0.19634954084936207D;
          double x = this.r * Math.cos(this.i);
          double y = 0.5D * this.i;
          double z = this.r * Math.sin(this.i);
          Location loc1 = getMob().getEntity().getLocation().clone().add(x, y, z);
          Location loc2 = getMob().getEntity().getLocation().clone().add(-x, y, -z);
          Location loc3 = getMob().getEntity().getLocation().clone().add(-x, y, z);
          Location loc4 = getMob().getEntity().getLocation().clone().add(x, y, -z);
          getMob().getEntity().getWorld().spawnParticle(Particle.HEART, loc1, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.HEART, loc2, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.HEART, loc3, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.HEART, loc4, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          this.j++;
        } else {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          getMob().getEntity().setHealth(getMob().getEntity().getMaxHealth());
          Bukkit.getScheduler().cancelTask(taskId);
        }
      }
    }, 0L, 2L);
  }

}
