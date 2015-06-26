package com.constantine.kmeans;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envy17 j110ea on 29/5/2015
 */
public class Stats {
    private Logger log = Logger.getLogger(this.getClass());


    public void printLabels(Cluster[] clusters, ArrayList<Pattern> patterns) {
        log.info("\n");

        log.info("*************************************************************************************");

        log.info("**********************************STATISTICS****************************************");

        log.info("*************************************************************************************");
        log.info("\n");


        int varNumber = patterns.get(1).getSize();

        int K = clusters.length;

        ArrayList<HashMap<String, Integer>> counters = new ArrayList<HashMap<String, Integer>>();

        for (int k = 0; k < K; k++) {
            counters.add(new HashMap<String, Integer>());
        }

        log.info("\n");
        for (int j = 0; j < varNumber; j++) {

            log.info("For Attribute " + j);
            for (int k = 0; k < K; k++) {
                for (Pattern pattern : patterns) {
                    if (pattern.getStringPattern()[j] == k) {
                        if (counters.get(k).containsKey(pattern.getClassname())) {
                            counters.get(k).put(pattern.getClassname(), counters.get(k).get(pattern.getClassname()) + 1);
                        } else {
                            counters.get(k).put(pattern.getClassname(), 1);
                        }
                    }

                }
            }

            int k = 0;
            for (Cluster c : clusters) {

                log.info("Cluster Label " + c.getLabel());


                log.info("Members Class Type ");

                Integer max = 0;
                String maxClass = null;
                int clusterMembers = 0;
                for (Map.Entry<String, Integer> e : counters.get(k).entrySet()) {
                    log.info(e.getKey() + ": " + e.getValue());

                    if (e.getValue() > max) {
                        max = e.getValue();
                        maxClass = e.getKey();
                    }

                    clusterMembers += e.getValue();
                }


                Double percentage = (Double.valueOf(max) / Double.valueOf(clusterMembers)) * 100;
                log.info("Prevalent Classname : " + maxClass + " for " + Double.toString(percentage) + "%");
                log.info("Assigned Cluster Classname: " + maxClass);
                log.info("\n");

                k++;
            }


        }


    }
    /*

    public void computeError(ArrayList<Cluster> clusters, ArrayList<Pattern> patterns) {
        int varNumber = clusters.get(0).getVarNum();
        for (int j = 0; j < varNumber; j++) {
            int wronglyClassifiedPatterns = 0;
            for (Pattern pattern : patterns) {
                int index = 0;

                double dist = extension.euclideanDistance(pattern.getVariables()[j], clusters.get(0).getCenter()[j]);

                for (Cluster cluster : clusters) {
                    double currentDist = extension.euclideanDistance(pattern.getVariables()[j], cluster.getCenter()[j]);
                    if (currentDist < dist) {
                        dist = currentDist;
                        index = clusters.indexOf(cluster);
                    }
                }

                if (index != pattern.getLabelValue(j)) {
                    wronglyClassifiedPatterns++;

                }
            }

            log.info("Wrongly classified  " + wronglyClassifiedPatterns);
        }
    }
*/

}
