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
            main.setK(Integer.parseInt(properties.getProperty("K")));

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
            min[i] = 1.0e10;
            max[i] = 0.0;
        }

        for (Pattern p : patterns) {
            for (int i = 0; i < size; i++) {
                if (p.getValue(i) < min[i]) min[i] = p.getValue(i);
                if (p.getValue(i) > max[i]) max[i] = p.getValue(i);
            }
        }

        for (Pattern p : patterns) {
            for (int i = 0; i < size; i++) {
                double d_new = (p.getValue(i) - min[i]) / (max[i] - min[i]);
//                System.out.println("Original value : " + p.getValue(i) + " Normalized value: " + d_new);
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

        kmeans();

        log.info("initialized clusters...");
        for (Cluster cluster : clusters)
            log.info(cluster);

        List<Cluster> nclusters = createNewClusters();
        System.out.println("Number of new clusters based on the StringPattern: " + nclusters.size());
    }

    private void kmeans() {
        int stabilized = 0;
        double[][] initialCenters = new double[size][k];
        double[] center = new double[k];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < k; j++) {
                initialCenters[i][j] = clusters[j].getCenter()[i];
            }

        while (stabilized < size) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < k; j++) {
                    for (Pattern pattern : patterns) {
                        pattern.arrangeAttribute(initialCenters[i], i);
                        if (pattern.getCluster(i) == j) {
                            clusters[j].setCenter(i, (clusters[j].getCenter()[i] + pattern.getValue(i)) / 2);
                            center[j] = (clusters[j].getCenter()[i] + pattern.getValue(i)) / 2;
                        }
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < k; j++) {
                    if (center[j] == initialCenters[i][j]) {
                        stabilized++;
                    } else
                        initialCenters[i][j] = center[j];
                }
            }
        }
    }

    public List<Cluster> createNewClusters() {
        Map<String, Cluster> newClusters = new HashMap<String, Cluster>();
        int count = 0;
        for (Pattern pattern : patterns) {
            if (!newClusters.containsKey(Arrays.toString(pattern.getStringPattern()))) {
                Cluster cluster = new Cluster(count, pattern.getSize());
                cluster.setCenter(pattern.getValues());
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
                    System.out.println(ex);
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
