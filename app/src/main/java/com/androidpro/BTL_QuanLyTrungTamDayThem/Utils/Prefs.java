package com.androidpro.BTL_QuanLyTrungTamDayThem.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
    private static final String P = "prefs_app";

    public static long getLastSync(Context ctx) {
        return ctx.getSharedPreferences(P, Context.MODE_PRIVATE).getLong("last_sync", 0L);
    }
    public static void setLastSync(Context ctx, long t) {
        SharedPreferences.Editor e = ctx.getSharedPreferences(P, Context.MODE_PRIVATE).edit();
        e.putLong("last_sync", t).apply();
    }
}
