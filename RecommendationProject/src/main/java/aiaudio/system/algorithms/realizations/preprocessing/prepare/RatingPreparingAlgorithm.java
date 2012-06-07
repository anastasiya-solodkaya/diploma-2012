package aiaudio.system.algorithms.realizations.preprocessing.prepare;

import aiaudio.system.algorithms.base.UsersArtistsBasedAlgorithm;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleFunction;
import org.apache.mahout.math.function.Functions;

/**
 *
 * @author Anastasiya
 */
public class RatingPreparingAlgorithm extends UsersArtistsBasedAlgorithm<RatingPreparingInputStorage, RatingPreparingOutputStorage> {

    private InitialData initialData;
    private Normalizaton normalizaton;

    public void setInitialData(InitialData initialData) {
        this.initialData = initialData;
    }

    public void setNormalizaton(Normalizaton normalizaton) {
        this.normalizaton = normalizaton;
    }

    @Override
    public void start() {
        super.start();
        outputStorage.initialize(artistsCount, usersCount);
        if (initialData == InitialData.INVERSED_LISTEN_COUNT) {
            inverseData();
        }

        if (normalizaton == Normalizaton.AVERAGE) {
            calculateAverage();
        }

        if (normalizaton == Normalizaton.ZSCORE) {
            calculateZScore();
        }


    }

    @Override
    protected RatingPreparingInputStorage createInputStorage() {
        return new RatingPreparingInputStorage();
    }

    @Override
    protected RatingPreparingOutputStorage createOutputStorage() {
        return new RatingPreparingOutputStorage();
    }

    private void inverseData() {
        log("Start listen count inversing");

        for (int artistId = 0; artistId < artistsCount; artistId++) {
            Vector processedRow = inputStorage.getVectorByArtist(artistId);
            double sum = processedRow.getNumNondefaultElements();
            Vector result = processedRow.assign(Functions.MULT, Math.log(sum == 0 ? 0 : usersCount / sum));
            inputStorage.setArtistRow(artistId, result);
            if (artistId % 1000 == 0) {
                log("Inversed ratings for " + artistId + " artists");
            }
        }

        log("End listen count inversing");
    }

    private void calculateDefaultByAvg() {
        log("Start default calculation (avg)");
        for (int userId = 0; userId < usersCount; userId++) {
            Vector userVector = inputStorage.getVectorByUser(userId);
            double valueByDefault = userVector.aggregate(Functions.PLUS, Functions.IDENTITY) / userVector.getNumNondefaultElements();
            inputStorage.setDefaultValue(userId, valueByDefault);
            if (userId % 1000 == 0) {
                log("Calculated default for " + userId + " users");
            }
        }
        log("End default calculation (avg)");
    }

    private void calculateDefaultByZero() {
        log("Start default calculation (zero)");
        for (int userId = 0; userId < usersCount; userId++) {
            inputStorage.setDefaultValue(userId, 0);
            if (userId % 1000 == 0) {
                log("Calculated default for " + userId + " users");
            }
        }
        log("End default calculation (zero)");
    }

    private void calculateAverage() {
        log("Start normalization calculation (avg)");
        for (int userId = 0; userId < usersCount; userId++) {
            processAverageForUser(userId);
            if (userId % 1000 == 0) {
                log("Normalized for " + userId + " users");
            }
        }
        log("End normalization calculation (avg)");
    }

    private void processAverageForUser(int userId) {
        Vector userVector = inputStorage.getVectorByUser(userId);
        double max = userVector.aggregate(Functions.MAX, Functions.IDENTITY);
        double min = userVector.aggregate(Functions.MIN, Functions.IDENTITY);
        AverageRatingCalculator calc = new AverageRatingCalculator(min, max);
        Vector newUserValue = userVector.assign(calc);
        outputStorage.setUserColumn(userId, newUserValue);
    }

    private void calculateZScore() {
        log("Start normalization calculation (z-score)");
        for (int userId = 0; userId < usersCount; userId++) {
            processZScoreForUser(userId);
            if (userId % 1000 == 0) {
                log("Normalized for " + userId + " users");
            }
        }
        log("End normalization calculation (z-score)");
    }

    private void processZScoreForUser(int userId) {
        Vector userVector = inputStorage.getVectorByUser(userId);

        double average = userVector.aggregate(Functions.PLUS, Functions.IDENTITY) / userVector.getNumNondefaultElements();

        Vector res = userVector.clone().assign(Functions.MINUS, average);
        res = res.assign(Functions.SQUARE);
        double stdDeviationSquare = res.aggregate(Functions.PLUS, Functions.IDENTITY) / userVector.getNumNondefaultElements();
        double stdDeviation = Math.sqrt(stdDeviationSquare);

        ZScoreRatingCalculator calc = new ZScoreRatingCalculator(average, stdDeviation);
        Vector newUserValue = userVector.assign(calc);

        outputStorage.setUserColumn(userId, newUserValue);

    }

    private static class AverageRatingCalculator implements DoubleFunction {

        private double min;
        private double max;

        public AverageRatingCalculator(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public double apply(double arg1) {
            if (arg1 == 0) {
                return 0;
            }
            if (max - min == 0) {
                return 1;
            }
            return (arg1 - min) / (max - min);
        }
    }

    private static class ZScoreRatingCalculator implements DoubleFunction {

        private double mean;
        private double standardDeviation;

        public ZScoreRatingCalculator(double mean, double standardDeviation) {
            this.mean = mean;
            this.standardDeviation = standardDeviation;
        }

        @Override
        public double apply(double arg1) {
            if (arg1 == 0) {
                return 0;
            }
            if (standardDeviation == 0) {
                return 0.1;
            }
            return (arg1 - mean) / standardDeviation;
        }
    }
}
