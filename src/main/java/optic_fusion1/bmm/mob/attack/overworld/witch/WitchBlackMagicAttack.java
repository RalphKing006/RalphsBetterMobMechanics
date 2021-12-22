package optic_fusion1.bmm.mob.attack.overworld.witch;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitchBlackMagicAttack extends Attack {

  private int taskId;
  private int secondTaskId;

  public WitchBlackMagicAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Witch.BlackMagicAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Witch.BlackMagicAttack.Speed"), 15.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Witch.BlackMagicAttack.Speed"), 15.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            particleBlackMagicAttack(target);
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 5L);
    }
  }

  private void particleBlackMagicAttack(Player target) {
    getMob().getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 100, true));
    final double health = getMob().getEntity().getHealth();
    secondTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
      double i = 0.0D;

      double r = 1.0D;

      int j = 0;

      @Override
      public void run() {
        if (getMob().getEntity().getHealth() < health || target.isDead() || !target.isOnline()) {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          getMob().runRandomAttack(target, 0);
          Bukkit.getScheduler().cancelTask(secondTaskId);
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
          getMob().getEntity().getWorld().spawnParticle(Particle.PORTAL, loc1, 0, 0.0D, 0.0D, 0.0D, 10.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.PORTAL, loc2, 0, 0.0D, 0.0D, 0.0D, 10.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.PORTAL, loc3, 0, 0.0D, 0.0D, 0.0D, 10.0D);
          getMob().getEntity().getWorld().spawnParticle(Particle.PORTAL, loc4, 0, 0.0D, 0.0D, 0.0D, 10.0D);
          this.j++;
        } else {
          getMob().getEntity().removePotionEffect(PotionEffectType.SLOW);
          target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * MobAI.settings.configuration.getInt("Witch.BlackMagicAttack.EffectLength"), 1));
          getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
          getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Witch.BlackMagicAttack.NextAttackDelay"));
          Bukkit.getScheduler().cancelTask(secondTaskId);
        }
      }
    }, 0L, 2L);
  }

}
