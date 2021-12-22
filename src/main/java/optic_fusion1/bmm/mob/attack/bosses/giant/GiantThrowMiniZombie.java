package optic_fusion1.bmm.mob.attack.bosses.giant;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses.BetterGiant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GiantThrowMiniZombie extends GiantAttack {

  private int scheduler;

  public GiantThrowMiniZombie(String name, BetterGiant giant) {
    super(name, giant);
  }

  @Override
  public void run(Player target) {
    final Player player = getMob().nearest();
    if (player == null) {
      return;
    }
    if (MobAI.settings.configuration.getBoolean("Giant.ThrowMiniZombie.Disable")) {
      return;
    }
    GameMode gameMode = player.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.ThrowMiniZombie.Speed"), 50.0D);

        @Override
        public void run() {
          if (getMob().getZombies().isEmpty()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE || !player.isOnline()
                  || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.ThrowMiniZombie.Speed"), 50.0D);
            Location location = getMob().getEntity().getLocation();
            location.setYaw(player.getLocation().getYaw() * -1.0F);
            getMob().getEntity().teleport(location);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            Location location = getMob().getEntity().getLocation();
            location.setYaw(player.getLocation().getYaw() * -1.0F);
            getMob().getEntity().teleport(location);
            ((Zombie) getMob().getZombies().get(0)).remove();
            getMob().getZombies().remove(0);
            Location inFront = getMob().inFront().add(0.0D, 7.0D, 0.0D);
            Zombie zombie = (Zombie) getMob().getEntity().getWorld().spawnEntity(inFront, EntityType.ZOMBIE);
            getMob().throwArm();
            double factor = getMob().distance2D(player.getLocation()) / 50.0D * -1.5D;
            Vector vector = inFront.toVector().subtract(player.getLocation().toVector()).normalize();
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 1));
            zombie.setVelocity(vector.multiply(factor).setY(1));
            zombie.setBaby(true);
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
