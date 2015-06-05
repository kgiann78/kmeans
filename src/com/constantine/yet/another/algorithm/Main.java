package com.constantine.yet.another.algorithm;

import com.constantine.algorithm.KDefinition;
import com.constantine.utils.Extension;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by constantine on 5/31/15.
 */
public class Main {
    private Logger log = Logger.getLogger(this.getClass());
    List<Pattern> patterns = new ArrayList<Pattern>();
    Cluster[] clusters;
    int size = 0;
    int k = 0;

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();
            Main main = new Main();
            Extension extension = new Extension();
            Properties properties = extension.readPropertiesFile("init.properties");
            String delimeter = properties.getProperty("delimeter").replace("\"", "");
            String file = properties.getProperty("datafile");
            String[] attributesPosition = properties.getProperty("attributes").split(",");

            int classnamePosition = Integer.parseInt(properties.getProperty("classname"));
            if (!properties.getProperty("K").isEmpty())
                main.setK(Integer.parseInt(properties.getProperty("K")));
            else
                main.setK(0);

            main.setSize(attributesPosition.length);
            main.loadPatterns(file, delimeter, attributesPosition, classnamePosition);
            main.normalize();
            main.loadClusters();
            main.initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void normalize() {
        log.info("Normalizing value...");

        double[] min = new double[size];
        double[] max = new double[size];

        for (int i = 0; i < size; i++) {
            min[i] = patterns.get(1).getValue(i);
            max[i] = patterns.get(1).getValue(i);
        }

        for (Pattern p : patterns) {
            for (int i = 0; i < size; i++) {
                if (p.getValue(i) < min[i]) min[i] = p.getValue(i);
                if (p.getValue(i) > max[i]) max[i] = p.getValue(i);
            }
        }

        for (Pattern p : patterns) {
            for (int i = 0; i < size; i++) {
                double d_new;
                if (p.getValue(i) - min[i] == 0) {

                    if (min[i] > (max[i] - min[i]))
                        d_new = min[i] / max[i];
                    else
                        d_new = min[i] / (max[i] - min[i]);
                } else
                    d_new = (p.getValue(i) - min[i]) / (max[i] - min[i]);
                p.setValue(i, d_new);
            }
        }
    }

    private void loadClusters() {
        if (k == 0) {
            k = KDefinition.ruleOfThumb(patterns.size());
        }

        clusters = new Cluster[k];
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster(i, size);
        }
    }

    private void initialize() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < k; j++) {
                clusters[j].setCenter(i, getCenters(i)[j]);
            }
        }

        log.info("Original clusters...");
        for (Cluster cluster : clusters)
            log.info(cluster);

        log.info("Running kmeans...");
        kmeans();

        log.info("initialized clusters...");
        for (Cluster cluster : clusters)
            log.info(cluster);

        System.out.println("Cluster dissimilarities:");
        for (int i = 0; i < size; i++) {
            System.out.println("Attribute " + i);
            System.out.println("Average Dissimilarity");
            System.out.println(KDefinition.averageDissimilarity(clusters[0], patterns, i));
            System.out.println("Lowest Average Dissimilarity");
            System.out.println(KDefinition.lowestAverageDissimilarity(clusters, patterns, i));
            System.out.println("Silhouette");
            System.out.println(KDefinition.silhouette(clusters, patterns, i, 0));
        }

//        List<Cluster> nclusters = createNewClusters();
//        System.out.println("Number of new clusters based on the StringPattern: " + nclusters.size());
    }

    /***
     * The kmeans algorithm that uses Patterns and Clusters <br>
     * 1. For each Pattern find the closest cluster<br>
     * 2. Add the ith value of the Pattern and find the new mean from the closest cluster's ith center<br>
     * 3. Set the new mean as the new ith center of the cluster<br>
     * 4. If the new center is equal to the old one then stop the process, else continue until everything is stabilized<br>
     */
    private void kmeans() {
        boolean hasFinished = false;
        //The structure to hold a set of the ith centers of the K clusters
        double[] center = new double[k];
        //The structure to hold all initial centers
        double[][] initcenter = new double[k][size];

        while (!hasFinished) {
            hasFinished = true;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < k; j++) {
                    initcenter[j][i] = clusters[j].getCenter(i);
                    center[j] = clusters[j].getCenter(i);
                }

                for (Pattern pattern : patterns) {
                    // find the closest cluster for the pattern and get that clusters number
                    int cluster = pattern.arrangePattern(center, i);
                    // center as double[] is reusable and so we update the new ith center of the jth cluster that was rearranged
                    center[cluster] = (center[cluster] + pattern.getValue(i)) / 2;
                    // set the rearranged cluster's center to the value of the updated center
                    clusters[cluster].setCenter(i, center[cluster]);
                }

                // examine if any of the old centers and the new centers are not equal
                // if there is even one then the whole process will be repeated
                for (int j = 0; j < k; j++) {
                    if (initcenter[j][i] != clusters[j].getCenter(i)) hasFinished = false;
                }
            }
        }
    }

    public List<Cluster> createNewClusters() {
        Map<String, Cluster> newClusters = new HashMap<String, Cluster>();
        int count = 0;
        for (Pattern pattern : patterns) {
            if (!newClusters.containsKey(Arrays.toString(pattern.getStringPattern()))) {
//                System.out.println("\nPattern: " + Arrays.toString(pattern.getAttributes()));
                Cluster cluster = new Cluster(count, pattern.getSize());
                cluster.setCenter(pattern.getAttributes());
                newClusters.put(Arrays.toString(pattern.getStringPattern()), cluster);
                count++;
            } else {
                double[] center = new double[pattern.getSize()];
                for (int i = 0; i < pattern.getSize(); i++) {
                    center[i] = (newClusters.get(Arrays.toString(pattern.getStringPattern())).getCenter()[i] + pattern.getValue(i)) / 2;
                }
                newClusters.get(Arrays.toString(pattern.getStringPattern())).setCenter(center);
            }
        }

        System.out.println("\nUnique Patterns:");
        for (Map.Entry<String, Cluster> entry : newClusters.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().getCenter()));
        }
        return new ArrayList<Cluster>(newClusters.values());
    }

    public double[] getValues(int pos) {
        double[] values = new double[patterns.size()];
        int i = 0;
        for (Pattern p : patterns) {
            values[i] = p.getValue(pos);
            i++;
        }
        return values;
    }

    public double[] getCenters(int var) {
        double[] variableValues = getValues(var);
        Extension extension = new Extension();
        double mean = extension.mean(variableValues);
        return extension.xs(extension.percentileZero(k), mean, extension.standardDeviation(variableValues, mean));
    }

    public void loadPatterns(String file, String delimeter, String[] attributesPosition, int classnamePosition) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (String line; (line = br.readLine()) != null; ) {
            String[] attributes = line.split(delimeter);
            Pattern pattern = new Pattern(size);
            for (int i = 0; i < size; i++) {
                try {
                    pattern.setValue(i, Double.parseDouble(attributes[Integer.parseInt(attributesPosition[i])]));
                } catch (java.lang.NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println(Arrays.toString(attributes));
                }
            }
            pattern.setClassname(attributes[classnamePosition]);

            patterns.add(pattern);
        }
        br.close();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }
}
