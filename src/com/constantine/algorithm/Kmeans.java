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
        }


        //while (!hasFinished)

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
                log.info("*****************************Centers Stabilized !!!!!!*****************************");
                log.info("*****************************Repeats :" + repeat + " *****************************");
            }


            repeat++;

        }
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
}

