/*
package com.constantine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        // read properties file:
        // define datafile file
        // define delimeter
        // define attributes (by index of the array)


        Extension extension = new Extension();
        Properties properties = extension.readPropertiesFile("init.properties");


        String dataFilename = properties.getProperty("datafile");
        String delimeter = properties.getProperty("delimeter");
        int classnameIndex = Integer.parseInt(properties.getProperty("classname"));
        int[] variablesArray = extension.toIntArray(properties.getProperty("variables").split(","));

        // File datafile = extension.getFile(dataFilename);

        ArrayList<Pattern> patterns = new ArrayList<Pattern>();


        System.out.println("-------------------------");
        System.out.println(String.format("datafile: %s\ndelimeter: %s\nclassnameIndex: %s", dataFilename, delimeter, classnameIndex));
        System.out.print("Variables: ");

        for (int i = 0; i < variablesArray.length; i++) {
            System.out.print(variablesArray[i] + " ");
        }

        System.out.println("\n-------------------------\n");


        // read data file
        // create data set

        List<String> dataset = Files.readAllLines(Paths.get(properties.getProperty("datafile")), Charset.defaultCharset());


//        System.out.println(dataset);

        int k = 3;


        double[] variables0 = new double[dataset.size()];

//        double[] pointY = new double[dataset.size()];

        int i = 0;
        for (String line : dataset) {
            String[] vars = line.split(delimeter);
            variables0[i] = Double.parseDouble(vars[variablesArray[0]]);
//            pointY[i] = Double.parseDouble(vars[variablesArray[1]]);
            i++;
        }

//        for (double p : pointX)
//            System.out.println(p);

        double mean = extension.mean(variables0);
        double standardDeviation = extension.standardDeviation(variables0, mean);
        double[] percentileZs = extension.percentileZero(k);
        double[] xs = extension.xs(percentileZs, mean, standardDeviation);
        //array with lists ; each list-> partition points
        List<Double>[] partitions;
        System.out.println("Mean for patterns' variable 0: " + mean);
        System.out.println("Deviation for patterns' variable 0: " + standardDeviation);

        System.out.println("Percentiles Zero:");
        for (double p : percentileZs)
            System.out.println(p);


        while (true) {
            System.out.println("\nX points for percentiles:");
            for (double x : xs)
                System.out.println(x);


//            for (List<Double> p : partitions)
//                System.out.println(p);

            partitions = extension.createPartitions(variables0, xs);
            if (xs[0] == extension.calculateCenter(extension.toDoubleArray(partitions[0].toArray()))
                    && xs[1] == extension.calculateCenter(extension.toDoubleArray(partitions[1].toArray()))
                    && xs[2] == extension.calculateCenter(extension.toDoubleArray(partitions[2].toArray())))
                break;

//calculate new data centers
            System.out.println("\n\nCost1 " + extension.cost(extension.toDoubleArray(partitions[0].toArray()), xs[0]));
            System.out.print("1. Old center was " + xs[0] + " ");

            xs[0] = extension.calculateCenter(extension.toDoubleArray(partitions[0].toArray()));
            System.out.print("but after recalculation new center will be " + xs[0] + "\n");

            System.out.println("Cost2 " + extension.cost(extension.toDoubleArray(partitions[1].toArray()), xs[1]));
            System.out.print("2. Old center was " + xs[1] + " ");
            xs[1] = extension.calculateCenter(extension.toDoubleArray(partitions[1].toArray()));
            System.out.print("but after recalculation new center will be " + xs[1] + "\n");

            System.out.println("Cost3 " + extension.cost(extension.toDoubleArray(partitions[2].toArray()), xs[2]));
            System.out.print("3. Old center was " + xs[2] + " ");
            xs[2] = extension.calculateCenter(extension.toDoubleArray(partitions[2].toArray()));
            System.out.print("but after recalculation new center will be " + xs[2] + "\n");


        }
        System.out.println("\n K-Centers stabilized at  : " + xs[0] + " , " + xs[1] + "  and  " + xs[2]);

        //For each element in the dataset, chose the closest centroid.
        //Make that centroid the element 's label.

    }
}
*/
