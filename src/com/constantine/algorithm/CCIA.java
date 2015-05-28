package com.constantine.algorithm;

import com.constantine.model.Cluster;
import com.constantine.model.Pattern;
import com.constantine.utils.Extension;
import com.constantine.utils.Utils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


/**
 * Created by envy17 j110ea on 21/5/2015.
 */
public class CCIA {
    private ArrayList<Pattern> patterns;

    private Extension extension;
    private Utils utils;
    private Properties properties;
    private int K;
    private int variablesNumber;
    private Logger log = Logger.getLogger(this.getClass());

    private ArrayList<Cluster> clusters;

    public int getVariablesNumber() {
        return variablesNumber;
    }

    public CCIA(Properties props) {
        this.extension = new Extension();
        this.properties = props;
        this.patterns = new ArrayList<Pattern>();
        this.setK(Integer.parseInt(props.getProperty("K")));
        utils = new Utils();
    }

    public void loadPatterns() throws Exception {
        String delim = properties.getProperty("delimeter");
        String file = properties.getProperty("datafile");
        String[] variableIndeces = properties.getProperty("variables").split(",");
        int classname = Integer.parseInt(properties.getProperty("classname"));
        this.patterns.clear();

        long id = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (String line; (line = br.readLine()) != null; ) {
                patterns.add(new Pattern(line, delim, id, variableIndeces, classname));
                //	log.log(Level.INFO, "Generated Pattern " + new Pattern(line, delim, id).toString());
                id++;
            }
            br.close();
            // TODO count from variables
            this.variablesNumber = variableIndeces.length;
            log.debug("Attributes Number in data  : " + this.variablesNumber);
        } catch (Exception ex) {
            log.debug(ex.toString());
            throw new Exception("Failed to read input file with data. Reason :  " + ex.toString());
        }
    }

    public ArrayList<Cluster> initializeCenters() {
        for (int i = 0; i < this.variablesNumber; i++) {
            double[] xs = getCenters(i);
            createPartitions(xs, i);
            log.debug("K-Centers stabilized at  : " + utils.getString(xs));
        }

        //Find unique strings, K0, which is the number
        // of distinguishable clusters and K0PK.
        //       Find the center of each of these clusters
        clusters = findUniqueClusters();

        //  If  K0 > K, apply MergeDBMSDC algorithm.
        log.info("CLUSTERS SIZE " + this.getK() + " K' size " + clusters.size());

        if (clusters.size() > this.getK()) {
            log.info("K' > K.  Calculating  centers for K' clusters...");

            MergeDBMSDC mergeDBMSDCenters = new MergeDBMSDC();
            mergeDBMSDCenters.setK(this.getK());

            clusters = mergeDBMSDCenters.merge(clusters);

            getMedianCenters(clusters);
        }


        log.info("Final " + clusters.size() + " centers are :...");

        for (Cluster cluster : clusters) {
            log.info(utils.getString(cluster.getCenter()));
        }

        return clusters;
    }


    public double[] getCenters(int var) {

        double[] variableValues = utils.getValues(var, this.patterns);

        double mean = extension.mean(variableValues);
        double standardDeviation = extension.standardDeviation(variableValues, mean);
        double[] percentileZs = extension.percentileZero(this.getK());
        double[] xs = extension.xs(percentileZs, mean, standardDeviation);

        //array with lists ; each list-> partition points

        log.debug("Mean for patterns' variable 0: " + mean);
        log.debug("Deviation for patterns' variable 0: " + standardDeviation);
        log.debug("Percentiles Zero:");
        for (double p : percentileZs) log.debug(p);
        return xs;
    }

    public List<Double>[] createPartitions(double[] xs, int attr) {
        List<Double>[] partitions;
        while (true) {
            log.debug("X points for percentiles:");
            for (double x : xs) log.debug(String.valueOf(x));
            double[] new_xs = new double[this.getK()];

            partitions = extension.createPartitions(patterns, attr, xs);

            int stableCenters = 0;

            //calculate new data centers
            for (int i = 0; i < xs.length; i++) {
                new_xs[i] = extension.calculateCenter(extension.toDoubleArray(partitions[i].toArray()));
                log.debug("Cost1 " + extension.cost(extension.toDoubleArray(partitions[i].toArray()), new_xs[i]));

                log.debug("1. Old center was " + xs[i] + " ");
                log.debug("but after recalculation new center will be " + new_xs[i] + "\n");

                if (xs[i] == new_xs[i]) {
                    stableCenters++;
                }

                xs[i] = new_xs[i];
            }
            if (stableCenters == xs.length) {
                break;
            }
        }
        return partitions;
    }


    //
    //normalize input data
    public void normalize() {
        log.info("Normalizing data...");

        double[] min = new double[variablesNumber];
        double[] max = new double[variablesNumber];


        utils.get_min_max(min, max, patterns);

        for (Pattern p : patterns) {
            p.normalize(min, max);
        }
    }


    public ArrayList<Cluster> findUniqueClusters() {

        HashMap<String, Cluster> newClusters = new HashMap<String, Cluster>();
        //assign patterns to new clusters
        for (Pattern p : patterns) {
            if (newClusters.containsKey(p.getLabelString())) {
                Cluster c = newClusters.get(p.getLabelString());
                c.addPattern(p);
                newClusters.put(p.getLabelString(), c);
                //    log.info("Modifying existing cluster for label " + p.getLabelString());
            } else {
                Cluster c = new Cluster(variablesNumber);
                c.setLabel(p.getLabelString());
                c.addPattern(p);
                newClusters.put(p.getLabelString(), c);
                log.info("Added new cluster for label " + p.getLabelString());
            }
        }


        log.info("K' clusters from unique labels: " + newClusters.size() + "  while K is " + this.getK());


        for (Map.Entry<String, Cluster> c : newClusters.entrySet()) {
            log.info("Label " + c.getKey() + " occurs " + c.getValue().getMembers() + " times ");

        }


        //compute new centers for each of those clusters


        getMedianCenters(newClusters);

        ArrayList<Cluster> clusterLists = new ArrayList<Cluster>();
        clusterLists.addAll(newClusters.values());

        return clusterLists;

    }

    //TODO fill this in
    //has to compare distance between a point and its  assigned cluster center with the distance of this point from the rest of the cluster centers
//if there another cluster center is closer than the one we're currently assigned to , then this point is on a wrong cluster
    public void computeError(double[] centers, int attributesNumber) {

        for (Pattern p : patterns) {
            int[] label = p.getLabel();


           /* double assignedDist = extension.euclideanDistance(p.getAttribute(variablesNumber), Double.parseDouble(label[variablesNumber]));

            for (double center : centers) {
                if (extension.euclideanDistance(Double.parseDouble(label[variablesNumber]), center) < assignedDist) {
                    log.warn("Point " + p.getId() + " assigned to wrong cluster! : " + extension.euclideanDistance(Double.parseDouble(label[variablesNumber]), center));
                    ;
                }
            }*/


        }

    }

    //Computes the  median-based center of a new cluster
    public void getMedianCenters(HashMap<String, Cluster> newClusters) {
        for (Map.Entry<String, Cluster> c : newClusters.entrySet()) {

            double[] newCenters = new double[variablesNumber];
            for (int i = 0; i < variablesNumber; i++) {

                newCenters[i] = extension.calculateCenter(utils.getValues(i, c.getValue().getPatterns()));

                //     log.info("calculated new center for  attribute " + i + " : " + newCenters[i]);

                c.getValue().setCenter(newCenters);

                newClusters.put(c.getKey(), c.getValue());
            }
            log.info("New center for cluster : " + utils.getString(c.getValue().getCenter()));

        }


    }

    public void getMedianCenters(ArrayList<Cluster> newClusters) {
        Iterator<Cluster> iterator = newClusters.iterator();
        while (iterator.hasNext()) {
            Cluster c = iterator.next();
            double[] newCenters = new double[variablesNumber];
            for (int i = 0; i < variablesNumber; i++) {

                newCenters[i] = extension.calculateCenter(utils.getValues(i, c.getPatterns()));

                //     log.info("calculated new center for  attribute " + i + " : " + newCenters[i]);

                c.setCenter(newCenters);

            }
            log.info("New center for cluster : " + utils.getString(c.getCenter()));

        }


    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(ArrayList<Pattern> patterns) {
        this.patterns = patterns;
    }


    public Extension getExtension() {
        return extension;
    }


    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }
}
