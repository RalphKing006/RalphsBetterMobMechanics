package com.LucasRomier.BetterMobAI.API;

import java.lang.reflect.Field;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface Reflection {

  static Object getClassFieldObject(String name, Class<?> clazz, Object object) {
    try {
      Field field = clazz.getDeclaredField(name);
      field.setAccessible(true);
      return field.get(object);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  default void setValue(Object object, String name, Object value) {
    try {
      Field field = object.getClass().getDeclaredField(name);
      field.setAccessible(true);
      field.set(object, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  default Object getValue(Object object, String name) {
    try {
      Field field = object.getClass().getDeclaredField(name);
      field.setAccessible(true);
      return field.get(object);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  default void sendPacket(Packet<?> packet, Player player) {
    (((CraftPlayer) player).getHandle()).b.sendPacket(packet);
  }

  default void sendPacket(Packet<?> packet) {
    for (Player p : Bukkit.getOnlinePlayers()) {
      sendPacket(packet, p);
    }
  }
}
