package com.LucasRomier.BetterMobAI.Mobs.Entity.Nether;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.nether.blaze.BlazeHeatAttack;
import optic_fusion1.bmm.mob.attack.nether.blaze.BlazeNormalAttack;
import optic_fusion1.bmm.mob.attack.nether.blaze.BlazeSmokeAttack;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.LivingEntity;

public class BetterBlaze extends BetterMob {

  public BetterBlaze(Blaze blaze) {
    super("Blaze", (LivingEntity) blaze);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new BlazeHeatAttack("HeatAttack", this));
    addAttack(new BlazeNormalAttack("NormalAttack", this));
    addAttack(new BlazeSmokeAttack("SmokeAttack", this));
  }

}
