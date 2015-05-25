package com.constantine.algorithm;

import com.constantine.model.Cluster;
import com.constantine.utils.Extension;
import com.constantine.utils.Utils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by envy17 j110ea on 24/5/2015.
 */
public class MergeDBMSDC {

    private Extension extension = new Extension();


    private Logger log = Logger.getLogger(this.getClass());
    private Utils utils = new Utils();
    private int K;

    public ArrayList<Cluster> merge(ArrayList<Cluster> clusters) {
        //Algorithm MergeDBMSDC

        /*
            Compute cluster center for every K' cluster
            Let B be the set of K' cluster centers
            Choose a positive integer q and initialize
            l = 1 and repeat steps 5–10 till B  is   empty /
       */


        int kCounter = 0;
        int l = 1;


        while (kCounter < K || clusters.size() > K) {
            log.info(" starting over for K = " + kCounter);
            Random rand = new Random();


            LinkedHashMap<Integer, List<Cluster>> mergedCenters = new LinkedHashMap<Integer, List<Cluster>>();


            while (!clusters.isEmpty()) {
    /*        For each cluster center xi, calculate
            the distance of the qth nearest neighbor
            of xi in B.Denote it by rq;
            xi
 */
                if (clusters.size() == 1) break;

//set q to cluster number
                int q = rand.nextInt(clusters.size() - 1) + 1;
                ///////
                q = clusters.size()-1;

                l=1;

//                if (q < 1 && clusters.size() > 0) q = 1;
                        ///////
                log.info("l is " + l);
                log.info("Cluster size " + clusters.size());
                log.info(" q " + q);

                ArrayList<Map.Entry<String, Double>> bestBuddies = new ArrayList<Map.Entry<String, Double>>();

                for (Cluster c : clusters) {
                    log.info("Cluster Label  " + c.getLabel());

                    HashMap<String, Double> buddyDistances = new HashMap<String, Double>();

//calculate distance from all buddies
                    for (Cluster buddy : clusters) {
                        if (buddy.getLabel() != c.getLabel()) {
                            buddyDistances.put(buddy.getLabel(), extension.euclideanDistance(c.getCenter(), buddy.getCenter()));
                        }
                    }
//sort buddies by nearest distance

                    buddyDistances = (HashMap<String, Double>) utils.sortByComparator(buddyDistances, true);

                    //        log.info("cluster buddies distances " + buddyDistances.entrySet());
                    //get q-th buddy
                    int i = 1;

                    for (Map.Entry<String, Double> b : buddyDistances.entrySet()) {

                        if (i == q) {
                            Map.Entry<String, Double> bestBuddy = b;

                            //  log.info("best buddy " + i + "  " + bestBuddy.getKey() + " marked  at " + bestBuddy.getValue());
                            bestBuddies.add(bestBuddy);
                            break;
                        }
                        i++;
                    }

                    //  log.info("cluster  best buddies   " + bestBuddies);

                }

            /*    Select the pount   xj in B, having the lowest
                value of buddy distance .
            */


                Map.Entry<String, Double> minBuddy = bestBuddies.get(1);


                int counter = 0;

                int minCenter = 1;

                for (Map.Entry<String, Double> buddy : bestBuddies) {
                    if (buddy.getValue() < minBuddy.getValue()) {
                        minBuddy = buddy;
                        minCenter = counter;
                    }
                    counter++;
                }

             //   log.info("min buddy among clusters:  " + minBuddy + " for  min cluster " + minCenter);
           /*create a set S and add xj to it


            Remove all points from B that lie with in
            a disc of radius 1,5rq centered at xj and
            add them to Sl.
             The set B consisting of

              The set B consisting of
            the remaining centers is to be renamed
            as B'.*/


                //Find distance of selected center from its best buddy and multiply x 1.5
                Double radius = minBuddy.getValue() * 1.5;

                double[] center = null;
                counter = 0;
                for (Cluster c : clusters) {
                    if (counter == minCenter) {
                //        log.info("Selected cluster center with minimum distance from his neighbors :" + c.getLabel());
                        //create a set S and add xj to it
                        List<Cluster> list = new ArrayList<Cluster>();

                        // list.add(c);
                        mergedCenters.put(l, list);

                        center = c.getCenter();

                    }
                    counter++;
                }

          /*  Remove all points from B that lie with in
            a disc of radius 1,5rq centered at xj and
            add them to Sl.
           */

                log.info(" \n");
                Iterator it = clusters.iterator();
                log.info("Calculating distance from selected cluster to other centers... :");
                while (it.hasNext()) {
                    Cluster c = (Cluster) it.next();

                    //create a set S and add xj to it

                    //   log.info("  distance  for center [" + utils.getString(c.getCenter()) + "] ... ");
                    log.info(" Distance is :   " + extension.euclideanDistance(center, c.getCenter()));
                    if (extension.euclideanDistance(center, c.getCenter()) < radius) {

                     //   log.info("Distance    smaller than radius " + radius);
//dont add the Center cluster again
                        if (extension.euclideanDistance(center, c.getCenter()) != 0) {
                          //  log.info(" Adding to merged centers ");
                            List<Cluster> list = mergedCenters.get(l);
                            list.add(c);

                            mergedCenters.put(l, list);
                        }
                        // log.info("Removing from initial cluster set... ");

                        it.remove();
                    }


                }


                log.info(" \n");

                log.info(" Cluster list for next iteration: " + clusters.size());

                for (Cluster c : clusters) {

                    log.info(utils.getString(c.getCenter()));

                    if (clusters.size() == 1) {
                        List<Cluster> list = mergedCenters.get(l);

                        list.add(c);
                        mergedCenters.put(l, list);

                    }

                }


                //if only one center left, add it to merged centers and break
                l++;
            }
            // log.info(" \n Final Merged Centers ");



            for (int o = 1; o < l; o++) {
                for (Cluster mc : mergedCenters.get(o)) {

                    log.info(utils.getString(mc.getCenter()));

                }
            }


            // clusters.clear();
            ArrayList<Cluster> newClusters = new ArrayList<Cluster>();

            for (List<Cluster> v : mergedCenters.values()) {


                Iterator it3 = v.iterator();
                while (it3.hasNext()) {

                    newClusters.add((Cluster) it3.next());

                }

            }

            clusters = newClusters;
            kCounter++;


        }


        return clusters;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Utils getUtils() {
        return utils;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }
}
