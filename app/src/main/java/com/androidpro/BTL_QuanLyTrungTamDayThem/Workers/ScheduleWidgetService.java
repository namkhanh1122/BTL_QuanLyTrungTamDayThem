package com.androidpro.BTL_QuanLyTrungTamDayThem.Workers;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ScheduleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // Trả về Factory, đây là nơi logic chính
        return new ScheduleWidgetFactory(this.getApplicationContext(), intent);
    }
}