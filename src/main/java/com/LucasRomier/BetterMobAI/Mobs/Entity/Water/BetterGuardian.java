package com.LucasRomier.BetterMobAI.Mobs.Entity.Water;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.water.guardian.GuardianInstantAttack;
import optic_fusion1.bmm.mob.attack.water.guardian.GuardianNailingVibesAttack;
import optic_fusion1.bmm.mob.attack.water.guardian.GuardianNormalAttack;
import optic_fusion1.bmm.mob.attack.water.guardian.GuardianSuffocationVibesAttack;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

public class BetterGuardian extends BetterMob {

  public BetterGuardian(Guardian guardian) {
    super("Guardian", (LivingEntity) guardian);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new GuardianNormalAttack("NormalAttack", this));
    addAttack(new GuardianInstantAttack("InstantAttack", this));
    addAttack(new GuardianSuffocationVibesAttack("SuffocationVibesAttack", this));
    addAttack(new GuardianNailingVibesAttack("NailingVibesAttack", this));
  }

}
