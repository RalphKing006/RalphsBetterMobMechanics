package optic_fusion1.bmm.mob.attack.bosses.giant;

import com.LucasRomier.BetterMobAI.Mobs.Entity.Bosses.BetterGiant;
import optic_fusion1.bmm.mob.attack.Attack;

public abstract class GiantAttack extends Attack {

  public GiantAttack(String name, BetterGiant giant) {
    super(name, giant);
  }

  @Override
  public BetterGiant getMob() {
    return (BetterGiant) super.getMob();
  }

  
  
}
