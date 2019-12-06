package com.gmail.enzocampanella98.candidatecrush.tools;

import com.badlogic.gdx.graphics.Color;

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
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static Color colorFromRGB(int r, int g, int b) {
        return new Color(r/255f, g/255f, b/255f, 1f);
    }
}
