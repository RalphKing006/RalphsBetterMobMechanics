package com.LucasRomier.BetterMobAI.Mobs.Entity.Nether;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.nether.ghast.GhastHighSpeedAttack;
import optic_fusion1.bmm.mob.attack.nether.ghast.GhastMultiAttack;
import optic_fusion1.bmm.mob.attack.nether.ghast.GhastNormalAttack;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;

public class BetterGhast extends BetterMob {

  public BetterGhast(Ghast ghast) {
    super("Ghast", (LivingEntity) ghast);
    registerAttack();
  }

  private void registerAttack() {
    addAttack(new GhastHighSpeedAttack("HighSpeedAttack", this));
    addAttack(new GhastMultiAttack("MultiAttack", this));
    addAttack(new GhastNormalAttack("NormalAttack", this));
  }

}
