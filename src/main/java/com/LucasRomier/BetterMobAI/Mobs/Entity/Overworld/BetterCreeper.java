package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.overworld.creeper.CreeperChargedCreeperAttack;
import optic_fusion1.bmm.mob.attack.overworld.creeper.CreeperChargedSuperCreeperAttack;
import optic_fusion1.bmm.mob.attack.overworld.creeper.CreeperImplosionAttack;
import optic_fusion1.bmm.mob.attack.overworld.creeper.CreeperNormalAttack;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;

public class BetterCreeper extends BetterMob {

  public BetterCreeper(Creeper creeper) {
    super("Creeper", (LivingEntity) creeper);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new CreeperNormalAttack("NormalAttack", this));
    addAttack(new CreeperImplosionAttack("ImplosionAttack", this));
    addAttack(new CreeperChargedSuperCreeperAttack("ChargedSuperCreeperAttack", this));
    addAttack(new CreeperChargedCreeperAttack("ChargedCreeperAttack", this));
  }

}
