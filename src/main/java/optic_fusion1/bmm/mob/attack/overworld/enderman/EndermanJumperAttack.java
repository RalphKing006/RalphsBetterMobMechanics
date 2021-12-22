package optic_fusion1.bmm.mob.attack.overworld.enderman;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld.BetterEnderman;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EndermanJumperAttack extends Attack {

  private int scheduler;
  private int secondary;

  public EndermanJumperAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Creeper.JumperAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Enderman.JumperAttack.Speed"), 15.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Enderman.JumperAttack.Speed"), 15.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 1.0F, 1.0D);
            jumpTowardsPlayer(target);
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      },
              0L, 5L);
    }
  }

  private void jumpTowardsPlayer(final Player target) {
    final Location location = getMob().getEntity().getLocation();
    this.secondary = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
      final double x = getMob().getEntity().getLocation().getX();

      final double z = getMob().getEntity().getLocation().getZ();

      double i = 0.0D;

      public void run() {
        if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                || !target.isOnline() || getMob().getEntity().isDead()) {
          getMob().setBusy(false);
          Bukkit.getScheduler().cancelTask(scheduler);
          return;
        }
        double xPortion = target.getLocation().getX() - getMob().getEntity().getLocation().getX() / this.i;
        double zPortion = target.getLocation().getZ() - getMob().getEntity().getLocation().getZ() / this.i;
        if (this.i < 6.0D) {
          try {
            getMob().getEntity().teleport(new Location(target.getWorld(), this.x + xPortion, target.getLocation().getY(), this.z + zPortion));
            getMob().getEntity().getWorld().playSound(getMob().getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
          } catch (IllegalArgumentException illegalArgumentException) {
          }
          this.i++;
        } else {
          getMob().getEntity().teleport(target.getLocation().clone().add(0.5D, 0.0D, 0.5D));
          target.damage(MobAI.settings.configuration.getDouble("Enderman.JumperAttack.Damage"));
          target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * MobAI.settings.configuration.getInt("Enderman.JumperAttack.Damage"), 1));
          getMob().getEntity().teleport(location);
          getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Enderman.JumperAttack.NextAttackDelay"));
          Bukkit.getScheduler().cancelTask(secondary);
        }
      }
    }, 0L, 5L);
  }

}
