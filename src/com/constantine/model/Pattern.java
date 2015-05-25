package com.constantine.model;

import org.apache.log4j.Logger;

import java.util.Arrays;


/**
 * Created by envy17 j110ea on 21/5/2015.
 */
public class Pattern {
    private int[] label;
    private String[] variables;
    private long id;
    private String classname;
    private int occurences;

    private Logger log = Logger.getLogger(this.getClass());

    public Pattern(String buff, String delim, long id, String[] variableIndeces, int classname) {

        String[] split = buff.split(delim);

        variables = new String[variableIndeces.length];

        for (int i=0; i < variables.length; i++) {
            variables[i] = split[Integer.parseInt(variableIndeces[i])];
        }
        this.classname = split[classname];

        label = new int[variables.length];
        this.id = id;

    }


    public Double getAttribute(int attributeNumber) {
        return Double.parseDouble(this.variables[attributeNumber]);

    }

    public int getAtrributeNum() {
        return this.variables.length;
    }


    public void normalize(double[] min, double[] max) {
        for (int i = 0; i < variables.length; i++) {
            if (isNumeric(variables[i])) {
                double d_new = (Double.parseDouble(variables[i]) - min[i]) / (max[i] - min[i]);
                //log.debug("Original value : " + variables[i] + "Normalized value: " + d_new);
                variables[i] = String.valueOf(d_new);
            }
        }
    }


    public void addLabel(int label, int attr) {
        this.label[attr] = label;
    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public int[] getLabel() {
        return label;
    }

    public String getLabelString() {
        String str = new String();
        for (int s : label) {
            str += s + " ";
        }
        return str;
    }

    public void setLabel(int[] label) {
        this.label = label;
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "label=" + Arrays.toString(label) +
                ", variables=" + Arrays.toString(variables) +
                ", id=" + id +
                ", classname='" + classname + '\'' +
                '}';
    }

    public int getOccurences() {
        return occurences;
    }

    public void setOccurences(int occurences) {
        this.occurences = occurences;
    }
}
