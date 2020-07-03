package com.gmail.enzocampanella98.candidatecrush.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

import java.util.Collection;

import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.IS_TESTING_LEVELS;
import static com.gmail.enzocampanella98.candidatecrush.CandidateCrush.IS_TESTING_WIN;

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

    public static int getGameVal(int prodVal, int testWinVal, int testLoseVal) {
        return IS_TESTING_LEVELS
                ? (IS_TESTING_WIN ? testWinVal : testLoseVal)
                : prodVal;
    }

    public static double getGameVal(double prodVal, double testWinVal, double testLoseVal) {
        return IS_TESTING_LEVELS
                ? (IS_TESTING_WIN ? testWinVal : testLoseVal)
                : prodVal;
    }

    public static float getGameVal(float prodVal, float testWinVal, float testLoseVal) {
        return IS_TESTING_LEVELS
                ? (IS_TESTING_WIN ? testWinVal : testLoseVal)
                : prodVal;
    }

    public static boolean getGameVal(boolean prodVal, boolean testWinVal, boolean testLoseVal) {
        return IS_TESTING_LEVELS
                ? (IS_TESTING_WIN ? testWinVal : testLoseVal)
                : prodVal;
    }

    public static Object getGameVal(Object prodVal, Object testWinVal, Object testLoseVal) {
        return IS_TESTING_LEVELS
                ? (IS_TESTING_WIN ? testWinVal : testLoseVal)
                : prodVal;
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
