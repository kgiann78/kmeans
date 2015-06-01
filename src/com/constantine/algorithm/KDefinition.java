package com.constantine.algorithm;

/**
 * Created by constantine on 5/29/15.
 */
public class KDefinition {
    public static int ruleOfThumb(int dataPoints) {
        if (dataPoints != 0)
            return (int)Math.sqrt((double)dataPoints/2);
        else
            return (int)Math.sqrt((double)150/2);
    }
}
