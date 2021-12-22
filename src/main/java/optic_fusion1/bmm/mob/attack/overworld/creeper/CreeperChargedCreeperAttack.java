package optic_fusion1.bmm.mob.attack.overworld.creeper;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterCreeper;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CreeperChargedCreeperAttack extends Attack {

  private int scheduler;

  public CreeperChargedCreeperAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Creeper.ChargedCreeperAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      getMob().getEntity().getWorld().strikeLightning(getMob().getEntity().getLocation());
      ((Creeper) getMob().getEntity()).setPowered(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Creeper.ChargedCreeperAttack.Speed"), 4.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            ((Creeper) getMob().getEntity()).setPowered(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Creeper.ChargedCreeperAttack.Speed"), 4.0D);
          } else {
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
