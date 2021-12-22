package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.overworld.spider.SpiderNormalAttack;
import optic_fusion1.bmm.mob.attack.overworld.spider.SpiderPoisonAttack;
import optic_fusion1.bmm.mob.attack.overworld.spider.SpiderSkyAttack;
import optic_fusion1.bmm.mob.attack.overworld.spider.SpiderWebAttack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;

public class BetterSpider extends BetterMob {

  public BetterSpider(Spider spider) {
    super("Spider", (LivingEntity) spider);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new SpiderNormalAttack("NormalAttack", this));
    addAttack(new SpiderPoisonAttack("PoisonAttack", this));
    addAttack(new SpiderSkyAttack("SkyAttack", this));
    addAttack(new SpiderWebAttack("WebAttack", this));
  }

}
