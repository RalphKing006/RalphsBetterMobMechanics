package optic_fusion1.bmm.mob.attack.nether.witherskeleton;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WitherSkeletonSwordAttack extends Attack {

  private int scheduler;
  private int secondary;

  public WitherSkeletonSwordAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("WitherSkeleton.SwordAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("WitherSkeleton.SwordAttack.Speed"), 1.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("WitherSkeleton.SwordAttack.Speed"), 1.0D);
          } else {
            if (!target.isDead() && target.isOnline()) {
              throwSword(target);
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

  private void throwSword(Player target) {
    if (MobAI.settings.configuration.getBoolean("WitherSkeleton.ThrowSwordAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    Item item = getMob().getEntity().getWorld().dropItem(getMob().getEntity().getEyeLocation().clone().add(0.0D, 0.2D, 0.0D), new ItemStack(Material.GOLDEN_SWORD));
    Vector vector = target.getLocation().subtract(getMob().getEntity().getLocation()).toVector();
    item.setVelocity(vector.multiply(0.5D));
    this.secondary = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), () -> {
      if (item.isOnGround()) {
        item.remove();
        Bukkit.getScheduler().cancelTask(this.secondary);
        return;
      }
      for (Entity entity : item.getNearbyEntities(0.2D, 0.2D, 0.2D)) {
        if (entity.getType().equals(EntityType.PLAYER)) {
          item.remove();
          target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
          target.damage(MobAI.settings.configuration.getDouble("WitherSkeleton.ThrowSwordAttack.Damage"));
          getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("WitherSkeleton.ThrowSwordAttack.NextAttackDelay"));
          Bukkit.getScheduler().cancelTask(secondary);
          break;
        }
      }
    }, 0L, 1L);
  }

}
