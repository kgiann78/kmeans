package com.constantine.algorithm;

import com.constantine.model.Cluster;
import com.constantine.model.Pattern;
import com.constantine.utils.Extension;
import com.constantine.utils.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by envy17 j110ea on 24/5/2015.
 */
public class Kmeans {
    private CCIA CCIA;
    private List<Cluster> clusters;
    private List<Pattern> patterns;
    private Utils utils;

    private Extension extension;

    private Properties properties;
    private Logger log = Logger.getLogger(this.getClass());

    public Kmeans(Properties properties) {
        this.properties = properties;
        extension = new Extension();
        extension = new Extension();
        utils = new Utils();
    }

    public void initializeCenters() throws Exception {
        CCIA = new CCIA(properties);

        CCIA.loadPatterns();

        CCIA.normalize();
        clusters = CCIA.initializeCenters();
    }

    public void execute() {
        log.info("Now ready to start executing k-means...");
        patterns = CCIA.getPatterns();

        boolean stabilizedCenters = false;

        String[] initCenters = new String[clusters.size()];
        String[] finalCenters = new String[clusters.size()];


        for (int counter = 0; counter < clusters.size(); counter++) {

            clusters.get(counter).getPatterns().clear();
            clusters.get(counter).setMembers(0);
        }

        int repeat = 0;

        while (!stabilizedCenters) {


            for (int counter = 0; counter < clusters.size(); counter++) {
                initCenters[counter] = utils.getString(clusters.get(counter).getCenter());
            }


            for (int j = 0; j < CCIA.getVariablesNumber(); j++) {
                addPattern(j);
            }


            for (int counter = 0; counter < clusters.size(); counter++) {
                finalCenters[counter] = utils.getString(clusters.get(counter).getCenter());
            }


            log.info("CLUSTER  INITIAL CENTERS: ");
            for (int counter = 0; counter < clusters.size(); counter++) {
                log.info(initCenters[counter]);

            }

            log.info("CLUSTER FINAL  CENTERS: ");
            for (int counter = 0; counter < clusters.size(); counter++) {
                log.info(finalCenters[counter]);
            }


            boolean allEqual = utils.compare(initCenters, finalCenters);

            if (allEqual) {
                stabilizedCenters = true;
                log.info("***************************** Centers Stabilized !!!!!!*****************************");
                log.info("***************************** Repeats :" + repeat + " *****************************");
            }


            repeat++;

        }

        computeError();
        printLabels();
    }


    public void addPattern(int i) {

        for (Pattern pattern : patterns) {
            int index = 0;

            double dist = extension.euclideanDistance(pattern.getVariables()[i], clusters.get(0).getCenter()[i]);
            for (Cluster cluster : clusters) {
                double currentDist = extension.euclideanDistance(pattern.getVariables()[i], cluster.getCenter()[i]);
                if (currentDist < dist) {
                    dist = currentDist;
                    index = clusters.indexOf(cluster);

                }
            }

            pattern.addLabel(index, i);

        }


        for (int k = 0; k < CCIA.getK(); k++) {
            List<Double> points = new ArrayList<Double>();

            for (Pattern pattern : patterns) {
                if (pattern.getLabel()[i] == k) {
                    points.add(pattern.getVariables()[i]);
                }
            }

            double[] currentCenter = clusters.get(k).getCenter();
            currentCenter[i] = extension.calculateCenter(extension.toDoubleArray(points.toArray()));
            clusters.get(k).setCenter(currentCenter);


        }

    }

    //TODO fill this in
    //has to compare distance between a point and its  assigned cluster center with the distance of this point from the rest of the cluster centers
//if there another cluster center is closer than the one we're currently assigned to , then this point is on a wrong cluster

    public void computeError() {
        for (int j = 0; j < CCIA.getVariablesNumber(); j++) {
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

                if (index == pattern.getLabelValue(j)) {
                    //   log.info("Assigned to correct cluster");
                } else {
                    wronglyClassifiedPatterns++;
                    //     log.info("Assigned to wrong cluster");
                }
            }
            log.info("Wrongly classified  " + wronglyClassifiedPatterns);
        }
    }


    public void printLabels() {


        for (int j = 0; j < CCIA.getVariablesNumber(); j++) {

           // for (Pattern pattern : patterns) {
         //      // log.info(" Label " + pattern.getLabelString());
              //  log.info(" Classname " + pattern.getClassname());
           // }

            log.info(" For Attribute " + j);

            for (int k = 0; k < CCIA.getK(); k++) {
                for (Pattern pattern : patterns) {
                    if (pattern.getLabelValue(j) == k) {
                        clusters.get(k).addPattern(pattern);
                        log.info("Label " + pattern.getLabelValue(j) + "  classname  " + pattern.getClassname());

                   }

                }
                log.info("\n");
            }

     /*    for (Cluster c : clusters) */{
/*
                log.info("Cluster Label " + c.getLabel());
                log.info("Cluster members " + c.getMembers());

                HashMap<String, Integer> diffValues = new HashMap<String, Integer>();

                for (Pattern p : c.getPatterns()) {

                    int count = diffValues.get(p.getClassname());

                    diffValues.put(p.getClassname(), count + 1);

                }

                log.info("\n");*/

          /*      for (Map.Entry<String, Integer> e : diffValues.entrySet()) {
                    log.info("  Label " + e.getKey());
                    log.info("Occurences in cluster " + e.getValue());

                    log.info("\n");
                }
 */

     }


        for (Cluster c : clusters) {
            c.getPatterns().clear();
            c.setMembers(0);
        }
        log.info("\n");


    }


}

}

