package com.constantine.yet.another.algorithm;

/**
 * Created by constantine on 5/31/15.
 */
public class Attribute {
    double data = 0.0;
    int cluster = 0;

    public Attribute() {
    }

    public Attribute(double data) {
        this.data = data;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
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
                "data=" + data +
                ", cluster=" + cluster +
                '}';
    }
}
