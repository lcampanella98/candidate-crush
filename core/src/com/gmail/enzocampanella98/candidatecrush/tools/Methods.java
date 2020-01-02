package com.gmail.enzocampanella98.candidatecrush.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

import java.util.Collection;

/**
 * Created by enzoc on 5/22/2018.
 */

public final class Methods {

    public static String getCommaSeparatedNumber(int n) {
        if (n < 0) return "-" + getCommaSeparatedNumber(-n);
        StringBuilder sb = new StringBuilder();
        if (n == 0) {
            return "0";
        }
        int portion = 0;
        int portionTemp = 0;
        while (n > 0) {
            if (sb.length() > 0) sb.append(',');
            portion = n % 1000;
            portionTemp = portion;
            if (portionTemp == 0) sb.append(0);
            while (portionTemp > 0) {
                sb.append(portionTemp % 10);
                portionTemp /= 10;
            }
            if (portion < 100) sb.append(0);
            if (portion < 10) sb.append(0);
            n /= 1000;
        }
        if (portion < 100) sb.deleteCharAt(sb.length() - 1);
        if (portion < 10) sb.deleteCharAt(sb.length() - 1);
        return sb.reverse().toString();
    }

    public static String firstToUpper(String s) {
        String[] spl = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : spl) {
            sb.append(w.substring(0, 1).toUpperCase());
            sb.append(w.substring(1));
        }
        return sb.toString();
    }

    public static Vector2 avg(Collection<Vector2> vects) {
        Vector2 avg = new Vector2(0, 0);
        int ct = 0;
        for (Vector2 v : vects) {
            avg.x += v.x;
            avg.y += v.y;
            ct++;
        }
        avg.x /= ct;
        avg.y /= ct;
        return avg;
    }

    public static Color colorFromRGB(int r, int g, int b) {
        return new Color(r/255f, g/255f, b/255f, 1f);
    }

    public static int getGameVal(int prodVal, int testVal) {
        return CandidateCrush.IS_TESTING_LEVELS ? testVal : prodVal;
    }

    public static double getGameVal(double prodVal, double testVal) {
        return CandidateCrush.IS_TESTING_LEVELS ? testVal : prodVal;
    }

    public static float getGameVal(float prodVal, float testVal) {
        return CandidateCrush.IS_TESTING_LEVELS ? testVal : prodVal;
    }

    public static boolean getGameVal(boolean prodVal, boolean testVal) {
        return CandidateCrush.IS_TESTING_LEVELS ? testVal : prodVal;
    }

    public static Object getGameVal(Object prodVal, Object testVal) {
        return CandidateCrush.IS_TESTING_LEVELS ? testVal : prodVal;
    }

    /*
    Maths
     */
    public static int roundUpToNearest(double n, int toNext) {
        return ((int)Math.floor(n / toNext)) * toNext;
    }
    public static int roundDownToNearest(double n, int toNext) {
        return ((int)Math.ceil(n / toNext)) * toNext;
    }

    public static int roundToNearest(double n, int toNext) {
        int down = roundDownToNearest(n, toNext);
        int up = roundUpToNearest(n, toNext);
        if (Math.abs(n - down) < Math.abs(n - up)) {
            return down;
        }
        return up;
    }


}
