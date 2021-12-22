package optic_fusion1.bmm.mob.attack.bosses.giant;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses.BetterGiant;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class GiantThrowBoulderAttack extends GiantAttack {

  private int scheduler;
  private int boulder;

  public GiantThrowBoulderAttack(String name, BetterGiant giant) {
    super(name, giant);
  }

  @Override
  public void run(Player target) {
    final Player player = getMob().nearest();
    if (player == null) {
      return;
    }
    if (MobAI.settings.configuration.getBoolean("Giant.ThrowBoulder.Disable")) {
      return;
    }
    if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.ThrowBoulder.Speed"), 50.0D);

        public void run() {
          if ((!player.getGameMode().equals(GameMode.SURVIVAL) && !player.getGameMode().equals(GameMode.ADVENTURE))
                  || !player.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(player.getLocation(), (float) MobAI.settings.configuration.getDouble("Giant.ThrowBoulder.Speed"), 50.0D);
            Location location = getMob().getEntity().getLocation();
            location.setYaw(player.getLocation().getYaw() * -1.0F);
            getMob().getEntity().teleport(location);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            Location location = getMob().getEntity().getLocation();
            location.setYaw(player.getLocation().getYaw() * -1.0F);
            getMob().getEntity().teleport(location);
            Location inFront = getMob().inFront().add(0.0D, 7.0D, 0.0D);
            FallingBlock block = getMob().getEntity().getWorld().spawnFallingBlock(inFront, new MaterialData(Material.COBBLESTONE));
            double factor = getMob().distance2D(player.getLocation()) / 50.0D * -1.5D;
            Vector vector = inFront.toVector().subtract(target.getLocation().toVector()).normalize();
            block.setVelocity(vector.multiply(factor).setY(1));
            trackBoulder(block);
            getMob().throwArm();
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

  private void trackBoulder(FallingBlock paramFallingBlock) {
    this.boulder = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), () -> {
      if (paramFallingBlock.isOnGround()) {
        paramFallingBlock.remove();
        double radius = MobAI.settings.configuration.getDouble("Giant.ThrowBoulder.DamageRadius");
        for (Entity e : paramFallingBlock.getNearbyEntities(radius, radius, radius)) {
          if (e.getType().equals(EntityType.PLAYER)) {
            ((LivingEntity) e).damage(MobAI.settings.configuration.getDouble("Giant.ThrowBoulder.Damage"));
          }
        }
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY, 1.0F);
        paramFallingBlock.getWorld().spawnParticle(Particle.REDSTONE, paramFallingBlock.getLocation(), 0, 0.0D, 0.0D, 40.0D, dustOptions);
        paramFallingBlock.getWorld().playSound(paramFallingBlock.getLocation(), Sound.BLOCK_STONE_BREAK, 4.0F, 4.0F);
        paramFallingBlock.getLocation().getBlock().setType(Material.AIR);
        Bukkit.getScheduler().cancelTask(this.boulder);
      }
    }, 0L, 5L);
  }

}
