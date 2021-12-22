package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.overworld.enderman.EndermanJumperAttack;
import optic_fusion1.bmm.mob.attack.overworld.enderman.EndermanNormalAttack;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;

public class BetterEnderman extends BetterMob {

  public BetterEnderman(Enderman enderman) {
    super("Enderman", (LivingEntity) enderman);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new EndermanJumperAttack("JumperAttack", this));
    addAttack(new EndermanNormalAttack("NormalAttack", this));
  }

}
