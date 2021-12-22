package optic_fusion1.bmm.mob.attack.overworld.zombie;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ZombieBloodRushAttack extends Attack {

  private int taskId;

  public ZombieBloodRushAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Zombie.BloodRushAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("zombie.BloodRushAttack.Speed"), 1.0D);
        double i = 0.0D;

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("zombie.BloodRushAttack.Speed"), 1.0D);
            this.i += 0.19634954084936207D;
            for (double j = 0.0D; j <= 6.283185307179586D; j += 0.19634954084936207D) {
              for (int k = 0; k <= 1; k++) {
                double x = 0.3D * (6.283185307179586D - j) * 0.5D * Math.cos(j + this.i + k * Math.PI);
                double y = 0.5D * j;
                double z = 0.3D * (6.283185307179586D - j) * 0.5D * Math.sin(j + this.i + k * Math.PI);
                Location location = getMob().getEntity().getLocation().clone().add(x, y, z);
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);
                location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, 0.0D, 0.0D, 1.0D, dustOptions);
              }
            }
          } else {
            if (!target.isDead() && target.isOnline()) {
              target.damage(MobAI.settings.configuration.getDouble("Zombie.BloodRushAttack.Damage"));
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Zombie.BloodRushAttack.NextAttackDelay"));
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 2L);
    }
  }

}
