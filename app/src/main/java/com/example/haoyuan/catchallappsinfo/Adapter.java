package com.example.haoyuan.catchallappsinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.haoyuan.catchallappsinfo.Adapter.ViewHolder;
import java.util.List;

/**
 * Created by haoyuan on 2018/1/3.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

  List<PackageInfoShow> showList;

  public Adapter(@NonNull List<PackageInfoShow> showList) {
    this.showList = showList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.app_info_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PackageInfoShow infoShow = showList.get(position);
    holder.relativeLayout.setOnClickListener(v -> {
    });
    holder.imageView.setImageDrawable(infoShow.icon);
    holder.label.setText(infoShow.appName);
    holder.packageName.setText(infoShow.packageName);
    holder.versionName.setText(infoShow.versionName);
    holder.versionCode.setText(infoShow.versionCode);
    holder.apkSize.setText(infoShow.apkSize);
    holder.more.setOnClickListener(v -> {

      View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pop_menu_view, null);
      TextView appInfo = view.findViewById(R.id.appInfo);
      appInfo.setOnClickListener(appInfoView -> {
        toApplicationDetails(appInfoView.getContext(), infoShow.packageName);
      });
      TextView uninstall = view.findViewById(R.id.uninstall);
      uninstall.setOnClickListener(appInfoView -> {
        uninstallApk(appInfoView.getContext(), infoShow.packageName);
      });

      PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT, true);
      popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
      popupWindow.showAsDropDown(holder.relativeLayout,
              0, (int) convertDpToPixel(-20, v.getContext()), Gravity.END);
    });

  }

  @Override
  public int getItemCount() {
    return showList.size();
  }

  public void setShowList(List<PackageInfoShow> showList) {
    this.showList = showList;
    notifyDataSetChanged();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout relativeLayout;
    ImageView imageView;
    TextView label;
    TextView packageName;
    TextView versionName;
    TextView versionCode;
    TextView apkSize;
    ImageView more;

    ViewHolder(View itemView) {
      super(itemView);

      relativeLayout = itemView.findViewById(R.id.relativeLayout);
      imageView = itemView.findViewById(R.id.icon);
      label = itemView.findViewById(R.id.label);
      packageName = itemView.findViewById(R.id.packageName);
      versionName = itemView.findViewById(R.id.versionName);
      versionCode = itemView.findViewById(R.id.versionCode);
      apkSize = itemView.findViewById(R.id.apkSize);
      more = itemView.findViewById(R.id.more);

    }
  }

  public static void uninstallApk(Context context, String packageName) {
    Uri packageURI = Uri.parse("package:" + packageName);
    Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
    context.startActivity(intent);
  }

  public static void toApplicationDetails(Context context, String packageName) {
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.parse("package:" + packageName);
    intent.setData(uri);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(intent);
  }

  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    return px;
  }


}
