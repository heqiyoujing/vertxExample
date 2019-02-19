package io.example.test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: yiqq
 * @date: 2018/10/10
 * @description:
 */
public class TestCalendar {
    private static final int TIME = 1000 * 60 * 5;
    /**
     * 获得这个时间的当天的0点时间
     */
    public static long getZeroTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        Date zero = calendar.getTime();
        return zero.getTime();
    }
    private static boolean isZeroNear() {
        long now = System.currentTimeMillis();

        // 当天 0 点
        long zeroTime = getZeroTime(now);

        // 次日 0 点
        long nextZeroTime = zeroTime + 1000 * 60 * 60 * 24;

        if (now - zeroTime < TIME) {// 在当日 0 点附近
            return true;
        }

        if (nextZeroTime - now < TIME) {// 在次日 0 点附近
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        if (isZeroNear()) {// 在 0 点附近不推送
            System.out.println(true);
            return ;
        }else{
            System.out.println(false);
        }
    }
}


