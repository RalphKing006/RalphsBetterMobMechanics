package optic_fusion1.bmm.mob.attack.overworld.creeper;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterCreeper;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CreeperChargedSuperCreeperAttack extends Attack {

  private int scheduler;

  public CreeperChargedSuperCreeperAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      getMob().getEntity().getWorld().strikeLightning(getMob().getEntity().getLocation());
      ((Creeper) getMob().getEntity()).setPowered(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Creeper.ChargedSuperCreeperAttack.Speed"), 4.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            ((Creeper) getMob().getEntity()).setPowered(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Creeper.ChargedSuperCreeperAttack.Speed"), 4.0D);
          } else {
            for (int i = 0; i < ThreadLocalRandom.current().nextInt(3) + 3; i++) {
              Location location = getMob().getEntity().getLocation().clone().add((ThreadLocalRandom.current().nextInt(6) - 3), 0.0D, ((new Random()).nextInt(6) - 3));
              Creeper creeper = (Creeper) getMob().getEntity().getWorld().spawnEntity(location, EntityType.CREEPER);
              new BetterCreeper(creeper).getAttack("NormalAttack").run(target);
            }
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
