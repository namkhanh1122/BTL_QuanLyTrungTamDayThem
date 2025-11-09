package com.androidpro.BTL_QuanLyTrungTamDayThem;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mUtils {
    public static int calculateProgressPercentage(Date startDate, Date endDate) {
        // 1. Lấy thời gian hiện tại
        long currentTime = new Date().getTime();

        // 2. Lấy thời gian bắt đầu và kết thúc (dưới dạng mili giây)
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        // 3. Xử lý các trường hợp đặc biệt (edge cases)

        // Nếu chưa bắt đầu
        if (currentTime < startTime) {
            return 0;
        }

        // Nếu đã kết thúc
        if (currentTime >= endTime) {
            return 100;
        }

        // Nếu ngày bắt đầu và kết thúc bị lỗi (vd: kết thúc <= bắt đầu)
        long totalDuration = endTime - startTime;
        if (totalDuration <= 0) {
            return 100; // Coi như đã xong nếu đã qua thời gian bắt đầu
        }

        // 4. Tính toán thời gian đã trôi qua
        long elapsedDuration = currentTime - startTime;

        // 5. Tính toán phần trăm
        // Dùng (double) để đảm bảo phép chia là số thực, sau đó mới làm tròn
        double percentage = ((double) elapsedDuration / (double) totalDuration) * 100.0;

        // Trả về số nguyên đã làm tròn (ví dụ: 75.8% -> 76)
        return (int) Math.round(percentage);
    }

}
