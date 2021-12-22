package optic_fusion1.bmm.mob.attack.bosses.giant;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses.BetterGiant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GiantLightningStriker extends GiantAttack {

  private int scheduler;

  public GiantLightningStriker(String name, BetterGiant giant) {
    super(name, giant);
  }

  @Override
  public void run(Player target) {
    final Player player = getMob().nearest();
    if (player == null) {
      return;
    }
    if (MobAI.settings.configuration.getBoolean("Giant.LightningStriker.Disable")) {
      return;
    }
    final double radius = MobAI.settings.configuration.getDouble("Giant.LightningStriker.Radius");
    if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.LightningStriker.Speed"), 30.0D);

        public void run() {
          if ((!player.getGameMode().equals(GameMode.SURVIVAL) && !player.getGameMode().equals(GameMode.ADVENTURE))
                  || !player.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.LightningStriker.Speed"), 30.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            for (Entity e : getMob().getEntity().getNearbyEntities(radius, radius, radius)) {
              if (e.getType().equals(EntityType.PLAYER)) {
                e.getWorld().strikeLightning(e.getLocation());
              }
            }
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
