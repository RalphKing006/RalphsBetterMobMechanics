package optic_fusion1.bmm.mob.attack.water.guardian;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuardianNailingVibesAttack extends Attack {

  private int taskID;

  public GuardianNailingVibesAttack(String name, BetterMob mob) {
    super(name, mob);
  }

  @Override
  public void run(Player target) {
    if (MobAI.settings.configuration.getBoolean("Guardian.NailingVibesAttack.Disable")) {
      getMob().getAttack("NormalAttack").run(target);
      return;
    }
    GameMode gameMode = target.getGameMode();
    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
      getMob().setBusy(true);
      taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobAI.getInstance(), new Runnable() {
        boolean b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Guardian.NailingVibesAttack.Speed"), 10.0D);

        @Override
        public void run() {
          if (gameMode != gameMode.SURVIVAL && gameMode != gameMode.ADVENTURE
                  || !target.isOnline() || getMob().getEntity().isDead()) {
            getMob().setBusy(false);
            Bukkit.getScheduler().cancelTask(taskID);
            return;
          }
          if (!b) {
            b = getMob().track(target.getLocation(), (float) MobAI.settings.configuration.getDouble("Guardian.NailingVibesAttack.Speed"), 10.0D);
          } else {
            if (target.isOnline() && !target.isDead()) {
              target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * MobAI.settings.configuration.getInt("Guardian.NailingVibesAttack.EffectLength"), 4));
              getMob().runRandomAttack(target, MobAI.settings.configuration.getInt("Guardian.NailingVibesAttack.NextAttackDelay"));
            } else {
              getMob().setBusy(false);
            }
            Bukkit.getScheduler().cancelTask(taskID);
          }
        }
      }, 0L, 5L);
    }
  }

}
