package optic_fusion1.bmm.mob.attack.overworld.spider;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class SpiderWebAttack extends Attack {

  private int taskId;

  public SpiderWebAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Spider.WebAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Spider.WebAttack.Speed"), 3.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Spider.WebAttack.Speed"), 3.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            Snowball snowball = (Snowball) getMob().getEntity().getWorld().spawnEntity(
                    getMob().getEntity().getLocation().clone().add(0.0D, 0.2D, 0.0D), EntityType.SNOWBALL);
            snowball.setVelocity(getMob().getEntity().getEyeLocation().clone().add(0.0D, 1.0D, 0.0D)
                    .subtract(target.getLocation().clone()).toVector().multiply(3));
            target.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().setType(Material.COBWEB);
            getMob().runRandomAttack(target,
                    MobAI.settings.configuration.getInt("Spider.WebAttack.NextAttackDelay"));
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 5L);
    }
  }

}
