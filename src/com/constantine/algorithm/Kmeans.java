package com.constantine.algorithm;

import com.constantine.model.Cluster;
import com.constantine.model.Pattern;
import com.constantine.utils.Extension;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by envy17 j110ea on 24/5/2015.
 */
public class Kmeans {
    private CCIA CCIA;
    private List<Cluster> clusters;
    private List<Pattern> patterns;


    private Properties properties;
    private Logger log = Logger.getLogger(this.getClass());

    public Kmeans(Properties properties) {
        this.properties = properties;
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

        boolean hasFinished = false;

        while (!hasFinished)
        {
            double[] initCenters = new double[CCIA.getVariablesNumber()];
            double[] finalCenters = new double[CCIA.getVariablesNumber()];

            System.out.println("\nInitial");

            for (Cluster cluster : clusters) {
                initCenters = cluster.getCenter();
                System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);
                cluster.getPatterns().clear();
            }

            for (int j = 0; j < CCIA.getVariablesNumber(); j++) {
                addPattern(j);
                kaiTwra(j);
            }

            System.out.println("\nFinal");
            for (Cluster cluster : clusters) {
                finalCenters = cluster.getCenter();
                System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);
            }


            for (int i=0; i < finalCenters.length; i++) {
                System.out.println("Init center at " + i + " variable: " + initCenters[i]);
                System.out.println("final center at " + i + " variable: " + finalCenters[i]);
                if (finalCenters[i] != initCenters[i])
                    hasFinished = false;
                else hasFinished = true;
            }
        }
    }

    public void addPattern(int i) {
        Extension extension = new Extension();


        for (Pattern pattern : patterns) {
            int index = 0;

            double dist = extension.euclideanDistance(pattern.getVariables()[i], clusters.get(0).getCenter()[i]);
            for (Cluster cluster : clusters) {
                double currentDist = extension.euclideanDistance(pattern.getVariables()[i], cluster.getCenter()[i]);
                if (currentDist < dist)
                    index = clusters.indexOf(cluster);
            }

            pattern.addLabel(index, i);
        }

    }

    public void kaiTwra(int i) {
        Extension extension = new Extension();


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
