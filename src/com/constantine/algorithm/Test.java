package com.constantine.algorithm;

import com.constantine.utils.Extension;
import org.apache.log4j.BasicConfigurator;

import java.util.Properties;

public class Test {

    public static void main(String[] args) throws Exception {

        // read properties file:
        // define datafile file
        // define delimeter
        // define attributes (by index of the array)


        Extension extension = new Extension();
        Properties properties = extension.readPropertiesFile("init.properties");

        BasicConfigurator.configure();


        Kmeans Kmeans = new Kmeans(properties);
        Kmeans.initializeCenters();

        Kmeans.execute();


    }
}
