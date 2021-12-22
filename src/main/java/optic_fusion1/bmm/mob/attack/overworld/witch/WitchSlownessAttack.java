package optic_fusion1.bmm.mob.attack.overworld.witch;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitchSlownessAttack extends Attack {

  private int taskId;
  private int secondTaskId;

  public WitchSlownessAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Witch.SlownessAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("With.SlownessAttack.Speed"), 15.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("With.SlownessAttack.Speed"), 15.0D);
          } else {
            getMob().track(target.getLocation(), 0.0F, 0.0D);
            particleSlownessAttack(target);
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 5L);
    }
  }

  private void particleSlownessAttack(Player target) {
    getMob().getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 100, true));
    double health = getMob().getEntity().getHealth();
    secondTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
      double i = 0.39269908169872414D;

      int j = 0;

      @Override
      public void run() {
        if (getMob().getEntity().getHealth() < health || target.isDead() || !target.isOnline()) {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          getMob().runRandomAttack(target, 0);
        } else if (this.j < 20) {
          this.i += 0.3141592653589793D;
          for (double alpha = 0.0D; alpha <= 6.283185307179586D; alpha += 0.39269908169872414D) {
            double x = this.i * Math.cos(alpha);
            double y = 2.0D * Math.exp(-0.1D * this.i) * Math.sin(this.i) + 1.5D;
            double z = this.i * Math.sin(alpha);
            Location loc1 = getMob().getEntity().getLocation().clone().add(x, y, z);
            getMob().getEntity().getWorld().spawnParticle(Particle.SNOW_SHOVEL, loc1, 0, 0.0D, 0.0D, 0.0D, 1.0D);
          }
          this.j++;
        } else {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * MobAI.settings.configuration.getInt("Witch.SlownessAttack.EffectLength"), 5));
          getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
          getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Witch.SlownessAttack.NextAttackDelay"));
          Bukkit.getScheduler().cancelTask(secondTaskId);
        }
      }
    }, 0L, 2L);
  }

}
