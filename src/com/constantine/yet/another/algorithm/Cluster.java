package com.constantine.yet.another.algorithm;

import com.constantine.model.Pattern;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by constantine on 5/31/15.
 */
public class Cluster {
    private Logger log = Logger.getLogger(this.getClass());
    private int label = 0;
    private double[] center;
    private int size = 0;

    public Cluster(int label, int size) {
        this.label = label;
        this.center = new double[size];
        this.size = size;
    }

    public Cluster(double[] center) {
        this.center = center;
        this.size = center.length;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public double[] getCenter() {
        return center;
    }

    public double getCenter(int pos) {
        return center[pos];
    }
    public void setCenter(double[] center) {
        this.center = center;
    }

    public void setCenter(int pos, double center) {
        this.center[pos] = center;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                label +
                ": " + Arrays.toString(center) +
                "}";
    }
}
