package optic_fusion1.bmm.mob.attack.overworld.zombie;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieVampireAttack extends Attack {

  private int taskId;

  public ZombieVampireAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Zombie.VampireAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      List<Bat> bats = new ArrayList<>();
      LivingEntity entity = getMob().getEntity();
      for (int i = 0; i < 5; i++) {
        Location location = entity.getLocation().clone().add((ThreadLocalRandom.current().nextInt(6) - 3), 0.0D, (ThreadLocalRandom.current().nextInt(6) - 3));
        Bat bat = (Bat) entity.getWorld().spawnEntity(location, EntityType.BAT);
        bat.setNoDamageTicks(400);
        bats.add(bat);
      }
      entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, true));
      taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Zombie.VampireAttack.Speed"), 1.0D);

        @Override
        public void run() {
          if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE
                  || !target.isOnline() || entity.isDead()) {
            getMob().setBusy(false);
            entity.removePotionEffect(PotionEffectType.INVISIBILITY);
            Bukkit.getScheduler().cancelTask(taskId);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Zombie.VampireAttack.Speed"), 1.0D);
          } else {
            entity.removePotionEffect(PotionEffectType.INVISIBILITY);
            for (int i = 0; i < 5; i++) {
              ((Bat) bats.get(i)).damage(100000.0D);
              Location location = entity.getLocation().clone().add((ThreadLocalRandom.current().nextInt(6) - 3), 0.0D, (ThreadLocalRandom.current().nextInt(6) - 3));
              Bat bat = (Bat) entity.getWorld().spawnEntity(location, EntityType.BAT);
              bat.setNoDamageTicks(400);
            }
            if (target.isOnline() && !target.isDead()) {
              target.damage(MobAI.settings.configuration.getDouble("Zombie.VampireAttack.Damage"));
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Zombie.VampireAttack.NextAttackDelay"));
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
