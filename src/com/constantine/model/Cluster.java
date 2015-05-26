package com.constantine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by envy17 j110ea on 21/5/2015.
 */
public class Cluster {
    private String label;
    private double[] center;
    private List<Pattern> patterns;
    private int members = 0;
    private int varNum;

    public Cluster(String label, double[] center) {
        this.label = label;
        this.center = center;
    }

    public Cluster(int var) {
        this.center = new double[var];
        this.varNum = var;
        patterns = new ArrayList<Pattern>();
    }


    public void addPattern(Pattern p) {
        patterns.add(p);
        members++;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public int getVarNum() {
        return varNum;
    }

    public void setVarNum(int varNum) {
        this.varNum = varNum;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "label='" + label + '\'' +
                ", center=" + Arrays.toString(center) +
//                ", patterns=" + patterns +
                ", members=" + members +
                ", varNum=" + varNum +
                '}';
    }
}
