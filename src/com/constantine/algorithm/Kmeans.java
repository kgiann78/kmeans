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
        patterns = new ArrayList<Pattern>();
//        for (Cluster cluster : clusters) {
//            System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);
//            System.out.println("TOTAL PATTERNS: " + cluster.getPatterns().size());
//        }

        for (Cluster cluster : clusters) {
//            patterns.addAll(cluster.getPatterns());
//            cluster.getPatterns().clear();
        }


        for (int i = 0; i < 10; i++) {
            for (Cluster cluster : clusters)
                System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);
            calc(0);
            kaiTwra(0);
            System.out.println("FINALLY");
            for (Cluster cluster : clusters)
                System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);

            System.out.println("\n");
        }

        System.out.println("FINALLY");
//        for (Cluster cluster : clusters) {
//            System.out.println("CLUSTER CENTER: " + cluster.getCenter()[0]);
//            System.out.println("TOTAL PATTERNS: " + cluster.getPatterns().size());
//            for (Pattern pattern : cluster.getPatterns())
//                System.out.println(pattern.getLabelString());
//        }
    }

    public void calc(int i) {
        Extension extension = new Extension();

        for (Cluster cluster1 : clusters) {
            for (Pattern pattern : cluster1.getPatterns()) {
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
    }

    public void kaiTwra(int i) {
        Extension extension = new Extension();

        for (Cluster cluster : clusters) {
            patterns.addAll(cluster.getPatterns());
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
