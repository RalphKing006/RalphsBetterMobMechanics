package optic_fusion1.bmm.mob.attack.overworld.witch;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterWitch;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitchNormalAttack extends Attack {

  private int taskId;
  private int secondTaskId;

  public WitchNormalAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Witch.NormalAttack.Speed"), 15.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Witch.NormalAttack.Speed"), 15.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            particleNormalAttack(target);
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 5L);
    }
  }

  private void particleNormalAttack(Player target) {
    getMob().getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 100, true));
    double health = getMob().getEntity().getHealth();
    secondTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
      double i = 0.0D;
      double r = 1.0D;
      int j = 0;

      @Override
      public void run() {
        if (getMob().getEntity().getHealth() < health || !target.isOnline() || target.isDead()) {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          getMob().runRandomAttack(target, 0);
          Bukkit.getScheduler().cancelTask(secondTaskId);
        } else if (j < 20) {
          if (i > 3.0D) {
            i = 0.0D;
          }
          this.i += 0.19634954084936207D;
          double x = this.r * Math.cos(this.i);
          double y = 0.5D * this.i;
          double z = this.r * Math.sin(this.i);
          Location loc1 = getMob().getEntity().getLocation().clone().add(x, y, z);
          Location loc2 = getMob().getEntity().getLocation().clone().add(-x, y, -z);
          Location loc3 = getMob().getEntity().getLocation().clone().add(-x, y, z);
          Location loc4 = getMob().getEntity().getLocation().clone().add(x, y, -z);
          getMob().getEntity().getWorld().spawnParticle(Particle.MOB_APPEARANCE, loc1, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.MOB_APPEARANCE, loc2, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.MOB_APPEARANCE, loc3, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.MOB_APPEARANCE, loc4, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          this.j++;
        } else {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          target.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
          getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
          getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Witch.NormalAttack.NextAttackDelay"));
          Bukkit.getScheduler().cancelTask(secondTaskId);
        }
      }
    }, 0L, 2L);
  }

}
