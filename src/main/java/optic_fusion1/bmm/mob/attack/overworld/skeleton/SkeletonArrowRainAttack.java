package optic_fusion1.bmm.mob.attack.overworld.skeleton;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.entity.Player;

public class SkeletonArrowRainAttack extends Attack {

  public SkeletonArrowRainAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    /*
        if (MobAI.settings.configuration.getBoolean("Skeleton.ArrowRain.Disable")) {
      normalAttack(player);
      return;
    }
    if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
      setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = BetterSkeleton.this.track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Skeleton.ArrowRain.Speed"), 10.0D);

        public void run() {
          if ((!player.getGameMode().equals(GameMode.SURVIVAL) && !player.getGameMode().equals(GameMode.ADVENTURE))
                  || !player.isOnline() || BetterSkeleton.this.entity.isDead()) {
            BetterSkeleton.this.setBusy(false);
            Bukkit.getScheduler().cancelTask(BetterSkeleton.this.scheduler);
            return;
          }
          if (!this.b) {
            this.b = BetterSkeleton.this.track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Skeleton.ArrowRain.Speed"), 10.0D);
          } else {
            BetterSkeleton.this.track(BetterSkeleton.this.entity.getLocation(), 0.0F, 0.0D);
            BetterSkeleton.lastArrow.put(player.getUniqueId().toString(), "None");
            for (int i = 0; i < MobAI.settings.configuration.getInt("Skeleton.ArrowRain.Arrows"); i++) {
              Vector vector;
              Arrow arrow = (Arrow) BetterSkeleton.this.entity.getWorld().spawnEntity(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.0D, 1.0D, 0.0D), EntityType.ARROW);
              switch (i) {
                case 0:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.3D, 1.0D, 0.0D)).toVector();
                  break;
                case 1:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.3D, 1.0D, 0.3D)).toVector();
                  break;
                case 2:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.0D, 1.0D, 0.0D)).toVector();
                  break;
                case 3:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(-0.3D, 1.0D, 0.0D)).toVector();
                  break;
                case 4:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(-0.3D, 1.0D, -0.3D)).toVector();
                  break;
                case 5:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(-0.3D, 1.0D, 0.3D)).toVector();
                  break;
                case 6:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.3D, 1.0D, -0.3D)).toVector();
                  break;
                default:
                  vector = player.getLocation().subtract(BetterSkeleton.this.entity.getEyeLocation().clone().add(0.0D, 1.0D, 0.0D)).toVector();
                  break;
              }
              arrow.setShooter((ProjectileSource) BetterSkeleton.this.entity);
              arrow.setVelocity(vector.multiply(1));
            }
            BetterSkeleton.this.randomAttack(player, MobAI.settings.configuration.getInt("Skeleton.ArrowRain.NextAttackDelay"));
            Bukkit.getScheduler().cancelTask(BetterSkeleton.this.scheduler);
          }
        }
      },
              0L, 5L);
    }
     */
  }

}
