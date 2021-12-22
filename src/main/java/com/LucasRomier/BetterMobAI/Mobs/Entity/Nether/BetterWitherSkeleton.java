package com.LucasRomier.BetterMobAI.Mobs.Entity.Nether;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.nether.witherskeleton.WitherSkeletonNormalAttack;
import optic_fusion1.bmm.mob.attack.nether.witherskeleton.WitherSkeletonSkullAttack;
import optic_fusion1.bmm.mob.attack.nether.witherskeleton.WitherSkeletonSwordAttack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;

public class BetterWitherSkeleton extends BetterMob {

  public BetterWitherSkeleton(Skeleton skeleton) {
    super("WitherSkeleton", (LivingEntity) skeleton);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new WitherSkeletonNormalAttack("NormalAttack", this));
    addAttack(new WitherSkeletonSkullAttack("SkullAttack", this));
    addAttack(new WitherSkeletonSwordAttack("SwordAttack", this));
  }

}
