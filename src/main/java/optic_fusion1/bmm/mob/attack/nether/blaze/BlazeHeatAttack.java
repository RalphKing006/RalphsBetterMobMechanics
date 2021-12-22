package optic_fusion1.bmm.mob.attack.nether.blaze;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlazeHeatAttack extends Attack {

  private int scheduler;

  public BlazeHeatAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Blaze.HeatAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Blaze.HeatAttack.Speed"), 5.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Blaze.HeatAttack.Speed"), 5.0D);
          } else {
            if (!target.isDead() && target.isOnline()) {
              getMob().getEntity().setFireTicks(20 * MobAI.settings.configuration.getInt("Blaze.HeatAttack.EffectLength"));
              target.setFireTicks(20 * MobAI.settings.configuration.getInt("Blaze.HeatAttack.EffectLength"));
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Blaze.HeatAttack.NextAttackDelay"));
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
