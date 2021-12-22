package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.overworld.zombie.ZombieBloodRushAttack;
import optic_fusion1.bmm.mob.attack.overworld.zombie.ZombieMinionsAttack;
import optic_fusion1.bmm.mob.attack.overworld.zombie.ZombieNormalAttack;
import optic_fusion1.bmm.mob.attack.overworld.zombie.ZombieVampireAttack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

public class BetterZombie extends BetterMob {

  public BetterZombie(Zombie zombie) {
    super("Zombie", (LivingEntity) zombie);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new ZombieNormalAttack("NormalAttack", this));
    addAttack(new ZombieBloodRushAttack("BloodRushAttack", this));
    addAttack(new ZombieMinionsAttack("MinionsAttack", this));
    addAttack(new ZombieVampireAttack("VampireAttack", this));
  }

}
