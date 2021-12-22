package optic_fusion1.bmm.mob.attack.overworld.skeleton;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.entity.Player;

public class SkeletonNailingArrowAttack extends Attack {

  public SkeletonNailingArrowAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    /*
        if (MobAI.settings.configuration.getBoolean("Skeleton.NailingArrowAttack.Disable")) {
      normalAttack(player);
      return;
    }
    if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
      setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = BetterSkeleton.this.track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Skeleton.NailingArrowAttack.Speed"), 10.0D);

        public void run() {
          if ((!player.getGameMode().equals(GameMode.SURVIVAL) && !player.getGameMode().equals(GameMode.ADVENTURE))
                  || !player.isOnline() || BetterSkeleton.this.entity.isDead()) {
            BetterSkeleton.this.setBusy(false);
            Bukkit.getScheduler().cancelTask(BetterSkeleton.this.scheduler);
            return;
          }
          if (!this.b) {
            this.b = BetterSkeleton.this.track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Skeleton.NailingArrowAttack.Speed"), 10.0D);
          } else {
            BetterSkeleton.this.track(BetterSkeleton.this.entity.getLocation(), 0.0F, 0.0D);
            Arrow arrow = (Arrow) BetterSkeleton.this.entity.getWorld().spawnEntity(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.0D, 1.0D, 0.0D), EntityType.ARROW);
            Vector vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.0D, 1.0D, 0.0D)).toVector();
            BetterSkeleton.lastArrow.put(player.getUniqueId().toString(), "Nailing");
            arrow.setShooter((ProjectileSource) BetterSkeleton.this.entity);
            arrow.setVelocity(vector.multiply(1));
            BetterSkeleton.this.randomAttack(player, MobAI.settings.configuration.getInt("Skeleton.NailingArrowAttack.NextAttackDelay"));
            Bukkit.getScheduler().cancelTask(BetterSkeleton.this.scheduler);
          }
        }
      },
              0L, 5L);
    }
     */
  }

}
