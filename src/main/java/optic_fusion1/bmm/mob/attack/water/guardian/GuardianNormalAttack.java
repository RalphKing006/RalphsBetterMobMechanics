package optic_fusion1.bmm.mob.attack.water.guardian;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftGuardian;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GuardianNormalAttack extends Attack {

  private int taskID;

  public GuardianNormalAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(),
                (float) MobAI.settings.configuration.getDouble("Guardian.NormalAttack.Speed"), 10.0D);

        @Override
        public void run() {
          if ((gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE || !target.isOnline())
                  || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskID);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(),
                    (float) MobAI.settings.configuration.getDouble("Guardian.NormalAttack.Speed"), 10.0D);
          } else {
            if (!target.isDead() && target.isOnline()) {
              ((CraftGuardian) getMob().getEntity()).getHandle().a(((CraftPlayer) target).getHandle());
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Guardian.NormalAttack.NextAttackDelay"));
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(taskID);
          }
        }
      }, 0L, 5L);
    }
  }

}
