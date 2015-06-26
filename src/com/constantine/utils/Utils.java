package com.constantine.utils;

import org.apache.log4j.Logger;

import java.util.*;

public class Utils {
    private Logger log = Logger.getLogger(this.getClass());


    public Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order) {

        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());

                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public String getString(double[] data) {
        String str = new String();
        for (double s : data) {
            str += s + " ";

        }

        return str;
    }


    public String printString(double[] data) {
        String str = "[ ";

        for (double s : data) {
            str += s + " , ";

        }
        str += "]";
        str = str.replace(", ]", "]");
        return str;
    }

    public String printString(int[] data) {
        String str = "[ ";

        for (int s : data) {
            str += s + " , ";

        }
        str += "]";
        str = str.replace(", ]", "]");
        return str;
    }

    public boolean compare(String[] a, String[] b) {
        boolean[] equal = new boolean[a.length];

        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                equal[i] = false;
                //        log.info("Not equal " + a[i] + " \n " + b[i]);
            } else {
                equal[i] = true;
                //  log.info("  equal " + a[i] + "  \n" + b[i]);
            }
        }
        boolean allEqual = true;

        for (int i = 0; i < equal.length; i++) {
            if (!equal[i]) {
                allEqual = false;
                //   log.info("Not All equal " + allEqual);
            }
        }

        return allEqual;

    }


    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }


}
