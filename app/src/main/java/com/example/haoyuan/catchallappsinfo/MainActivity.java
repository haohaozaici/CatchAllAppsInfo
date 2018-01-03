package com.example.haoyuan.catchallappsinfo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private Adapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerView info = findViewById(R.id.RecyclerView);
    info.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new Adapter(new ArrayList<>());
    info.setAdapter(mAdapter);

    loadPackageInfo();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_refresh:
        loadPackageInfo();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void loadPackageInfo() {

    Observable.create((ObservableOnSubscribe<List<PackageInfoShow>>) e -> {
      List<PackageInfoShow> infoShows = new ArrayList<>();

      PackageManager pm = getPackageManager();
      List<PackageInfo> pmList = pm.getInstalledPackages(0);
      for (PackageInfo info : pmList) {
        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
          infoShows.add(new PackageInfoShow(
              info.applicationInfo.loadIcon(pm),
              info.applicationInfo.loadLabel(pm).toString(),
              info.packageName,
              info.versionName,
              "(" + info.versionCode + ")",
              getPacakgeSize(info.applicationInfo)));
        }
      }
//      XLog.b().json(new Gson().toJson(infoShows));
      e.onNext(infoShows);
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<PackageInfoShow>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(List<PackageInfoShow> packageInfoShows) {
            mAdapter.setShowList(packageInfoShows);
            Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });


  }

  public static String formatString(String text) {

    StringBuilder json = new StringBuilder();
    String indentString = "";

    for (int i = 0; i < text.length(); i++) {
      char letter = text.charAt(i);
      switch (letter) {
        case '{':
        case '[':
          json.append("\n").append(indentString).append(letter).append("\n");
          indentString = indentString + "\t";
          json.append(indentString);
          break;
        case '}':
        case ']':
          indentString = indentString.replaceFirst("\t", "");
          json.append("\n" + indentString + letter);
          break;
        case ',':
          json.append(letter + "\n" + indentString);
          break;

        default:
          json.append(letter);
          break;
      }
    }

    return json.toString();
  }

  public String getPacakgeSize(ApplicationInfo applicationInfo) {
    File file = new File(applicationInfo.sourceDir);
    String size = humanReadableByteCount(file.length(), true);
    return size;
  }

  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) {
      return bytes + " B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }

}
