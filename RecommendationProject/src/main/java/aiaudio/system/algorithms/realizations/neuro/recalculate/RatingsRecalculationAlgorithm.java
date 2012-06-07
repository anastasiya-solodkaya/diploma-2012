package aiaudio.system.algorithms.realizations.neuro.recalculate;

import aiaudio.system.algorithms.base.Algorithm;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.Functions;

/**
 *
 * @author Anastasiya
 */
public class RatingsRecalculationAlgorithm extends Algorithm<RatingsRecalculationInputStorage, RatingsRecalculationOutputStorage> {

    @Override
    public void start() {
        int groupCount = inputStorage.groupCount();
        int artistCount = inputStorage.learningArtistsCount();
        int userCount = inputStorage.userCount();
        outputStorage.initialize(artistCount, userCount, groupCount);
        calculateGroupRatings(groupCount, artistCount);
        recalculateUsers(userCount, artistCount);
    }

    private double calculateWeightedSum(Vector artistRating, Vector groupUsersDistance, double sumWeight) {
        Vector result = artistRating.times(groupUsersDistance);
        double aggregated = result.aggregate(Functions.PLUS, Functions.IDENTITY);
        double rating = aggregated / sumWeight;
        return rating;
    }

    private void recalculateUsers(int userCount, int artistCount) {
        log("start users ratings calculation");
        for (int j = 0; j < userCount; j++) {
            int groupByUser = inputStorage.getGroupByUser(j);
            Vector groupRatings = outputStorage.getGroupRatings(groupByUser);
            outputStorage.setRecalculatedRatings(j, groupRatings);
            if (j % 1000 == 0) {
                log("processed " + j + " users");
            }
        }
        log("end uers ratings calculation");
    }

    private void calculateGroupRatings(int groupCount, int artistCount) {
        log("start groups ratings calculation");
        for (int groupId = 0; groupId < groupCount; groupId++) {
            Vector usersByGroup = inputStorage.getUsersByGroup(groupId);
            double sumWeight = usersByGroup.getNumNondefaultElements();
            if(sumWeight == 0){
                continue;
            }
            for (int artistId = 0; artistId < artistCount; artistId++) {
                Vector artistRating = inputStorage.getArtistsLearningRatings(artistId);
                double rating = calculateWeightedSum(artistRating, usersByGroup, sumWeight);
                if (Math.abs(rating) >= 0.01) {
                    outputStorage.setArtistGroupRating(artistId, groupId, rating);
                }
            }
            log("processed " + groupId + " groups");
        }
        log("end groups ratings calculation");
    }
    @Override
    protected RatingsRecalculationInputStorage createInputStorage() {
        return new RatingsRecalculationInputStorage();
    }

    @Override
    protected RatingsRecalculationOutputStorage createOutputStorage() {
        return new RatingsRecalculationOutputStorage();
    }
}
