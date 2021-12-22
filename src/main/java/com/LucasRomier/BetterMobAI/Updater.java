package com.LucasRomier.BetterMobAI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater {

  private String oldVersion;

  private JavaPlugin plugin;

  public enum UpdateResult {
    BAD_RESOURCEID, DISABLED, FAIL_NOVERSION, FAIL_SPIGOT, NO_UPDATE, UPDATE_AVAILABLE;
  }

  private String RESOURCE_ID = "";

  private UpdateResult result = UpdateResult.DISABLED;

  private String version;

  public Updater(JavaPlugin plugin, Integer resourceId, boolean disabled) {
    this.RESOURCE_ID = resourceId + "";
    this.plugin = plugin;
    this.oldVersion = this.plugin.getDescription().getVersion();
    if (disabled) {
      this.result = UpdateResult.DISABLED;
      return;
    }
    run();
  }

  public UpdateResult getResult() {
    return this.result;
  }

  public String getVersion() {
    return this.version;
  }

  private void run() {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.RESOURCE_ID)).openConnection();
      int timed_out = 2000;
      connection.setConnectTimeout(timed_out);
      connection.setReadTimeout(timed_out);
      this.version = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
      connection.disconnect();
      versionCheck();
      return;
    } catch (Exception e) {
      this.result = UpdateResult.FAIL_SPIGOT;
      this.result = UpdateResult.FAIL_SPIGOT;
      return;
    }
  }

  public boolean shouldUpdate(String localVersion, String remoteVersion) {
    return (versionCompare(localVersion, remoteVersion) == 2);
  }

  private void versionCheck() {
    if (shouldUpdate(this.oldVersion, this.version)) {
      this.result = UpdateResult.UPDATE_AVAILABLE;
    } else {
      this.result = UpdateResult.NO_UPDATE;
    }
  }

  public int versionCompare(String v1, String v2) {
    int v1Len = StringUtils.countMatches(v1, ".");
    int v2Len = StringUtils.countMatches(v2, ".");
    if (v1Len != v2Len) {
      int count = Math.abs(v1Len - v2Len);
      if (v1Len > v2Len) {
        for (int j = 1; j <= count; j++) {
          v2 = v2 + ".0";
        }
      } else {
        for (int j = 1; j <= count; j++) {
          v1 = v1 + ".0";
        }
      }
    }
    if (v1.equals(v2)) {
      return 0;
    }
    String[] v1Str = StringUtils.split(v1, ".");
    String[] v2Str = StringUtils.split(v2, ".");
    for (int i = 0; i < v1Str.length; i++) {
      String str1 = "", str2 = "";
      for (char c : v1Str[i].toCharArray()) {
        if (Character.isLetter(c)) {
          int u = c - 97 + 1;
          if (u < 10) {
            str1 = str1 + String.valueOf("0" + u);
          } else {
            str1 = str1 + String.valueOf(u);
          }
        } else {
          str1 = str1 + String.valueOf(c);
        }
      }
      for (char c : v2Str[i].toCharArray()) {
        if (Character.isLetter(c)) {
          int u = c - 97 + 1;
          if (u < 10) {
            str2 = str2 + String.valueOf("0" + u);
          } else {
            str2 = str2 + String.valueOf(u);
          }
        } else {
          str2 = str2 + String.valueOf(c);
        }
      }
      v1Str[i] = "1" + str1;
      v2Str[i] = "1" + str2;
      int num1 = Integer.parseInt(v1Str[i]);
      int num2 = Integer.parseInt(v2Str[i]);
      if (num1 != num2) {
        if (num1 > num2) {
          return 1;
        }
        return 2;
      }
    }
    return -1;
  }
}
