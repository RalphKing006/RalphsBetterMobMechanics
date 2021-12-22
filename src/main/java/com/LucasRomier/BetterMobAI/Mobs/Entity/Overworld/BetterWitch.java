package com.LucasRomier.BetterMobAI.Mobs.Entity.Overworld;

import com.LucasRomier.BetterMobAI.MobAI;
import com.LucasRomier.BetterMobAI.Mobs.BetterMob;
import optic_fusion1.bmm.mob.attack.Attack;
import optic_fusion1.bmm.mob.attack.overworld.witch.WitchBlackMagicAttack;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;

public class BetterWitch extends BetterMob {

  public BetterWitch(Witch witch) {
    super("Witch", (LivingEntity) witch);
    registerAttacks();
  }

  private void registerAttacks() {
    addAttack(new WitchBlackMagicAttack("WitchBlackMagicAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchBurnAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchEndermitesAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchFireCircleAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchHealEffect", this));
    addAttack(new WitchBlackMagicAttack("WitchLavaAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchNormalAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchPoisonAttack", this));
    addAttack(new WitchBlackMagicAttack("WitchSlownessAttack", this));
  }

  @Override
  public void runRandomAttack(Player target, int delay) {
    setBusy(true);
    Bukkit.getScheduler().scheduleSyncDelayedTask(MobAI.getInstance(), () -> {
      Attack attack = getRandomAttack();
      if (attack.getName().equals("HealEffect") && getEntity().getHealth() < getEntity().getMaxHealth()) {
        attack.run(target);
        return;
      }
      attack.run(target);
    }, 20 * delay);
  }

}
