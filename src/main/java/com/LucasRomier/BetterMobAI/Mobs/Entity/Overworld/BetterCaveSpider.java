package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.overworld.cavespider.CaveSpiderNormalAttack;
import optic_fusion1.bmm.mob.attack.overworld.cavespider.CaveSpiderPoisonAttack;
import optic_fusion1.bmm.mob.attack.overworld.cavespider.CaveSpiderSkyAttack;
import optic_fusion1.bmm.mob.attack.overworld.cavespider.CaveSpiderWebAttack;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.LivingEntity;

public class BetterCaveSpider extends BetterMob {

  public BetterCaveSpider(CaveSpider spider) {
    super("CaveSpider", (LivingEntity) spider);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new CaveSpiderNormalAttack("NormalAttack", this));
    addAttack(new CaveSpiderPoisonAttack("PoisonAttack", this));
    addAttack(new CaveSpiderSkyAttack("SkyAttack", this));
    addAttack(new CaveSpiderWebAttack("WebAttack", this));
  }

}
