package com.androidpro.BTL_QuanLyTrungTamDayThem.Workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncWorker extends Worker {
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull @Override
    public Result doWork() {
        // TODO: gọi repository.pushDirty(); repository.pullSince(lastSync)
        return Result.success();
    }
}
