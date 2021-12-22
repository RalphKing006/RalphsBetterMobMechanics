package optic_fusion1.bmm.mob.attack.nether.witherskeleton;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class WitherSkeletonSkullAttack extends Attack {

  private int scheduler;

  public WitherSkeletonSkullAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("WitherSkeleton.WitherSkullAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("WitherSkeleton.WitherSkullAttack.Speed"), 10.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("WitherSkeleton.WitherSkullAttack.Speed"), 10.0D);
          } else {
            if (!target.isDead() && target.isOnline()) {
              Vector vector = target.getLocation().subtract(getMob().getEntity().getLocation()).toVector();
              WitherSkull skull = (WitherSkull) getMob().getEntity().getWorld().spawnEntity(getMob().getEntity().getEyeLocation().clone().add(0.0D, 0.2D, 0.0D), EntityType.WITHER_SKULL);
              skull.setGlowing(true);
              skull.setDirection(vector);
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
