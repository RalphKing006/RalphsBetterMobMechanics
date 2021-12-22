package optic_fusion1.bmm.mob.attack.overworld.zombie;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class ZombieMinionsAttack extends Attack {

  private int taskId;

  public ZombieMinionsAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Zombie.MinionsAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Zombie.MinionsAttack.Speed"), 1.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Zombie.MinionsAttack.Speed"), 1.0D);
          } else {
            if (target.isOnline() && !target.isDead()) {
              Random random = ThreadLocalRandom.current();
              for (int i = 0; i < random.nextInt(3) + 3; i++) {
                Location location = getMob().getEntity().getLocation().clone().add((random.nextInt(6) - 3), 0.0D,
                        (random.nextInt(6) - 3));
                Zombie zombie = (Zombie) getMob().getEntity().getWorld().spawnEntity(location, EntityType.ZOMBIE);
                ((CraftZombie) zombie).getHandle().setBaby(true);
                getMob().setBusy(false);
                getMob().getEntity().remove();
              }
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
