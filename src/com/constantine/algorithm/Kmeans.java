package com.constantine.algorithm;

import com.constantine.model.Cluster;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by envy17 j110ea on 24/5/2015.
 */
public class Kmeans {
    private CCIA CCIA;
    private List<Cluster> clusters;


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
        System.out.println(clusters);
    }

    public void execute() {
        log.info("Now ready to start executing k-means...");
    }
}
