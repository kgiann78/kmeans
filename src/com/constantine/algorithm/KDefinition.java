package com.constantine.algorithm;

import com.constantine.yet.another.algorithm.Cluster;
import com.constantine.yet.another.algorithm.Pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by constantine on 5/29/15.
 */
public class KDefinition {
    public static int ruleOfThumb(int dataPoints) {
        if (dataPoints > 0)
            return (int) Math.sqrt((double) dataPoints / 2);
        return 0;
    }

    /***
     * For each datum i, let a(i) be the average dissimilarity of i with all other data within the same cluster.<br>
     * We can interpret a(i) as how well i is assigned to its cluster (the smaller the value, the better the assignment).<br>
     * We then define the average dissimilarity of point i to a cluster c as the average of the distance from i to points in c.
     *
     * @param cluster
     * @param patterns
     * @param pos
     * @return
     */
    public static double averageDissimilarity(Cluster cluster, List<Pattern> patterns, int pos) {
        double avg = 0.0;
        int count = 0;

        for (Pattern pattern : patterns) {
            if (pattern.getCluster(pos) == cluster.getLabel()) {
                avg += Math.abs(pattern.getValue(pos) - cluster.getCenter(pos));
                count++;
            }
        }

        return avg / count;
    }

    /***
     * Let b(i) be the lowest average dissimilarity of i to any other cluster, of which i is not a member.<br>
     * The cluster with this lowest average dissimilarity is said to be the "neighbouring cluster" of i because<br>
     * it is the next best fit cluster for point i.
     *
     * @param clusters
     * @param patterns
     * @param pos
     * @return
     */
    public static double lowestAverageDissimilarity(Cluster[] clusters, List<Pattern> patterns, int pos) {
        double[] avg = new double[clusters.length];

        int count = 0;

        for (Pattern pattern : patterns) {
            for (int j = 0; j < clusters.length; j++) {
                if (pattern.getCluster(pos) != j) {
                    avg[j] += Math.abs(pattern.getValue(pos) - clusters[j].getCenter(pos));
                    count++;
                }
            }
        }

        avg[0] /= count;
        double neighbor = avg[0];
        for (int j = 1; j < clusters.length; j++) {
            avg[j] /= count;
            neighbor = Math.min(avg[j - 1], avg[j]);
        }

        return neighbor;
    }

    /***
     * We now define a silhouette:<br><br>
     * s(i) = \frac{b(i) - a(i)}{\max\{a(i),b(i)\}}<br><br>
     * From the above definition it is clear that<br>
     * -1 \le s(i) \le 1
     *
     * @param clusters
     * @param patterns
     * @param pos
     * @param k
     * @return
     */
    public static double silhouette(Cluster[] clusters, List<Pattern> patterns, int pos, int k) {
        return (lowestAverageDissimilarity(clusters, patterns, pos) - averageDissimilarity(clusters[k], patterns, pos)) / Math.max(averageDissimilarity(clusters[k], patterns, pos), lowestAverageDissimilarity(clusters, patterns, pos));
    }

    /***
     * For a region to be identified as a cluster, it is important to analyse not only its internal distribution
     * but also its interdependence with other object groupings in the data set. <br><br>
     * <p/>
     * In K-means clustering, the distortion of a cluster is a function of the data population and the distance
     * between objects and the cluster centre according to <br><br>
     * I_j = Sum_{t=1}^{N_j}[d(x_{jt}, w_j)]^2<br><br>
     * where Ij is the distortion of cluster j,<br>
     * w_j is the centre of cluster j, <br>
     * N_j is the number of objects belonging to cluster j,<br>
     * x_{jt} is the tth object belonging to cluster j and<br>
     * d(x_{jt}, w_j) is the distance between object x_{jt} and the centre w_j of cluster j.<br><br>
     * <p/>
     * Each cluster is represented by its distortion and its impact on the entire data set is assessed by
     * its contribution to the sum of all distortions, SK, given by<br><br>
     * S_k = Sum_{j=1}^K(I_j)<br><br>
     * where K is the specified number of clusters.<br><br>
     * <p/>
     * Thus, such information is important in assessing whether a particular region in the object space
     * could be considered a cluster.<br>
     *
     * @param cluster  the cluster in question
     * @param patterns the list of patterns
     * @param pos      the ith position of the attribute examined
     * @return returns the number of cluster's distortion
     */
    public static double distortionOfCluster(Cluster cluster, List<Pattern> patterns, int pos) {
        double sum = 0.0;
        for (Pattern pattern : patterns) {
            if (pattern.getCluster(pos) == cluster.getLabel()) {
                sum += Math.pow(cluster.getCenter(pos) - pattern.getValue(pos), 2);
            }
        }
        return sum;
    }

    public static double sumOfDistortions(Cluster[] clusters, List<Pattern> patterns, int k, int pos) {
        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            sum += distortionOfCluster(clusters[i], patterns, pos);
        }
        return sum;
    }

    public static double evaluationFunction(Cluster[] clusters, List<Pattern> patterns, int k, int pos) {
        int n = patterns.get(0).getSize();
        if (k == 1) {
            return 1.0;
        } else if (k > 1 && sumOfDistortions(clusters, patterns, k - 1, pos) != 0) {
            System.out.println("Sk/Sk-1: " + sumOfDistortions(clusters, patterns, k, pos) / (weightFactor(k, n) * sumOfDistortions(clusters, patterns, k - 1, pos)));
            return sumOfDistortions(clusters, patterns, k, pos) / (weightFactor(k, n) * sumOfDistortions(clusters, patterns, k - 1, pos));
        } else if (k > 1 && sumOfDistortions(clusters, patterns, k - 1, pos) == 0) {
            return 1.0;
        }
        return 0.0;
    }

    public static double weightFactor(int k, int n) {
        if (k == 2 && n > 1) {
            return 1 - (3 / (4 * n));
        } else if (k > 2 && n > 1) {
            return weightFactor(k - 1, n) + ((1 - weightFactor(k - 1, n)) / 6);
        }
        return 0;
    }

}
