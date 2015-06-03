package com.constantine.yet.another.algorithm;

/**
 * Created by constantine on 5/31/15.
 */
public class Attribute {
    private double value = 0.0;
    private int cluster = 0;

    public Attribute() {
    }

    public Attribute(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "value=" + value +
                ", cluster=" + cluster +
                '}';
    }
}
