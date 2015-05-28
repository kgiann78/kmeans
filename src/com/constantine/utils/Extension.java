package com.constantine.utils;

import com.constantine.model.Cluster;
import com.constantine.model.Pattern;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Extension {
    private Logger log = Logger.getLogger(this.getClass());

    public double mean(double[] points) {
        double m = 0.0;
        for (double point : points)
            m += point;
        return m / points.length;
    }

    public double standardDeviation(double[] points, double mean) {
        double sigma = 0;
        for (double point : points) {
            sigma += Math.pow(point - mean, 2);
        }
        sigma = Math.sqrt(sigma / points.length);

        return sigma;
    }

    public double[] percentileZero(int k) {
        double[] zs = new double[k];
        for (int i = 1; i <= k; i++) {
            zs[i - 1] = Gaussian.PhiInverse(((double) ((2 * i) - 1) / (2 * k)));
        }
        return zs;
    }

    public double[] xs(double[] percentileZero, double mean, double deviation) {
        double[] x = new double[percentileZero.length];
        for (int i = 0; i < percentileZero.length; i++) {
            x[i] = percentileZero[i] * deviation + mean;
        }
        return x;
    }

    public double euclideanDistance(double[] pointsX, double[] pointsY) {
        double sum = 0;
        for (int i = 0; i < pointsX.length; i++) {
            sum += Math.pow(pointsX[i] - pointsY[i], 2);
        }
        return Math.sqrt(sum);
    }

    public double euclideanDistance(double pointX, double pointY) {
        return Math.abs(pointX - pointY);
    }

    public double euclideanDistance(double[] pointsX) {
        double sum = 0;
        for (int i = 1; i < pointsX.length; i++) {
            sum += Math.pow(pointsX[i - 1] - pointsX[i], 2);
        }
        return Math.sqrt(sum);
    }

    public double cost(double[] points, double center) {
        double sum = 0;

        for (int j = 0; j < points.length; j++) {
            sum += euclideanDistance(points[j], center);
        }

        return sum;
    }

    public double calculateCenter(double[] points) {
        return mean(points);
    }

    public List<Double>[] createPartitions(ArrayList<Pattern> patterns, int attr, double[] xs) {
        int k = xs.length;
        List<Double>[] partitions = new List[k];

        for (int i = 0; i < k; i++)
            partitions[i] = new ArrayList<Double>();

        for (Pattern pattern : patterns) {

            double point = pattern.getAttribute(attr);
            double dist = euclideanDistance(point, xs[0]);
            pattern.addLabel(0, attr);
            int index = 0;

            for (int i = 1; i < k; i++) {
                if (euclideanDistance(point, xs[i]) < dist) {
                    dist = euclideanDistance(point, xs[i]);
                    index = i;
                }
            }

            partitions[index].add(point);
            // add label to pattern
            pattern.addLabel(index, attr);

            //   -> [1,2,3]

        }

        return partitions;
    }


    public Properties readPropertiesFile(String fileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
        }
        return properties;
    }

    public File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    public int[] toIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    public double[] toDoubleArray(Object[] objects) {
        double[] doubles = new double[objects.length];
        for (int i = 0; i < objects.length; i++) {
            doubles[i] = (Double) objects[i];
        }
        return doubles;
    }


}
