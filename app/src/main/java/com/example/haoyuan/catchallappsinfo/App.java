package com.example.haoyuan.catchallappsinfo;

import android.app.Application;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter;
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter;

/**
 * Created by haoyuan on 2018/1/2.
 */

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    LogConfiguration config = new LogConfiguration.Builder()
        .tag("PackageInfo")
        .b()
        .jsonFormatter(new DefaultJsonFormatter())
        .borderFormatter(new DefaultBorderFormatter())
        .build();
    XLog.init(config);
  }
}
