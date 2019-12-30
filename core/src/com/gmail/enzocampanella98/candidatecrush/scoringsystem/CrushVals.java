package com.gmail.enzocampanella98.candidatecrush.scoringsystem;

import java.util.HashMap;
import java.util.Map;

public class CrushVals {

    private final Map<CrushType, Integer> vals;

    public CrushVals(int val3, int val4, int valT, int val5) {
        vals = new HashMap<>();
        vals.put(CrushType.THREE, val3);
        vals.put(CrushType.FOUR, val4);
        vals.put(CrushType.T, valT);
        vals.put(CrushType.FIVE, val5);
    }

    public int get(CrushType crushType) {
        return vals.get(crushType);
    }

}
