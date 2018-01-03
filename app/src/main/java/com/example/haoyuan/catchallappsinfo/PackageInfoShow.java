package com.example.haoyuan.catchallappsinfo;

import android.graphics.drawable.Drawable;

/**
 * Created by haoyuan on 2018/1/2.
 */

public class PackageInfoShow {

  Drawable icon;
  String appName;
  String packageName;
  String versionName;
  String versionCode;
  String apkSize;

  public PackageInfoShow(Drawable icon, String appName, String packageName,
      String versionName, String versionCode, String apkSize) {
    this.icon = icon;
    this.appName = appName;
    this.packageName = packageName;
    this.versionName = versionName;
    this.versionCode = versionCode;
    this.apkSize = apkSize;
  }
}
