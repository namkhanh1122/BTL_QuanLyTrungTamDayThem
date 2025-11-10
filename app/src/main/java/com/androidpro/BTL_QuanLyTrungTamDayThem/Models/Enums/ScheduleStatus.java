package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums;


public enum ScheduleStatus {
    Planned(0),
    Active(1),
    Completed(2),
    Canceled(3);

    private final int value;

    ScheduleStatus(int value) { this.value = value; }

    public int getValue() { return value; }

    public static ScheduleStatus getTypeByValue(int value) {
        switch (value) {
            case 0:
                return ScheduleStatus.Planned;
            case 1:
                return ScheduleStatus.Active;
            case 2:
                return ScheduleStatus.Completed;
            case 3:
                return ScheduleStatus.Canceled;
        }
        return ScheduleStatus.Planned;
    }
}
