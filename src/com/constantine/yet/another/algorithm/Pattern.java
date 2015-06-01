package com.constantine.yet.another.algorithm;

import com.constantine.utils.Extension;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created by constantine on 5/31/15.
 */
public class Pattern {
    private Logger log = Logger.getLogger(this.getClass());
    int[] stringPattern;
    Attribute[] attribute;
    int size = 0;
    String classname;

    public Pattern(Attribute[] attribute) {
        this.attribute = attribute;
        this.size = attribute.length;
        this.stringPattern = new int[size];
    }

    public Pattern(int size) {
        this.size = size;
        this.stringPattern = new int[size];
        this.attribute = new Attribute[size];
        for (int i = 0; i < size; i++) {
            this.attribute[i] = new Attribute();
        }
    }

    public void arrangeAttribute(double[] xs, int pos) {
        Attribute attribute = this.attribute[pos];
        Extension extension = new Extension();
        double distance = extension.euclideanDistance(xs[0], attribute.getData());
        int index = 0;
        for (int i = 1; i < xs.length; i++) {
            if (distance > extension.euclideanDistance(xs[i], attribute.getData())) {
                distance = extension.euclideanDistance(xs[i], attribute.getData());
                index = i;
            }
        }

        attribute.setCluster(index);
        this.stringPattern[pos] = index;
    }

    public int[] getStringPattern() {
        return this.stringPattern;
    }

    public int getCluster(int pos) {
        return attribute[pos].getCluster();
    }

    public void setStringPattern(int[] stringPattern) {
        this.stringPattern = stringPattern;
    }

    public void setCluster(int pos, int cluster) {
        this.attribute[pos].setCluster(cluster);
    }

    public Attribute[] getAttribute() {
        return attribute;
    }

    public double getData(int pos) {
        return attribute[pos].getData();
    }

    public void setAttribute(Attribute[] attribute) {
        this.attribute = attribute;
    }

    public void setData(int pos, double data) {
        this.attribute[pos].setData(data);
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
            values[i] = this.getData(i);
        }
        return values;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "stringPattern=" + Arrays.toString(stringPattern) +
                ", attribute=" + Arrays.toString(attribute) +
                ", size=" + size +
                ", classname='" + classname + '\'' +
                '}';
    }
}
