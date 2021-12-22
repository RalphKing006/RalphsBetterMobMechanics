package optic_fusion1.bmm.mob.attack.overworld.cavespider;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSpider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CaveSpiderSkyAttack extends Attack {

  private int scheduler;

  public CaveSpiderSkyAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("CaveSpider.SkyAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    if (target.getGameMode().equals(GameMode.SURVIVAL) || target.getGameMode().equals(GameMode.ADVENTURE)) {
      getMob().setBusy(true);
      this.scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("CaveSpider.SkyAttack.Speed"), 3.0D);

        public void run() {
          if ((!target.getGameMode().equals(GameMode.SURVIVAL) && !target.getGameMode().equals(GameMode.ADVENTURE))
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(scheduler);
            return;
          }
          if (!this.b) {
            this.b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("CaveSpider.SkyAttack.Speed"), 3.0D);
          } else {
            getMob().track(getMob().getEntity().getLocation(), 0.0F, 0.0D);
            getMob().getEntity().teleport(getMob().getEntity().getLocation().clone().add(0.0D, 20.0D, 0.0D));
            getMob().getEntity().setNoDamageTicks(10);
            ((CraftSpider) getMob().getEntity()).getHandle().a(((CraftPlayer) target).getHandle());
            target.damage(MobAI.settings.configuration.getDouble("CaveSpider.SkyAttack.Damage"));
            getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("CaveSpider.SkyAttack.NextAttackDelay"));
            Bukkit.getScheduler().cancelTask(scheduler);
          }
        }
      }, 0L, 5L);
    }
  }

}
