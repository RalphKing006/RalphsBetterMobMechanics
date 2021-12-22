package optic_fusion1.bmm.mob.attack;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import org.bukkit.entity.Player;

public abstract class Attack {

  private String name;
  private BetterMob mob;

  public Attack(String name, BetterMob mob) {
    this.name = name;
    this.mob = mob;
  }

  public abstract void run(Player target);

  public String getName() {
    return name;
  }

  public BetterMob getMob() {
    return mob;
  }

}
