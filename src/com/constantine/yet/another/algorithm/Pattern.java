package com.constantine.yet.another.algorithm;

import com.constantine.utils.Extension;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created by constantine on 5/31/15.
 */
public class Pattern {
    private static Logger logger = Logger.getLogger(Pattern.class);
    private int[] stringPattern;
    private double[] attributes;
    private int size = 0;
    private String classname;

    public Pattern(double[] attributes) {
        this.attributes = attributes;
        this.size = attributes.length;
        this.stringPattern = new int[size];
    }

    public Pattern(int size) {
        this.size = size;
        this.stringPattern = new int[size];
        this.attributes = new double[size];
    }

    /***
     * Arranges the value of a Pattern's attribute to a cluster, according to
     * the euclidean distance from the closest cluster's center
     * @param centers the set of centers of the clusters that corresponding to the selected attribute
     * @param pos the specific attribute that is to be examined
     *
     * @return true if an arrangement took place, false if nothing changed
     */
    public int arrangePattern(double[] centers, int pos) {
        /*
        1. for each of the N clusters of size M select the N centers that correspond to the ith position (parameter 'pos')
        2. set the initial distance to the euclidean distance between the first center and the ith attribute.
        3. for each of the rest selected N-1 centers, examine the euclidean distances and find the cluster that owns the shortest one
        4. designate this cluster in the Pattern's StringPattern.
         */
        Extension extension = new Extension();
        double distance = extension.euclideanDistance(centers[0], getValue(pos));
        int index = 0;
        for (int i = 1; i < centers.length; i++) {
            if (distance > extension.euclideanDistance(centers[i], getValue(pos))) {
                distance = extension.euclideanDistance(centers[i], getValue(pos));
                index = i;
            }
        }

        if (getCluster(pos) != index)
            setCluster(pos, index);

        return getCluster(pos);
    }

    public int[] getStringPattern() {
        return stringPattern;
    }

    public void setStringPattern(int[] stringPattern) {
        this.stringPattern = stringPattern;
    }

    public int getCluster(int pos) {
        return stringPattern[pos];
    }

    public void setCluster(int pos, int cluster) {
        this.stringPattern[pos] = cluster;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }

    public double getValue(int pos) {
        return attributes[pos];
    }

    public void setValue(int pos, double value) {
        this.attributes[pos] = value;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "stringPattern=" + Arrays.toString(stringPattern) +
                ", attributes=" + Arrays.toString(attributes) +
                ", size=" + size +
                ", classname='" + classname + '\'' +
                '}';
    }
}
