package com.androidpro.BTL_QuanLyTrungTamDayThem.Workers;

import android.content.Context;
import androidx.work.*;

import java.util.concurrent.TimeUnit;

public final class SyncScheduler {
    public static void schedule(Context ctx) {
        Constraints c = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest req = new PeriodicWorkRequest.Builder(
                SyncWorker.class, 30, TimeUnit.MINUTES)
                .setConstraints(c)
                .build();
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                "sync", ExistingPeriodicWorkPolicy.KEEP, req
        );
    }
}
