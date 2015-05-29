package com.constantine.algorithm;

import com.constantine.model.Cluster;
import com.constantine.model.Pattern;
import com.constantine.utils.Extension;
import com.constantine.utils.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envy17 j110ea on 29/5/2015.
 */
public class Stats {
    private Logger log = Logger.getLogger(this.getClass());
    private Utils utils = new Utils();
    private Extension extension = new Extension();

    public void printLabels(ArrayList<Cluster> clusters, ArrayList<Pattern> patterns) {
        log.info("\n");

        log.info("*************************************************");

        log.info("****************STATISTICS***********************");

        log.info("*************************************************");
        log.info("\n");
        log.info("Final Cluster Centers : ");

        int varNumber = clusters.get(0).getVarNum();

        int K = clusters.size();
        for (Cluster cluster : clusters) {
            log.info(utils.printString(cluster.getCenter()));
            cluster.clear();
        }


        for (int j = 0; j < varNumber; j++) {


            log.info("For Attribute " + j);

            for (int k = 0; k < K; k++) {
                for (Pattern pattern : patterns) {
                    if (pattern.getLabelValue(j) == k) {
                        clusters.get(k).addPattern(pattern);

                    }

                }


            }


            for (Cluster c : clusters) {
                log.info("Cluster INFO : ");
                log.info("Cluster Label " + c.getLabel());
                log.info("Cluster members " + c.getMembers());

                HashMap<String, Integer> diffValues = new HashMap<String, Integer>();

                for (Pattern p : c.getPatterns()) {


                    if (diffValues.containsKey(p.getClassname())) {
                        int count = diffValues.get(p.getClassname());
                        diffValues.put(p.getClassname(), count + 1);

                    } else {
                        diffValues.put(p.getClassname(), 1);
                    }
                }

                log.info("Members Class Type ");


                Integer max = 0;
                String maxClass = null;
                for (Map.Entry<String, Integer> e : diffValues.entrySet()) {
                    log.info(e.getKey() + ": " + e.getValue());

                    if (e.getValue() > max) {
                        max = e.getValue();
                        maxClass = e.getKey();
                    }

                }

                Double percentage = (Double.valueOf(max) / Double.valueOf(c.getMembers())) * 100;
                log.info("Prevalent Classname : " + maxClass + " for " + Double.toString(percentage) + "%");
                log.info("Assigned Cluster Classname: " + maxClass);
                log.info("\n");


            }

            for (Cluster cluster : clusters) {
                cluster.clear();
            }

        }


    }

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
}
