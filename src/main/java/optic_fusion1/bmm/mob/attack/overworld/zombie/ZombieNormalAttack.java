package optic_fusion1.bmm.mob.attack.overworld.zombie;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ZombieNormalAttack extends Attack {

  private int taskId;

  public ZombieNormalAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(),
                (float) MobAI.settings.configuration.getDouble("Zombie.NormalAttack.Speed"), 1.0D);

        @Override
        public void run() {
          GameMode gameMode = target.getGameMode();
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(),
                    (float) MobAI.settings.configuration.getDouble("Zombie.NormalAttack.Speed"), 1.0D);
          } else {
            if (target.isOnline() & !target.isDead()) {
              target.damage(MobAI.settings.configuration.getDouble("Zombie.NormalAttack.Damage"));
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Zombie.NormalAttack.NextAttackDelay"));
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(taskId);
          }
        }
      }, 0L, 5L);
    }
  }

}
