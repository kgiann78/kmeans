package com.constantine.yet.another.algorithm;

import com.constantine.utils.Extension;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created by constantine on 5/31/15.
 */
public class Pattern {
    private Logger log = Logger.getLogger(this.getClass());
    private int[] stringPattern;
    private Attribute[] attributes;
    private int size = 0;
    private String classname;

    public Pattern(Attribute[] attributes) {
        this.attributes = attributes;
        this.size = attributes.length;
        this.stringPattern = new int[size];
    }

    public Pattern(int size) {
        this.size = size;
        this.stringPattern = new int[size];
        this.attributes = new Attribute[size];
        for (int i = 0; i < size; i++) {
            this.attributes[i] = new Attribute();
        }
    }

    public void arrangeAttribute(double[] xs, int pos) {
        Attribute attribute = this.attributes[pos];
        Extension extension = new Extension();
        double distance = extension.euclideanDistance(xs[0], attribute.getValue());
        int index = 0;
        for (int i = 1; i < xs.length; i++) {
            if (distance > extension.euclideanDistance(xs[i], attribute.getValue())) {
                distance = extension.euclideanDistance(xs[i], attribute.getValue());
                index = i;
            }
        }

        setCluster(pos, index);
    }

    public int[] getStringPattern() {
        return this.stringPattern;
    }

    public int getCluster(int pos) {
        return attributes[pos].getCluster();
    }

    public void setStringPattern(int[] stringPattern) {
        this.stringPattern = stringPattern;
    }

    public void setCluster(int pos, int cluster) {
        this.attributes[pos].setCluster(cluster);
        this.stringPattern[pos] = cluster;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public double getValue(int pos) {
        return attributes[pos].getValue();
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
        for (int i = 0; i < size; i++) {
            this.stringPattern[i] = attributes[i].getCluster();
        }
    }

    public void setValue(int pos, double data) {
        this.attributes[pos].setValue(data);
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

    public double[] getValues() {
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = this.getValue(i);
        }
        return values;
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
