package com.androidpro.BTL_QuanLyTrungTamDayThem.Workers;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.LessonDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.MainActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.SplashActivity;

public class ScheduleWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_REFRESH = "com.androidpro.BTL_QuanLyTrungTamDayThem.ACTION_REFRESH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static PendingIntent getRefreshPendingIntent(Context context, int appWidgetId) {
        Intent intent = new Intent(context, ScheduleWidgetProvider.class);
        intent.setAction(ACTION_REFRESH);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        return PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_schedule_widget);

        Intent serviceIntent = new Intent(context, ScheduleWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

        Intent clickIntentTemplate = new Intent(context, LessonDetailActivity.class);
        clickIntentTemplate.setAction(Intent.ACTION_VIEW);
        PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(
                context,
                0,
                clickIntentTemplate,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntentTemplate);


        views.setOnClickPendingIntent(
                R.id.widget_refresh_button,
                getRefreshPendingIntent(context, appWidgetId)
        );


        appWidgetManager.updateAppWidget(appWidgetId, views);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_REFRESH.equals(intent.getAction())) {

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);

                Toast.makeText(context, "Đang làm mới...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Widget đầu tiên được thêm
    }

    @Override
    public void onDisabled(Context context) {
        // Widget cuối cùng bị xóa
    }
}