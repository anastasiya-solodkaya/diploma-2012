package aiaudio.system.algorithms.realizations.social.aggregate;

import aiaudio.system.algorithms.base.UsersArtistsBasedAlgorithm;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.Functions;

/**
 *
 * @author Dmitry
 */
public class SocialAlgorithm extends UsersArtistsBasedAlgorithm<SocialInputStorage, SocialOutputStorage> {

    private double friendsWeight;
    private double neuroWeight;
    private double similarityWeight;
    private double similarityDistanceThreshold;

    public void setFriendsWeight(double friendsWeight) {
        this.friendsWeight = friendsWeight;
    }

    public void setNeuroWeight(double neuroWeight) {
        this.neuroWeight = neuroWeight;
    }

    public void setSimilarityWeight(double similarityWeight) {
        this.similarityWeight = similarityWeight;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityDistanceThreshold = similarityThreshold;
    }

    @Override
    public void start() {
        super.start();
        outputStorage.initialize(usersCount, artistsCount);
        calculateWeights();
        outputStorage.saveTemporary("weights.csv");
        calculateSocialRatings();
    }

    private void calculateSocialRatings() {
        log("Start social ratings recalculation");
        for (int userId = 0; userId < usersCount; userId++) {
            calculateSocialRatingsForSingleUser(userId);
            log("Social ratings calculated for " + userId + " users");
        }
        log("End social ratings recalculation");
    }

    private void calculateSocialRatingsForSingleUser(int userId) {
        Vector summaryRatings = null;
        double summaryWidth = 0;
        for (int anotherUserId = 0; anotherUserId < usersCount; anotherUserId++) {
            double weight = outputStorage.getWeight(userId, anotherUserId);
            if (userId != anotherUserId && weight != 0) {
                Vector anotherRatings = inputStorage.getVectorByUser(userId).clone();
                if (summaryRatings == null) {
                    summaryRatings = anotherRatings;
                    summaryRatings.assign(Functions.MULT, weight);
                } else {
                    anotherRatings.assign(Functions.MULT, weight);
                    summaryRatings = summaryRatings.plus(anotherRatings);
                }
            }
        }
        if (summaryRatings != null && summaryWidth != 0) {
            summaryRatings.assign(Functions.DIV, summaryWidth);
            outputStorage.setSocialCalculatedRatings(userId, summaryRatings);
        }
    }

    private void calculateWeights() {
        log("Start weight calculation");
        for (int user1 = 0; user1 < usersCount; user1++) {
            for (int user2 = user1 + 1; user2 < usersCount; user2++) {
                processUserPair(user1, user2);
            }
            log("weights processed for " + user1 + " users");
        }
        log("End weight calculation");
    }

    private void processUserPair(int user1, int user2) {
        boolean similar = checkSimilarity(user1, user2);
        boolean sameGroup = checkSameGroup(user1, user2);
        boolean friends = checkIsFriends(user1, user2);

        double weight = 0;
        if (similar) {
            weight += similarityWeight;
        }
        if (sameGroup) {
            weight += neuroWeight;
        }
        if (friends) {
            weight += friendsWeight;
        }

        outputStorage.setWeight(user1, user2, weight);
    }

    @Override
    protected SocialInputStorage createInputStorage() {
        return new SocialInputStorage();
    }

    @Override
    protected SocialOutputStorage createOutputStorage() {
        return new SocialOutputStorage();
    }

    private boolean checkSimilarity(int user1, int user2) {
        Vector user1Ratings = inputStorage.getVectorByUser(user1);
        Vector user2Ratings = inputStorage.getVectorByUser(user2);
        Double distanceSquared = user1Ratings.getDistanceSquared(user2Ratings);

        return Math.sqrt(distanceSquared) <= similarityDistanceThreshold;
    }

    private boolean checkSameGroup(int user1, int user2) {
        int group1 = inputStorage.getUserGroup(user1);
        int group2 = inputStorage.getUserGroup(user2);

        return group1 == group2;
    }

    private boolean checkIsFriends(int user1, int user2) {
        return inputStorage.isFriends(user1, user2);
    }
}
